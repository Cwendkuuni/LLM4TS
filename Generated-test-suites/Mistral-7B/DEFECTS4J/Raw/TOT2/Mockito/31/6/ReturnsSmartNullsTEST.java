import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private Object mock;

    @Before
    public void setUp() {
        returnsSmartNulls = mock(ReturnsSmartNulls.class);
        mock = mock(Object.class);
    }

    @Test
    public void testAnswerMethodWithNullReturnValue() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod"));
        when(invocation.getTarget()).thenReturn(mock);

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerMethodWithNotNullReturnValue() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod"));
        when(invocation.getTarget()).thenReturn(mock);
        when(mock.toString()).thenReturn("Mock Object");

        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("Mock Object", result);
    }

    @Test
    public void testAnswerMethodWithToStringCall() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(ReturnsSmartNulls.class, "answer"));

        String result = (String) returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed answer() method on ReturnsSmartNulls", result);
    }

    @Test(expected = Throwable.class)
    public void testAnswerMethodWithThrowingCall() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(Object.class, "testThrowingMethod"));

        returnsSmartNulls.answer(invocation);
    }

    @Test
    public void testAnswerMethodWithFinalTypeReturnValue() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(Boolean.class, "valueOf", new Class[]{boolean.class}));

        Boolean result = (Boolean) returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerMethodWithNonMockableTypeReturnValue() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(String[].class, "clone"));

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
}