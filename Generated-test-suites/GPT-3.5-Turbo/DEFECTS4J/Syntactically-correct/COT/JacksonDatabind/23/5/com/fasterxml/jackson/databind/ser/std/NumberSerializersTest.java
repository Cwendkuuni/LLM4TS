
import org.junit.Assert;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 10;
        String expected = "10";
        Assert.assertEquals(expected, serializer.serialize(value));
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        Integer value = 100;
        String expected = "100";
        Assert.assertEquals(expected, serializer.serialize(value));
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Number value = 50;
        String expected = "50";
        Assert.assertEquals(expected, serializer.serialize(value));
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer();
        Long value = 1000L;
        String expected = "1000";
        Assert.assertEquals(expected, serializer.serialize(value));
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float value = 3.14f;
        String expected = "3.14";
        Assert.assertEquals(expected, serializer.serialize(value));
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();
        Double value = 3.14159265359;
        String expected = "3.14159265359";
        Assert.assertEquals(expected, serializer.serialize(value));
    }
}
