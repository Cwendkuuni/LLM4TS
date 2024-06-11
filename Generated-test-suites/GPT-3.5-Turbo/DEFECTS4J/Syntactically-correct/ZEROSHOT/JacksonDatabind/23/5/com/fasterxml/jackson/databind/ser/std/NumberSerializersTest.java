import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    private NumberSerializers.IntegerSerializer integerSerializer;
    private NumberSerializers.ShortSerializer shortSerializer;
    private NumberSerializers.IntLikeSerializer intLikeSerializer;
    private NumberSerializers.LongSerializer longSerializer;
    private NumberSerializers.FloatSerializer floatSerializer;
    private NumberSerializers.DoubleSerializer doubleSerializer;

    @Before
    public void setUp() {
        integerSerializer = new NumberSerializers.IntegerSerializer();
        shortSerializer = new NumberSerializers.ShortSerializer();
        intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        longSerializer = new NumberSerializers.LongSerializer();
        floatSerializer = new NumberSerializers.FloatSerializer();
        doubleSerializer = new NumberSerializers.DoubleSerializer();
    }

    @Test
    public void testIntegerSerializerSerialize() {
        Integer value = 100;
        String expected = "100";
        try {
            assertEquals(expected, integerSerializer.serialize(value, null, null));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testShortSerializerSerialize() {
        Short value = 100;
        String expected = "100";
        try {
            assertEquals(expected, shortSerializer.serialize(value, null, null));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testIntLikeSerializerSerialize() {
        Number value = 100;
        String expected = "100";
        try {
            assertEquals(expected, intLikeSerializer.serialize(value, null, null));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLongSerializerSerialize() {
        Long value = 100L;
        String expected = "100";
        try {
            assertEquals(expected, longSerializer.serialize(value, null, null));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testFloatSerializerSerialize() {
        Float value = 3.14f;
        String expected = "3.14";
        try {
            assertEquals(expected, floatSerializer.serialize(value, null, null));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testDoubleSerializerSerialize() {
        Double value = 3.14;
        String expected = "3.14";
        try {
            assertEquals(expected, doubleSerializer.serialize(value, null, null));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}