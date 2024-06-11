package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import junit.framework.TestCase;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreatorCollectorTest {

    private static ObjectMapper mapper;
    private static BeanDescription beanDescription;
    private static DeserializationConfig deserializationConfig;
    private static SerializationConfig serializationConfig;
    private static JsonFactory jsonFactory;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() throws Exception {
        mapper = new ObjectMapper();

        // Create BeanDescription for testing
        Class<CreatorCollector> clazz = CreatorCollector.class;
        JavaType javaType = mapper.getTypeFactory().constructType(clazz);
        beanDescription = mapper.getFactory().findDefaultInstanceValueSerializer(javaType);

        deserializationConfig = mapper.getDeserializationConfig();
        serializationConfig = mapper.getSerializationConfig();
        jsonFactory = mapper.getFactory();
    }

    @Test
    public void testDefaultCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDescription, true);

        Method defaultConstructor = clazz.getDeclaredConstructor();
        AnnotatedWithParams constructor = new AnnotatedWithParams(defaultConstructor, null);
        collector.setDefaultCreator(constructor);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        Object createdObject = instantiator.createValue(deserializationContext());

        assertEquals(CreatorCollector.class, createdObject.getClass());
    }

    @Test
    public void testStringCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDescription, true);

        String constructorName = "CreatorWithStringArg";
        String constructorArg = "test";
        Method constructor = new Class[] {String.class}.length > 0
                ? clazz.getDeclaredMethod(constructorName, String.class)
                : clazz.getDeclaredMethod(constructorName);
        AnnotatedWithParams creator = new AnnotatedWithParams(constructor, constructorArg);

        collector.addStringCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        Object createdObject = instantiator.createValue(deserializationContext(), "testStringArg");

        assertEquals("testStringArg", createdObject);
    }

    @Test
    public void testIntCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDescription, true);

        String constructorName = "CreatorWithIntArg";
        int constructorArg = 1;
        Method constructor = new Class[] {int.class}.length > 0
                ? clazz.getDeclaredMethod(constructorName, int.class)
                : clazz.getDeclaredMethod(constructorName);
        AnnotatedWithParams creator = new AnnotatedWithParams(constructor, constructorArg);

        collector.addIntCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        Object createdObject = instantiator.createValue(deserializationContext(), 1);

        assertEquals(Integer.valueOf(1), createdObject);
    }

    @Test
    public void testLongCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDescription, true);

        String constructorName = "CreatorWithLongArg";
        long constructorArg = 1L;
        Method constructor = new Class[] {long.class}.length > 0
                ? clazz.getDeclaredMethod(constructorName, long.class)
                : clazz.getDeclaredMethod(constructorName);
        AnnotatedWithParams creator = new AnnotatedWithParams(constructor, constructorArg);

        collector.addLongCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        Object createdObject = instantiator.createValue(deserializationContext(), 1L);

        assertEquals(Long.valueOf(1L), createdObject);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDescription, true);

        String constructorName = "CreatorWithDoubleArg";
        double constructorArg = 1.0;
        Method constructor = new Class[] {double.class}.length > 0
                ? clazz.getDeclaredMethod(constructorName, double.class)
                : clazz.getDeclaredMethod(constructorName);
        AnnotatedWithParams creator = new AnnotatedWithParams(constructor, constructorArg);

        collector.addDoubleCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        Object createdObject = instantiator.createValue(deserializationContext(), 1.0);

        assertEquals(Double.valueOf(1.0), createdObject);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDescription, true);

        String constructorName = "CreatorWithBooleanArg";
        boolean constructorArg = true;
        Method constructor = new Class[] {boolean.class}.length > 0
                ? clazz.getDeclaredMethod(constructorName, boolean.class)
                : clazz.getDeclaredMethod(constructorName);
        AnnotatedWithParams creator = new AnnotatedWithParams(constructor, constructorArg);

        collector.addBooleanCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        Object createdObject = instantiator.createValue(deserializationContext(), true);

        assertEquals(Boolean.TRUE, createdObject);
    }

    @Test
    public void testDelegateCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDescription, true);

        String constructorName = "CreatorWithDelegate";
        Class<?> delegateType = String.class;
        Method constructor = clazz.getDeclaredMethod(constructorName, String.class);
        AnnotatedWithParams creator = new AnnotatedWithParams(constructor, null);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty("delegate", String.class, null, null);

        collector.addDelegatingCreator(creator, true, injectables);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        Object createdObject = instantiator.createValue(deserializationContext(), "test");

        assertEquals("test", createdObject);
    }

    @Test
    public void testPropertyCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDescription, true);

        String constructorName = "CreatorWithProperties";
        Method constructor = clazz.getDeclaredMethod(constructorName, null);
        AnnotatedWithParams creator = new AnnotatedWithParams(constructor, null);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("property1", String.class, null, null);
        properties[1] = new CreatorProperty("property2", Integer.class, null, null);

        collector.addPropertyCreator(creator, true, properties);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        Map<String, Object> createdObject = instantiator.createValue(deserializationContext(),
                new Object[] {"testString", Integer.valueOf(1)});

        assertEquals(Map.class, createdObject.getClass());
        assertEquals("testString", createdObject.get("property1"));
        assertEquals(Integer.valueOf(1), createdObject.get("property2"));
    }

    @Test
    public void testMissingCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDescription, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        Object createdObject = instantiator.createValue(deserializationContext());

        assertNull(createdObject);
    }

    private DeserializationContext deserializationContext() {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getBeanDescription()).thenReturn(beanDescription);
        when(context.getDeserializationConfig()).thenReturn(deserializationConfig);
        when(context.getSerializationConfig()).thenReturn(serializationConfig);
        when(context.getFactory()).thenReturn(jsonFactory);
        when(context.getConfig()).thenReturn(deserializationConfig);
        when(context.findValueInstantiator(beanDescription, deserializationConfig)).thenReturn(instantiator());
        return context;
    }

    private ValueInstantiator instantiator() {
        return mock(ValueInstantiator.class);
    }
}