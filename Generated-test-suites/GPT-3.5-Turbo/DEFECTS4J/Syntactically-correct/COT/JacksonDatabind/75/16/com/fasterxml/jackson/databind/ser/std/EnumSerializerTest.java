import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class EnumSerializerTest {
    @Test
    public void testSerialize() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, MockEnum.class));

        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        enumSerializer.serialize(MockEnum.VALUE1, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeString("VALUE1");
    }

    @Test
    public void testSerializeAsIndex() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, MockEnum.class), true);

        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        enumSerializer.serialize(MockEnum.VALUE1, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(0);
    }

    @Test
    public void testSerializeUsingToString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, MockEnum.class));

        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        enumSerializer.serialize(MockEnum.VALUE1, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeString("VALUE1");
    }

    @Test
    public void testGetSchema() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, MockEnum.class));

        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);

        JsonNode jsonNode = enumSerializer.getSchema(serializerProvider, typeHint);

        assertNotNull(jsonNode);
        assertTrue(jsonNode instanceof ObjectNode);

        ObjectNode objectNode = (ObjectNode) jsonNode;
        assertTrue(objectNode.has("enum"));
        ArrayNode enumNode = (ArrayNode) objectNode.get("enum");
        assertEquals(2, enumNode.size());
        assertTrue(enumNode.contains("VALUE1"));
        assertTrue(enumNode.contains("VALUE2"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, MockEnum.class));

        JsonFormatVisitorWrapper visitorWrapper = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        enumSerializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);

        verify(visitorWrapper, times(1)).expectStringFormat(typeHint);
    }
}