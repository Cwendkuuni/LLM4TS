import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

  // Test for equal method
  @Test
  public void testEqual() {
    Expression left = new MockExpression(2);
    Expression right = new MockExpression(2);

    CoreOperationCompare compare = new MockCoreOperationCompare(left, right);

    EvalContext context = new EvalContext();

    boolean result = compare.equal(context, left, right);

    Assert.assertTrue(result);
  }
  
  // Test for contains method when the iterator contains the value
  @Test
  public void testContainsValue() {
    MockCollection collection = new MockCollection();
    collection.add(1);
    collection.add(2);
    collection.add(3);
    
    Iterator iterator = collection.iterator();
    Object value = 2;

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.contains(iterator, value);

    Assert.assertTrue(result);
  }
  
  // Test for contains method when the iterator doesn't contain the value
  @Test
  public void testDoesNotContainValue() {
    MockCollection collection = new MockCollection();
    collection.add("hello");
    collection.add("world");
    
    Iterator iterator = collection.iterator();
    Object value = "foo";

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.contains(iterator, value);

    Assert.assertFalse(result);
  }
  
  // Test for findMatch method when there is a match between the iterators
  @Test
  public void testFindMatch() {
    MockCollection collection1 = new MockCollection();
    collection1.add(1);
    collection1.add(2);
    collection1.add(3);
    
    MockCollection collection2 = new MockCollection();
    collection2.add(4);
    collection2.add(5);
    collection2.add(6);
    collection2.add(2);
    
    Iterator iterator1 = collection1.iterator();
    Iterator iterator2 = collection2.iterator();

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.findMatch(iterator1, iterator2);

    Assert.assertTrue(result);
  }
  
  // Test for findMatch method when there is no match between the iterators
  @Test
  public void testNoMatch() {
    MockCollection collection1 = new MockCollection();
    collection1.add(1);
    collection1.add(2);
    collection1.add(3);
    
    MockCollection collection2 = new MockCollection();
    collection2.add(4);
    collection2.add(5);
    collection2.add(6);
    
    Iterator iterator1 = collection1.iterator();
    Iterator iterator2 = collection2.iterator();

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.findMatch(iterator1, iterator2);

    Assert.assertFalse(result);
  }
  
  // Test for equal method when l and r are Pointers and they are equal
  @Test
  public void testEqualPointers() {
    Pointer pointer1 = new Pointer("value1");
    Pointer pointer2 = new Pointer("value1");

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(pointer1, pointer2);

    Assert.assertTrue(result);
  }
  
  // Test for equal method when l and r are Pointers and they are not equal
  @Test
  public void testNotEqualPointers() {
    Pointer pointer1 = new Pointer("value1");
    Pointer pointer2 = new Pointer("value2");

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(pointer1, pointer2);

    Assert.assertFalse(result);
  }
  
  // Test for equal method when l is a Pointer and it equals the value of Pointer r
  @Test
  public void testEqualPointerWithValue() {
    Pointer pointer = new Pointer("value");
    Pointer pointerValue = new Pointer("value");

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(pointer, pointerValue);

    Assert.assertTrue(result);
  }
  
  // Test for equal method when r is a Pointer and it equals the value of Pointer l
  @Test
  public void testEqualWithValuePointer() {
    Pointer pointer = new Pointer("value");
    Pointer pointerValue = new Pointer("value");

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(pointerValue, pointer);

    Assert.assertTrue(result);
  }
  
  // Test for equal method when l and r are Booleans and they are equal
  @Test
  public void testEqualBooleans() {
    boolean l = true;
    boolean r = true;

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(l, r);

    Assert.assertTrue(result);
  }
  
  // Test for equal method when l and r are Booleans and they are not equal
  @Test
  public void testNotEqualBooleans() {
    boolean l = true;
    boolean r = false;

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(l, r);

    Assert.assertFalse(result);
  }
  
  // Test for equal method when l and r are Numbers and they are equal
  @Test
  public void testEqualNumbers() {
    double l = 2.5;
    double r = 2.5;

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(l, r);

    Assert.assertTrue(result);
  }
  
  // Test for equal method when l and r are Numbers and they are not equal
  @Test
  public void testNotEqualNumbers() {
    double l = 2.5;
    double r = 3.0;

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(l, r);

    Assert.assertFalse(result);
  }
  
  // Test for equal method when l and r are Strings and they are equal
  @Test
  public void testEqualStrings() {
    String l = "hello";
    String r = "hello";

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(l, r);

    Assert.assertTrue(result);
  }
  
  // Test for equal method when l and r are Strings and they are not equal
  @Test
  public void testNotEqualStrings() {
    String l = "hello";
    String r = "world";

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(l, r);

    Assert.assertFalse(result);
  }
  
  // Test for equal method when l is not equal to r
  @Test
  public void testNotEqualValues() {
    Object l = new Object();
    Object r = new Object();

    CoreOperationCompare compare = new MockCoreOperationCompare();

    boolean result = compare.equal(l, r);

    Assert.assertFalse(result);
  }

  // Mock classes for testing
  
  private class MockExpression extends Expression {
    private Object value;
    
    public MockExpression(Object value) {
      this.value = value;
    }
    
    @Override
    public Object compute(EvalContext context) {
      return value;
    }
  }
  
  private class MockCoreOperationCompare extends CoreOperationCompare {
    public MockCoreOperationCompare(Expression arg1, Expression arg2) {
      super(arg1, arg2);
    }
    
    public MockCoreOperationCompare() {
      super(null, null);
    }
  }
  
  private class MockCollection extends Collection {
    private List<Object> objects;
    
    public MockCollection() {
      objects = new ArrayList<>();
    }
    
    public void add(Object obj) {
      objects.add(obj);
    }

    // Other required methods for implementing Collection interface
  }
}