package org.mockito.internal.stubbing.answers.test;

import org.junit.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

public class CallsRealMethodsTest {

    private CallsRealMethods callable;
    private Object realObject;

    @Before
    public void setup() {
        realObject = new Object() {
            public String method1() {
                return "real method result";
            }
        };
        callable = new CallsRealMethods();
        doNothing().when(callable).answer(any(InvocationOnMock.class));
    }

    @Test
    public void testCallsRealMethod() {
        String result = callable.answer(mock(InvocationOnMock.class));
        InvocationOnMock invocation = argThat(invocation -> invocation.getTarget() == realObject);
        verify(realObject).method1();
        assertNotNull(result);
        assertEquals("real method result", result);
    }

    @Test
    public void testCallsRealMethodWithThrowable() throws Exception {
        callable = new CallsRealMethods();
        Object realObject = new Object() {
            public String method1() throws Exception {
                throw new RuntimeException("Test Exception");
            }
        };
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(realObject.getClass().getMethod("method1"));
        doThrow(new RuntimeException("Test Exception from Test")).when(callable).answer(eq(invocation));

        try {
            callable.answer(invocation);
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Test Exception from Test", e.getMessage());
            assertEquals("Test Exception", realObject.toString());
        }
    }

    @Test
    public void testCallsRealMethodWithExceptionInRealObject() {
        callable = new CallsRealMethods();
        Object realObject = new Object() {
            public String method1() throws Exception {
                throw new RuntimeException("Real Method Exception");
            }
        };
        doCallRealMethod().when(callable).answer(any(InvocationOnMock.class));

        try {
            callable.answer(mock(InvocationOnMock.class));
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Real Method Exception", e.getMessage());
            verifyNoMoreInteractions(realObject);
        }
    }
}