import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Node root;
  private Node externs;
  private InlineObjectLiterals inlineObjectLiterals;
  private Behavior behavior;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    externs = Node.objectLiteral();
    root = Node.script(Lists.newArrayList(externs));
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "test_id");
    behavior = inlineObjectLiterals.new InliningBehavior();
  }

  @Test
  public void testInlineSimpleObject() {
    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getLastChild().getScope();
    Var var = scope.getVar("test_object");
    ReferenceCollection referenceInfo = new ReferenceCollection(var, Lists.newArrayList(
        new Reference(var, IR.name("test_object"), null, null, null, null, null, null, null, null, null)));
    assertEquals(1, referenceInfo.references.size());

    Map<String, String> varmap = inlineObjectLiterals.new InliningBehavior().computeVarList(var, referenceInfo);
    assertEquals(2, varmap.size());
    assertEquals("test_key1", varmap.get("test_key1"));
    assertEquals("test_key2", varmap.get("test_key2"));

    Node vnode = var.getScope().getRootNode().getLastChild().getFirstChild();
    assertEquals(2, vnode.getNumChildren());

    Node test_key1 = vnode.getChildAtIndex(0);
    Node test_key2 = vnode.getChildAtIndex(1);

    assertEquals("test_key1", test_key1.getString());
    assertEquals("test_key2", test_key2.getString());
  }

  @Test
  public void testInlineNestedObject() {
    Node test_object = Node.objectLiteral(
        Node.property("test_key1", Node.objectLiteral(
            Node.property("test_key2", Node.numberLiteral(1)))));

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getLastChild().getScope();
    Var var = scope.getVar("test_object");
    ReferenceCollection referenceInfo = new ReferenceCollection(var, Lists.newArrayList(
        new Reference(var, IR.name("test_object"), null, null, null, null, null, null, null, null, null)));
    assertEquals(1, referenceInfo.references.size());

    Map<String, String> varmap = inlineObjectLiterals.new InliningBehavior().computeVarList(var, referenceInfo);
    assertEquals(4, varmap.size());
    assertEquals("test_key1", varmap.get("test_key1"));
    assertEquals("test_key1_test_key2", varmap.get("test_key1_test_key2"));
    assertEquals("test_key2", varmap.get("test_key2"));
    assertEquals("test_key2_value", varmap.get("test_key2_value"));

    Node vnode = var.getScope().getRootNode().getLastChild().getFirstChild();
    assertEquals(4, vnode.getNumChildren());

    Node test_key1 = vnode.getChildAtIndex(0);
    Node test_key1_test_key2 = test_key1.getChildAtIndex(0);
    Node test_key2 = test_key1.getChildAtIndex(1);
    Node test_key2_value = test_key2.getFirstChild();

    assertEquals("test_key1", test_key1.getString());
    assertEquals("test_key1_test_key2", test_key1_test_key2.getString());
    assertEquals("test_key2", test_key2.getString());
    assertEquals("test_key2_value", test_key2_value.getString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testErrorWhenObjectHasSelfReference() {
    Node selfRefObject = Node.objectLiteral(
        Node.property("a", Node.objectLiteral(
            Node.property("b", Node.name("this")))));

    inlineObjectLiterals.process(externs, root);
  }

  @Test
  public void testInlineObjectWithGetter() {
    Node test_object = Node.objectLiteral(
        Node.property("test_key1", Node.numberLiteral(1)),
        Node.getter("getTestKey2", Node.objectLiteral(
            Node.property("test_key3", Node.numberLiteral(2)))));

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getLastChild().getScope();
    Var var = scope.getVar("test_object");
    ReferenceCollection referenceInfo = new ReferenceCollection(var, Lists.newArrayList(
        new Reference(var, IR.name("test_object"), null, null, null, null, null, null, null, null, null)));
    assertEquals(1, referenceInfo.references.size());

    Map<String, String> varmap = inlineObjectLiterals.new InliningBehavior().computeVarList(var, referenceInfo);
    assertEquals(5, varmap.size());
    assertEquals("test_object", varmap.get("test_object"));
    assertEquals("test_object_test_key1", varmap.get("test_object_test_key1"));
    assertEquals("test_object_getTestKey2", varmap.get("test_object_getTestKey2"));
    assertEquals("test_object_getTestKey2_test_key3", varmap.get("test_object_getTestKey2_test_key3"));
    assertEquals("test_object_getTestKey2_test_key3_test_key3", varmap.get("test_object_getTestKey2_test_key3_test_key3"));

    Node vnode = var.getScope().getRootNode().getLastChild().getFirstChild();
    assertEquals(5, vnode.getNumChildren());

    Node test_object_test_key1 = vnode.getChildAtIndex(0);
    Node test_object = vnode.getChildAtIndex(1);
    Node test_object_getTestKey2 = test_object.getChildAtIndex(0);
    Node test_object_getTestKey2_test_key3 = test_object_getTestKey2.getFirstChild();
    Node test_object_getTestKey2_test_key3_test_key3 = test_object_getTestKey2_test_key3.getFirstChild();

    assertEquals("test_object", test_object.getString());
    assertEquals("test_object_getTestKey2", test_object_getTestKey2.getString());
    assertEquals("test_object_getTestKey2_test_key3", test_object_getTestKey2_test_key3.getString());
    assertEquals("test_object_getTestKey2_test_key3_test_key3", test_object_getTestKey2_test_key3_test_key3.getString());

    assertEquals(2, test_object_test_key1.getNumChildren());
    assertEquals("test_object_test_key1", test_object_test_key1.getString());
    assertEquals(1, test_object_test_key1.getFirstChild().getNumChildren());
    assertEquals("test_object_test_key1_test_key1", test_object_test_key1.getFirstChild().getString());
    assertEquals(1, test_object_test_key1.getFirstChild().getFirstChild().getNumChildren());
    assertEquals("test_object_test_key1_test_key1_value", test_object_test_key1.getFirstChild().getFirstChild().getFirstChild().getString());
  }
}