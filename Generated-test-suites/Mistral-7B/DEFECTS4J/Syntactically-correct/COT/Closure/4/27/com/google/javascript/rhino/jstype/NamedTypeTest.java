package com.google.javascript.rhino.jstype.test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.TypeErrorReporter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NamedTypeTest {

    private NamedType namedType;
    private final JSTypeRegistry registry = mock(JSTypeRegistry.class);
    private final ErrorReporter errorReporter = mock(ErrorReporter.class);
    private final AtomicReference<JSType> resolvedTypeRef = new AtomicReference<>();
    private final StaticScope<JSType> staticScope = mock(StaticScope.class);

    @Before
    public void setup() {
        when(registry.getType("TestType")).thenReturn(null);
        when(registry.getType("JSTypeNative")).thenReturn(JSTypeNative.UNKNOWN_TYPE);
        when(registry.getType("ObjectType")).thenReturn(ObjectType.VOID);
        when(registry.getType("FunctionType")).thenReturn(FunctionType.VOID);
        when(registry.getType("EnumTypeTest")).thenReturn(mock(EnumType.class));

        namedType = new NamedType(registry, "TestType", "test.js", 1, 1);
        namedType.setValidator(new Function<JSType, Boolean>() {
            @Override
            public Boolean apply(JSType input) {
                resolvedTypeRef.set(input);
                return true;
            }
        });
    }

    @Test
    public void testUnresolvedType() {
        namedType.resolveInternal(errorReporter, staticScope);
        assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());
        assertEquals(JSTypeNative.NO_RESOLVED_TYPE, resolvedTypeRef.get());
    }

    @Test
    public void testResolvedTypeViaRegistry() {
        final JSType testType = new ObjectType("TestType");
        when(registry.getType("TestType")).thenReturn(testType);

        namedType.resolveInternal(errorReporter, staticScope);
        assertEquals(testType, namedType.getReferencedType());
        assertEquals(testType, resolvedTypeRef.get());
    }

    @Test(expected = TypeError.class)
    public void testResolvedTypeViaPropertiesWithCycle() {
        Node node = Mockito.mock(Node.class);
        when(staticScope.getSlot("TestType")).thenReturn(
                new StaticSlot<JSType>(node, null, null) {
                    @Override
                    public JSType getType() {
                        return namedType;
                    }
                });

        namedType.resolveInternal(errorReporter, staticScope);
    }

    @Test(expected = TypeError.class)
    public void testResolvedTypeViaPropertiesWithUnresolvable() {
        when(staticScope.getSlot("TestType")).thenReturn(null);

        namedType.resolveInternal(errorReporter, staticScope);
    }

    @Test(expected = TypeError.class)
    public void testResolvedTypeViaPropertiesWithUnknownType() {
        when(staticScope.getSlot("TestType")).thenReturn(new StaticSlot<>(null, null, null) {
            @Override
            public JSType getType() {
                return registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);
            }
        });

        namedType.resolveInternal(errorReporter, staticScope);
    }

    @Test
    public void testDefinePropertyWhenUnresolved() {
        namedType.defineProperty("testProperty", FunctionType.VOID, true, null);
        assertTrue(namedType.propertyContinuations != null);
        assertEquals(1, namedType.propertyContinuations.size());
    }

    @Test
    public void testFinishPropertyContinuations() {
        ObjectType testType = new ObjectType("TestType");
        namedType.propertyContinuations = Lists.newArrayList(
                new PropertyContinuation("testProperty", FunctionType.VOID, true, null) {
                    @Override
                    void commit(ObjectType target) {
                        target.defineProperty("testProperty", FunctionType.VOID, true, null);
                    }
                }
        );
        when(registry.isLastGeneration()).thenReturn(false);
        when(registry.getType("TestType")).thenReturn(testType);

        namedType.finishPropertyContinuations();

        ObjectType testObjectType = ObjectType.cast(namedType.getReferencedType());
        assertEquals("testProperty", testObjectType.getPropertyNames().get(0));
        assertNotNull(testObjectType.getProperty("testProperty"));
    }

    @Test
    public void testResolveViaRegistryWithEnumType() {
        JSType enumType = Mockito.mock(EnumType.class);
        EnumType elementsType = Mockito.mock(EnumType.class);
        when(registry.getType("EnumTypeTest")).thenReturn(enumType);
        when(enumType.getElementsType()).thenReturn(elementsType);

        namedType.resolveInternal(errorReporter, staticScope);
        assertEquals(elementsType, namedType.getReferencedType());
        assertEquals(elementsType, resolvedTypeRef.get());
    }

    @Test(expected = TypeError.class)
    public void testResolveViaPropertiesWithNoObjectType() {
        when(staticScope.getSlot("TestType")).thenReturn(
                new StaticSlot<JSType>(null, null, null) {
                    @Override
                    public JSType getType() {
                        return registry.getNativeFunctionType(
                                JSTypeNative.NO_OBJECT_TYPE);
                    }
                });

        namedType.resolveInternal(errorReporter, staticScope);
    }

    @Test
    public void testResolveViaPropertiesWithFunctionTypeAndInstanceType() {
        FunctionType functionType = FunctionType.VOID;
        ObjectType instanceType = new ObjectType("TestType");
        when(staticScope.getSlot("TestType")).thenReturn(new StaticSlot<>(
                null, null, null) {
                    @Override
                    public JSType getType() {
                        return functionType;
                    }
                });

        namedType.resolveInternal(errorReporter, staticScope);
        assertEquals(instanceType, namedType.getReferencedType());
        assertEquals(instanceType, resolvedTypeRef.get());
    }

    @Test(expected = TypeError.class)
    public void testResolveViaPropertiesWithInterfaceType() {
        FunctionType functionType = FunctionType.VOID;
        when(staticScope.getSlot("TestType")).thenReturn(functionType);

        namedType.resolveInternal(errorReporter, staticScope);
    }
}