package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {
    private MapperConfig<?> config;
    private CreatorCollector collector;

    @Before
    public void setUp() {
        config = new MapperConfig<>();
        collector = new CreatorCollector(new BeanDescription(), config);
    }

    @Test
    public void testAddDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(MyClass.class, MyClass.class.getDeclaredConstructor());
        collector.setDefaultCreator(creator);
        assertTrue(collector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new StringCreator(), String.class);
        collector.addStringCreator(creator, false);
        assertTrue(collector.hasCreatorForType(String.class));
        assertEquals(creator, collector.getStringCreator());
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new IntCreator(), int.class);
        collector.addIntCreator(creator, false);
        assertTrue(collector.hasCreatorForType(int.class));
        assertEquals(creator, collector.getIntCreator());
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new LongCreator(), long.class);
        collector.addLongCreator(creator, false);
        assertTrue(collector.hasCreatorForType(long.class));
        assertEquals(creator, collector.getLongCreator());
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new DoubleCreator(), double.class);
        collector.addDoubleCreator(creator, false);
        assertTrue(collector.hasCreatorForType(double.class));
        assertEquals(creator, collector.getDoubleCreator());
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new BooleanCreator(), boolean.class);
        collector.addBooleanCreator(creator, false);
        assertTrue(collector.hasCreatorForType(boolean.class));
        assertEquals(creator, collector.getBooleanCreator());
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new DelegatingCreator(), MyDelegate.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = new SettableBeanProperty(MyDelegate.class, "property");
        collector.addDelegatingCreator(creator, false, injectables);
        assertTrue(collector.hasDelegatingCreator());
        assertEquals(creator, collector.getDelegatingCreator());
        assertEquals(injectables, collector.getDelegateArgs());
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new PropertyCreator(), MyClass.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        properties[0] = new SettableBeanProperty(MyClass.class, "property1");
        properties[1] = new SettableBeanProperty(MyClass.class, "property2");
        collector.addPropertyCreator(creator, false, properties);
        assertTrue(collector.hasPropertyBasedCreator());
        assertEquals(creator, collector.getPropertyBasedCreator());
        assertEquals(properties, collector.getPropertyBasedArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new StringCreator(), String.class);
        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new IntCreator(), int.class);
        collector.addIntCreator(creator, false);
        collector.addIntCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new LongCreator(), long.class);
        collector.addLongCreator(creator, false);
        collector.addLongCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new DoubleCreator(), double.class);
        collector.addDoubleCreator(creator, false);
        collector.addDoubleCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new BooleanCreator(), boolean.class);
        collector.addBooleanCreator(creator, false);
        collector.addBooleanCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new DelegatingCreator(), MyDelegate.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = new SettableBeanProperty(MyDelegate.class, "property");
        collector.addDelegatingCreator(creator, false, injectables);
        collector.addDelegatingCreator(creator, false, injectables);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicatePropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(new PropertyCreator(), MyClass.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        properties[0] = new SettableBeanProperty(MyClass.class, "property1");
        properties[1] = new SettableBeanProperty(MyClass.class, "property1");
        collector.addPropertyCreator(creator, false, properties);
    }

    @Test
    public void testConstructValueInstantiator() {
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(new StringCreator(), String.class);
        AnnotatedWithParams intCreator = new AnnotatedWithParams(new IntCreator(), int.class);
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(new BooleanCreator(), boolean.class);
        AnnotatedWithParams delegatingCreator = new AnnotatedWithParams(new DelegatingCreator(), MyDelegate.class);
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        delegateArgs[0] = new SettableBeanProperty(MyDelegate.class, "property");
        AnnotatedWithParams propertyBasedCreator = new AnnotatedWithParams(new PropertyCreator(), MyClass.class);
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        propertyBasedArgs[0] = new SettableBeanProperty(MyClass.class, "property1");
        propertyBasedArgs[1] = new SettableBeanProperty(MyClass.class, "property2");

        collector.setDefaultCreator(null);
        collector.addStringCreator(stringCreator, false);
        collector.addIntCreator(intCreator, false);
        collector.addBooleanCreator(booleanCreator, false);
        collector.addDelegatingCreator(delegatingCreator, false, delegateArgs);
        collector.addPropertyCreator(propertyBasedCreator, false, propertyBasedArgs);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);

        assertNotNull(instantiator);

        assertEquals(new StdValueInstantiator.ValueInstantiatorImpl(null, null, null), instantiator);

        assertEquals(stringCreator, instantiator.getStringCreator());
        assertEquals(intCreator, instantiator.getIntCreator());
        assertEquals(booleanCreator, instantiator.getBooleanCreator());
        assertEquals(delegateArgs, instantiator.getDelegateArgs());
        assertEquals(propertyBasedArgs, instantiator.getPropertyBasedArgs());
    }
}

class MyClass {
    // empty class
}

class MyDelegate {
    @JsonProperty("property")
    private String property;

    public MyDelegate(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}

class StringCreator {
    @JsonCreator
    public static String create(String str) {
        return str;
    }
}

class IntCreator {
    @JsonCreator
    public static int create(int i) {
        return i;
    }
}

class LongCreator {
    @JsonCreator
    public static long create(long l) {
        return l;
    }
}

class DoubleCreator {
    @JsonCreator
    public static double create(double d) {
        return d;
    }
}

class BooleanCreator {
    @JsonCreator
    public static boolean create(boolean b) {
        return b;
    }
}

class PropertyCreator {
    @JsonCreator
    public static MyClass create(@JsonProperty("property1") String property1,
            @JsonProperty("property2") int property2) {
        return new MyClass();
    }
}

class DelegatingCreator {
    @JsonCreator
    public static MyDelegate create(@JsonProperty("delegate") MyDelegate delegate) {
        return delegate;
    }
}