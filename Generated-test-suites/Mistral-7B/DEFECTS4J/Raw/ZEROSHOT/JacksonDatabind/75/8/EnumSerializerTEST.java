package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.Enum;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;

public class EnumSerializerTest {

    @Test
    public void testSerializationWithIndex() throws JsonProcessingException {
        MyEnum enumValue = MyEnum.FIRST;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(EnumSerializer.class).createContextual(null, null);
        gen.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, ObjectMapper.DefaultTyping.NONE);
        gen.writeValue(null, enumValue);
        assertEquals(Integer.valueOf(0), gen.getOutputContent());
    }

    @Test
    public void testSerializationWithString() throws JsonProcessingException {
        MyEnum enumValue = MyEnum.SECOND;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(EnumSerializer.class).createContextual(null, null);
        gen.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, ObjectMapper.DefaultTyping.NONE);
        gen.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        gen.writeValue(null, enumValue);
        assertEquals("SECOND", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializationWithJsonFormatAnnotation() throws JsonProcessingException {
        MyEnumWithJsonFormat enumValue = MyEnumWithJsonFormat.FIRST;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(EnumSerializer.class).createContextual(null, null);
        gen.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, ObjectMapper.DefaultTyping.NONE);
        gen.writeValue(null, enumValue);
        assertEquals(Integer.valueOf(0), gen.getOutputContent());
    }

    @Test
    public void testSerializationWithJsonFormatAnnotationToString() throws JsonProcessingException {
        MyEnumWithJsonFormat enumValue = MyEnumWithJsonFormat.SECOND;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(EnumSerializer.class).createContextual(null, null);
        gen.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, ObjectMapper.DefaultTyping.NONE);
        gen.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        gen.writeValue(null, enumValue);
        assertEquals("SECOND", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializationWithContextualSerializer() throws JsonProcessingException {
        MyEnumContextual enumValue = MyEnumContextual.FIRST;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(EnumSerializer.class).createContextual(null, null);
        gen.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, ObjectMapper.DefaultTyping.NONE);
        gen.writeValue(null, enumValue);
        assertEquals(Integer.valueOf(0), gen.getOutputContent());
    }

    @Test
    public void testSerializationWithContextualSerializerToString() throws JsonProcessingException {
        MyEnumContextual enumValue = MyEnumContextual.SECOND;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(EnumSerializer.class).createContextual(null, null);
        gen.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, ObjectMapper.DefaultTyping.NONE);
        gen.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        gen.writeValue(null, enumValue);
        assertEquals("SECOND", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializationWithNull() throws JsonProcessingException {
        MyEnum nullEnum = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(EnumSerializer.class).createContextual(null, null);
        gen.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, ObjectMapper.DefaultTyping.NONE);
        gen.writeValue(null, nullEnum);
        assertTrue(gen.getOutputContent().isNullValue());
    }

    @Test
    public void testDeserializationWithIndex() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("0");
        MyEnum enumValue = mapper.readValue(parser, MyEnum.class);
        assertEquals(MyEnum.FIRST, enumValue);
    }

    @Test
    public void testDeserializationWithString() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"FIRST\"");
        MyEnum enumValue = mapper.readValue(parser, MyEnum.class);
        assertEquals(MyEnum.FIRST, enumValue);
    }

    @Test
    public void testDeserializationWithJsonFormatAnnotation() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        ObjectNode node = mapper.createObjectNode();
        node.put("@JsonFormat(shape = Shape.INT)", MyEnumWithJsonFormat.class.getName());
        JsonParser parser = mapper.getFactory().createParser(node);
        MyEnumWithJsonFormat enumValue = mapper.readValue(parser, MyEnumWithJsonFormat.class);
        assertEquals(MyEnumWithJsonFormat.FIRST, enumValue);
    }

    @Test
    public void testDeserializationWithJsonFormatAnnotationToString() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        ObjectNode node = mapper.createObjectNode();
        node.put("@JsonFormat(shape = Shape.STRING)", MyEnumWithJsonFormat.class.getName());
        JsonParser parser = mapper.getFactory().createParser(node);
        MyEnumWithJsonFormat enumValue = mapper.readValue(parser, MyEnumWithJsonFormat.class);
        assertEquals(MyEnumWithJsonFormat.SECOND, enumValue);
    }

    @Test
    public void testDeserializationWithContextualSerializer() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("enumType", "MyEnumContextual");
        JsonParser parser = mapper.getFactory().createParser(node);
        MyEnumContextual enumValue = mapper.readValue(parser, MyEnumContextual.class);
        assertEquals(MyEnumContextual.FIRST, enumValue);
    }

    @Test
    public void testDeserializationWithContextualSerializerToString() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("enumType", "MyEnumContextual");
        JsonParser parser = mapper.getFactory().createParser(node);
        MyEnumContextual enumValue = mapper.readValue(parser, MyEnumContextual.class);
        assertEquals(MyEnumContextual.SECOND, enumValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithNull() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.putNull("enumType");
        JsonParser parser = mapper.getFactory().createParser(node);
        mapper.readValue(parser, MyEnum.class);
    }

    enum MyEnum {
        FIRST,
        SECOND
    }

    enum MyEnumWithJsonFormat {
        @JsonFormat(shape = Shape.INT)
        FIRST,
        @JsonFormat(shape = Shape.STRING)
        SECOND
    }

    enum MyEnumContextual {
        @JsonFormat(shape = Shape.INT)
        FIRST,
        @JsonFormat(shape = Shape.STRING)
        SECOND
    }
}