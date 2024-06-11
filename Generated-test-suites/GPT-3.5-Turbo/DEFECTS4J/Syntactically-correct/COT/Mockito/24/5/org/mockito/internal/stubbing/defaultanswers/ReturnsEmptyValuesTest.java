import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        String expected = "Mock for ReturnsEmptyValues, hashCode: " + answer.hashCode();
        assertEquals(expected, answer.answer(null));
    }

    @Test
    public void testAnswer_compareTo() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        int expected = 1;
        assertEquals(expected, answer.answer(null));
    }

    @Test
    public void testReturnValueFor_primitiveTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        assertNull(answer.returnValueFor(void.class));
        assertEquals(false, answer.returnValueFor(boolean.class));
        assertEquals((byte)0, answer.returnValueFor(byte.class));
        assertEquals((short)0, answer.returnValueFor(short.class));
        assertEquals((int)0, answer.returnValueFor(int.class));
        assertEquals((long)0, answer.returnValueFor(long.class));
        assertEquals((float)0, answer.returnValueFor(float.class), 0);
        assertEquals((double)0, answer.returnValueFor(double.class), 0);
        assertEquals('\0', answer.returnValueFor(char.class));
    }

    @Test
    public void testReturnValueFor_wrapperClasses() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        assertNull(answer.returnValueFor(Void.class));
        assertEquals(false, answer.returnValueFor(Boolean.class));
        assertEquals((byte)0, answer.returnValueFor(Byte.class));
        assertEquals((short)0, answer.returnValueFor(Short.class));
        assertEquals((int)0, answer.returnValueFor(Integer.class));
        assertEquals((long)0, answer.returnValueFor(Long.class));
        assertEquals((float)0, answer.returnValueFor(Float.class), 0);
        assertEquals((double)0, answer.returnValueFor(Double.class), 0);
        assertEquals('\0', answer.returnValueFor(Character.class));
    }

    @Test
    public void testReturnValueFor_collectionTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        assertTrue(answer.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(answer.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(answer.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(answer.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(answer.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(answer.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(answer.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(answer.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(answer.returnValueFor(ArrayList.class) instanceof ArrayList);
        assertTrue(answer.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(answer.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(answer.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(answer.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(answer.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);

        // TODO add test case for empty Iterable
    }

}