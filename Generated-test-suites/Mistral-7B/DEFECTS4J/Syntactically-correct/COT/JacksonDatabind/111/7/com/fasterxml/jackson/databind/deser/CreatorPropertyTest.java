package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.impl.CreatorPropertyImpl;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * Test cases for CreatorProperty class.
 */
public class CreatorPropertyTest extends TestBase {

    @SuppressWarnings("unchecked")
    @JsonCreator
    static class TestBean {
        private final CreatorProperty _name;
        private final CreatorProperty _integer;

        TestBean(String name, int integer) {
            this._name = new CreatorPropertyImpl.Builder(
                    PropertyName.construct("name"), ObjectType.construct(), null, null, null,
                    null, 0, null, null)
                    .setName("name")
                    .setValueDeserializer(StringDeserializers.instance)
                    .build();
            this._integer = new CreatorPropertyImpl.Builder(
                    PropertyName.construct("integer"), IntType.construct(), null, null, null,
                    null, 0, null, null)
                    .setName("integer")
                    .setValueDeserializer(NumberDeserializers.createIntDeserializer(null))
                    .build();
        }

        String getName() {
            return _name.get(null);
        }

        int getInteger() {
            return (int) _integer.get(null);
        }

        @JsonProperty("name")
        String name;

        @JsonProperty("integer")
        int integer;
    }

    @Test
    public void testCreatorProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"test\", \"integer\":42}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals("test", bean.getName());
        assertEquals(42, bean.getInteger());
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig config = mapper.getConfig();
        TestBean bean = new TestBean("test", 42);
        CreatorProperty nameProp = bean._name;

        // Test null value
        JsonFactory factory = mapper.getFactory();
        JsonParser p = factory.createParser("{}");
        assertNull(nameProp.findInjectableValue(config, null));

        // Test non-existing value
        Object injectableValue = new Object();
        config.injectableValues().addValue(new StandardInjectableValues.PropertyValue("injectValueId", injectableValue));
        assertNull(nameProp.findInjectableValue(config, bean));

        // Test existing value
        JsonNode injectNode = factory.createObjectNode();
        injectNode.put("injectValueId", "injectValue");
        p.setValueAsTree(injectNode);
        assertEquals("injectValue", nameProp.findInjectableValue(config, bean).getValueAsString());
    }

    @Test
    public void testInject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig config = mapper.getConfig();
        TestBean bean = new TestBean("test", 42);
        CreatorProperty nameProp = bean._name;

        Object injectValue = new Object();
        config.injectableValues().addValue(new StandardInjectableValues.PropertyValue("injectValueId", injectValue));

        // Test null value
        JsonFactory factory = mapper.getFactory();
        JsonParser p = factory.createParser("{}");
        assertNull(nameProp.findInjectableValue(config, bean));
        nameProp.inject(config, bean);
        assertNull(bean.getName());

        // Test existing value
        JsonNode injectNode = factory.createObjectNode();
        injectNode.put("injectValueId", "injectValue");
        p.setValueAsTree(injectNode);
        nameProp.inject(config, bean);
        assertEquals("injectValue", bean.getName());
    }

    @Test(expected = IOException.class)
    public void testMissingSetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig config = mapper.getConfig();

        // Test null injectable value id
        TestBean bean = new TestBean(null, 42);
        CreatorProperty property = bean._name;
        property.inject(config, bean);
        fail("Expected IOException");

        // Test missing setter
        TestBean bean2 = new TestBean("test", 42);
        CreatorProperty property2 = new CreatorPropertyImpl.Builder(
                PropertyName.construct("missingSetter"), ObjectType.construct(), null, null, null,
                null, 0, null, null)
                .setName("missingSetter")
                .build();
        property2.fixAccess(config);
        property2.inject(config, bean2);
        fail("Expected IOException");
    }
}