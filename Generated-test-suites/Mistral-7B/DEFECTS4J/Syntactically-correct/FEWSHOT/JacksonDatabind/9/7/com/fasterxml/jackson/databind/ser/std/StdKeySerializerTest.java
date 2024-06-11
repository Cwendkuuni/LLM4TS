package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.MINIMAL_SERIALIZER_PROVIDER;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();
        SerializerProvider provider = new StandardSerializerProvider();
        provider.setDefaultSerializer(Date.class, mapper.getSerializerProvider().findValueSerializer(Date.class));
        provider.setDefaultSerializer(StdKeySerializer.class, keySerializer);

        Date date = new Date();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        keySerializer.serialize(date, jgen, provider);
        jgen.close();
    }

    @Test
    public void testSerializeString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();
        SerializerProvider provider = MINIMAL_SERIALIZER_PROVIDER;
        provider.setDefaultSerializer(StdKeySerializer.class, keySerializer);

        String str = "foo";

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        keySerializer.serialize(str, jgen, provider);
        jgen.close();
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();
        JavaType typeHint = mapper.getTypeFactory().constructType(String.class);

        ObjectNode schemaNode = keySerializer.getSchema(null, typeHint);
        assertEquals("string", schemaNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mapper.treeAsTokens("{}");

        keySerializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonFormat.Value.STRING, visitor.getFormatValue());
    }
}