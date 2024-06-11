package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testExternalTypeHandler() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDeserializerFactory deserializerFactory = mapper.getDeserializerFactory();

        // Test with a simple POJO
        class TestPojo {
            private String prop1;
            private String prop2;
            private Integer prop3;

            public String getProp1() {
                return prop1;
            }

            public void setProp1(String prop1) {
                this.prop1 = prop1;
            }

            public String getProp2() {
                return prop2;
            }

            public void setProp2(String prop2) {
                this.prop2 = prop2;
            }

            public Integer getProp3() {
                return prop3;
            }

            public void setProp3(Integer prop3) {
                this.prop3 = prop3;
            }
        }

        // Define a type deserializer for Integer type
        class IntTypeDeserializer extends StdTypeDeserializer<Integer> {
            public IntTypeDeserializer(BeanProperty property, TypeDeserializer valueDeserializer) {
                super(property, valueDeserializer);
            }

            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return ctxt.numberInputParser().nextIntValue();
            }

            @Override
            public TypeIdResolver _valueTypeSerializer() {
                return null;
            }

            @Override
            public PropertyName getPropertyName() {
                return new PropertyName("type");
            }
        }

        // Create a builder for ExternalTypeHandler
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();

        // Add properties to the builder
        SettableBeanProperty prop1 = new SettableBeanProperty("prop1", String.class);
        SettableBeanProperty prop2 = new SettableBeanProperty("prop2", String.class);
        SettableBeanProperty prop3 = new SettableBeanProperty("prop3", Integer.class);

        TypeDeserializer typeDeserializerInt = new IntTypeDeserializer(prop3, null);

        builder.addExternal(prop1, null);
        builder.addExternal(prop2, null);
        builder.addExternal(prop3, typeDeserializerInt);

        // Create ExternalTypeHandler instance using the builder
        ExternalTypeHandler handler = builder.build();

        // Test handleTypePropertyValue method
        JsonParser parser = mapper.getFactory().createParser("[{\"prop1\":\"test1\",\"type\":\"Integer\",\"prop2\":\"test2\"}]");
        DeserializationContext context = mapper.getDeserializationContext();
        TestPojo pojo = new TestPojo();

        handler.start();
        handler.handleTypePropertyValue(parser, context, "prop1", pojo);
        handler.handleTypePropertyValue(parser, context, "type", pojo);
        handler.handleTypePropertyValue(parser, context, "prop2", pojo);
        handler.handleTypePropertyValue(parser, context, "type", pojo); // Test handling multiple type properties
        handler.complete(parser, context, pojo);

        assertEquals("test1", pojo.getProp1());
        assertEquals(null, pojo.getProp3()); // Type property "type" was handled instead of "prop3"

        // Test handlePropertyValue method
        parser = mapper.getFactory().createParser("[{\"prop1\":\"test1\",\"prop2\":123}]");
        handler.start();
        handler.handlePropertyValue(parser, context, "prop1", pojo);
        handler.handlePropertyValue(parser, context, "prop2", pojo);
        handler.handlePropertyValue(parser, context, "prop2", pojo); // Test handling multiple values for the same property
        handler.complete(parser, context, pojo);

        assertEquals("test1", pojo.getProp1());
        assertEquals(123, pojo.getProp3().intValue());

        // Test complete method with a JSON object
        String jsonObject = "{\"prop1\":\"test1\",\"prop2\":\"test2\",\"prop3\":123}";
        parser = mapper.getFactory().createParser(jsonObject);
        handler.start();
        handler.complete(parser, context, pojo);

        assertEquals("test1", pojo.getProp1());
        assertEquals("test2", pojo.getProp2());
        assertEquals(123, pojo.getProp3().intValue());

        // Test complete method with a PropertyValueBuffer and PropertyBasedCreator
        class TestCreator {
            public TestPojo create(JsonParser p, DeserializationContext ctxt, PropertyValueBuffer buffer)
                    throws IOException {
                TestPojo pojo = new TestPojo();
                pojo.setProp1(buffer.nextTextValue());
                pojo.setProp2(buffer.nextTextValue());
                pojo.setProp3(buffer.nextValueAs(Integer.class));
                return pojo;
            }
        }

        TestCreator creator = new TestCreator();
        parser = mapper.getFactory().createParser(jsonObject);
        handler.start();
        handler.complete(parser, context, null, creator);

        TestPojo pojoCreated = handler.complete(parser, context, null, creator);
        assertEquals("test1", pojoCreated.getProp1());
        assertEquals("test2", pojoCreated.getProp2());
        assertEquals(123, pojoCreated.getProp3().intValue());
    }
}