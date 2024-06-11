import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_ReturnsMockDescriptionInToStringMethod() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // mock toString() method should return mock description
        assertEquals("Mock for ReturnsEmptyValues, hashCode: " + emptyValues.hashCode(), emptyValues.answer(mockitoInvocation));
    }

    @Test
    public void testAnswer_ReturnsNonZeroForCompareToMethod() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // compareTo() method should return a non-zero value
        assertEquals(1, emptyValues.answer(mockitoInvocation));
    }

    @Test
    public void testAnswer_ReturnsDefaultForPrimitiveMethods() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // primitive or wrapper class methods should return default values
        assertEquals(0, emptyValues.answer(mockitoInvocation));
    }

    @Test
    public void testAnswer_ReturnsEmptyCollectionForCollectionMethods() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // collection methods should return empty collections
        assertEquals(new LinkedList<>(), emptyValues.answer(mockitoInvocation));
    }

    @Test
    public void testReturnValueFor_ReturnsEmptyCollectionForCollectionClass() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // returnValueFor method should return empty collection for Collection class
        assertEquals(new LinkedList<>(), emptyValues.returnValueFor(Collection.class));
    }

    @Test
    public void testReturnValueFor_ReturnsEmptySetForSetClass() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // returnValueFor method should return empty set for Set class
        assertEquals(new HashSet<>(), emptyValues.returnValueFor(Set.class));
    }

    @Test
    public void testReturnValueFor_ReturnsEmptyMapForMapClass() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // returnValueFor method should return empty map for Map class
        assertEquals(new HashMap<>(), emptyValues.returnValueFor(Map.class));
    }

}