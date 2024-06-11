/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    private static class TestClass {
        public String getString() {
            return null;
        }
        
        public int getInt() {
            return 0;
        }
        
        public Object getObject() {
            return null;
        }
        
        public void voidMethod() {
            // Do nothing
        }
    }

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocationString = mock(InvocationOnMock.class);
        when(invocationString.getMethod()).thenReturn(TestClass.class.getMethod("getString"));
        assertNull(returnsSmartNulls.answer(invocationString));

        InvocationOnMock invocationInt = mock(InvocationOnMock.class);
        when(invocationInt.getMethod()).thenReturn(TestClass.class.getMethod("getInt"));
        assertEquals(0, returnsSmartNulls.answer(invocationInt));

        InvocationOnMock invocationObject = mock(InvocationOnMock.class);
        when(invocationObject.getMethod()).thenReturn(TestClass.class.getMethod("getObject"));
        assertNull(returnsSmartNulls.answer(invocationObject));

        InvocationOnMock invocationVoid = mock(InvocationOnMock.class);
        when(invocationVoid.getMethod()).thenReturn(TestClass.class.getMethod("voidMethod"));
        assertNull(returnsSmartNulls.answer(invocationVoid));
    }

    @Test
    public void testAnswerThrowsSmartNullPointerException() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        Location location = new Location();
        ObjectMethodsGuru objectMethodsGuru = mock(ObjectMethodsGuru.class);
        Method toStringMethod = Object.class.getMethod("toString");
        when(objectMethodsGuru.isToString(toStringMethod)).thenReturn(true);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(toStringMethod);
        when(invocation.getMethod()).thenReturn(toStringMethod);

        assertEquals("SmartNull returned by unstubbed toString() method on mock",
                returnsSmartNulls.answer(invocation));
    }

    @Test
    public void testAnswerReturnsMock() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("getObject"));

        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        when(classImposterizer.canImposterise(Object.class)).thenReturn(true);

        TestClass mock = mock(TestClass.class);
        when(classImposterizer.imposterise(any(Answer.class), eq(Object.class))).thenReturn(mock);

        assertEquals(mock, returnsSmartNulls.answer(invocation));
    }

    @Test
    public void testAnswerReturnsNullForFinalClass() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("getObject"));

        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        when(classImposterizer.canImposterise(Object.class)).thenReturn(false);

        assertNull(returnsSmartNulls.answer(invocation));
    }

}