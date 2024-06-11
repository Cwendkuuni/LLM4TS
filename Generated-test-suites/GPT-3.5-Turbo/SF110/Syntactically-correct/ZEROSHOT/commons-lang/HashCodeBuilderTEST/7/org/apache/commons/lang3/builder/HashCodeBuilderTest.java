import org.apache.commons.lang3.builder.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.*;

public class HashCodeBuilderTest {
    
    private HashCodeBuilder builder;
    
    @Before
    public void setUp() {
        builder = new HashCodeBuilder();
    }
    
    @Test
    public void testAppendBoolean() {
        builder.append(true);
        int expected = 17 * 37 + 0;
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendBooleanArray() {
        builder.append(new boolean[]{true, false, true});
        int expected = 17 * 37 + (0 * 37 + (1 * 37 + 0));
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendByte() {
        builder.append((byte)1);
        int expected = 17 * 37 + 1;
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendByteArray() {
        builder.append(new byte[]{1, 2, 3});
        int expected = 17 * 37 + (1 * 37 + (2 * 37 + 3));
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendChar() {
        builder.append('a');
        int expected = 17 * 37 + 'a';
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendCharArray() {
        builder.append(new char[]{'a', 'b', 'c'});
        int expected = 17 * 37 + ('a' * 37 + ('b' * 37 + 'c'));
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendDouble() {
        builder.append(1.5);
        long bits = Double.doubleToLongBits(1.5);
        int expected = 17 * 37 + (int)(bits ^ bits >> 32);
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendDoubleArray() {
        builder.append(new double[]{1.5, 2.5, 3.5});
        int expected = 17 * 37 + ((int)(Double.doubleToLongBits(1.5) ^ Double.doubleToLongBits(1.5) >> 32) * 37 + ((int)(Double.doubleToLongBits(2.5) ^ Double.doubleToLongBits(2.5) >> 32) * 37 + (int)(Double.doubleToLongBits(3.5) ^ Double.doubleToLongBits(3.5) >> 32)));
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendFloat() {
        builder.append(1.5f);
        int expected = 17 * 37 + Float.floatToIntBits(1.5f);
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendFloatArray() {
        builder.append(new float[]{1.5f, 2.5f, 3.5f});
        int expected = 17 * 37 + (Float.floatToIntBits(1.5f) * 37 + (Float.floatToIntBits(2.5f) * 37 + Float.floatToIntBits(3.5f)));
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendInt() {
        builder.append(3);
        int expected = 17 * 37 + 3;
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendIntArray() {
        builder.append(new int[]{1, 2, 3});
        int expected = 17 * 37 + (1 * 37 + (2 * 37 + 3));
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendLong() {
        builder.append(10L);
        int expected = 17 * 37 + (int)(10L ^ 10L >> 32);
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendLongArray() {
        builder.append(new long[]{1L, 2L, 3L});
        int expected = 17 * 37 + ((int)(1L ^ 1L >> 32) * 37 + ((int)(2L ^ 2L >> 32) * 37 + (int)(3L ^ 3L >> 32)));
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendObject() {
        builder.append("test");
        int expected = 17 * 37 + "test".hashCode();
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendObjectArray() {
        builder.append(new Object[]{"test", true, 1});
        int expected = 17 * 37 + ("test".hashCode() * 37 + (0 * 37 + 1));
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendShort() {
        builder.append((short)5);
        int expected = 17 * 37 + 5;
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendShortArray() {
        builder.append(new short[]{1, 2, 3});
        int expected = 17 * 37 + (1 * 37 + (2 * 37 + 3));
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testAppendSuper() {
        builder.appendSuper(10);
        int expected = 17 * 37 + 10;
        Assert.assertEquals(expected, builder.toHashCode());
    }
    
    @Test
    public void testBuild() {
        Integer result = builder.build();
        Assert.assertEquals(builder.toHashCode(), result.intValue());
    }
    
    @Test
    public void testHashCode() {
        int hash1 = builder.hashCode();
        int hash2 = builder.toHashCode();
        Assert.assertEquals(hash1, hash2);
    }
    
}