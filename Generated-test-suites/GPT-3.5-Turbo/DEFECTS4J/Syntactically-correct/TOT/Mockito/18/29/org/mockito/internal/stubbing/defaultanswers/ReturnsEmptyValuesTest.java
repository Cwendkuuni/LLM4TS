import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswerToStringMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getDeclaredMethods()[0]);
        assertEquals("Mock for String, hashCode: " + invocation.getMock().hashCode(), returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswerCompareToMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);
        when(invocation.getMock()).thenReturn("mockObject");
        when(invocation.getArguments()).thenReturn(new Object[] { "mockObject" });

        assertEquals(0, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswerPrimitiveReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(PrimitiveMock.class.getDeclaredMethods()[0]);

        assertEquals(0, returnsEmptyValues.answer(invocation));
        assertEquals(false, returnsEmptyValues.answer(invocation));
        assertEquals('\u0000', returnsEmptyValues.answer(invocation));
        // repeat for other primitive types
    }
    
    @Test
    public void testAnswerCollectionReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(CollectionMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof LinkedList);
        assertEquals(0, ((List<?>) answer).size());
    }

    @Test
    public void testAnswerHashSetReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashSetMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof HashSet);
        assertEquals(0, ((HashSet<?>) answer).size());
    }    

    @Test
    public void testAnswerSortedSetReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedSetMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof TreeSet);
        assertEquals(0, ((TreeSet<?>) answer).size());
    }

    @Test
    public void testAnswerTreeSetReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeSetMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof TreeSet);
        assertEquals(0, ((TreeSet<?>) answer).size());
    }

    @Test
    public void testAnswerLinkedHashSetReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashSetMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof LinkedHashSet);
        assertEquals(0, ((LinkedHashSet<?>) answer).size());
    }

    @Test
    public void testAnswerListReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ListMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof LinkedList);
        assertEquals(0, ((List<?>) answer).size());
    }

    @Test
    public void testAnswerLinkedListReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedListMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof LinkedList);
        assertEquals(0, ((LinkedList<?>) answer).size());
    }

    @Test
    public void testAnswerArrayListReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ArrayListMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof ArrayList);
        assertEquals(0, ((ArrayList<?>) answer).size());
    }

    @Test
    public void testAnswerMapReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(MapMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof LinkedHashMap);
        assertEquals(0, ((Map<?, ?>) answer).size());
    }

    @Test
    public void testAnswerHashMapReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashMapMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof HashMap);
        assertEquals(0, ((HashMap<?, ?>) answer).size());
    }

    @Test
    public void testAnswerSortedMapReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedMapMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof LinkedHashMap);
        assertEquals(0, ((SortedMap<?, ?>) answer).size());
    }

    @Test
    public void testAnswerTreeMapReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeMapMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof TreeMap);
        assertEquals(0, ((TreeMap<?, ?>) answer).size());
    }

    @Test
    public void testAnswerLinkedHashMapReturnMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashMapMock.class.getDeclaredMethods()[0]);

        Object answer = returnsEmptyValues.answer(invocation);

        assertNotNull(answer);
        assertTrue(answer instanceof LinkedHashMap);
        assertEquals(0, ((LinkedHashMap<?, ?>) answer).size());
    }
    
    @Test
    public void testAnswerNullForOtherMethods() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(OtherMethod.class.getDeclaredMethods()[0]);

        assertNull(returnsEmptyValues.answer(invocation));
    }
}