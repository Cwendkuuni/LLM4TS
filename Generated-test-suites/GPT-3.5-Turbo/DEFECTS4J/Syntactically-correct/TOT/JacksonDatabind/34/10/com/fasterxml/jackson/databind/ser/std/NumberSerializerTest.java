import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {
    
    private NumberSerializer numberSerializer;
    
    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
    }
    
    @Test
    public void testSerialize_BigDecimal() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        numberSerializer.serialize(new BigDecimal("123.456"), generator, null);
        assertEquals("123.456", generator.getResult());
        assertEquals(JsonGeneratorStub.Methods.WRITE_NUMBER, generator.getLastCalledMethod());
    }
    
    @Test
    public void testSerialize_BigInteger() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        numberSerializer.serialize(new BigInteger("1234"), generator, null);
        assertEquals("1234", generator.getResult());
        assertEquals(JsonGeneratorStub.Methods.WRITE_NUMBER, generator.getLastCalledMethod());
    }
    
    @Test
    public void testSerialize_Integer() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        numberSerializer.serialize(100, generator, null);
        assertEquals("100", generator.getResult());
        assertEquals(JsonGeneratorStub.Methods.WRITE_NUMBER, generator.getLastCalledMethod());
    }
    
    @Test
    public void testSerialize_Long() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        numberSerializer.serialize(1234567890123L, generator, null);
        assertEquals("1234567890123", generator.getResult());
        assertEquals(JsonGeneratorStub.Methods.WRITE_NUMBER, generator.getLastCalledMethod());
    }
    
    @Test
    public void testSerialize_Double() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        numberSerializer.serialize(12.34, generator, null);
        assertEquals("12.34", generator.getResult());
        assertEquals(JsonGeneratorStub.Methods.WRITE_NUMBER, generator.getLastCalledMethod());
    }

    @Test
    public void testSerialize_Float() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        numberSerializer.serialize(12.34f, generator, null);
        assertEquals("12.34", generator.getResult());
        assertEquals(JsonGeneratorStub.Methods.WRITE_NUMBER, generator.getLastCalledMethod());
    }
    
    @Test
    public void testSerialize_Byte() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        numberSerializer.serialize((byte) 100, generator, null);
        assertEquals("100", generator.getResult());
        assertEquals(JsonGeneratorStub.Methods.WRITE_NUMBER, generator.getLastCalledMethod());
    }
    
    @Test
    public void testSerialize_Short() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        numberSerializer.serialize((short) 1000, generator, null);
        assertEquals("1000", generator.getResult());
        assertEquals(JsonGeneratorStub.Methods.WRITE_NUMBER, generator.getLastCalledMethod());
    }
    
    @Test
    public void testSerialize_OtherNumber() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        numberSerializer.serialize(12345, generator, null);
        assertEquals("12345", generator.getResult());
        assertEquals(JsonGeneratorStub.Methods.WRITE_NUMBER, generator.getLastCalledMethod());
    }

}