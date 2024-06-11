import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.MockUtil;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
    private ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testAnswerToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(methodsGuru.isToString(invocation.getMethod())).thenReturn(true);
        Object mock = mock(Object.class);
        MockName name = mockUtil.getMockName(mock);
        when(mockUtil.getMockName(mock)).thenReturn(name);
        when(name.isDefault()).thenReturn(true);
        when(mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName()).thenReturn("TypeToMock");
        when(mock.hashCode()).thenReturn(1234);

        Object result = returnValue.answer(invocation);

        assertEquals("Mock for TypeToMock, hashCode: 1234", result);
        verify(methodsGuru).isToString(invocation.getMethod());
        verify(mockUtil).getMockName(mock);
        verify(name).isDefault();
        verify(mockUtil).getMockSettings(mock);
        verify(mockUtil.getMockSettings(mock)).getTypeToMock().getSimpleName();
        verify(mock).hashCode();
    }

    @Test
    public void testAnswerCompareToMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(methodsGuru.isCompareToMethod(invocation.getMethod())).thenReturn(true);

        Object result = returnValue.answer(invocation);

        assertEquals(1, result);
        verify(methodsGuru).isCompareToMethod(invocation.getMethod());
    }

    @Test
    public void testAnswerPrimitiveReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);

        Object result = returnValue.answer(invocation);

        assertEquals(0, result);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerWrapperReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);

        Object result = returnValue.answer(invocation);

        assertEquals(0, result);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerCollectionReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Collection.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerSetReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Set.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerHashSetReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(HashSet.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerSortedSetReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(SortedSet.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerTreeSetReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(TreeSet.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerLinkedHashSetReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashSet.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerListReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(List.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerLinkedListReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedList.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerArrayListReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(ArrayList.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerMapReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Map.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerHashMapReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(HashMap.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerSortedMapReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(SortedMap.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerTreeMapReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(TreeMap.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        verify(invocation.getMethod()).getReturnType();
    }

    @Test
    public void testAnswerLinkedHashMapReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashMap.class);

        Object result = returnValue.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        verify(invocation.getMethod()).getReturnType();
    }
}