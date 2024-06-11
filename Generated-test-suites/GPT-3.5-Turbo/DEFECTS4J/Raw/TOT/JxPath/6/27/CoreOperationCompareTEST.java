import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

  @Test
  public void testEqual_WithNullValues_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare(null, null);
    assertTrue("Expected true for equal null values", operation.equal(null, null));
  }

  @Test
  public void testEqual_WithSamePointerValues_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare(pointer1, pointer1);
    assertTrue("Expected true for equal pointer values", operation.equal(pointer1, pointer1));
  }

  @Test
  public void testEqual_WithDifferentPointerValues_ReturnsFalse() {
    CoreOperationCompare operation = new CoreOperationCompare(pointer1, pointer2);
    assertFalse("Expected false for different pointer values", operation.equal(pointer1, pointer2));
  }

  @Test
  public void testEqual_WithEqualBooleanValues_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare(true, true);
    assertTrue("Expected true for equal boolean values", operation.equal(true, true));
  }

  @Test
  public void testEqual_WithDifferentBooleanValues_ReturnsFalse() {
    CoreOperationCompare operation = new CoreOperationCompare(true, false);
    assertFalse("Expected false for different boolean values", operation.equal(true, false));
  }

  @Test
  public void testEqual_WithEqualNumberValues_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare(2, 2);
    assertTrue("Expected true for equal number values", operation.equal(2, 2));
  }

  @Test
  public void testEqual_WithDifferentNumberValues_ReturnsFalse() {
    CoreOperationCompare operation = new CoreOperationCompare(2, 3);
    assertFalse("Expected false for different number values", operation.equal(2, 3));
  }

  @Test
  public void testEqual_WithEqualStringValues_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare("hello", "hello");
    assertTrue("Expected true for equal string values", operation.equal("hello", "hello"));
  }

  @Test
  public void testEqual_WithDifferentStringValues_ReturnsFalse() {
    CoreOperationCompare operation = new CoreOperationCompare("hello", "world");
    assertFalse("Expected false for different string values", operation.equal("hello", "world"));
  }

  @Test
  public void testEqual_WithEqualCollections_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare(collection1, collection1);
    assertTrue("Expected true for equal collections", operation.equal(collection1, collection1));
  }

  @Test
  public void testEqual_WithDifferentCollections_ReturnsFalse() {
    CoreOperationCompare operation = new CoreOperationCompare(collection1, collection2);
    assertFalse("Expected false for different collections", operation.equal(collection1, collection2));
  }

  @Test
  public void testEqual_WithCollectionAndElement_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare(collection1, element1);
    assertTrue("Expected true for collection and element", operation.equal(collection1, element1));
  }

  @Test
  public void testEqual_WithElementAndCollection_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare(element1, collection1);
    assertTrue("Expected true for element and collection", operation.equal(element1, collection1));
  }

  @Test
  public void testContains_WithIteratorContainingValue_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare(iter, element1);
    assertTrue("Expected true for iterator containing value", operation.contains(iter, element1));
  }

  @Test
  public void testContains_WithIteratorNotContainingValue_ReturnsFalse() {
    CoreOperationCompare operation = new CoreOperationCompare(iter, element1);
    assertFalse("Expected false for iterator not containing value", operation.contains(iter, element2));
  }

  @Test
  public void testFindMatch_WithIteratorsContainingValue_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare(iter1, iter2);
    assertTrue("Expected true for iterators containing value", operation.findMatch(iter1, iter2));
  }

  @Test
  public void testFindMatch_WithNoMatchingIterators_ReturnsFalse() {
    CoreOperationCompare operation = new CoreOperationCompare(iter1, iter2);
    assertFalse("Expected false for no matching iterators", operation.findMatch(iter1, iter3));
  }

  @Test
  public void testEqual_WithEqualObjects_ReturnsTrue() {
    CoreOperationCompare operation = new CoreOperationCompare(obj1, obj1);
    assertTrue("Expected true for equal objects", operation.equal(obj1, obj1));
  }

  @Test
  public void testEqual_WithDifferentObjects_ReturnsFalse() {
    CoreOperationCompare operation = new CoreOperationCompare(obj1, obj2);
    assertFalse("Expected false for different objects", operation.equal(obj1, obj2));
  }

}