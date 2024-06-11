import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expectedType = Integer.TYPE;
        Class<Integer> actualType = Primitives.primitiveTypeOf(intClass);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        boolean expectedResult = true;
        boolean actualResult = Primitives.isPrimitiveWrapper(booleanWrapperClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expectedResult = false;
        Boolean actualResult = Primitives.primitiveWrapperOf(booleanClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expectedResult = false;
        Boolean actualResult = Primitives.primitiveValueOrNullFor(booleanClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveTypeOf_NullClass() {
        Class<?> nullClass = null;
        try {
            Primitives.primitiveTypeOf(nullClass);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPrimitiveTypeOf_PrimitiveClass() {
        Class<Boolean> booleanClass = Boolean.class;
        Class<Boolean> expectedType = Boolean.TYPE;
        Class<Boolean> actualType = Primitives.primitiveTypeOf(booleanClass);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testPrimitiveTypeOf_ObjectClass() {
        Class<Object> objectClass = Object.class;
        try {
            Primitives.primitiveTypeOf(objectClass);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsPrimitiveWrapper_NullClass() {
        Class<?> nullClass = null;
        try {
            Primitives.isPrimitiveWrapper(nullClass);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIsPrimitiveWrapper_NonPrimitiveClass() {
        Class<String> stringClass = String.class;
        boolean expectedResult = false;
        boolean actualResult = Primitives.isPrimitiveWrapper(stringClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveWrapperOf_NullClass() {
        Class<?> nullClass = null;
        try {
            Primitives.primitiveWrapperOf(nullClass);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPrimitiveWrapperOf_NonPrimitiveClass() {
        Class<String> stringClass = String.class;
        try {
            Primitives.primitiveWrapperOf(stringClass);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPrimitiveValueOrNullFor_NullClass() {
        Class<?> nullClass = null;
        try {
            Primitives.primitiveValueOrNullFor(nullClass);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPrimitiveValueOrNullFor_NonPrimitiveClass() {
        Class<String> stringClass = String.class;
        try {
            Primitives.primitiveValueOrNullFor(stringClass);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPrimitiveValues_NullClass() {
        Class<?> nullClass = null;
        try {
            Primitives.primitiveValues.get(nullClass);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPrimitiveValues_NonPrimitiveClass() {
        Class<String> stringClass = String.class;
        try {
            Primitives.primitiveValues.get(stringClass);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}