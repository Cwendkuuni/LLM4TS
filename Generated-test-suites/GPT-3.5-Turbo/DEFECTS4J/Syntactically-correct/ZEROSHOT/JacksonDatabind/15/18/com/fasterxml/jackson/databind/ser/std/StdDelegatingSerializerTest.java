package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

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
    private JsonGenerator gen;

    @Mock
    private TypeSerializer typeSer;

    private StdDelegatingSerializer stdDelegatingSerializer;

    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stdDelegatingSerializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void shouldSerializeNullValue() throws IOException {
        //given
        Object value = null;

        //when
        stdDelegatingSerializer.serialize(value, gen, provider);

        //then
        verify(provider).defaultSerializeNull(gen);
    }

    @Test
    public void shouldSerializeNonNullValue() throws IOException {
        //given
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        //when
        stdDelegatingSerializer.serialize(value, gen, provider);

        //then
        verify(delegateSerializer).serialize(delegateValue, gen, provider);
    }

    @Test
    public void shouldSerializeWithDelegateType() throws IOException {
        //given
        Object value = new Object();
        Object delegateValue = new Object();
        stdDelegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        when(converter.convert(value)).thenReturn(delegateValue);

        //when
        stdDelegatingSerializer.serialize(value, gen, provider);

        //then
        verify(delegateSerializer).serialize(delegateValue, gen, provider);
    }

    @Test
    public void shouldSerializeWithType() throws IOException {
        //given
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        //when
        stdDelegatingSerializer.serializeWithType(value, gen, provider, typeSer);

        //then
        verify(delegateSerializer).serializeWithType(delegateValue, gen, provider, typeSer);
    }

    @Test
    public void shouldCheckIfValueIsEmpty() {
        //given
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);

        //when
        stdDelegatingSerializer.isEmpty(provider, value);

        //then
        verify(delegateSerializer).isEmpty(delegateValue);
    }

    @Test
    public void shouldCheckIfValueIsEmptyWithSerializerProvider() {
        //given
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(provider, delegateValue)).thenReturn(true);

        //when
        stdDelegatingSerializer.isEmpty(provider, value);

        //then
        verify(delegateSerializer).isEmpty(provider, delegateValue);
    }

    @Test
    public void shouldGetSchemaForValue() {
        //given
        Type typeHint = null;

        //when
        stdDelegatingSerializer.getSchema(provider, typeHint);

        //then
        verify(delegateSerializer).getSchema(provider, typeHint);
    }

    @Test
    public void shouldGetSchemaWithOptionalValue() {
        //given
        Type typeHint = null;
        boolean isOptional = false;

        //when
        stdDelegatingSerializer.getSchema(provider, typeHint, isOptional);

        //then
        verify(delegateSerializer).getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void shouldAcceptJsonFormatVisitor() {
        //given
        JavaType typeHint = null;

        //when
        stdDelegatingSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        //then
        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
    }
}