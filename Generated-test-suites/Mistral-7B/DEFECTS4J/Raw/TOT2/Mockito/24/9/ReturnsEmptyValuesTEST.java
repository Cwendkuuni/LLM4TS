import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TestMethod1 {
    @Test
    public void testAnswerMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(Object.class);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(new MockitoMethod("toString", null, Object.class));

        String result = (String) returnsEmptyValues.answer(invocation);

        verify(mock).toString();
        verifyNoMoreInteractions(mock);
        assertEquals("Mock for [mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues, hashCode: " + mock.hashCode() + "]", result);
    }

    @Test
    public void testReturnValueForPrimitive() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        byte result = (byte) returnsEmptyValues.returnValueFor(Byte.TYPE);

        assertEquals((byte) 0, result);
    }

    @Test
    public void testReturnValueForCollection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        List<String> result = (List<String>) returnsEmptyValues.returnValueFor(List.class);

        assertTrue(result.isEmpty());
    }
}