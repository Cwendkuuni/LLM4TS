package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = new ReturnsMoreEmptyValues();
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationMock.getMethod()).thenReturn(TestClass.class.getMethod("method"));
        Object defaultReturnValue = delegate.answer(invocationMock);

        assertNull(defaultReturnValue);

        Mockito.when(invocationMock.getMethod()).thenReturn(TestClass.class.getMethod("finalMethod"));
        defaultReturnValue = delegate.answer(invocationMock);

        assertNull(defaultReturnValue);

        ClassImposterizer.INSTANCE = Mockito.mock(ClassImposterizer.class);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(TestClass.class)).thenReturn(true);
        Class<?> type = TestClass.class;
        Mockito.when(ClassImposterizer.INSTANCE.imposterise(Mockito.any(), Mockito.eq(type))).thenReturn(new Object());

        defaultReturnValue = returnsSmartNulls.answer(invocationMock);

        assertNotNull(defaultReturnValue);
        assertEquals(Object.class, defaultReturnValue.getClass());

        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(TestClass.class)).thenReturn(false);

        defaultReturnValue = returnsSmartNulls.answer(invocationMock);

        assertNull(defaultReturnValue);
    }

    public static class TestClass {
        public Object method() {
            return null;
        }

        public final Object finalMethod() {
            return null;
        }
    }
}