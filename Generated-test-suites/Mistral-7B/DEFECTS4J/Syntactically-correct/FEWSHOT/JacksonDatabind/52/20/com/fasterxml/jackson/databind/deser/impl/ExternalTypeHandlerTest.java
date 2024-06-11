package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Test cases for ExternalTypeHandler class.
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testExternalTypeHandler() throws IOException {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);

        // Test empty ExternalTypeHandler
        ExternalTypeHandler etHandler1 = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean1 = etHandler1.complete(jf.getParser(), mapper.getDeserializationContext(), null);
        assertNull(bean1);

        // Test ExternalTypeHandler with properties
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty prop1 = new SimplePojo().getClass().getDeclaredField("prop1").getAnnotation(JsonProperty.class);
        SettableBeanProperty prop2 = new SimplePojo().getClass().getDeclaredField("prop2").getAnnotation(JsonProperty.class);
        TypeDeserializer td1 = mapper.getTypeDeserializerForValue(new SimplePojo().getClass(), SimplePojo.class);
        TypeDeserializer td2 = mapper.getTypeDeserializerForValue(new AnotherSimplePojo().getClass(), AnotherSimplePojo.class);
        builder.addExternal(prop1, td1);
        builder.addExternal(prop2, td2);
        ExternalTypeHandler etHandler2 = builder.build();

        SimplePojo simplePojo = new SimplePojo();
        AnotherSimplePojo anotherSimplePojo = new AnotherSimplePojo();
        String json = "{\"prop1\":\"value1\",\"prop2\":\"AnotherSimplePojo\"}";
        JsonParser p = jf.getParser(json);
        Object bean2 = etHandler2.start().handlePropertyValue(p, mapper.getDeserializationContext(), "prop1", simplePojo);
        assertEquals("value1", simplePojo.getProp1());
        bean2 = etHandler2.handlePropertyValue(p, mapper.getDeserializationContext(), "prop2", anotherSimplePojo);
        assertEquals("AnotherSimplePojo", anotherSimplePojo.getClass().getSimpleName());

        // Test complete method
        Object bean3 = etHandler2.complete(p, mapper.getDeserializationContext(), null);
        SimplePojo simplePojoComplete = (SimplePojo) bean3;
        assertEquals("value1", simplePojoComplete.getProp1());
        AnotherSimplePojo anotherSimplePojoComplete = (AnotherSimplePojo) etHandler2.getProperties()[1].getProperty().get(bean3);
        assertEquals("AnotherValue", anotherSimplePojoComplete.getValue());

        // Test property-based creator
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                SimplePojo pojo = new SimplePojo();
                pojo.setProp1(buffer.get(0));
                pojo.setProp2(buffer.get(1));
                return pojo;
            }
        };
        Object bean4 = etHandler2.complete(p, mapper.getDeserializationContext(), null, creator);
        SimplePojo simplePojoCreator = (SimplePojo) bean4;
        assertEquals("value1", simplePojoCreator.getProp1());
        AnotherSimplePojo anotherSimplePojoCreator = (AnotherSimplePojo) etHandler2.getProperties()[1].getProperty().get(bean4);
        assertEquals("AnotherValue", anotherSimplePojoCreator.getValue());

        // Test completion with missing type id or property
        List<Map.Entry<String, Integer>> expectedErrors = new ArrayList<>();
        expectedErrors.add(Map.entry("Missing property 'prop1' for external type id 'SimplePojo'", 0));
        expectedErrors.add(Map.entry("Missing property 'prop2' for external type id 'AnotherSimplePojo'", 1));
        JsonParser missingTypeIdParser = jf.getParser("[{\"typeId\":\"SimplePojo\"}]");
        Object bean5 = etHandler2.complete(missingTypeIdParser, mapper.getDeserializationContext(), null);
        assertNull(bean5);
        Map<String, List<String>> mappingErrors = mapper.getDeserializationConfig().getDeserializationProblemHandler().getDeserializationProblems();
        List<String> errors = mappingErrors.get("com.fasterxml.jackson.databind.JsonMappingException");
        assertEquals(2, errors.size());
        for (String error : errors) {
            assertTrue(expectedErrors.stream().anyMatch(entry -> entry.getValue().equals(error.split(": ")[1])));
            expectedErrors.remove(entry);
        }
        assertFalse(expectedErrors.isEmpty());

        // Test completion with missing property
        JsonParser missingPropertyParser = jf.getParser("{\"typeId\":\"AnotherSimplePojo\",\"prop1\":\"value1\"}");
        Object bean6 = etHandler2.complete(missingPropertyParser, mapper.getDeserializationContext(), null);
        assertNull(bean6);
        mappingErrors = mapper.getDeserializationConfig().getDeserializationProblemHandler().getDeserializationProblems();
        errors = mappingErrors.get("com.fasterxml.jackson.databind.JsonMappingException");
        assertEquals(1, errors.size());
        assertEquals("Missing property 'prop2' for external type id 'AnotherSimplePojo'", errors.get(0));

        // Test completion with missing both type id and property
        JsonParser missingBothParser = jf.getParser("{\"prop1\":\"value1\"}");
        Object bean7 = etHandler2.complete(missingBothParser, mapper.getDeserializationContext(), null);
        assertNull(bean7);
        mappingErrors = mapper.getDeserializationConfig().getDeserializationProblemHandler().getDeserializationProblems();
        errors = mappingErrors.get("com.fasterxml.jackson.databind.JsonMappingException");
        assertEquals(1, errors.size());
        assertEquals("Missing external type id property 'AnotherSimplePojo.typeId'", errors.get(0));
    }
}

class SimplePojo {
    private String prop1;

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }
}

class AnotherSimplePojo {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

class PropertyBasedCreator {
    public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
        return null;
    }
}