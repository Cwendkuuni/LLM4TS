package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * Container class for serializers used for handling standard JDK-provided types.
 */
@SuppressWarnings("serial")
public class NumberSerializersTest {
    private Map<String, JsonSerializer<?>> allDeserializers;
    private BeanProperty property;
    private SerializerProvider provider;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        property = mock(BeanProperty.class);
        provider = mock(SerializerProvider.class);
    }

    @Test
    public void testAddAll() {
        NumberSerializers.addAll(allDeserializers);
        assertEquals(IntegerSerializer.class.getName(), allDeserializers.get(Integer.class.getName()).getClass().getName());
        assertEquals(IntegerSerializer.class.getName(), allDeserializers.get(Integer.TYPE.getName()).getClass().getName());
        assertEquals(LongSerializer.class.getName(), allDeserializers.get(Long.class.getName()).getClass().getName());
        assertEquals(LongSerializer.class.getName(), allDeserializers.get(Long.TYPE.getName()).getClass().getName());
        assertEquals(IntLikeSerializer.class.getName(), allDeserializers.get(Byte.class.getName()).getClass().getName());
        assertEquals(IntLikeSerializer.class.getName(), allDeserializers.get(Byte.TYPE.getName()).getClass().getName());
        assertEquals(ShortSerializer.class.getName(), allDeserializers.get(Short.class.getName()).getClass().getName());
        assertEquals(ShortSerializer.class.getName(), allDeserializers.get(Short.TYPE.getName()).getClass().getName());
        assertEquals(FloatSerializer.class.getName(), allDeserializers.get(Float.class.getName()).getClass().getName());
        assertEquals(FloatSerializer.class.getName(), allDeserializers.get(Float.TYPE.getName()).getClass().getName());
        assertEquals(DoubleSerializer.class.getName(), allDeserializers.get(Double.class.getName()).getClass().getName());
        assertEquals(DoubleSerializer.class.getName(), allDeserializers.get(Double.TYPE.getName()).getClass().getName());
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        assertEquals(Short.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("number", serializer._schemaType);

        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(Short.valueOf("100"), gen, provider);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer();
        assertEquals(Integer.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);

        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(Integer.valueOf("200"), gen, provider);
    }

    @Test
    public void testIntegerSerializer_WithType() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer();
        assertEquals(Integer.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);

        JsonGenerator gen = mock(JsonGenerator.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        serializer.serializeWithType(Integer.valueOf("200"), gen, provider, typeSer);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        assertEquals(Number.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);

        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(100, gen, provider);
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer();
        assertEquals(Long.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.LONG, serializer._numberType);
        assertEquals("number", serializer._schemaType);

        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(Long.valueOf("300"), gen, provider);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        assertEquals(Float.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.FLOAT, serializer._numberType);
        assertEquals("number", serializer._schemaType);

        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(Float.valueOf("3.14"), gen, provider);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer();
        assertEquals(Double.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.DOUBLE, serializer._numberType);
        assertEquals("number", serializer._schemaType);

        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(Double.valueOf("3.14159"), gen, provider);
    }

    @Test
    public void testBase() throws IOException {
        Base<Object> serializer = new Base<Object>(Integer.class, JsonParser.NumberType.INT, "schemaType") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
                    throws JsonMappingException {
                return null;
            }
        };

        assertEquals(Integer.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("schemaType", serializer._schemaType);

        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(Integer.valueOf("100"), gen, provider);
        assertNull(serializer.getSchema(provider, null));
        serializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), null);
        assertNull(serializer.createContextual(provider, null));
    }
}