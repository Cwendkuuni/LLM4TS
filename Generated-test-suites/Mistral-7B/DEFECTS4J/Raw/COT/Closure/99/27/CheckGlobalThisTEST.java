import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

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
  public void testShouldTraverseFunction() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    traversal.traverse(check, new Node(FUNCTION, null));
    assertTrue(check.shouldTraverse(traversal, new Node(FUNCTION, null), null));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node constructorNode = new Node(FUNCTION, null);
    JSDocInfo constructorDoc = new JSDocInfo();
    constructorDoc.setConstructor(true);

    constructorNode.setJSDocInfo(constructorDoc);

    traversal.traverse(check, constructorNode, null);
    assertFalse(check.shouldTraverse(traversal, constructorNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNode = new Node(FUNCTION, null);
    JSDocInfo functionDoc = new JSDocInfo();
    functionDoc.setThisType("MyType");

    functionNode.setJSDocInfo(functionDoc);

    traversal.traverse(check, functionNode, null);
    assertFalse(check.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNode = new Node(FUNCTION, null);

    traversal.traverse(check, functionNode, new Node(SCRIPT, null));
    assertTrue(check.shouldTraverse(traversal, functionNode, new Node(SCRIPT, null)));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoParent() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNode = new Node(FUNCTION, null);
    traversal.traverse(check, functionNode, null);
    assertFalse(check.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNonScriptParent() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNode = new Node(FUNCTION, null);
    Node parentNode = new Node(BLOCK, null);
    traversal.traverse(check, functionNode, parentNode);
    assertFalse(check.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(THIS, null);

    assignNode.setFirstChild(leftSide);

    Node parentNode = new Node(SCRIPT, null);
    traversal.traverse(check, leftSide, parentNode);
    assertTrue(check.shouldTraverse(traversal, leftSide, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(ID, "obj");
    Node rightSide = new Node(GETPROP, null, new Node(ID, "prototype"), new Node(ID, "prop"));

    assignNode.setFirstChild(leftSide);
    assignNode.setLastChild(rightSide);

    Node parentNode = new Node(SCRIPT, null);
    traversal.traverse(check, rightSide, parentNode);
    assertFalse(check.shouldTraverse(traversal, rightSide, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubProperty() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(ID, "obj");
    Node rightSide = new Node(GETPROP, null, new Node(ID, "prototype"), new Node(GETPROP, null, new Node(ID, "prop"), new Node(ID, "subProp")));

    assignNode.setFirstChild(leftSide);
    assignNode.setLastChild(rightSide);

    Node parentNode = new Node(SCRIPT, null);
    traversal.traverse(check, rightSide, parentNode);
    assertFalse(check.shouldTraverse(traversal, rightSide, parentNode));
  }

  @Test
  public void testShouldReportThisKeyword() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler() {}, CheckLevel.WARNING);

    Node thisNode = new Node(THIS, null);

    Node parentNode = new Node(SCRIPT, null);
    traversal.traverse(check, thisNode, parentNode);
    assertTrue(check.shouldReportThis(traversal, thisNode, parentNode));
  }

  @Test
  public void testShouldNotReportThisKeywordOnLeftSideOfFunctionAssignment() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler() {}, CheckLevel.WARNING);

    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(THIS, null);
    Node function = new Node(FUNCTION, null);

    assignNode.setFirstChild(leftSide);
    assignNode.setLastChild(function);

    Node parentNode = new Node(SCRIPT, null);
    traversal.traverse(check, leftSide, parentNode);
    assertFalse(check.shouldReportThis(traversal, leftSide, parentNode));
  }

  @Test
  public void testShouldNotReportThisKeywordOnPropertyAccessOnLeftSideOfFunctionAssignment() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler() {}, CheckLevel.WARNING);

    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(GETPROP, null, new Node(THIS, null), new Node(ID, "fun"));
    Node function = new Node(FUNCTION, null);

    assignNode.setFirstChild(leftSide);
    assignNode.setLastChild(function);

    Node parentNode = new Node(SCRIPT, null);
    traversal.traverse(check, leftSide, parentNode);
    assertFalse(check.shouldReportThis(traversal, leftSide, parentNode));
  }

  @Test
  public void testShouldReportThisKeywordOnPropertyAccessOnRightSideOfFunctionAssignment() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler() {}, CheckLevel.WARNING);

    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(ID, "obj");
    Node rightSide = new Node(THIS, null);
    Node function = new Node(FUNCTION, null);

    assignNode.setFirstChild(leftSide);
    assignNode.setLastChild(function);

    Node parentNode = new Node(SCRIPT, null);
    traversal.traverse(check, rightSide, parentNode);
    assertTrue(check.shouldReportThis(traversal, rightSide, parentNode));
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThisThrowsExceptionWhenNullCompilerIsPassed() {
    new CheckGlobalThis(null, CheckLevel.WARNING);
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThisThrowsExceptionWhenNullLevelIsPassed() {
    new CheckGlobalThis(new AbstractCompiler() {}, null);
  }
}