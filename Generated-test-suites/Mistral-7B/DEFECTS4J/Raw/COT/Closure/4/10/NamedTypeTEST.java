package com.google.javascript.rhino.jstype;

import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NamedTypeTest {

    private NamedType namedType;
    private ErrorReporter errorReporter;
    private JSTypeRegistry typeRegistry;
    private StaticScope<JSType> enclosingScope;

    @Before
    public void setup() {
        errorReporter = mock(ErrorReporter.class);
        typeRegistry = mock(JSTypeRegistry.class);
        enclosingScope = mock(StaticScope.class);

        namedType = new NamedType(typeRegistry, "MyType", "TestSource", 1, 1);
    }

    @Test
    public void testBasicFunctionality() {
        Assert.assertEquals("MyType", namedType.getReferenceName());
        Assert.assertEquals(UNKNOWN_TYPE, namedType.getReferencedType());
        Assert.assertEquals(UNKNOWN_TYPE, namedType.getResolvedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyUnresolvedType() {
        when(typeRegistry.getType("MyType")).thenReturn(null);
        namedType.defineProperty("test", UNKNOWN_TYPE, false, null);
    }

    @Test
    public void testDefinePropertyResolvedType() {
        JSType resolvedType = mock(JSType.class);
        when(typeRegistry.getType("MyType")).thenReturn(resolvedType);

        namedType.defineProperty("test", UNKNOWN_TYPE, false, null);

        ObjectType objectType = ObjectType.cast(resolvedType);
        Assert.assertNotNull(objectType.getPropertyType("test"));
    }

    @Test
    public void testResolveViaRegistry() {
        JSType type = mock(JSType.class);
        when(typeRegistry.getType("MyType")).thenReturn(type);

        namedType.resolveInternal(errorReporter, enclosingScope);

        Assert.assertEquals(type, namedType.getReferencedType());
        Assert.assertEquals(type, namedType.getResolvedType());
    }

    @Test
    public void testResolveViaProperties() {
        FunctionType functionType = mock(FunctionType.class);
        when(enclosingScope.getSlot("MyType")).thenReturn(
            new StaticSlot<>(functionType, null));

        namedType.resolveInternal(errorReporter, enclosingScope);

        FunctionType functionTypeInstance = functionType.getInstanceType();
        Assert.assertEquals(functionTypeInstance, namedType.getReferencedType());
        Assert.assertEquals(functionTypeInstance, namedType.getResolvedType());
    }

    @Test
    public void testUnresolvedTypeWarning() {
        when(typeRegistry.getType("MyType")).thenReturn(null);

        namedType.resolveInternal(errorReporter, enclosingScope);

        Assert.assertEquals(NO_RESOLVED_TYPE, namedType.getReferencedType());
        Assert.assertEquals(NO_RESOLVED_TYPE, namedType.getResolvedType());
        Assert.assertEquals(1, errorReporter.getWarningCount());
    }

    @Test
    public void testTypeCycleWarning() {
        JSType cycleType = mock(JSType.class);
        when(typeRegistry.getType("MyType")).thenReturn(cycleType);
        when(cycleType.isInstanceOf(NamedType.class)).thenReturn(true);
        when(cycleType.getReferenceName()).thenReturn("MyType");

        namedType.resolveInternal(errorReporter, enclosingScope);

        Assert.assertEquals(NO_RESOLVED_TYPE, namedType.getReferencedType());
        Assert.assertEquals(NO_RESOLVED_TYPE, namedType.getResolvedType());
        Assert.assertEquals(1, errorReporter.getWarningCount());
    }

    @Test
    public void testPropertyContinuationCommit() {
        String propertyName = "test";
        JSType propertyType = UNKNOWN_TYPE;

        namedType.defineProperty(propertyName, propertyType, false, null);

        ObjectType objectType = ObjectType.cast(namedType);
        Assert.assertNotNull(objectType.getPropertyType(propertyName));
    }
}