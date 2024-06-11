package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testEmptyConstructor() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Assert.assertNull(serializer._converter);
        Assert.assertNull(serializer._delegateType);
        Assert.assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testObjectConstructor() {
        Converter<Object, ?> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Assert.assertEquals(converter, serializer._converter);
        Assert.assertNull(serializer._delegateType);
        Assert.assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testClassConstructor() {
        Class<Object> cls = Object.class;
        Converter<Object, ?> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(cls, converter);
        Assert.assertEquals(cls, serializer.getClass());
        Assert.assertEquals(converter, serializer._converter);
        Assert.assertNull(serializer._delegateType);
        Assert.assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testThreeArgConstructor() {
        Converter<Object, ?> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Assert.assertEquals(converter, serializer._converter);
        Assert.assertEquals(delegateType, serializer._delegateType);
        Assert.assertEquals(delegateSerializer, serializer._delegateSerializer);
    }

    @Test
    public void testWithDelegate() {
        Converter<Object, ?> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        StdDelegatingSerializer newSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);
        Assert.assertEquals(converter, newSerializer._converter);
        Assert.assertEquals(delegateType, newSerializer._delegateType);
        Assert.assertEquals(delegateSerializer, newSerializer._delegateSerializer);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, IOException {
        ObjectInput source = mock(ObjectInput.class);
        Converter<Object, ?> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();

        when(converter.convert(value)).thenReturn(new Object());
        when(delegateSerializer.serialize(new Object(), any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(null);

        serializer.serialize(value, null, null);

        verify(delegateSerializer).serialize(any(), any(), any());
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, IOException {
        ObjectInput source = mock(ObjectInput.class);
        Converter<Object, ?> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();
        Type type = mock(Type.class);

        when(converter.convert(value)).thenReturn(new Object());
        when(delegateSerializer.serializeWithType(new Object(), any(JsonGenerator.class), any(SerializerProvider.class), any(TypeSerializer.class))).thenReturn(null);

        serializer.serializeWithType(value, null, null, typeSerializer);

        verify(delegateSerializer).serializeWithType(any(), any(), any(), any());
    }

    @Test
    public void testIsEmpty() throws IOException {
        ObjectInput source = mock(ObjectInput.class);
        Converter<Object, ?> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();
        boolean expected = false;

        when(converter.convert(value)).thenReturn(null);
        when(delegateSerializer.isEmpty(null)).thenReturn(expected);

        boolean result = serializer.isEmpty(value);

        verify(delegateSerializer).isEmpty(any());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectInput source = mock(ObjectInput.class);
        Converter<Object, ?> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Type typeHint = mock(Type.class);

        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(delegateSerializer.getSchema(any(SerializerProvider.class), typeHint)).thenReturn(null);

        Object schema = serializer.getSchema(null, typeHint);

        verify(delegateSerializer).getSchema(any(), typeHint);
        Assert.assertNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectInput source = mock(ObjectInput.class);
        Converter<Object, ?> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue() throws IOException {
        ObjectInput source = mock(ObjectInput.class);
        Converter<Object, ?> converter = mock(Converter.class);
        Object value = new Object();
        Object result = new Object();

        when(converter.convert(value)).thenReturn(result);

        Object delegateValue = new StdDelegatingSerializer(converter).convertValue(value);

        Assert.assertEquals(result, delegateValue);
    }
}