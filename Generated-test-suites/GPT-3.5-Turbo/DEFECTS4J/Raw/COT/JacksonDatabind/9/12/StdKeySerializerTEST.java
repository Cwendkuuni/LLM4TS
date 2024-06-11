
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_withDate_shouldCallDefaultSerializeDateKey() throws IOException {
        // Arrange
        Object value = new Date();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializer serializer = new StdKeySerializer();

        // Act
        serializer.serialize(value, jgen, provider);

        // Assert
        verify(provider).defaultSerializeDateKey((Date) value, jgen);
    }

    @Test
    public void testSerialize_withNonDateObject_shouldWriteFieldName() throws IOException {
        // Arrange
        Object value = 123;
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializer serializer = new StdKeySerializer();

        // Act
        serializer.serialize(value, jgen, provider);

        // Assert
        verify(jgen).writeFieldName(value.toString());
    }

    @Test
    public void testGetSchema_shouldReturnStringSchemaNode() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        StdKeySerializer serializer = new StdKeySerializer();

        // Act
        JsonNode schemaNode = serializer.getSchema(provider, typeHint);

        // Assert
        assertEquals("string", schemaNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_shouldCallExpectStringFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        StdKeySerializer serializer = new StdKeySerializer();

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor).expectStringFormat(typeHint);
    }
}
