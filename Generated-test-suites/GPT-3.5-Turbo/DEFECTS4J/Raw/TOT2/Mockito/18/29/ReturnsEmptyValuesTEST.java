import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import java.util.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        String result = (String) emptyValues.answer(null);
        assertNotNull(result);
        assertTrue(result.contains("Mock for "));
    }

    @Test
    public void testCompareTo() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        int result = (int) emptyValues.answer(null);
        assertTrue(result == 1 || result == 0);
    }

    @Test
    public void testReturnValueForPrimitives() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        int primitiveResult = (int) emptyValues.returnValueFor(int.class);
        assertEquals(0, primitiveResult);
    }

    @Test
    public void testReturnValueForCollections() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Collection<Object> collectionResult = (Collection<Object>) emptyValues.returnValueFor(Collection.class);
        assertNotNull(collectionResult);
        assertTrue(collectionResult instanceof LinkedList);
    }

    @Test
    public void testReturnValueForSets() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Set<Object> setResult = (Set<Object>) emptyValues.returnValueFor(Set.class);
        assertNotNull(setResult);
        assertTrue(setResult instanceof HashSet);
    }

    @Test
    public void testReturnValueForMaps() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Map<Object, Object> mapResult = (Map<Object, Object>) emptyValues.returnValueFor(Map.class);
        assertNotNull(mapResult);
        assertTrue(mapResult instanceof HashMap);
    }
}