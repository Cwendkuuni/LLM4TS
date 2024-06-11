package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = new ReturnsMoreEmptyValues();
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        // Test delegate answer
        assertEquals("test", delegate.answer(mockInvocation()));
        assertNull(delegate.answer(mockInvocationNoReturnValue()));

        // Test null return value
        assertNull(answer.answer(mockInvocationNoReturnValue()));

        // Test impoterise for mockable type
        Class<?> mockableType = Object.class;
        assertEquals("SmartNull returned by unstubbed method on mock",
                answer.answer(mockInvocation(mockableType)));

        // Test impoterise for non-mockable type
        Class<?> nonMockableType = String.class;
        assertNull(answer.answer(mockInvocation(nonMockableType)));

    }

    private InvocationOnMock mockInvocation() throws NoSuchMethodException {
        return mockInvocation(Object.class);
    }

    private InvocationOnMock mockInvocationNoReturnValue() throws NoSuchMethodException {
        return mockInvocation(Void.TYPE);
    }

    private InvocationOnMock mockInvocation(Class<?> returnType) throws NoSuchMethodException {
        InvocationOnMock invocation = new InvocationOnMock() {

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public ObjectMock getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Location getLocation() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public void continueWith(Answer<?> answer) {
            }
        };
        invocation.getClass().getMethod("getMethod").setAccessible(true);
        invocation.getClass().getMethod("getMethod").invoke(invocation);

        return invocation;
    }
}