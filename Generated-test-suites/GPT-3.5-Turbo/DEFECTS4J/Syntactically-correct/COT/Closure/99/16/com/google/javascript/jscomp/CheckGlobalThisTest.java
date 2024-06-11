import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalFunctionWithNoAnnotation() {
    // Test the shouldTraverse method with a global function that has no annotations
    // Should return true since there is no reason to skip traversal
    // Setup
    Node functionNode = new Node(Token.FUNCTION);

    // Execute
    boolean result = CheckGlobalThis.shouldTraverse(null, functionNode, null);

    // Verify
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_GlobalFunctionConstructorAnnotation() {
    // Test the shouldTraverse method with a global function that has a constructor annotation (@constructor)
    // Should return false since the function should be skipped
    // Setup
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    functionNode.setJSDocInfo(jsDocInfo);

    // Execute
    boolean result = CheckGlobalThis.shouldTraverse(null, functionNode, null);

    // Verify
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalFunctionThisAnnotation() {
    // Test the shouldTraverse method with a global function that has a @this annotation
    // Should return false since the function should be skipped
    // Setup
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType("Type");
    functionNode.setJSDocInfo(jsDocInfo);

    // Execute
    boolean result = CheckGlobalThis.shouldTraverse(null, functionNode, null);

    // Verify
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalFunctionOverrideAnnotation() {
    // Test the shouldTraverse method with a global function that has an @override annotation
    // Should return false since the function should be skipped
    // Setup
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setOverride(true);
    functionNode.setJSDocInfo(jsDocInfo);

    // Execute
    boolean result = CheckGlobalThis.shouldTraverse(null, functionNode, null);

    // Verify
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_NestedFunctionInsideBlock() {
    // Test the shouldTraverse method with a nested function inside a block
    // Should return false since the function should be skipped
    // Setup
    Node functionNode = new Node(Token.FUNCTION);
    Node parentBlock = new Node(Token.BLOCK);
    parentBlock.addChildToFront(functionNode);

    // Execute
    boolean result = CheckGlobalThis.shouldTraverse(null, functionNode, parentBlock);

    // Verify
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideVarAssignment() {
    // Test the shouldTraverse method with a function inside a var assignment
    // Should return false since the function should be skipped
    // Setup
    Node functionNode = new Node(Token.FUNCTION);
    Node parentAssign = new Node(Token.ASSIGN);
    parentAssign.addChildToFront(functionNode);

    // Execute
    boolean result = CheckGlobalThis.shouldTraverse(null, functionNode, parentAssign);

    // Verify
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalFunctionInsideVar() {
    // Test the shouldTraverse method with a global function inside a var
    // Should return true since the function should be traversed
    // Setup
    Node functionNode = new Node(Token.FUNCTION);
    Node parentVar = new Node(Token.VAR);
    parentVar.addChildToFront(functionNode);

    // Execute
    boolean result = CheckGlobalThis.shouldTraverse(null, functionNode, parentVar);

    // Verify
    assertTrue(result);
  }

  @Test
  public void testVisit_ThisNodeOnLeftSideOfAssignment() {
    // Test the visit method with a THIS node on the left side of an assignment
    // Should report an error for dangerous use of global 'this' object
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node thisNode = new Node(Token.THIS);
    Node parentAssign = new Node(Token.ASSIGN);
    parentAssign.addChildToFront(thisNode);

    // Execute
    checkGlobalThis.visit(null, thisNode, parentAssign);

    // Verify
    // Verify that an error was reported
    // TODO: Add assertion for compiler.report
  }

  @Test
  public void testVisit_ThisNodeOnRightSideOfAssignmentToPrototype() {
    // Test the visit method with a THIS node on the right side of an assignment to a prototype property
    // Should not report an error since assignments to prototype properties are allowed
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node thisNode = new Node(Token.THIS);
    Node parentAssign = new Node(Token.ASSIGN);
    parentAssign.addChildToFront(new Node(Token.GETPROP, new Node(Token.THIS), new Node("prototype")));
    parentAssign.addChildToBack(thisNode);

    // Execute
    checkGlobalThis.visit(null, thisNode, parentAssign);

    // Verify
    // Verify that no error was reported
    // TODO: Add assertion for compiler.report
  }

  @Test
  public void testVisit_ThisNodeOnRightSideOfAssignmentToPrototypeSubproperty() {
    // Test the visit method with a THIS node on the right side of an assignment to a prototype subproperty
    // Should not report an error since assignments to prototype subproperties are allowed
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node thisNode = new Node(Token.THIS);
    Node parentAssign = new Node(Token.ASSIGN);
    parentAssign.addChildToFront(new Node(Token.GETPROP, new Node(Token.THIS), new Node("prototype.property")));
    parentAssign.addChildToBack(thisNode);

    // Execute
    checkGlobalThis.visit(null, thisNode, parentAssign);

    // Verify
    // Verify that no error was reported
    // TODO: Add assertion for compiler.report
  }

  @Test
  public void testVisit_ThisNodeOnRightSideOfRegularAssignment() {
    // Test the visit method with a THIS node on the right side of a regular assignment
    // Should report an error for dangerous use of global 'this' object
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node thisNode = new Node(Token.THIS);
    Node parentAssign = new Node(Token.ASSIGN);
    parentAssign.addChildToFront(new Node(Token.GETPROP, new Node(Token.THIS), new Node("property")));
    parentAssign.addChildToBack(thisNode);

    // Execute
    checkGlobalThis.visit(null, thisNode, parentAssign);

    // Verify
    // Verify that an error was reported
    // TODO: Add assertion for compiler.report
  }

  @Test
  public void testVisit_ThisNodeOnRightSideOfAssignmentToUndefined() {
    // Test the visit method with a THIS node on the right side of an assignment to undefined
    // Should report an error for dangerous use of global 'this' object
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node thisNode = new Node(Token.THIS);
    Node parentAssign = new Node(Token.ASSIGN);
    parentAssign.addChildToFront(new Node(Token.VOID));
    parentAssign.addChildToBack(thisNode);

    // Execute
    checkGlobalThis.visit(null, thisNode, parentAssign);

    // Verify
    // Verify that an error was reported
    // TODO: Add assertion for compiler.report
  }

  @Test
  public void testVisit_ThisNodeOnRightSideOfAssignmentWithNoParent() {
    // Test the visit method with a THIS node on the right side of an assignment with no parent
    // Should not report an error since there is no logical place to put a 'this' annotation
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node thisNode = new Node(Token.THIS);

    // Execute
    checkGlobalThis.visit(null, thisNode, null);

    // Verify
    // Verify that no error was reported
    // TODO: Add assertion for compiler.report
  }

  @Test
  public void testVisit_NonThisNodeOnLeftSideOfAssignment() {
    // Test the visit method with a non-THIS node on the left side of an assignment
    // Should not report an error since it's not a dangerous use of global 'this' object
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node lhsNode = new Node(Token.NAME);
    Node parentAssign = new Node(Token.ASSIGN);
    parentAssign.addChildToFront(lhsNode);

    // Execute
    checkGlobalThis.visit(null, lhsNode, parentAssign);

    // Verify
    // Verify that no error was reported
    // TODO: Add assertion for compiler.report
  }

  @Test
  public void testVisit_NonThisNodeOnRightSideOfAssignment() {
    // Test the visit method with a non-THIS node on the right side of an assignment
    // Should not report an error since it is not a dangerous use of global 'this' object
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node rhsNode = new Node(Token.NAME);
    Node parentAssign = new Node(Token.ASSIGN);
    parentAssign.addChildToBack(rhsNode);

    // Execute
    checkGlobalThis.visit(null, rhsNode, parentAssign);

    // Verify
    // Verify that no error was reported
    // TODO: Add assertion for compiler.report
  }

  @Test
  public void testVisit_NonThisNodeOnLeftSideOfNestedAssignment() {
    // Test the visit method with a non-THIS node on the left side of a nested assignment
    // Should not report an error since it is not a dangerous use of global 'this' object
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node lhsNode = new Node(Token.NAME);
    Node parentAssign1 = new Node(Token.ASSIGN);
    Node parentAssign2 = new Node(Token.ASSIGN);
    parentAssign1.addChildToFront(parentAssign2);
    parentAssign2.addChildToFront(lhsNode);

    // Execute
    checkGlobalThis.visit(null, lhsNode, parentAssign1);

    // Verify
    // Verify that no error was reported
    // TODO: Add assertion for compiler.report
  }

  @Test
  public void testVisit_NonThisNodeOnRightSideOfNestedAssignment() {
    // Test the visit method with a non-THIS node on the right side of a nested assignment
    // Should not report an error since it is not a dangerous use of global 'this' object
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node rhsNode = new Node(Token.NAME);
    Node parentAssign1 = new Node(Token.ASSIGN);
    Node parentAssign2 = new Node(Token.ASSIGN);
    parentAssign1.addChildToBack(parentAssign2);
    parentAssign2.addChildToBack(rhsNode);

    // Execute
    checkGlobalThis.visit(null, rhsNode, parentAssign1);

    // Verify
    // Verify that no error was reported
    // TODO: Add assertion for compiler.report
  }
}