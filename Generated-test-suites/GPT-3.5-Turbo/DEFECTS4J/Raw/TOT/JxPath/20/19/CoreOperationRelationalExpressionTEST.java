import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

  // Test case for computeValue method
  @Test
  public void testComputeValue() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    // Typical Use Case: Valid operands that evaluate to true
    EvalContext context1 = new EvalContext();
    context1.set("args[0]", 5);
    context1.set("args[1]", 10);
    Assert.assertEquals(Boolean.TRUE, expression.computeValue(context1));
    
    // Typical Use Case: Valid operands that evaluate to false
    EvalContext context2 = new EvalContext();
    context2.set("args[0]", 20);
    context2.set("args[1]", 15);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(context2));
    
    // Edge Case: Both operands are equal
    EvalContext context3 = new EvalContext();
    context3.set("args[0]", 10);
    context3.set("args[1]", 10);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(context3));
    
    // Error Scenario: One or both operands are null
    EvalContext context4 = new EvalContext();
    context4.set("args[0]", null);
    context4.set("args[1]", 5);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(context4));
    
    EvalContext context5 = new EvalContext();
    context5.set("args[0]", 5);
    context5.set("args[1]", null);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(context5));
    
    EvalContext context6 = new EvalContext();
    context6.set("args[0]", null);
    context6.set("args[1]", null);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(context6));
  }
  
  // Test case for getPrecedence method
  @Test
  public void testGetPrecedence() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    Assert.assertEquals(6, expression.getPrecedence());
  }
  
  // Test case for isSymmetric method
  @Test
  public void testIsSymmetric() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    Assert.assertFalse(expression.isSymmetric());
  }
  
  // Test case for evaluateCompare method
  @Test
  public void testEvaluateCompare() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    // Typical Use Case: Comparison result is 0
    Assert.assertTrue(expression.evaluateCompare(0));
    
    // Typical Use Case: Comparison result is positive
    Assert.assertFalse(expression.evaluateCompare(1));
    
    // Typical Use Case: Comparison result is negative
    Assert.assertFalse(expression.evaluateCompare(-1));
  }
  
  // Test case for compute method
  @Test
  public void testCompute() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    // Typical Use Case: Two iterators with intersecting elements
    Iterator<Integer> leftIterator1 = Arrays.asList(1, 2, 3).iterator();
    Iterator<Integer> rightIterator1 = Arrays.asList(3, 4, 5).iterator();
    Assert.assertTrue(expression.compute(leftIterator1, rightIterator1));
    
    // Typical Use Case: Two iterators with no intersecting elements
    Iterator<Integer> leftIterator2 = Arrays.asList(1, 2, 3).iterator();
    Iterator<Integer> rightIterator2 = Arrays.asList(4, 5, 6).iterator();
    Assert.assertFalse(expression.compute(leftIterator2, rightIterator2));
    
    // Error Scenario: One or both iterators are null
    Assert.assertFalse(expression.compute(null, rightIterator1));
    Assert.assertFalse(expression.compute(leftIterator1, null));
    Assert.assertFalse(expression.compute(null, null));
    
    // Typical Use Case: One iterator and one value
    Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
    Integer value = 2;
    Assert.assertTrue(expression.compute(iterator, value));
  }
  
  // Test case for reduce method
  @Test
  public void testReduce() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    // Typical Use Case: Operand is SelfContext
    SelfContext selfContext = new SelfContext();
    Assert.assertEquals(selfContext.getSingleNodePointer(), expression.reduce(selfContext));
    
    // Typical Use Case: Operand is Collection
    Collection<Integer> collection = Arrays.asList(1, 2, 3);
    Iterator<Integer> iterator = collection.iterator();
    Assert.assertEquals(iterator, expression.reduce(collection));
    
    // Typical Use Case: Operand is neither SelfContext nor Collection
    Object operand = 5;
    Assert.assertEquals(operand, expression.reduce(operand));
  }
  
  // Test case for containsMatch method
  @Test
  public void testContainsMatch() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    // Typical Use Case: Iterator contains a matching element
    Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
    Integer value = 2;
    Assert.assertTrue(expression.containsMatch(iterator, value));
    
    // Typical Use Case: Iterator does not contain a matching element
    iterator = Arrays.asList(1, 2, 3).iterator();
    value = 4;
    Assert.assertFalse(expression.containsMatch(iterator, value));
  }
  
  // Test case for findMatch method
  @Test
  public void testFindMatch() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    // Typical Use Case: Iterators have intersecting elements
    Iterator<Integer> leftIterator = Arrays.asList(1, 2, 3).iterator();
    Iterator<Integer> rightIterator = Arrays.asList(3, 4, 5).iterator();
    Assert.assertTrue(expression.findMatch(leftIterator, rightIterator));
    
    // Typical Use Case: Iterators do not have intersecting elements
    leftIterator = Arrays.asList(1, 2, 3).iterator();
    rightIterator = Arrays.asList(4, 5, 6).iterator();
    Assert.assertFalse(expression.findMatch(leftIterator, rightIterator));
  }
  
}