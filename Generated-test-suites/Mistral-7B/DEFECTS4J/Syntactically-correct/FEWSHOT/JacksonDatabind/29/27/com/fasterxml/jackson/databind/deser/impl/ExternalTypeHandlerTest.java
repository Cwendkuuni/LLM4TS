package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Tests for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    // Add test cases for the constructor here

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        // Test case 1: Handle a valid external type id
        String jsonString1 = "{\"someProperty\":\"SomeType\"}";
        JsonParser jsonParser1 = new ObjectMapper().readTree(jsonString1);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        assertTrue(externalTypeHandler.handleTypePropertyValue(jsonParser1, new DeserializationContext(null), "someProperty", bean));

        // Test case 2: Handle an invalid external type id (property not found)
        String jsonString2 = "{\"nonexistentProperty\":\"SomeType\"}";
        JsonParser jsonParser2 = new ObjectMapper().readTree(jsonString2);
        assertFalse(externalTypeHandler.handleTypePropertyValue(jsonParser2, new DeserializationContext(null), "nonexistentProperty", bean));

        // Test case 3: Handle an invalid external type id (type property name not matched)
        String jsonString3 = "{\"someProperty\":\"SomeOtherType\"}";
        JsonParser jsonParser3 = new ObjectMapper().readTree(jsonString3);
        ExtTypedProperty someProperty = new ExtTypedProperty(new SimpleProperty("someProperty"), new SimpleTypeDeserializer(String.class));
        ExternalTypeHandler customExternalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(someProperty, new SimpleTypeDeserializer(String.class))
                .build());
        assertFalse(customExternalTypeHandler.handleTypePropertyValue(jsonParser3, new DeserializationContext(null), "someProperty", bean));

        // Test case 4: Handle an external type id with a missing property
        String jsonString4 = "{\"someProperty\":\"SomeType\"}";
        JsonParser jsonParser4 = new ObjectMapper().readTree(jsonString4);
        assertFalse(externalTypeHandler.handleTypePropertyValue(jsonParser4, new DeserializationContext(null), "anotherProperty", bean));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        // Test case 1: Handle a property value with an external type id
        String jsonString1 = "{\"someProperty\":\"Value\", \"typeProperty\":\"SomeType\"}";
        JsonParser jsonParser1 = new ObjectMapper().readTree(jsonString1);
        Object bean = new Object();

        assertTrue(externalTypeHandler.handlePropertyValue(jsonParser1, new DeserializationContext(null), "someProperty", bean));

        // Test case 2: Handle a property value without an external type id
        String jsonString2 = "{\"someProperty\":\"Value\"}";
        JsonParser jsonParser2 = new ObjectMapper().readTree(jsonString2);
        assertFalse(externalTypeHandler.handlePropertyValue(jsonParser2, new DeserializationContext(null), "someProperty", bean));

        // Test case 3: Handle a property value with an invalid external type id
        String jsonString3 = "{\"someProperty\":\"Value\", \"typeProperty\":\"SomeOtherType\"}";
        JsonParser jsonParser3 = new ObjectMapper().readTree(jsonString3);
        ExtTypedProperty someProperty = new ExtTypedProperty(new SimpleProperty("someProperty"), new SimpleTypeDeserializer(String.class));
        ExternalTypeHandler customExternalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(someProperty, new SimpleTypeDeserializer(String.class))
                .build());
        assertFalse(customExternalTypeHandler.handlePropertyValue(jsonParser3, new DeserializationContext(null), "someProperty", bean));

        // Test case 4: Handle a property value with a missing property
        String jsonString4 = "{\"someProperty\":\"Value\", \"nonexistentProperty\":\"SomeType\"}";
        JsonParser jsonParser4 = new ObjectMapper().readTree(jsonString4);
        assertFalse(externalTypeHandler.handlePropertyValue(jsonParser4, new DeserializationContext(null), "anotherProperty", bean));
    }

    @Test
    public void testComplete() throws IOException {
        // Test case 1: Complete deserialization with all properties having external type ids
        String jsonString1 = "{\"property1\":\"Value1\", \"property2\":\"Value2\", \"typeProperty1\":\"Type1\", \"typeProperty2\":\"Type2\"}";
        JsonParser jsonParser1 = new ObjectMapper().readTree(jsonString1);
        Object bean = new Object();

        Object result = externalTypeHandler.complete(jsonParser1, new DeserializationContext(null), bean);

        assertEquals(bean, result);

        // Test case 2: Complete deserialization with one missing external type id
        String jsonString2 = "{\"property1\":\"Value1\", \"property2\":\"Value2\", \"typeProperty1\":\"Type1\"}";
        JsonParser jsonParser2 = new ObjectMapper().readTree(jsonString2);
        SettableBeanProperty missingProperty = new SimpleProperty("typeProperty2");
        ExternalTypeHandler customExternalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new ExtTypedProperty(new SimpleProperty("property1"), new SimpleTypeDeserializer(String.class)), null)
                .addExternal(new ExtTypedProperty(new SimpleProperty("property2"), new SimpleTypeDeserializer(String.class)), null)
                .addExternal(new ExtTypedProperty(missingProperty, new SimpleTypeDeserializer(String.class)), null)
                .build());
        assertNotNull(customExternalTypeHandler.complete(jsonParser2, new DeserializationContext(null), bean));
        assertNull(bean.getClass().getField(missingProperty.getName()));

        // Test case 3: Complete deserialization with all properties having default types
        String jsonString3 = "{\"property1\":\"Value1\", \"property2\":\"Value2\"}";
        JsonParser jsonParser3 = new ObjectMapper().readTree(jsonString3);
        Object beanWithDefaultTypes = new Object();

        Object resultWithDefaultTypes = externalTypeHandler.complete(jsonParser3, new DeserializationContext(null), beanWithDefaultTypes);

        assertEquals(beanWithDefaultTypes, resultWithDefaultTypes);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        // Test case 1: Complete deserialization with property-based creator
        String jsonString1 = "{\"property1\":{\"someProperty\":\"Value1\", \"typeProperty\":\"Type1\"}, \"property2\":{\"someProperty\":\"Value2\", \"typeProperty\":\"Type2\"}}";
        JsonParser jsonParser1 = new ObjectMapper().readTree(jsonString1);
        PropertyBasedCreator creator = new PropertyBasedCreator();
        Object result = externalTypeHandler.complete(jsonParser1, new DeserializationContext(null), creator);

        assertEquals(creator.build(new DeserializationContext(null), new PropertyValueBuffer()), result);
    }
}