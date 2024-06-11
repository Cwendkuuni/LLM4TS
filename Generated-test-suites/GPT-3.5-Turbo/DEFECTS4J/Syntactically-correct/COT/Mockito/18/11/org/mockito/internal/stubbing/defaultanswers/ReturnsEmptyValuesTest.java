import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // Test isToString method
        assertEquals("Mock for Object, hashCode: 0", returnsEmptyValues.answer(null));

        // Test isCompareToMethod method
        assertEquals(0, returnsEmptyValues.answer(null));

        // Test returnType method
        assertNull(returnsEmptyValues.returnValueFor(String.class));
        assertEquals(Boolean.FALSE, returnsEmptyValues.returnValueFor(Boolean.class));
        assertEquals(Character.valueOf('\u0000'), returnsEmptyValues.returnValueFor(Character.class));
        assertEquals(Byte.valueOf((byte)0), returnsEmptyValues.returnValueFor(Byte.class));
        assertEquals(Short.valueOf((short)0), returnsEmptyValues.returnValueFor(Short.class));
        assertEquals(Integer.valueOf(0), returnsEmptyValues.returnValueFor(Integer.class));
        assertEquals(Long.valueOf(0), returnsEmptyValues.returnValueFor(Long.class));
        assertEquals(Float.valueOf(0), returnsEmptyValues.returnValueFor(Float.class));
        assertEquals(Double.valueOf(0), returnsEmptyValues.returnValueFor(Double.class));

        // Test collection types
        assertTrue(returnsEmptyValues.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(returnsEmptyValues.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(ArrayList.class) instanceof ArrayList);
        assertTrue(returnsEmptyValues.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);

        // Test unsupported type
        assertNull(returnsEmptyValues.returnValueFor(Object.class));
    }

}