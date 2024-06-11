package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    @Mock
    private Converter<Object, ?> converter;
    @Mock
    private JsonGenerator gen;
    @Mock
    private SerializerProvider provider;
    @Mock
    private TypeSerializer typeSer;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private SerializerProvider prov;
    @Mock
    private BeanProperty property;
    @Mock
    private JsonFormatVisitorWrapper visitor;
    @Mock
    private JavaType typeHint;

    private StdDelegatingSerializer serializer;

    public void setup() {
        MockitoAnnotations.initMocks(this);
        serializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        ResolvableSerializer resolvableSerializer = mock(ResolvableSerializer.class);
        serializer._delegateSerializer = resolvableSerializer;
        serializer.resolve(provider);
        verify(resolvableSerializer).resolve(same(provider));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonSerializer<?> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;
        assertEquals(serializer, serializer.createContextual(prov, property));
        verifyZeroInteractions(delegateSerializer);
    }

    @Test
    public void testSerialize() throws Exception {
        Object value = new Object();
        serializer.serialize(value, gen, provider);
        verify(converter).convert(value);
        verify(provider).defaultSerializeNull(same(gen));
    }

    @Test
    public void testSerializeWithType() throws Exception {
        Object value = new Object();
        serializer.serializeWithType(value, gen, provider, typeSer);
        verify(converter).convert(value);
    }

    @Test
    @Deprecated
    public void testIsEmpty1() {
        Object value = new Object();
        serializer.isEmpty(value);
        verify(converter).convert(value);
    }

    @Test
    public void testIsEmpty2() {
        Object value = new Object();
        serializer.isEmpty(prov, value);
        verify(converter).convert(value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SchemaAware delegateSerializer = mock(SchemaAware.class);
        when(serializer._delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(serializer._delegateSerializer).thenReturn(delegateSerializer);
        serializer.getSchema(prov, typeHint);
        verify(delegateSerializer).getSchema(same(prov), same(typeHint));
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        SchemaAware delegateSerializer = mock(SchemaAware.class);
        when(serializer._delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(serializer._delegateSerializer).thenReturn(delegateSerializer);
        serializer.getSchema(prov, typeHint, true);
        verify(delegateSerializer).getSchema(same(prov), same(typeHint), eq(true));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        when(serializer._delegateSerializer).thenReturn(mock(JsonSerializer.class));
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(serializer._delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue() {
        Object value = new Object();
        Object convertedValue = new Object();
        when(converter.convert(value)).thenReturn(convertedValue);
        assertEquals(convertedValue, serializer.convertValue(value));
        verify(converter).convert(value);
    }
}