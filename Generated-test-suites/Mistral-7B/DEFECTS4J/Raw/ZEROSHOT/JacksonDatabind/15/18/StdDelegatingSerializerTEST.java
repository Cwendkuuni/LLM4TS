import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private static final Object SOME_VALUE = new Object();
    private static final Object SOME_DELEGATE_VALUE = new Object();;
    private static final Converter<Object, ?> CONVERTER = mock(Converter.class);
    private static final JavaType DELEGATE_TYPE = mock(JavaType.class);
    private static final JsonSerializer<Object> DELEGATE_SERIALIZER = mock(JsonSerializer.class);

    private ObjectMapper objectMapper;

    @BeforeMethod
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        when(DELEGATE_SERIALIZER.serialize(SOME_DELEGATE_VALUE, any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(null);
        when(DELEGATE_SERIALIZER.acceptJsonFormatVisitor(any(JsonFormatVisitorWrapper.class), any(Type.class))).thenReturn(null);
        when(DELEGATE_SERIALIZER.getSchema(any(SerializerProvider.class), any(Type.class))).thenReturn(null);
        when(DELEGATE_SERIALIZER.getSchema(any(SerializerProvider.class), any(Type.class), anyBoolean())).thenReturn(null);
    }

    @Test
    public void testDefaultSerialization() throws JsonProcessingException, JsonGenerationException, IOException {
        // Given
        when(CONVERTER.convert(SOME_VALUE)).thenReturn(SOME_DELEGATE_VALUE);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(CONVERTER);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        // When
        serializer.serialize(SOME_VALUE, jsonGenerator, serializerProvider);

        // Then
        verify(DELEGATE_SERIALIZER, times(1)).serialize(SOME_DELEGATE_VALUE, jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerializationWithType() throws JsonProcessingException, JsonGenerationException, IOException {
        // Given
        when(CONVERTER.convert(SOME_VALUE)).thenReturn(SOME_DELEGATE_VALUE);
        when(DELEGATE_SERIALIZER.serializeWithType(SOME_DELEGATE_VALUE, any(JsonGenerator.class), any(SerializerProvider.class), any(TypeSerializer.class))).thenReturn(null);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(CONVERTER);

        // When
        serializer.serializeWithType(SOME_VALUE, null, null, null);

        // Then
        verify(DELEGATE_SERIALIZER, times(1)).serializeWithType(SOME_DELEGATE_VALUE, any(JsonGenerator.class), any(SerializerProvider.class), any(TypeSerializer.class));
    }

    @Test
    public void testGetDelegatee() {
        // Given
        when(CONVERTER.convert(SOME_VALUE)).thenReturn(SOME_DELEGATE_VALUE);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(CONVERTER);

        // When
        JsonSerializer<Object> delegateSerializer = serializer.getDelegatee();

        // Then
        assertEquals(DELEGATE_SERIALIZER, delegateSerializer);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        // Given
        when(CONVERTER.convert(SOME_VALUE)).thenReturn(SOME_DELEGATE_VALUE);
        when(DELEGATE_SERIALIZER.getClass()).thenReturn(JsonSerializer.class);
        when(DELEGATE_SERIALIZER.resolve(any(SerializerProvider.class))).thenReturn(DELEGATE_SERIALIZER);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(CONVERVER, null, null);
        SerializerProvider provider = mock(SerializerProvider.class);

        // When
        serializer.resolve(provider);

        // Then
        verify(DELEGATE_SERIALIZER, times(1)).resolve(provider);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        // Given
        when(CONVERTER.convert(SOME_VALUE)).thenReturn(SOME_DELEGATE_VALUE);
        when(DELEGATE_SERIALIZER.getClass()).thenReturn(JsonSerializer.class);
        when(DELEGATE_SERIALIZER.isContextualSerialization()).thenReturn(true);
        when(DELEGATE_SERIALIZER.createContextual(any(SerializerProvider.class), any(BeanProperty.class))).thenReturn(DELEGATE_SERIALIZER);
        when(DELEGATE_SERIALIZER.getDelegatee()).thenReturn(null);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(CONVERVER, null, null);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        // When
        JsonSerializer<Object> result = serializer.createContextual(provider, property);

        // Then
        assertEquals(DELEGATE_SERIALIZER, result);
    }

    @Test
    public void testGetConverter() {
        // Given
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(CONVERVER, null, null);

        // When
        Converter<Object, ?> converter = serializer.getConverter();

        // Then
        assertEquals(CONVERTER, converter);
    }

    @Test
    public void testEmpty() throws JsonMappingException {
        // Given
        when(CONVERTER.convert(null)).thenReturn(null);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(CONVERVER);

        // When
        boolean result = serializer.isEmpty(null);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        // Given
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        when(DELEGATE_SERIALIZER.getClass()).thenReturn(JsonSerializer.class);
        when(DELEGATE_SERIALIZER.acceptJsonFormatVisitor(any(JsonFormatVisitorWrapper.class), any(JavaType.class))).thenReturn(null);

        // When
        JsonNode result = new StdDelegatingSerializer(CONVERVER, null, DELEGATE_SERIALIZER).getSchema(objectMapper, null);

        // Then
        verify(DELEGATE_SERIALIZER, times(1)).acceptJsonFormatVisitor(any(JsonFormatVisitorWrapper.class), any(JavaType.class));
        assertEquals(null, result);
    }
}