package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("[]");
        StringCollectionDeserializer deserializer = mapper.findValue("Collection<String>", json);
        Assert.assertEquals(Collections.emptyList(), deserializer);
    }

    @Test
    public void testSingleElementCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("['element']");
        StringCollectionDeserializer deserializer = mapper.findValue("Collection<String>", json);
        List<String> list = new ArrayList<>(deserializer);
        Assert.assertEquals("element", list.get(0));
    }

    @Test
    public void testMultipleElementCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("[\"element1\", \"element2\"]");
        StringCollectionDeserializer deserializer = mapper.findValue("Collection<String>", json);
        List<String> list = new ArrayList<>(deserializer);
        Assert.assertEquals("element1", list.get(0));
        Assert.assertEquals("element2", list.get(1));
    }

    @Test
    public void testNullValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("[null]");
        StringCollectionDeserializer deserializer = mapper.findValue("Collection<String>", json);
        List<String> list = new ArrayList<>(deserializer);
        Assert.assertEquals(1, list.size());
        Assert.assertNull(list.get(0));
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidInput() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("['invalid']");
        StringCollectionDeserializer deserializer = mapper.findValue("Collection<String>", json);
    }

    @Test
    public void testCustomValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void configure(DeserializationContext ctxt) throws AnnotationIntrospector.InstanceNotFoundException {
                ctxt.findValueDeserializer(String.class, new StdSerializer<String>(String.class) {
                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return p.getValueAsString() + "_custom";
                    }
                });
            }
        });
        JsonNode json = mapper.readTree("[\"element\"]");
        StringCollectionDeserializer deserializer = mapper.findValue("Collection<String>", json);
        List<String> list = new ArrayList<>(deserializer);
        Assert.assertEquals("element_custom", list.get(0));
    }

    @Test
    public void testDelegatorDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("[{\"delegateDeserializer\":\"delegate\"}]");
        StringCollectionDeserializer deserializer = mapper.findValue("Collection<String>", json);
        Assert.assertNotNull(deserializer._delegateDeserializer);
    }

    @Test
    public void testValueInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("[null]");
        StringCollectionDeserializer deserializer = mapper.findValue("Collection<String>", json);
        List<String> list = new ArrayList<>(deserializer._valueInstantiator.createValueInstance(JsonType.STANDARD_TYPE));
        Assert.assertEquals(1, list.size());
        Assert.assertNull(list.get(0));
    }

    @Test
    public void testFormatFeature() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("[\"element\"]");
        StringCollectionDeserializer deserializer = mapper.findValue("Collection<String>", json);
        deserializer.findFormatFeature(null, null, String.class, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        Assert.assertFalse(deserializer.unwrapSingle);
        json = mapper.readTree("[\"element\"]", JsonFactory.feature(JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY));
        deserializer = mapper.findValue("Collection<String>", json);
        Assert.assertTrue(deserializer.unwrapSingle);
    }
}