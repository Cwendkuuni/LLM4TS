package com.example.test; // replace with your testing package

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Test class for CreatorCollector.
 */
public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() {
        // Given
        BeanDescription beanDesc = new BeanDescription(Object.class);
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);

        // When
        // Test default creator
        creatorCollector.setDefaultCreator(new AnnotatedWithParams(new Method(Object.class, ""), null));
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        assertEquals(Object.class, valueInstantiator.getValueType());

        // Test string creator
        creatorCollector.addStringCreator(new AnnotatedWithParams(new Method(String.class, ""), null), false);
        valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        assertEquals(String.class, valueInstantiator.getValueType());

        // Test int creator
        creatorCollector.addIntCreator(new AnnotatedWithParams(new Method(Integer.class, ""), null), false);
        valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        assertEquals(Integer.class, valueInstantiator.getValueType());

        // Test long creator
        creatorCollector.addLongCreator(new AnnotatedWithParams(new Method(Long.class, ""), null), false);
        valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        assertEquals(Long.class, valueInstantiator.getValueType());

        // Test double creator
        creatorCollector.addDoubleCreator(new AnnotatedWithParams(new Method(Double.class, ""), null), false);
        valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        assertEquals(Double.class, valueInstantiator.getValueType());

        // Test boolean creator
        creatorCollector.addBooleanCreator(new AnnotatedWithParams(new Method(Boolean.class, ""), null), false);
        valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        assertEquals(Boolean.class, valueInstantiator.getValueType());

        // Test delegate creator
        Class<CreatorCollectorTest> clazz = CreatorCollectorTest.class;
        Method method = clazz.getMethod("createDelegate", String[].class);
        creatorCollector.addDelegatingCreator(new AnnotatedWithParams(method, null, null), false, null);
        valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(valueInstantiator);

        // Test property-based creator
        PropertyName propertyName = PropertyName.construct("propertyName");
        creatorCollector.addPropertyCreator(new AnnotatedWithParams(method, null, new SettableBeanProperty[]{
                new SettableBeanProperty(propertyName, String.class, null),
                new SettableBeanProperty(propertyName, Integer.class, null)
        }), false, null);
        valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(valueInstantiator);

        // Then
        // Test default creator absence
        creatorCollector.setDefaultCreator(null);
        try {
            creatorCollector.constructValueInstantiator(new DeserializationConfig());
            fail("Expected java.lang.IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test string creator absence
        creatorCollector.addStringCreator(null);
        creatorCollector.addIntCreator(null);
        // ... and so on
        try {
            creatorCollector.constructValueInstantiator(new DeserializationConfig());
            fail("Expected java.lang.IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test type mismatch in delegating creator
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[]{new SettableBeanProperty(new PropertyName(""), String.class, null)};
        creatorCollector.addDelegatingCreator(new AnnotatedWithParams(method, null, delegateArgs), false, null);
        try {
            creatorCollector.constructValueInstantiator(new DeserializationConfig());
            fail("Expected java.lang.IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test duplicate creators
        creatorCollector.addStringCreator(new AnnotatedWithParams(new Method(String.class, ""), null), false);
        creatorCollector.addStringCreator(new AnnotatedWithParams(new Method(String.class, ""), null), false);
        try {
            creatorCollector.constructValueInstantiator(new DeserializationConfig());
            fail("Expected java.lang.IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test explicit creators
        creatorCollector.addStringCreator(new AnnotatedWithParams(new Method(String[].class, ""), null), true);
        creatorCollector.addIntCreator(new AnnotatedWithParams(new Method(Integer[].class, ""), null), true);
        // ... and so on
        creatorCollector.constructValueInstantiator(new DeserializationConfig()); // should not throw an exception
    }

    /**
     * Test method to be used by CreatorCollector as a delegate creator.
     *
     * @param strings a String array
     */
    public static String createDelegate(String[] strings) {
        return "Delegate created with " + strings.length + " strings.";
    }
}