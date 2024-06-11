import org.junit.*;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

  @Test
  public void testAnswer() {
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    // Test toString() method
    InvocationOnMock invocationMock1 = new InvocationOnMock(mock(Object.class), mock(Object.class).getClass().getMethod("toString"), null);
    assertEquals("Mock for Object, hashCode: " + invocationMock1.getMock().hashCode(), returnsEmptyValues.answer(invocationMock1));

    // Test compareTo() method
    InvocationOnMock invocationMock2 = new InvocationOnMock(mock(Comparable.class), Comparable.class.getMethod("compareTo", Object.class), null);
    assertEquals(1, returnsEmptyValues.answer(invocationMock2));

    // Test primitive return type
    InvocationOnMock invocationMock3 = new InvocationOnMock(mock(Object.class), mock(Object.class).getClass().getMethod("hashCode"), null);
    assertEquals(0, returnsEmptyValues.answer(invocationMock3));

    // Test collection return type
    InvocationOnMock invocationMock4 = new InvocationOnMock(mock(Collection.class), Collection.class.getMethod("size"), null);
    assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(invocationMock4));

    InvocationOnMock invocationMock5 = new InvocationOnMock(mock(Set.class), Set.class.getMethod("size"), null);
    assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(invocationMock5));

    InvocationOnMock invocationMock6 = new InvocationOnMock(mock(HashSet.class), HashSet.class.getMethod("size"), null);
    assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(invocationMock6));

    InvocationOnMock invocationMock7 = new InvocationOnMock(mock(SortedSet.class), SortedSet.class.getMethod("size"), null);
    assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(invocationMock7));

    InvocationOnMock invocationMock8 = new InvocationOnMock(mock(TreeSet.class), TreeSet.class.getMethod("size"), null);
    assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(invocationMock8));

    InvocationOnMock invocationMock9 = new InvocationOnMock(mock(LinkedHashSet.class), LinkedHashSet.class.getMethod("size"), null);
    assertEquals(new LinkedHashSet<Object>(), returnsEmptyValues.answer(invocationMock9));

    InvocationOnMock invocationMock10 = new InvocationOnMock(mock(List.class), List.class.getMethod("size"), null);
    assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(invocationMock10));

    InvocationOnMock invocationMock11 = new InvocationOnMock(mock(LinkedList.class), LinkedList.class.getMethod("size"), null);
    assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(invocationMock11));

    InvocationOnMock invocationMock12 = new InvocationOnMock(mock(ArrayList.class), ArrayList.class.getMethod("size"), null);
    assertEquals(new ArrayList<Object>(), returnsEmptyValues.answer(invocationMock12));

    InvocationOnMock invocationMock13 = new InvocationOnMock(mock(Map.class), Map.class.getMethod("size"), null);
    assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(invocationMock13));

    InvocationOnMock invocationMock14 = new InvocationOnMock(mock(HashMap.class), HashMap.class.getMethod("size"), null);
    assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(invocationMock14));

    InvocationOnMock invocationMock15 = new InvocationOnMock(mock(SortedMap.class), SortedMap.class.getMethod("size"), null);
    assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(invocationMock15));

    InvocationOnMock invocationMock16 = new InvocationOnMock(mock(TreeMap.class), TreeMap.class.getMethod("size"), null);
    assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(invocationMock16));

    InvocationOnMock invocationMock17 = new InvocationOnMock(mock(LinkedHashMap.class), LinkedHashMap.class.getMethod("size"), null);
    assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.answer(invocationMock17));
  }

}