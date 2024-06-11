package org.apache.commons.cli.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if (InlineObjectLiterals.class.getName().equals(passName)) {
          return new InlineObjectLiterals(this, () -> "tempVar_") {
            @Override
            public String getName() {
              return "InlineObjectLiteralsTestPass";
            }
          };
        }
        return null;
      }
    };

    // Sample input code
    String inputCode = "function foo() {" +
        "  var object = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: {d: 3}" +
        "  };" +
        "  bar(object);" +
        "}";

    Node root = AbstractCompilerTest.compile(compiler, inputCode);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, new InliningBehavior());
    callback.process(null, root);

    Scope scope = root.getFirstChild().getFirstChild().getLastChild();

    // Check if variables were inlined correctly
    for (Iterator<Scope.Var> it = scope.getVars().iterator(); it.hasNext();) {
      Scope.Var var = it.next();
      String varName = var.name;
      if (varName.startsWith(InlineObjectLiterals.VAR_PREFIX)) {
        ReferenceCollection referenceInfo = callback.getReferenceMap().get(var);
        List<Reference> references = referenceInfo.references;

        assertEquals(1, references.size());
        Reference initReference = references.get(0);
        Preconditions.checkState(initReference.isInitializingDeclaration());
        Node initNode = initReference.getNode();
        Preconditions.checkState(initNode.isAssign());

        Node assignee = initNode.getFirstChild();
        Preconditions.checkState(assignee.isGetProp());
        String objectName = assignee.getString();
        String expectedVarName = varName.substring(InlineObjectLiterals.VAR_PREFIX.length());

        // Check if corresponding object key was inlined correctly
        for (int i = 1; i < initNode.getNumChildren(); i++) {
          Node child = initNode.getChildAtIndex(i);
          Preconditions.checkState(child.isAssign());
          Node lhs = child.getFirstChild();
          String keyName = lhs.getString();
          Preconditions.checkState(keyName.equals(expectedVarName));
          Node rhs = child.getNext();
          Preconditions.checkState(rhs.isNumberLiteral());
          int expectedValue = 0;
          if (expectedVarName.equals("c")) {
            expectedValue = 3;
          } else {
            expectedValue = Integer.parseInt(keyName);
          }
          assertEquals(expectedValue, rhs.getInt());
        }
      }
    }

    // Check if 'bar' function reference was updated correctly
    Node functionCall = root.getFirstChild().getFirstChild();
    Preconditions.checkState(functionCall.isCall());
    Reference functionReference = callback.getReferenceMap().get("bar");
    Preconditions.checkState(functionReference != null);
    Node functionNode = functionReference.getNode();
    Preconditions.checkState(functionNode.isGetProp());
    String functionName = functionNode.getString();
    String expectedFunctionName = "bar_";
    assertEquals(expectedFunctionName, functionName);
  }
}

class InliningBehavior extends ReferenceCollectingCallback.Behavior {
  // Copy the existing InliningBehavior implementation here
  // (only the parts not related to staleVars and blacklistVarReferencesInTree)

  // Add a new method to check if a node is an ObjectLiteral
  public static boolean isObjectLiteral(Node node) {
    return node.isObjectLit();
  }

  // Override isInlinableObject to use isObjectLiteral instead of isObjectLit
  public boolean isInlinableObject(List<Reference> refs) {
    for (Reference ref : refs) {
      Node node = ref.getNode();
      if (node.isAssign() && node.getFirstChild() != null && isObjectLiteral(node.getFirstChild())) {
        return true;
      }
    }
    return false;
  }

  // Override computeVarList to handle object literals
  public Map<String, Node> computeVarList(Var var, ReferenceCollection referenceInfo) {
    Map<String, Node> varmap = super.computeVarList(var, referenceInfo);

    if (isObjectLiteral(var.initialValue)) {
      Node obj = var.initialValue;
      for (Node child = obj.getFirstChild(); child != null; child = child.getNext()) {
        String varname = child.getString();
        String var = VAR_PREFIX + varname + "_" + safeNameIdSupplier.get();
        varmap.put(varname, child);
      }
    }

    return varmap;
  }

  // Override replaceAssignmentExpression to handle object literals
  public void replaceAssignmentExpression(Var v, Reference ref,
                                          Map<String, String> varmap) {
    if (isObjectLiteral(ref.getNode().getFirstChild())) {
      List<Node> nodes = Lists.newArrayList();
      Node obj = ref.getNode().getFirstChild();
      for (Node key = obj.getFirstChild(); key != null; key = key.getNext()) {
        String var = key.getString();
        String varName = varmap.get(var);
        nodes.add(IR.assign(IR.name(varName), key.removeFirstChild()));
      }
      Node replacement = new Node(Token.COMMA);
      Node cur = replacement;
      int i;
      for (i = 0; i < nodes.size() - 2; i++) {
        cur.addChildToFront(nodes.get(i));
        Node t = new Node(Token.COMMA);
        cur.addChildToFront(t);
        cur = t;
      }
      cur.addChildToFront(nodes.get(i));
      cur.addChildToFront(nodes.get(i + 1));

      if (ref.getParent() instanceof Var) {
        ((Var) ref.getParent()).initialValue = IR.name(varmap.get(var.name));
      }

      ref.getParent().replaceChild(ref, replacement);
    } else {
      super.replaceAssignmentExpression(v, ref, varmap);
    }
  }
}