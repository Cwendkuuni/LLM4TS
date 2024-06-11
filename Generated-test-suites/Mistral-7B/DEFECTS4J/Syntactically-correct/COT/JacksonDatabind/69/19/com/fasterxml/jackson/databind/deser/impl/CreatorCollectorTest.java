package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class CreatorCollectorTest {

    private static class TestBean {
        private int intValue;
        private String stringValue;
        private boolean booleanValue;
        private List<Integer> intList;
        private Map<String, String> stringMap;
        private Map<String, TestBean> testBeanMap;

        @JsonCreator(mode = Mode.PROPERTIES)
        public TestBean(@JsonProperty("intValue") int intValue,
                       @JsonProperty("stringValue") String stringValue,
                       @JsonProperty("booleanValue") boolean booleanValue,
                       @JsonProperty("intList") List<Integer> intList,
                       @JsonProperty("stringMap") Map<String, String> stringMap,
                       @JsonProperty("testBeanMap") Map<String, TestBean> testBeanMap) {
            this.intValue = intValue;
            this.stringValue = stringValue;
            this.booleanValue = booleanValue;
            this.intList = intList;
            this.stringMap = stringMap;
            this.testBeanMap = testBeanMap;
        }

        @JsonCreator(mode = Mode.FROM_PARAMETERS)
        public TestBean(int intValue, List<Integer> intList, Map<String, TestBean> testBeanMap) {
            this.intValue = intValue;
            this.intList = intList;
            this.testBeanMap = testBeanMap;
        }

        @JsonProperty
        public int getIntValue() {
            return intValue;
        }

        @JsonProperty
        public String getStringValue() {
            return stringValue;
        }

        @JsonProperty
        public boolean getBooleanValue() {
            return booleanValue;
        }

        @JsonProperty
        public List<Integer> getIntList() {
            return intList;
        }

        @JsonProperty
        public Map<String, String> getStringMap() {
            return stringMap;
        }

        @JsonProperty
        public Map<String, TestBean> getTestBeanMap() {
            return testBeanMap;
        }
    }

    private CreatorCollector creatorCollector;
    private BeanDescription beanDescription;
    private MapperConfig<?> config;

    @Before
    public void setUp() throws Exception {
        Class<?> clazz = TestBean.class;
        beanDescription = new BeanDescription(clazz);
        config = new MapperConfig.StandardConfig();
        creatorCollector = new CreatorCollector(beanDescription, config);
    }

    @Test
    public void testDefaultCreator() {
        creatorCollector.setDefaultCreator(getNoArgConstructorCreator(TestBean.class));
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        TestBean testBean = instantiator.createValueInstance(null);
        assertNotNull(testBean);
    }

    @Test
    public void testStringCreator() throws Exception {
        TestBean testBean = new TestBean("1", "foo", false, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        creatorCollector.addStringCreator(getCreatorMethod(TestBean.class, "TestBean",
                "TestBean", new String[]{"stringValue"}, null), true);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        TestBean testBeanInstance = instantiator.createValueInstance(new String[]{"foo"});
        assertEquals(testBean, testBeanInstance);
    }

    @Test
    public void testIntCreator() throws Exception {
        TestBean testBean = new TestBean(1, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        creatorCollector.addIntCreator(getCreatorMethod(TestBean.class, "TestBean",
                "TestBean", new int[0], new Class[0]), true);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        TestBean testBeanInstance = instantiator.createValueInstance(new int[]{1});
        assertEquals(testBean, testBeanInstance);
    }

    @Test
    public void testLongCreator() throws Exception {
        TestBean testBean = new TestBean(1L, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        creatorCollector.addLongCreator(getCreatorMethod(TestBean.class, "TestBean",
                "TestBean", new long[0], new Class[0]), true);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        TestBean testBeanInstance = instantiator.createValueInstance(new long[]{1L});
        assertEquals(testBean, testBeanInstance);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        TestBean testBean = new TestBean(1.0, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        creatorCollector.addDoubleCreator(getCreatorMethod(TestBean.class, "TestBean",
                "TestBean", new double[0], new Class[0]), true);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        TestBean testBeanInstance = instantiator.createValueInstance(new double[]{1.0});
        assertEquals(testBean, testBeanInstance);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        TestBean testBean = new TestBean(false, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        creatorCollector.addBooleanCreator(getCreatorMethod(TestBean.class, "TestBean",
                "TestBean", new boolean[0], new Class[0]), true);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        TestBean testBeanInstance = instantiator.createValueInstance(new boolean[]{false});
        assertEquals(testBean, testBeanInstance);
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        TestBean testBean = new TestBean(1, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        TestBean delegate = new TestBean(2, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        List<SettableBeanProperty> delegateArgs = Collections.singletonList(
                new SettableBeanProperty(TestBean.class.getDeclaredField("intValue"), 2));
        creatorCollector.addDelegatingCreator(getNoArgConstructorCreator(TestBean.class),
                false, delegateArgs.toArray(new SettableBeanProperty[0]));
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        TestBean testBeanInstance = instantiator.createValueInstance(null);
        assertEquals(delegate, testBeanInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        TestBean testBean1 = new TestBean(1, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        TestBean testBean2 = new TestBean(2, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        creatorCollector.addIntCreator(getCreatorMethod(TestBean.class, "TestBean",
                "TestBean", new int[0], new Class[0]), true);
        creatorCollector.addIntCreator(getCreatorMethod(TestBean.class, "TestBean",
                "TestBean", new int[0], new Class[0]), true);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        instantiator.createValueInstance(new int[]{1});
        instantiator.createValueInstance(new int[]{2});
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        TestBean testBean = new TestBean(1, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        List<SettableBeanProperty> propertyBasedArgs = Collections.singletonList(
                new SettableBeanProperty(TestBean.class.getDeclaredField("intValue"), 1));
        creatorCollector.addPropertyCreator(getCreatorMethod(TestBean.class, "TestBean",
                "TestBean", new Class[0], propertyBasedArgs.toArray(new SettableBeanProperty[0])),
                true);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        TestBean testBeanInstance = instantiator.createValueInstance(new Object[0]);
        assertEquals(testBean, testBeanInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyBasedCreator() throws Exception {
        TestBean testBean1 = new TestBean(1, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        TestBean testBean2 = new TestBean(2, Collections.emptyList(),
                Collections.emptyMap(), Collections.emptyMap());
        List<SettableBeanProperty> propertyBasedArgs1 = Collections.singletonList(
                new SettableBeanProperty(TestBean.class.getDeclaredField("intValue"), 1));
        List<SettableBeanProperty> propertyBasedArgs2 = Collections.singletonList(
                new SettableBeanProperty(TestBean.class.getDeclaredField("intValue"), 2));
        creatorCollector.addPropertyCreator(getCreatorMethod(TestBean.class, "TestBean",
                "TestBean", new Class[0], propertyBasedArgs1.toArray(new SettableBeanProperty[0])),
                true);
        creatorCollector.addPropertyCreator(getCreatorMethod(TestBean.class, "TestBean",
                "TestBean", new Class[0], propertyBasedArgs2.toArray(new SettableBeanProperty[0])),
                true);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        instantiator.createValueInstance(new Object[0]);
    }

    private AnnotatedWithParams getNoArgConstructorCreator(Class<?> clazz) {
        return getConstructorCreator(clazz, new Class[0], new Class[0]);
    }

    private AnnotatedWithParams getCreatorMethod(Class<?> clazz, String methodName,
            String constructorName, Class<?>[] parameterTypes, Class<?>[] constructorParameterTypes) {
        AnnotatedClass annClass = Introspect.findCreator(clazz);
        AnnotatedMethod creatorMethod = annClass.findMethod(methodName, constructorParameterTypes);
        if (creatorMethod == null) {
            throw new IllegalStateException("Could not find creator method \""+methodName+"\" on "+clazz);
        }
        if (!creatorMethod.isAnnotationPresent(JsonCreator.class)) {
            throw new IllegalStateException("Method \""+methodName+"\" is not a creator method on "+clazz);
        }
        return new AnnotatedWithParams(creatorMethod, null);
    }

    private AnnotatedWithParams getConstructorCreator(Class<?> clazz,
            Class<?>[] constructorParameterTypes, Class<?>[] constructorParameterAnnotations) {
        AnnotatedElement constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor(constructorParameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find constructor on "+clazz, e);
        }
        AnnotatedMethod method = (AnnotatedMethod) ClassUtil.getMember(constructor);
        if (method == null || constructorParameterAnnotations == null) {
            throw new IllegalStateException("Constructor on "+clazz+" does not have valid creators");
        }
        return new AnnotatedWithParams(method, constructorParameterAnnotations);
    }
}

class AnnotatedWithParams {
    private final AnnotatedMethod method;
    private final AnnotatedType[] constructorParameterAnnotations;

    public AnnotatedWithParams(AnnotatedMethod method, AnnotatedType[] constructorParameterAnnotations) {
        this.method = method;
        this.constructorParameterAnnotations = constructorParameterAnnotations;
    }

    public AnnotatedMethod getAnnotated() {
        return method;
    }

    public Class<?>[] getRawParameterTypes() {
        return method.getParameterTypes();
    }

    public AnnotatedParameter[] getParameters() {
        return method.getParameters();
    }

    public AnnotatedType getRawParameterType(int index) {
        return method.getParameterTypes()[index];
    }

    public AnnotatedParameter getParameter(int index) {
        return method.getParameters()[index];
    }

    public Class<?> getRawReturnType() {
        return method.getReturnType();
    }

    public AnnotatedType getAnnotatedReturnType() {
        return ClassUtil.getMember(method.getReturnType());
    }

    public Class<?>[] getAnnotatedParameterTypes() {
        AnnotatedType[] types = method.getParameterAnnotations();
        return (types == null) ? new Class[0] : types;
    }

    public AnnotatedParameter getAnnotatedParameter(int index) {
        AnnotatedParameter param = method.getParameters()[index];
        return ClassUtil.getMember(param);
    }
}