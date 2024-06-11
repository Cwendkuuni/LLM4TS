
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;

@SuppressWarnings("serial")
public class StdDelegatingSerializerTest {

    private Converter<Object, Object> converter;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private TypeSerializer typeSerializer;
    private BeanProperty beanProperty;

    @Before
    public void setUp() {
        converter = mock(Converter.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        typeSerializer = mock(TypeSerializer.class);
        beanProperty = mock(BeanProperty.class);
    }

    @Test
    public void testSerialize() throws JsonMappingException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer<>(converter);

        Object value = new Object();
        Object delegateValue = new Object();
        
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeObject(delegateValue);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer<>(converter);

        Object value = new Object();
        Object delegateValue = new Object();
        
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer.serializeWithType(value, jsonGenerator, serializerProvider, typeSerializer);

        verify(jsonGenerator, times(1)).writeObject(delegateValue);
    }

    @Test
    public void testIsEmpty() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer<>(converter);

        Object value = new Object();
        Object delegateValue = new Object();
        
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer.isEmpty(serializerProvider, value);

        verify(serializerProvider, times(1)).isEmpty(delegateValue);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer<>(converter);

        JavaType typeHint = mock(JavaType.class);
        serializer.getSchema(serializerProvider, typeHint);

        verify(serializerProvider, times(1)).findValueSerializer(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer<>(converter);

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).expectAnyFormat(typeHint);
    }
}
