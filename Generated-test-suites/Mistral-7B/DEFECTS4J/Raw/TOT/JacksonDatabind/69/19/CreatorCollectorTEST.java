package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.PropertyName;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonCreator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDescriptor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreatorCollectorTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() throws IOException {
        MapperFactory mapperFactory = new MapperFactory();
        ObjectMapper om = mapperFactory.getObjectMapper();
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper = om;
    }

    @Test
    public void testCreatorCollectorDefaultCreator() throws IOException {
        BeanDescription beanDescription = objectMapper.introspect(ClassWithDefaultConstructor.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDescription, objectMapper.getConfig());
        creatorCollector.setDefaultCreator(_getAnnotatedWithParams(ClassWithDefaultConstructor.class, ""));
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(ClassWithDefaultConstructor.class, valueInstantiator.getValueType());
        assertEquals(0, valueInstantiator.getValueType().getParameterTypes().length);
    }

    @Test
    public void testCreatorCollectorStringCreator() throws IOException {
        BeanDescription beanDescription = objectMapper.introspect(ClassWithStringConstructor.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDescription, objectMapper.getConfig());
        creatorCollector.addStringCreator(_getAnnotatedWithParams(ClassWithStringConstructor.class, "StringCreator"));
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(ClassWithStringConstructor.class, valueInstantiator.getValueType());
        assertEquals(String.class, valueInstantiator.getValueType().getParameterTypes()[0].getClass());
        assertEquals("StringCreator", valueInstantiator.getParameterName(0));
    }

    @Test
    public void testCreatorCollectorIntCreator() throws IOException {
        BeanDescription beanDescription = objectMapper.introspect(ClassWithIntConstructor.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDescription, objectMapper.getConfig());
        creatorCollector.addIntCreator(_getAnnotatedWithParams(ClassWithIntConstructor.class, "IntCreator"));
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(ClassWithIntConstructor.class, valueInstantiator.getValueType());
        assertEquals(int.class, valueInstantiator.getValueType().getParameterTypes()[0].getClass());
        assertEquals("IntCreator", valueInstantiator.getParameterName(0));
    }

    @Test
    public void testCreatorCollectorDelegateCreator() throws IOException {
        BeanDescription beanDescription = objectMapper.introspect(ClassWithDelegateConstructor.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDescription, objectMapper.getConfig());
        creatorCollector.addDelegatingCreator(_getAnnotatedWithParams(ClassWithDelegateConstructor.class, "DelegateCreator"), false, new SettableBeanProperty[0]);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(ClassWithDelegateConstructor.class, valueInstantiator.getValueType());
        assertEquals(ClassWithDelegateConstructor.Delegate.class, valueInstantiator.getValueType().getParameterTypes()[0]);
        assertEquals("DelegateCreator", valueInstantiator.getParameterName(0));
    }

    @Test
    public void testCreatorCollectorPropertyBasedCreator() throws IOException {
        BeanDescription beanDescription = objectMapper.introspect(ClassWithPropertyBasedConstructor.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDescription, objectMapper.getConfig());
        creatorCollector.addPropertyCreator(_getAnnotatedWithParams(ClassWithPropertyBasedConstructor.class, "PropertyBasedCreator"), false, new SettableBeanProperty[]{
                new SettableBeanProperty("property1", String.class),
                new SettableBeanProperty("property2", Integer.class)
        });
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(ClassWithPropertyBasedConstructor.class, valueInstantiator.getValueType());
        assertEquals(2, valueInstantiator.getValueType().getParameterTypes().length);
        assertEquals("property1", valueInstantiator.getParameterName(0));
        assertEquals("property2", valueInstantiator.getParameterName(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateCreator() throws IOException {
        BeanDescription beanDescription = objectMapper.introspect(ClassWithDuplicateConstructors.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDescription, objectMapper.getConfig());
        creatorCollector.addStringCreator(_getAnnotatedWithParams(ClassWithDuplicateConstructors.class, "StringCreator"));
        creatorCollector.addStringCreator(_getAnnotatedWithParams(ClassWithDuplicateConstructors.class, "StringCreator"));
        creatorCollector.constructValueInstantiator(objectMapper.getConfig());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorMismatchedCreator() throws IOException {
        BeanDescription beanDescription = objectMapper.introspect(ClassWithMismatchedConstructors.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDescription, objectMapper.getConfig());
        creatorCollector.addIntCreator(_getAnnotatedWithParams(ClassWithMismatchedConstructors.class, "IntCreator"));
        creatorCollector.addStringCreator(_getAnnotatedWithParams(ClassWithMismatchedConstructors.class, "StringCreator"));
        creatorCollector.constructValueInstantiator(objectMapper.getConfig());
    }

    private AnnotatedWithParams _getAnnotatedWithParams(Class<?> clazz, String methodName) throws IOException {
        AnnotatedClass ac = objectMapper.getAnnotatedClass(clazz);
        BeanPropertyDescriptor[] properties = ac.findProperties();
        for (BeanPropertyDescriptor property : properties) {
            if (property.getAnnotations().hasAnnotation(JsonCreator.class) && methodName.equals(property.getName())) {
                return property.getMember();
            }
        }
        throw new IOException("Cannot find method: " + clazz.getName() + "." + methodName);
    }
}

class ClassWithDefaultConstructor {
}

class ClassWithStringConstructor {
    @JsonCreator
    public ClassWithStringConstructor(String s) {
    }
}

class ClassWithIntConstructor {
    @JsonCreator
    public ClassWithIntConstructor(int i) {
    }
}

class ClassWithDelegateConstructor {
    @JsonCreator
    public static ClassWithDelegateConstructor DelegateCreator(ClassWithDelegateConstructor delegate) {
        return delegate;
    }

    public static class Delegate {
    }
}

class ClassWithPropertyBasedConstructor {
    @JsonCreator
    public ClassWithPropertyBasedConstructor(String property1, int property2) {
    }
}

class ClassWithDuplicateConstructors {
    @JsonCreator
    public ClassWithDuplicateConstructors(String s) {
    }

    @JsonCreator
    public ClassWithDuplicateConstructors(String s, int i) {
    }
}

class ClassWithMismatchedConstructors {
    @JsonCreator
    public ClassWithMismatchedConstructors(int i) {
    }

    @JsonCreator
    public ClassWithMismatchedConstructors(String s) {
    }
}