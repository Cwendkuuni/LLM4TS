package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.MatchersPrinter;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {
    
    @Test
    public void testEquals() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        MockitoMethod method1 = new MockitoMethod("method1");
        MockitoMethod method2 = new MockitoMethod("method2");
        Object[] args1 = { "arg1" };
        Object[] args2 = { "arg2" };
        RealMethod realMethod1 = new RealMethod();
        RealMethod realMethod2 = new RealMethod();
        
        Invocation inv1 = new Invocation(mock1, method1, args1, 1, realMethod1);
        Invocation inv2 = new Invocation(mock1, method1, args1, 1, realMethod1);
        Invocation inv3 = new Invocation(mock2, method1, args1, 1, realMethod1);
        Invocation inv4 = new Invocation(mock1, method2, args1, 1, realMethod1);
        Invocation inv5 = new Invocation(mock1, method1, args2, 1, realMethod1);
        Invocation inv6 = new Invocation(mock1, method1, args1, 2, realMethod1);
        Invocation inv7 = new Invocation(mock1, method1, args1, 1, realMethod2);
        
        // Same invocation
        assertTrue(inv1.equals(inv1));
        assertTrue(inv2.equals(inv2));
        assertTrue(inv3.equals(inv3));
        assertTrue(inv4.equals(inv4));
        assertTrue(inv5.equals(inv5));
        assertTrue(inv6.equals(inv6));
        assertTrue(inv7.equals(inv7));
        
        // Different invocations
        assertTrue(inv1.equals(inv2));
        assertFalse(inv1.equals(inv3));
        assertFalse(inv1.equals(inv4));
        assertFalse(inv1.equals(inv5));
        assertFalse(inv1.equals(inv6));
        assertFalse(inv1.equals(inv7));
        
        assertFalse(inv1.equals(null));
        assertFalse(inv1.equals(new Object()));
    }
    
    @Test
    public void testArgumentsToMatchers() {
        Object[] args = { "arg1", new int[] { 1, 2, 3 }, 1, 2.0 };
        
        Invocation inv = new Invocation(null, null, args, 1, null);
        
        List<Matcher> result = inv.argumentsToMatchers();
        
        assertEquals(4, result.size());
        assertTrue(result.get(0) instanceof Equals);
        assertTrue(result.get(1) instanceof ArrayEquals);
        assertTrue(result.get(2) instanceof Equals);
        assertTrue(result.get(3) instanceof Equals);
    }
    
    @Test
    public void testIsToString() {
        MockitoMethod methodToString = new MockitoMethod("toString");
        MockitoMethod methodNotToString = new MockitoMethod("notToString");
        
        InvocationOnMock invocationToString = new Invocation(null, methodToString, null);
        InvocationOnMock invocationNotToString = new Invocation(null, methodNotToString, null);
        
        assertTrue(Invocation.isToString(invocationToString));
        assertFalse(Invocation.isToString(invocationNotToString));
    }
    
    @Test
    public void testIsValidException() {
        MockitoMethod method = new MockitoMethod("method", null, new Class<?>[] { RuntimeException.class });
        
        Invocation inv = new Invocation(null, method, null);
        
        assertTrue(inv.isValidException(new RuntimeException()));
        assertFalse(inv.isValidException(new Exception()));
    }
    
    @Test
    public void testIsValidReturnType() {
        MockitoMethod methodPrimitive = new MockitoMethod("method", int.class, null);
        MockitoMethod methodNonPrimitive = new MockitoMethod("method", Integer.class, null);
        
        Invocation invPrimitive = new Invocation(null, methodPrimitive, null);
        Invocation invNonPrimitive = new Invocation(null, methodNonPrimitive, null);
        
        assertTrue(invPrimitive.isValidReturnType(int.class));
        assertFalse(invPrimitive.isValidReturnType(Integer.class));
        
        assertTrue(invNonPrimitive.isValidReturnType(Integer.class));
        assertFalse(invNonPrimitive.isValidReturnType(int.class));
    }
    
    @Test
    public void testIsVoid() {
        MockitoMethod methodVoid = new MockitoMethod("method", void.class, null);
        MockitoMethod methodNonVoid = new MockitoMethod("method", int.class, null);
        
        Invocation invVoid = new Invocation(null, methodVoid, null);
        Invocation invNonVoid = new Invocation(null, methodNonVoid, null);
        
        assertTrue(invVoid.isVoid());
        assertFalse(invNonVoid.isVoid());
    }
    
    @Test
    public void testGetMethodName() {
        MockitoMethod method = new MockitoMethod("method");
        
        Invocation inv = new Invocation(null, method, null);
        
        assertEquals("method", inv.getMethodName());
    }
    
    @Test
    public void testReturnsPrimitive() {
        MockitoMethod methodPrimitive = new MockitoMethod("method", int.class, null);
        MockitoMethod methodNonPrimitive = new MockitoMethod("method", Integer.class, null);
        
        Invocation invPrimitive = new Invocation(null, methodPrimitive, null);
        Invocation invNonPrimitive = new Invocation(null, methodNonPrimitive, null);
        
        assertTrue(invPrimitive.returnsPrimitive());
        assertFalse(invNonPrimitive.returnsPrimitive());
    }
    
    @Test
    public void testGetLocation() {
        Location location = new Location();
        
        Invocation inv = new Invocation(null, null, null);
        inv.setLocation(location);
        
        assertEquals(location, inv.getLocation());
    }
    
    @Test
    public void testGetArgumentsCount() {
        Object[] args = { "arg1", "arg2", "arg3" };
        
        Invocation inv = new Invocation(null, null, args);
        
        assertEquals(3, inv.getArgumentsCount());
    }
    
    @Test
    public void testGetRawArguments() {
        Object[] args = { "arg1", "arg2", "arg3" };
        
        Invocation inv = new Invocation(null, null, args);
        
        assertTrue(Arrays.equals(args, inv.getRawArguments()));
    }
}