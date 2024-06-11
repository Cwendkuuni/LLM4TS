package com.example.junit.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        String propName = "testProperty";
        String typeId = "TestType";

        assertTrue(handler.handleTypePropertyValue(parser, context, propName, bean));
        assertEquals(typeId, handler.getTypeIds()[handler._nameToPropertyIndex.get(propName).intValue()]);
    }

    @Test
    public void testHandleTypePropertyValue_noMatchingTypeProperty() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        String propName = "testProperty";

        assertFalse(handler.handleTypePropertyValue(parser, context, propName, bean));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        String propName = "testProperty";

        assertTrue(handler.handlePropertyValue(parser, context, propName, bean));
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValue_typeIdNotFound() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        String propName = "testProperty";

        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip PROPERTY_NAME propName
        parser.nextToken(); // skip VALUE_STRING typeId
        parser.nextToken(); // skip START_ARRAY
        parser.nextToken(); // skip VALUE_STRING
        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip END_OBJECT

        handler.handlePropertyValue(parser, context, propName, bean);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValue_noProperty() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        String propName = "nonexistentProperty";

        handler.handlePropertyValue(parser, context, propName, bean);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        handler.complete(parser, context, bean);
    }

    @Test(expected = IOException.class)
    public void testComplete_missingTypeId() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        parser.nextToken(); // skip START_OBJECT

        handler.complete(parser, context, bean);
    }

    @Test(expected = IOException.class)
    public void testComplete_missingProperty() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip PROPERTY_NAME
        parser.nextToken(); // skip VALUE_STRING
        parser.nextToken(); // skip END_OBJECT

        handler.complete(parser, context, bean);
    }

    @Test
    public void testComplete_nullValue() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip PROPERTY_NAME
        parser.nextToken(); // skip VALUE_NULL
        parser.nextToken(); // skip END_OBJECT

        Object result = handler.complete(parser, context, bean);
        assertNull(result);
    }

    @Test
    public void testComplete_withoutTypeId() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip PROPERTY_NAME propName1
        parser.nextToken(); // skip VALUE_STRING typeId1
        parser.nextToken(); // skip START_ARRAY
        parser.nextToken(); // skip VALUE_STRING
        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip PROPERTY_NAME propName2
        parser.nextToken(); // skip VALUE_NULL
        parser.nextToken(); // skip END_OBJECT

        Object result = handler.complete(parser, context, bean);
        assertNotNull(result);
    }

    @Test
    public void testComplete_withTypeId() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        Object bean = createTestBean();

        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip PROPERTY_NAME propName1
        parser.nextToken(); // skip VALUE_STRING typeId1
        parser.nextToken(); // skip START_ARRAY
        parser.nextToken(); // skip VALUE_NULL
        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip PROPERTY_NAME propName2
        parser.nextToken(); // skip VALUE_STRING typeId2
        parser.nextToken(); // skip START_ARRAY
        parser.nextToken(); // skip VALUE_NULL
        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip END_OBJECT

        Object result = handler.complete(parser, context, bean);
        assertNotNull(result);
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        ExternalTypeHandler handler = createTestExternalTypeHandler();
        JsonParser parser = createTestJsonParser();
        DeserializationContext context = createTestContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = createTestPropertyBasedCreator();

        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip PROPERTY_NAME propName1
        parser.nextToken(); // skip VALUE_STRING typeId1
        parser.nextToken(); // skip START_ARRAY
        parser.nextToken(); // skip VALUE_NUMBER 1
        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip PROPERTY_NAME propName2
        parser.nextToken(); // skip VALUE_STRING typeId2
        parser.nextToken(); // skip START_ARRAY
        parser.nextToken(); // skip VALUE_NUMBER 2
        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip END_OBJECT

        Object result = handler.complete(parser, context, buffer, creator);
        assertNotNull(result);
    }

    private Object createTestBean() {
        return new Object();
    }

    private JsonParser createTestJsonParser() throws IOException {
        ObjectCodec objectMapper = new ObjectMapper();
        return objectMapper.getFactory().createParser("{}");
    }

    private DeserializationContext createTestContext() {
        ObjectCodec objectMapper = new ObjectMapper();
        return new TestDeserializationContext(objectMapper);
    }

    private ExternalTypeHandler createTestExternalTypeHandler() {
        ObjectCodec objectMapper = new ObjectMapper();
        Builder builder = new Builder();

        SettableBeanProperty prop1 = new PropertyName("prop1");
        TypeDeserializer typeDeser1 = new SimpleTypeDeserializer(String.class);
        builder.addExternal(prop1, typeDeser1);

        SettableBeanProperty prop2 = new PropertyName("prop2");
        TypeDeserializer typeDeser2 = new SimpleTypeDeserializer(Integer.class);
        builder.addExternal(prop2, typeDeser2);

        builder.linkTypeProperties();
        return builder.build();
    }

    private PropertyBasedCreator createTestPropertyBasedCreator() {
        return new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer v) throws IOException {
                return new Object();
            }
        };
    }

    private static class TestDeserializationContext extends DeserializationContextBase {
        public TestDeserializationContext(ObjectMapper mapper) {
            super(mapper, null, null, null, null);
        }

        @Override
        public void reportMappingException(String msg) throws IOException {
            fail(msg);
        }
    }

    private static class SimpleTypeDeserializer extends TypeDeserializer {
        private final Class<?> _valueClass;

        public SimpleTypeDeserializer(Class<?> valueClass) {
            _valueClass = valueClass;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsTree().getValueAsToken().getValueAsString();
        }

        @Override
        public TypeIdResolver getTypeIdResolver() {
            return new SimpleTypeIdResolver() {
                @Override
                public TypeId idFromValueAndType(Object value, Class<?> type) {
                    return new TypeId(value.getClass(), null);
                }
            };
        }

        @Override
        public String getPropertyName() {
            return "SimpleType";
        }

        @Override
        public Class<?> getValueClass() {
            return _valueClass;
        }
    }

    private static class PropertyBasedCreator {
        public Object build(DeserializationContext ctxt, PropertyValueBuffer v) throws IOException {
            return null;
        }
    }
}

class ExtTypedProperty {
    private final SettableBeanProperty _property;
    private final TypeDeserializer _typeDeserializer;
    private String _typePropertyName;
    private SettableBeanProperty _typeProperty;

    public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeser) {
        _property = property;
        _typeDeserializer = typeDeser;
        _typePropertyName = typeDeser.getPropertyName();
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

class Builder {
    private final List<ExtTypedProperty> _properties = new ArrayList<>();
    private final Map<String, Integer> _nameToPropertyIndex = new HashMap<>();

    public void addExternal(SettableBeanProperty property, TypeDeserializer typeDeser) {
        Integer index = _properties.size();
        _properties.add(new ExtTypedProperty(property, typeDeser));
        _nameToPropertyIndex.put(property.getName(), index);
        _nameToPropertyIndex.put(typeDeser.getPropertyName(), index);
    }

    public ExternalTypeHandler build() {
        return new ExternalTypeHandler(_properties.toArray(new ExtTypedProperty[_properties.size()]),
                _nameToPropertyIndex, null, null);
    }
}