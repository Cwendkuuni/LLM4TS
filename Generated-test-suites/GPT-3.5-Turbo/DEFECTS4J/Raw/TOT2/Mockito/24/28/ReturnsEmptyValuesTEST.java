import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // Default toString for mock
        assertEquals("Mock for ReturnsEmptyValues, hashCode: " + emptyValues.hashCode(), emptyValues.answer(null));
    }

    @Test
    public void testAnswer_compareTo() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // compareTo implementation
        assertEquals(1, emptyValues.answer(null));
    }

    @Test
    public void testReturnValueFor_primitiveType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // Verify primitive type handling
        assertEquals(0, emptyValues.returnValueFor(int.class));
    }

    @Test
    public void testReturnValueFor_collectionType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // Verify collection type handling
        assertTrue(emptyValues.returnValueFor(Collection.class) instanceof LinkedList);
    }

    @Test
    public void testReturnValueFor_mapType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // Verify map type handling
        assertTrue(emptyValues.returnValueFor(Map.class) instanceof HashMap);
    }
}