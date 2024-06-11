import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testShouldTraverseFunctions() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("functionName", null, null);

    when(functionNode.getParent()).thenReturn(Node.newBlock("blockNode"));
    when(traversal.getCurrentNode()).thenReturn(Node.newBlock("currentNode"));

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseConstructors() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    Node constructorNode = Node.newFunction("constructorName", "(args)", null, "constructor");

    assertTrue(!checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), constructorNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionsWithThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    Node functionNode = Node.newFunction("functionName", null, null);
    JSDocInfo jsDoc = Node.newJsDocInfo("@this", null);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertTrue(!checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionsWithOverrideAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    Node functionNode = Node.newFunction("functionName", null, null);
    JSDocInfo jsDoc = Node.newJsDocInfo("@override", null);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertTrue(!checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionsThatAreNestedTooDeeply() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    Node functionNode = Node.newFunction("functionName", null, null);
    Node parent = Node.newBlock("parentBlock");
    Node grandParent = Node.newBlock("grandParentBlock");

    when(functionNode.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(grandParent);

    assertTrue(!checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, null));
  }

  @org.junit.Test
  public void testShouldTraverseLeftSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    Node assignmentNode = Node.newAssign(Node.newName("a"), Node.newThis());

    assertTrue(checkGlobalThis.shouldReportThis(assignmentNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseRightSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    Node assignmentNode = Node.newAssign(Node.newName("a"), Node.newName("this"));

    assertTrue(!checkGlobalThis.shouldReportThis(assignmentNode, null));
  }

  @org.junit.Test
  public void testShouldTraverseLeftSideOfPropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    Node propertyAccessNode = Node.newGetProp(Node.newName("a"), "prototype");

    assertTrue(checkGlobalThis.shouldReportThis(propertyAccessNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseLeftSideOfNestedPropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    Node propertyAccessNode = Node.newGetProp(Node.newGetProp(Node.newName("a"), "prototype"), "prototype");

    assertTrue(checkGlobalThis.shouldReportThis(propertyAccessNode, null));
  }
}