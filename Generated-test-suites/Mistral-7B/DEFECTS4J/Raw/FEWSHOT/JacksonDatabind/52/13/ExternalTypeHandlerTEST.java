package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(),
                createTypeIds(), createTokens());
        handler.start();

        JsonParser p = createParser();
        DeserializationContext ctxt = createContext();

        Object bean = new Object();
        String propName = "typeProperty";
        String typeId = "TypeId1";

        // Test handling of existing property
        handler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertEquals(typeId, handler._typeIds[handler._properties.length - 1]);
        assertNull(handler._tokens[handler._properties.length - 1]);

        // Test handling of missing property
        propName = "missingProperty";
        handler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertNull(handler._typeIds[handler._properties.length - 1]);
        assertNull(handler._tokens[handler._properties.length - 1]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(),
                createTypeIds(), createTokens());
        handler.start();

        JsonParser p = createParser();
        DeserializationContext ctxt = createContext();

        Object bean = new Object();
        String propName = "property";
        String typeId = "TypeId1";

        // Test handling of existing property with type id
        handler.handlePropertyValue(p, ctxt, propName, bean);
        assertNull(handler._typeIds[handler._properties.length - 1]);
        assertNotNull(handler._tokens[handler._properties.length - 1]);

        // Test handling of existing property without type id
        handler._tokens[handler._properties.length - 1] = null;
        handler.handlePropertyValue(p, ctxt, propName, bean);
        assertEquals(typeId, handler._typeIds[handler._properties.length - 1]);
        assertNull(handler._tokens[handler._properties.length - 1]);

        // Test handling of missing property
        handler._typeIds[handler._properties.length - 1] = null;
        handler.handlePropertyValue(p, ctxt, propName, bean);
        assertNull(handler._typeIds[handler._properties.length - 1]);
        assertNull(handler._tokens[handler._properties.length - 1]);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(),
                createTypeIds(), createTokens());
        handler.start();

        JsonParser p = createParser();
        DeserializationContext ctxt = createContext();
        Object bean = new Object();

        handler.complete(p, ctxt, bean);

        for (int i = 0; i < handler._properties.length; i++) {
            String typeId = handler._typeIds[i];
            if (typeId != null) {
                assertEquals(typeId, handler._properties[i].getTypePropertyName());
            } else {
                assertNull(handler._tokens[i]);
            }
        }
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(),
                createTypeIds(), createTokens());
        handler.start();

        JsonParser p = createParser();
        DeserializationContext ctxt = createContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer(null);
        PropertyBasedCreator creator = new MapPropertyBasedCreator();

        handler.complete(p, ctxt, buffer, creator);

        Object bean = creator.build(ctxt, buffer);

        for (int i = 0; i < handler._properties.length; i++) {
            if (handler._properties[i].getCreatorIndex() >= 0) {
                assertNotNull(bean);
                Object value = handler._properties[i].getProperty().get(bean);
                assertEquals(i, handler._properties[i].getProperty().getCreatorIndex());
            } else {
                SettableBeanProperty prop = handler._properties[i].getProperty();
                assertEquals(prop.getName(), handler._properties[i].getTypePropertyName());
                assertEquals(value, prop.get(bean));
            }
        }
    }

    // Helper methods

    private ExternalTypeHandler.Builder createBuilder() {
        return new ExternalTypeHandler.Builder();
    }

    private ExtTypedProperty[] createProperties() {
        ExternalTypeHandler.Builder builder = createBuilder();
        builder.addExternal(new SimplePojo().getClass().getDeclaredField("typeProperty"), new SimpleTypeDeserializer());
        builder.addExternal(new SimplePojo().getClass().getDeclaredField("property"), new SimplePropertyTypeDeserializer());
        return builder.build();
    }

    private HashMap<String, Integer> createNameToPropertyIndex() {
        return new HashMap<>();
    }

    private String[] createTypeIds() {
        return new String[0];
    }

    private TokenBuffer[] createTokens() {
        return new TokenBuffer[0];
    }

    private ExtTypedProperty[] createTokensAsArray(TokenBuffer[] tokens) {
        ExternalTypeHandler.Builder builder = createBuilder();
        for (int i = 0; i < tokens.length; i++) {
            builder.addExternal(new SimplePojo().getClass().getDeclaredField("property" + i), new SimplePropertyTypeDeserializer());
        }
        return builder.build();
    }

    private ExternalTypeHandler newHandler() {
        return new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), createTypeIds(), createTokens());
    }

    private JsonParser createParser() throws IOException {
        String json = "{\"typeProperty\": \"TypeId1\", \"property0\": \"Value0\", \"property1\": \"Value1\"}";
        ObjectCodec oc = JsonFactory.getDefaultInstance().getObjectMapper().getFactory();
        return oc.createParser(new ByteArrayInputStream(json.getBytes()));
    }

    private DeserializationContext createContext() {
        ObjectCodec oc = JsonFactory.getDefaultInstance().getObjectMapper().getFactory();
        return oc.getDeserializationContext();
    }

    private static class SimplePojo {
        private String typeProperty;
        private Object property0;
        private Object property1;
        // Add more properties as needed
    }

    private static class SimpleTypeDeserializer extends StdTypeDeserializer {
        public SimpleTypeDeserializer() {
            this(null);
        }

        public SimpleTypeDeserializer(BeanDescription beanDesc) {
            super(SimplePojo.class, beanDesc);
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vtd)
                throws IOException {
            String typeId = p.getText();
            p.skipChildren();
            return vtd.deserializeTypedFromObject(ctxt.findValueValidator(getType(), typeId), ctxt);
        }

        @Override
        public String getPropertyName() {
            return "typeProperty";
        }
    }

    private static class SimplePropertyTypeDeserializer extends StdDeserializer<Object> {
        public SimplePropertyTypeDeserializer() {
            this(null);
        }

        public SimplePropertyTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String typeId = p.getText();
            p.skipChildren();
            TypeDeserializer vtd = findContentDeserializer(ctxt, String.class);
            return vtd.deserializeTypedFromObject(ctxt.findValueValidator(getType(), typeId), ctxt);
        }

        @Override
        public String getPropertyName() {
            return "property";
        }
    }
}