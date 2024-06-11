package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.EnumValues;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Test cases for {@link EnumSerializer}.
 */
@JacksonStdImpl
public class EnumSerializerTest {

    private static final Class<MyEnum> MY_ENUM_CLASS = MyEnum.class;

    /**
     * Test construction when no serialization format is specified.
     */
    @Test
    public void testConstructNoFormat() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructForClass(MY_ENUM_CLASS), null);
        assertEquals(MY_ENUM_CLASS, serializer.handledType());
        assertNull(serializer._values);
        assertNull(serializer._serializeAsIndex);
    }

    /**
     * Test construction when serialization as index is specified.
     */
    @Test
    public void testConstructSerializeAsIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructForClass(MY_ENUM_CLASS), true);
        assertEquals(MY_ENUM_CLASS, serializer.handledType());
        assertNotNull(serializer._values);
        assertTrue(serializer._serializeAsIndex);
    }

    /**
     * Test construction when serialization format is specified.
     */
    @Test
    public void testConstructFormat() throws Exception {
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);

        EnumSerializer serializer = new EnumSerializer(EnumValues.constructForClass(MY_ENUM_CLASS), null, null, format);
        assertEquals(MY_ENUM_CLASS, serializer.handledType());
        assertNotNull(serializer._values);
        assertNull(serializer._serializeAsIndex);

        assertEquals(JsonFormat.Shape.STRING, serializer._values.getSerializationShape());
    }

    /**
     * Test serialization as index.
     */
    @Test
    public void testSerializeAsIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructForClass(MY_ENUM_CLASS), true);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        serializer.serialize(MyEnum.VALUE_1, gen, new SerializerProvider());
        assertEquals((int) MyEnum.VALUE_1.ordinal(), gen.getCurrentPosition().getBytes()[0] & 0xFF);

        serializer.serialize(MyEnum.VALUE_2, gen, new SerializerProvider());
        assertEquals((int) MyEnum.VALUE_2.ordinal(), gen.getCurrentPosition().getBytes()[0] & 0xFF);
    }

    /**
     * Test serialization as name.
     */
    @Test
    public void testSerializeAsName() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructForClass(MY_ENUM_CLASS), null);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        serializer.serialize(MyEnum.VALUE_1, gen, new SerializerProvider());
        assertEquals("VALUE_1", gen.getCurrentValueAsString());

        serializer.serialize(MyEnum.VALUE_2, gen, new SerializerProvider());
        assertEquals("VALUE_2", gen.getCurrentValueAsString());
    }

    /**
     * Test serialization as name when "WRITE_ENUMS_USING_TO_STRING" feature is enabled.
     */
    @Test
    public void testSerializeAsNameWithToStringFeature() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        EnumSerializer serializer = new EnumSerializer(EnumValues.constructForClass(MY_ENUM_CLASS), null);

        serializer.serialize(MyEnum.VALUE_1, gen, new SerializerProvider());
        assertEquals(MyEnum.VALUE_1.toString(), gen.getCurrentValueAsString());

        serializer.serialize(MyEnum.VALUE_2, gen, new SerializerProvider());
        assertEquals(MyEnum.VALUE_2.toString(), gen.getCurrentValueAsString());
    }

    /**
     * Test schema generation.
     */
    @Test
    public void testGetSchema() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructForClass(MY_ENUM_CLASS), null);

        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonNode schema = serializer.getSchema(serializers, MY_ENUM_CLASS);

        JsonNode schemaType = schema.path("type");
        assertEquals("string", schemaType.asText());

        JsonNode schemaProperties = schema.path("properties");
        assertNotNull(schemaProperties);
        assertEquals(2, schemaProperties.size());

        JsonNode valueSchema = schemaProperties.get(MyEnum.VALUE_1.name());
        assertEquals("string", valueSchema.path("type").asText());
        assertEquals("VALUE_1", valueSchema.path("description").asText());

        JsonNode valueSchema2 = schemaProperties.get(MyEnum.VALUE_2.name());
        assertEquals("string", valueSchema2.path("type").asText());
        assertEquals("VALUE_2", valueSchema2.path("description").asText());
    }

    /**
     * Test handling of custom serialization format.
     */
    @Test
    public void testCustomFormat() throws Exception {
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        format.serializableBindings().put(MyEnum.class, new EnumSerializer.SerializerByValue());

        EnumSerializer serializer = new EnumSerializer(EnumValues.constructForClass(MY_ENUM_CLASS), null, null, format);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        serializer.serialize(MyEnum.VALUE_1, gen, new SerializerProvider());
        assertEquals("VALUE_1_VALUE_1", gen.getCurrentValueAsString());

        serializer.serialize(MyEnum.VALUE_2, gen, new SerializerProvider());
        assertEquals("VALUE_2_VALUE_2", gen.getCurrentValueAsString());
    }

    /**
     * Test handling of custom serialization format when "WRITE_ENUMS_USING_TO_STRING" feature is enabled.
     */
    @Test
    public void testCustomFormatWithToStringFeature() throws Exception {
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        format.serializableBindings().put(MyEnum.class, new EnumSerializer.SerializerByValue());

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        EnumSerializer serializer = new EnumSerializer(EnumValues.constructForClass(MY_ENUM_CLASS), null, null, format);

        serializer.serialize(MyEnum.VALUE_1, gen, new SerializerProvider());
        assertEquals(MyEnum.VALUE_1.toString() + "_VALUE_1", gen.getCurrentValueAsString());

        serializer.serialize(MyEnum.VALUE_2, gen, new SerializerProvider());
        assertEquals(MyEnum.VALUE_2.toString() + "_VALUE_2", gen.getCurrentValueAsString());
    }

    /**
     * Test handling of contextual serialization.
     */
    @Test
    public void testContextualSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        format.serializableBindings().put(MyEnum.class, new EnumSerializer.SerializerByValue());

        MyClass myClass = new MyClass();
        myClass.setMyEnumField(MyEnum.VALUE_1);

        ObjectNode node = mapper.writeValueAsObject(myClass, JsonEncoding.UTF8);

        JsonNode nodeValue = node.path("myEnumField");
        assertEquals("VALUE_1_VALUE_1", nodeValue.asText());

        MyClass myClass2 = new MyClass();
        myClass2.setMyEnumField(MyEnum.VALUE_2);

        ObjectNode node2 = mapper.writeValueAsObject(myClass2, JsonEncoding.UTF8);

        JsonNode nodeValue2 = node2.path("myEnumField");
        assertEquals("VALUE_2_VALUE_2", nodeValue2.asText());

        MyClass myClass3 = new MyClass();
        myClass3.setMyEnumField(null);

        ObjectNode node3 = mapper.writeValueAsObject(myClass3, JsonEncoding.UTF8);

        JsonNode nodeValue3 = node3.path("myEnumField");
        assertNull(nodeValue3);
    }

    static class MyClass {
        private MyEnum myEnumField;

        public MyEnum getMyEnumField() {
            return myEnumField;
        }

        public void setMyEnumField(MyEnum myEnumField) {
            this.myEnumField = myEnumField;
        }
    }

    static class EnumSerializer_SerializerByValue extends StdSerializer<Enum<?>> {

        public EnumSerializer_SerializerByValue() {
            this(null);
        }

        public EnumSerializer_SerializerByValue(Class<Enum<?>> t) {
            this(null);
        }

        public EnumSerializer_SerializerByValue(Class<Enum<?>> t, boolean unquoted) {
            super(t, unquoted);
        }

        @Override
        public void serialize(Enum<?> value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            JsonGenerator.Feature feature = JsonGenerator.Feature.AUTO_CLOSE_TARGET;
            gen.configure(feature, true);
            gen.writeString(value.name() + "_" + value.toString());
        }
    }

    static enum MyEnum {
        VALUE_1, VALUE_2;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        public String getSerializableName() {
            return this.name();
        }

        @Override
        public String toString() {
            return this.name();
        }
    }
}