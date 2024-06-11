import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        // Test basic functionality of the answer
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        assertNull(smartNulls.answer(invocationMock));

        // Test that ordinary return values are returned when available
        Answer<Object> delegateMock = mock(Answer.class);
        when(delegateMock.answer(invocationMock)).thenReturn("returnValue");
        assertEquals("returnValue", smartNulls.answer(invocationMock));

        // Test that SmartNulls are returned
        Class<?> returnType = Object.class;
        ClassImposterizer imposterizerMock = mock(ClassImposterizer.class);
        when(imposterizerMock.canImposterise(returnType)).thenReturn(true);
        when(imposterizerMock.imposterise(any(ReturnsSmartNulls.ThrowingInterceptor.class), eq(returnType))).thenReturn("SmartNull");
        ClassImposterizer originalImposterizer = ClassImposterizer.INSTANCE;
        ClassImposterizer.INSTANCE = imposterizerMock;

        assertEquals("SmartNull", smartNulls.answer(invocationMock));

        ClassImposterizer.INSTANCE = originalImposterizer;
    }
}