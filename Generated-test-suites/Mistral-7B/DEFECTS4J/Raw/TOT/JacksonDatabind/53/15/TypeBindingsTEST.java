package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link TypeBindings} class.
 */
public class TypeBindingsTest {

    /**
     * Test creating {@link TypeBindings} for empty class.
     */
    @Test
    public void testEmptyClass() {
        Class<?> emptyClass = Object.class;
        TypeBindings expected = TypeBindings.emptyBindings();
        TypeBindings actual = TypeBindings.create(emptyClass, null);
        assertEquals(expected, actual);
    }

    /**
     * Test creating {@link TypeBindings} for simple class with one type parameter.
     */
    @Test
    public void testSimpleClassWithOneTypeParameter() {
        Class<?> simpleClass = List.class;
        JavaType expectedFirstTypeArgument = JavaType.constructParametricType(ArrayList.class, Number.class);
        TypeBindings actual = TypeBindings.create(simpleClass, expectedFirstTypeArgument);
        JavaType actualFirstTypeArgument = actual.getBoundType(0);
        assertEquals(expectedFirstTypeArgument, actualFirstTypeArgument);
    }

    /**
     * Test creating {@link TypeBindings} for simple class with two type parameters.
     */
    @Test
    public void testSimpleClassWithTwoTypeParameters() {
        Class<?> simpleClass = Map.class;
        JavaType expectedFirstTypeArgument = JavaType.constructParametricType(HashMap.class, String.class, Number.class);
        TypeBindings actual = TypeBindings.create(simpleClass, expectedFirstTypeArgument);
        JavaType actualFirstTypeArgument = actual.getBoundType(0);
        assertEquals(expectedFirstTypeArgument, actualFirstTypeArgument);

        JavaType expectedSecondTypeArgument = JavaType.constructRawType(Number.class);
        JavaType actualSecondTypeArgument = actual.getBoundType(1);
        assertEquals(expectedSecondTypeArgument, actualSecondTypeArgument);
    }

    /**
     * Test creating {@link TypeBindings} for class with no type parameters.
     */
    @Test
    public void testClassWithoutTypeParameters() {
        Class<?> simpleClass = String.class;
        TypeBindings actual = TypeBindings.create(simpleClass, null);
        assertTrue(actual.isEmpty());
    }

    /**
     * Test creating {@link TypeBindings} for class with mismatched number of type parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedNumberOfTypeParameters() {
        Class<?> simpleClass = List.class;
        JavaType[] typeArguments = new JavaType[2];
        typeArguments[0] = JavaType.constructParametricType(ArrayList.class, Number.class);
        // would cause mismatch
        typeArguments[1] = JavaType.constructParametricType(ArrayList.class, Number.class, String.class);

        TypeBindings.create(simpleClass, typeArguments);
    }

    /**
     * Test creating {@link TypeBindings} for class with mismatched type parameter names.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedTypeParameterNames() {
        Class<?> simpleClass = Map.class;
        JavaType expectedFirstTypeArgument = JavaType.constructParametricType(HashMap.class, String.class, Number.class);
        JavaType[] typeArguments = new JavaType[]{expectedFirstTypeArgument};
        String[] mismatchedNames = new String[]{"ElemType", "KeyType"};

        try {
            TypeBindings.create(simpleClass, typeArguments, mismatchedNames);
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Can not create TypeBindings for class " + simpleClass.getName()
                    + " with 2 type parameters: class expects 2, but given 1";
            String actualMessage = e.getMessage();
            assertEquals(expectedMessage, actualMessage);
            throw e;
        }
    }

    /**
     * Test creating {@link TypeBindings} for class with mismatched type parameter types.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedTypeParameterTypes() {
        Class<?> simpleClass = Map.class;
        JavaType expectedFirstTypeArgument = JavaType.constructParametricType(HashMap.class, String.class, Number.class);
        JavaType expectedSecondTypeArgument = JavaType.constructRawType(Number.class);
        JavaType[] typeArguments = new JavaType[]{expectedFirstTypeArgument, expectedSecondTypeArgument};

        String[] mismatchedNames = new String[]{"ElemType", "KeyType"};

        try {
            TypeBindings.create(simpleClass, typeArguments, mismatchedNames);
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Can not create TypeBindings for class " + simpleClass.getName()
                    + " with 2 type parameters: class expects 2 'Number' type arguments but was given 1 'java.lang.Object' and 1 'java.lang.Class<java.lang.Number>'";
            String actualMessage = e.getMessage();
            assertEquals(expectedMessage, actualMessage);
            throw e;
        }
    }
}