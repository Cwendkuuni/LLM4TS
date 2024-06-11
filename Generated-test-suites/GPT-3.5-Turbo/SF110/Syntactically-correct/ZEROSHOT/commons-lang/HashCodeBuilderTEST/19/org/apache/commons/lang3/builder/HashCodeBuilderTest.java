import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppend_boolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(17 * 37 + 0, builder.toHashCode());
        builder.append(false);
        assertEquals((17 * 37 + 0) * 37 + 1, builder.toHashCode());
    }

    @Test
    public void testAppend_booleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((boolean[]) null);
        assertEquals(17 * 37, builder.toHashCode());
        builder.append(new boolean[]{true, false});
        assertEquals((17 * 37) * 37 + (true ? 0 : 1), builder.toHashCode());
    }

    @Test
    public void testAppend_byte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        assertEquals(17 * 37 + 1, builder.toHashCode());
        builder.append((byte) 2);
        assertEquals((17 * 37 + 1) * 37 + 2, builder.toHashCode());
    }

    @Test
    public void testAppend_byteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte[]) null);
        assertEquals(17 * 37, builder.toHashCode());
        builder.append(new byte[]{1, 2});
        assertEquals((17 * 37) * 37 + 1, builder.toHashCode());
    }

    @Test
    public void testAppend_char() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assertEquals(17 * 37 + 'a', builder.toHashCode());
        builder.append('b');
        assertEquals((17 * 37 + 'a') * 37 + 'b', builder.toHashCode());
    }

    @Test
    public void testAppend_charArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((char[]) null);
        assertEquals(17 * 37, builder.toHashCode());
        builder.append(new char[]{'a', 'b'});
        assertEquals((17 * 37) * 37 + 'a', builder.toHashCode());
    }

    @Test
    public void testAppend_double() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.23);
        long doubleToLongBits = Double.doubleToLongBits(1.23);
        assertEquals(17 * 37 + (int) (doubleToLongBits ^ doubleToLongBits >> 32), builder.toHashCode());
        builder.append(4.56);
        doubleToLongBits = Double.doubleToLongBits(4.56);
        assertEquals((17 * 37 + (int) (doubleToLongBits ^ doubleToLongBits >> 32)) * 37 + (int) (doubleToLongBits ^ doubleToLongBits >> 32), builder.toHashCode());
    }

    @Test
    public void testAppend_doubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((double[]) null);
        assertEquals(17 * 37, builder.toHashCode());
        builder.append(new double[]{1.23, 4.56});
        long doubleToLongBits = Double.doubleToLongBits(1.23);
        assertEquals((17 * 37) * 37 + (int) (doubleToLongBits ^ doubleToLongBits >> 32), builder.toHashCode());
    }

    @Test
    public void testAppend_float() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.23f);
        assertEquals(17 * 37 + Float.floatToIntBits(1.23f), builder.toHashCode());
        builder.append(4.56f);
        assertEquals((17 * 37 + Float.floatToIntBits(1.23f)) * 37 + Float.floatToIntBits(4.56f), builder.toHashCode());
    }

    @Test
    public void testAppend_floatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((float[]) null);
        assertEquals(17 * 37, builder.toHashCode());
        builder.append(new float[]{1.23f, 4.56f});
        assertEquals((17 * 37) * 37 + Float.floatToIntBits(1.23f), builder.toHashCode());
    }

    @Test
    public void testAppend_int() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123);
        assertEquals(17 * 37 + 123, builder.toHashCode());
        builder.append(456);
        assertEquals((17 * 37 + 123) * 37 + 456, builder.toHashCode());
    }

    @Test
    public void testAppend_intArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((int[]) null);
        assertEquals(17 * 37, builder.toHashCode());
        builder.append(new int[]{123, 456});
        assertEquals((17 * 37) * 37 + 123, builder.toHashCode());
    }

    @Test
    public void testAppend_long() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123L);
        assertEquals(17 * 37 + (int)(123L ^ 123L >> 32), builder.toHashCode());
        builder.append(456L);
        assertEquals((17 * 37 + (int)(123L ^ 123L >> 32)) * 37 + (int)(456L ^ 456L >> 32), builder.toHashCode());
    }

    @Test
    public void testAppend_longArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((long[]) null);
        assertEquals(17 * 37, builder.toHashCode());
        builder.append(new long[]{123L, 456L});
        assertEquals((17 * 37) * 37 + (int)(123L ^ 123L >> 32), builder.toHashCode());
    }

    @Test
    public void testAppend_Object() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((Object) null);
        assertEquals(17 * 37, builder.toHashCode());
        builder.append(new Object());
        assertEquals((17 * 37) * 37 + new Object().hashCode(), builder.toHashCode());
    }

    @Test
    public void testAppend_ObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((Object[]) null);
        assertEquals(17 * 37, builder.toHashCode());
        builder.append(new Object[]{new Object(), new Object()});
        assertEquals((17 * 37) * 37 + new Object().hashCode(), builder.toHashCode());
    }

    @Test
    public void testAppend_short() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        assertEquals(17 * 37 + 1, builder.toHashCode());
        builder.append((short) 2);
        assertEquals((17 * 37 + 1) * 37 + 2, builder.toHashCode());
    }

    @Test
    public void testAppend_shortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short[]) null);
        assertEquals(17 * 37, builder.toHashCode());
        builder.append(new short[]{1, 2});
        assertEquals((17 * 37) * 37 + 1, builder.toHashCode());
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(123);
        assertEquals(17 * 37 + 123, builder.toHashCode());
        builder.appendSuper(456);
        assertEquals((17 * 37 + 123) * 37 + 456, builder.toHashCode());
    }

    @Test
    public void testToHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123);
        assertEquals(17 * 37 + 123, builder.toHashCode());
        builder.append(456);
        assertEquals((17 * 37 + 123) * 37 + 456, builder.toHashCode());
        assertEquals((17 * 37 + 123) * 37 + 456, builder.hashCode());
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123);
        assertEquals(Integer.valueOf(17 * 37 + 123), builder.build());
    }
}