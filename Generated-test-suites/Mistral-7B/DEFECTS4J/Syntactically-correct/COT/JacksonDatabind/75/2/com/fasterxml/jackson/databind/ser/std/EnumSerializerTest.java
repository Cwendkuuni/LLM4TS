package com.example; // replace with your test package name

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.EnumSet;

public class EnumSerializerTest {
    

    @Test
    public void testConstructor() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.all(MyEnum.class));
        assertNotNull(serializer);
    }

    @Test
    public void testSerializeAsIndex() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.all(MyEnum.class), true);
        MyEnum enumValue = MyEnum.VALUE1;

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(enumValue, gen, null);
        gen.flush();

        String actual = mapper.readValue(System.out.toString(), String.class);
        assertEquals("0", actual);
    }

    @Test
    public void testSerializeToString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.all(MyEnum.class), false);
        MyEnum enumValue = MyEnum.VALUE1;

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(enumValue, gen, null);
        gen.flush();

        String actual = mapper.readValue(System.out.toString(), String.class);
        assertEquals(enumValue.name(), actual);
    }

    @Test
    public void testCreateContextualWithJsonFormat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyEnumProperty myEnumProperty = new MyEnumProperty();
        myEnumProperty.setMyEnum(MyEnum.VALUE1);

        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);

        EnumSerializer serializer = mapper.findValueSerializer(MyEnum.class, MyEnumProperty.class, null);
        assertNotNull(serializer);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(myEnumProperty.getMyEnum(), gen, null);
        gen.flush();

        String actual = mapper.readValue(System.out.toString(), String.class);
        assertEquals(MyEnum.VALUE1.name(), actual);
    }

    @Test
    public void testCreateContextualWithoutJsonFormat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyClass myClass = new MyClass();
        myClass.setMyEnum(MyEnum.VALUE1);

        EnumSerializer serializer = mapper.findValueSerializer(MyClass.class, null, null);
        assertNotNull(serializer);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(myClass.getMyEnum(), gen, null);
        gen.flush();

        String actual = mapper.readValue(System.out.toString(), String.class);
        assertEquals(MyEnum.VALUE1.name(), actual);
    }

    @Test
    public void testGetSchema() {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.all(MyEnum.class), null);

        ObjectNode schemaNode = serializer.getSchema(mapper.getSerializationConfig(), Object.class);
        assertNotNull(schemaNode);

        String expectedSchema = "{\"type\":\"object\",\"properties\":{\"type\":\"string\",\"enum\":[\"VALUE1\",\"VALUE2\"]},\"required\":[\"type\"]}";
        assertEquals(expectedSchema, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaNode));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.all(MyEnum.class), null);

        ObjectNode schemaNode = mapper.creatingBuilder()
                .readValue(getClass().getResource("/expected_schema.json"), ObjectNode.class);

        JsonFormatVisitorWrapper visitor = mapper.schemaProvider().finderFor(ObjectNode.class, null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
        JsonFormatSerializer serializerWrapper = visitor.getSerializers().get(0);

        assertEquals(schemaNode.toString(), serializerWrapper.getSchemaDescription().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsShapeWrittenUsingIndexWithUnsupportedShape() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.all(MyEnum.class), null);

        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.NUMBER);

        Boolean result = serializer._isShapeWrittenUsingIndex(MyEnum.class, jsonFormat, false);
        fail("IllegalArgumentException expected");
    }

    enum MyEnum {
        VALUE1,
        VALUE2;
    }

    static class MyEnumProperty {
        private MyEnum myEnum;

        public MyEnum getMyEnum() {
            return myEnum;
        }

        public void setMyEnum(MyEnum myEnum) {
            this.myEnum = myEnum;
        }
    }

    static class MyClass {
        private MyEnum myEnum;

        public MyEnum getMyEnum() {
            return myEnum;
        }

        public void setMyEnum(MyEnum myEnum) {
            this.myEnum = myEnum;
        }
    }

    
}