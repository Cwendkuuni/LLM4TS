package com.fasterxml.jackson.databind.type; // adjust the package name if necessary

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeVariableStash;

public class TypeBindingsTest {

    private Class<?> testClass;
    private JavaType[] testTypes;

    @Before
    public void setUp() {
        testClass = List.class;
        testTypes = new JavaType[] { JavaType.constructParametricType(List.class, Integer.class),
                JavaType.constructParametricType(List.class, String.class) };
    }

    // Test for factory method create() with single type argument

    @Test
    public void testCreateWithSingleTypeArgument() {
        TypeBindings bindings = TypeBindings.create(testClass, testTypes[0]);
        assertArrayEquals(new String[] {"T"}, bindings.getBoundNames());
        assertArrayEquals(new JavaType[] { testTypes[0] }, bindings.getBoundTypes());
        assertNull(bindings.hasUnbound("T"));
    }

    // Test for factory method create() with multiple type arguments

    @Test
    public void testCreateWithMultipleTypeArguments() {
        TypeBindings bindings = TypeBindings.create(testClass, testTypes);
        assertArrayEquals(new String[] {"E", "K"}, bindings.getBoundNames());
        assertArrayEquals(new JavaType[] { JavaType.getTypeParameter(testClass, 0),
                JavaType.getTypeParameter(testClass, 1) }, bindings.getBoundTypes());
        assertNull(bindings.hasUnbound("E"));
        assertNull(bindings.hasUnbound("K"));
    }

    // Test for factory method createIfNeeded() with single type argument

    @Test
    public void testCreateIfNeededWithSingleTypeArgument() {
        TypeBindings bindings = TypeBindings.createIfNeeded(testClass, testTypes[0]);
        assertNull(bindings.isEmpty());
        assertArrayEquals(new String[] {"T"}, bindings.getBoundNames());
        assertArrayEquals(new JavaType[] { testTypes[0] }, bindings.getBoundTypes());
        assertNull(bindings.hasUnbound("T"));
    }

    // Test for factory method createIfNeeded() with multiple type arguments

    @Test
    public void testCreateIfNeededWithMultipleTypeArguments() {
        TypeBindings bindings = TypeBindings.createIfNeeded(testClass, testTypes);
        assertNull(bindings.isEmpty());
        assertArrayEquals(new String[] {"E", "K"}, bindings.getBoundNames());
        assertArrayEquals(new JavaType[] { JavaType.getTypeParameter(testClass, 0),
                JavaType.getTypeParameter(testClass, 1) }, bindings.getBoundTypes());
        assertNull(bindings.hasUnbound("E"));
        assertNull(bindings.hasUnbound("K"));
    }

    // Test for method findBoundType()

    @Test
    public void testFindBoundType() {
        TypeBindings bindings = TypeBindings.create(testClass, testTypes);
        assertSame(testTypes[0], bindings.findBoundType("E"));
        assertSame(testTypes[1], bindings.findBoundType("K"));
        assertNull(bindings.findBoundType("Foo"));
    }

    // Test for method isEmpty()

    @Test
    public void testIsEmpty() {
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        assertTrue(bindings1.isEmpty());
        TypeBindings bindings2 = TypeBindings.create(testClass, testTypes);
        assertFalse(bindings2.isEmpty());
    }

    // Test for method size()

    @Test
    public void testSize() {
        TypeBindings bindings = TypeBindings.create(testClass, testTypes);
        int size = bindings.size();
        assertEquals(2, size);
    }

    // Test for method getBoundName()

    @Test
    public void testGetBoundName() {
        TypeBindings bindings = TypeBindings.create(testClass, testTypes);
        assertEquals("E", bindings.getBoundName(0));
        assertEquals("K", bindings.getBoundName(1));
        assertNull(bindings.getBoundName(2));
    }

    // Test for method getBoundType()

    @Test
    public void testGetBoundType() {
        TypeBindings bindings = TypeBindings.create(testClass, testTypes);
        assertSame(JavaType.getTypeParameter(testClass, 0), bindings.getBoundType(0));
        assertSame(JavaType.getTypeParameter(testClass, 1), bindings.getBoundType(1));
        assertNull(bindings.getBoundType(2));
    }

    // Test for method getTypeParameters()

    @Test
    public void testGetTypeParameters() {
        TypeBindings bindings = TypeBindings.create(testClass, testTypes);
        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(2, typeParameters.size());
        assertSame(JavaType.getTypeParameter(testClass, 0), typeParameters.get(0));
        assertSame(JavaType.getTypeParameter(testClass, 1), typeParameters.get(1));
    }

    // Test for method hasUnbound()

    @Test
    public void testHasUnbound() {
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        assertFalse(bindings1.hasUnbound("E"));
        TypeBindings bindings2 = TypeBindings.create(testClass, testTypes);
        assertFalse(bindings2.hasUnbound("E"));
        assertFalse(bindings2.hasUnbound("K"));
        TypeBindings bindings3 = bindings2.withUnboundVariable("Foo");
        assertTrue(bindings3.hasUnbound("Foo"));
    }
}