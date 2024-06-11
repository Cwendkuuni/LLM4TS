import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testToStringReturnsMockDescription() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        Object result = answer.answer(invocation);
        
        Object mock = invocation.getMock();
        MockName name = mockUtil.getMockName(mock);
        String expected = name.isDefault() 
            ? "Mock for " + mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode()
            : name.toString();
        
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToMethodReturnsNonZero() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;

        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));

        Object result = answer.answer(invocation);

        assertEquals(1, result);
    }
    
    @Test
    public void testReturnValueForPrimitiveOrWrapperType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;

        when(invocation.getMethod()).thenReturn(Integer.class.getMethod("intValue"));
        Object result = answer.answer(invocation);
        assertEquals(0, result);

        when(invocation.getMethod()).thenReturn(int.class.getMethod("intValue"));
        result = answer.answer(invocation);
        assertEquals(0, result);
    }
    
    @Test
    public void testReturnValueForCollectionType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;

        when(invocation.getMethod()).thenReturn(Collection.class.getMethod("isEmpty"));
        Object result = answer.answer(invocation);
        assertTrue(result instanceof LinkedList<?>);

        when(invocation.getMethod()).thenReturn(Set.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof HashSet<?>);

        when(invocation.getMethod()).thenReturn(HashSet.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof HashSet<?>);

        when(invocation.getMethod()).thenReturn(SortedSet.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof TreeSet<?>);

        when(invocation.getMethod()).thenReturn(TreeSet.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof TreeSet<?>);

        when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof LinkedHashSet<?>);

        when(invocation.getMethod()).thenReturn(List.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof LinkedList<?>);

        when(invocation.getMethod()).thenReturn(LinkedList.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof LinkedList<?>);

        when(invocation.getMethod()).thenReturn(ArrayList.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof ArrayList<?>);
    }
    
    @Test
    public void testReturnValueForMapType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;

        when(invocation.getMethod()).thenReturn(Map.class.getMethod("isEmpty"));
        Object result = answer.answer(invocation);
        assertTrue(result instanceof HashMap<?, ?>);

        when(invocation.getMethod()).thenReturn(HashMap.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof HashMap<?, ?>);

        when(invocation.getMethod()).thenReturn(SortedMap.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof TreeMap<?, ?>);

        when(invocation.getMethod()).thenReturn(TreeMap.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof TreeMap<?, ?>);

        when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getMethod("isEmpty"));
        result = answer.answer(invocation);
        assertTrue(result instanceof LinkedHashMap<?, ?>);
    }
}