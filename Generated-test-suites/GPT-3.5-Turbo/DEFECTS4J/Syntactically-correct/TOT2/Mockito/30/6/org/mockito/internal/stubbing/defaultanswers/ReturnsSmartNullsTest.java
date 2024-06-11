import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsSmartNulls() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsSmartNulls;

        Object result = answer.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testAnswerReturnsSmartNullsForUnstubbedMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsSmartNulls;

        Object result = answer.answer(invocation);

        assertTrue(result.getClass().getName().contains("SmartNull"));
    }

    @Test
    public void testAnswerReturnsNoSmartNullsForNonNullReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsSmartNulls;
        ReturnsMoreEmptyValues delegate = new ReturnsMoreEmptyValues();

        // Stub the delegate to return a non-null value
        when(delegate.answer(invocation)).thenReturn(new Object());

        Object result = answer.answer(invocation);

        assertNotNull(result);
    }
}