import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    Node globalFunction = Node.newFunctionNode("function globalFunction() {}");
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, globalFunction, null));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorFunction = Node.newFunctionNode("function MyConstructor() {}",
        new JSDocInfo("MyConstructor", new String[] {"constructor"}));
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, constructorFunction, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node thisAnnotationFunction = Node.newFunctionNode(
        "function MyFunction() { @this };", new JSDocInfo("MyFunction", null));
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, thisAnnotationFunction, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node noLogicalPlaceFunction = Node.newFunctionNode(
        "function MyFunction() { return 42; }");
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, noLogicalPlaceFunction, null));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node assignmentNode = Node.newAssign(Node.newThisNode(), Node.newName("x"));
    Node functionNode = Node.newFunctionNode("function MyFunction() {}", null);
    Node parent = Node.newAssign(Node.newName("y"), assignmentNode);
    Node root = Node.newAssign(Node.newName("z"), functionNode);

    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    checker.visit(NodeTraversal.EMPTY, assignmentNode, parent);
    checker.visit(NodeTraversal.EMPTY, functionNode, root);
    assertEquals(1, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    Node assignmentNode = Node.newAssign(Node.newName("x"), Node.newThisNode());
    Node functionNode = Node.newFunctionNode("function MyFunction() {}", null);
    Node parent = Node.newAssign(Node.newName("y"), Node.newThisNode());
    Node root = Node.newAssign(Node.newName("z"), functionNode);

    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    checker.visit(NodeTraversal.EMPTY, assignmentNode, parent);
    checker.visit(NodeTraversal.EMPTY, functionNode, root);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfPropertyAccess() {
    Node propertyAccessNode = Node.newGetProp(Node.newThisNode(), "x");
    Node functionNode = Node.newFunctionNode("function MyFunction() {}", null);
    Node parent = Node.newName("y");
    Node root = Node.newGetProp(Node.newName("z"), propertyAccessNode);

    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    checker.visit(NodeTraversal.EMPTY, propertyAccessNode, parent);
    checker.visit(NodeTraversal.EMPTY, functionNode, root);
    assertEquals(1, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfPropertyAccess() {
    Node propertyAccessNode = Node.newGetProp(Node.newThisNode(), "x");
    Node functionNode = Node.newFunctionNode("function MyFunction() {}", null);
    Node parent = Node.newGetProp(Node.newName("y"), Node.newName("prototype"));
    Node root = Node.newAssign(Node.newName("z"), propertyAccessNode);

    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    checker.visit(NodeTraversal.EMPTY, propertyAccessNode, parent);
    checker.visit(NodeTraversal.EMPTY, functionNode, root);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisKeywordOnLeftSideOfAssignmentToPrototypeProperty() {
    Node prototypePropertyAssignmentNode = Node.newAssign(Node.newGetProp(Node.newThisNode(), "prototype"), Node.newName("x"));
    Node functionNode = Node.newFunctionNode("function MyFunction() {}", null);
    Node parent = Node.newName("y");
    Node root = Node.newAssign(Node.newName("z"), functionNode);

    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    checker.visit(NodeTraversal.EMPTY, prototypePropertyAssignmentNode, parent);
    checker.visit(NodeTraversal.EMPTY, functionNode, root);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignmentToPrototypeProperty() {
    Node prototypePropertyAssignmentNode = Node.newAssign(Node.newThisNode(), Node.newName("prototype"));
    Node functionNode = Node.newFunctionNode("function MyFunction() {}", null);
    Node parent = Node.newName("y");
    Node root = Node.newAssign(Node.newName("z"), functionNode);

    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    checker.visit(NodeTraversal.EMPTY, prototypePropertyAssignmentNode, parent);
    checker.visit(NodeTraversal.EMPTY, functionNode, root);
    assertEquals(0, compiler.getDiagnostics().size());
  }
}