import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;

public class ReturnsEmptyValuesTest {

  ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

  @Test
  public void testAnswer() {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Object result = returnsEmptyValues.answer(invocation);
    assertNull(result);
  }

  @Test
  public void testReturnValueForPrimitive() {
    Class<?> type = int.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(0, result);
  }

  @Test
  public void testReturnValueForPrimitiveWrapper() {
    Class<?> type = Integer.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(0, result);
  }

  @Test
  public void testReturnValueForCollection() {
    Class<?> type = Collection.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new LinkedList<Object>(), result);
  }

  @Test
  public void testReturnValueForSet() {
    Class<?> type = Set.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new HashSet<Object>(), result);
  }

  @Test
  public void testReturnValueForHashSet() {
    Class<?> type = HashSet.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new HashSet<Object>(), result);
  }

  @Test
  public void testReturnValueForSortedSet() {
    Class<?> type = SortedSet.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new TreeSet<Object>(), result);
  }

  @Test
  public void testReturnValueForTreeSet() {
    Class<?> type = TreeSet.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new TreeSet<Object>(), result);
  }

  @Test
  public void testReturnValueForLinkedHashSet() {
    Class<?> type = LinkedHashSet.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new LinkedHashSet<Object>(), result);
  }

  @Test
  public void testReturnValueForList() {
    Class<?> type = List.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new LinkedList<Object>(), result);
  }

  @Test
  public void testReturnValueForLinkedList() {
    Class<?> type = LinkedList.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new LinkedList<Object>(), result);
  }

  @Test
  public void testReturnValueForArrayList() {
    Class<?> type = ArrayList.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new ArrayList<Object>(), result);
  }

  @Test
  public void testReturnValueForMap() {
    Class<?> type = Map.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new HashMap<Object, Object>(), result);
  }

  @Test
  public void testReturnValueForHashMap() {
    Class<?> type = HashMap.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new HashMap<Object, Object>(), result);
  }

  @Test
  public void testReturnValueForSortedMap() {
    Class<?> type = SortedMap.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new TreeMap<Object, Object>(), result);
  }

  @Test
  public void testReturnValueForTreeMap() {
    Class<?> type = TreeMap.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new TreeMap<Object, Object>(), result);
  }

  @Test
  public void testReturnValueForLinkedHashMap() {
    Class<?> type = LinkedHashMap.class;
    Object result = returnsEmptyValues.returnValueFor(type);
    assertEquals(new LinkedHashMap<Object, Object>(), result);
  }
}