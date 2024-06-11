import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {
    
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    
    @Test
    public void testAnswerToString() {
        InvocationOnMock invocation = new InvocationOnMock();
        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof String);
    }
    
    @Test
    public void testAnswerCompareTo() {
        InvocationOnMock invocation = new InvocationOnMock();
        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }
    
    @Test
    public void testAnswerPrimitiveOrWrapper() {
        InvocationOnMock invocation = new InvocationOnMock();
        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }
    
    @Test
    public void testReturnValueForCollection() {
        Object result = returnsEmptyValues.returnValueFor(Collection.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }
    
    @Test
    public void testReturnValueForSet() {
        Object result = returnsEmptyValues.returnValueFor(Set.class);
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
    }
    
    @Test
    public void testReturnValueForMap() {
        Object result = returnsEmptyValues.returnValueFor(Map.class);
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
    }
    
    @Test
    public void testReturnValueForSortedMap() {
        Object result = returnsEmptyValues.returnValueFor(SortedMap.class);
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
    }
    
    @Test
    public void testReturnValueForInvalidType() {
        Object result = returnsEmptyValues.returnValueFor(String.class);
        assertNull(result);
    }
}