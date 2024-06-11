package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        // Test with return value from delegate
        Answer<Object> delegateAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return 1;
            }
        };
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation1.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethod("equals", Object.class));
        Mockito.when(invocation1.getMethod().getReturnType()).thenReturn(Integer.class);
        assertNull(returnsSmartNulls.answer(invocation1));

        // Test with null return type
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethod("equals", Object.class));
        Mockito.when(invocation2.getMethod().getReturnType()).thenReturn(null);
        assertNull(returnsSmartNulls.answer(invocation2));

        // Test with mockable return type
        ClassImposterizer mockClassImposterizer = Mockito.mock(ClassImposterizer.class);
        Mockito.when(mockClassImposterizer.canImposterise(Integer.class)).thenReturn(true);
        InvocationOnMock invocation3 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation3.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethod("equals", Object.class));
        Mockito.when(invocation3.getMethod().getReturnType()).thenReturn(Integer.class);
        assertNotNull(returnsSmartNulls.answer(invocation3));
    }
}