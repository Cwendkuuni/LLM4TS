import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.annotation.JsonFormat;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        JsonSerializer<?> result = numberSerializer.createContextual(provider, property);
        assertNull(result);
    }

    @Test
    public void testSerialize() throws IOException {
        Number value = new BigDecimal(10);
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        numberSerializer.serialize(value, g, provider);
        // Check the result
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = new Type();
        JsonNode result = numberSerializer.getSchema(provider, typeHint);
        // Check the result
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Check the result
    }

}