import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(String.class));
    }

    @Test
    public void testAnswerCompareToMethod() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);
        Object result = answer.answer(invocation);
        assert(result.equals(1));
    }

    @Test
    public void testAnswerPrimitiveReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[0]);
        Object result = answer.answer(invocation);
        assert(result.equals(0));
    }

    @Test
    public void testAnswerPrimitiveWrapperReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[1]);
        Object result = answer.answer(invocation);
        assert(result.equals(0));
    }

    @Test
    public void testAnswerCollectionReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[2]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(LinkedList.class));
    }

    @Test
    public void testAnswerSetReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[3]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(HashSet.class));
    }

    @Test
    public void testAnswerHashSetReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[4]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(HashSet.class));
    }

    @Test
    public void testAnswerSortedSetReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[5]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(TreeSet.class));
    }

    @Test
    public void testAnswerTreeSetReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[6]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(TreeSet.class));
    }

    @Test
    public void testAnswerLinkedHashSetReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[7]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(LinkedHashSet.class));
    }

    @Test
    public void testAnswerListReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[8]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(LinkedList.class));
    }

    @Test
    public void testAnswerLinkedListReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[9]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(LinkedList.class));
    }

    @Test
    public void testAnswerArrayListReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[10]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(ArrayList.class));
    }

    @Test
    public void testAnswerMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[11]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(HashMap.class));
    }

    @Test
    public void testAnswerHashMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[12]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(HashMap.class));
    }

    @Test
    public void testAnswerSortedMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[13]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(TreeMap.class));
    }

    @Test
    public void testAnswerTreeMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[14]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(TreeMap.class));
    }

    @Test
    public void testAnswerLinkedHashMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[15]);
        Object result = answer.answer(invocation);
        assert(result.getClass().equals(LinkedHashMap.class));
    }
}