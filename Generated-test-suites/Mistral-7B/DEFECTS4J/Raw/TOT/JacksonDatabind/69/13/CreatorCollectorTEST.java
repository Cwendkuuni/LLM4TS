package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.PropertyName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.AbstractValueFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreatorCollectorTest {

    private ObjectMapper objectMapper;

    @BeforeMethod
    public void setup() throws Exception {
        MapperFactory mapperFactory = new MapperFactory();
        ObjectMapper om = mapperFactory.getObjectMapper();
        objectMapper = new ObjectMapper(om.getDeserializationConfig());
    }

    @Test
    public void testDefaultCreator() throws Exception {
        BeanDescription beanDescription = objectMapper.findDeserializer(String.class);
        CreatorCollector collector = new CreatorCollector(beanDescription, objectMapper.getDeserializationConfig());
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        Class<?> stringClass = String.class;
        Assert.assertEquals(valueInstantiator.construct(stringClass), "");
    }

    @Test
    public void testStringCreator() throws Exception {
        AnnotatedMember stringCreator = findCreatorAnnotationMethod(String.class, "valueOf");
        CreatorCollector collector = new CreatorCollector(null, objectMapper.getDeserializationConfig());
        collector.addStringCreator(new AnnotatedWithParams(stringCreator), false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        Class<?> stringClass = String.class;
        Assert.assertEquals(valueInstantiator.construct(stringClass, "test"), "test");
    }

    @Test
    public void testIntCreator() throws Exception {
        AnnotatedMember intCreator = findCreatorAnnotationMethod(int.class, "valueOf");
        CreatorCollector collector = new CreatorCollector(null, objectMapper.getDeserializationConfig());
        collector.addIntCreator(new AnnotatedWithParams(intCreator), false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        Class<?> intClass = int.class;
        Assert.assertEquals(valueInstantiator.construct(intClass, 10), 10);
    }

    @Test
    public void testLongCreator() throws Exception {
        AnnotatedMember longCreator = findCreatorAnnotationMethod(long.class, "valueOf");
        CreatorCollector collector = new CreatorCollector(null, objectMapper.getDeserializationConfig());
        collector.addLongCreator(new AnnotatedWithParams(longCreator), false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        Class<?> longClass = long.class;
        Assert.assertEquals(valueInstantiator.construct(longClass, 10L), 10L);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        AnnotatedMember booleanCreator = findCreatorAnnotationMethod(boolean.class, "valueOf");
        CreatorCollector collector = new CreatorCollector(null, objectMapper.getDeserializationConfig());
        collector.addBooleanCreator(new AnnotatedWithParams(booleanCreator), false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        Class<?> booleanClass = boolean.class;
        Assert.assertEquals(valueInstantiator.construct(booleanClass, false), false);
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        AnnotatedClass stringClass = objectMapper.getTypeFactory().constructType(String.class);
        AnnotatedMethod delegateMethod = stringClass.findMethod("valueOf", String.class, Collection.class);
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        BeanPropertyDefinition delegateProperty = new BeanPropertyDefinition("delegate", String.class);
        delegateProperty.setSetter(new SetterMethod(delegateMethod, null));
        delegateArgs[0] = new SettableBeanProperty(delegateProperty);
        CreatorCollector collector = new CreatorCollector(null, objectMapper.getDeserializationConfig());
        collector.addDelegatingCreator(new AnnotatedWithParams(delegateMethod), false, delegateArgs);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        Class<?> stringClass1 = String.class;
        List<String> collection = Arrays.asList("test1", "test2");
        Assert.assertEquals(valueInstantiator.construct(stringClass1, collection), "test1,test2");
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        AnnotatedClass stringClass = objectMapper.getTypeFactory().constructType(String.class);
        AnnotatedMethod propertyBasedMethod = stringClass.findConstructor();
        List<PropertyName> propertyNames = Collections.singletonList(PropertyName.construct("value"));
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[1];
        BeanPropertyDefinition propertyBasedProperty = new BeanPropertyDefinition("propertyBased", String.class);
        propertyBasedProperty.setSetter(new SetterMethod(propertyBasedMethod, propertyNames));
        propertyBasedArgs[0] = new SettableBeanProperty(propertyBasedProperty);
        CreatorCollector collector = new CreatorCollector(null, objectMapper.getDeserializationConfig());
        collector.addPropertyCreator(new AnnotatedWithParams(propertyBasedMethod), false, propertyBasedArgs);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        Map<String, String> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        Assert.assertEquals(valueInstantiator.construct(map.getClass()), map);
    }

    @Test
    public void testNoCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDescription = mapper.findDeserializer(Map.class);
        CreatorCollector collector = new CreatorCollector(beanDescription, mapper.getDeserializationConfig());
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        Assert.assertNull(valueInstantiator);
    }

    private AnnotatedMember findCreatorAnnotationMethod(Class<?> clazz, String methodName) throws Exception {
        AnnotatedClass annotatedClass = objectMapper.getTypeFactory().constructType(clazz);
        AnnotatedMember member = annotatedClass.findMethod(methodName, (Class<?>[]) null);
        if (member == null) {
            throw new IllegalStateException("Method not found: " + clazz.getName() + "." + methodName);
        }
        if (!member.isAnnotationPresent(Creator.class)) {
            throw new IllegalStateException("Method not annotated: " + clazz.getName() + "." + methodName);
        }
        return member;
    }

    private static class AnnotatedWithParams {
        private final AnnotatedMember _member;

        public AnnotatedWithParams(AnnotatedMember member) {
            _member = member;
        }

        public AnnotatedMember getAnnotated() {
            return _member;
        }

        public Class<?> getRawParameterType(int index) {
            return _member.getRawParameterTypes()[index];
        }

        public String getName() {
            return _member.getName();
        }
    }

    private static class SetterMethod implements BeanProperty.BeanPropertySetter {
        private final AnnotatedMethod _method;

        public SetterMethod(AnnotatedMethod method, AbstractValueFactory<?> valueFactory) {
            _method = method;
        }

        @Override
        public void set(Object bean, Object value) throws IllegalAccessException {
            try {
                _method.call(bean, value);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}