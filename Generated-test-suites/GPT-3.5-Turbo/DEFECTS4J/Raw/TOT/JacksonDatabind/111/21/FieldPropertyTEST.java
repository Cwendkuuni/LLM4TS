package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {

    @Test
    public void deserializeAndSet_Test() {
        // Typical use case
        FieldProperty fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            new AnnotatedField()
        );
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("{\"field\":\"value\"}");
        DeserializationContext deserializationContext = new DeserializationContext(null, null);
        Object instance = new Object();
        try {
            fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        } catch (IOException e) {
            Assert.fail("IOException thrown during deserializeAndSet: " + e.getMessage());
        }
        Assert.assertEquals("value", instance.getField());
        
        // Edge case: field is null
        fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            null
        );
        try {
            fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
            Assert.fail("No exception thrown for null field");
        } catch (IOException e) {
            Assert.assertEquals("Missing field (broken JDK (de)serialization?)", e.getMessage());
        }
        
        // Error scenario: JsonParser is null
        jsonParser = null;
        try {
            fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
            Assert.fail("No exception thrown for null JsonParser");
        } catch (IOException e) {
            Assert.assertEquals("Null JsonParser provided", e.getMessage());
        }
    }

    @Test
    public void deserializeSetAndReturn_Test() {
        // Typical use case
        FieldProperty fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            new AnnotatedField()
        );
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("{\"field\":\"value\"}");
        DeserializationContext deserializationContext = new DeserializationContext(null, null);
        Object instance = new Object();
        try {
            instance = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        } catch (IOException e) {
            Assert.fail("IOException thrown during deserializeSetAndReturn: " + e.getMessage());
        }
        Assert.assertEquals("value", instance.getField());
        
        // Edge case: field is null
        fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            null
        );
        try {
            instance = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
            Assert.fail("No exception thrown for null field");
        } catch (IOException e) {
            Assert.assertEquals("Missing field (broken JDK (de)serialization?)", e.getMessage());
        }
        
        // Error scenario: JsonParser is null
        jsonParser = null;
        try {
            instance = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
            Assert.fail("No exception thrown for null JsonParser");
        } catch (IOException e) {
            Assert.assertEquals("Null JsonParser provided", e.getMessage());
        }
    }

    @Test
    public void set_Test() {
        // Typical use case
        FieldProperty fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            new AnnotatedField()
        );
        Object instance = new Object();
        try {
            fieldProperty.set(instance, "value");
        } catch (IOException e) {
            Assert.fail("IOException thrown during set: " + e.getMessage());
        }
        Assert.assertEquals("value", instance.getField());
        
        // Edge case: field is null
        fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            null
        );
        try {
            fieldProperty.set(instance, "value");
            Assert.fail("No exception thrown for null field");
        } catch (IOException e) {
            Assert.assertEquals("Missing field (broken JDK (de)serialization?)", e.getMessage());
        }
        
        // Error scenario: Instance is null
        instance = null;
        try {
            fieldProperty.set(instance, "value");
            Assert.fail("No exception thrown for null instance");
        } catch (IOException e) {
            Assert.assertEquals("Null instance provided", e.getMessage());
        }
    }

    @Test
    public void setAndReturn_Test() {
        // Typical use case
        FieldProperty fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            new AnnotatedField()
        );
        Object instance = new Object();
        try {
            instance = fieldProperty.setAndReturn(instance, "value");
        } catch (IOException e) {
            Assert.fail("IOException thrown during setAndReturn: " + e.getMessage());
        }
        Assert.assertEquals("value", instance.getField());
        
        // Edge case: field is null
        fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            null
        );
        try {
            instance = fieldProperty.setAndReturn(instance, "value");
            Assert.fail("No exception thrown for null field");
        } catch (IOException e) {
            Assert.assertEquals("Missing field (broken JDK (de)serialization?)", e.getMessage());
        }
        
        // Error scenario: Instance is null
        instance = null;
        try {
            instance = fieldProperty.setAndReturn(instance, "value");
            Assert.fail("No exception thrown for null instance");
        } catch (IOException e) {
            Assert.assertEquals("Null instance provided", e.getMessage());
        }
    }

    @Test
    public void fixAccess_Test() {
        // Typical use case: Config enables overriding public access modifiers
        FieldProperty fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            new AnnotatedField()
        );
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        config.enable(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        fieldProperty.fixAccess(config);
        
        // Edge case: Config does not enable overriding public access modifiers
        config.disable(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        fieldProperty.fixAccess(config);
    }
    
    @Test
    public void getAnnotation_Test() {
        // Typical use case
        FieldProperty fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            new AnnotatedField()
        );
        Assert.assertNull(fieldProperty.getAnnotation(Test.class));
        
        // Edge case: Annotated member is null
        fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            null
        );
        Assert.assertNull(fieldProperty.getAnnotation(Test.class));
    }

    @Test
    public void getMember_Test() {
        // Typical use case
        FieldProperty fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            new AnnotatedField()
        );
        Assert.assertNotNull(fieldProperty.getMember());
        
        // Edge case: Annotated member is null
        fieldProperty = new FieldProperty(
            new BeanPropertyDefinition(),
            null,
            null,
            new Annotations(),
            null
        );
        Assert.assertNull(fieldProperty.getMember());
    }

}