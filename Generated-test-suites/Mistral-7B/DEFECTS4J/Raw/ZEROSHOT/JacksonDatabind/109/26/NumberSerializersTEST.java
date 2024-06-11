package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@SuppressWarnings("unchecked")
public class NumberSerializersTest {

    private ObjectMapper objectMapper;
    private Map<String, JsonSerializer<?>> serializersMap;

    @BeforeClass
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        serializersMap = mock(Map.class);
        when(objectMapper.getSerializers()).thenReturn(objectMapper.getDefaultSerializerProvider().getSerializers());
        doReturn(serializersMap).when(objectMapper.getDefaultSerializerProvider()).findDeserializersForClass(any());
    }

    @Test
    public void testAddAll() throws Exception {
        NumberSerializers numberSerializers = new NumberSerializers();

        numberSerializers.addAll(serializersMap);

        assertEquals(serializersMap.size(), 11);
        assertEquals(serializersMap.get(Integer.class.getName()), instanceOf(IntegerSerializer.class));
        assertEquals(serializersMap.get(Integer.TYPE.getName()), instanceOf(IntegerSerializer.class));
        assertEquals(serializersMap.get(Long.class.getName()), instanceOf(LongSerializer.class));
        assertEquals(serializersMap.get(Long.TYPE.getName()), instanceOf(LongSerializer.class));
        assertEquals(serializersMap.get(Byte.class.getName()), instanceOf(IntLikeSerializer.class));
        assertEquals(serializersMap.get(Byte.TYPE.getName()), instanceOf(IntLikeSerializer.class));
        assertEquals(serializersMap.get(Short.class.getName()), instanceOf(ShortSerializer.class));
        assertEquals(serializersMap.get(Short.TYPE.getName()), instanceOf(ShortSerializer.class));
        assertEquals(serializersMap.get(Double.class.getName()), instanceOf(DoubleSerializer.class));
        assertEquals(serializersMap.get(Double.TYPE.getName()), instanceOf(DoubleSerializer.class));
        assertEquals(serializersMap.get(Float.class.getName()), instanceOf(FloatSerializer.class));
        assertEquals(serializersMap.get(Float.TYPE.getName()), instanceOf(FloatSerializer.class));
    }

    @Test
    public void testBaseClass() throws Exception {
        JsonParser mockParser = mock(JsonParser.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        Object value = 123;

        Base<Object> shortSerializer = new ShortSerializer();
        Base<Object> integerSerializer = new IntegerSerializer(Integer.class);
        Base<Object> intLikeSerializer = new IntLikeSerializer();
        Base<Object> longSerializer = new LongSerializer(Long.class);

        when(mockParser.getCurrentName()).thenReturn("test");
        when(mockParser.getCodec()).thenReturn(objectMapper);
        when(mockParser.getParserConfig()).thenReturn(objectMapper.getSerializationConfig());
        when(mockParser.isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE)).thenReturn(true);
        when(mockParser.isEnabled(JsonParser.Feature.AUTO_DETECT_GETTERS)).thenReturn(true);
        when(mockParser.isEnabled(JsonParser.Feature.AUTO_DETECT_CREATORS)).thenReturn(true);
        when(mockParser.isEnabled(JsonParser.Feature.AUTO_DETECT_FIELDS)).thenReturn(true);

        when(mockParser.getSerializationConfig().isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);
        when(mockParser.getSerializationConfig().isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);

        when(mockParser.getValueAs(Integer.TYPE)).thenReturn(123);
        when(mockParser.getValueAs(Long.TYPE)).thenReturn(123L);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonSerializer<?> shortSerializerInstance = shortSerializer.accept(mockParser, objectMapper.getTypeFactory().constructType(Object.class));
        JsonSerializer<?> integerSerializerInstance = integerSerializer.accept(mockParser, objectMapper.getTypeFactory().constructType(Object.class));
        JsonSerializer<?> intLikeSerializerInstance = intLikeSerializer.accept(mockParser, objectMapper.getTypeFactory().constructType(Object.class));
        JsonSerializer<?> longSerializerInstance = longSerializer.accept(mockParser, objectMapper.getTypeFactory().constructType(Object.class));

        shortSerializerInstance.serialize(value, mockGenerator, objectMapper.getSerializerProvider());
        integerSerializerInstance.serialize(value, mockGenerator, objectMapper.getSerializerProvider());
        intLikeSerializerInstance.serialize(value, mockGenerator, objectMapper.getSerializerProvider());
        longSerializerInstance.serialize(value, mockGenerator, objectMapper.getSerializerProvider());

        verify(mockGenerator, times(4)).writeNumber(eq(123));
    }
}