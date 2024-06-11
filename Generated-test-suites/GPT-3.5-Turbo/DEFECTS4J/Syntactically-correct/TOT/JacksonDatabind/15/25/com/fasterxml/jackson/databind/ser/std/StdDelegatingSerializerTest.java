import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;
    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;

    private static class DummyConverter<T> implements com.fasterxml.jackson.databind.util.Converter<T, T>, Serializable {
        public T convert(T value) { return value; }
        public JavaType getInputType(TypeFactory tf) { return null; }
        public JavaType getOutputType(TypeFactory tf) { return null; }
    }

    private static class DummySerializer<T> extends StdSerializer<T> {
        protected DummySerializer(Class<T> t) {
            super(t);
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        }
    }

    @Before
    public void setup() {
        DummyConverter<Object> converter = new DummyConverter<>();
        serializer = new StdDelegatingSerializer(converter);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory jsonFactory = new JsonFactory(mapper);
        try {
            jsonGenerator = jsonFactory.createGenerator(new StringWriter());
            serializerProvider = new SerializerProvider(mapper.getSerializerProvider(), jsonFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerialize_withValidValue_shouldReturnSerializedValue() {
        // Arrange
        Object value = new Object();

        // Act
        try {
            // Serialize
            serializer.serialize(value, jsonGenerator, serializerProvider);

            // Flush and close
            jsonGenerator.flush();
            jsonGenerator.close();

            // Assert
            assertTrue(jsonGenerator.getOutputTarget().toString().contains(value.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerialize_withNullValue_shouldSerializeNull() {
        // Arrange
        Object value = null;

        // Act
        try {
            // Serialize
            serializer.serialize(value, jsonGenerator, serializerProvider);

            // Flush and close
            jsonGenerator.flush();
            jsonGenerator.close();

            // Assert
            assertTrue(jsonGenerator.getOutputTarget().toString().contains("null"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerializeWithType_withValidValue_shouldReturnSerializedValueWithType() {
        // Arrange
        Object value = new Object();

        // Act
        try {
            // Serialize with type
            serializer.serializeWithType(value, jsonGenerator, serializerProvider, null);

            // Flush and close
            jsonGenerator.flush();
            jsonGenerator.close();

            // Assert
            assertTrue(jsonGenerator.getOutputTarget().toString().contains("\""+value.getClass().getName()+"\""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerializeWithType_withNullValue_shouldSerializeNullWithType() {
        // Arrange
        Object value = null;

        // Act
        try {
            // Serialize with type
            serializer.serializeWithType(value, jsonGenerator, serializerProvider, null);

            // Flush and close
            jsonGenerator.flush();
            jsonGenerator.close();

            // Assert
            assertTrue(jsonGenerator.getOutputTarget().toString().contains("\"java.lang.NullPointerException\""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsEmpty_withNonNullValue_shouldReturnFalse() {
        // Arrange
        Object value = new Object();

        // Act
        boolean isEmpty = serializer.isEmpty(serializerProvider, value);

        // Assert
        assertEquals(false, isEmpty);
    }

    @Test
    public void testIsEmpty_withNullValue_shouldReturnTrue() {
        // Arrange
        Object value = null;

        // Act
        boolean isEmpty = serializer.isEmpty(serializerProvider, value);

        // Assert
        assertEquals(true, isEmpty);
    }

    @Test
    public void testGetSchema_withDelegateSerializerHavingSchemaAwareInterface_shouldReturnDelegateSchema() {
        // Arrange
        DummySerializer<Object> delegateSerializer = new DummySerializer<>(Object.class);
        serializer = new StdDelegatingSerializer(new DummyConverter<>(), null, delegateSerializer);

        // Act
        JsonNode schema = serializer.getSchema(serializerProvider, null);

        // Assert
        assertEquals(delegateSerializer.getSchema(serializerProvider, null), schema);
    }

    @Test
    public void testGetSchema_withDelegateSerializerNotHavingSchemaAwareInterface_shouldReturnSuperGetSchema() {
        // Arrange
        DummySerializer<Object> delegateSerializer = new DummySerializer<>(Object.class);
        serializer = new StdDelegatingSerializer(new DummyConverter<>(), null, delegateSerializer);

        // Assigning null to delegateSerializer's schema
        delegateSerializer._schema = null;

        // Act
        JsonNode schema = serializer.getSchema(serializerProvider, null);

        // Assert
        assertEquals(serializer.getSchema(serializerProvider, null), schema);
    }

    @Test
    public void testGetSchema_withDelegateSerializerHavingSchemaAwareInterfaceAndIsOptional_shouldReturnDelegateSchema() {
        // Arrange
        DummySerializer<Object> delegateSerializer = new DummySerializer<>(Object.class);
        serializer = new StdDelegatingSerializer(new DummyConverter<>(), null, delegateSerializer);

        // Act
        JsonNode schema = serializer.getSchema(serializerProvider, null, true);

        // Assert
        assertEquals(delegateSerializer.getSchema(serializerProvider, null, true), schema);
    }

    @Test
    public void testGetSchema_withDelegateSerializerNotHavingSchemaAwareInterfaceAndIsOptional_shouldReturnSuperGetSchema() {
        // Arrange
        DummySerializer<Object> delegateSerializer = new DummySerializer<>(Object.class);
        serializer = new StdDelegatingSerializer(new DummyConverter<>(), null, delegateSerializer);

        // Assigning null to delegateSerializer's schema
        delegateSerializer._schema = null;

        // Act
        JsonNode schema = serializer.getSchema(serializerProvider, null, true);

        // Assert
        assertEquals(serializer.getSchema(serializerProvider, null, true), schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor_withDelegateSerializerHavingContextualSerializer_shouldCallDelegateAcceptJsonFormatVisitor() {
        // Arrange
        DummySerializer<Object> delegateSerializer = new DummySerializer<>(Object.class);
        serializer = new StdDelegatingSerializer(new DummyConverter<>(), null, delegateSerializer);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();

        // Act
        try {
            serializer.acceptJsonFormatVisitor(visitor, null);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }

        // Assert
        assertEquals(1, visitor.getReturnTypeCount());
    }

    @Test
    public void testAcceptJsonFormatVisitor_withDelegateSerializerNotHavingContextualSerializer_shouldNotCallDelegateAcceptJsonFormatVisitor() {
        // Arrange
        DummySerializer<Object> delegateSerializer = new DummySerializer<>(Object.class);
        serializer = new StdDelegatingSerializer(new DummyConverter<>(), null, delegateSerializer);

        // Assigning null to delegateSerializer's handleSecondaryContextualization return
        delegateSerializer._handleSecondaryContextualizationReturn = null;

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();

        // Act
        try {
            serializer.acceptJsonFormatVisitor(visitor, null);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }

        // Assert
        assertEquals(0, visitor.getReturnTypeCount());
    }

    @Test
    public void testConvertValue_withNonNullValue_shouldReturnSameValue() {
        // Arrange
        Object value = new Object();

        // Act
        Object convertedValue = serializer.convertValue(value);

        // Assert
        assertEquals(value, convertedValue);
    }

    @Test
    public void testConvertValue_withNullValue_shouldReturnNull() {
        // Arrange
        Object value = null;

        // Act
        Object convertedValue = serializer.convertValue(value);

        // Assert
        assertEquals(null, convertedValue);
    }
}