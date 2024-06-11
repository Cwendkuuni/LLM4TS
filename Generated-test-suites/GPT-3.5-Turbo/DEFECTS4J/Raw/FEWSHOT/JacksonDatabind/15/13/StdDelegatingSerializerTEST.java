import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Mock
    private Converter<Object, ?> converter;
    @Mock
    private JavaType delegateType;
    @Mock
    private JsonSerializer<Object> delegateSerializer;
    @Mock
    private SerializerProvider provider;
    @Mock
    private BeanProperty property;
    @Mock
    private JsonFormatVisitorWrapper visitor;
    @Mock
    private Type typeHint;
    @Mock
    private JsonGenerator gen;
    @Mock
    private TypeSerializer typeSer;
    @Mock
    private Object value;
    @Mock
    private Object delegateValue;
    @Mock
    private JsonNode schema;
    
    private StdDelegatingSerializer serializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        ResolvableSerializer resolvableSerializer = mock(ResolvableSerializer.class);

        when(delegateSerializer instanceof ResolvableSerializer).thenReturn(true);
        when(delegateSerializer).thenReturn(resolvableSerializer);

        serializer.resolve(provider);

        verify(resolvableSerializer).resolve(provider);
    }

    @Test
    public void testCreateContextualWithDelegateSerializer() throws JsonMappingException {
        when(delegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(provider.handleSecondaryContextualization(delegateSerializer, property)).thenReturn(delegateSerializer);

        JsonSerializer<?> result = serializer.createContextual(provider, property);

        assertEquals(delegateSerializer, result);
    }

    @Test
    public void testCreateContextualWithoutDelegateSerializer() throws JsonMappingException {
        when(converter.getOutputType(provider.getTypeFactory())).thenReturn(delegateType);
        when(provider.findValueSerializer(delegateType)).thenReturn(delegateSerializer);
        when(delegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(provider.handleSecondaryContextualization(delegateSerializer, property)).thenReturn(delegateSerializer);

        JsonSerializer<?> result = serializer.createContextual(provider, property);

        assertEquals(delegateSerializer, result);
    }

    @Test
    public void testCreateContextualWithSameDelegateSerializer() throws JsonMappingException {
        when(delegateSerializer instanceof ContextualSerializer).thenReturn(true);

        JsonSerializer<?> result = serializer.createContextual(provider, property);

        assertEquals(serializer, result);
    }

    @Test
    public void testSerializeWithNullValue() throws IOException {
        when(convertValue(value)).thenReturn(null);
        when(provider.defaultSerializeNull(gen)).thenReturn(gen);

        serializer.serialize(value, gen, provider);

        verify(provider).defaultSerializeNull(gen);
    }

    @Test
    public void testSerialize() throws IOException {
        when(convertValue(value)).thenReturn(delegateValue);

        serializer.serialize(value, gen, provider);

        verify(delegateSerializer).serialize(delegateValue, gen, provider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        when(convertValue(value)).thenReturn(delegateValue);

        serializer.serializeWithType(value, gen, provider, typeSer);

        verify(delegateSerializer).serializeWithType(delegateValue, gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty() {
        when(convertValue(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);

        boolean result = serializer.isEmpty(provider, value);

        assertEquals(true, result);
    }

    @Test
    public void testGetSchemaWithSchemaAwareDelegateSerializer() throws JsonMappingException {
        SchemaAware schemaAware = mock(SchemaAware.class);

        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(delegateSerializer).thenReturn(schemaAware);
        when(schemaAware.getSchema(provider, typeHint)).thenReturn(schema);

        JsonNode result = serializer.getSchema(provider, typeHint);

        assertEquals(schema, result);
    }

    @Test
    public void testGetSchemaWithoutSchemaAwareDelegateSerializer() throws JsonMappingException {
        JsonNode result = serializer.getSchema(provider, typeHint);

        assertEquals(null, result);
    }

    @Test
    public void testGetSchemaWithSchemaAwareDelegateSerializerAndOptional() throws JsonMappingException {
        SchemaAware schemaAware = mock(SchemaAware.class);

        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(delegateSerializer).thenReturn(schemaAware);
        when(schemaAware.getSchema(provider, typeHint, true)).thenReturn(schema);

        JsonNode result = serializer.getSchema(provider, typeHint, true);

        assertEquals(schema, result);
    }

    @Test
    public void testGetSchemaWithoutSchemaAwareDelegateSerializerAndOptional() throws JsonMappingException {
        JsonNode result = serializer.getSchema(provider, typeHint, true);

        assertEquals(null, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue() {
        when(converter.convert(value)).thenReturn(delegateValue);

        Object result = serializer.convertValue(value);

        assertEquals(delegateValue, result);
    }
}