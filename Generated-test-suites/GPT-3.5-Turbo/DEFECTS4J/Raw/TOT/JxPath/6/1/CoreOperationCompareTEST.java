import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

  @Test
  public void testEqual() {
    // Test when both values are equal Boolean
    boolean result1 = CoreOperationCompare.equal(new EvalContext(), new Expression<Boolean>(true), new Expression<Boolean>(true));
    Assert.assertEquals(true, result1);

    // Test when both values are different Boolean
    boolean result2 = CoreOperationCompare.equal(new EvalContext(), new Expression<Boolean>(true), new Expression<Boolean>(false));
    Assert.assertEquals(false, result2);

    // Test when both values are equal Number
    boolean result3 = CoreOperationCompare.equal(new EvalContext(), new Expression<Number>(10), new Expression<Number>(10));
    Assert.assertEquals(true, result3);

    // Test when both values are different Number
    boolean result4 = CoreOperationCompare.equal(new EvalContext(), new Expression<Number>(10), new Expression<Number>(20));
    Assert.assertEquals(false, result4);

    // Test when both values are equal String
    boolean result5 = CoreOperationCompare.equal(new EvalContext(), new Expression<String>("abc"), new Expression<String>("abc"));
    Assert.assertEquals(true, result5);

    // Test when both values are different String
    boolean result6 = CoreOperationCompare.equal(new EvalContext(), new Expression<String>("abc"), new Expression<String>("def"));
    Assert.assertEquals(false, result6);

    // Test when both values are equal Pointer
    Object pointer1 = new Pointer();
    boolean result7 = CoreOperationCompare.equal(new EvalContext(), new Expression<Object>(pointer1), new Expression<Object>(pointer1));
    Assert.assertEquals(true, result7);

    // Test when both values are different Pointer
    Object pointer2 = new Pointer();
    boolean result8 = CoreOperationCompare.equal(new EvalContext(), new Expression<Object>(pointer1), new Expression<Object>(pointer2));
    Assert.assertEquals(false, result8);

    // Test when left value is a Collection and it contains the right value
    Collection<Object> collection1 = new ArrayList<Object>();
    collection1.add("abc");
    collection1.add("def");
    boolean result9 = CoreOperationCompare.equal(new EvalContext(), new Expression<Collection<Object>>(collection1), new Expression<Object>("abc"));
    Assert.assertEquals(true, result9);

    // Test when left value is a Collection and it does not contain the right value
    boolean result10 = CoreOperationCompare.equal(new EvalContext(), new Expression<Collection<Object>>(collection1), new Expression<Object>("ghi"));
    Assert.assertEquals(false, result10);

    // Test when right value is a Collection and it contains the left value
    Collection<Object> collection2 = new ArrayList<Object>();
    collection2.add("abc");
    collection2.add("def");
    boolean result11 = CoreOperationCompare.equal(new EvalContext(), new Expression<Object>("abc"), new Expression<Collection<Object>>(collection2));
    Assert.assertEquals(true, result11);

    // Test when right value is a Collection and it does not contain the left value
    boolean result12 = CoreOperationCompare.equal(new EvalContext(), new Expression<Object>("ghi"), new Expression<Collection<Object>>(collection2));
    Assert.assertEquals(false, result12);

    // Test when both values are Iterators and they have a match
    List<Object> list1 = new ArrayList<Object>();
    list1.add("abc");
    list1.add("def");
    Iterator<Object> iterator1 = list1.iterator();
    List<Object> list2 = new ArrayList<Object>();
    list2.add("def");
    list2.add("abc");
    Iterator<Object> iterator2 = list2.iterator();
    boolean result13 = CoreOperationCompare.equal(new EvalContext(), new Expression<Iterator<Object>>(iterator1), new Expression<Iterator<Object>>(iterator2));
    Assert.assertEquals(true, result13);

    // Test when both values are Iterators and they do not have a match
    List<Object> list3 = new ArrayList<Object>();
    list3.add("abc");
    list3.add("def");
    Iterator<Object> iterator3 = list3.iterator();
    List<Object> list4 = new ArrayList<Object>();
    list4.add("ghi");
    list4.add("jkl");
    Iterator<Object> iterator4 = list4.iterator();
    boolean result14 = CoreOperationCompare.equal(new EvalContext(), new Expression<Iterator<Object>>(iterator3), new Expression<Iterator<Object>>(iterator4));
    Assert.assertEquals(false, result14);
  }

  @Test
  public void testContains() {
    // Test when iterator contains the value
    List<Object> list1 = new ArrayList<Object>();
    list1.add("abc");
    list1.add("def");
    Iterator<Object> iterator1 = list1.iterator();
    boolean result1 = CoreOperationCompare.contains(iterator1, "abc");
    Assert.assertEquals(true, result1);

    // Test when iterator does not contain the value
    List<Object> list2 = new ArrayList<Object>();
    list2.add("abc");
    list2.add("def");
    Iterator<Object> iterator2 = list2.iterator();
    boolean result2 = CoreOperationCompare.contains(iterator2, "ghi");
    Assert.assertEquals(false, result2);
  }

  @Test
  public void testFindMatch() {
    // Test when there is a match between iterators
    List<Object> list1 = new ArrayList<Object>();
    list1.add("abc");
    list1.add("def");
    Iterator<Object> iterator1 = list1.iterator();
    List<Object> list2 = new ArrayList<Object>();
    list2.add("def");
    list2.add("hij");
    Iterator<Object> iterator2 = list2.iterator();
    boolean result1 = CoreOperationCompare.findMatch(iterator1, iterator2);
    Assert.assertEquals(true, result1);

    // Test when there is no match between iterators
    List<Object> list3 = new ArrayList<Object>();
    list3.add("abc");
    list3.add("def");
    Iterator<Object> iterator3 = list3.iterator();
    List<Object> list4 = new ArrayList<Object>();
    list4.add("ghi");
    list4.add("jkl");
    Iterator<Object> iterator4 = list4.iterator();
    boolean result2 = CoreOperationCompare.findMatch(iterator3, iterator4);
    Assert.assertEquals(false, result2);
  }

  @Test
  public void testEqualWithNull() {
    // Test when both values are null
    boolean result1 = CoreOperationCompare.equal(new EvalContext(), new Expression<Object>(null), new Expression<Object>(null));
    Assert.assertEquals(true, result1);

    // Test when left value is null and right value is not null
    boolean result2 = CoreOperationCompare.equal(new EvalContext(), new Expression<Object>(null), new Expression<Object>("abc"));
    Assert.assertEquals(false, result2);

    // Test when left value is not null and right value is null
    boolean result3 = CoreOperationCompare.equal(new EvalContext(), new Expression<Object>("abc"), new Expression<Object>(null));
    Assert.assertEquals(false, result3);
  }
}