package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    @Test
    public void testConstructor_WithConverter() {
        Converter<Object, String> converter = new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
        assertEquals(converter, serializer._converter);
    }

    @Test
    public void testConstructor_WithType() {
        Converter<Object, String> converter = new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        };

        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, type, null);

        assertEquals(type, serializer._delegateType);
        assertNull(serializer._delegateSerializer);
        assertEquals(converter, serializer._converter);
    }


    @Test
    public void testSerialize_WithNull() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });

        serializer.serialize(null, mockJsonGenerator, mockSerializerProvider);
        assertTrue(serializer.isEmpty(mockSerializerProvider, null));
    }

    @Test
    public void testSerialize_WithNonNull() throws IOException {
        Converter<Object, String> converter = new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        serializer.serialize("test", mockJsonGenerator, mockSerializerProvider);
    }

    @Test
    public void testConvertValue() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<String, Integer>() {
            @Override
            public Integer convert(String value) {
                return Integer.parseInt(value);
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });

        Object result = serializer.convertValue("123");
        assertEquals(123, result);
    }
}