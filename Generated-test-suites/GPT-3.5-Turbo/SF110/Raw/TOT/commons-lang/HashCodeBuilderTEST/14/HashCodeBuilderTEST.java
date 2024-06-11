import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    private HashCodeBuilder hashCodeBuilder;

    @Before
    public void setUp() {
        hashCodeBuilder = new HashCodeBuilder();
    }

    @Test
    public void append_boolean_shouldAddValueToHashCode() {
        hashCodeBuilder.append(true);
        assertEquals(37, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_booleanArray_withNullArray_shouldMultiplyTheTotalByConstant() {
        boolean[] arr = null;
        hashCodeBuilder.append(arr);
        assertEquals(0, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_booleanArray_shouldAddEachElementToHashCode() {
        boolean[] arr = {true, false, true};
        hashCodeBuilder.append(arr);
        assertEquals(24529, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_byte_shouldAddValueToHashCode() {
        hashCodeBuilder.append((byte) 100);
        assertEquals(100, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_byteArray_withNullArray_shouldMultiplyTheTotalByConstant() {
        byte[] arr = null;
        hashCodeBuilder.append(arr);
        assertEquals(0, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_byteArray_shouldAddEachElementToHashCode() {
        byte[] arr = {1, 2, 3};
        hashCodeBuilder.append(arr);
        assertEquals(1869564, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_char_shouldAddValueToHashCode() {
        hashCodeBuilder.append('a');
        assertEquals(97, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_charArray_withNullArray_shouldMultiplyTheTotalByConstant() {
        char[] arr = null;
        hashCodeBuilder.append(arr);
        assertEquals(0, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_charArray_shouldAddEachElementToHashCode() {
        char[] arr = {'a', 'b', 'c'};
        hashCodeBuilder.append(arr);
        assertEquals(94371, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_double_shouldAddValueToHashCode() {
        hashCodeBuilder.append(3.1415);
        assertEquals(858925821, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_doubleArray_withNullArray_shouldMultiplyTheTotalByConstant() {
        double[] arr = null;
        hashCodeBuilder.append(arr);
        assertEquals(0, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_doubleArray_shouldAddEachElementToHashCode() {
        double[] arr = {1.1, 2.2, 3.3};
        hashCodeBuilder.append(arr);
        assertEquals(32759629, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_float_shouldAddValueToHashCode() {
        hashCodeBuilder.append(1.23f);
        assertEquals(43223135, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_floatArray_withNullArray_shouldMultiplyTheTotalByConstant() {
        float[] arr = null;
        hashCodeBuilder.append(arr);
        assertEquals(0, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_floatArray_shouldAddEachElementToHashCode() {
        float[] arr = {1.1f, 2.2f, 3.3f};
        hashCodeBuilder.append(arr);
        assertEquals(13429166, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_int_shouldAddValueToHashCode() {
        hashCodeBuilder.append(42);
        assertEquals(150, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_intArray_withNullArray_shouldMultiplyTheTotalByConstant() {
        int[] arr = null;
        hashCodeBuilder.append(arr);
        assertEquals(0, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_intArray_shouldAddEachElementToHashCode() {
        int[] arr = {1, 2, 3};
        hashCodeBuilder.append(arr);
        assertEquals(163944556, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_long_shouldAddValueToHashCode() {
        hashCodeBuilder.append(1234567890L);
        assertEquals(430673697, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_longArray_withNullArray_shouldMultiplyTheTotalByConstant() {
        long[] arr = null;
        hashCodeBuilder.append(arr);
        assertEquals(0, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_longArray_shouldAddEachElementToHashCode() {
        long[] arr = {1000000000L, 2000000000L, 3000000000L};
        hashCodeBuilder.append(arr);
        assertEquals(771132229, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_object_shouldAddHashCodeOfObject() {
        TestObject obj = new TestObject(42);
        hashCodeBuilder.append(obj);
        assertEquals(16777739, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_objectArray_withNullArray_shouldMultiplyTheTotalByConstant() {
        Object[] arr = null;
        hashCodeBuilder.append(arr);
        assertEquals(0, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_objectArray_withPrimitiveArray_shouldAddEachElementToHashCode() {
        int[] intArr = {1, 2, 3};
        Object[] arr = {intArr, "Hello"};
        hashCodeBuilder.append(arr);
        assertEquals(47062, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_objectArray_shouldAddEachElementToHashCode() {
        Object[] arr = {new TestObject(1), new TestObject(2)};
        hashCodeBuilder.append(arr);
        assertEquals(12975965, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_short_shouldAddValueToHashCode() {
        hashCodeBuilder.append((short) 10);
        assertEquals(370, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_shortArray_withNullArray_shouldMultiplyTheTotalByConstant() {
        short[] arr = null;
        hashCodeBuilder.append(arr);
        assertEquals(0, hashCodeBuilder.toHashCode());
    }

    @Test
    public void append_shortArray_shouldAddEachElementToHashCode() {
        short[] arr = {1, 2, 3};
        hashCodeBuilder.append(arr);
        assertEquals(12802, hashCodeBuilder.toHashCode());
    }

    @Test
    public void appendSuper_shouldAddSuperHashCodeToTotal() {
        hashCodeBuilder.appendSuper(123456);
        assertEquals(461267408, hashCodeBuilder.toHashCode());
    }

    private static class TestObject {
        private final int value;

        TestObject(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value;
        }
    }
}