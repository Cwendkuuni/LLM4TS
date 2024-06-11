import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("toString"));
        when(invocation.getMock()).thenReturn("mockObject");

        Object result = answer.answer(invocation);

        assertEquals("Mock for String, hashCode: mockObject".toString(), result);
    }

    @Test
    public void testAnswerCompareToWithEqualArguments() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        when(invocation.getMock()).thenReturn("mockObject");
        when(invocation.getArguments()).thenReturn(new Object[]{"mockObject"});

        Object result = answer.answer(invocation);

        assertEquals(0, result);
    }

    @Test
    public void testAnswerCompareToWithDifferentArguments() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        when(invocation.getMock()).thenReturn("mockObject");
        when(invocation.getArguments()).thenReturn(new Object[]{"otherObject"});

        Object result = answer.answer(invocation);

        assertEquals(1, result);
    }

    @Test
    public void testAnswerWithPrimitiveReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Integer.class.getMethod("intValue"));

        Object result = answer.answer(invocation);

        assertEquals(0, result);
    }

    @Test
    public void testAnswerWithWrapperClassReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Integer.class.getMethod("valueOf", int.class));

        Object result = answer.answer(invocation);

        assertEquals(0, result);
    }

    @Test
    public void testAnswerWithCollectionReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Collection.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithSetReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Set.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithHashSetReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashSet.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithSortedSetReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedSet.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithTreeSetReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeSet.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithLinkedHashSetReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithListReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(List.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithLinkedListReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedList.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithArrayListReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ArrayList.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Map.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithHashMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashMap.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithSortedMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedMap.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithTreeMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeMap.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }

    @Test
    public void testAnswerWithLinkedHashMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertTrue((Boolean) result);
    }
}