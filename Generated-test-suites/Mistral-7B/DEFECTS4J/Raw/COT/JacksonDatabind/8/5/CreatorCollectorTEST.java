package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdCreatorProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getFactory().findConcreteDeserializationContext()
                .findBeanDescription(String[].class);

        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfiguration());
        assertNotNull(instantiator);

        List<ValueDeserializer<?>> constructors = Arrays.asList(
                mapper.getValueConstructors().findValueDeserializer(Collections.class),
                mapper.getValueConstructors().findValueDeserializer(ArrayList.class),
                mapper.getValueConstructors().findValueDeserializer(LinkedHashMap.class),
                mapper.getValueConstructors().findValueDeserializer(HashMap.class)
        );

        StdValueInstantiator stdInst = (StdValueInstantiator) instantiator;
        for (ValueDeserializer<?> constructor : constructors) {
            Class<?> constructorType = constructor.getType();
            String typeName = constructorType.getName();
            int index = 0;
            if (typeName.equals(Collections.class.getName())) {
                index = CreatorCollector.C_DEFAULT;
            } else if (typeName.equals(ArrayList.class.getName())) {
                index = CreatorCollector.C_COLLECTION;
            } else if (typeName.equals(LinkedHashMap.class.getName())) {
                index = CreatorCollector.C_MAP;
            } else if (typeName.equals(HashMap.class.getName())) {
                index = CreatorCollector.C_HASH_MAP;
            }
            stdInst.assertConfigured(index, constructor, null);
        }
    }

    @Test
    public void testDefaultCreator() {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getFactory().findConcreteDeserializationContext()
                .findBeanDescription(String[].class);

        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams defaultCreator = mapper.findValueDeserializerAnnotation(beanDesc.getRawClass(), null);
        collector.setDefaultCreator(defaultCreator);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfiguration());
        assertNotNull(instantiator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateStringCreator() {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getFactory().findConcreteDeserializationContext()
                .findBeanDescription(String[].class);

        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams stringCreator = mapper.findValueDeserializerAnnotation(String.class, null);
        collector.addStringCreator(stringCreator, true);
        collector.addStringCreator(stringCreator, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateIntCreator() {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getFactory().findConcreteDeserializationContext()
                .findBeanDescription(Integer.class);

        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams intCreator = mapper.findValueDeserializerAnnotation(Integer.class, null);
        collector.addIntCreator(intCreator, true);
        collector.addIntCreator(intCreator, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateLongCreator() {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getFactory().findConcreteDeserializationContext()
                .findBeanDescription(Long.class);

        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams longCreator = mapper.findValueDeserializerAnnotation(Long.class, null);
        collector.addLongCreator(longCreator, true);
        collector.addLongCreator(longCreator, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateDoubleCreator() {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getFactory().findConcreteDeserializationContext()
                .findBeanDescription(Double.class);

        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams doubleCreator = mapper.findValueDeserializerAnnotation(Double.class, null);
        collector.addDoubleCreator(doubleCreator, true);
        collector.addDoubleCreator(doubleCreator, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateBooleanCreator() {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getFactory().findConcreteDeserializationContext()
                .findBeanDescription(Boolean.class);

        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams booleanCreator = mapper.findValueDeserializerAnnotation(Boolean.class, null);
        collector.addBooleanCreator(booleanCreator, true);
        collector.addBooleanCreator(booleanCreator, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateDelegateCreator() {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getFactory().findConcreteDeserializationContext()
                .findBeanDescription(String.class);

        CreatorProperty[] delegateArgs = new CreatorProperty[1];
       CreatorProperty arg1 = new StdCreatorProperty("arg1", Object.class, null);
        delegateArgs[0] = arg1;

        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams delegateCreator = mapper.findValueDeserializerAnnotation(String.class, null);
        collector.addDelegatingCreator(delegateCreator, true, delegateArgs);
        collector.addDelegatingCreator(delegateCreator, true, delegateArgs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyCreator() {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getFactory().findConcreteDeserializationContext()
                .findBeanDescription(MyClass.class);

        BeanPropertyDefinition[] properties = new JacksonAnnotationIntrospector()
                .findProperties(beanDesc, null);
        CreatorProperty[] propertyBasedArgs = new CreatorProperty[2];
        propertyBasedArgs[0] = new StdCreatorProperty("prop1", String.class, null);
        propertyBasedArgs[1] = new StdCreatorProperty("prop1", String.class, null);

        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams propertyCreator = mapper.findValueDeserializerAnnotation(MyClass.class, null);
        collector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);
        collector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);
    }

    private static class MyClass {
        private String prop1;
    }
}