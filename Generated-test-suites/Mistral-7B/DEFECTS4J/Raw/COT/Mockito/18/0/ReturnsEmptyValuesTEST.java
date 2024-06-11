import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testToStringMethod() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 String expected = "Mock for [class java.lang.Object], hashCode: " + System.identityHashCode(invocation.getMock());
 String actual = (String) returnsEmptyValues.answer(invocation);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testCompareToMethodWithSameArguments() {
 Object mock = Mockito.mock(Comparable.class);
 Object argument = mock;
 int expected = 0;
 int actual = ((Comparable) returnsEmptyValues).answer(new InvocationOnMock(null, null, Comparable.class, "compareTo", new Object[]{argument}, new Object[0][0])).intValue();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testCompareToMethodWithDifferentArguments() {
 Object mock = Mockito.mock(Comparable.class);
 Object argument = new Object();
 int expected = 1;
 int actual = ((Comparable) returnsEmptyValues).answer(new InvocationOnMock(null, null, Comparable.class, "compareTo", new Object[]{mock, argument}, new Object[0][0])).intValue();
 Assert.assertNotEquals(expected, actual);
 }

 @Test
 public void testPrimitiveTypes() {
 Class<?> intType = int.class;
 Object actual = returnsEmptyValues.returnValueFor(intType);
 int expected = Primitives.intValue(0);
 Assert.assertEquals(expected, actual);

 Class<?> booleanType = boolean.class;
 Object actualBoolean = returnsEmptyValues.returnValueFor(booleanType);
 boolean expectedBoolean = Primitives.booleanValue(false);
 Assert.assertEquals(expectedBoolean, actualBoolean);
 }

 @Test
 public void testEmptyCollection() {
 Class<?> collectionType = Collection.class;
 Object actualCollection = returnsEmptyValues.returnValueFor(collectionType);
 Assert.assertTrue(actualCollection instanceof LinkedList);
 Assert.assertTrue(actualCollection.isEmpty());
 }

 @Test
 public void testEmptySet() {
 Class<?> setType = HashSet.class;
 Object actualSet = returnsEmptyValues.returnValueFor(setType);
 Assert.assertTrue(actualSet instanceof HashSet);
 Assert.assertTrue(actualSet.isEmpty());
 }

 @Test
 public void testEmptySortedSet() {
 Class<?> sortedSetType = TreeSet.class;
 Object actualSortedSet = returnsEmptyValues.returnValueFor(sortedSetType);
 Assert.assertTrue(actualSortedSet instanceof TreeSet);
 Assert.assertTrue(actualSortedSet.isEmpty());
 }

 @Test
 public void testEmptyList() {
 Class<?> listType = ArrayList.class;
 Object actualList = returnsEmptyValues.returnValueFor(listType);
 Assert.assertTrue(actualList instanceof LinkedList);
 Assert.assertTrue(actualList.isEmpty());
 }

 @Test
 public void testEmptyLinkedList() {
 Class<?> linkedListType = LinkedList.class;
 Object actualLinkedList = returnsEmptyValues.returnValueFor(linkedListType);
 Assert.assertTrue(actualLinkedList instanceof LinkedList);
 Assert.assertTrue(actualLinkedList.isEmpty());
 }

 @Test
 public void testEmptyMap() {
 Class<?> mapType = HashMap.class;
 Object actualMap = returnsEmptyValues.returnValueFor(mapType);
 Assert.assertTrue(actualMap instanceof HashMap);
 Assert.assertTrue(actualMap.isEmpty());
 }

 @Test
 public void testEmptySortedMap() {
 Class<?> sortedMapType = TreeMap.class;
 Object actualSortedMap = returnsEmptyValues.returnValueFor(sortedMapType);
 Assert.assertTrue(actualSortedMap instanceof TreeMap);
 Assert.assertTrue(actualSortedMap.isEmpty());
 }

 @Test
 public void testEmptyLinkedHashMap() {
 Class<?> linkedHashMapType = LinkedHashMap.class;
 Object actualLinkedHashMap = returnsEmptyValues.returnValueFor(linkedHashMapType);
 Assert.assertTrue(actualLinkedHashMap instanceof LinkedHashMap);
 Assert.assertTrue(actualLinkedHashMap.isEmpty());
 }
}