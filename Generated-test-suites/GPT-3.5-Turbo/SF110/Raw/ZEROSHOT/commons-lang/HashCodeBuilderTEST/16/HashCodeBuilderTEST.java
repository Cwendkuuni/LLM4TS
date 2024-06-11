import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void getRegistry_shouldReturnRegistry() {
        assertNotNull(HashCodeBuilder.getRegistry());
    }

    @Test
    public void isRegistered_withNullValue_shouldReturnFalse() {
        assertFalse(HashCodeBuilder.isRegistered(null));
    }

    @Test
    public void isRegistered_withRegisteredValue_shouldReturnTrue() {
        Object value = new Object();
        HashCodeBuilder.register(value);
        assertTrue(HashCodeBuilder.isRegistered(value));
        HashCodeBuilder.unregister(value);
    }

    @Test
    public void reflectionAppend_withRegisteredValue_shouldSkipAppending() {
        Object value = new Object();
        HashCodeBuilder.register(value);
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder.reflectionAppend(value, value.getClass(), builder, false, new String[0]);
        assertEquals(builder.toHashCode(), 17);
        HashCodeBuilder.unregister(value);
    }

    @Test
    public void reflectionHashCode_withObjectAndExcludeFields_shouldReturnHashCode() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, "field1", "field2");
        assertEquals(17, hashCode);
    }

    @Test
    public void reflectionHashCode_withObjectAndExcludeFieldsList_shouldReturnHashCode() {
        Object object = new Object();
        Collection<String> excludeFields = new HashSet<>(Arrays.asList("field1", "field2"));
        int hashCode = HashCodeBuilder.reflectionHashCode(object, excludeFields);
        assertEquals(17, hashCode);
    }

    @Test
    public void register_withNewValue_shouldAddToRegistry() {
        Object value = new Object();
        HashCodeBuilder.register(value);
        assertTrue(HashCodeBuilder.getRegistry().contains(new HashCodeBuilder.IDKey(value)));
        HashCodeBuilder.unregister(value);
    }

    @Test
    public void unregister_withRegisteredValue_shouldRemoveFromRegistry() {
        Object value = new Object();
        HashCodeBuilder.register(value);
        HashCodeBuilder.unregister(value);
        assertFalse(HashCodeBuilder.getRegistry().contains(new HashCodeBuilder.IDKey(value)));
    }

    @Test
    public void unregister_withoutRegisteredValue_shouldNotThrowException() {
        Object value = new Object();
        HashCodeBuilder.unregister(value);
    }

    @Test
    public void constructor_withNoArguments_shouldInitializeFields() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(builder.toHashCode(), 17);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withEvenInitialOddNumber_shouldThrowException() {
        new HashCodeBuilder(14, 37);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withEvenMultiplierOddNumber_shouldThrowException() {
        new HashCodeBuilder(17, 40);
    }

    @Test(expected = IllegalArgumentException.class)
    public void append_withEvenMultiplierOddNumber_shouldThrowException() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
    }

    @Test
    public void append_withBoolean_shouldAppendValueToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(builder.toHashCode(), 37);
    }

    @Test
    public void append_withBooleanArray_shouldAppendValuesToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = {true, false};
        builder.append(array);
        assertEquals(builder.toHashCode(), 73);
    }

    @Test
    public void append_withByte_shouldAppendValueToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        assertEquals(builder.toHashCode(), 54);
    }

    @Test
    public void append_withByteArray_shouldAppendValuesToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] array = {1, 2};
        builder.append(array);
        assertEquals(builder.toHashCode(), 178);
    }

    @Test
    public void append_withChar_shouldAppendValueToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assertEquals(builder.toHashCode(), 165);
    }

    @Test
    public void append_withCharArray_shouldAppendValuesToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] array = {'a', 'b'};
        builder.append(array);
        assertEquals(builder.toHashCode(), 5550);
    }

    @Test
    public void append_withDouble_shouldAppendValueToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0);
        assertEquals(builder.toHashCode(), 9216);
    }

    @Test
    public void append_withDoubleArray_shouldAppendValuesToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] array = {1.0, 2.0};
        builder.append(array);
        assertEquals(builder.toHashCode(), 59536);
    }

    @Test
    public void append_withFloat_shouldAppendValueToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0f);
        assertEquals(builder.toHashCode(), 59133);
    }

    @Test
    public void append_withFloatArray_shouldAppendValuesToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] array = {1.0f, 2.0f};
        builder.append(array);
        assertEquals(builder.toHashCode(), 1139006);
    }

    @Test
    public void append_withInt_shouldAppendValueToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        assertEquals(builder.toHashCode(), 54);
    }

    @Test
    public void append_withIntArray_shouldAppendValuesToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] array = {1, 2};
        builder.append(array);
        assertEquals(builder.toHashCode(), 178);
    }

    @Test
    public void append_withLong_shouldAppendValueToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        assertEquals(builder.toHashCode(), 2050);
    }

    @Test
    public void append_withLongArray_shouldAppendValuesToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] array = {1L, 2L};
        builder.append(array);
        assertEquals(builder.toHashCode(), 66626);
    }

    @Test
    public void append_withNullObject_shouldAppendToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((String) null);
        assertEquals(builder.toHashCode(), 17);
    }

    @Test
    public void append_withObject_shouldAppendValueToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append("Test");
        assertEquals(builder.toHashCode(), 2001);
    }

    @Test
    public void append_withObjectArray_shouldAppendValuesToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] array = {1, "Test"};
        builder.append(array);
        assertEquals(builder.toHashCode(), 28569397);
    }

    @Test
    public void append_withShort_shouldAppendValueToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        assertEquals(builder.toHashCode(), 54);
    }

    @Test
    public void append_withShortArray_shouldAppendValuesToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] array = {1, 2};
        builder.append(array);
        assertEquals(builder.toHashCode(), 178);
    }

    @Test
    public void appendSuper_withHashCode_shouldAppendToTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(1234);
        assertEquals(builder.toHashCode(), 42535);
    }

    @Test
    public void toHashCode_shouldReturnTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        assertEquals(builder.toHashCode(), builder.toHashCode());
    }

    @Test
    public void build_shouldReturnTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        assertEquals(builder.toHashCode(), (int) builder.build());
    }

    @Test
    public void hashCode_shouldReturnTotal() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        assertEquals(builder.toHashCode(), builder.hashCode());
    }

}