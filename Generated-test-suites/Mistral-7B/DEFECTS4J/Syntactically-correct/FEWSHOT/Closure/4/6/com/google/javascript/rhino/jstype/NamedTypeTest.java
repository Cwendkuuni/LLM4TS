package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyType;
import com.google.javascript.rhino.jstype.StaticTypeEnvironment;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.util.Boolector;

import java.util.List;

public class NamedTypeTest {
    private static final ErrorReporter errorReporter = new ErrorReporter() {
        @Override
        public void warning(String message, String sourceName, int line, int charPosition) {
            System.out.println("WARNING: " + message);
        }

        @Override
        public void error(String message, String sourceName, int line, int charPosition) {
            throw new RuntimeException("ERROR: " + message);
        }
    };

    @Test
    public void testResolveViaRegistry() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Int32", new Int32Type());
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        NamedType namedType = new NamedType(registry, "Int32", "Test", 1, 1);

        // Test
        JSType result = namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertTrue(namedType.isResolved());
        assertTrue(result.isInstanceOf(Int32Type.class));
    }

    @Test
    public void testResolveViaProperties() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Object", ObjectType.OBJECT);
        registry.putType("Int32", new Int32Type());
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        ObjectType objType = TypeFactory.createObjectType("MyObject", registry);
        PropertyType propType = TypeFactory.createPropertyType("myProp", Int32Type.INT, null, null);
        objType.defineProperty("myProp", propType, false, null);
        StaticSlot<JSType> slot = enclosing.putSlot("MyObject", objType);

        NamedType namedType = new NamedType(registry, "MyObject.myProp", "Test", 1, 1);

        // Test
        JSType result = namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertTrue(namedType.isResolved());
        assertTrue(result.isInstanceOf(Int32Type.class));
    }

    @Test
    public void testResolveViaPropertiesWithFunctionType() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Object", ObjectType.OBJECT);
        registry.putType("Int32", new Int32Type());
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        ObjectType objType = TypeFactory.createObjectType("MyObject", registry);
        FunctionType funcType = TypeFactory.createFunctionType(null, null, null, null, null, null);
        PropertyType propType = TypeFactory.createPropertyType("myFunc", funcType, null, null);
        objType.defineProperty("myFunc", propType, false, null);
        StaticSlot<JSType> slot = enclosing.putSlot("MyObject", objType);

        NamedType namedType = new NamedType(registry, "MyObject.myFunc", "Test", 1, 1);

        // Test
        JSType result = namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertTrue(namedType.isResolved());
        assertTrue(result.isInstanceOf(FunctionType.class));
    }

    @Test
    public void testResolveViaPropertiesWithEnumType() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Object", ObjectType.OBJECT);
        registry.putType("MyEnum", new EnumType("MyEnum", registry, null, null, null));
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        ObjectType objType = TypeFactory.createObjectType("MyObject", registry);
        PropertyType propType = TypeFactory.createPropertyType("myEnum", MyEnumType.MyEnum.class, null, null);
        objType.defineProperty("myEnum", propType, false, null);
        StaticSlot<JSType> slot = enclosing.putSlot("MyObject", objType);

        NamedType namedType = new NamedType(registry, "MyObject.myEnum", "Test", 1, 1);

        // Test
        JSType result = namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertTrue(namedType.isResolved());
        assertTrue(result.isInstanceOf(MyEnumType.MyEnum.class.getDeclaringClass()));
    }

    @Test
    public void testResolveViaPropertiesWithUnknownType() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Object", ObjectType.OBJECT);
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        ObjectType objType = TypeFactory.createObjectType("MyObject", registry);
        StaticSlot<JSType> slot = enclosing.putSlot("MyObject", objType);

        NamedType namedType = new NamedType(registry, "MyObject.unknown", "Test", 1, 1);

        // Test
        JSType result = namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertFalse(namedType.isResolved());
        assertNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testResolveCircularType() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("CircularType", new NamedType(registry, "CircularType.circular", "Test", 1, 1));
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        NamedType circularType = new NamedType(registry, "CircularType.circular", "Test", 1, 1);

        // Test
        circularType.resolveInternal(errorReporter, enclosing);

        // Verify - exception thrown
    }

    @Test
    public void testHandleTypeCycle() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("CircularType", new NamedType(registry, "CircularType.circular", "Test", 1, 1));
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        NamedType circularType = new NamedType(registry, "CircularType.circular", "Test", 1, 1);

        // Test
        circularType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertTrue(circularType.isResolved());
        assertTrue(circularType.getReferencedType().isInstanceOf(JSTypeNative.UNKNOWN_TYPE));
    }

    @Test
    public void testSetValidator() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Int32", new Int32Type());
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        NamedType namedType = new NamedType(registry, "Int32", "Test", 1, 1);

        // Test
        Predicate<JSType> validator = new Predicate<JSType>() {
            @Override
            public boolean apply(JSType input) {
                return input.isInstanceOf(Int32Type.class);
            }
        };
        namedType.setValidator(validator);
        JSType result = namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertTrue(namedType.isResolved());
        assertTrue(result.isInstanceOf(Int32Type.class));
    }

    @Test
    public void testSetValidatorWithUnresolvedType() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Int32", new Int32Type());
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        NamedType namedType = new NamedType(registry, "UnknownType", "Test", 1, 1);

        // Test
        Predicate<JSType> validator = new Predicate<JSType>() {
            @Override
            public boolean apply(JSType input) {
                return input.isInstanceOf(Int32Type.class);
            }
        };
        namedType.setValidator(validator);
        JSType result = namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertFalse(namedType.isResolved());
        assertNull(result);
    }

    @Test
    public void testSetValidatorWithUnknownTypeValidator() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Int32", new Int32Type());
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        NamedType namedType = new NamedType(registry, "UnknownType", "Test", 1, 1);

        // Test
        Predicate<JSType> validator = new Predicate<JSType>() {
            @Override
            public boolean apply(JSType input) {
                return input.isInstanceOf(JSTypeNative.UNKNOWN_TYPE);
            }
        };
        namedType.setValidator(validator);
        JSType result = namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertFalse(namedType.isResolved());
        assertTrue(namedType.getReferencedType().isInstanceOf(JSTypeNative.UNKNOWN_TYPE));
    }

    @Test
    public void testSetValidatorWithTypeThatDoesNotMatch() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Int32", new Int32Type());
        registry.putType("String", new StringType());
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        NamedType namedType = new NamedType(registry, "Int32", "Test", 1, 1);

        // Test
        Predicate<JSType> validator = new Predicate<JSType>() {
            @Override
            public boolean apply(JSType input) {
                return input.isInstanceOf(StringType.class);
            }
        };
        namedType.setValidator(validator);
        JSType result = namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertFalse(namedType.isResolved());
        assertNull(result);
    }

    @Test
    public void testSetValidatorWithNoType() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Int32", new Int32Type());
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        NamedType namedType = new NamedType(registry, "Int32", "Test", 1, 1);

        // Test
        Predicate<JSType> validator = null;
        namedType.setValidator(validator);
        JSType result = namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        assertTrue(namedType.isResolved());
        assertTrue(namedType.getReferencedType().isInstanceOf(Int32Type.class));
    }

    @Test
    public void testPropertyContinuation() {
        // Setup
        JSTypeRegistry registry = JSTypeRegistry.createRegistry();
        registry.putType("Object", ObjectType.OBJECT);
        StaticTypeEnvironment staticTypeEnvironment = new StaticTypeEnvironment();
        StaticScope<JSType> enclosing = staticTypeEnvironment.getTopScope();
        ObjectType objType = TypeFactory.createObjectType("MyObject", registry);
        PropertyType propType = TypeFactory.createPropertyType("myProp", Int32Type.INT, null, null);
        PropertyContinuation continuation = new PropertyContinuation(
                "myProp", Int32Type.INT, false, null);
        objType.defineProperty("myProp", propType, false, null);
        StaticSlot<JSType> slot = enclosing.putSlot("MyObject", objType);

        NamedType namedType = new NamedType(registry, "MyObject.myProp", "Test", 1, 1);

        // Test
        namedType.resolveInternal(errorReporter, enclosing);

        // Verify
        objType.defineProperty("myProp", propType, false, null);
        assertEquals(Int32Type.INT, namedType.getReferencedType());
    }
}