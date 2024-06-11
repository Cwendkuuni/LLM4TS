package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() {
        BeanDescription beanDesc = new BeanDescription(Foo.class);
        MapperConfig<Object> config = new MapperConfig.Builder().build();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        // Test default creator
        AnnotatedWithParams defaultCreator = new AnnotatedWithParams(Foo.class, Foo.class.getDeclaredConstructor());
        collector.setDefaultCreator(defaultCreator);

        // Test string creator
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(FooStringCreator.class, FooStringCreator.class.getDeclaredConstructor((Class<String[]>) ClassUtil.wrap(String[].class)));
        collector.addStringCreator(stringCreator, false);

        // Test int creator
        AnnotatedWithParams intCreator = new AnnotatedWithParams(FooIntCreator.class, FooIntCreator.class.getDeclaredConstructor((Class<Integer[]>) ClassUtil.wrap(Integer[].class)));
        collector.addIntCreator(intCreator, false);

        // Test long creator
        AnnotatedWithParams longCreator = new AnnotatedWithParams(FooLongCreator.class, FooLongCreator.class.getDeclaredConstructor((Class<Long[]>) ClassUtil.wrap(Long[].class)));
        collector.addLongCreator(longCreator, false);

        // Test double creator
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(FooDoubleCreator.class, FooDoubleCreator.class.getDeclaredConstructor((Class<Double[]>) ClassUtil.wrap(Double[].class)));
        collector.addDoubleCreator(doubleCreator, false);

        // Test boolean creator
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(FooBooleanCreator.class, FooBooleanCreator.class.getDeclaredConstructor((Class<Boolean[]>) ClassUtil.wrap(Boolean[].class)));
        collector.addBooleanCreator(booleanCreator, false);

        // Test delegating creator
        AnnotatedWithParams delegatingCreator = new AnnotatedWithParams(FooDelegatingCreator.class, FooDelegatingCreator.class.getDeclaredConstructor((Class<FooDelegate[]>) ClassUtil.wrap(FooDelegate[].class)));
        collector.addDelegatingCreator(delegatingCreator, false, new SettableBeanProperty[0]);

        // Test property-based creator
        AnnotatedWithParams propertyBasedCreator = new AnnotatedWithParams(FooPropertyCreator.class, FooPropertyCreator.class.getDeclaredConstructor(FooPropertyCreatorArg.class));
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[]{new SettableBeanProperty("prop1", String.class), new SettableBeanProperty("prop2", Integer.class)};
        collector.addPropertyCreator(propertyBasedCreator, false, propertyBasedArgs);

        // Test incomplete parameter
        AnnotatedParameter incompleteParameter = Foo.class.getDeclaredMethod("setFoo", Integer.class).getParameters()[0];
        collector.addIncompleteParameter(incompleteParameter);

        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(valueInstantiator);

        // Test default creator
        Object defaultInstance = valueInstantiator.createUsingDefault(new DeserializationContext());
        assertEquals(Foo.class, defaultInstance.getClass());

        // Test string creator
        Object[] stringArgs = {"foo"};
        Object stringInstance = valueInstantiator.createValueInstance(Foo.class, String[].class, stringArgs);
        assertEquals(Foo.class, stringInstance.getClass());
        assertEquals("foo", ((Foo) stringInstance).getString());

        // Test int creator
        Object[] intArgs = {1};
        Object intInstance = valueInstantiator.createValueInstance(Foo.class, Integer[].class, intArgs);
        assertEquals(Foo.class, intInstance.getClass());
        assertEquals(1, ((Foo) intInstance).getInt());

        // Test long creator
        Object[] longArgs = {1L};
        Object longInstance = valueInstantiator.createValueInstance(Foo.class, Long[].class, longArgs);
        assertEquals(Foo.class, longInstance.getClass());
        assertEquals(1L, ((Foo) longInstance).getLong());

        // Test double creator
        Object[] doubleArgs = {1.0};
        Object doubleInstance = valueInstantiator.createValueInstance(Foo.class, Double[].class, doubleArgs);
        assertEquals(Foo.class, doubleInstance.getClass());
        assertEquals(1.0, ((Foo) doubleInstance).getDouble());

        // Test boolean creator
        Object[] booleanArgs = {true};
        Object booleanInstance = valueInstantiator.createValueInstance(Foo.class, Boolean[].class, booleanArgs);
        assertEquals(Foo.class, booleanInstance.getClass());
        assertEquals(true, ((Foo) booleanInstance).isBoolean());

        // Test delegating creator
        Object[] delegatingArgs = {new FooDelegate("delegate")};
        Object delegatingInstance = valueInstantiator.createValueInstance(Foo.class, FooDelegate[].class, delegatingArgs);
        assertEquals(Foo.class, delegatingInstance.getClass());
        assertEquals("delegate", ((Foo) delegatingInstance).getDelegate().getName());

        // Test property-based creator
        Object[] propertyBasedArgs = {new FooPropertyCreatorArg("prop1", "prop1Value"), new FooPropertyCreatorArg("prop2", 1)};
        Object propertyBasedInstance = valueInstantiator.createValueInstance(Foo.class, Object[].class, propertyBasedArgs);
        assertEquals(Foo.class, propertyBasedInstance.getClass());
        assertEquals("prop1Value", ((Foo) propertyBasedInstance).getProp1());
        assertEquals(1, ((Foo) propertyBasedInstance).getIntProp2());

        // Test incomplete parameter
        Object incompleteInstance = valueInstantiator.createValueInstance(Foo.class, Object[].class, new Object[0]);
        assertEquals(Foo.class, incompleteInstance.getClass());
        assertNull(((Foo) incompleteInstance).getInt());
    }

    // Inner classes
    static class Foo {
        private int intValue;
        private long longValue;
        private double doubleValue;
        private boolean booleanValue;
        private String stringValue;
        private FooDelegate delegate;
        private String prop1;
        private int intProp2;

        @JsonCreator
        public Foo(@JsonProperty("int") int intValue, @JsonProperty("long") long longValue,
                @JsonProperty("double") double doubleValue, @JsonProperty("boolean") boolean booleanValue,
                @JsonProperty("string") String stringValue) {
            this.intValue = intValue;
            this.longValue = longValue;
            this.doubleValue = doubleValue;
            this.booleanValue = booleanValue;
            this.stringValue = stringValue;
        }

        public Foo() {
            // default constructor
        }

        public void setInt(int intValue) {
            this.intValue = intValue;
        }

        public int getInt() {
            return intValue;
        }

        public void setLong(long longValue) {
            this.longValue = longValue;
        }

        public long getLong() {
            return longValue;
        }

        public void setDouble(double doubleValue) {
            this.doubleValue = doubleValue;
        }

        public double getDouble() {
            return doubleValue;
        }

        public void setBoolean(boolean booleanValue) {
            this.booleanValue = booleanValue;
        }

        public boolean isBoolean() {
            return booleanValue;
        }

        public void setString(String stringValue) {
            this.stringValue = stringValue;
        }

        public String getString() {
            return stringValue;
        }

        public void setDelegate(FooDelegate delegate) {
            this.delegate = delegate;
        }

        public FooDelegate getDelegate() {
            return delegate;
        }

        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }

        public String getProp1() {
            return prop1;
        }

        public void setIntProp2(int intProp2) {
            this.intProp2 = intProp2;
        }

        public int getIntProp2() {
            return intProp2;
        }
    }

    static class FooDelegate {
        private String name;

        public FooDelegate(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class FooPropertyCreatorArg {
        private String name;
        private Object value;

        public FooPropertyCreatorArg(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    static class FooStringCreator {
        @JsonCreator
        public static Foo create(@JsonProperty("strings") String... strings) {
            Foo foo = new Foo();
            for (int i = 0; i < strings.length; i++) {
                foo.setString(strings[i]);
            }
            return foo;
        }
    }

    static class FooIntCreator {
        @JsonCreator
        public static Foo create(@JsonProperty("ints") int... ints) {
            Foo foo = new Foo();
            for (int i = 0; i < ints.length; i++) {
                foo.setInt(ints[i]);
            }
            return foo;
        }
    }

    static class FooLongCreator {
        @JsonCreator
        public static Foo create(@JsonProperty("longs") long... longs) {
            Foo foo = new Foo();
            for (int i = 0; i < longs.length; i++) {
                foo.setLong(longs[i]);
            }
            return foo;
        }
    }

    static class FooDoubleCreator {
        @JsonCreator
        public static Foo create(@JsonProperty("doubles") double... doubles) {
            Foo foo = new Foo();
            for (int i = 0; i < doubles.length; i++) {
                foo.setDouble(doubles[i]);
            }
            return foo;
        }
    }

    static class FooBooleanCreator {
        @JsonCreator
        public static Foo create(@JsonProperty("booleans") boolean... booleans) {
            Foo foo = new Foo();
            for (int i = 0; i < booleans.length; i++) {
                foo.setBoolean(booleans[i]);
            }
            return foo;
        }
    }
}