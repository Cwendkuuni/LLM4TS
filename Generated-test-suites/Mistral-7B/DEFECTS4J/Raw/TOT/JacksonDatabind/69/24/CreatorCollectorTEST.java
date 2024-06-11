package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.test.BaseMapTest;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

import static org.junit.Assert.*;

public class CreatorCollectorTest extends BaseMapTest {

    @Test
    public void testDefaultConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(String[].class);
        CreatorCollector cc = new CreatorCollector(beanDesc, mapper.getConfig());

        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
        String[] result = inst.createValue(String[].class, null);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testStringConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(ArrayList.class);
        AnnotatedConstructor constructor = beanDesc.getFactory().getRawInstantiator().getMemberInstances()[0];
        AnnotatedWithParams params = new AnnotatedWithParams(constructor, constructor.getParameterDescriptor(0), null);

        cc.setDefaultCreator(params);
        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
        ArrayList<String> result = inst.createValue(ArrayList.class, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testIntConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        AnnotatedConstructor constructor = beanDesc.getFactory().getRawInstantiator().getMemberInstances()[0];
        AnnotatedWithParams params = new AnnotatedWithParams(constructor, constructor.getParameterDescriptor(0), null);

        cc.addIntCreator(params, true);
        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
        HashMap<String, Integer> result = inst.createValue(HashMap.class, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testLongConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(LinkedHashMap.class);
        AnnotatedConstructor constructor = beanDesc.getFactory().getRawInstantiator().getMemberInstances()[0];
        AnnotatedWithParams params = new AnnotatedWithParams(constructor, constructor.getParameterDescriptor(0), null);

        cc.addLongCreator(params, true);
        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
        LinkedHashMap<String, Long> result = inst.createValue(LinkedHashMap.class, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testDoubleConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(ArrayList.class);
        AnnotatedConstructor constructor = beanDesc.getFactory().getRawInstantiator().getMemberInstances()[0];
        AnnotatedWithParams params = new AnnotatedWithParams(constructor, constructor.getParameterDescriptor(0), null);

        cc.addDoubleCreator(params, true);
        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
        ArrayList<Double> result = inst.createValue(ArrayList.class, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testBooleanConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        AnnotatedConstructor constructor = beanDesc.getFactory().getRawInstantiator().getMemberInstances()[0];
        AnnotatedWithParams params = new AnnotatedWithParams(constructor, constructor.getParameterDescriptor(0), null);

        cc.addBooleanCreator(params, true);
        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
        HashMap<String, Boolean> result = inst.createValue(HashMap.class, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testDelegatingConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(LinkedHashMap.class);
        Constructor<LinkedHashMap<String, Object>> constructor = beanDesc.getRawDeserInstance().getClass().getDeclaredConstructor();
        constructor.setAccessible(true);
        AnnotatedWithParams params = new AnnotatedWithParams(constructor, constructor.getParameterDescriptor(0), null);

        List<String> list = Arrays.asList("one", "two", "three");
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        delegateArgs[0] = mapper.findBeanProperty(LinkedHashMap.class, "entrySet");

        cc.addDelegatingCreator(params, true, delegateArgs);
        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
        LinkedHashMap<String, Object> result = inst.createValue(LinkedHashMap.class, null);
        assertNotNull(result);
        assertEquals(3, result.size());
        for (Map.Entry<String, Object> entry : result.entrySet()) {
            assertEquals("one", entry.getKey());
            assertEquals("one", entry.getValue().toString());
        }
    }

    @Test
    public void testPropertyBasedConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);
        AnnotatedConstructor constructor = beanDesc.getFactory().getRawInstantiator().getMemberInstances()[0];
        AnnotatedWithParams params = new AnnotatedWithParams(constructor, constructor.getParameterDescriptor(0), null);

        AnnotatedMember member1 = beanDesc.findMemberAnnotation("first");
        SettableBeanProperty property1 = mapper.findBeanProperty(MyClass.class, member1.getName());

        AnnotatedMember member2 = beanDesc.findMemberAnnotation("second");
        SettableBeanProperty property2 = mapper.findBeanProperty(MyClass.class, member2.getName());

        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        propertyBasedArgs[0] = property1;
        propertyBasedArgs[1] = property2;

        cc.addPropertyCreator(params, true, propertyBasedArgs);
        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
        MyClass result = inst.createValue(MyClass.class, null);
        assertNotNull(result);
        assertEquals("one", result.getFirst());
        assertEquals("two", result.getSecond());
    }

    @Test
    public void testArrayDelegateConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(MyArrayClass.class);
        AnnotatedConstructor constructor = beanDesc.getFactory().getRawInstantiator().getMemberInstances()[0];
        AnnotatedWithParams params = new AnnotatedWithParams(constructor, constructor.getParameterDescriptor(0), null);

        AnnotatedMember member = beanDesc.findMemberAnnotation("value");
        SettableBeanProperty property = mapper.findBeanProperty(MyArrayClass.class, member.getName());

        SettableBeanProperty[] arrayDelegateArgs = new SettableBeanProperty[1];
        arrayDelegateArgs[0] = property;

        cc.addArrayDelegateCreator(params, true, arrayDelegateArgs);
        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
        MyArrayClass result = inst.createValue(MyArrayClass.class, null);
        assertNotNull(result);
        assertEquals(3, result.getValue().length);
        for (int i = 0; i < result.getValue().length; i++) {
            assertEquals("["+i+"]", result.getValue()[i].toString());
        }
    }

    @Test
    public void testConstructorWithIncompleteParameter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);
        AnnotatedConstructor constructor = beanDesc.getFactory().getRawInstantiator().getMemberInstances()[0];
        AnnotatedParameter incompleteParameter = constructor.getParameterDescriptor(1);

        cc.addIncompleteParameter(incompleteParameter);
        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
        MyClass result = inst.createValue(MyClass.class, null);
        assertNotNull(result);
        assertTrue(result.foo == null);
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = mapper.getTypeFactory().constructValueType(CreatorCollector.class);

        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);
        AnnotatedConstructor constructor = beanDesc.getFactory().getRawInstantiator().getMemberInstances()[0];
        AnnotatedWithParams params = new AnnotatedWithParams(constructor, constructor.getParameterDescriptor(0), null);

        AnnotatedMember member = beanDesc.findMemberAnnotation("first");
        SettableBeanProperty property = mapper.findBeanProperty(MyClass.class, member.getName());

        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        propertyBasedArgs[0] = property;

        cc.addPropertyCreator(params, true, propertyBasedArgs);
        cc.addPropertyCreator(params, true, propertyBasedArgs);
        ValueInstantiator inst = cc.constructValueInstantiator(mapper.getConfig());
    }

    private static class MyClass {
        @JsonProperty
        private String first;
        @JsonProperty
        private String second;

        public MyClass(String first, String second) {
            this.first = first;
            this.second = second;
        }

        public String getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }

        public String foo;
    }

    private static class MyArrayClass {
        @JsonProperty
        private String[] value;

        public MyArrayClass(String[] value) {
            this.value = value;
        }

        public String[] getValue() {
            return value;
        }
    }
}