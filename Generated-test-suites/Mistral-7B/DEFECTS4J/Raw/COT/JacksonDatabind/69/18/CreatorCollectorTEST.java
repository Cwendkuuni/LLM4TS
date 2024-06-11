package com.example; // replace with the actual package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private MapperConfig<Object> config;
    private BeanDescription beanDesc;

    @Before
    public void setup() {
        beanDesc = new BeanDescription(Object.class);
        config = new MapperConfig.Builder().build();
        creatorCollector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testDefaultCreator() {
        // Create a default constructor test case
        creatorCollector.setDefaultCreator(null);
        StdValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertNotNull(instantiator.createUsingDefault(null));
    }

    @Test
    public void testStringCreator() {
        // Create a String constructor test case
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        creatorCollector.addStringCreator(new AnnotatedWithParams(null, clazz), false);
        StdValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        String value = "Test String";
        assertEquals(value, instantiator.createValueInstance(null, null));
    }

    @Test
    public void testIntCreator() {
        // Create an int constructor test case
        int intValue = 1;
        creatorCollector.addIntCreator(new AnnotatedWithParams(null, intValue.getClass()), false);
        StdValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertEquals(intValue, instantiator.createValueInstance(null, null));
    }

    @Test
    public void testLongCreator() {
        // Create a long constructor test case
        long longValue = 1L;
        creatorCollector.addLongCreator(new AnnotatedWithParams(null, longValue.getClass()), false);
        StdValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertEquals(longValue, instantiator.createValueInstance(null, null));
    }

    @Test
    public void testDoubleCreator() {
        // Create a double constructor test case
        double doubleValue = 1.0;
        creatorCollector.addDoubleCreator(new AnnotatedWithParams(null, doubleValue.getClass()), false);
        StdValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertEquals(doubleValue, instantiator.createValueInstance(null, null), 0.0);
    }

    @Test
    public void testBooleanCreator() {
        // Create a boolean constructor test case
        boolean booleanValue = true;
        creatorCollector.addBooleanCreator(new AnnotatedWithParams(null, booleanValue.getClass()), false);
        StdValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertEquals(booleanValue, instantiator.createValueInstance(null, null));
    }

    @Test
    public void testDelegateCreator() {
        // Create a delegate constructor test case
        String delegateClassName = "java.lang.String";
        Class<?> delegateClass = Class.forName(delegateClassName);
        SettableBeanProperty[] delegateArgs = {new SettableBeanProperty("delegate", String.class)};
        creatorCollector.addDelegatingCreator(new AnnotatedWithParams(null, delegateClass), false, delegateArgs);
        StdValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        String delegateValue = "Delegate String";
        SettableBeanProperty delegateProperty = new SettableBeanProperty("delegate", String.class);
        delegateProperty.set(null, delegateValue);
        Object instance = instantiator.createValueInstance(null, new Object[]{delegateProperty});
        assertEquals(delegateValue, ((String) instance.getClass().getDeclaredField("delegate").get(instance)));
    }

    @Test
    public void testArrayDelegateCreator() {
        // Create an array delegate constructor test case
        int[] arrayDelegateValues = {1, 2, 3};
        SettableBeanProperty[] arrayDelegateArgs = {new SettableBeanProperty("arrayDelegate", int[].class)};
        creatorCollector.addArrayDelegateCreator(new AnnotatedWithParams(null, int[].class), false, arrayDelegateArgs);
        StdValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        int[] expectedArray = arrayDelegateValues;
        int[] actualArray = (int[]) instantiator.createValueInstance(null, new Object[]{expectedArray});
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() {
        // Create a duplicate constructor test case
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        creatorCollector.addStringCreator(new AnnotatedWithParams(null, clazz), false);
        creatorCollector.addStringCreator(new AnnotatedWithParams(null, clazz), false);
        creatorCollector.constructValueInstantiator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentTypeCreator() {
        // Create a different type constructor test case
        String className1 = "java.lang.String";
        Class<?> clazz1 = Class.forName(className1);
        String className2 = "java.lang.Integer";
        Class<?> clazz2 = Class.forName(className2);
        creatorCollector.addStringCreator(new AnnotatedWithParams(null, clazz1), false);
        creatorCollector.addIntCreator(new AnnotatedWithParams(null, clazz2), false);
        creatorCollector.constructValueInstantiator(null);
    }
}