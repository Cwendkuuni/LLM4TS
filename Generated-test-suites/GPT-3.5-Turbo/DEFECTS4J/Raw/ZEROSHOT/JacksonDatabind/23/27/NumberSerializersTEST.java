import org.junit.Assert;
import org.junit.Test;

public class NumberSerializersTest {
    
    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 10;
        Assert.assertEquals("10", serializer.serialize(value));
    }
    
    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        Integer value = 10;
        Assert.assertEquals("10", serializer.serialize(value));
    }
    
    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Byte value = 10;
        Assert.assertEquals("10", serializer.serialize(value));
    }
    
    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer();
        Long value = 10L;
        Assert.assertEquals("10", serializer.serialize(value));
    }
    
    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float value = 10.5f;
        Assert.assertEquals("10.5", serializer.serialize(value));
    }
    
    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();
        Double value = 10.5;
        Assert.assertEquals("10.5", serializer.serialize(value));
    }
}