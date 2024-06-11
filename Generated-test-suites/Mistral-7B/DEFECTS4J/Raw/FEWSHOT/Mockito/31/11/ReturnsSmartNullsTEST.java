import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    @Test
    public void testReturnsSmartNullsForVoidMethod() {
        Answer<Object> returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Runnable target = mock(Runnable.class, returnsSmartNulls);

        target.run();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(Void.TYPE);

        ReturnsSmartNulls returnsSmartNullsInstance = new ReturnsSmartNulls();
        Object answer = returnsSmartNullsInstance.answer(invocation);
        assertNotNull(answer);
        assertEquals("SmartNull returned by unstubbed run() method on mock", answer.toString());
    }

    @Test
    public void testReturnsSmartNullsForFinalClass() {
        final String FINAL_STRING = "final string";
        Answer<Object> returnsSmartNulls = mock(ReturnsSmartNulls.class);

        String target = FINAL_STRING;

        String result = (String) returnsSmartNulls.answer(mock(InvocationOnMock.class).withMethodReturnType(String.class).withArguments(target));
        assertNull(result);
    }
}