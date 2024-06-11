import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        Short value = Short.valueOf("123");
        try {
            shortSerializer.serialize(value, null, null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = 123;
        try {
            integerSerializer.serialize(value, null, null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        Number value = 123;
        try {
            intLikeSerializer.serialize(value, null, null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);
        Long value = 123L;
        try {
            longSerializer.serialize(value, null, null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        Float value = 123.45f;
        try {
            floatSerializer.serialize(value, null, null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = 123.45;
        try {
            doubleSerializer.serialize(value, null, null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }
}