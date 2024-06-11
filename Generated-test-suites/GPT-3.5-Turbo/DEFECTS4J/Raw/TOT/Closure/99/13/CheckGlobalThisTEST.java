import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseWithConstructor() {
    // Test shouldTraverse method when encountering a constructor function
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    // Create a constructor function
    Node constructorFunction = new Node(Token.FUNCTION);
    // Set the @constructor annotation in JSDoc
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    constructorFunction.setJSDocInfo(jsDoc);

    // Test that shouldTraverse returns false
    assertFalse(checkGlobalThis.shouldTraverse(null, constructorFunction, null));
  }

  @Test
  public void testShouldTraverseWithThisAnnotation() {
    // Test shouldTraverse method when encountering a function with @this annotation
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    // Create a function with @this annotation
    Node functionWithThisAnnotation = new Node(Token.FUNCTION);
    // Set the @this annotation in JSDoc
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(new JSTypeExpression(new Node(Token.BANG), "Object"));
    functionWithThisAnnotation.setJSDocInfo(jsDoc);

    // Test that shouldTraverse returns false
    assertFalse(checkGlobalThis.shouldTraverse(null, functionWithThisAnnotation, null));
  }

  @Test
  public void testShouldTraverseWithGlobalContext() {
    // Test shouldTraverse method when encountering a function with global context
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    // Create a function without the proper context for @this annotation
    Node functionWithGlobalContext = new Node(Token.FUNCTION);
    Node parent = new Node(Token.PLUS, new Node(Token.ADD, new Node(Token.NUMBER, "1"), new Node(Token.NUMBER, "2")));
    functionWithGlobalContext.setParent(parent);

    // Test that shouldTraverse returns false
    assertFalse(checkGlobalThis.shouldTraverse(null, functionWithGlobalContext, parent));
  }

  @Test
  public void testShouldTraverseWithAssignment() {
    // Test shouldTraverse method when encountering an assignment
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    // Create an assignment statement
    Node assignment = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.THIS);
    assignment.addChildToBack(lhs);
    assignment.addChildToBack(rhs);
    Node parent = new Node(Token.EXPR_RESULT, assignment);
    assignment.setParent(parent);

    // Test that shouldTraverse returns true when the left side of the assignment is THIS
    assertTrue(checkGlobalThis.shouldTraverse(null, lhs, assignment));
  }

  @Test
  public void testShouldTraverseWithPrototypePropertyAssignment() {
    // Test shouldTraverse method when encountering an assignment to a prototype property
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    // Create an assignment to a prototype property
    Node assignment = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "obj"), new Node(Token.STRING, "prototype"));
    Node rhs = new Node(Token.THIS);
    assignment.addChildToBack(lhs);
    assignment.addChildToBack(rhs);
    Node parent = new Node(Token.EXPR_RESULT, assignment);
    assignment.setParent(parent);

    // Test that shouldTraverse returns false when the assignment is to a prototype property
    assertFalse(checkGlobalThis.shouldTraverse(null, lhs, assignment));
  }

  @Test
  public void testShouldTraverseWithNestedAssignment() {
    // Test shouldTraverse method when encountering a nested assignment
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    // Create a nested assignment statement
    Node outerAssignment = new Node(Token.ASSIGN);
    Node innerAssignment = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "a"), new Node(Token.STRING, "property"));
    Node rhs = new Node(Token.THIS);
    innerAssignment.addChildToBack(lhs);
    innerAssignment.addChildToBack(rhs);
    outerAssignment.addChildToBack(lhs);
    outerAssignment.addChildToBack(innerAssignment);
    Node parent = new Node(Token.EXPR_RESULT, outerAssignment);
    outerAssignment.setParent(parent);

    // Test that shouldTraverse returns false when the assignment is nested and to a prototype property
    assertFalse(checkGlobalThis.shouldTraverse(null, lhs, outerAssignment));
  }

  @Test
  public void testVisitWithGlobalThis() {
    // Test visit method when encountering a global this keyword
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    
    // Create a global this node
    Node globalThis = new Node(Token.THIS);
    Node parent = new Node(Token.EXPR_RESULT, globalThis);

    // Call visit method
    checkGlobalThis.visit(t, globalThis, parent);

    // Test that the compiler reports an error
    assertEquals(1, compiler.getErrorCount());
  }

  @Test
  public void testVisitWithAssignmentLeftSideThis() {
    // Test visit method when encountering a this keyword on the left side of an assignment
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    
    // Create an assignment statement with this on the left side
    Node assignment = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    Node rhs = new Node(Token.NAME, "a");
    assignment.addChildToBack(lhs);
    assignment.addChildToBack(rhs);
    Node parent = new Node(Token.EXPR_RESULT, assignment);

    // Call visit method
    checkGlobalThis.visit(t, lhs, assignment);

    // Test that the compiler reports an error
    assertEquals(1, compiler.getErrorCount());
  }

  @Test
  public void testVisitWithNonAssignmentThis() {
    // Test visit method when encountering a this keyword that is not on the left side of an assignment
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    
    // Create a this node that is not on the left side of an assignment
    Node nonAssignmentThis = new Node(Token.THIS);
    Node parent = new Node(Token.ADD, new Node(Token.NAME, "a"), new Node(Token.NAME, "b"));

    // Call visit method
    checkGlobalThis.visit(t, nonAssignmentThis, parent);

    // Test that the compiler does not report an error
    assertEquals(0, compiler.getErrorCount());
  }


  @Test
  public void testVisitWithAssignmentLeftSideAndNestedAssignmentRightSide() {
    // Test visit method when encountering an assignment with this on the left side and a nested assignment on the right side
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    
    // Create an assignment statement with this on the left side and a nested assignment on the right side
    Node assignment = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    Node rhs = new Node(Token.ASSIGN);
    Node rhsLhs = new Node(Token.GETPROP, new Node(Token.NAME, "a"), new Node(Token.STRING, "property"));
    Node rhsRhs = new Node(Token.NAME, "c");
    rhs.addChildToBack(rhsLhs);
    rhs.addChildToBack(rhsRhs);
    assignment.addChildToBack(lhs);
    assignment.addChildToBack(rhs);
    Node parent = new Node(Token.EXPR_RESULT, assignment);

    // Call visit method
    checkGlobalThis.visit(t, lhs, assignment);

    // Test that the compiler reports an error
    assertEquals(1, compiler.getErrorCount());
  }

  @Test
  public void testVisitWithAssignmentRightSideThis() {
    // Test visit method when encountering a this keyword on the right side of an assignment
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    
    // Create an assignment statement with this on the right side
    Node assignment = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.THIS);
    assignment.addChildToBack(lhs);
    assignment.addChildToBack(rhs);
    Node parent = new Node(Token.EXPR_RESULT, assignment);

    // Call visit method
    checkGlobalThis.visit(t, rhs, assignment);

    // Test that the compiler does not report an error
    assertEquals(0, compiler.getErrorCount());
  }

  @Test
  public void testVisitWithNonAssignmentLeftSide() {
    // Test visit method when encountering a non-assignment left side
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    
    // Create an expression with a non-assignment left side
    Node nonAssignmentLeftSide = new Node(Token.NAME, "a");
    Node parent = new Node(Token.ADD, nonAssignmentLeftSide, new Node(Token.NAME, "b"));

    // Call visit method
    checkGlobalThis.visit(t, nonAssignmentLeftSide, parent);

    // Test that the compiler does not report an error
    assertEquals(0, compiler.getErrorCount());
  }

  @Test
  public void testVisitWithNonGetThisAccess() {
    // Test visit method when encountering a non-get this access
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    
    // Create a this node that is not part of a get property access
    Node nonGetPropertyThis = new Node(Token.THIS);
    Node parent = new Node(Token.NAME, "a");

    // Call visit method
    checkGlobalThis.visit(t, nonGetPropertyThis, parent);

    // Test that the compiler does not report an error
    assertEquals(0, compiler.getErrorCount());
  }
}