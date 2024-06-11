import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionWithNoAnnotations() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node node = createTestFunctionNode();
    NodeTraversal traversal = new NodeTraversal(null, null, null, checker);
    traversal.traverse(NodeUtil.topLevelContainer(node));
    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node node = createTestFunctionNodeWithConstructorAnnotation();
    NodeTraversal traversal = new NodeTraversal(null, null, null, checker);
    traversal.traverse(NodeUtil.topLevelContainer(node));
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node node = createTestFunctionNodeWithOverrideAnnotation();
    NodeTraversal traversal = new NodeTraversal(null, null, null, checker);
    traversal.traverse(NodeUtil.topLevelContainer(node));
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithInterfaceAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node node = createTestFunctionNodeWithInterfaceAnnotation();
    NodeTraversal traversal = new NodeTraversal(null, null, null, checker);
    traversal.traverse(NodeUtil.topLevelContainer(node));
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node node = createTestFunctionNodeWithThisAnnotation();
    NodeTraversal traversal = new NodeTraversal(null, null, null, checker);
    traversal.traverse(NodeUtil.topLevelContainer(node));
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldReportGlobalThisInFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(), WARNING);
    Node node = createTestFunctionNodeWithGlobalThis();
    NodeTraversal traversal = new NodeTraversal(null, null, null, checker);
    traversal.traverse(NodeUtil.topLevelContainer(node));
    assertEquals(1, checker.compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldReportGlobalThisInPropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(), WARNING);
    Node node = createTestPropertyAccessNodeWithGlobalThis();
    NodeTraversal traversal = new NodeTraversal(null, null, null, checker);
    traversal.traverse(NodeUtil.topLevelContainer(node));
    assertEquals(1, checker.compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportGlobalThisInPrototypePropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(), WARNING);
    Node node = createTestPrototypePropertyAccessNode();
    NodeTraversal traversal = new NodeTraversal(null, null, null, checker);
    traversal.traverse(NodeUtil.topLevelContainer(node));
    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportGlobalThisInNestedAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(), WARNING);
    Node node = createTestNestedAssignmentNode();
    NodeTraversal traversal = new NodeTraversal(null, null, null, checker);
    traversal.traverse(NodeUtil.topLevelContainer(node));
    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldReportGlobalThisInAssignmentToPrototypeProperty() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(), WARNING);
    Node node = createTestAssignmentToPrototypePropertyNode();
    NodeTraversal traversal = new NodeTraversal(null, null, null, checker);
    traversal.traverse(NodeUtil.topLevelContainer(node));
    assertEquals(1, checker.compiler.getDiagnostics().size());
  }

  private Compiler createCompiler() {
    return new Compiler(null, null, null, null, null, null, null, null);
  }

  private Node createTestFunctionNode() {
    return new Node(Token.FUNCTION, new int[] {Token.DEF, Token.FUNCTION},
        null, new int[] {Token.NAME, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        null, null);
  }

  private Node createTestFunctionNodeWithConstructorAnnotation() {
    return new Node(Token.FUNCTION, new int[] {Token.DEF, Token.CONSTRUCTOR},
        null, new int[] {Token.NAME, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        null, null);
  }

  private Node createTestFunctionNodeWithOverrideAnnotation() {
    return new Node(Token.FUNCTION, new int[] {Token.DEF, Token.OVERRIDE},
        null, new int[] {Token.NAME, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        null, null);
  }

  private Node createTestFunctionNodeWithInterfaceAnnotation() {
    return new Node(Token.FUNCTION, new int[] {Token.DEF, Token.INTERFACE},
        null, new int[] {Token.NAME, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        null, null);
  }

  private Node createTestFunctionNodeWithThisAnnotation() {
    return new Node(Token.FUNCTION, new int[] {Token.DEF, Token.THIS},
        null, new int[] {Token.NAME, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        null, null);
  }

  private Node createTestFunctionNodeWithGlobalThis() {
    return new Node(Token.FUNCTION, new int[] {Token.DEF},
        null, new int[] {Token.NAME, Token.OPEN_PAREN, Token.THIS, Token.CLOSE_PAREN},
        null, null);
  }

  private Node createTestPropertyAccessNodeWithGlobalThis() {
    return new Node(Token.GETPROP, new int[] {Token.GET, Token.GETPROP},
        null, new int[] {Token.NAME, Token.STRING_LITERAL, Token.THIS},
        null, null);
  }

  private Node createTestPrototypePropertyAccessNode() {
    return new Node(Token.GETPROP, new int[] {Token.GET, Token.GETPROP},
        null, new int[] {Token.NAME, Token.STRING_LITERAL, Token.NAME},
        null, null);
  }

  private Node createTestNestedAssignmentNode() {
    return new Node(Token.ASSIGN, new int[] {Token.ASSIGN},
        null, new int[] {Token.NAME, Token.NAME, Token.THIS},
        null, null);
  }

  private Node createTestAssignmentToPrototypePropertyNode() {
    return new Node(Token.ASSIGN, new int[] {Token.ASSIGN},
        null, new int[] {Token.NAME, Token.STRING_LITERAL, Token.NAME, Token.THIS},
        null, null);
  }
}