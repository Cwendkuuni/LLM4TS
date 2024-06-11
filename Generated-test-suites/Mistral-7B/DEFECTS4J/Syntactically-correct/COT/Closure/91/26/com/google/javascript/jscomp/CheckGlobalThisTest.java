import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis_constructorAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node constructorNode = new Node(Token.FUNCTION, null, null, null, null, null, null);
    constructorNode.setJSDocInfo(new JSDocInfo(new String[]{"@constructor"}));

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, constructorNode, null);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertFalse(checker.shouldTraverse(traversal, constructorNode, null));
  }

  @Test
  public void testCheckGlobalThis_prototypeMethod() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node prototypeMethodNode = new Node(Token.FUNCTION, null, null, null, null, null, null);
    prototypeMethodNode.setJSDocInfo(new JSDocInfo(new String[]{"@this", "MyObject"}));

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, prototypeMethodNode, new Node(Token.NAME, null, null, null, null, null, null, null, null, new Node(Token.STRING, "MyObject.prototype")));

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, prototypeMethodNode, new Node(Token.NAME, null, null, null, null, null, null, null, null, new Node(Token.STRING, "MyObject.prototype"))));
  }

  @Test
  public void testCheckGlobalThis_thisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node thisAnnotationNode = new Node(Token.NAME, null, null, null, null, null, null, null, null, null, new Node(Token.STRING, "@this"));

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, thisAnnotationNode, null);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertFalse(checker.shouldTraverse(traversal, thisAnnotationNode, null));
  }

  @Test
  public void testCheckGlobalThis_functionWithNoLogicalPlaceForThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, null);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, functionNode, null);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testCheckGlobalThis_functionInsideBlock() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node blockNode = new Node(Token.BLOCK, null, null, null, null, null, null);
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, null);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, blockNode, null);
    traversal.traverse(checker, functionNode, blockNode);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, functionNode, blockNode));
  }

  @Test
  public void testCheckGlobalThis_functionInsideFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node outerFunctionNode = new Node(Token.FUNCTION, null, null, null, null, null, null);
    Node innerFunctionNode = new Node(Token.FUNCTION, null, null, null, null, null, null);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, outerFunctionNode, null);
    traversal.traverse(checker, innerFunctionNode, outerFunctionNode);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, innerFunctionNode, outerFunctionNode));
  }

  @Test
  public void testCheckGlobalThis_functionInsideObjectLiteral() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node objectNode = new Node(Token.OBJECT, null, null, null, null, null, null);
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, null);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, objectNode, null);
    traversal.traverse(checker, functionNode, objectNode);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, functionNode, objectNode));
  }

  @Test
  public void testCheckGlobalThis_functionInsideObjectLiteralKey() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node objectNode = new Node(Token.OBJECT, null, null, null, null, null, null);
    Node getPropNode = new Node(Token.GETPROP, null, null, null, null, null, null, null, null, new Node(Token.STRING, "key"));
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, null);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, objectNode, null);
    traversal.traverse(checker, getPropNode, objectNode);
    traversal.traverse(checker, functionNode, getPropNode);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, functionNode, getPropNode));
  }

  @Test
  public void testCheckGlobalThis_functionInsideObjectLiteralSubProperty() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node objectNode = new Node(Token.OBJECT, null, null, null, null, null, null);
    Node getPropNode = new Node(Token.GETPROP, null, null, null, null, null, null, null, null, new Node(Token.OBJECT, null, null, null, null, null, null));
    Node getPropInObjectNode = new Node(Token.GETPROP, null, null, null, null, null, null, null, null, new Node(Token.STRING, "subProperty"));
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, null);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, objectNode, null);
    traversal.traverse(checker, getPropNode, objectNode);
    traversal.traverse(checker, getPropInObjectNode, getPropNode);
    traversal.traverse(checker, functionNode, getPropInObjectNode);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, functionNode, getPropInObjectNode));
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThis_functionLentToPrototype() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node prototypeNode = new Node(Token.NAME, null, null, null, null, null, null, null, null, null, new Node(Token.STRING, "prototype"));
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, null);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, prototypeNode, null);
    traversal.traverse(checker, functionNode, prototypeNode);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, functionNode, prototypeNode));

    // Throws an AssertionError due to the check in shouldReportThis()
  }

  @Test
  public void testCheckGlobalThis_functionWithAssignLhsChild() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node assignNode = new Node(Token.ASSIGN, null, null, null, null, null, null);
    Node lhs = assignNode.getFirstChild();
    Node rhs = assignNode.getNext();
    Node functionNode = rhs;

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, assignNode, null);
    traversal.traverse(checker, functionNode, assignNode);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, functionNode, assignNode));
  }

  @Test
  public void testCheckGlobalThis_functionWithAssignLhsChildAndGetProp() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node assignNode = new Node(Token.ASSIGN, null, null, null, null, null, null);
    Node lhs = assignNode.getFirstChild();
    Node rhs = assignNode.getNext();
    Node getPropNode = rhs.getFirstChild();
    Node functionNode = getPropNode.getNext();

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, assignNode, null);
    traversal.traverse(checker, functionNode, assignNode);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, functionNode, assignNode));

    // getPropNode should not be traversed, but its parent is, so we need to check
    // that this.shouldReportThis() returns false for it
    assertFalse(checker.shouldReportThis(getPropNode, assignNode));
  }

  @Test
  public void testCheckGlobalThis_functionWithAssignLhsChildAndGetPropInObject() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node assignNode = new Node(Token.ASSIGN, null, null, null, null, null, null);
    Node lhs = assignNode.getFirstChild();
    Node rhs = assignNode.getNext();
    Node getPropNode = rhs.getFirstChild();
    Node getPropInObjectNode = getPropNode.getFirstChild();
    Node functionNode = getPropInObjectNode.getNext();

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(checker, assignNode, null);
    traversal.traverse(checker, functionNode, assignNode);

    assertTrue(checker.level.isAtLeast(WARNING));
    assertTrue(checker.shouldTraverse(traversal, functionNode, assignNode));

    // getPropNode should not be traversed, but its parent is, so we need to check
    // that this.shouldReportThis() returns false for it
    assertFalse(checker.shouldReportThis(getPropNode, assignNode));

    // getPropInObjectNode should not be traversed, but its parent is, so we need to check
    // that this.shouldReportThis() returns false for it
    assertFalse(checker.shouldReportThis(getPropInObjectNode, assignNode));
  }
}