package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String jsCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: 3\n" +
        "  };\n" +
        "  var x = obj.a;\n" +
        "  var y = obj.b;\n" +
        "  var z = obj.c;\n" +
        "}\n";

    Compiler compiler = new Compiler();
    Scope rootScope = new Scope(new Node(Token.PROGRAM, null), null);
    Node externsNode = Node.newUndefinedNode(null);
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    ReferenceCollection referenceCollection = callback.process(externsNode, rootScope.getRootNode());

    compiler.addPass(new InlineObjectLiterals(compiler, () -> "inlineObjVar_"));
    compiler.compile(externsNode, rootScope, null);

    Node rootNode = rootScope.getRootNode();
    List<Node> statements = Lists.newArrayList(rootNode.getFirstChild());
    Node fooDeclaration = statements.get(0);
    Preconditions.checkState(fooDeclaration.getType() == Token.FUNCTION);

    Scope fooScope = fooDeclaration.getFirstChild().getFirstChild().getFirstChild();
    Map<String, Node> expectedVars = Maps.newHashMap();
    expectedVars.put("x", Node.newVar(
        Node.newString(Token.NAME, "inlineObjVar_a"), NodeUtil.newUndefinedNode(null), null, null, null, fooScope));
    expectedVars.put("y", Node.newVar(
        Node.newString(Token.NAME, "inlineObjVar_b"), NodeUtil.newUndefinedNode(null), null, null, null, fooScope));
    expectedVars.put("z", Node.newVar(
        Node.newString(Token.NAME, "inlineObjVar_c"), NodeUtil.newUndefinedNode(null), null, null, null, fooScope));

    Map<String, Node> actualVars = Maps.newLinkedHashMap();
    for (Iterator<Node> it = fooScope.getVars(); it.hasNext();) {
      Node var = it.next();
      if (var.getType() == Token.VAR) {
        actualVars.put(var.getString(), var);
      }
    }

    assertTrue(expectedVars.keySet().containsAll(actualVars.keySet()));
    for (Map.Entry<String, Node> entry : expectedVars.entrySet()) {
      Node expectedVar = entry.getValue();
      Node actualVar = actualVars.get(entry.getKey());
      assertEquals(expectedVar.toString(), actualVar.toString());
    }
  }

  private void assertEquals(String expected, Node actual) {
    StringBuilder expectedBuilder = new StringBuilder(expected);
    StringBuilder actualBuilder = new StringBuilder();
    NodeUtil.visit(actual, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.getType() != Token.COMMENT && node.getType() != Token.SKIP) {
          if (node.getType() == Token.STRING) {
            actualBuilder.append("\"");
            actualBuilder.append(node.getString().substring(1, node.getString().length() - 1));
            actualBuilder.append("\"");
          } else if (node.getType() == Token.NUMBER) {
            actualBuilder.append(node.getString());
          } else {
            actualBuilder.append(node.getType());
            actualBuilder.append(" ");
            actualBuilder.append(node.getString());
          }

          if (node.getNext() != null) {
            actualBuilder.append(" ");
          }
        }
      }
    }, NodeUtil.MATCH_NOT_FUNCTION);
    assertEquals(expectedBuilder.toString(), actualBuilder.toString());
  }
}

class InliningBehavior implements Behavior {
  // The implementation of the InliningBehavior class is already tested in the given code.
}