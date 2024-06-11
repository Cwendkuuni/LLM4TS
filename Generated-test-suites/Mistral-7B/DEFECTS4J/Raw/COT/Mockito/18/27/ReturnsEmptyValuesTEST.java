import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.*;
import static org.mockito.Mockito.*;;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setup() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswerToStringMethod() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(Object.class, "toString"));
 Object mock = Mockito.mock(Object.class);

 assertEquals("Mock for [class java.lang.Object], hashCode: " + mock.hashCode(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testAnswerCompareToMethod() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);

 assertEquals(0, returnsEmptyValues.answer(new InvocationOnMock(Mockito.method(Comparable.class, "compareTo", Object.class), mock1, new Object[0])).getArguments()[0]);
 assertEquals(1, returnsEmptyValues.answer(new InvocationOnMock(Mockito.method(Comparable.class, "compareTo", Object.class), mock1, mock2)).getArguments()[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAnswerUnknownMethod() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(Object.class, "unknownMethod", new Class[0]));

 try {
 returnsEmptyValues.answer(invocation);
 fail("IllegalArgumentException expected but not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testReturnValueForPrimitiveTypes() {
 assertEquals(0, returnsEmptyValues.returnValueFor(int.class).hashCode());
 assertEquals(0L, returnsEmptyValues.returnValueFor(long.class).hashCode());
 assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class).booleanValue());
 }

 @Test
 public void testReturnValueForCollectionTypes() {
 List<Object> list = (List<Object>) returnsEmptyValues.returnValueFor(List.class);
 Set<Object> set = (Set<Object>) returnsEmptyValues.returnValueFor(Set.class);
 HashSet<Object> hashSet = (HashSet<Object>) returnsEmptyValues.returnValueFor(HashSet.class);
 LinkedHashSet<Object> linkedHashSet = (LinkedHashSet<Object>) returnsEmptyValues.returnValueFor(LinkedHashSet.class);
 SortedSet<Object> sortedSet = (SortedSet<Object>) returnsEmptyValues.returnValueFor(SortedSet.class);
 TreeSet<Object> treeSet = (TreeSet<Object>) returnsEmptyValues.returnValueFor(TreeSet.class);

 assertTrue(list.isEmpty());
 assertTrue(set.isEmpty());
 assertTrue(hashSet.isEmpty());
 assertTrue(linkedHashSet.isEmpty());
 assertTrue(sortedSet.isEmpty());
 assertTrue(treeSet.isEmpty());
 }

 @Test
 public void testReturnValueForMapTypes() {
 Map<Object, Object> map = (Map<Object, Object>) returnsEmptyValues.returnValueFor(Map.class);
 HashMap<Object, Object> hashMap = (HashMap<Object, Object>) returnsEmptyValues.returnValueFor(HashMap.class);
 LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);
 SortedMap<Object, Object> sortedMap = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(SortedMap.class);
 TreeMap<Object, Object> treeMap = (TreeMap<Object, Object>) returnsEmptyValues.returnValueFor(TreeMap.class);

 assertTrue(map.isEmpty());
 assertTrue(hashMap.isEmpty());
 assertTrue(linkedHashMap.isEmpty());
 assertTrue(sortedMap.isEmpty());
 assertTrue(treeMap.isEmpty());
 }

}