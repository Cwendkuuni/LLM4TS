import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject_WhenTypePropertyIsPresent() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("{\"type\":\"object\",\"name\":\"John\",\"age\":30}");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);
        Object result = typeDeserializer.deserializeTypedFromObject(mockParser, mockContext);

        // Assert
        assertNotNull(result);
        assertEquals("object", result.getType());
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    public void testDeserializeTypedFromObject_WhenTypePropertyIsMissing_ThenUseDefaultImpl() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("{\"name\":\"John\",\"age\":30}");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, new GenericType(), As.PROPERTY);
        Object result = typeDeserializer.deserializeTypedFromObject(mockParser, mockContext);

        // Assert
        assertNotNull(result);
        assertEquals(GenericType.class, result.getClass());
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    public void testDeserializeTypedFromObject_WhenStartObjectTokenIsMissing_ThenHandleAsDefaultImpl() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("[{\"type\":\"object\",\"name\":\"John\",\"age\":30}]");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, new GenericType(), As.PROPERTY);
        Object result = typeDeserializer.deserializeTypedFromObject(mockParser, mockContext);

        // Assert
        assertNotNull(result);
        assertEquals(GenericType.class, result.getClass());
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    public void testDeserializeTypedFromObject_WhenFieldTypeIsNotFieldName_And_DefaultImplIsNotDefined_ThenReportWrongTokenException() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("{\"name\":\"John\",\"age\":30}");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);

        try {
            typeDeserializer.deserializeTypedFromObject(mockParser, mockContext);
            fail("Expected WrongTokenException to be thrown");
        } catch (WrongTokenException ex) {
            // Assert
            assertEquals("missing property 'type' that is to contain type id (for class GenericType)", ex.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedForId_WhenTypeIdVisibleIsTrue_ThenMergeIdBackInJsonInput() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("{\"name\":\"John\",\"age\":30}");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", true, new GenericType(), As.PROPERTY);
        Object result = typeDeserializer.deserializeTypedForId(mockParser, mockContext, null);

        // Assert
        assertNotNull(result);
        assertEquals(GenericType.class, result.getClass());
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    public void testDeserializeTypedForId_WhenTokenBufferIsNotNull_ThenPutBackSkippedProperties() throws IOException {
        // Arrange
        TokenBuffer tokenBuffer = new TokenBuffer(new ObjectMapper(), false);
        tokenBuffer.writeFieldName("type");
        tokenBuffer.writeString("object");
        tokenBuffer.writeEndObject();

        JsonParser mockParser = new JsonFactory().createParser("{\"name\":\"John\",\"age\":30}");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);
        Object result = typeDeserializer.deserializeTypedForId(mockParser, mockContext, tokenBuffer);

        // Assert
        assertNotNull(result);
        assertEquals(GenericType.class, result.getClass());
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WhenDefaultImplDeserializerIsNotNull_ThenDeserialize() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("[{\"name\":\"John\",\"age\":30}]");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, new GenericType(), As.PROPERTY);
        Object result = typeDeserializer.deserializeTypedUsingDefaultImpl(mockParser, mockContext, null);

        // Assert
        assertNotNull(result);
        assertEquals(GenericType.class, result.getClass());
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WhenFieldTypeIsNatural_ThenDeserializeIfNatural() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("{\"name\":\"John\",\"age\":30}");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);
        Object result = typeDeserializer.deserializeTypedUsingDefaultImpl(mockParser, mockContext, null);

        // Assert
        assertNotNull(result);
        assertEquals(GenericType.class, result.getClass());
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WhenStartArrayTokenIsPresent_ThenDeserializeTypedFromAny() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("[{\"type\":\"object\",\"name\":\"John\",\"age\":30}]");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);
        Object result = typeDeserializer.deserializeTypedUsingDefaultImpl(mockParser, mockContext, null);

        // Assert
        assertNotNull(result);
        assertEquals(GenericType.class, result.getClass());
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WhenFieldTypeIsNotFieldName_And_DefaultImplIsNotDefined_ThenReportWrongTokenException() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("[{\"type\":\"object\",\"name\":\"John\",\"age\":30}]");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);

        try {
            typeDeserializer.deserializeTypedUsingDefaultImpl(mockParser, mockContext, null);
            fail("Expected WrongTokenException to be thrown");
        } catch (WrongTokenException ex) {
            // Assert
            assertEquals("missing property 'type' that is to contain type id (for class GenericType)", ex.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedFromAny_WhenStartArrayTokenIsPresent_ThenDeserializeTypedFromArray() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("[{\"type\":\"object\",\"name\":\"John\",\"age\":30}]");
        DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);
        Object result = typeDeserializer.deserializeTypedFromAny(mockParser, mockContext);

        // Assert
        assertNotNull(result);
        assertEquals(GenericType.class, result.getClass());
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    public void testDeserializeTypedFromAny_WhenStartObjectTokenIsPresent_ThenDeserializeTypedFromObject() throws IOException {
        // Arrange
        JsonParser mockParser = new JsonFactory().createParser("{\"type\":\"object\",\"name\":\"John\",\"age\":30}");
	      DeserializationContext mockContext = new DefaultDeserializationContext(new ObjectMapper(), mockParser, null);

        // Act
        AsPropertyTypeDeserializer typeDeserializer =
                new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);
        Object result = typeDeserializer.deserializeTypedFromAny(mockParser, mockContext);

        // Assert
        assertNotNull(result);
        assertEquals(GenericType.class, result.getClass());
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }
}