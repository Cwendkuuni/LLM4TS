import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private InvocationOnMock invocation;

    @Before
    public void setUp() {
        // Create the instance of ReturnsSmartNulls
        returnsSmartNulls = new ReturnsSmartNulls();

        // Create a mock InvocationOnMock for testing
        invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);
    }

    @Test
    public void answer_DefaultReturnValueNotNull_ReturnDefaultReturnValue() throws Throwable {
        // Set up
        Object defaultReturnValue = "default";
        when(delegate.answer(invocation)).thenReturn(defaultReturnValue);

        // Test
        Object actualResult = returnsSmartNulls.answer(invocation);

        // Verify
        assertEquals(defaultReturnValue, actualResult);
        verify(delegate).answer(invocation);
    }

    @Test
    public void answer_DefaultReturnValueNull_MockFinalClass() throws Throwable {
        // Set up
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(FinalClass.class);
        when(ClassImposterizer.INSTANCE.canImposterise(FinalClass.class)).thenReturn(true);

        // Test
        Object actualResult = returnsSmartNulls.answer(invocation);

        // Verify
        assertTrue(actualResult instanceof FinalClass);
        verify(delegate).answer(invocation);
        verify(ClassImposterizer.INSTANCE).canImposterise(FinalClass.class);
        verify(ClassImposterizer.INSTANCE).imposterise(any(ThrowsInterceptor.class), eq(FinalClass.class));
    }

    @Test
    public void answer_DefaultReturnValueNull_MethodToString() throws Throwable {
        // Set up
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getName()).thenReturn("toString");
        when(invocation.getArguments()).thenReturn(new Object[]{});

        // Test
        Object actualResult = returnsSmartNulls.answer(invocation);

        // Verify
        assertTrue(actualResult instanceof String);
        assertTrue(((String) actualResult).startsWith("SmartNull returned by unstubbed"));
        verify(delegate).answer(invocation);
    }

    @Test(expected = NullPointerException.class)
    public void answer_DefaultReturnValueNull_NotMockableReturnType() throws Throwable {
        // Set up
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(NotMockableClass.class);
        when(ClassImposterizer.INSTANCE.canImposterise(NotMockableClass.class)).thenReturn(false);

        // Test
        returnsSmartNulls.answer(invocation);
    }
    
    // Additional test cases can be added for edge cases and error scenarios

}