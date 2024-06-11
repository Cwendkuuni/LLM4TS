import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

  private CoreOperationCompare testObj;

  @Before
  public void setUp() {
    testObj = new CoreOperationCompare(new Expression(), new Expression());
  }

  @Test
  public void testEqual_withNullValues_returnsTrue() {
    boolean result = testObj.equal(null, null);
    assertTrue(result);
  }

  @Test
  public void testEqual_withDifferentTypes_returnsFalse() {
    boolean result = testObj.equal("abc", 123);
    assertFalse(result);
  }

  @Test
  public void testEqual_withEqualPointers_returnsTrue() {
    Pointer pointer1 = new Pointer();
    Pointer pointer2 = pointer1;
    boolean result = testObj.equal(pointer1, pointer2);
    assertTrue(result);
  }

  @Test
  public void testEqual_withDifferentPointers_returnsFalse() {
    Pointer pointer1 = new Pointer();
    Pointer pointer2 = new Pointer();
    boolean result = testObj.equal(pointer1, pointer2);
    assertFalse(result);
  }

  @Test
  public void testEqual_withEqualNumberValues_returnsTrue() {
    boolean result = testObj.equal(123, 123);
    assertTrue(result);
  }

  @Test
  public void testEqual_withDifferentNumberValues_returnsFalse() {
    boolean result = testObj.equal(123, 456);
    assertFalse(result);
  }

  @Test
  public void testEqual_withEqualBooleanValues_returnsTrue() {
    boolean result = testObj.equal(true, true);
    assertTrue(result);
  }

  @Test
  public void testEqual_withDifferentBooleanValues_returnsFalse() {
    boolean result = testObj.equal(true, false);
    assertFalse(result);
  }

  @Test
  public void testEqual_withEqualStringValues_returnsTrue() {
    boolean result = testObj.equal("abc", "abc");
    assertTrue(result);
  }

  @Test
  public void testEqual_withDifferentStringValues_returnsFalse() {
    boolean result = testObj.equal("abc", "def");
    assertFalse(result);
  }

  @Test
  public void testEqual_withEqualCollectionValues_returnsTrue() {
    Collection<String> collection1 = new ArrayList<>();
    collection1.add("abc");
    collection1.add("def");

    Collection<String> collection2 = new ArrayList<>();
    collection2.add("abc");
    collection2.add("def");

    boolean result = testObj.equal(collection1, collection2);
    assertTrue(result);
  }

  @Test
  public void testEqual_withDifferentCollectionValues_returnsFalse() {
    Collection<String> collection1 = new ArrayList<>();
    collection1.add("abc");
    collection1.add("def");

    Collection<String> collection2 = new ArrayList<>();
    collection2.add("abc");
    collection2.add("xyz");

    boolean result = testObj.equal(collection1, collection2);
    assertFalse(result);
  }

  @Test
  public void testEqual_withEqualIteratorValues_returnsTrue() {
    Collection<String> collection = new ArrayList<>();
    collection.add("abc");
    collection.add("def");
    Iterator<String> iterator = collection.iterator();

    boolean result = testObj.equal(iterator, iterator);
    assertTrue(result);
  }

  @Test
  public void testEqual_withDifferentIteratorValues_returnsFalse() {
    Collection<String> collection = new ArrayList<>();
    collection.add("abc");
    collection.add("def");
    Iterator<String> iterator1 = collection.iterator();
    Iterator<String> iterator2 = collection.iterator();

    boolean result = testObj.equal(iterator1, iterator2);
    assertFalse(result);
  }

  @Test
  public void testEqual_withMatchingValuesInIterators_returnsTrue() {
    Collection<String> collection1 = new ArrayList<>();
    collection1.add("abc");
    collection1.add("def");

    Collection<String> collection2 = new ArrayList<>();
    collection2.add("xyz");
    collection2.add("abc");

    Iterator<String> iterator1 = collection1.iterator();
    Iterator<String> iterator2 = collection2.iterator();

    boolean result = testObj.equal(iterator1, iterator2);
    assertTrue(result);
  }

  @Test
  public void testEqual_withNoMatchingValuesInIterators_returnsFalse() {
    Collection<String> collection1 = new ArrayList<>();
    collection1.add("abc");
    collection1.add("def");

    Collection<String> collection2 = new ArrayList<>();
    collection2.add("xyz");
    collection2.add("pqr");

    Iterator<String> iterator1 = collection1.iterator();
    Iterator<String> iterator2 = collection2.iterator();

    boolean result = testObj.equal(iterator1, iterator2);
    assertFalse(result);
  }

  @Test
  public void testContains_withMatchingValueInIterator_returnsTrue() {
    Collection<String> collection = new ArrayList<>();
    collection.add("abc");
    collection.add("def");
    Iterator<String> iterator = collection.iterator();

    boolean result = testObj.contains(iterator, "abc");
    assertTrue(result);
  }

  @Test
  public void testContains_withNoMatchingValueInIterator_returnsFalse() {
    Collection<String> collection = new ArrayList<>();
    collection.add("abc");
    collection.add("def");
    Iterator<String> iterator = collection.iterator();

    boolean result = testObj.contains(iterator, "xyz");
    assertFalse(result);
  }

  @Test
  public void testFindMatch_withMatchingValuesInIterators_returnsTrue() {
    Collection<String> collection1 = new ArrayList<>();
    collection1.add("abc");
    collection1.add("def");

    Collection<String> collection2 = new ArrayList<>();
    collection2.add("xyz");
    collection2.add("abc");

    Iterator<String> iterator1 = collection1.iterator();
    Iterator<String> iterator2 = collection2.iterator();

    boolean result = testObj.findMatch(iterator1, iterator2);
    assertTrue(result);
  }

  @Test
  public void testFindMatch_withNoMatchingValuesInIterators_returnsFalse() {
    Collection<String> collection1 = new ArrayList<>();
    collection1.add("abc");
    collection1.add("def");

    Collection<String> collection2 = new ArrayList<>();
    collection2.add("xyz");
    collection2.add("pqr");

    Iterator<String> iterator1 = collection1.iterator();
    Iterator<String> iterator2 = collection2.iterator();

    boolean result = testObj.findMatch(iterator1, iterator2);
    assertFalse(result);
  }

  @Test
  public void testFindMatch_withNoMatchingValuesInMixedIterators_returnsFalse() {
    Collection<String> collection = new ArrayList<>();
    collection.add("abc");
    collection.add("def");
    Iterator<String> iterator = collection.iterator();

    boolean result = testObj.findMatch(iterator, "xyz");
    assertFalse(result);
  }
}