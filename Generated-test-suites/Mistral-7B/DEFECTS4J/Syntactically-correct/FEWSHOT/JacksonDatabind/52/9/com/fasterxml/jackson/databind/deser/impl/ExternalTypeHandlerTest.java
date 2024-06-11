package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testExternalTypeHandler() {
        ExternalTypeHandler handler = new ExternalTypeHandler(setupProperties(), setupNameToPropertyIndex(), setupTypeIds(), setupTokens());
        assertNotNull(handler);

        // test start method
        ExternalTypeHandler newHandler = handler.start();
        assertNotNull(newHandler);
        assertEquals(handler, newHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = setupHandlerWithMockedParser();
        DeserializationContext ctxt = mockContext();
        Object bean = new Object();
        String propName = "propName";

        // test with valid type property name
        handler.handleTypePropertyValue(mockParser(), ctxt, propName, bean);
        verify(parserMock).getText();
        assertEquals("typeId", handler.getTypeIds()[0]);

        // test with invalid property name
        handler = setupHandlerWithMockedParser();
        when(parserMock.getText()).thenReturn("invalidPropName");
        handler.handleTypePropertyValue(mockParser(), ctxt, propName, bean);
        verify(parserMock).nextToken(); // should skip the current token
        assertFalse(handler.getTypeIds()[0] != null);

        // test with null bean
        handler = setupHandlerWithMockedParser();
        handler.handleTypePropertyValue(mockParser(), ctxt, propName, null);
        verify(parserMock).nextToken(); // should skip the current token
        assertNull(handler.getTypeIds());
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = setupHandlerWithMockedParser();
        DeserializationContext ctxt = mockContext();
        Object bean = new Object();
        String propName = "propName";

        // test with valid property and type property name
        handler.handlePropertyValue(mockParser(), ctxt, propName, bean);
        verify(parserMock).getText();
        assertEquals("typeId", handler.getTypeIds()[0]);

        // test with invalid property name
        handler = setupHandlerWithMockedParser();
        when(parserMock.getText()).thenReturn("invalidPropName");
        handler.handlePropertyValue(mockParser(), ctxt, propName, bean);
        verify(parserMock).skipChildren();
        assertNull(handler.getTypeIds());

        // test with null bean
        handler = setupHandlerWithMockedParser();
        handler.handlePropertyValue(mockParser(), ctxt, propName, null);
        verify(parserMock).skipChildren();
        assertNull(handler.getTypeIds());
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = setupHandlerWithMockedParserAndValues();
        JsonParser p = mockParser();
        DeserializationContext ctxt = mockContext();
        Object bean = new Object();

        handler.complete(p, ctxt, bean);

        for (int i = 0; i < handler.getTypeIds().length; i++) {
            if (handler.getTypeIds()[i] != null) {
                assertEquals("expectedTypeId", handler.getTypeIds()[i], handler.getProperties()[i].getTypePropertyName());
                SettableBeanProperty prop = handler.getProperties()[i].getProperty();
                assertNotNull(prop.get(bean));
            }
        }
    }

    @Test
    public void testCompleteWithPropertyValueBufferAndPropertyBasedCreator() throws IOException {
        ExternalTypeHandler handler = setupHandlerWithMockedParserAndValues();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = mockCreator();

        handler.complete(mockParser(), mockContext(), buffer, creator);

        for (int i = 0; i < handler.getTypeIds().length; i++) {
            if (handler.getTypeIds()[i] != null) {
                assertEquals("expectedTypeId", handler.getTypeIds()[i], handler.getProperties()[i].getTypePropertyName());
                SettableBeanProperty prop = handler.getProperties()[i].getProperty();
                assertEquals("expectedValue", prop.get(buffer.getBuffer()), prop.get(handler.getCompleteObject(mockParser(), mockContext(), buffer, creator)));
            }
        }
    }

    private ExternalTypeHandler setupHandlerWithMockedParser() {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        return new ExternalTypeHandler(setupProperties(), setupNameToPropertyIndex(), null, null) {
            @Override
            JsonParser asParser(JsonParser p) {
                return parser;
            }
        };
    }

    private ExternalTypeHandler setupHandlerWithMockedParserAndValues() {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{" +
                "\"prop1\":\"value1\"," +
                "\"prop2\":{\"typeId\":\"expectedTypeId\"," +
                "\"value\":\"expectedValue\"" +
                "}}");
        return new ExternalTypeHandler(setupProperties(), setupNameToPropertyIndex(), setupTypeIds(), setupTokens()) {
            @Override
            JsonParser asParser(JsonParser p) {
                return parser;
            }
        };
    }

    private ExternalTypeHandler.Builder setupBuilder() {
        return new ExternalTypeHandler.Builder();
    }

    private ExtTypedProperty[] setupProperties() {
        ExternalTypeHandler.Builder builder = setupBuilder();
        SettableBeanProperty property1 = mockProperty("prop1");
        SettableBeanProperty property2 = mockProperty("prop2");
        TypeDeserializer typeDeserializer = mockTypeDeserializer("expectedTypeId");
        builder.addExternal(property1, typeDeserializer);
        builder.addExternal(property2, typeDeserializer);
        return builder.build();
    }

    private HashMap<String, Integer> setupNameToPropertyIndex() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("prop1", 0);
        map.put("prop2", 1);
        map.put("expectedTypeId", 1);
        return map;
    }

    private String[] setupTypeIds() {
        return new String[]{"expectedTypeId1", "expectedTypeId2", "expectedTypeId"};
    }

    private TokenBuffer[] setupTokens() {
        TokenBuffer[] tokens = new TokenBuffer[3];
        tokens[0] = new TokenBuffer();
        tokens[1] = new TokenBuffer();
        tokens[2] = new TokenBuffer();
        return tokens;
    }

    private DeserializationContext mockContext() {
        return mock(DeserializationContext.class);
    }

    private JsonParser mockParser() {
        return mock(JsonParser.class);
    }

    private PropertyBasedCreator mockCreator() {
        return mock(PropertyBasedCreator.class);
    }

    private SettableBeanProperty mockProperty(String name) {
        return mock(SettableBeanProperty.class);
    }

    private TypeDeserializer mockTypeDeserializer(String typeId) {
        return mock(TypeDeserializer.class);
    }

    private Object mockCreatorObject(Object... args) {
        Object result = mock(Object.class);
        when(creatorMock.build(eq(mockContext()), eq(args[0]))).thenReturn(result);
        return result;
    }

    private PropertyValueBuffer mockPropertyValueBuffer() {
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        when(buffer.getBuffer()).thenReturn(new Object[0]);
        return buffer;
    }
}