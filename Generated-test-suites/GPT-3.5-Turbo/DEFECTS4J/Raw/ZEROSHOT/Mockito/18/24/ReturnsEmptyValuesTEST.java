import static org.mockito.Mockito.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        
        // Test toString() method
        InvocationOnMock toStringInvocation = mock(InvocationOnMock.class);
        when(toStringInvocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        String toStringResult = (String) emptyValues.answer(toStringInvocation);
        assertEquals("Mock for Object, hashCode: 0", toStringResult);
        
        // Test compareTo() method
        InvocationOnMock compareToInvocation = mock(InvocationOnMock.class);
        when(compareToInvocation.getMock()).thenReturn(new Object());
        when(compareToInvocation.getArguments()).thenReturn(new Object[] { new Object() });
        int compareToResult = (int) emptyValues.answer(compareToInvocation);
        assertEquals(1, compareToResult);
        
        // Test primitive wrapper class
        InvocationOnMock intInvocation = mock(InvocationOnMock.class);
        when(intInvocation.getMethod()).thenReturn(Integer.class.getDeclaredMethods()[0]);
        int intResult = (int) emptyValues.answer(intInvocation);
        assertEquals(0, intResult);
        
        InvocationOnMock booleanInvocation = mock(InvocationOnMock.class);
        when(booleanInvocation.getMethod()).thenReturn(Boolean.class.getDeclaredMethods()[0]);
        boolean booleanResult = (boolean) emptyValues.answer(booleanInvocation);
        assertFalse(booleanResult);
        
        InvocationOnMock charInvocation = mock(InvocationOnMock.class);
        when(charInvocation.getMethod()).thenReturn(Character.class.getDeclaredMethods()[0]);
        char charResult = (char) emptyValues.answer(charInvocation);
        assertEquals('\u0000', charResult);
        
        InvocationOnMock byteInvocation = mock(InvocationOnMock.class);
        when(byteInvocation.getMethod()).thenReturn(Byte.class.getDeclaredMethods()[0]);
        byte byteResult = (byte) emptyValues.answer(byteInvocation);
        assertEquals((byte)0, byteResult);
        
        InvocationOnMock shortInvocation = mock(InvocationOnMock.class);
        when(shortInvocation.getMethod()).thenReturn(Short.class.getDeclaredMethods()[0]);
        short shortResult = (short) emptyValues.answer(shortInvocation);
        assertEquals((short)0, shortResult);
        
        InvocationOnMock longInvocation = mock(InvocationOnMock.class);
        when(longInvocation.getMethod()).thenReturn(Long.class.getDeclaredMethods()[0]);
        long longResult = (long) emptyValues.answer(longInvocation);
        assertEquals(0L, longResult);
        
        InvocationOnMock floatInvocation = mock(InvocationOnMock.class);
        when(floatInvocation.getMethod()).thenReturn(Float.class.getDeclaredMethods()[0]);
        float floatResult = (float) emptyValues.answer(floatInvocation);
        assertEquals(0.0f, floatResult, 0.0001);
        
        InvocationOnMock doubleInvocation = mock(InvocationOnMock.class);
        when(doubleInvocation.getMethod()).thenReturn(Double.class.getDeclaredMethods()[0]);
        double doubleResult = (double) emptyValues.answer(doubleInvocation);
        assertEquals(0.0, doubleResult, 0.0001);
        
        // Test collection-returning methods
        InvocationOnMock collectionInvocation = mock(InvocationOnMock.class);
        when(collectionInvocation.getMethod()).thenReturn(Collection.class.getDeclaredMethods()[0]);
        Collection<Object> collectionResult = (Collection<Object>) emptyValues.answer(collectionInvocation);
        assertNotNull(collectionResult);
        assertTrue(collectionResult.isEmpty());
        
        // Test other types
        InvocationOnMock otherInvocation = mock(InvocationOnMock.class);
        when(otherInvocation.getMethod()).thenReturn(Void.class.getDeclaredMethods()[0]);
        Object otherResult = emptyValues.answer(otherInvocation);
        assertNull(otherResult);
    }
}