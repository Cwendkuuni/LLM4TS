import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intType = Integer.class;
        Class<Integer> intTypeWrapped = Integer.TYPE;
        Class<Integer> intTypePrimitive = Primitives.primitiveTypeOf(intType);
        assertTrue(intTypePrimitive.isPrimitive());
        assertEquals(intTypeWrapped, intTypePrimitive);
    }

    @Test
    public void testIsPrimitiveWrapperBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        assertTrue(Primitives.isPrimitiveWrapper(booleanClass));
    }

    @Test
    public void testIsPrimitiveWrapperString() {
        Class<String> stringClass = String.class;
        assertFalse(Primitives.isPrimitiveWrapper(stringClass));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPrimitiveWrapperNullInput() {
        Primitives.isPrimitiveWrapper(null);
    }

    @Test
    public void testPrimitiveWrapperOfBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(booleanClass);
        assertTrue(booleanWrapper instanceof Boolean);
        assertEquals(false, booleanWrapper);
    }

    @Test
    public void testPrimitiveWrapperOfCharacter() {
        Class<Character> characterClass = Character.class;
        Character characterWrapper = Primitives.primitiveWrapperOf(characterClass);
        assertTrue(characterWrapper instanceof Character);
        assertEquals('\u0000', characterWrapper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveWrapperOfNullInput() {
        Primitives.primitiveWrapperOf(null);
    }

    @Test
    public void testPrimitiveValueOrNullForBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(booleanClass);
        assertTrue(booleanValue instanceof Boolean);
        assertEquals(false, booleanValue);
    }

    @Test
    public void testPrimitiveValueOrNullForCharacter() {
        Class<Character> characterClass = Character.class;
        Character characterValue = Primitives.primitiveValueOrNullFor(characterClass);
        assertTrue(characterValue instanceof Character);
        assertEquals('\u0000', characterValue);
    }

    @Test
    public void testPrimitiveValueOrNullForInteger() {
        Class<Integer> integerClass = Integer.class;
        Integer integerValue = Primitives.primitiveValueOrNullFor(integerClass);
        assertNull(integerValue);
    }

    @Test
    public void testPrimitiveValueOrNullForUnsupportedType() {
        Class<BigInteger> bigIntegerClass = BigInteger.class;
        Object bigIntegerValue = Primitives.primitiveValueOrNullFor(bigIntegerClass);
        assertNull(bigIntegerValue);
    }
}