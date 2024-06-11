import org.junit.Test;
import java.util.HashSet;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object);
        assertNotNull(hashCode);
    }

    @Test
    public void testReflectionHashCodeWithTransients() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, true);
        assertNotNull(hashCode);
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object object = new Object();
        String[] excludeFields = {"field1", "field2"};
        int hashCode = HashCodeBuilder.reflectionHashCode(object, excludeFields);
        assertNotNull(hashCode);
    }

    @Test
    public void testReflectionHashCodeWithReflectUpToClass() {
        Object object = new Object();
        Class<? super Object> reflectUpToClass = Object.class;
        int hashCode = HashCodeBuilder.reflectionHashCode(object, reflectUpToClass);
        assertNotNull(hashCode);
    }

    @Test
    public void testReflectiionHashCodeWithAllParameters() {
        Object object = new Object();
        int initialNonZeroOddNumber = 123;
        int multiplierNonZeroOddNumber = 789;
        String[] excludeFields = {"field1", "field2"};
        Class<? super Object> reflectUpToClass = Object.class;
        int hashCode = HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, true, reflectUpToClass, excludeFields);
        assertNotNull(hashCode);
    }

    @Test
    public void testReflectionHashCodeWithCollection() {
        Object object = new Object();
        HashSet<String> excludeFields = new HashSet<>();
        excludeFields.add("field1");
        excludeFields.add("field2");
        int hashCode = HashCodeBuilder.reflectionHashCode(object, excludeFields);
        assertNotNull(hashCode);
    }

    @Test
    public void testHashCodeBuilderDefaultConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testHashCodeBuilderConstructorWithParameters() {
        int initialOddNumber = 123;
        int multiplierOddNumber = 789;
        HashCodeBuilder builder = new HashCodeBuilder(initialOddNumber, multiplierOddNumber);
        assertNotNull(builder);
    }

    @Test
    public void testAppendBoolean() {
        boolean value = true;
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(value);
        assertNotNull(result);
    }

    @Test
    public void testAppendBooleanArray() {
        boolean[] array = {true, false, true};
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(array);
        assertNotNull(result);
    }

    @Test
    public void testAppendByte() {
        byte value = 123;
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(value);
        assertNotNull(result);
    }

    @Test
    public void testAppendByteArray() {
        byte[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(array);
        assertNotNull(result);
    }

    @Test
    public void testAppendChar() {
        char value = 'a';
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(value);
        assertNotNull(result);
    }

    @Test
    public void testAppendCharArray() {
        char[] array = {'a', 'b', 'c'};
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(array);
        assertNotNull(result);
    }

    @Test
    public void testAppendDouble() {
        double value = 3.14;
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(value);
        assertNotNull(result);
    }

    @Test
    public void testAppendDoubleArray() {
        double[] array = {1.1, 2.2, 3.3};
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(array);
        assertNotNull(result);
    }

    @Test
    public void testAppendFloat() {
        float value = 1.23f;
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(value);
        assertNotNull(result);
    }

    @Test
    public void testAppendFloatArray() {
        float[] array = {1.23f, 4.56f, 7.89f};
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(array);
        assertNotNull(result);
    }

    @Test
    public void testAppendInt() {
        int value = 123;
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(value);
        assertNotNull(result);
    }

    @Test
    public void testAppendIntArray() {
        int[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(array);
        assertNotNull(result);
    }

    @Test
    public void testAppendLong() {
        long value = 1234567890;
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(value);
        assertNotNull(result);
    }

    @Test
    public void testAppendLongArray() {
        long[] array = {1234567890, 9876543210};
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(array);
        assertNotNull(result);
    }

    @Test
    public void testAppendObject() {
        Object object = new Object();
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(object);
        assertNotNull(result);
    }

    @Test
    public void testAppendObjectArray() {
        Object[] array = {new Object(), new Object()};
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(array);
        assertNotNull(result);
    }

    @Test
    public void testAppendShort() {
        short value = 123;
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(value);
        assertNotNull(result);
    }

    @Test
    public void testAppendShortArray() {
        short[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append(array);
        assertNotNull(result);
    }

    @Test
    public void testAppendSuper() {
        int superHashCode = 12345;
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.appendSuper(superHashCode);
        assertNotNull(result);
    }

    @Test
    public void testToHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer hashCode = builder.build();
        assertNotNull(hashCode);
    }

    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashCode = builder.hashCode();
        assertNotNull(hashCode);
    }

}