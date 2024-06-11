package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

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

public class ExternalTypeHandlerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testExternalTypeHandler() throws IOException {
        // Given
        String[] typeIds = {"StringType", "NumberType"};
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();

        properties[0] = new ExtTypedProperty(new SettableBeanProperty(new Object(), "stringProperty"), new StringTypeDeserializer());
        properties[0].linkTypeProperty(new SettableBeanProperty(new Object(), "stringTypeProperty"));
        nameToPropertyIndex.put("stringProperty", 0);
        nameToPropertyIndex.put("stringTypeProperty", 0);

        properties[1] = new ExtTypedProperty(new SettableBeanProperty(new Object(), "numberProperty"), new NumberTypeDeserializer());
        properties[1].linkTypeProperty(new SettableBeanProperty(new Object(), "numberTypeProperty"));
        nameToPropertyIndex.put("numberProperty", 1);
        nameToPropertyIndex.put("numberTypeProperty", 1);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        // When handling type property
        JsonParser p = createMockJsonParser();
        DeserializationContext ctxt = createMockContext();
        Object object = new Object();
        String propName = "stringTypeProperty";
        String typeId = "StringType";

        handler.handleTypePropertyValue(p, ctxt, propName, object);

        assertEquals("StringType", handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        // When handling property value
        propName = "stringProperty";
        handler._typeIds[0] = null;

        handler.handlePropertyValue(p, ctxt, propName, object);

        JsonParser buffered = handler._tokens[0].asParser(p);
        String result = handler._properties[0].getProperty().deserialize(buffered, ctxt).getTextValue();
        assertEquals("", result);

        assertEquals("StringType", handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        // When handling multiple properties
        List<Object> list = new ArrayList<>();
        Object bean = list;

        handler.complete(p, ctxt, bean);

        assertEquals(2, handler._properties.length);
        assertEquals("StringType", handler._typeIds[0]);
        assertEquals("NumberType", handler._typeIds[1]);
        assertNull(handler._tokens[0]);
        assertNull(handler._tokens[1]);
    }

    // Helper methods

    private JsonParser createMockJsonParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext createMockContext() {
        return mock(DeserializationContext.class);
    }
}

class StringTypeDeserializer extends StdScalarTypeDeserializer<String> {

    public StringTypeDeserializer() {
        this(null);
    }

    public StringTypeDeserializer(Class<String> vc) {
        super(String.class);
    }

    @Override
    public String getPropertyName() {
        return "stringTypeProperty";
    }
}

class NumberTypeDeserializer extends StdScalarTypeDeserializer<Number> {

    public NumberTypeDeserializer() {
        this(null);
    }

    public NumberTypeDeserializer(Class<Number> vc) {
        super(Number.class);
    }

    @Override
    public String getPropertyName() {
        return "numberTypeProperty";
    }
}