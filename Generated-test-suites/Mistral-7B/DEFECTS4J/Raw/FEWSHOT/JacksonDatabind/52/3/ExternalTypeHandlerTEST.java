package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Test class for ExternalTypeHandler
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        // Given: a valid ExternalTypeHandler instance
        ExternalTypeHandler handler = createTestHandler();

        // When: handleTypePropertyValue is called with valid arguments
        String propName = "typeProperty";
        Object bean = new Object();

        // Then: the method should return true and update the handler state accordingly
        String typeId = "TypeId1";
        handler.handleTypePropertyValue(new JsonParser(){}, new DeserializationContext(){}, propName, bean);
        assertEquals(typeId, handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);
        assertNull(handler._tokens[handler._nameToPropertyIndex.get(propName).intValue()]);

        // When: the method is called again with the same property name
        typeId = "TypeId2";
        handler.handleTypePropertyValue(new JsonParser(){}, new DeserializationContext(){}, propName, bean);
        assertEquals(typeId, handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);
        assertNull(handler._tokens[handler._nameToPropertyIndex.get(propName).intValue()]);

        // When: the method is called with an unknown property name
        String unknownPropName = "UnknownProperty";
        handler.handleTypePropertyValue(new JsonParser(){}, new DeserializationContext(){}, unknownPropName, bean);
        assertNull(handler._typeIds[handler._nameToPropertyIndex.getOrDefault(unknownPropName, -1)]);
        assertNull(handler._tokens[handler._nameToPropertyIndex.getOrDefault(unknownPropName, -1)]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        // Given: a valid ExternalTypeHandler instance
        ExternalTypeHandler handler = createTestHandler();

        // When: handlePropertyValue is called with valid arguments and the property does not have a type property name
        String propName = "property";
        Object bean = new Object();
        String value = "value";

        // Then: the method should return true and update the handler state accordingly
        handler.handlePropertyValue(new JsonParser(){}, new DeserializationContext(){}, propName, bean);
        assertNull(handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);
        assertNull(handler._tokens[handler._nameToPropertyIndex.get(propName).intValue()]);

        // When: handlePropertyValue is called again with the same property name and the same value
        handler.handlePropertyValue(new JsonParser(){}, new DeserializationContext(){}, propName, bean);
        assertNull(handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);
        assertNull(handler._tokens[handler._nameToPropertyIndex.get(propName).intValue()]);

        // When: handlePropertyValue is called with an unknown property name
        String unknownPropName = "UnknownProperty";
        handler.handlePropertyValue(new JsonParser(){}, new DeserializationContext(){}, unknownPropName, bean);
        assertNull(handler._typeIds[handler._nameToPropertyIndex.getOrDefault(unknownPropName, -1)]);
        assertNull(handler._tokens[handler._nameToPropertyIndex.getOrDefault(unknownPropName, -1)]);

        // When: handlePropertyValue is called with a property that has a type property name
        String typePropertyName = "typeProperty";
        SettableBeanProperty typeProperty = new SettableBeanProperty("typeProperty", String.class);
        handler.addExternal(typeProperty, new SimpleTypeDeserializer(String.class));

        // Then: the method should not update the handler state for the given property
        handler.handlePropertyValue(new JsonParser(){}, new DeserializationContext(){}, propName, bean);
        assertNull(handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);
        assertNull(handler._tokens[handler._nameToPropertyIndex.get(propName).intValue()]);

        // When: handlePropertyValue is called with the type property name
        handler.handlePropertyValue(new JsonParser(){}, new DeserializationContext(){}, typePropertyName, bean);
        assertEquals("TypeId1", handler._typeIds[handler._nameToPropertyIndex.get(typePropertyName).intValue()]);
        assertNull(handler._tokens[handler._nameToPropertyIndex.get(typePropertyName).intValue()]);
    }

    @Test
    public void testComplete() throws IOException {
        // Given: a valid ExternalTypeHandler instance
        ExternalTypeHandler handler = createTestHandler();

        // When: complete is called and all properties have been processed
        Object bean = new Object();
        handler.complete(new JsonParser(){}, new DeserializationContext(){}, bean);

        // Then: all properties should have been processed and their values set on the bean
        List<String> propertyValues = new ArrayList<>();
        propertyValues.add("PropertyValue1");
        propertyValues.add("PropertyValue2");
        Map<String, Integer> propertyIndexMap = new HashMap<>();
        propertyIndexMap.put("property1", 0);
        propertyIndexMap.put("property2", 1);
        for (int i = 0; i < handler._properties.size(); i++) {
            SettableBeanProperty property = handler._properties.get(i).getProperty();
            assertEquals(property.getName(), handler._nameToPropertyIndex.get(property.getName()));
            assertEquals(propertyValues.get(i), property.get(bean));
        }
    }

    private ExternalTypeHandler createTestHandler() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        List<SettableBeanProperty> properties = new ArrayList<>();
        properties.add(new SettableBeanProperty("property1", String.class));
        properties.add(new SettableBeanProperty("property2", String.class));
        SettableBeanProperty typeProperty = new SettableBeanProperty("typeProperty", String.class);
        handler.addExternal(typeProperty, new SimpleTypeDeserializer(String.class));
        handler._properties = properties.toArray(new ExtTypedProperty[0]);
        handler._nameToPropertyIndex = new HashMap<>();
        return handler;
    }
}