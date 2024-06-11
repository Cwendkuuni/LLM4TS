import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        
        // typical use case
        assertEquals("10", serializeNumber(numberSerializer, 10));
        
        // edge cases
        assertEquals("0.001", serializeNumber(numberSerializer, 0.001));
        assertEquals("100.0", serializeNumber(numberSerializer, 100.0));
        
        // error scenarios
        try {
            serializeNumber(numberSerializer, "invalid"); // should throw exception
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }
    
    private String serializeNumber(NumberSerializer serializer, Number number) {
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        
        try {
            serializer.serialize(number, generator, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return generator.getOutput();
    }
    
    private class JsonGeneratorMock extends JsonGenerator {
        private StringBuilder output = new StringBuilder();
        
        @Override
        public void writeNumber(int v) throws IOException {
            output.append(v);
        }

        @Override
        public void writeNumber(long v) throws IOException {
            output.append(v);
        }

        @Override
        public void writeNumber(double v) throws IOException {
            output.append(v);
        }

        @Override
        public void writeNumber(float f) throws IOException {
            output.append(f);
        }

        @Override
        public void writeNumber(BigInteger value) throws IOException {
            output.append(value);
        }

        @Override
        public void writeNumber(BigDecimal value) throws IOException {
            output.append(value);
        }

        public String getOutput() {
            return output.toString();
        }
        
        // other methods are not implemented for brevity
    }
    
    private class SerializerProviderMock extends SerializerProvider {
        // methods are not implemented for brevity
    }
}