package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Test class for ExternalTypeHandler
 */
public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser parser;
    private DeserializationContext ctxt;

    @Before
    public void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ctxt = mapper.getDeserializationContext();
        parser = mapper.getFactory().createParser("[]");
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class),
                        new StringTypeDeserializer(String.class))
                .addExternal(new SettableBeanProperty("prop2", Integer.class),
                        new IntegerTypeDeserializer(Integer.class))
                .build());
    }

    /**
     * Test for handleTypePropertyValue method
     */
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "prop1", null);
        String typeId = "type1";
        handler.handleTypePropertyValue(parser, ctxt, "prop1", null);
        assertEquals("type1", handler._typeIds[0]);

        handler.complete(parser, ctxt, null);
        assertEquals("type1", handler._properties[0].getProperty().getValue(handler.complete(parser, ctxt, new Object())));
    }

    /**
     * Test for handlePropertyValue method
     */
    @Test
    public void testHandlePropertyValue() throws IOException {
        handler.start();
        handler.handlePropertyValue(parser, ctxt, "prop1", null);
        handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
        assertEquals(null, handler._typeIds[0]);

        handler.complete(parser, ctxt, new Object());
        assertEquals(null, handler._properties[0].getProperty().getValue(handler.complete(parser, ctxt, new Object())));
    }

    /**
     * Test for complete method with provided type id
     */
    @Test
    public void testCompleteWithTypeId() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "prop1", null);
        handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
        handler.handlePropertyValue(parser, ctxt, "prop2", new Object());
        handler.handlePropertyValue(parser, ctxt, "prop1", new Object());

        handler.complete(parser, ctxt, new Object());
        assertEquals("type1", handler._properties[0].getProperty().getValue(handler.complete(parser, ctxt, new Object())));
        assertEquals(null, handler._properties[1].getProperty().getValue(handler.complete(parser, ctxt, new Object())));
    }

    /**
     * Test for complete method without provided type id
     */
    @Test
    public void testCompleteWithoutTypeId() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "prop1", null);
        handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
        handler.handlePropertyValue(parser, ctxt, "prop2", new Object());
        handler.handlePropertyValue(parser, ctxt, "prop1", new Object());

        handler.complete(parser, ctxt, null);
        assertEquals(null, handler._properties[0].getProperty().getValue(handler.complete(parser, ctxt, new Object())));
        assertEquals(null, handler._properties[1].getProperty().getValue(handler.complete(parser, ctxt, new Object())));
    }

    /**
     * Test for edge case when type id is null and property value is null
     */
    @Test(expected = IOException.class)
    public void testNullTypeIdAndNullValue() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "prop1", null);
        handler.handlePropertyValue(parser, ctxt, "prop1", null);
        handler.complete(parser, ctxt, new Object());
    }

    /**
     * Test for edge case when property value is null but type id is not
     */
    @Test(expected = IOException.class)
    public void testNullValueButNotNullTypeId() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "prop1", null);
        handler.handlePropertyValue(parser, ctxt, "prop1", null);
        handler.handleTypePropertyValue(parser, ctxt, "prop1", "type1");
        handler.complete(parser, ctxt, new Object());
    }

    /**
     * Test for exception handling - missing property for type id
     */
    @Test(expected = IOException.class)
    public void testMissingPropertyForTypeId() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "prop1", null);
        handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
        handler.handleTypePropertyValue(parser, ctxt, "missingProp", "type1");
        handler.complete(parser, ctxt, new Object());
    }

    /**
     * Test for exception handling - missing type id for property
     */
    @Test(expected = IOException.class)
    public void testMissingTypeIdForProperty() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "missingProp", null);
        handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
        handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
        handler.complete(parser, ctxt, new Object());
    }

    /**
     * Test for exception handling - missing both type id and property
     */
    @Test(expected = IOException.class)
    public void testMissingBothTypeIdAndProperty() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "missingProp", null);
        handler.handlePropertyValue(parser, ctxt, "missingProp", null);
        handler.complete(parser, ctxt, new Object());
    }

    /**
     * Test for exception handling - missing both type id and property, but type is known
     */
    @Test(expected = IOException.class)
    public void testMissingBothTypeIdAndPropertyWithKnownType() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "missingProp", null);
        handler.handlePropertyValue(parser, ctxt, "missingProp", null);
        handler.handleTypePropertyValue(parser, ctxt, "prop1", null);
        handler.handlePropertyValue(parser, ctxt, "prop1", null);
        handler.handleTypePropertyValue(parser, ctxt, "prop1", "knownType");
        handler.complete(parser, ctxt, null);
    }

    /**
     * Inner classes for TypeDeserializers to simulate their behavior for testing
     */
    private static class StringTypeDeserializer extends TypeDeserializer {
        public StringTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public String getPropertyName() {
            return "stringTypeDeserializer";
        }

        @Override
        public JsonTypeIdResolver getTypeIdResolver() {
            return new JsonStringTypeIdResolver();
        }
    }

    private static class IntegerTypeDeserializer extends TypeDeserializer {
        public IntegerTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }

        @Override
        public String getPropertyName() {
            return "integerTypeDeserializer";
        }

        @Override
        public JsonTypeIdResolver getTypeIdResolver() {
            return new JsonStringTypeIdResolver();
        }
    }

    /**
     * Custom JsonTypeIdResolver for testing
     */
    private static class JsonStringTypeIdResolver extends JsonTypeIdResolverBase {
        @Override
        public JsonTypeId idFromValueAndType(Serializable NS, Class<?> CT) {
            return new JsonTypeId(CT.getName(), String.class.getName());
        }
    }
}