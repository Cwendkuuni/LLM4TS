import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        String expected = "Mock for ReturnsEmptyValues, hashCode: " + answer.hashCode();
        assertEquals(expected, answer.answer(null));
    }
    
    @Test
    public void testAnswerCompareTo() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Object mock = new Object();
        Object[] args = { mock };
        assertEquals(1, answer.answer(invocationWithArgs(args)));
        assertEquals(0, answer.answer(invocationWithArgs(mock)));
    }
    
    @Test
    public void testAnswerPrimitiveReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        assertEquals((byte) 0, answer.answer(invocationWithReturnType(byte.class)));
        assertEquals((short) 0, answer.answer(invocationWithReturnType(short.class)));
        assertEquals(0, answer.answer(invocationWithReturnType(int.class)));
        assertEquals(0L, answer.answer(invocationWithReturnType(long.class)));
        assertEquals(0.0f, answer.answer(invocationWithReturnType(float.class)));
        assertEquals(0.0, answer.answer(invocationWithReturnType(double.class)));
        assertEquals(false, answer.answer(invocationWithReturnType(boolean.class)));
        assertEquals('\0', answer.answer(invocationWithReturnType(char.class)));
        
        assertEquals(0, answer.answer(invocationWithReturnType(Byte.class)));
        assertEquals(0, answer.answer(invocationWithReturnType(Short.class)));
        assertEquals(0, answer.answer(invocationWithReturnType(Integer.class)));
        assertEquals(0L, answer.answer(invocationWithReturnType(Long.class)));
        assertEquals(0.0f, answer.answer(invocationWithReturnType(Float.class)));
        assertEquals(0.0, answer.answer(invocationWithReturnType(Double.class)));
        assertEquals(false, answer.answer(invocationWithReturnType(Boolean.class)));
        assertEquals('\0', answer.answer(invocationWithReturnType(Character.class)));
    }
    
    @Test
    public void testAnswerCollectionReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        assertEquals(new LinkedList<Object>(), answer.answer(invocationWithReturnType(Collection.class)));
        assertEquals(new HashSet<Object>(), answer.answer(invocationWithReturnType(Set.class)));
        assertEquals(new HashSet<Object>(), answer.answer(invocationWithReturnType(HashSet.class)));
        assertEquals(new TreeSet<Object>(), answer.answer(invocationWithReturnType(SortedSet.class)));
        assertEquals(new TreeSet<Object>(), answer.answer(invocationWithReturnType(TreeSet.class)));
        assertEquals(new LinkedHashSet<Object>(), answer.answer(invocationWithReturnType(LinkedHashSet.class)));
        assertEquals(new LinkedList<Object>(), answer.answer(invocationWithReturnType(List.class)));
        assertEquals(new LinkedList<Object>(), answer.answer(invocationWithReturnType(LinkedList.class)));
        assertEquals(new ArrayList<Object>(), answer.answer(invocationWithReturnType(ArrayList.class)));
    }
    
    @Test
    public void testAnswerMapReturnType() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        assertEquals(new HashMap<Object, Object>(), answer.answer(invocationWithReturnType(Map.class)));
        assertEquals(new HashMap<Object, Object>(), answer.answer(invocationWithReturnType(HashMap.class)));
        assertEquals(new TreeMap<Object, Object>(), answer.answer(invocationWithReturnType(SortedMap.class)));
        assertEquals(new TreeMap<Object, Object>(), answer.answer(invocationWithReturnType(TreeMap.class)));
        assertEquals(new LinkedHashMap<Object, Object>(), answer.answer(invocationWithReturnType(LinkedHashMap.class)));
    }
    
    private InvocationOnMock invocationWithArgs(Object... args) {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getArguments()).thenReturn(args);
        return invocation;
    }
    
    private InvocationOnMock invocationWithReturnType(Class<?> returnType) {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(new MethodStub(returnType));
        return invocation;
    }
    
    private class MethodStub implements Method {
        private Class<?> returnType;
        
        public MethodStub(Class<?> returnType) {
            this.returnType = returnType;
        }
        
        public Class<?> getReturnType() {
            return returnType;
        }
    }
}