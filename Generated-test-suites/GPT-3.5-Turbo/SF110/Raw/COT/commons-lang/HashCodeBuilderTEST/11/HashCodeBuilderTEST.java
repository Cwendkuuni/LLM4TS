import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    
    @Test
    public void testReflectionHashCode() {
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new TestObject());
        assertEquals(123456789, hashCode);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeNullObject() {
        HashCodeBuilder.reflectionHashCode(17, 37, null);
    }
    
    @Test
    public void testReflectionHashCodeWithTransients() {
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new TestObject(), true);
        assertEquals(123456789, hashCode);
    }
    
    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new TestObject(), false, TestObject.class, "field2");
        assertEquals(123456789, hashCode);
    }
    
    @Test
    public void testReflectionHashCodeWithCollectionExcludeFields() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new TestObject(), "field2");
        assertEquals(123456789, hashCode);
    }
    
    @Test
    public void testReflectionHashCodeWithArrayExcludeFields() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new TestObject(), new String[] {"field2"});
        assertEquals(123456789, hashCode);
    }
    
    @Test
    public void testHashCodeBuilderDefaultConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertNotNull(builder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderOddInitialValue() {
        new HashCodeBuilder(2, 37);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderOddMultiplier() {
        new HashCodeBuilder(17, 2);
    }
    
    @Test
    public void testHashCodeBuilderAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[] {true, false});
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[] {1, 2});
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[] {'a', 'b'});
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.23);
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[] {1.23, 4.56});
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.23f);
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[] {1.23f, 4.56f});
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123);
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[] {123, 456});
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123L);
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[] {123L, 456L});
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new TestObject());
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new TestObject[] {new TestObject()});
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 123);
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[] {123, 456});
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testHashCodeBuilderAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(123456789);
        assertEquals(37, builder.toHashCode());
    }
    
    static class TestObject {
        private int field1 = 1;
        private int field2 = 2;
        
        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }
    }

}