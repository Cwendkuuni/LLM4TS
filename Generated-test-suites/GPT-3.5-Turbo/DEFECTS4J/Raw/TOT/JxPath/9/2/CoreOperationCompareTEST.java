import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
  
  // Test cases for equal method
  
  @Test
  public void testEqualWithEqualPointers() {
    EvalContext context = new EvalContext();
    Pointer pointer1 = new Pointer("value1");
    Pointer pointer2 = new Pointer("value1");
    Expression left = new Expression(pointer1);
    Expression right = new Expression(pointer2);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualWithDifferentPointers() {
    EvalContext context = new EvalContext();
    Pointer pointer1 = new Pointer("value1");
    Pointer pointer2 = new Pointer("value2");
    Expression left = new Expression(pointer1);
    Expression right = new Expression(pointer2);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  @Test
  public void testEqualWithEqualValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression("value1");
    Expression right = new Expression("value1");
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualWithDifferentValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression("value1");
    Expression right = new Expression("value2");
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  @Test
  public void testEqualWithBooleanValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(true);
    Expression right = new Expression(true);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualWithDifferentBooleanValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(true);
    Expression right = new Expression(false);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  @Test
  public void testEqualWithNumberValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(10);
    Expression right = new Expression(10);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualWithDifferentNumberValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(10);
    Expression right = new Expression(20);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  @Test
  public void testEqualWithStrings() {
    EvalContext context = new EvalContext();
    Expression left = new Expression("abc");
    Expression right = new Expression("abc");
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualWithDifferentStrings() {
    EvalContext context = new EvalContext();
    Expression left = new Expression("abc");
    Expression right = new Expression("def");
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  @Test
  public void testEqualWithNullValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(null);
    Expression right = new Expression(null);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  // Test cases for contains method
  
  @Test
  public void testContainsWithEmptyIterator() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new Object[]{});
    Expression right = new Expression("value");
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.contains(context, left.getValues().iterator(), right.getValue());
    
    assertFalse(result);
  }
  
  @Test
  public void testContainsWithIteratorContainingValue() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new Object[]{"value1", "value2", "value3"});
    Expression right = new Expression("value2");
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.contains(context, left.getValues().iterator(), right.getValue());
    
    assertTrue(result);
  }
  
  @Test
  public void testContainsWithIteratorNotContainingValue() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new Object[]{"value1", "value2", "value3"});
    Expression right = new Expression("value4");
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.contains(context, left.getValues().iterator(), right.getValue());
    
    assertFalse(result);
  }
  
  // Test cases for findMatch method
  
  @Test
  public void testFindMatchWithEmptyIterators() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new Object[]{});
    Expression right = new Expression(new Object[]{});
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.findMatch(context, left.getValues().iterator(), right.getValues().iterator());
    
    assertFalse(result);
  }
  
  @Test
  public void testFindMatchWithSameValueInBothIterators() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new Object[]{"value1", "value2", "value3"});
    Expression right = new Expression(new Object[]{"value4", "value5", "value2"});
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.findMatch(context, left.getValues().iterator(), right.getValues().iterator());
    
    assertTrue(result);
  }
  
  @Test
  public void testFindMatchWithNoMatchingValuesInIterators() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new Object[]{"value1", "value2", "value3"});
    Expression right = new Expression(new Object[]{"value4", "value5", "value6"});
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.findMatch(context, left.getValues().iterator(), right.getValues().iterator());
    
    assertFalse(result);
  }
  
  // Test cases for equal method
  
  @Test
  public void testEqualWithPointerValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new Pointer("value1"));
    Expression right = new Expression(new Pointer("value1"));
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualWithDifferentPointerValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new Pointer("value1"));
    Expression right = new Expression(new Pointer("value2"));
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  // Test cases for equal method with Resettable pointers
  
  @Test
  public void testEqualWithResettablePointers() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new InitialContext());
    Expression right = new Expression(new InitialContext());
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualWithResettablePointerAndNonResettablePointer() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new InitialContext());
    Expression right = new Expression(new SelfContext());
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  // Test cases for equal method with Collection
  
  @Test
  public void testEqualWithIteratorValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new Object[]{"value1", "value2", "value3"}.iterator());
    Expression right = new Expression(new Object[]{"value4", "value5", "value2"}.iterator());
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualWithDifferentIteratorValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(new Object[]{"value1", "value2", "value3"}.iterator());
    Expression right = new Expression(new Object[]{"value4", "value5", "value6"}.iterator());
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  // Test cases for equal method with NaN values
  
  @Test
  public void testEqualWithNaNValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(Double.NaN);
    Expression right = new Expression(Double.NaN);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  // Test cases for equal method with Double values
  
  @Test
  public void testEqualWithDoubleValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(10.0);
    Expression right = new Expression(10.0);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualWithDifferentDoubleValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(10.0);
    Expression right = new Expression(20.0);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  // Test cases for equal method with String values
  
  @Test
  public void testEqualWithSameStringValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression("abc");
    Expression right = new Expression("abc");
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualWithDifferentStringValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression("abc");
    Expression right = new Expression("def");
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertFalse(result);
  }
  
  // Test cases for equal method with Null values
  
  @Test
  public void testEqualWithNullValues() {
    EvalContext context = new EvalContext();
    Expression left = new Expression(null);
    Expression right = new Expression(null);
    CoreOperationCompare operation = new CoreOperationCompare(left, right);
    
    boolean result = operation.equal(context, left, right);
    
    assertTrue(result);
  }
  
}