import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ser.std.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        Short value = 10;
        
        try {
            shortSerializer.serialize(value, null, null);
        } catch (Exception e) {
            fail("Failed to serialize Short value");
        }
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        Integer value = 100;
        
        try {
            integerSerializer.serialize(value, null, null);
            integerSerializer.serializeWithType(value, null, null, null);
        } catch (Exception e) {
            fail("Failed to serialize Integer value");
        }
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        Number value = 200;
        
        try {
            intLikeSerializer.serialize(value, null, null);
        } catch (Exception e) {
            fail("Failed to serialize Number value");
        }
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        Long value = 1000L;
        
        try {
            longSerializer.serialize(value, null, null);
        } catch (Exception e) {
            fail("Failed to serialize Long value");
        }
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        Float value = 10.5f;
        
        try {
            floatSerializer.serialize(value, null, null);
        } catch (Exception e) {
            fail("Failed to serialize Float value");
        }
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        Double value = 10.123;
        
        try {
            doubleSerializer.serialize(value, null, null);
            doubleSerializer.serializeWithType(value, null, null, null);
        } catch (Exception e) {
            fail("Failed to serialize Double value");
        }
    }

}