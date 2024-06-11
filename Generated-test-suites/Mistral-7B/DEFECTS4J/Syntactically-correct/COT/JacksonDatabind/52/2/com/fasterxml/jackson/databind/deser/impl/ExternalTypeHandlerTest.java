package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ExternalTypeHandler.
 */
public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;

    @Before
    public void setUp() {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];

        SettableBeanProperty property1 = new SimplePojo().getClass().getDeclaredField("property1").getAnnotation(JsonProperty.class);
        TypeDeserializer typeDeserializer1 = new StubTypeDeserializer();
        ExtTypedProperty extTypedProperty1 = new ExtTypedProperty(property1, typeDeserializer1);
        properties[0] = extTypedProperty1;
        nameToPropertyIndex.put(property1.getName(), 0);
        nameToPropertyIndex.put(typeDeserializer1.getPropertyName(), 0);
        typeIds[0] = "typeId1";

        SettableBeanProperty property2 = new SimplePojo().getClass().getDeclaredField("property2").getAnnotation(JsonProperty.class);
        TypeDeserializer typeDeserializer2 = new StubTypeDeserializer();
        ExtTypedProperty extTypedProperty2 = new ExtTypedProperty(property2, typeDeserializer2);
        properties[1] = extTypedProperty2;
        nameToPropertyIndex.put(property2.getName(), 1);
        nameToPropertyIndex.put(typeDeserializer2.getPropertyName(), 1);
        typeIds[1] = "typeId2";

        handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testStart() throws IOException {
        ExternalTypeHandler newHandler = handler.start();
        assertNotNull(newHandler);
    }

    @Test
    public void testHandleTypePropertyValue_nullBean() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        assertFalse(handler.handleTypePropertyValue(parser, context, "property1", null));
    }

    @Test
    public void testHandleTypePropertyValue_noMatchingProperty() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        assertFalse(handler.handleTypePropertyValue(parser, context, "nonExistentProperty", new SimplePojo()));
    }

    @Test
    public void testHandleTypePropertyValue_correctTypeId() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        parser.nextTokenId(JsonToken.ID_START_OBJECT);
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextText("property1");
        parser.nextToken(JsonToken.VALUE_STRING);
        parser.nextText("typeId1");

        assertTrue(handler.handleTypePropertyValue(parser, context, "property1", new SimplePojo()));

        assertEquals("typeId1", handler._typeIds[0]);
    }

    @Test(expected = IOException.class)
    public void testHandleTypePropertyValue_missingTypeId() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        parser.nextTokenId(JsonToken.ID_START_OBJECT);
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextText("property1");
        parser.nextToken(JsonToken.VALUE_NULL);

        handler.handleTypePropertyValue(parser, context, "property1", new SimplePojo());

        fail("Expected IOException but no exception was thrown.");
    }

    @Test
    public void testHandlePropertyValue_nullBean() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        assertFalse(handler.handlePropertyValue(parser, context, "property1", null));
    }

    @Test
    public void testHandlePropertyValue_noTypeIdProperty() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        parser.nextTokenId(JsonToken.ID_START_OBJECT);
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextText("property1");
        parser.nextToken(JsonToken.VALUE_STRING);
        parser.nextText("value1");
        parser.nextTokenId(JsonToken.ID_END_OBJECT);

        parser.nextTokenId(JsonToken.ID_START_ARRAY);
        parser.nextToken(JsonToken.VALUE_STRING);
        parser.nextText("property2");
        parser.nextTokenId(JsonToken.ID_END_ARRAY);

        parser.nextTokenId(JsonToken.ID_START_OBJECT);
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextText("property2");
        parser.nextToken(JsonToken.VALUE_STRING);
        parser.nextText("value2");
        parser.nextTokenId(JsonToken.ID_END_OBJECT);

        assertFalse(handler.handlePropertyValue(parser, context, "property1", new SimplePojo()));
    }

    @Test
    public void testHandlePropertyValue_correctTypeId() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        parser.nextTokenId(JsonToken.ID_START_OBJECT);
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextText("property1");
        parser.nextToken(JsonToken.VALUE_NULL);

        parser.nextTokenId(JsonToken.ID_START_ARRAY);
        parser.nextToken(JsonToken.VALUE_STRING);
        parser.nextText("property2");
        parser.nextTokenId(JsonToken.ID_END_ARRAY);

        parser.nextTokenId(JsonToken.ID_START_OBJECT);
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextText("property2");
        parser.nextToken(JsonToken.VALUE_STRING);
        parser.nextText("typeId1");
        parser.nextTokenId(JsonToken.ID_END_OBJECT);

        assertTrue(handler.handlePropertyValue(parser, context, "property1", new SimplePojo()));

        assertEquals("typeId1", handler._typeIds[1]);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValue_missingTypeId() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        parser.nextTokenId(JsonToken.ID_START_OBJECT);
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextText("property1");
        parser.nextToken(JsonToken.VALUE_NULL);

        parser.nextTokenId(JsonToken.ID_START_ARRAY);
        parser.nextToken(JsonToken.VALUE_STRING);
        parser.nextText("property2");
        parser.nextTokenId(JsonToken.ID_END_ARRAY);

        parser.nextTokenId(JsonToken.ID_START_OBJECT);
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextText("property2");
        parser.nextToken(JsonToken.VALUE_NULL);
        parser.nextTokenId(JsonToken.ID_END_OBJECT);

        handler.handlePropertyValue(parser, context, "property1", new SimplePojo());

        fail("Expected IOException but no exception was thrown.");
    }

    @Test
    public void testComplete_noMissingData() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        parser.nextTokenId(JsonToken.ID_START_OBJECT);

        handler.handleTypePropertyValue(parser, context, "property1", new SimplePojo());
        handler.handlePropertyValue(parser, context, "property2", new SimplePojo());

        Object result = handler.complete(parser, context, new SimplePojo());

        assertNotNull(result);
        assertEquals(new SimplePojo().getClass(), result.getClass());
    }

    @Test
    public void testComplete_missingTypeId() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        parser.nextTokenId(JsonToken.ID_START_OBJECT);

        handler.handleTypePropertyValue(parser, context, "property1", new SimplePojo());
        handler.handlePropertyValue(parser, context, "property2", new SimplePojo());

        handler._typeIds[1] = null;

        Object result = handler.complete(parser, context, new SimplePojo());

        assertNotNull(result);
        assertEquals(new SimplePojo().getClass(), result.getClass());
        assertTrue(handler._tokens[1] != null);
    }

    @Test
    public void testComplete_missingProperty() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        parser.nextTokenId(JsonToken.ID_START_OBJECT);

        handler.handleTypePropertyValue(parser, context, "property1", new SimplePojo());
        handler.handlePropertyValue(parser, context, "property2", new SimplePojo());

        handler._typeIds[0] = null;

        Object result = handler.complete(parser, context, new SimplePojo());

        assertNotNull(result);
        assertEquals(new SimplePojo().getClass(), result.getClass());
        assertTrue(handler._properties[0].getProperty().get(result) == null);
    }

    @Test(expected = IOException.class)
    public void testComplete_missingBoth() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        parser.nextTokenId(JsonToken.ID_START_OBJECT);

        handler.handleTypePropertyValue(parser, context, "property1", new SimplePojo());
        handler.handlePropertyValue(parser, context, "property2", new SimplePojo());

        handler._typeIds[0] = null;
        handler._tokens[1] = null;

        Object result = handler.complete(parser, context, new SimplePojo());

        fail("Expected IOException but no exception was thrown.");
    }

    @Test
    public void testComplete_creatorProperty() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new StubCreator();

        parser.nextTokenId(JsonToken.ID_START_OBJECT);

        handler.handleTypePropertyValue(parser, context, "property1", new SimplePojo());
        handler.handlePropertyValue(parser, context, "property2", new SimplePojo());

        handler.complete(parser, context, buffer, creator);

        Object result = creator.build(context, buffer);

        assertNotNull(result);
        assertEquals(new ComplexPojo().getClass(), result.getClass());
    }

    @Test
    public void testComplete_missingCreatorProperty() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new StubCreator();

        parser.nextTokenId(JsonToken.ID_START_OBJECT);

        handler.handleTypePropertyValue(parser, context, "property1", new SimplePojo());
        handler.handlePropertyValue(parser, context, "property2", new SimplePojo());

        handler._tokens[1] = null;

        handler.complete(parser, context, buffer, creator);

        Object result = creator.build(context, buffer);

        assertNotNull(result);
        assertEquals(new SimplePojo().getClass(), result.getClass());
    }

    @Test(expected = IOException.class)
    public void testComplete_missingBothCreatorProperties() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new StubCreator();

        parser.nextTokenId(JsonToken.ID_START_OBJECT);

        handler.handleTypePropertyValue(parser, context, "property1", new SimplePojo());
        handler.handlePropertyValue(parser, context, "property2", new SimplePojo());

        handler._typeIds[0] = null;
        handler._tokens[1] = null;

        handler.complete(parser, context, buffer, creator);

        fail("Expected IOException but no exception was thrown.");
    }

    private JsonParser createMockParser() {
        return CreateMock.parser();
    }

    private DeserializationContext createMockContext() {
        return CreateMock.context();
    }

    private static class StubTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "StubType";
        }

        @Override
        public String getPropertyName() {
            return "stubType";
        }
    }

    private static class StubCreator implements PropertyBasedCreator {
        @Override
        public Object createUsingGuid(DeserializationContext ctxt) throws IOException {
            return new ComplexPojo();
        }

        @Override
        public Object createUsingJsonProvider(DeserializationContext ctxt, JsonProvider provider) throws IOException {
            return new ComplexPojo();
        }
    }

    private static class SimplePojo {
        private String property1;

        public String getProperty1() {
            return property1;
        }

        public void setProperty1(String property1) {
            this.property1 = property1;
        }
    }

    private static class ComplexPojo {
        private String property1;
        private String property2;

        public String getProperty1() {
            return property1;
        }

        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        public String getProperty2() {
            return property2;
        }

        public void setProperty2(String property2) {
            this.property2 = property2;
        }
    }
}