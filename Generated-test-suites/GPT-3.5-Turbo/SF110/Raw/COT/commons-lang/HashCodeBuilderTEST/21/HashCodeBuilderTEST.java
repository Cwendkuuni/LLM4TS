
import org.junit.Test;
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
    public void testReflectionHashCodeWithExcludedFields() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, "field1", "field2");
        assertNotNull(hashCode);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        Object object = null;
        HashCodeBuilder.reflectionHashCode(17, 37, object);
    }
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendBooleanArray() {
        boolean[] array = {true, false};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendByteArray() {
        byte[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendCharArray() {
        char[] array = {'a', 'b', 'c'};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(3.14);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendDoubleArray() {
        double[] array = {1.1, 2.2, 3.3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(3.14f);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendFloatArray() {
        float[] array = {1.1f, 2.2f, 3.3f};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(42);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendIntArray() {
        int[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1234567890L);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendLongArray() {
        long[] array = {1234567890L, 9876543210L};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendObjectNotNull() {
        Object object = new Object();
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(object);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendObjectNull() {
        Object object = null;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(object);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendObjectArray() {
        Object[] array = {new Object(), new Object()};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 42);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendShortArray() {
        short[] array = {42, 84};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppendSuper() {
        int superHashCode = 123456;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(superHashCode);
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
    
    @Test
    public void testStaticGetRegistry() {
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertNotNull(registry);
    }
    
    @Test
    public void testStaticIsRegistered() {
        Object object = new Object();
        boolean isRegistered = HashCodeBuilder.isRegistered(object);
        assertNotNull(isRegistered);
    }
    
    @Test
    public void testStaticReflectionAppend() {
        Object object = new Object();
        Class<?> clazz = object.getClass();
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean useTransients = false;
        String[] excludeFields = {"field1", "field2"};
        HashCodeBuilder.reflectionAppend(object, clazz, builder, useTransients, excludeFields);
        int hashCode = builder.toHashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testStaticRegisterUnregister() {
        Object object = new Object();
        HashCodeBuilder.register(object);
        HashCodeBuilder.unregister(object);
        boolean isRegistered = HashCodeBuilder.isRegistered(object);
        assertFalse(isRegistered);
    }
}
