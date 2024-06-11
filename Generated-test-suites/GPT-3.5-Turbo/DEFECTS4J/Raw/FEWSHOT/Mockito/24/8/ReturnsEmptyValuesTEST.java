import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for Object, hashCode: 0", answer.answer(invocationOnMock("toString")));
        
        // Test compareTo() method
        assertEquals(1, answer.answer(invocationOnMock("compareTo")));
        
        // Test primitive and wrapper classes return values
        assertEquals(false, answer.answer(invocationOnMock("booleanReturningMethod")));
        assertEquals((byte) 0, answer.answer(invocationOnMock("byteReturningMethod")));
        assertEquals((short) 0, answer.answer(invocationOnMock("shortReturningMethod")));
        assertEquals((char) 0, answer.answer(invocationOnMock("charReturningMethod")));
        assertEquals(0, answer.answer(invocationOnMock("intReturningMethod")));
        assertEquals(0L, answer.answer(invocationOnMock("longReturningMethod")));
        assertEquals(0.0f, answer.answer(invocationOnMock("floatReturningMethod")));
        assertEquals(0.0d, answer.answer(invocationOnMock("doubleReturningMethod")));
        assertNull(answer.answer(invocationOnMock("objectReturningMethod")));
        
        // Test collection return values
        assertEquals(new LinkedList<Object>(), answer.answer(invocationOnMock("collectionReturningMethod")));
        assertEquals(new HashSet<Object>(), answer.answer(invocationOnMock("setReturningMethod")));
        assertEquals(new TreeSet<Object>(), answer.answer(invocationOnMock("sortedSetReturningMethod")));
        assertEquals(new LinkedHashSet<Object>(), answer.answer(invocationOnMock("linkedHashSetReturningMethod")));
        assertEquals(new ArrayList<Object>(), answer.answer(invocationOnMock("listReturningMethod")));
        assertEquals(new HashMap<Object, Object>(), answer.answer(invocationOnMock("mapReturningMethod")));
        assertEquals(new TreeMap<Object, Object>(), answer.answer(invocationOnMock("sortedMapReturningMethod")));
        assertEquals(new LinkedHashMap<Object, Object>(), answer.answer(invocationOnMock("linkedHashMapReturningMethod")));
        
        // TODO Test Iterable return value
        
        // Test null return value for other methods
        assertNull(answer.answer(invocationOnMock("otherMethod")));
    }
    
    private InvocationOnMock invocationOnMock(String methodName) {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod(methodName));
        return invocation;
    }
    
    private Method getMethod(String methodName) {
        Method method = null;
        try {
            method = ReturnsEmptyValues.class.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }
}