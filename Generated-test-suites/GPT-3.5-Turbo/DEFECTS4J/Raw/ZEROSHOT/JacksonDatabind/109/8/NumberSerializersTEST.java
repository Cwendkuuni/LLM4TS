import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {
    
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        // create a JsonGenerator and SerializerProvider mocks
        // serialize a value using the serializer
        // assert the result with expected value
    }
    
    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        // create a JsonGenerator and SerializerProvider mocks
        // serialize a value using the serializer
        // assert the result with expected value
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        // create a JsonGenerator and SerializerProvider mocks
        // serialize a value using the serializer
        // assert the result with expected value
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer(Long.class);
        // create a JsonGenerator and SerializerProvider mocks
        // serialize a value using the serializer
        // assert the result with expected value
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = FloatSerializer.instance;
        // create a JsonGenerator and SerializerProvider mocks
        // serialize a value using the serializer
        // assert the result with expected value
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        // create a JsonGenerator and SerializerProvider mocks
        // serialize a value using the serializer
        // assert the result with expected value
    }
}