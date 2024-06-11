package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Mock
    private Converter<Object, Object> _converter;

    @Mock
    private JavaType _delegateType;

    @Mock
    private JsonSerializer<Object> _delegateSerializer;

    private StdDelegatingSerializer stdDelegatingSerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stdDelegatingSerializer = new StdDelegatingSerializer(_converter);
    }

    @Test
    public void convertValue_ShouldCallConverterConvertMethod() {
        Object value = new Object();
        stdDelegatingSerializer.convertValue(value);
        verify(_converter, times(1)).convert(value);
    }

    @Test
    public void getDelegatee_ShouldReturnDelegateSerializer() {
        stdDelegatingSerializer._delegateSerializer = _delegateSerializer;
        JsonSerializer<?> delegatee = stdDelegatingSerializer.getDelegatee();
        assertEquals(_delegateSerializer, delegatee);
    }

    @Test
    public void serialize_ShouldSerializeDelegateValue() throws IOException {
        Object value = new Object();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        stdDelegatingSerializer._delegateSerializer = _delegateSerializer;

        stdDelegatingSerializer.serialize(value, jsonGenerator, provider);

        verify(_delegateSerializer, times(1)).serialize(any(), any(JsonGenerator.class), any(SerializerProvider.class));
    }

    @Test
    public void serializeWithType_ShouldSerializeDelegateValue() throws IOException {
        Object value = new Object();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        stdDelegatingSerializer._delegateSerializer = _delegateSerializer;

        stdDelegatingSerializer.serializeWithType(value, jsonGenerator, provider, typeSer);

        verify(_delegateSerializer, times(1)).serializeWithType(any(), any(JsonGenerator.class), any(SerializerProvider.class), any(TypeSerializer.class));
    }

    @Test
    public void isEmpty_ObjectValue_ShouldCallDelegateSerializerIsEmpty() {
        Object value = new Object();
        stdDelegatingSerializer._delegateSerializer = _delegateSerializer;

        stdDelegatingSerializer.isEmpty(value);

        verify(_delegateSerializer, times(1)).isEmpty(any());
    }

    @Test
    public void isEmpty_SerializerProviderAndObjectValue_ShouldCallDelegateSerializerIsEmpty() {
        Object value = new Object();
        stdDelegatingSerializer._delegateSerializer = _delegateSerializer;
        SerializerProvider prov = mock(SerializerProvider.class);

        stdDelegatingSerializer.isEmpty(prov, value);

        verify(_delegateSerializer, times(1)).isEmpty(any(SerializerProvider.class), any());
    }

    @Test
    public void getSchema_TypeHint_ShouldReturnSchemaFromDelegateSerializer() throws JsonMappingException {
        Type typeHint = mock(Type.class);
        stdDelegatingSerializer._delegateSerializer = _delegateSerializer;
        SerializerProvider provider = mock(SerializerProvider.class);

        stdDelegatingSerializer.getSchema(provider, typeHint);

        verify(_delegateSerializer, times(1)).getSchema(provider, typeHint);
    }

    @Test
    public void getSchema_TypeHintAndIsOptional_ShouldReturnSchemaFromDelegateSerializer() throws JsonMappingException {
        Type typeHint = mock(Type.class);
        boolean isOptional = true;
        stdDelegatingSerializer._delegateSerializer = _delegateSerializer;
        SerializerProvider provider = mock(SerializerProvider.class);

        stdDelegatingSerializer.getSchema(provider, typeHint, isOptional);

        verify(_delegateSerializer, times(1)).getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void acceptJsonFormatVisitor_VisitorAndTypeHint_ShouldCallDelegateSerializerAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        stdDelegatingSerializer._delegateSerializer = _delegateSerializer;

        stdDelegatingSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(_delegateSerializer, times(1)).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test(expected = IllegalStateException.class)
    public void withDelegate_ShouldThrowIllegalStateException() {
        stdDelegatingSerializer.withDelegate(_converter, _delegateType, _delegateSerializer);
    }

}