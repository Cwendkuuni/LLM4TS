import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;

import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_withNonNullDefaultReturnValue_shouldReturnDefaultReturnValue() throws Throwable {
        InvocationOnMock invocation = createMockInvocation();
        Object defaultReturnValue = new Object();
        setDefaultReturnValue(invocation, defaultReturnValue);

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testAnswer_withNullDefaultReturnValue_shouldReturnSmartNull() throws Throwable {
        InvocationOnMock invocation = createMockInvocation();
        setDefaultReturnValue(invocation, null);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof SmartNull);
    }

    @Test
    public void testAnswer_withNonMockableReturnType_shouldReturnNull() throws Throwable {
        InvocationOnMock invocation = createMockInvocation();
        setDefaultReturnValue(invocation, null);
        setReturnType(invocation, ReturnsSmartNulls.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswer_withToStringMethod_shouldReturnSmartNullWithMethodCall() throws Throwable {
        InvocationOnMock invocation = createMockInvocation();
        setDefaultReturnValue(invocation, null);
        setReturnType(invocation, String.class);
        setMethodName(invocation, "toString");

        Object result = returnsSmartNulls.answer(invocation);

        assertTrue(result instanceof String);
        assertTrue(result.toString().startsWith("SmartNull returned by unstubbed"));
        assertTrue(result.toString().endsWith("method on mock"));
    }
    
    private InvocationOnMock createMockInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        MockCreationSettings settings = Mockito.mock(MockCreationSettings.class);
        return new InvocationOnMock(invocation, settings);
    }

    private void setDefaultReturnValue(InvocationOnMock invocation, Object value) {
        Stubbing stubbing = Mockito.mock(Stubbing.class);
        Mockito.when(invocation.getInvocation()).thenReturn(stubbing);
        Mockito.when(stubbing.getDefaultReturnValue()).thenReturn(value);
    }

    private void setReturnType(InvocationOnMock invocation, Class<?> returnType) {
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(returnType);
    }

    private void setMethodName(InvocationOnMock invocation, String methodName) {
        Mockito.when(invocation.getMethod().getName()).thenReturn(methodName);
    }
}