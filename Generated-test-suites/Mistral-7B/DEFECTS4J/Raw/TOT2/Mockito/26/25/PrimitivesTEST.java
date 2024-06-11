import org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PrimitivesTest {

    @Test public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expectedIntType = Integer.TYPE;
        Class<Integer> actualIntType = Primitives.primitiveTypeOf(intClass);
        assertEquals(expectedIntType, actualIntType);
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveTypeOfNullClass() {
        Class<?> nullClass = null;
        Primitives.primitiveTypeOf(nullClass);
    }

    @Test public void testPrimitiveTypeOfObject() {
        Object obj = new Object();
        Class<?> objClass = obj.getClass();
        Class<?> expectedObjClass = obj.getClass();
        Class<?> actualObjType = Primitives.primitiveTypeOf(objClass);
        assertEquals(expectedObjClass, actualObjType);
    }

    @Test public void testPrimitiveTypeOfNonPrimitiveClass() {
        Class<String> stringClass = String.class;
        Class<String> expectedStringClass = String.class;
        Class<?> actualStringType = Primitives.primitiveTypeOf(stringClass);
        assertNotEquals(expectedStringClass, actualStringType);
    }

    @Test public void testIsPrimitiveWrapperBooleanWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        boolean expectedResult = true;
        boolean actualResult = Primitives.isPrimitiveWrapper(booleanWrapperClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test public void testIsPrimitiveWrapperCharacterWrapper() {
        Class<Character> characterWrapperClass = Character.class;
        boolean expectedResult = true;
        boolean actualResult = Primitives.isPrimitiveWrapper(characterWrapperClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test public void testIsPrimitiveWrapperNonWrapperClass() {
        Class<Integer> integerClass = Integer.class;
        boolean expectedResult = false;
        boolean actualResult = Primitives.isPrimitiveWrapper(integerClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test public void testPrimitiveWrapperOfBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expectedBoolean = false;
        Boolean actualBoolean = Primitives.primitiveWrapperOf(booleanClass);
        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveWrapperOfNullClass() {
        Class<?> nullClass = null;
        Primitives.primitiveWrapperOf(nullClass);
    }

    @Test public void testPrimitiveWrapperOfNonPrimitiveClass() {
        Class<Integer> integerClass = Integer.class;
        Object expectedObject = 0;
        Object actualObject = Primitives.primitiveWrapperOf(integerClass);
        assertEquals(expectedObject, actualObject);
        assertInstanceOf(Number.class, actualObject);
    }

    @Test public void testPrimitiveValueOrNullForBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        boolean expectedBoolean = false;
        boolean actualBoolean = Primitives.primitiveValueOrNullFor(booleanClass);
        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test public void testPrimitiveValueOrNullForNullClass() {
        Class<?> nullClass = null;
        Object actualObject = Primitives.primitiveValueOrNullFor(nullClass);
        assertNull(actualObject);
    }

    @Test public void testPrimitiveValueOrNullForNonPrimitiveClass() {
        Class<String> stringClass = String.class;
        Object expectedObject = null;
        Object actualObject = Primitives.primitiveValueOrNullFor(stringClass);
        assertEquals(expectedObject, actualObject);
    }
}