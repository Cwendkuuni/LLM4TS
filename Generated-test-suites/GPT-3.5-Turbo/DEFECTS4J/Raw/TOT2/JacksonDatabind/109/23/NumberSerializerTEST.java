import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        // Add test logic for serializing BigDecimal
    }
    
    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        // Add test logic for serializing BigInteger
    }
    
    @Test
    public void testSerializeLong() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        // Add test logic for serializing Long
    }
    
    @Test
    public void testSerializeDouble() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        // Add test logic for serializing Double
    }
    
    @Test
    public void testSerializeFloat() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        // Add test logic for serializing Float
    }
    
    @Test
    public void testSerializeInteger() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        // Add test logic for serializing Integer
    }
    
    @Test
    public void testSerializeByte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        // Add test logic for serializing Byte
    }
    
    @Test
    public void testSerializeShort() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        // Add test logic for serializing Short
    }
    
    @Test
    public void testSerializeOtherNumber() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test logic for serializing other Number types
    }
    
    @Test
    public void testContextualSerializer() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test logic for contextual serializer
    }
    
    // Add more test cases for getSchema, acceptJsonFormatVisitor, and other methods as needed
    
}