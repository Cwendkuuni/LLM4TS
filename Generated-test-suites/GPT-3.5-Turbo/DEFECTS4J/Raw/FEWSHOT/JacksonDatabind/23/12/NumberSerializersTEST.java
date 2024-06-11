package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

public class NumberSerializersTest {
    private NumberSerializers.Base base;
    private JsonSerializer jsonSerializer;
    private SerializerProvider serializerProvider;
    private BeanProperty beanProperty;
    private JsonGenerator jsonGenerator;
    private TypeSerializer typeSerializer;

    @Before
    public void setup(){
        base = mock(NumberSerializers.Base.class);
        jsonSerializer = mock(JsonSerializer.class);
        serializerProvider = mock(SerializerProvider.class);
        beanProperty = mock(BeanProperty.class);
        jsonGenerator = mock(JsonGenerator.class);
        typeSerializer = mock(TypeSerializer.class);
    }

    @Test
    public void testAddAll() {
        HashMap<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(9, allDeserializers.size());
    }

    // Base class cannot be instantiated

    @Test
    public void testShortSerializer() throws IOException{
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        Short value = 1;
        shortSerializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testIntegerSerializer() throws IOException{
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        Integer value = 1;
        integerSerializer.serialize(value, jsonGenerator, serializerProvider);
        integerSerializer.serializeWithType(value, jsonGenerator, serializerProvider, typeSerializer);
    }

    @Test
    public void testIntLikeSerializer() throws IOException{
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        Number value = 1;
        intLikeSerializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testLongSerializer() throws IOException{
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();
        Long value = Long.parseLong("1");
        longSerializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testFloatSerializer() throws IOException{
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        Float value = 1.0f;
        floatSerializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testDoubleSerializer() throws IOException{
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
        Double value = 1.0;
        doubleSerializer.serialize(value, jsonGenerator, serializerProvider);
        doubleSerializer.serializeWithType(value, jsonGenerator, serializerProvider, typeSerializer);
    }

    // acceptance tests for getSchema and acceptJsonFormatVisitor methods are skipped as they both call non-stubbed abstract method
}