package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.StdSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class StdSerializerTest extends BaseMapTest {

    private StdSerializer<Object> objSerializer;
    private ObjectMapper mapper;
    private SerializerProvider provider;
    private FilterProvider filters;

    @BeforeMethod
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        provider = mapper.getSerializerProvider();
        filters = provider.getFilterProvider();
        objSerializer = new StdSerializer<Object>(Object.class);
    }

    @Test
    public void testDefaultSerialization() throws Exception {
        ObjectNode node = mapper.readValue(objectMapperTestData, ObjectNode.class);
        Object nodeValue = node.get(0).get("value");
        objSerializer.serialize(nodeValue, mapper.getFactory().createGenerator(new StringWriter()), provider);
        String serializedValue = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodeValue);
        ObjectMapper checkMapper = new ObjectMapper();
        Object checkValue = checkMapper.readValue(new StringReader(serializedValue), Object.class);
        assertEquals(nodeValue, checkValue);
    }

    @Test
    public void testSchemaGeneration() throws Exception {
        ObjectNode node = mapper.readValue(objectMapperTestData, ObjectNode.class);
        ObjectNode schemaNode = objSerializer.getSchema(provider, Object.class);
        assertEquals(schemaNode.toString(), node.toString());
    }

    @Test
    public void testSchemaGenerationWithOptional() throws Exception {
        ObjectNode node = mapper.readValue(objectMapperTestData, ObjectNode.class);
        ObjectNode schemaNode = objSerializer.getSchema(provider, Object.class, true);
        assertEquals(schemaNode.toString(), node.toString());
    }

    @Test
    public void testFilteredSerialization() throws Exception {
        PropertyFilter filter = findPropertyFilter(provider, "testFilter", "testValue");
        objSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                if (filter != null) {
                    filter.serializeProperties(this, jgen, value, provider);
                } else {
                    super.serialize(value, jgen, provider);
                }
            }
        };
        ObjectNode node = mapper.readValue(objectMapperTestData, ObjectNode.class);
        Object nodeValue = node.get(0).get("testValue");
        objSerializer.serialize(nodeValue, mapper.getFactory().createGenerator(new StringWriter()), provider);
        String serializedValue = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodeValue);
        ObjectMapper checkMapper = new ObjectMapper();
        Object checkValue = checkMapper.readValue(new StringReader(serializedValue), Object.class);
        assertNull(checkValue);
    }
}