package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSet_NullValueToken_EmptyCollection() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();

        parser.setCurrentToken(JsonToken.VALUE_NULL);
        property.deserializeAndSet(parser, context, instance);

        // Ensure that the collection is empty after deserializing null token
        Object propertyValue = property.get(instance);
        assertTrue(propertyValue instanceof Collection);
        assertTrue(((Collection) propertyValue).isEmpty());
    }

    @Test
    public void testDeserializeAndSet_NullValueToken_ErrorMessageThrown() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();

        parser.setCurrentToken(JsonToken.VALUE_NULL);
        try {
            property.deserializeAndSet(parser, context, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Problem deserializing 'setterless' property ('null'): no way to handle typed deser with setterless yet",
                    e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_GetterReturnsNull_ErrorMessageThrown() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();

        parser.setCurrentToken(JsonToken.VALUE_STRING);
        try {
            property.deserializeAndSet(parser, context, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Problem deserializing 'setterless' property 'null': get method returned null",
                    e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();

        parser.setCurrentToken(JsonToken.VALUE_NUMBER_INT);
        property.deserializeSetAndReturn(parser, context, instance);

        // Assert that the deserialization and set method call is successful
        assertEquals(instance, property);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet_UnsupportedOperationExceptionThrown() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();

        property.set(instance, null);
    }

    @Test
    public void testSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();

        property.setAndReturn(instance, null);

        // Assert that the set and return method call is successful
        assertEquals(instance, property);
    }

    @Test
    public void testFixAccess_OverridePublicAccessModifiers() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        DeserializationConfig config = new DeserializationConfig();

        property.fixAccess(config);

        // Assert that the fixAccess method call is successful
        // (No exceptions thrown)
    }

    @Test
    public void testWithNullProvider_NewSetterlessPropertyReturned() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        NullValueProvider nva = new NullValueProvider();
        
        SettableBeanProperty newProperty = property.withNullProvider(nva);

        // Assert that the new property has the same values as the original property except for the null provider
        assertNotNull(newProperty);
        assertEquals(property.getName(), newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(nva, newProperty.getNullProvider());
    }

    @Test
    public void testWithNullProvider_SameSetterlessPropertyReturned() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        NullValueProvider nva = new NullValueProvider();

        SettableBeanProperty sameProperty = property.withNullProvider(nva);

        // Assert that the same property is returned if the null provider is the same
        assertSame(property, sameProperty);
    }

    @Test
    public void testWithname_NewSetterlessPropertyReturned() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        PropertyName newName = new PropertyName("newName");

        SettableBeanProperty newProperty = property.withName(newName);

        // Assert that the new property has the same values as the original property except for the name
        assertNotNull(newProperty);
        assertEquals(newName, newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(property.getNullProvider(), newProperty.getNullProvider());
    }

    @Test
    public void testWithname_SameSetterlessPropertyReturned() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        PropertyName sameName = new PropertyName(property.getName());

        SettableBeanProperty sameProperty = property.withName(sameName);

        // Assert that the same property is returned if the name is the same
        assertSame(property, sameProperty);
    }

    @Test
    public void testwithValueDeserializer_NewSetterlessPropertyReturned() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        SettableBeanProperty newProperty = property.withValueDeserializer(deser);

        // Assert that the new property has the same values as the original property except for the value deserializer
        assertNotNull(newProperty);
        assertEquals(property.getName(), newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(deser, newProperty.getValueDeserializer());
        assertEquals(property.getNullProvider(), newProperty.getNullProvider());
    }

    @Test
    public void testwithValueDeserializer_SameSetterlessPropertyReturned() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonDeserializer<?> sameDeser = new JsonDeserializer<>();

        SettableBeanProperty sameProperty = property.withValueDeserializer(sameDeser);

        // Assert that the same property is returned if the value deserializer is the same
        assertSame(property, sameProperty);
    }

}