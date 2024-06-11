import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private static final String VAR_PREFIX = "JSCompiler_object_inline_";

  @SuppressWarnings("unchecked")
  private static void initMocks(AbstractCompiler compiler) {
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    when(safeNameIdSupplier.get()).thenReturn("_mock_id");
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    when(compiler.getPassManager().getPasses()).thenReturn(
        Lists.newArrayList(pass));
  }

  @SuppressWarnings("unchecked")
  private static void initNodes(Node root) {
    Node externs = Node.newObject(Token.NEW);
    Node objLit = Node.newObjectLit();
    Node prop1 = Node.newName("p1");
    Node val1 = Node.newNumber(1);
    Node prop2 = Node.newName("p2");
    Node val2 = Node.newString("s1");
    objLit.addChildToBack(prop1);
    objLit.addChildToBack(val1);
    objLit.addChildToBack(prop2);
    objLit.addChildToBack(val2);
    root.addChildToBack(externs);
    root.addChildToBack(objLit);
  }

  @SuppressWarnings("unchecked")
  private static void initScopeAndVar(Scope scope, Node root) {
    List<Var> vars = Lists.newArrayList();
    Scope parent = new Scope(root, null, null);
    scope = new Scope(root, parent, vars);
    Var var = new Var(Token.VAR, "obj", 0, null, null, null, null, null);
    vars.add(var);
  }

  @SuppressWarnings("unchecked")
  private static void initReferences(ReferenceCollection referenceInfo,
                                     List<Reference> refs, int offset) {
    for (int i = 0; i < refs.size(); i++) {
      Reference nodeRef = refs.get(i);
      nodeRef.setIndex(i + offset);
      referenceInfo.addReference(nodeRef);
    }
  }

  @SuppressWarnings("unchecked")
  private static void initAssignments(Node root, Map<String, Node> initvals) {
    for (Map.Entry<String, Node> entry : initvals.entrySet()) {
      String key = entry.getKey();
      Node keyNode = Node.newName(key);
      Node valNode = entry.getValue();
      Node assign = new Node(Token.ASSIGN, keyNode, valNode);
      root.addChildToBack(assign);
    }
  }

  @SuppressWarnings("unchecked")
  private static void initObjects(Node root, Map<String, Node> initvals,
                                Map<String, String> varmap) {
    for (Map.Entry<String, Node> entry : initvals.entrySet()) {
      String key = entry.getKey();
      Node keyNode = Node.newName(key);
      Node prop = new Node(Token.GETPROP, root, keyNode);
      Node val = entry.getValue();
      if (varmap.containsKey(key)) {
        val = Node.newVar(Token.GETPROP, val, 0, null, null, null, null,
            NodeUtil.newNameNode(varmap.get(key)));
      }
      prop.addChildToBack(val);
      root.addChildToBack(prop);
    }
  }

  @org.junit.Test
  public void testInlineObjectLiterals() {
    // Setup
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node root = Node.newBlock();
    initMocks(compiler);
    initNodes(root);
    Scope scope = null;
    initScopeAndVar(scope, root);

    // Create the reference collecting callback
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, null);

    // Process the root node
    callback.process(Node.newScope(Token.NEW, null, null), root);

    // Extract the InliningBehavior and the ReferenceCollection
    InliningBehavior inliningBehavior = (InliningBehavior) callback.getBehavior();
    ReferenceCollection referenceInfo = callback.getReferenceMap();

    // Check that we collected the expected references
    List<Reference> refs = Lists.newArrayList();
    for (Iterator<Reference> it = referenceInfo.iterator(); it.hasNext();) {
      refs.add(it.next());
    }
    assertEquals(4, refs.size());
    assertEquals("obj", refs.get(0).getName());
    assertEquals(Token.ASSIGN, refs.get(0).getNodeType());
    assertEquals(0, refs.get(0).getIndex());
    assertEquals("p1", refs.get(1).getName());
    assertEquals(Token.GETPROP, refs.get(1).getNodeType());
    assertEquals(1, refs.get(1).getIndex());
    assertEquals("p2", refs.get(2).getName());
    assertEquals(Token.GETPROP, refs.get(2).getNodeType());
    assertEquals(2, refs.get(2).getIndex());
    assertEquals("obj", refs.get(3).getName());
    assertEquals(Token.ASSIGN, refs.get(3).getNodeType());
    assertEquals(3, refs.get(3).getIndex());

    // Check the staleVars set
    Set<Var> staleVars = inliningBehavior.staleVars;
    assertEquals(1, staleVars.size());
    Var var = staleVars.iterator().next();
    assertEquals("obj", var.getName());
    assertEquals(Token.VAR, var.getNodeType());
    assertEquals(0, var.getIndex());

    // Check the inliningBehavior state
    Map<String, Node> initvals = Maps.newHashMap();
    initvals.put("p1", Node.newNumber(1));
    initvals.put("p2", Node.newString("s1"));
    initAssignments(root, initvals, null);

    // Perform the inlineObjectLiterals pass
    CompilerPass pass = callback.getPassManager().getPasses().get(0);
    pass.process(Node.newScope(Token.NEW, null, null), root);

    // Check the result
    Node result = root.getFirstChild();
    assertEquals(Node.NEW_EXPR, result.getNodeType());
    assertEquals("Object", result.getClass().getSimpleName());
    Node expression = result.getFirstChild();
    assertEquals(Token.LEFT_CURLY_BRACE, expression.getNodeType());

    Node p1 = null;
    Node p2 = null;
    for (Node child = expression.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isName() && "p1".equals(child.getString())) {
        p1 = child;
      }
      if (child.isName() && "p2".equals(child.getString())) {
        p2 = child;
      }
      if (child.getNodeType() == Token.RIGHT_CURLY_BRACE) {
        break;
      }
    }

    assertEquals(null, p1);
    assertEquals(null, p2);

    if (p1 != null && p2 != null) {
      initObjects(root, initvals, null);
      Node obj = expression;
      assertEquals(p1, obj.getFirstChild());
      assertEquals(Node.newNumber(1), p1.getFirstChild());
      assertEquals(p2, obj.getChildren().get(1));
      assertEquals(Node.newString("s1"), p2.getFirstChild());
    }

    // Check the scope
    Scope finalScope = root.getParent();
    List<Var> finalVars = finalScope.getVars();
    assertEquals(2, finalVars.size());
    Var objVar = finalVars.get(0);
    assertEquals("obj", objVar.getName());
    assertEquals(Token.VAR, objVar.getNodeType());
    assertEquals(0, objVar.getIndex());

    Var p1Var = finalVars.get(1);
    assertEquals("p1", p1Var.getName());
    assertEquals(Token.VAR, p1Var.getNodeType());
    assertEquals(1, p1Var.getIndex());

    Var p2Var = finalVars.get(2);
    assertEquals("p2", p2Var.getName());
    assertEquals(Token.VAR, p2Var.getNodeType());
    assertEquals(2, p2Var.getIndex());

    // Verify that the object literal was inlined
    Node object = objVar.getInitializingValue();
    assertEquals(Node.newObjectLit(), object);
    List<Node> children = object.getChildren();

    assertEquals(2, children.size());

    Node property1 = children.get(0);
    assertEquals(Node.newName("p1"), property1);

    Node propertyValue1 = property1.getNext();
    assertEquals(Node.newNumber(1), propertyValue1);

    Node property2 = children.get(1);
    assertEquals(Node.newName("p2"), property2);

    Node propertyValue2 = property2.getNext();
    assertEquals(Node.newString("s1"), propertyValue2);

    // Verify that the object literal was inlined and that the
    // original object literal references were replaced
    assertEquals(3, referenceInfo.size());

    Reference objRef = referenceInfo.getReference(0);
    assertEquals("obj", objRef.getName());
    assertEquals(Token.VAR, objRef.getNodeType());
    assertEquals(0, objRef.getIndex());
    assertEquals(1, objRef.getGrandparent().getIndex());

    Reference p1Ref = referenceInfo.getReference(1);
    assertEquals("p1", p1Ref.getName());
    assertEquals(Token.GETPROP, p1Ref.getNodeType());
    assertEquals(1, p1Ref.getIndex());
    assertEquals(1, p1Ref.getParent().getIndex());
    assertEquals(objRef.getParent(), p1Ref.getGrandparent());

    Reference p2Ref = referenceInfo.getReference(2);
    assertEquals("p2", p2Ref.getName());
    assertEquals(Token.GETPROP, p2Ref.getNodeType());
    assertEquals(2, p2Ref.getIndex());
    assertEquals(1, p2Ref.getParent().getIndex());
    assertEquals(objRef.getParent(), p2Ref.getGrandparent());
  }
}