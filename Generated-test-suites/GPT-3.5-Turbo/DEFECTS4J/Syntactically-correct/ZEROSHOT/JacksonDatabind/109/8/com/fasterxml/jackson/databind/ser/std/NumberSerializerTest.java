import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {
    
    private NumberSerializer numberSerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    
    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
        jsonGenerator = new JsonGenerator(); // Assuming the class exists
        serializerProvider = new SerializerProvider(); // Assuming the class exists
    }
    
    @After
    public void tearDown() {
        numberSerializer = null;
        jsonGenerator = null;
        serializerProvider = null;
    }
    
    @Test
    public void testSerializeBigDecimal() throws IOException {
        Number value = new BigDecimal(10.5);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assertion
        Assert.assertEquals(value, jsonGenerator.getValue());
    }
    
    @Test
    public void testSerializeBigInteger() throws IOException {
        Number value = new BigInteger("10");
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assertion
        Assert.assertEquals(value, jsonGenerator.getValue());
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        Number value = new Long(10);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assertion
        Assert.assertEquals(value, jsonGenerator.getValue());
    }
    
    @Test
    public void testSerializeDouble() throws IOException {
        Number value = new Double(10.5);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assertion
        Assert.assertEquals(value, jsonGenerator.getValue());
    }
    
    @Test
    public void testSerializeFloat() throws IOException {
        Number value = new Float(10.5);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assertion
        Assert.assertEquals(value, jsonGenerator.getValue());
    }
    
    @Test
    public void testSerializeInt() throws IOException {
        Number value = new Integer(10);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assertion
        Assert.assertEquals(value, jsonGenerator.getValue());
    }
    
    @Test
    public void testSerializeByte() throws IOException {
        Number value = new Byte(10);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assertion
        Assert.assertEquals(value, jsonGenerator.getValue());
    }
    
    @Test
    public void testSerializeShort() throws IOException {
        Number value = new Short(10);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assertion
        Assert.assertEquals(value, jsonGenerator.getValue());
    }
    
    @Test
    public void testSerializeDefault() throws IOException {
        Number value = 10;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assertion
        Assert.assertEquals(value.toString(), jsonGenerator.getValue());
    }
    
}