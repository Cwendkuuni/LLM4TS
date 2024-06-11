
import org.junit.Assert;
import org.junit.Test;

public class PrimitivesTest {
    
    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(Boolean.class);
        Assert.assertEquals(boolean.class, booleanClass);

        Class<Character> characterClass = Primitives.primitiveTypeOf(Character.class);
        Assert.assertEquals(char.class, characterClass);

        // repeat this for all other wrapper types
    }

    @Test
    public void testIsPrimitiveWrapper() {
        boolean isBooleanWrapper = Primitives.isPrimitiveWrapper(Boolean.class);
        Assert.assertTrue(isBooleanWrapper);

        boolean isStringWrapper = Primitives.isPrimitiveWrapper(String.class);
        Assert.assertFalse(isStringWrapper);

        // repeat this for all other wrapper types
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(boolean.class);
        Assert.assertEquals(Boolean.FALSE, booleanWrapper);

        Character characterWrapper = Primitives.primitiveWrapperOf(char.class);
        Assert.assertEquals(Character.valueOf('\u0000'), characterWrapper);

        // repeat this for all other primitive types
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(Boolean.class);
        Assert.assertNull(booleanValue);

        // repeat this for all other primitive types
    }
}
