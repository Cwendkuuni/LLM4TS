
import org.junit.Test;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HashCodeBuilderTest {
    
    @Test
    public void testReflectionHashCode() {
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object());
        // Add assertion here
    }
    
    @Test
    public void testReflectionHashCodeWithTransients() {
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), true);
        // Add assertion here
    }
    
    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, Object.class, "fieldName");
        // Add assertion here
    }
    
    @Test
    public void testReflectionHashCodeWithExcludeFieldsList() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new Object(), Arrays.asList("fieldName"));
        // Add assertion here
    }
    
    @Test
    public void testReflectionHashCodeWithExcludeFieldsArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new Object(), "fieldName");
        // Add assertion here
    }
    
    @Test
    public void testHashCodeBuilderDefault() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testHashCodeBuilderWithInitialAndMultiplier() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        builder.append(true);
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[] { true, false });
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[] { 1, 2 });
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[] { 'a', 'b' });
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0);
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[] { 1.0, 2.0 });
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0f);
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[] { 1.0f, 2.0f });
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[] { 1, 2 });
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[] { 1L, 2L });
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object());
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object[] { new Object(), new Object() });
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[] { (short) 1, (short) 2 });
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(123);
        int hashCode = builder.toHashCode();
        // Add assertion here
    }
    
    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer hashCode = builder.build();
        // Add assertion here
    }
    
    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashCode = builder.hashCode();
        // Add assertion here
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithEvenInitialValue() {
        HashCodeBuilder builder = new HashCodeBuilder(16, 37);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithEvenMultiplier() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 38);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithNullArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte[]) null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithNullObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((Object) null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithNullObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((Object[]) null);
    }
    
}

