import org.junit.Test;
import org.junit.Assert;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode_defaultValues() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false);
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testReflectionHashCode_testTransients() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, true);
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testReflectionHashCode_reflectUpToClass() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false, Object.class);
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCode_nullObject() {
        int hashCode = HashCodeBuilder.reflectionHashCode(null, false);
    }
    
    @Test
    public void testReflectionHashCode_excludeFields() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, new String[]{"field1", "field2"});
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testReflectionHashCode_noNullStringArray() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, "field1", "field2");
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testHashCode_defaultConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        builder.append((byte) 1);
        builder.append('a');
        builder.append(1.5);
        builder.append(1.0f);
        builder.append(1);
        builder.append(1L);
        builder.append(new Object());
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testHashCode_parameterizedConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        builder.append(true);
        builder.append((byte) 1);
        builder.append('a');
        builder.append(1.5);
        builder.append(1.0f);
        builder.append(1);
        builder.append(1L);
        builder.append(new Object());
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_boolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_booleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[]{true, false});
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_byte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_byteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[]{1, 2});
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_char() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_charArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[]{'a', 'b'});
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_double() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.5);
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_doubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[]{1.0, 2.0});
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_float() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0f);
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_floatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[]{1.0f, 2.0f});
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_int() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_intArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[]{1, 2});
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_long() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_longArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[]{1L, 2L});
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_object() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object());
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_objectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object[]{new Object(), new Object()});
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_short() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppend_shortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[]{(short) 1, (short) 2});
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(123);
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testToHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashCode = builder.toHashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer hashCode = builder.build();
        Assert.assertNotEquals(0, hashCode);
    }

}