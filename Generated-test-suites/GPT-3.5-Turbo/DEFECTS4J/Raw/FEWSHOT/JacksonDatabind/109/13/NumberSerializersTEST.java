package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

public class NumberSerializersTest {
    private NumberSerializers numberSerializers;
    private Map<String, JsonSerializer<?>> allDeserializers;
    private BeanProperty mockBeanProperty;
    private SerializerProvider mockSerializerProvider;
    private JsonFormat.Value mockJsonFormatValue;
    private JsonGenerator mockJsonGenerator;
    private TypeSerializer mockTypeSerializer;
    private Object mockValue;

    @Before
    public void setUp() throws Exception {
        numberSerializers = new NumberSerializers();
        allDeserializers = mock(Map.class);
        mockBeanProperty = mock(BeanProperty.class);
        mockSerializerProvider = mock(SerializerProvider.class);
        mockJsonFormatValue = mock(JsonFormat.Value.class);
        mockJsonGenerator = mock(JsonGenerator.class);
        mockTypeSerializer = mock(TypeSerializer.class);
        mockValue = mock(Object.class);
    }

    @Test
    public void testAddAll() {
        doNothing().when(allDeserializers).put(any(), any());
        numberSerializers.addAll(allDeserializers);
        verify(allDeserializers).put(Integer.class.getName(), new IntegerSerializer(Integer.class));
        verify(allDeserializers).put(Integer.TYPE.getName(), new IntegerSerializer(Integer.TYPE));
        verify(allDeserializers).put(Long.class.getName(), new LongSerializer(Long.class));
        verify(allDeserializers).put(Long.TYPE.getName(), new LongSerializer(Long.TYPE));
        verify(allDeserializers).put(Byte.class.getName(), IntLikeSerializer.instance);
        verify(allDeserializers).put(Byte.TYPE.getName(), IntLikeSerializer.instance);
        verify(allDeserializers).put(Short.class.getName(), ShortSerializer.instance);
        verify(allDeserializers).put(Short.TYPE.getName(), ShortSerializer.instance);
        verify(allDeserializers).put(Double.class.getName(), new DoubleSerializer(Double.class));
        verify(allDeserializers).put(Double.TYPE.getName(), new DoubleSerializer(Double.TYPE));
        verify(allDeserializers).put(Float.class.getName(), FloatSerializer.instance);
        verify(allDeserializers).put(Float.TYPE.getName(), FloatSerializer.instance);
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals(Short.class, shortSerializer.handledType());
        verify(mockJsonGenerator).writeNumber(any(Short.class));
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        assertEquals(Integer.class, integerSerializer.handledType());
        verify(mockJsonGenerator).writeNumber(any(Integer.class));
        integerSerializer.serializeWithType(mockValue, mockJsonGenerator, mockSerializerProvider,
                mockTypeSerializer);
        verify(mockJsonGenerator).writeNumber(any(Integer.class));
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertEquals(Number.class, intLikeSerializer.handledType());
        verify(mockJsonGenerator).writeNumber(any(Number.class));
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        assertEquals(Long.class, longSerializer.handledType());
        verify(mockJsonGenerator).writeNumber(any(Long.class));
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        assertEquals(Float.class, floatSerializer.handledType());
        verify(mockJsonGenerator).writeNumber(any(Float.class));
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        assertEquals(Double.class, doubleSerializer.handledType());
        verify(mockJsonGenerator).writeNumber(any(Double.class));
        doubleSerializer.serializeWithType(mockValue, mockJsonGenerator, mockSerializerProvider,
                mockTypeSerializer);
        verify(mockJsonGenerator).writeNumber(any(Double.class));
    }

    @Test
    public void testBase() throws IOException {
        Base<Object> base = numberSerializers.new Base<Object>(Number.class, JsonParser.NumberType.INT,
                "integer") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeNumber(0);
            }
        };
        assertEquals(Number.class, base.handledType());

        JsonNode schemaNode = base.getSchema(mockSerializerProvider, mock(Type.class));
        verify(schemaNode).isObject();
        verify(mockJsonGenerator).writeNumber(0);

        base.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), mock(JavaType.class));
        verify(mockJsonGenerator).writeNumber(0);

        JsonSerializer<?> contextualSerializer = base.createContextual(mockSerializerProvider,
                mockBeanProperty);
        assertEquals(base, contextualSerializer);
    }

}