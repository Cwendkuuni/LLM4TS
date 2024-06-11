import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_whenIsToString_thenMockDescriptionReturned() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        String description = (String) emptyValues.answer(mockito.invocation(new ObjectMethodsGuru().toStringMethod()), new Object());
        assertTrue(description.contains("Mock for "));
    }

    @Test
    public void testAnswer_whenIsCompareTo_thenNonZeroReturned() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        int result = (int) emptyValues.answer(mockito.invocation(new ObjectMethodsGuru().compareToMethod()), new Object());
        assertNotEquals(0, result);
    }

    @Test
    public void testAnswer_whenIsPrimitiveReturnType_thenDefaultValueReturned() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object result = emptyValues.returnValueFor(int.class);
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_whenIsCollectionReturnType_thenLinkedListReturned() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object result = emptyValues.returnValueFor(Collection.class);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testAnswer_whenIsSetReturnType_thenHashSetReturned() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object result = emptyValues.returnValueFor(Set.class);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testAnswer_whenIsMapReturnType_thenHashMapReturned() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object result = emptyValues.returnValueFor(Map.class);
        assertTrue(result instanceof HashMap);
    }
}