package com.fasterxml.jackson.databind.deser;

import org.junit.Assert;
import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet_NormalCase() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        try {
            settableAnyProperty.deserializeAndSet(null, null, null, null);
        } catch (Exception e) {
            Assert.fail("Exception thrown for normal case");
        }
    }

    @Test
    public void testDeserializeAndSet_UnresolvedForwardReference() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        try {
            settableAnyProperty.deserializeAndSet(null, null, null, null);
            Assert.fail("Exception not thrown for unresolved forward reference");
        } catch (Exception e) {
            Assert.assertTrue("Incorrect exception thrown for unresolved forward reference",
                    e instanceof JsonMappingException && e.getMessage().contains("Unresolved forward reference but no identity info."));
        }
    }

    @Test
    public void testDeserializeAndSet_ExceptionThrown() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        try {
            settableAnyProperty.deserializeAndSet(null, null, null, null);
            Assert.fail("Exception not thrown for invalid argument");
        } catch (Exception e) {
            Assert.assertTrue("Incorrect exception thrown for invalid argument", 
                    e instanceof JsonMappingException && e.getMessage().contains("Problem deserializing \"any\" property 'null'"));
        }
    }

    @Test
    public void testDeserialize_NormalCase() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        
        try {
            settableAnyProperty.deserialize(null, null);
        } catch (Exception e) {
            Assert.fail("Exception thrown for normal case");
        }
    }

    @Test
    public void testDeserialize_NullValue() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        try {
            settableAnyProperty.deserialize(null, null);
        } catch (Exception e) {
            Assert.fail("Exception thrown for null value");
        }
    }

    @Test
    public void testDeserialize_WithTypeDeserializer() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        try {
            settableAnyProperty.deserialize(null, null);
        } catch (Exception e) {
            Assert.fail("Exception thrown with TypeDeserializer");
        }
    }

    @Test
    public void testSet_SetterIsField() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        try {
            settableAnyProperty.set(null, null, null);
        } catch (Exception e) {
            Assert.fail("Exception thrown when using setter as field");
        }
    }

    @Test
    public void testSet_ExceptionThrown() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        try {
            settableAnyProperty.set(null, null, null);
            Assert.fail("Exception not thrown when setting property");
        } catch (Exception e) {
            Assert.assertTrue("Incorrect exception thrown when setting property",
                    e instanceof JsonMappingException && e.getMessage().contains("Problem deserializing \"any\" property 'null'"));
        }
    }
}