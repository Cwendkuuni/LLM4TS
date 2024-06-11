package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        Class<MyEnum> enumClass = MyEnum.class;
        Type enumType = new ClassType(enumClass);
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        Collection<MyEnum> enumValues = EnumSet.allOf(MyEnum.class);
        EnumValues enumValuesEnum = new EnumValues(enumClass, enumValues);
        this.enumSerializer = new EnumSerializer(enumValuesEnum, true);
        this.objectMapper = new JsonMapper();
    }

    @Test
    public void testStandardSerialization() throws JsonProcessingException, IOException {
        MyEnum enumValue = MyEnum.VALUE_1;
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(System.out);
        this.enumSerializer.serialize(enumValue, jsonGenerator, this.objectMapper.getSerializerProvider());
        ObjectNode actualObjectNode = (ObjectNode) this.objectMapper.readTree(System.in);
        String expectedString = "VALUE_1";
        Assert.assertEquals(expectedString, actualObjectNode.get(0).asText());
    }

    @Test
    public void testSerializationAsIndex() throws JsonProcessingException, IOException {
        MyEnum enumValue = MyEnum.VALUE_1;
        SerializerProvider serializerProvider = this.objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = serializerProvider.getGeneratorForCurrentOutput();
        this.enumSerializer = this.enumSerializer.createContextual(null, null);
        this.enumSerializer.serialize(enumValue, jsonGenerator, serializerProvider);
        int actualInt = jsonGenerator.getCode();
        int expectedInt = enumValue.ordinal();
        Assert.assertEquals(expectedInt, actualInt);
    }

    @Test
    public void testSerializationUsingToString() throws JsonProcessingException, IOException {
        MyEnum enumValue = MyEnum.VALUE_3;
        SerializerProvider serializerProvider = this.objectMapper.getSerializerProvider();
        serializerProvider.setEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        this.enumSerializer = this.enumSerializer.createContextual(null, null);
        JsonGenerator jsonGenerator = serializerProvider.getGeneratorForCurrentOutput();
        this.enumSerializer.serialize(enumValue, jsonGenerator, serializerProvider);
        String actualString = jsonGenerator.getOutputFactory().createStringBuilder().subSequence(0, jsonGenerator.getOutputFactory().createStringBuilder().position()).toString();
        String expectedString = enumValue.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testSerializationWithEmptyEnumValues() throws JsonProcessingException, IOException {
        EnumSerializer emptyEnumSerializer = new EnumSerializer(new EnumValues(new Class<?>[0]), false);
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(System.out);
        emptyEnumSerializer.serialize(null, jsonGenerator, this.objectMapper.getSerializerProvider());
        Assert.assertEquals("null", jsonGenerator.getOutputFactory().createStringBuilder().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializationWithUnsupportedShape() throws JsonProcessingException, IOException {
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.NUMBER);
        Collection<MyEnum> enumValues = EnumSet.allOf(MyEnum.class);
        EnumValues enumValuesEnum = new EnumValues(MyEnum.class, enumValues);
        EnumSerializer enumSerializer = new EnumSerializer(enumValuesEnum, true);
        this.enumSerializer = enumSerializer.createContextual(null, null);
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(System.out);
        this.enumSerializer.serialize(MyEnum.VALUE_1, jsonGenerator, this.objectMapper.getSerializerProvider());
    }
}

enum MyEnum {
    VALUE_1,
    VALUE_2,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    VALUE_3
}