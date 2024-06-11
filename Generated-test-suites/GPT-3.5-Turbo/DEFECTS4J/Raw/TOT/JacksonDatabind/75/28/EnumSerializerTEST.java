import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;
    private SerializerProvider serializerProvider;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        EnumValues enumValues = new EnumValues(Enum.class, Arrays.asList(Enum.values()));
        enumSerializer = new EnumSerializer(enumValues, true);
        mapper = new ObjectMapper(new JsonFactory());
    }

    @Test
    public void testSerialize_WhenSerializeAsIndex_ThenReturnOrdinal() throws Exception {
        // Arrange
        Enum<?> en = Enum.ONE;
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        // Act
        enumSerializer.serialize(en, gen, serializerProvider);
        gen.flush();
        // Assert
        assertEquals(0, en.ordinal());
    }

    @Test
    public void testSerialize_WhenSerializeUsingToString_ThenReturnEnumValueAsString() throws Exception {
        // Arrange
        Enum<?> en = Enum.TWO;
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializerProvider.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        // Act
        enumSerializer.serialize(en, gen, serializerProvider);
        gen.flush();
        // Assert
        assertEquals("TWO", en.toString());
    }

    @Test
    public void testSerialize_WhenSerializeAsString_ThenReturnSerializedValue() throws Exception {
        // Arrange
        Enum<?> en = Enum.THREE;
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        // Act
        enumSerializer.serialize(en, gen, serializerProvider);
        gen.flush();
        // Assert
        assertEquals("THREE", en.toString());
    }

    @Test
    public void testGetSchema_WhenSerializeAsIndex_ThenReturnTypeIsInteger() throws Exception {
        // Arrange
        SerializerProvider provider = mapper.getSerializerProvider();
        Type typeHint = Enum.class;
        // Act
        JsonNode schema = enumSerializer.getSchema(provider, typeHint);
        // Assert
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_WhenSerializeAsString_ThenReturnTypeIsString() throws Exception {
        // Arrange
        SerializerProvider provider = mapper.getSerializerProvider();
        Type typeHint = Enum.class;
        // Act
        JsonNode schema = enumSerializer.getSchema(provider, typeHint);
        // Assert
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenSerializeAsIndex_ThenReturnTypeIsInteger() throws Exception {
        // Arrange
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = mapper.constructType(Enum.class);
        // Act
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Assert
        assertTrue(visitor.getProvider().isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX));
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenSerializeAsString_ThenReturnTypeIsString() throws Exception {
        // Arrange
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = mapper.constructType(Enum.class);
        // Act
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Assert
        assertTrue(visitor.getProvider().isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING));
    }
}