import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean value1 = true;
        boolean value2 = false;
        builder.append(value1);
        builder.append(value2);
        int hashCode = builder.toHashCode();
        assert hashCode == 622;
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = {true, false};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assert hashCode == 622;
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte value = 10;
        builder.append(value);
        int hashCode = builder.toHashCode();
        assert hashCode == 380;
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] array = {10, 20};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assert hashCode == 8930;
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char value = 'a';
        builder.append(value);
        int hashCode = builder.toHashCode();
        assert hashCode == 428;
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] array = {'a', 'b'};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assert hashCode == 14175;
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double value = 1.5;
        builder.append(value);
        int hashCode = builder.toHashCode();
        assert hashCode == 19;
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] array = {1.5, 2.5};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assert hashCode == 116;
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float value = 1.5f;
        builder.append(value);
        int hashCode = builder.toHashCode();
        assert hashCode == 31;
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] array = {1.5f, 2.5f};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assert hashCode == 75;
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int value = 10;
        builder.append(value);
        int hashCode = builder.toHashCode();
        assert hashCode == 377;
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] array = {10, 20};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assert hashCode == 19474;
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long value = 10000000000L;
        builder.append(value);
        int hashCode = builder.toHashCode();
        assert hashCode == -16745516;
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] array = {10000000000L, 20000000000L};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assert hashCode == 6698518;
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object object = new Object();
        builder.append(object);
        int hashCode = builder.toHashCode();
        assert hashCode == System.identityHashCode(object);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] array = {new Object(), new Object()};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assert hashCode == 993;
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short value = 10;
        builder.append(value);
        int hashCode = builder.toHashCode();
        assert hashCode == 672;
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] array = {10, 20};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assert hashCode == 19506;
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 100;
        builder.appendSuper(superHashCode);
        int hashCode = builder.toHashCode();
        assert hashCode == 3737;
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer hashCode = builder.build();
        assert hashCode == builder.toHashCode();
    }

    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashCode = builder.hashCode();
        assert hashCode == builder.toHashCode();
    }
    
    @Test
    public void testReflectionHashCode() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false);
        assert hashCode == System.identityHashCode(object);
    }
    
    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object object = new Object();
        String[] excludeFields = {"field1", "field2"};
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, object, false, Object.class, excludeFields);
        assert hashCode == System.identityHashCode(object);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        Object object = null;
        HashCodeBuilder.reflectionHashCode(object, false);
    }
    
    @Test
    public void testReflectionHashCodeWithCollectionExcludeFields() {
        Object object = new Object();
        Set<String> excludeFields = new HashSet<>(Arrays.asList("field1", "field2"));
        int hashCode = HashCodeBuilder.reflectionHashCode(object, excludeFields);
        assert hashCode == System.identityHashCode(object);
    }
    
    @Test
    public void testReflectionHashCodeWithArrayExcludeFields() {
        Object object = new Object();
        String[] excludeFields = {"field1", "field2"};
        int hashCode = HashCodeBuilder.reflectionHashCode(object, excludeFields);
        assert hashCode == System.identityHashCode(object);
    }
    
    @Test
    public void testRegisterAndUnregister() {
        Object object = new Object();
        HashCodeBuilder.register(object);
        boolean isRegistered = HashCodeBuilder.isRegistered(object);
        assert isRegistered;
        HashCodeBuilder.unregister(object);
        isRegistered = HashCodeBuilder.isRegistered(object);
        assert !isRegistered;
    }

}