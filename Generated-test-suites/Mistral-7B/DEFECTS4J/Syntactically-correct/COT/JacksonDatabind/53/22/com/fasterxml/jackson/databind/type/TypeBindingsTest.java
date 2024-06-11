package com.fasterxml.jackson.databind.type; // Import necessary packages

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeParameter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JavaTypeResolverBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType(""));
        assertEquals("", empty.toString());
    }

    @Test
    public void testCreate() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametic(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        assertEquals(1, intBindings.size());
        assertEquals("java.lang.Integer", intBindings.getBoundName(0));
        assertEquals(JavaType.constructParametric(int[].class, int.class), intBindings.getBoundType(0));
        assertEquals("java.lang.Integer[]", intBindings.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidNumberOfTypes() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametic(int[].class, int.class));
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        TypeBindings invalidIntBindings = TypeBindings.create(intListClass, new JavaType[] { intTypes.get(0) });

        assertEquals(1, intBindings.size());
        assertEquals("java.lang.Integer", intBindings.getBoundName(0));
        assertEquals(JavaType.constructParametric(int[].class, int.class), intBindings.getBoundType(0));

        // This test case should fail with IllegalArgumentException
        TypeBindings invalidIntBindings2 = TypeBindings.create(intListClass, new JavaType[] { intTypes.get(0), intTypes.get(1) });
    }

    @Test
    public void testCreateIfNeeded() {
        Class<?> intListClass = Collections.class;
        TypeBindings intBindings = TypeBindings.createIfNeeded(intListClass, null);

        assertTrue(intBindings.isEmpty());
        assertEquals(0, intBindings.size());

        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindingsWithTypes = TypeBindings.createIfNeeded(intListClass, intTypes.toArray(new JavaType[0]));

        assertEquals(1, intBindingsWithTypes.size());
        assertEquals("java.lang.Integer", intBindingsWithTypes.getBoundName(0));
        assertEquals(JavaType.constructParametric(int[].class, int.class), intBindingsWithTypes.getBoundType(0));
        assertEquals("java.lang.Integer[]", intBindingsWithTypes.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIfNeededInvalidTypes() {
        Class<?> intListClass = Collections.class;
        TypeBindings intBindings = TypeBindings.createIfNeeded(intListClass, new JavaType[] { JavaType.constructParametric(int[].class), JavaType.constructParametric(String.class) });
    }

    @Test
    public void testWithUnboundVariable() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        TypeBindings intWithUnboundVariable = intBindings.withUnboundVariable("T");
        String[] unboundVariables = intWithUnboundVariable.getUnboundVariables();
        assertEquals(1, unboundVariables.length);
        assertEquals("T", unboundVariables[0]);

        JavaType unboundVariableType = intWithUnboundVariable.findBoundType("T");
        assertNull(unboundVariableType);
    }

    @Test
    public void testFindBoundType() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        JavaType expected = JavaType.constructParametric(int[].class, int.class);
        JavaType boundType = intBindings.findBoundType("java.lang.Integer");
        assertEquals(expected, boundType);
    }

    @Test
    public void testFindBoundTypeNull() {
        Class<?> intListClass = Collections.class;
        TypeBindings intBindings = TypeBindings.emptyBindings();

        JavaType boundType = intBindings.findBoundType("java.lang.Integer");
        assertNull(boundType);
    }

    @Test
    public void testFindBoundTypeUnknownName() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        JavaType boundType = intBindings.findBoundType("java.lang.String");
        assertNull(boundType);
    }

    @Test
    public void testIsEmpty() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());

        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        assertFalse(intBindings.isEmpty());
    }

    @Test
    public void testSize() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());

        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        assertEquals(1, intBindings.size());
    }

    @Test
    public void testGetBoundName() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        assertEquals("java.lang.Integer", intBindings.getBoundName(0));
    }

    @Test
    public void testGetBoundNameInvalidIndex() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        assertNull(intBindings.getBoundName(-1));
    }

    @Test
    public void testGetBoundType() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        assertEquals(JavaType.constructParametric(int[].class, int.class), intBindings.getBoundType(0));
    }

    @Test
    public void testGetBoundTypeInvalidIndex() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        assertNull(intBindings.getBoundType(-1));
    }

    @Test
    public void testGetTypeParameters() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        List<JavaType> expected = Arrays.asList(JavaType.constructParametric(int[].class, int.class));
        List<JavaType> actual = intBindings.getTypeParameters();

        assertEquals(expected, actual);
    }

    @Test
    public void testHasUnbound() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        assertFalse(intBindings.hasUnbound("java.lang.Integer"));

        intBindings = intBindings.withUnboundVariable("T");

        assertTrue(intBindings.hasUnbound("T"));
    }

    @Test
    public void testHasUnboundUnknownName() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        assertFalse(intBindings.hasUnbound("java.lang.String"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBoundType() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        intBindings.setBoundType(0, JavaType.constructParametric(String[].class, int.class));
    }

    @Test
    public void testToString() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        assertEquals("java.lang.Integer[]", intBindings.toString());
    }

    @Test
    public void testEqualsNull() {
        TypeBindings empty = TypeBindings.emptyBindings();

        assertFalse(empty.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        TypeBindings empty = TypeBindings.emptyBindings();

        assertTrue(empty.equals(empty));
    }

    @Test
    public void testEqualsDifferentClass() {
        TypeBindings intBindings = TypeBindings.create(Object[].class, new JavaType[0]);
        TypeBindings stringBindings = TypeBindings.create(String[].class, new JavaType[0]);

        assertFalse(intBindings.equals(stringBindings));
    }

    @Test
    public void testEqualsDifferentTypes() {
        TypeBindings intBindings = TypeBindings.create(int[].class, new JavaType[] { JavaType.constructParametric(int.class, int.class) });
        TypeBindings stringBindings = TypeBindings.create(String[].class, new JavaType[] { JavaType.constructParametric(String.class, String.class) });

        assertFalse(intBindings.equals(stringBindings));
    }

    @Test
    public void testEqualsSameTypes() {
        TypeBindings intBindings = TypeBindings.create(int[].class, new JavaType[] { JavaType.constructParametric(int.class, int.class) });
        TypeBindings stringBindings = TypeBindings.create(int[].class, new JavaType[] { JavaType.constructParametric(int.class, int.class) });

        assertTrue(intBindings.equals(stringBindings));
    }

    @Test
    public void testEqualsSameTypesDifferentOrder() {
        TypeBindings intBindings = TypeBindings.create(int[].class, new JavaType[] { JavaType.constructParametric(int.class, int.class) });
        TypeBindings stringBindings = TypeBindings.create(int[].class, new JavaType[] { JavaType.constructParametric(int.class, int.class), JavaType.constructParametric(int.class, int.class) });

        assertTrue(intBindings.equals(stringBindings));
    }

    @Test
    public void testEqualsSameTypesSameInstance() {
        TypeBindings intBindings = TypeBindings.create(int[].class, new JavaType[] { JavaType.constructParametric(int.class, int.class) });
        TypeBindings stringBindings = intBindings;

        assertTrue(intBindings.equals(stringBindings));
    }

    @Test
    public void testClone() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        TypeBindings cloned = (TypeBindings) intBindings.clone();

        assertEquals(intBindings, cloned);
    }

    @Test
    public void testCloneDifferentTypes() {
        Class<?> intListClass = Collections.class;
        List<JavaType> intTypes = new ArrayList<>();
        intTypes.add(JavaType.constructParametric(int[].class, int.class));
        TypeBindings intBindings = TypeBindings.create(intListClass, intTypes);

        Class<?> stringListClass = List.class;
        List<JavaType> stringTypes = new ArrayList<>();
        stringTypes.add(JavaType.constructParametric(String[].class, String.class));
        TypeBindings stringBindings = TypeBindings.create(stringListClass, stringTypes);

        TypeBindings clonedInt = (TypeBindings) intBindings.clone();
        TypeBindings clonedString = (TypeBindings) stringBindings.clone();

        assertNotEquals(intBindings, clonedInt);
        assertNotEquals(intBindings, clonedString);
        assertNotEquals(clonedInt, clonedString);
    }

    
}