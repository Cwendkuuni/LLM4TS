package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test class for ExternalTypeHandler
 */
public class ExternalTypeHandlerTest {

    @SuppressWarnings("unchecked")
    private static ExternalTypeHandler createInstance(Object bean, TypeDeserializer[] typeDeserializers) {
        ExtTypedProperty[] properties = new ExtTypedProperty[typeDeserializers.length];
        for (int i = 0; i < properties.length; i++) {
            properties[i] = new ExtTypedProperty(new StdScalarValueProperty<Object>(String.format("property%d", i)), typeDeserializers[i]);
        }
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        for (int i = 0; i < properties.length; i++) {
            nameToPropertyIndex.put(properties[i].getProperty().getName(), i);
            nameToPropertyIndex.put(typeDeserializers[i].getPropertyName(), i);
        }
        return new ExternalTypeHandler(properties, nameToPropertyIndex, null, null).start();
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        Object bean = new Object[0];
        ExternalTypeHandler handler = createInstance(bean, new TypeDeserializer[0]);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeProperty\":\"typeId\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        for (int i = 0; i < 5; i++) {
            String propName = String.format("typeProperty%d", i);
            String typeId = String.format("typeId%d", i);
            handler.handleTypePropertyValue(jp, ctxt, propName, bean);
            assertEquals(typeId, jp.getText());
            jp.nextToken();
        }
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        Object bean = new Object[0];
        ExternalTypeHandler handler = createInstance(bean, new TypeDeserializer[0]);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"property\":[\"typeId1\",\"typeId2\",\"typeId3\"]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        for (int i = 0; i < 3; i++) {
            String propName = "property";
            String typeId = String.format("typeId%d", i + 1);
            handler.handlePropertyValue(jp, ctxt, propName, bean);
            assertEquals(typeId, handler.getTypeIds()[i]);
            assertEquals(JsonToken.START_ARRAY, jp.getCurrentToken().getId());
        }
        jp.nextToken(); // move to next token after array
        assertNull(handler.getTypeIds()[3]);
        assertEquals(JsonToken.END_ARRAY, jp.getCurrentToken().getId());
    }

    @Test
    public void testComplete() throws IOException {
        Object bean = new Object[0];
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeProperty1\":\"typeId1\",\"typeProperty2\":\"typeId2\",\"property\":[\"typeId3\",\"typeId4\"]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ExternalTypeHandler handler = createInstance(bean, new TypeDeserializer[]{
                new SimpleTypeDeserializer(String.class),
                new SimpleTypeDeserializer(Integer.class),
                new SimpleTypeDeserializer(Double.class),
                new SimpleTypeDeserializer(List.class),
                new SimpleTypeDeserializer(Map.class)
        });
        handler.handleTypePropertyValue(jp, ctxt, "typeProperty1", bean);
        handler.handleTypePropertyValue(jp, ctxt, "typeProperty2", bean);
        handler.handlePropertyValue(jp, ctxt, "property", bean);
        Object result = handler.complete(jp, ctxt, bean);
        assertEquals(bean.getClass(), result.getClass());
        assertEquals("typeId1", handler.getTypeIds()[0]);
        assertEquals("typeId2", handler.getTypeIds()[1]);
        assertEquals("typeId3", handler.getTypeIds()[2]);
        assertEquals("typeId4", handler.getTypeIds()[3]);
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingProperty() throws IOException {
        Object bean = new Object[0];
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeProperty\":\"typeId1\",\"property\":[\"typeId2\",\"typeId3\"]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ExternalTypeHandler handler = createInstance(bean, new TypeDeserializer[]{
                new SimpleTypeDeserializer(String.class),
                new SimpleTypeDeserializer(Integer.class),
                new SimpleTypeDeserializer(Double.class)
        });
        handler.handleTypePropertyValue(jp, ctxt, "typeProperty", bean);
        handler.handlePropertyValue(jp, ctxt, "property", bean);
        handler.complete(jp, ctxt, bean);
        fail("Exception expected");
    }

    @Test(expected = JsonMappingException.class)
    public void testCompleteMissingTypeId() throws IOException {
        Object bean = new Object[0];
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeProperty1\":\"\",\"typeProperty2\":\"typeId2\",\"property\":[\"typeId3\",\"typeId4\"]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ExternalTypeHandler handler = createInstance(bean, new TypeDeserializer[]{
                new SimpleTypeDeserializer(String.class),
                new SimpleTypeDeserializer(Integer.class),
                new SimpleTypeDeserializer(Double.class),
                new SimpleTypeDeserializer(List.class),
                new SimpleTypeDeserializer(Map.class)
        });
        handler.handleTypePropertyValue(jp, ctxt, "typeProperty1", bean);
        handler.handleTypePropertyValue(jp, ctxt, "typeProperty2", bean);
        handler.handlePropertyValue(jp, ctxt, "property", bean);
        handler.complete(jp, ctxt, bean);
        fail("Exception expected");
    }

    @Test(expected = JsonMappingException.class)
    public void testCompleteMissingExternalTypeIdProperty() throws IOException {
        Object bean = new Object[0];
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"property\":\"value\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ExternalTypeHandler handler = createInstance(bean, new TypeDeserializer[]{
                new SimpleTypeDeserializer(String.class),
                new SimpleTypeDeserializer(Integer.class),
                new SimpleTypeDeserializer(Double.class)
        });
        handler.handlePropertyValue(jp, ctxt, "property", bean);
        handler.complete(jp, ctxt, bean);
        fail("Exception expected");
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        Object bean = new Object[0];
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"creatorProperty1\":\"creatorValue1\",\"property\":[\"typeId3\",\"typeId4\"]}");
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                Object result = new Object[2];
                buffer.assignParameter(new StdScalarValueProperty<Object>("result1"), result[0]);
                buffer.assignParameter(new StdScalarValueProperty<Object>("result2"), result[1]);
                return result;
            }
        };
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ExternalTypeHandler handler = createInstance(bean, new TypeDeserializer[]{
                new SimpleTypeDeserializer(String.class),
                new SimpleTypeDeserializer(Integer.class),
                new SimpleTypeDeserializer(Double.class)
        });
        handler.handleTypePropertyValue(jp, ctxt, "creatorProperty1", bean);
        handler.handlePropertyValue(jp, ctxt, "property", bean);
        Object result = handler.complete(jp, ctxt, null, creator);
        assertEquals(2, result.length);
        assertEquals("typeId3", handler.getTypeIds()[2]);
        assertEquals("typeId4", handler.getTypeIds()[3]);
    }
}

class PropertyBasedCreator {
    // Empty interface
}

class StdScalarValueProperty<T> {
    private final String name;
    private final Class<T> clazz;

    public StdScalarValueProperty(String name, Class<T> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return ctxt.readValueAs(clazz);
    }

    public void set(Object o, T value) {
        if (o instanceof Map) {
            ((Map) o).put(name, value);
        } else {
            throw new RuntimeException("Cannot set property on non-map object");
        }
    }
}

class SimpleTypeDeserializer extends TypeDeserializer {
    private final Class<?> type;

    public SimpleTypeDeserializer(Class<?> type) {
        super(null, null);
        this.type = type;
    }

    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
            throws IOException {
        return ctxt.readValueAs(type);
    }

    @Override
    public Object deserializeValue(JsonParser p, DeserializationContext ctxt) throws IOException {
        return deserializeWithType(p, ctxt, null);
    }

    @Override
    public String getPropertyName() {
        return "typeProperty";
    }
}