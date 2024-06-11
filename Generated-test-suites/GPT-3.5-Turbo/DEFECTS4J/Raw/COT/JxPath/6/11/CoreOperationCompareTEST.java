import org.junit.Test;

public class CoreOperationCompareTest {

  @Test
  public void testEqual() {
    // Test case for equal method
    EvalContext context = new EvalContext();
    Expression left = new Expression();
    Expression right = new Expression();
    CoreOperationCompare compare = new CoreOperationCompare(left, right);
    
    // Test when left and right values are equal
    context.setSingleNodePointer(new Pointer("test"));
    assert(compare.equal(context, left, right));
    
    // Test when left and right values are not equal
    context.setSingleNodePointer(new Pointer("test1"));
    assert(!compare.equal(context, left, right));
  }
  
  // Add other JUnit test cases for other methods in the CoreOperationCompare class

  @Test
  public void testContains() {
    // Test case for contains method
    Expression expression = new Expression();
    CoreOperationCompare compare = new CoreOperationCompare(expression, expression);
    Iterator it = new Iterator();
    Object value = new Object();
    
    // Test when iterator contains the value
    it.add(value);
    assert(compare.contains(it, value));
    
    // Test when iterator does not contain the value
    assert(!compare.contains(it, new Object()));
  }
  
  @Test
  public void testFindMatch() {
    // Test case for findMatch method
    Expression expression = new Expression();
    CoreOperationCompare compare = new CoreOperationCompare(expression, expression);
    Iterator lit = new Iterator();
    Iterator rit = new Iterator();
    
    // Test when there is a match between the iterators
    lit.add(new Object());
    rit.add(new Object());
    assert(compare.findMatch(lit, rit));
    
    // Test when there is no match between the iterators
    assert(!compare.findMatch(lit, new Iterator()));
  }
  
  @Test
  public void testEqualValues() {
    // Test case for equal method with values
    CoreOperationCompare compare = new CoreOperationCompare(new Expression(), new Expression());
    Object l = new Object();
    Object r = new Object();
    
    // Test when l and r are equal values
    assert(compare.equal(l, l));
    
    // Test when l and r are not equal values
    assert(!compare.equal(l, r));
  }

}