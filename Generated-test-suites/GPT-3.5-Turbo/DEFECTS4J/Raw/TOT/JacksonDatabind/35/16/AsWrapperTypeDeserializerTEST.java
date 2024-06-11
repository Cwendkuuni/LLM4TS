import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AsWrapperTypeDeserializerTest {

    private ObjectMapper objectMapper;
    private AsWrapperTypeDeserializer typeDeserializer;
    private TokenBuffer tokenBuffer;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        typeDeserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        tokenBuffer = new TokenBuffer(null, false);
    }

    @Test
    public void whenDeserializeTypedFromObject_ThenReturnDeserializedObject() throws IOException {
        // Arrange
        tokenBuffer.writeStartObject();
        tokenBuffer.writeFieldName("typeId");
        tokenBuffer.writeString("testType");
        tokenBuffer.writeFieldName("testField");
        tokenBuffer.writeString("testValue");
        tokenBuffer.writeEndObject();
        JsonParser parser = tokenBuffer.asParser();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        Object result = typeDeserializer.deserializeTypedFromObject(parser, context);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void whenDeserializeTypedFromObjectWithWrongToken_ThenThrowJsonMappingException() throws IOException {
        // Arrange
        tokenBuffer.writeStartArray();
        tokenBuffer.writeEndArray();
        JsonParser parser = tokenBuffer.asParser();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        typeDeserializer.deserializeTypedFromObject(parser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void whenDeserializeTypedFromObjectWithoutTypeId_ThenThrowJsonMappingException() throws IOException {
        // Arrange
        tokenBuffer.writeStartObject();
        tokenBuffer.writeFieldName("testField");
        tokenBuffer.writeString("testValue");
        tokenBuffer.writeEndObject();
        JsonParser parser = tokenBuffer.asParser();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        typeDeserializer.deserializeTypedFromObject(parser, context);
    }

    @Test
    public void whenDeserializeTypedFromArray_ThenReturnDeserializedObject() throws IOException {
        // Arrange
        tokenBuffer.writeStartArray();
        tokenBuffer.writeString("testValue");
        tokenBuffer.writeEndArray();
        JsonParser parser = tokenBuffer.asParser();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        Object result = typeDeserializer.deserializeTypedFromArray(parser, context);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void whenDeserializeTypedFromArrayWithWrongToken_ThenThrowJsonMappingException() throws IOException {
        // Arrange
        tokenBuffer.writeStartObject();
        tokenBuffer.writeFieldName("typeId");
        tokenBuffer.writeString("testType");
        tokenBuffer.writeFieldName("testField");
        tokenBuffer.writeString("testValue");
        tokenBuffer.writeEndObject();
        JsonParser parser = tokenBuffer.asParser();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        typeDeserializer.deserializeTypedFromArray(parser, context);
    }

    @Test
    public void whenDeserializeTypedFromScalar_ThenReturnDeserializedObject() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(outputStream);
        generator.writeString("testValue");
        generator.flush();
        byte[] jsonBytes = outputStream.toByteArray();
        JsonParser parser = new JsonFactory().createParser(jsonBytes);
        parser.nextToken();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        Object result = typeDeserializer.deserializeTypedFromScalar(parser, context);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void whenDeserializeTypedFromScalarWithWrongToken_ThenThrowJsonMappingException() throws IOException {
        // Arrange
        tokenBuffer.writeStartArray();
        tokenBuffer.writeString("testValue");
        tokenBuffer.writeEndArray();
        JsonParser parser = tokenBuffer.asParser();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        typeDeserializer.deserializeTypedFromScalar(parser, context);
    }

    @Test
    public void whenDeserializeTypedFromAny_ThenReturnDeserializedObject() throws IOException {
        // Arrange
        tokenBuffer.writeStartObject();
        tokenBuffer.writeFieldName("typeId");
        tokenBuffer.writeString("testType");
        tokenBuffer.writeFieldName("testField");
        tokenBuffer.writeString("testValue");
        tokenBuffer.writeEndObject();
        JsonParser parser = tokenBuffer.asParser();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        Object result = typeDeserializer.deserializeTypedFromAny(parser, context);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void whenDeserializeTypedFromAnyWithWrongToken_ThenThrowJsonMappingException() throws IOException {
        // Arrange
        tokenBuffer.writeStartArray();
        tokenBuffer.writeString("testValue");
        tokenBuffer.writeEndArray();
        JsonParser parser = tokenBuffer.asParser();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        typeDeserializer.deserializeTypedFromAny(parser, context);
    }

    @Test
    public void whenDeserializeTypedFromAnyWithTypeId_ThenReturnDeserializedObject() throws IOException {
        // Arrange
        tokenBuffer.writeStartObject();
        tokenBuffer.writeFieldName("typeId");
        tokenBuffer.writeString("testType");
        tokenBuffer.writeFieldName("testField");
        tokenBuffer.writeString("testValue");
        tokenBuffer.writeEndObject();
        JsonParser parser = tokenBuffer.asParser();
        DeserializationContext context = objectMapper.getDeserializationContext();
        parser.nextToken();

        // Act
        Object result = typeDeserializer.deserializeTypedFromAny(parser, context);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void whenDeserializeTypedFromAnyWithWrongTypeId_ThenThrowJsonMappingException() throws IOException {
        // Arrange
        tokenBuffer.writeStartObject();
        tokenBuffer.writeFieldName("typeId");
        tokenBuffer.writeString("wrongType");
        tokenBuffer.writeFieldName("testField");
        tokenBuffer.writeString("testValue");
        tokenBuffer.writeEndObject();
        JsonParser parser = tokenBuffer.asParser();
        DeserializationContext context = objectMapper.getDeserializationContext();
        parser.nextToken();

        // Act
        typeDeserializer.deserializeTypedFromAny(parser, context);
    }
}