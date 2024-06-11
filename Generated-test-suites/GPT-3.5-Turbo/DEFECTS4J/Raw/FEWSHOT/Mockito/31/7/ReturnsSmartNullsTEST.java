
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    
    private ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    
    @Test
    public void testAnswer() throws Throwable {
        // Test delegate answer
        ReturnsMoreEmptyValues returnsMoreEmptyValues = new ReturnsMoreEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        assertNull(returnsSmartNulls.answer(invocation));
        
        // Test defaultReturnValue is not null
        Method method = TestClass.class.getMethod("notNullMethod");
        invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(returnsMoreEmptyValues.answer(invocation)).thenReturn("Not null");
        assertEquals("Not null", returnsSmartNulls.answer(invocation));
        
        // Test ClassImposterizer can imposterise type
        method = TestClass.class.getMethod("imposteriseMethod");
        invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        ClassImposterizer classImposterizer = Mockito.mock(ClassImposterizer.class);
        Mockito.when(ClassImposterizer.INSTANCE).thenReturn(classImposterizer);
        Mockito.when(classImposterizer.canImposterise(TestClass.class)).thenReturn(true);
        assertEquals("SmartNull returned by unstubbed imposteriseMethod() method on mock", returnsSmartNulls.answer(invocation));
        
        // Test ClassImposterizer cannot imposterise type
        method = TestClass.class.getMethod("cannotImposteriseMethod");
        invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(classImposterizer.canImposterise(TestClass.class)).thenReturn(false);
        assertNull(returnsSmartNulls.answer(invocation));
    }
    
    public class TestClass {
        public String notNullMethod() {
            return "";
        }
        
        public TestClass imposteriseMethod() {
            return null;
        }
        
        public final TestClass cannotImposteriseMethod() {
            return null;
        }
    }
}
