import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringDefaultName() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethod("toString"));
        Object result = values.answer(invocation);
        assertEquals("Mock for ObjectMethodsGuru, hashCode: " + invocation.getMock().hashCode(), result);
    }

    @Test
    public void testAnswerToStringCustomName() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethod("toString"));
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        Mockito.when(mockUtil.getMockName(invocation.getMock())).thenReturn(new MockName("customName"));
        Object result = values.answer(invocation);
        assertEquals("customName", result);
    }

    @Test
    public void testAnswerCompareToSameReference() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethod("compareTo", Object.class));
        Object[] arguments = new Object[]{invocation.getMock()};
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Object result = values.answer(invocation);
        assertEquals(0, result);
    }

    @Test
    public void testAnswerCompareToDifferentReference() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethod("compareTo", Object.class));
        Object[] arguments = new Object[]{new Object()};
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Object result = values.answer(invocation);
        assertEquals(1, result);
    }

    @Test
    public void testAnswerPrimitiveReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Primitives.class.getMethod("defaultValueForPrimitiveOrWrapper", Class.class));
        Object result = values.answer(invocation);
        assertEquals(0, result);
    }

    @Test
    public void testAnswerCollectionReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Collection.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof LinkedList<?>);
        assertTrue(((Collection<?>) result).isEmpty());
    }

    @Test
    public void testAnswerSetReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Set.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof HashSet<?>);
        assertTrue(((Set<?>) result).isEmpty());
    }

    @Test
    public void testAnswerHashSetReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(HashSet.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof HashSet<?>);
        assertTrue(((HashSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswerSortedSetReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(SortedSet.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof TreeSet<?>);
        assertTrue(((SortedSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswerTreeSetReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(TreeSet.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof TreeSet<?>);
        assertTrue(((TreeSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswerLinkedHashSetReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashSet.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof LinkedHashSet<?>);
        assertTrue(((LinkedHashSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswerListReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(List.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof LinkedList<?>);
        assertTrue(((List<?>) result).isEmpty());
    }

    @Test
    public void testAnswerLinkedListReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(LinkedList.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof LinkedList<?>);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }

    @Test
    public void testAnswerArrayListReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(ArrayList.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof ArrayList<?>);
        assertTrue(((ArrayList<?>) result).isEmpty());
    }

    @Test
    public void testAnswerMapReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Map.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof HashMap<?, ?>);
        assertTrue(((Map<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswerHashMapReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(HashMap.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof HashMap<?, ?>);
        assertTrue(((HashMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswerSortedMapReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(SortedMap.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof TreeMap<?, ?>);
        assertTrue(((SortedMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswerTreeMapReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(TreeMap.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof TreeMap<?, ?>);
        assertTrue(((TreeMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswerLinkedHashMapReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashMap.class);
        Object result = values.answer(invocation);
        assertTrue(result instanceof LinkedHashMap<?, ?>);
        assertTrue(((LinkedHashMap<?, ?>) result).isEmpty());
    }
    
}