package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private static Object createTestObject(String[] typeIds, TokenBuffer[] tokens) {
        ExtTypedProperty[] properties = new ExtTypedProperty[typeIds.length];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        for (int i = 0; i < properties.length; i++) {
            String propertyName = "prop" + i;
            TypeDeserializer typeDeserializer = new BaseTypeDeserializer();
            properties[i] = new ExtTypedProperty(new StdSerializer<>(String.class), typeDeserializer);
            nameToPropertyIndex.put(propertyName, i);
        }
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens).start();
        return handler;
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String[] typeIds = {"intType", "stringType"};
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = createTestObject(typeIds, tokens);

        JsonParser p = new JsonFactory().createParser("{\"intProp\":\"intType\", \"stringProp\":\"stringType\"}");
        DeserializationContext ctxt = new TestDeserializationContext();

        for (String propName : new String[]{"intProp", "stringProp"}) {
            Object bean = new Object[1][];
            boolean handled = handler.handleTypePropertyValue(p, ctxt, propName, bean);
            assertTrue(handled);
            assertEquals(propName, handler._nameToPropertyIndex.get(propName).intValue());
            assertEquals(propName, handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);
        }
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String[] typeIds = {"intType", "stringType"};
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = createTestObject(typeIds, tokens);

        JsonParser p = new JsonFactory().createParser("{\"intProp\":1, \"stringProp\":\"test\"}");
        DeserializationContext ctxt = new TestDeserializationContext();

        for (String propName : new String[]{"intProp", "stringProp"}) {
            Object bean = new Object[1][];
            boolean handled = handler.handlePropertyValue(p, ctxt, propName, bean);
            assertTrue(handled);
            assertEquals(propName, handler._nameToPropertyIndex.get(propName).intValue());
            assertEquals(propName, handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);
        }
    }

    @Test
    public void testComplete() throws IOException {
        String[] typeIds = {"intType", "stringType"};
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = createTestObject(typeIds, tokens);

        JsonParser p = new JsonFactory().createParser("{\"intProp\":1, \"stringProp\":\"test\"}");
        DeserializationContext ctxt = new TestDeserializationContext();

        Object bean = handler.complete(p, ctxt, null);
        assertNotNull(bean);

        for (int i = 0; i < handler._properties.length; i++) {
            SettableBeanProperty prop = handler._properties[i].getProperty();
            assertEquals(i, prop.getCreatorIndex());
            assertEquals(typeIds[i], handler._typeIds[i]);
            assertEquals("prop" + i, prop.getName());
        }
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        String[] typeIds = {"intType", "stringType"};
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = createTestObject(typeIds, tokens);

        JsonFactory factory = new JsonFactory();
        JsonParser p = factory.createParser("{\"intProp\":1, \"stringProp\":\"test\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object[] bean = new Object[1][];
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new MapPropertyBasedCreator(Map.class);

        handler.complete(p, ctxt, buffer, creator);

        assertEquals(Map.class, bean[0].getClass());
        Map map = (Map) bean[0];
        assertEquals("intType", map.get("intProp").getClass().getName());
        assertEquals("int", map.get("intProp").getClass().getComponentType().getName());
        assertEquals("stringType", map.get("stringProp").getClass().getName());
        assertEquals("java.lang.String", map.get("stringProp").getClass().getComponentType().getName());
    }

    @Test
    public void testCompleteWithNulls() throws IOException {
        String[] typeIds = {"intType", "stringType"};
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = createTestObject(typeIds, tokens);

        JsonParser p = new JsonFactory().createParser("{\"intProp\":null, \"stringProp\":null}");
        DeserializationContext ctxt = new TestDeserializationContext();

        Object bean = handler.complete(p, ctxt, null);
        assertNotNull(bean);

        for (int i = 0; i < handler._properties.length; i++) {
            SettableBeanProperty prop = handler._properties[i].getProperty();
            if (i == 0) {
                assertEquals(null, prop.get(bean));
            } else {
                assertEquals("", prop.get(bean));
            }
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingProperty() throws IOException {
        String[] typeIds = {"intType", "stringType"};
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = createTestObject(typeIds, tokens);

        JsonParser p = new JsonFactory().createParser("{\"intProp\":1}");
        DeserializationContext ctxt = new TestDeserializationContext();

        handler.complete(p, ctxt, null);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingTypeId() throws IOException {
        String[] typeIds = {"intType", null};
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = createTestObject(typeIds, tokens);

        JsonParser p = new JsonFactory().createParser("{\"intProp\":1, \"stringProp\":\"test\"}");
        DeserializationContext ctxt = new TestDeserializationContext();

        handler.complete(p, ctxt, null);
        fail("Expected JsonMappingException");
    }
}

class TestDeserializationContext extends DeserializationContextBase {
    @Override
    public JsonParser _parser() {
        return null;
    }

    @Override
    public Object instantiateValue(Class<?> valueType) throws InstantiationException, IllegalAccessException {
        return null;
    }
}

class BaseTypeDeserializer extends StdDeserializer<String> {
    @Override
    public String getPropertyName() {
        return "baseType";
    }
}

class MapPropertyBasedCreator extends PropertyBasedCreator<Map<String, Object>> {
    public MapPropertyBasedCreator(Class<Map<String, Object>> c) {
        super(c);
    }

    @Override
    public Map<String, Object> build(DeserializationContext ctxt, PropertyValueBuffer pvb)
            throws IOException {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<? extends String, ? extends Object> e : pvb.asMap().entrySet()) {
            map.put(e.getKey(), e.getValue());
        }
        return map;
    }
}

class PropertyValueBuffer {
    private final Map<String, Object> _values = new HashMap<>();

    public void assignParameter(SettableBeanProperty prop, Object value) {
        _values.put(prop.getName(), value);
    }

    public Map<String, Object> asMap() {
        return _values;
    }
}

class ExtTypedProperty {
    private final SettableBeanProperty _property;
    private final TypeDeserializer _typeDeserializer;
    private final String _typePropertyName;
    private SettableBeanProperty _typeProperty;

    public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeserializer) {
        _property = property;
        _typeDeserializer = typeDeserializer;
        _typePropertyName = typeDeserializer.getPropertyName();
    }

    public void linkTypeProperty(SettableBeanProperty p) {
        _typeProperty = p;
    }

    public boolean hasTypePropertyName(String n) {
        return n.equals(_typePropertyName);
    }

    public boolean hasDefaultType() {
        return _typeDeserializer.getDefaultImpl() != null;
    }

    public String getDefaultTypeId() {
        Class<?> defaultType = _typeDeserializer.getDefaultImpl();
        if (defaultType == null) {
            return null;
        }
        return _typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultType);
    }

    public String getTypePropertyName() {
        return _typePropertyName;
    }

    public SettableBeanProperty getProperty() {
        return _property;
    }

    public SettableBeanProperty getTypeProperty() {
        return _typeProperty;
    }
}