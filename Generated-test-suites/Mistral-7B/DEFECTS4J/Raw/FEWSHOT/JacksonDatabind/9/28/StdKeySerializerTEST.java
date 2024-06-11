package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToType;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        Date date = new Date();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
        StdKeySerializer keySerializer = new StdKeySerializer();
        keySerializer.serialize(date, jgen, provider);
        jgen.flush();

        assertEquals("\"@JsonKey(\"value=\": \"" + date.toInstant().toString() + "\")\"", jgen.getOutputContent());
    }

    @Test
    public void testSerializeString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        String value = "key";
        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
        StdKeySerializer keySerializer = new StdKeySerializer();
        keySerializer.serialize(value, jgen, provider);
        jgen.writeEndObject();
        jgen.flush();

        assertEquals("\"key\"", jgen.getOutputContent());
    }

    @Test
    public void testSerializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
        StdKeySerializer keySerializer = new StdKeySerializer();
        keySerializer.serialize(null, jgen, mapper.getSerializerProvider());
        jgen.writeEndObject();
        jgen.flush();

        assertEquals("null", jgen.getOutputContent());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);

        JsonNode schema = keySerializer.getSchema(mapper.getSerializerProvider(), javaType);
        ObjectNode expectedSchema = mapper.createObjectNode();
        expectedSchema.put("type", "string");

        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);

        ObjectNode expectedJsonNode = mapper.createObjectNode();
        expectedJsonNode.put("type", "string");

        JsonFormatVisitorWrapper visitor = mapper.schemaBuilder().buildSchemaVisitor(javaType, null, null);
        keySerializer.acceptJsonFormatVisitor(visitor, javaType);

        assertEquals(expectedJsonNode, visitor.getSchema());
    }
}