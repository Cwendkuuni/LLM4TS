import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeTraversal.Callback;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;
  private Node node;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
    node = Node.newIntLiteral(1);
  }

  @Test
  public void testShouldTraverseNotFunction() {
    Node parent = Node.newName("parent");
    Node n = Node.newIdentifier("this");

    when(traversal.getCurrentNode()).thenReturn(n);
    when(traversal.getParentNode()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(traversal, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionNoJSDoc() {
    Node functionNode = Node.newFunction("functionName", new Node[0], new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(functionNode);
    when(functionNode.getParent()).thenReturn(Node.newBlock(new Node[0]));

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithJSDocNoAnnotation() {
    Node functionNode = Node.newFunction("functionName", new Node[0], new Node[0]);
    JSDocInfo jsDocInfo = Node.newJSDocInfo("JSDocInfo");
    functionNode.setJSDocInfo(jsDocInfo);

    when(traversal.getCurrentNode()).thenReturn(functionNode);
    when(functionNode.getParent()).thenReturn(Node.newBlock(new Node[0]));

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    Node functionNode = Node.newFunction("constructorName", new Node[0], new Node[0]);
    JSDocInfo jsDocInfo = Node.newJSDocInfo("JSDocInfo");
    functionNode.setJSDocInfo(jsDocInfo);
    jsDocInfo.setIsConstructor(true);

    when(traversal.getCurrentNode()).thenReturn(functionNode);
    when(functionNode.getParent()).thenReturn(Node.newBlock(new Node[0]));

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunction("functionName", new Node[0], new Node[0]);
    JSDocInfo jsDocInfo = Node.newJSDocInfo("JSDocInfo");
    functionNode.setJSDocInfo(jsDocInfo);
    jsDocInfo.setHasThisType(true);

    when(traversal.getCurrentNode()).thenReturn(functionNode);
    when(functionNode.getParent()).thenReturn(Node.newBlock(new Node[0]));

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithOverrideAnnotation() {
    Node functionNode = Node.newFunction("functionName", new Node[0], new Node[0]);
    JSDocInfo jsDocInfo = Node.newJSDocInfo("JSDocInfo");
    functionNode.setJSDocInfo(jsDocInfo);
    jsDocInfo.setIsOverride(true);

    when(traversal.getCurrentNode()).thenReturn(functionNode);
    when(functionNode.getParent()).thenReturn(Node.newBlock(new Node[0]));

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionOutsideBlock() {
    Node functionNode = Node.newFunction("functionName", new Node[0], new Node[0]);
    Node parent = Node.newScript(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(functionNode);
    when(functionNode.getParent()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideBlock() {
    Node functionNode = Node.newFunction("functionName", new Node[0], new Node[0]);
    Node block = Node.newBlock(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(functionNode);
    when(functionNode.getParent()).thenReturn(block);

    assertTrue(checker.shouldTraverse(traversal, functionNode, block));
  }

  @Test
  public void testShouldTraverseFunctionInsidePrototypeProperty() {
    Node functionNode = Node.newFunction("functionName", new Node[0], new Node[0]);
    Node prototype = Node.newName("prototype");
    Node getProp = Node.newGetProp(functionNode, prototype);
    Node block = Node.newBlock(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(getProp);
    when(getProp.getParent()).thenReturn(block);

    assertTrue(checker.shouldTraverse(traversal, functionNode, block));
  }

  @Test
  public void testShouldTraverseFunctionInsideSubProperty() {
    Node functionNode = Node.newFunction("functionName", new Node[0], new Node[0]);
    Node prototype = Node.newName("prototype");
    Node getProp = Node.newGetProp(functionNode, prototype);
    Node qualifier = Node.newName("qualifier");
    Node getProp2 = Node.newGetProp(getProp, qualifier);
    Node block = Node.newBlock(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(getProp2);
    when(getProp2.getParent()).thenReturn(block);

    assertTrue(checker.shouldTraverse(traversal, functionNode, block));
  }

  @Test
  public void testShouldTraverseAssignment() {
    Node parent = Node.newName("parent");
    Node lhs = Node.newIdentifier("lhs");
    Node rhs = Node.newIdentifier("rhs");
    Node assign = Node.newAssign(lhs, rhs);
    Node block = Node.newBlock(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(rhs);
    when(traversal.getParentNode()).thenReturn(assign);
    when(assign.getParent()).thenReturn(block);

    assertTrue(checker.shouldTraverse(traversal, rhs, block));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node parent = Node.newName("parent");
    Node lhs = Node.newGetProp(parent, Node.newName("prototype"));
    Node rhs = Node.newIdentifier("rhs");
    Node assign = Node.newAssign(lhs, rhs);
    Node block = Node.newBlock(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(rhs);
    when(traversal.getParentNode()).thenReturn(assign);
    when(assign.getParent()).thenReturn(block);

    assertFalse(checker.shouldTraverse(traversal, rhs, block));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeSubProperty() {
    Node parent = Node.newName("parent");
    Node lhs = Node.newGetProp(Node.newGetProp(parent, Node.newName("prototype")), Node.newName("subProperty"));
    Node rhs = Node.newIdentifier("rhs");
    Node assign = Node.newAssign(lhs, rhs);
    Node block = Node.newBlock(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(rhs);
    when(traversal.getParentNode()).thenReturn(assign);
    when(assign.getParent()).thenReturn(block);

    assertFalse(checker.shouldTraverse(traversal, rhs, block));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node parent = Node.newName("parent");
    Node lhs = Node.newIdentifier("this");
    Node rhs = Node.newIdentifier("rhs");
    Node assign = Node.newAssign(lhs, rhs);
    Node block = Node.newBlock(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(lhs);

    checker.visit(traversal, null, block);

    compiler.reportCalled();
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node parent = Node.newName("parent");
    Node lhs = Node.newIdentifier("lhs");
    Node rhs = Node.newIdentifier("this");
    Node assign = Node.newAssign(lhs, rhs);
    Node block = Node.newBlock(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(rhs);

    checker.visit(traversal, null, block);

    assertFalse(compiler.reportCalled());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node parent = Node.newName("parent");
    Node lhs = Node.newGetProp(parent, Node.newName("prototype"));
    Node rhs = Node.newIdentifier("this");
    Node block = Node.newBlock(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(lhs);

    checker.visit(traversal, null, block);

    compiler.reportCalled();
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfPropertyAccess() {
    Node parent = Node.newName("parent");
    Node lhs = Node.newIdentifier("lhs");
    Node getProp = Node.newGetProp(lhs, Node.newName("prototype"));
    Node rhs = Node.newIdentifier("this");
    Node block = Node.newBlock(new Node[0]);

    when(traversal.getCurrentNode()).thenReturn(rhs);

    checker.visit(traversal, null, block);

    assertFalse(compiler.reportCalled());
  }
}