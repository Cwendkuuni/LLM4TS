package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactoryBuilder;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Date;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactoryBuilder()
                .withSerializerInstance(StdKeySerializer.class)
                .build());
        JsonGenerator jgen = mapper.getFactory().createGenerator();
        Date date = new Date();

        mapper.writeValue(jgen, date);
        jgen.flush();

        JsonNode node = mapper.readTree(jgen.getOutputStream());
        assertNotNull(node);
        assertEquals("$date", node.get("@class").asText());
        assertEquals("2022-12-31T12:00:00.000Z", node.get("@value").asText());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactoryBuilder()
                .withSerializerInstance(StdKeySerializer.class)
                .build());

        String str = "foo";
        JsonGenerator jgen = mapper.getFactory().createGenerator();
        mapper.writeValue(jgen, str);
        jgen.flush();

        JsonNode node = mapper.readTree(jgen.getOutputStream());
        assertEquals("foo", node.asText());
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactoryBuilder()
                .withSerializerInstance(StdKeySerializer.class)
                .build());

        JsonGenerator jgen = mapper.getFactory().createGenerator();
        mapper.writeNull(jgen);
        jgen.flush();

        JsonNode node = mapper.readTree(jgen.getOutputStream());
        assertNull(node);
    }

    @Test
    public void testGetSchema() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactoryBuilder()
                .withSerializerInstance(StdKeySerializer.class)
                .build());

        JsonSchemaProvider schemaProvider = mapper.getFactory().getJsonSchemaProvider();
        JsonSchema schema = schemaProvider.schemaFor(StdKeySerializer.class);

        assertEquals("string", schema.getSchemaName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactoryBuilder()
                .withSerializerInstance(StdKeySerializer.class)
                .build());

        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getFactory()
                .createJsonFormatVisitor(null, null, null);
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance.constructType(String.class));

        assertEquals("string", visitor.getCurrentValueFormat(null).getShape().getDescription().getDescription());
    }
}