package com.example.junit;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.ClassCastException;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class EnumSerializerTest {

    private static final Class<MyEnum> MY_ENUM_CLASS = MyEnum.class;
    private static final ObjectMapper MAPPER = JsonMapperFactory.getMapper();
    private EnumSerializer<MyEnum> enumSerializer;
    private EnumValues enumValues;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        enumValues = EnumValues.constructFromName(MAPPER.getSerializationConfig(), MY_ENUM_CLASS);
        enumSerializer = new EnumSerializer<>(enumValues, null);
    }

    @Test
    public void testSerializeToString() throws JsonGenerationException, JsonMappingException, IOException {
        Enum<MyEnum> enumValue = MyEnum.FIRST;
        String expected = enumValues.serializedValueFor(enumValue);

        String result = serializeAndReadValueAsString(enumSerializer, enumValue);

        assertEquals(expected, result);
    }

    @Test
    public void testSerializeToIndex() throws JsonGenerationException, JsonMappingException, IOException {
        Enum<MyEnum> enumValue = MyEnum.FIRST;
        int expectedOrdinal = enumValue.ordinal();

        int result = serializeAndReadValueAsInt(enumSerializer, enumValue);

        assertEquals(expectedOrdinal, result);
    }

    @Test
    public void testSerializeToStringWithToStringEnabled() throws JsonGenerationException, JsonMappingException, IOException {
        Enum<MyEnum> enumValue = MyEnum.SECOND;
        String expectedToString = enumValue.toString();

        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        String result = serializeAndReadValueAsString(enumSerializer, enumValue, serializerProvider);

        assertEquals(expectedToString, result);
    }

    @Test
    public void testSerializeToStringWithToStringDisabled() throws JsonGenerationException, JsonMappingException, IOException {
        Enum<MyEnum> enumValue = MyEnum.THIRD;

        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        String result = serializeAndReadValueAsString(enumSerializer, enumValue, serializerProvider);

        String expected = enumValues.serializedValueFor(enumValue);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode expectedSchema = MAPPER.readTree("{\"type\": \"string\", \"enum\": [\"FIRST\", \"SECOND\", \"THIRD\"]}");

        JsonNode result = enumSerializer.getSchema(MAPPER.getSerializationConfig(), MY_ENUM_CLASS);

        assertEquals(expectedSchema, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateContextualForInvalidType() throws JsonMappingException {
        ObjectMapper objectMapper = MAPPER;
        ObjectNode objectNode = objectMapper.createObjectNode();
        BeanDescription beanDescription = mock(BeanDescription.class);
        when(beanDescription.getType()).thenReturn(Integer.class);

        enumSerializer.createContextual(objectMapper.getSerializerProvider(), beanDescription);
    }

    @Test
    public void testCreateContextualForValidEnumType() throws JsonMappingException {
        ObjectMapper objectMapper = MAPPER;
        ObjectNode objectNode = objectMapper.createObjectNode();
        BeanDescription beanDescription = mock(BeanDescription.class);
        when(beanDescription.getType()).thenReturn(MY_ENUM_CLASS);

        JsonSerializer<?> result = enumSerializer.createContextual(objectMapper.getSerializerProvider(), beanDescription);

        assertTrue(result instanceof EnumSerializer);
    }

    private String serializeAndReadValueAsString(EnumSerializer<MyEnum> enumSerializer, Enum<MyEnum> enumValue, SerializerProvider serializerProvider) throws JsonGenerationException, JsonMappingException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(outputStream);
        enumSerializer.serialize(enumValue, jsonGenerator, serializerProvider);
        jsonGenerator.close();

        JsonParser jsonParser = MAPPER.getFactory().createParser(new ByteArrayInputStream(outputStream.toByteArray()));

        String result = jsonParser.readValueAs(String.class);

        return result;
    }

    private int serializeAndReadValueAsInt(EnumSerializer<MyEnum> enumSerializer, Enum<MyEnum> enumValue) throws JsonGenerationException, JsonMappingException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(outputStream);
        enumSerializer.serialize(enumValue, jsonGenerator, null);
        jsonGenerator.close();

        ObjectMapper objectMapper = MAPPER;
        ObjectNode objectNode = objectMapper.readValueAs(ObjectNode.class, new ByteArrayInputStream(outputStream.toByteArray()));

        int result = objectMapper.readerForUpdating(0)
                .readValueAs(Integer.class, JsonParser.parseString(objectNode.toString()));

        return result;
    }
}

enum MyEnum {
    FIRST,
    SECOND,
    THIRD
}