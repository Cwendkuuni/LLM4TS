import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // Test isToString method
        Object mock = new Object();
        String string = (String) returnsEmptyValues.answer(new InvocationOnMock(mock, mock.getClass().getMethod("toString"), new Object[0]));
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), string);

        // Test isCompareToMethod method
        Integer integer = 1;
        int result = (int) returnsEmptyValues.answer(new InvocationOnMock(integer, integer.getClass().getMethod("compareTo", Object.class), new Object[]{integer}));
        assertEquals(0, result);

        // Test returnValueFor method
        // Primitives
        assertNull(returnsEmptyValues.returnValueFor(Void.class));
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
        assertEquals(0L, returnsEmptyValues.returnValueFor(long.class));
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(float.class));
        assertEquals(0.0, returnsEmptyValues.returnValueFor(double.class));
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class));
        assertEquals('\u0000', returnsEmptyValues.returnValueFor(char.class));
        // Primitive Wrappers
        assertNull(returnsEmptyValues.returnValueFor(Void.class));
        assertEquals(0, returnsEmptyValues.returnValueFor(Integer.class));
        assertEquals(0L, returnsEmptyValues.returnValueFor(Long.class));
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(Float.class));
        assertEquals(0.0, returnsEmptyValues.returnValueFor(Double.class));
        assertEquals(false, returnsEmptyValues.returnValueFor(Boolean.class));
        assertEquals('\u0000', returnsEmptyValues.returnValueFor(Character.class));
        // Collections
        assertNotNull(returnsEmptyValues.returnValueFor(Collection.class));
        assertNotNull(returnsEmptyValues.returnValueFor(Set.class));
        assertNotNull(returnsEmptyValues.returnValueFor(HashSet.class));
        assertNotNull(returnsEmptyValues.returnValueFor(SortedSet.class));
        assertNotNull(returnsEmptyValues.returnValueFor(TreeSet.class));
        assertNotNull(returnsEmptyValues.returnValueFor(LinkedHashSet.class));
        assertNotNull(returnsEmptyValues.returnValueFor(List.class));
        assertNotNull(returnsEmptyValues.returnValueFor(LinkedList.class));
        assertNotNull(returnsEmptyValues.returnValueFor(ArrayList.class));
        assertNotNull(returnsEmptyValues.returnValueFor(Map.class));
        assertNotNull(returnsEmptyValues.returnValueFor(HashMap.class));
        assertNotNull(returnsEmptyValues.returnValueFor(SortedMap.class));
        assertNotNull(returnsEmptyValues.returnValueFor(TreeMap.class));
        assertNotNull(returnsEmptyValues.returnValueFor(LinkedHashMap.class));
    }

}