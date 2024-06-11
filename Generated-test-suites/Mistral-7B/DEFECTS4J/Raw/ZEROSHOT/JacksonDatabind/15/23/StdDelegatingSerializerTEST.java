import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;

import static com.fasterxml.jackson.databind.JsonMapping.SerializerParams.EmptyBehavior.ALWAYS_SERIALIZE_EMPTY_ARRAYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StdDelegatingSerializerTest {

    private JsonSerializer<Object> mockDelegateSerializer;
    private Converter<Object, ?> mockConverter;
    private StdDelegatingSerializer delegatingSerializer;

    @Before
    public void setup() {
        mockDelegateSerializer = mock(JsonSerializer.class);
        mockConverter = mock(Converter.class);
        delegatingSerializer = new StdDelegatingSerializer(mockConverter);
    }

    @Test
    public void testSerializationWithNullValue() throws JsonProcessingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        delegatingSerializer.serialize(null, gen, provider);

        verify(provider).defaultSerializeNull(gen);
    }

    @Test
    public void testSerializationWithValue() throws JsonProcessingException {
        ObjectNode jsonNode = mock(ObjectNode.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(mockDelegateSerializer.getDelegatee()).thenReturn(mockDelegateSerializer);
        when(mockDelegateSerializer.serialize(anyObject(), eq(gen), eq(provider))).thenReturn(jsonNode);

        Object value = new Object();

        delegatingSerializer.delegateSerializer = mockDelegateSerializer;

        delegatingSerializer.serialize(value, gen, provider);

        verify(mockDelegateSerializer).serialize(value, gen, provider);
    }

    @Test
    public void testSerializationWithValueAndType() throws JsonProcessingException {
        ObjectNode jsonNode = mock(ObjectNode.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        when(mockDelegateSerializer.getDelegatee()).thenReturn(mockDelegateSerializer);
        when(mockDelegateSerializer.serializeWithType(anyObject(), eq(gen), eq(provider), eq(typeSerializer))).thenReturn(jsonNode);

        Object value = new Object();

        delegatingSerializer.delegateSerializer = mockDelegateSerializer;

        delegatingSerializer.serializeWithType(value, gen, provider, typeSerializer);

        verify(mockDelegateSerializer).serializeWithType(value, gen, provider, typeSerializer);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode schema = mock(JsonNode.class);
        JsonSchemaProvider schemaProvider = mock(JsonSchemaProvider.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        when(schemaProvider.schemaForHandler(anyObject())).thenReturn(schema);
        when(serializerProvider.getSchemaProvider()).thenReturn(schemaProvider);

        ObjectNode expectedSchema = mock(ObjectNode.class);

        when(mockDelegateSerializer.getSchema(anyObject(), eq(typeHint))).thenReturn(expectedSchema);
        when(mockDelegateSerializer.isEnabled(anyString())).thenReturn(true);

        JsonSerializer<Object> delegateSerializer = mockDelegateSerializer;

        delegatingSerializer._delegateSerializer = delegateSerializer;

        JsonNode actualSchema = delegatingSerializer.getSchema(serializerProvider, typeHint);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        delegatingSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(mockDelegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);

        delegatingSerializer.resolve(provider);

        if (delegatingSerializer.delegateSerializer != null) {
            delegatingSerializer.delegateSerializer.resolve(provider);
        }
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonSerializer<Object> delegateSerializer = mockDelegateSerializer;
        JavaType delegateType = mock(JavaType.class);

        JsonSerializer<?> actualSerializer = delegatingSerializer.createContextual(provider, property);

        assertEquals(delegateSerializer, actualSerializer);

        if (delegatingSerializer.delegateSerializer != null) {
            verify(delegatingSerializer).withDelegate(any(Converter.class), eq(delegateType), eq(delegateSerializer));
        }
    }

    @Test
    public void testResolveWithDelegateSerializer() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonSerializer<Object> delegateSerializer = mockDelegateSerializer;

        delegatingSerializer.resolve(provider);

        verify(delegateSerializer).resolve(provider);
    }

    @Test
    public void testConvertValue() {
        Object value = new Object();

        Object delegateValue = delegatingSerializer.convertValue(value);

        verify(mockConverter).convert(value);

        assertEquals(delegateValue, value);
    }

    @Test
    public void testWithDelegate() throws Exception {
        Class<Object> cls = Object.class;
        Converter<Object, ?> converter = mockConverter;
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<?> delegateSerializer = mockDelegateSerializer;

        StdDelegatingSerializer actual = delegatingSerializer.withDelegate(converter, delegateType, delegateSerializer);

        assertEquals(delegatingSerializer.getClass(), actual.getClass());
        assertEquals(converter, actual.getConverter());
        assertEquals(delegateType, actual._delegateType);
        assertEquals(delegateSerializer, actual._delegateSerializer);
    }
}