import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BigDecimal value = new BigDecimal("123.45");
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        Mockito.verify(jsonGenerator).writeNumber(value);
    }
}