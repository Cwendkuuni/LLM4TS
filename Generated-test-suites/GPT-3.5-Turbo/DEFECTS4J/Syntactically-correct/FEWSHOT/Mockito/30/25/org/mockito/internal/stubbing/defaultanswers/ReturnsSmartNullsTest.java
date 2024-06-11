package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.util.ObjectMethodsGuru;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_withReturnValue() throws Throwable {
        ReturnsMoreEmptyValues delegate = new ReturnsMoreEmptyValues();
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        InvocationOnMock invocation = createInvocationOnMock();
        Object returnValue = delegate.answer(invocation);

        assertEquals(returnValue, answer.answer(invocation));
    }

    @Test
    public void testAnswer_withNullReturnValue() throws Throwable {
        ReturnsMoreEmptyValues delegate = new ReturnsMoreEmptyValues();
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        InvocationOnMock invocation = createInvocationOnMock();
        Object returnValue = null;

        assertEquals(returnValue, answer.answer(invocation));
    }

    @Test
    public void testAnswer_withMockableReturnType() throws Throwable {
        Class<?> type = Object.class;
        InvocationOnMock invocation = createInvocationOnMock(type);
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        Object returnValue = null;
        if (ClassImposterizer.INSTANCE.canImposterise(type)) {
            ObjectMethodsGuru objectMethodsGuru = new ObjectMethodsGuru();
            if (!objectMethodsGuru.isToString(invocation.getMethod())) {
                returnValue = ClassImposterizer.INSTANCE.imposterise(
                        new ReturnsSmartNulls.ThrowingInterceptor(invocation), type);
            }
        }
        assertEquals(returnValue, answer.answer(invocation));
    }

    @Test
    public void testAnswer_withNonMockableReturnType() throws Throwable {
        Class<?> type = String.class;
        InvocationOnMock invocation = createInvocationOnMock(type);
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        Object returnValue = null;
        assertEquals(returnValue, answer.answer(invocation));
    }

    private InvocationOnMock createInvocationOnMock() {
		return Mockito.mock(InvocationOnMock.class);
    }

    private InvocationOnMock createInvocationOnMock(Class<?> returnType) {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(returnType);
        return invocation;
    }
}