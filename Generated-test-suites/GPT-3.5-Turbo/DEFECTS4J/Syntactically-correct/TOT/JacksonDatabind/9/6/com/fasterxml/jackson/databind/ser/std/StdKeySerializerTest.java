import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

public class StdKeySerializerTest {

    private StdKeySerializer serializer;
    private JsonGenerator generator;
    private SerializerProvider provider;
    private JsonFormatVisitorWrapper visitor;
    private JavaType typeHint;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        serializer = new StdKeySerializer();
        mapper = new ObjectMapper();
        try {
            generator = new JsonFactory().createGenerator(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        provider = mapper.getSerializerProvider();
        typeHint = mapper.constructType(Object.class);
        visitor = mapper.getSerializerProvider().visitorForPrimitiveArray(generator, typeHint);
    }

    @Test
    public void testSerialize_GivenDateValue_ShouldSerializeDateKey() throws IOException {
        // Arrange
        Date date = new Date();

        // Act
        serializer.serialize(date, generator, provider);
        generator.flush();

        // Assert
        assertEquals(mapper.writeValueAsString(date), "{\"$date\": " + date.getTime() + "}");
    }

    @Test
    public void testSerialize_GivenNonDateValue_ShouldSerializeToString() throws IOException {
        // Arrange
        Object value = 123;

        // Act
        serializer.serialize(value, generator, provider);
        generator.flush();

        // Assert
        assertEquals(mapper.writeValueAsString(value), "123");
    }

    @Test
    public void testGetSchema_ShouldReturnStringSchemaNode() throws JsonMappingException {
        // Arrange
        Type typeHint = Object.class;

        // Act
        JsonNode schema = serializer.getSchema(provider, typeHint);

        // Assert
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() throws JsonMappingException {
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        assertTrue(visitor instanceof JsonFormatVisitorWrapper.Base);
    }
}