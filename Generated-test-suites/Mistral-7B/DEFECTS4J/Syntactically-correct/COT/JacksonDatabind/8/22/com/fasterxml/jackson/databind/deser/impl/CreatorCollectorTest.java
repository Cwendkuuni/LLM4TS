package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private BeanDescription beanDescription;

    @Before
    public void setUp() throws Exception {
        BeanPropertyDefinition p1 = new BeanPropertyDefinition("p1", String.class, null, null);
        BeanPropertyDefinition p2 = new BeanPropertyDefinition("p2", int.class, null, null);
        BeanPropertyDefinition p3 = new BeanPropertyDefinition("p3", long.class, null, null);
        BeanPropertyDefinition p4 = new BeanPropertyDefinition("p4", double.class, null, null);
        BeanPropertyDefinition p5 = new BeanPropertyDefinition("p5", boolean.class, null, null);
        BeanPropertyDefinition p6 = new BeanPropertyDefinition("p6", String[].class, null, null);

        List<BeanPropertyDefinition> propertyDefinitions = Arrays.asList(p1, p2, p3, p4, p5, p6);

        AnnotatedClass ac = new AnnotatedClass(Object.class, null);
        beanDescription = new BeanDescription(ac, new JavaType(Object.class), propertyDefinitions, null, null, null);

        creatorCollector = new CreatorCollector(beanDescription, true);
    }

    @Test
    public void testSetDefaultCreator() throws Exception {
        Method defaultMethod = CreatorCollector.class.getDeclaredMethod("setDefaultCreator", AnnotatedWithParams.class);
        AnnotatedWithParams defaultCreator = new AnnotatedWithParams(new MethodTest(), new Object[0], null, null, null);
        creatorCollector.setDefaultCreator(defaultCreator);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertTrue(instantiator.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() throws Exception {
        Method stringMethod = CreatorCollector.class.getDeclaredMethod("addStringCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(new StringConstructorTest(), new Object[0], null, new JsonCreator(Mode.INSTANCE), null);
        creatorCollector.addStringCreator(stringCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertEquals("default", instantiator.getValueTypeDesc());
        assertNotNull(instantiator.createValueInstance(null));
        String value = (String) instantiator.createValueInstance(null);
        assertEquals("created using String constructor", value);
    }

    @Test
    public void testAddIntCreator() throws Exception {
        Method intMethod = CreatorCollector.class.getDeclaredMethod("addIntCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams intCreator = new AnnotatedWithParams(new IntConstructorTest(), new Object[0], null, new JsonCreator(Mode.INSTANCE), null);
        creatorCollector.addIntCreator(intCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertEquals("default", instantiator.getValueTypeDesc());
        assertNotNull(instantiator.createValueInstance(null));
        int value = (int) instantiator.createValueInstance(null);
        assertEquals(42, value);
    }

    @Test
    public void testAddLongCreator() throws Exception {
        Method longMethod = CreatorCollector.class.getDeclaredMethod("addLongCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams longCreator = new AnnotatedWithParams(new LongConstructorTest(), new Object[0], null, new JsonCreator(Mode.INSTANCE), null);
        creatorCollector.addLongCreator(longCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertEquals("default", instantiator.getValueTypeDesc());
        assertNotNull(instantiator.createValueInstance(null));
        long value = (long) instantiator.createValueInstance(null);
        assertEquals(42L, value);
    }

    @Test
    public void testAddDoubleCreator() throws Exception {
        Method doubleMethod = CreatorCollector.class.getDeclaredMethod("addDoubleCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(new DoubleConstructorTest(), new Object[0], null, new JsonCreator(Mode.INSTANCE), null);
        creatorCollector.addDoubleCreator(doubleCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertEquals("default", instantiator.getValueTypeDesc());
        assertNotNull(instantiator.createValueInstance(null));
        double value = (double) instantiator.createValueInstance(null);
        assertEquals(3.14, value, 0.01);
    }

    @Test
    public void testAddBooleanCreator() throws Exception {
        Method booleanMethod = CreatorCollector.class.getDeclaredMethod("addBooleanCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(new BooleanConstructorTest(), new Object[0], null, new JsonCreator(Mode.INSTANCE), null);
        creatorCollector.addBooleanCreator(booleanCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertEquals("default", instantiator.getValueTypeDesc());
        assertNotNull(instantiator.createValueInstance(null));
        boolean value = (boolean) instantiator.createValueInstance(null);
        assertTrue(value);
    }

    @Test
    public void testAddDelegatingCreator() throws Exception {
        Method delegatingMethod = CreatorCollector.class.getDeclaredMethod("addDelegatingCreator", AnnotatedWithParams.class, boolean[].class, CreatorProperty[].class);
        AnnotatedWithParams delegatingCreator = new AnnotatedWithParams(new DelegatingConstructorTest(), new Object[0], null, null, null);
        CreatorProperty[] injectables = new CreatorProperty[2];
        injectables[0] = new CreatorProperty("p1", String.class);
        injectables[1] = new CreatorProperty("p2", int.class);
        creatorCollector.addDelegatingCreator(delegatingCreator, false, injectables);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertNotNull(instantiator);
        Object value = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals("created using DelegatingConstructor", value.toString());
    }

    @Test
    public void testAddPropertyCreator() throws Exception {
        Method propertyMethod = CreatorCollector.class.getDeclaredMethod("addPropertyCreator", AnnotatedWithParams.class, boolean[].class, CreatorProperty[].class);
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(new PropertyConstructorTest(), new Object[0], null, null, null);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("p1", String.class, new JsonProperty("p1Prop"));
        properties[1] = new CreatorProperty("p2", int.class, new JsonProperty("p2Prop"));
        creatorCollector.addPropertyCreator(propertyCreator, false, properties);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertNotNull(instantiator);
        Object value = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals("created using PropertyConstructor", value.toString());
        Map<?,?> map = (Map<?,?>) value;
        assertEquals("value1", map.get("p1Prop"));
        assertEquals(42, map.get("p2Prop"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateCreator_default() throws Exception {
        Method defaultMethod = CreatorCollector.class.getDeclaredMethod("addStringCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(new StringConstructorTest(), new Object[0], null, new JsonCreator(Mode.INSTANCE), null);
        creatorCollector.addStringCreator(stringCreator, false);
        creatorCollector.addStringCreator(stringCreator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateCreator_explicit() throws Exception {
        Method stringMethod = CreatorCollector.class.getDeclaredMethod("addStringCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(new StringConstructorTest(), new Object[0], null, new JsonCreator(Mode.INSTANCE), null);
        creatorCollector.addStringCreator(stringCreator, true);
        creatorCollector.addStringCreator(stringCreator, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateCreator_withSameNamedProperty() throws Exception {
        Method propertyMethod = CreatorCollector.class.getDeclaredMethod("addPropertyCreator", AnnotatedWithParams.class, boolean[].class, CreatorProperty[].class);
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(new PropertyConstructorTest(), new Object[0], null, null, null);
        CreatorProperty p1 = new CreatorProperty("p1", String.class, new JsonProperty("p1Prop"));
        CreatorProperty p2 = new CreatorProperty("p1", int.class, new JsonProperty("p1Prop"));
        creatorCollector.addPropertyCreator(propertyCreator, false, new CreatorProperty[] { p1, p2 });
    }

    @Test
    public void testAddIncompleteParameter() throws Exception {
        Method incompleteParameterMethod = CreatorCollector.class.getDeclaredMethod("addIncompleteParameter", AnnotatedParameter.class);
        AnnotatedParameter incompleteParameter = new AnnotatedParameter(new MethodTest(), new Object[0], null, new JsonProperty("p1Prop"));
        creatorCollector.addIncompleteParameter(incompleteParameter);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);
        assertNotNull(instantiator);
        Object value = instantiator.createValueInstance(Collections.emptyMap());
        assertEquals("created using MethodTest without specifying a constructor", value.toString());
    }

    static class MethodTest {
        public MethodTest() {
        }
    }

    static class StringConstructorTest {
        public StringConstructorTest() {
        }

        @JsonCreator
        public static String create(String s) {
            return s;
        }
    }

    static class IntConstructorTest {
        public IntConstructorTest() {
        }

        @JsonCreator
        public static int create(int i) {
            return i + 42;
        }
    }

    static class LongConstructorTest {
        public LongConstructorTest() {
        }

        @JsonCreator
        public static long create(long l) {
            return l + 42L;
        }
    }

    static class DoubleConstructorTest {
        public DoubleConstructorTest() {
        }

        @JsonCreator
        public static double create(double d) {
            return d + 3.14;
        }
    }

    static class BooleanConstructorTest {
        public BooleanConstructorTest() {
        }

        @JsonCreator
        public static boolean create(boolean b) {
            return !b;
        }
    }

    static class DelegatingConstructorTest {
        public DelegatingConstructorTest() {
        }

        @JsonCreator
        public static Object create(String s, int i) {
            return "created using DelegatingConstructor: " + s + i;
        }
    }

    static class PropertyConstructorTest {
        public PropertyConstructorTest() {
        }

        @JsonCreator
        public static Object create(@JsonProperty("p1Prop") String s, @JsonProperty("p2Prop") int i) {
            return "created using PropertyConstructor: " + s + i;
        }
    }
}