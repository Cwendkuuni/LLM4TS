
import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }
    
    @Test
    public void testAnswer_toString() {
        assertEquals("Mock for Object, hashCode: 0", returnsEmptyValues.answer(invocationMock));
    }
    
    @Test
    public void testAnswer_compareTo() {
        assertEquals(1, returnsEmptyValues.answer(invocationMock));
    }
    
    @Test
    public void testAnswer_primitiveTypes() {
        assertEquals(0, returnsEmptyValues.answer(invocationMock)); // int
        assertEquals(0L, returnsEmptyValues.answer(invocationMock)); // long
        assertEquals(0.0f, returnsEmptyValues.answer(invocationMock), 0.0); // float
        assertEquals(0.0, returnsEmptyValues.answer(invocationMock), 0.0); // double
        assertEquals((byte) 0, returnsEmptyValues.answer(invocationMock)); // byte
        assertEquals((short) 0, returnsEmptyValues.answer(invocationMock)); // short
        assertEquals('0', returnsEmptyValues.answer(invocationMock)); // char
        assertEquals(false, returnsEmptyValues.answer(invocationMock)); // boolean
    }
    
    @Test
    public void testAnswer_wrapperTypes() {
        assertEquals(Integer.valueOf(0), returnsEmptyValues.answer(invocationMock)); // Integer
        assertEquals(Long.valueOf(0L), returnsEmptyValues.answer(invocationMock)); // Long
        assertEquals(Float.valueOf(0.0f), returnsEmptyValues.answer(invocationMock)); // Float
        assertEquals(Double.valueOf(0.0), returnsEmptyValues.answer(invocationMock)); // Double
        assertEquals(Byte.valueOf((byte) 0), returnsEmptyValues.answer(invocationMock)); // Byte
        assertEquals(Short.valueOf((short) 0), returnsEmptyValues.answer(invocationMock)); // Short
        assertEquals(Character.valueOf('0'), returnsEmptyValues.answer(invocationMock)); // Character
        assertEquals(Boolean.FALSE, returnsEmptyValues.answer(invocationMock)); // Boolean
    }
    
    @Test
    public void testAnswer_collectionTypes() {
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof LinkedList); // Collection
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof HashSet); // Set
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof HashSet); // HashSet
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof TreeSet); // SortedSet
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof TreeSet); // TreeSet
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof LinkedHashSet); // LinkedHashSet
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof LinkedList); // List
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof LinkedList); // LinkedList
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof ArrayList); // ArrayList
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof HashMap); // Map
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof HashMap); // HashMap
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof TreeMap); // SortedMap
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof TreeMap); // TreeMap
        assertTrue(returnsEmptyValues.answer(invocationMock) instanceof LinkedHashMap); // LinkedHashMap
    }
    
    @Test
    public void testAnswer_otherTypes() {
        assertTrue(returnsEmptyValues.answer(invocationMock) == null);
    }
}
