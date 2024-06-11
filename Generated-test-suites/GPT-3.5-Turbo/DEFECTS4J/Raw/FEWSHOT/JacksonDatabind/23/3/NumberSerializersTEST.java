import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;
import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();

        Short value = 5;
        assertEquals("5", serializer.serialize(value));
        
        value = -10;
        assertEquals("-10", serializer.serialize(value));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();

        Integer value = 100;
        assertEquals("100", serializer.serialize(value));
        
        value = -500;
        assertEquals("-500", serializer.serialize(value));
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();

        Number value = 1000;
        assertEquals("1000", serializer.serialize(value));
        
        value = -5000;
        assertEquals("-5000", serializer.serialize(value));
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();

        Long value = 1000000L;
        assertEquals("1000000", serializer.serialize(value));
        
        value = -5000000L;
        assertEquals("-5000000", serializer.serialize(value));
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();

        Float value = 10.5f;
        assertEquals("10.5", serializer.serialize(value));
        
        value = -5.7f;
        assertEquals("-5.7", serializer.serialize(value));
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();

        Double value = 10.1;
        assertEquals("10.1", serializer.serialize(value));
        
        value = -5.5;
        assertEquals("-5.5", serializer.serialize(value));
    }
}