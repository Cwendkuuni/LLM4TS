package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testInstanceCreation() throws Exception {
        MapperConfig<Object> config = new MapperFactory(Object.class).getConfig();

        BeanDescription beanDesc = new BeanDescription(Object.class, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        // default creator
        AnnotatedMethod defaultCreator = new AnnotatedMethod(Object.class, "getDefaultInstance", (ClassType) null);
        collector.setDefaultCreator(new AnnotatedWithParams(defaultCreator));

        // int creator
        AnnotatedMethod intCreator = new AnnotatedMethod(Integer.class, "<init>", new Class[]{int.class});
        collector.addIntCreator(new AnnotatedWithParams(intCreator), true);

        // ValueInstantiator
        ValueInstantiator inst = collector.constructValueInstantiator(config);

        Object created = inst.createValueInstance(null);
        assertNotNull(created);

        // check that int creator was used
        int i = 42;
        Object createdWithInt = inst.createValueInstance(i.getClass(), i);
        assertNotNull(createdWithInt);
        assertSame(i, createdWithInt);
    }

    @Test
    public void testStringCreator() throws Exception {
        MapperConfig<Object> config = new MapperFactory(Object.class).getConfig();

        BeanDescription beanDesc = new BeanDescription(String.class, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        // string creator
        AnnotatedMethod stringCreator = new AnnotatedMethod(String.class, "<init>", new Class[]{String.class});
        collector.addStringCreator(new AnnotatedWithParams(stringCreator), true);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        String created = (String) inst.createValueInstance(null);
        assertNull(created);

        String createdWithString = (String) inst.createValueInstance("foo");
        assertNotNull(createdWithString);
        assertEquals("foo", createdWithString);
    }

    @Test
    public void testNestedCreator() throws Exception {
        MapperConfig<Object> config = new MapperFactory(Object.class).getConfig();

        BeanDescription beanDesc = new BeanDescription(Nested.class, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        // int creator for Nested.inner
        AnnotatedMethod intCreator = new AnnotatedMethod(Nested.Inner.class, "<init>", new Class[]{int.class});
        collector.addIntCreator(new AnnotatedWithParams(intCreator), true);

        // string creator for Nested
        AnnotatedMethod stringCreator = new AnnotatedMethod(Nested.class, "<init>", new Class[0]);
        collector.addStringCreator(new AnnotatedWithParams(stringCreator), true);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        Nested created = (Nested) inst.createValueInstance(null);
        assertNotNull(created);

        Nested createdWithString = (Nested) inst.createValueInstance(String.class, "foo");
        assertNotNull(createdWithString);
        assertEquals("foo", createdWithString.inner);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConflictingCreators() throws Exception {
        MapperConfig<Object> config = new MapperFactory(Object.class).getConfig();

        BeanDescription beanDesc = new BeanDescription(Object.class, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        // int creator
        AnnotatedMethod intCreator = new AnnotatedMethod(Integer.class, "<init>", new Class[]{int.class});
        collector.addIntCreator(new AnnotatedWithParams(intCreator), true);

        // another int creator
        AnnotatedMethod intCreator2 = new AnnotatedMethod(Integer.class, "<init>", new Class[]{int.class});
        collector.addIntCreator(new AnnotatedWithParams(intCreator2), true);

        ValueInstantiator inst = collector.constructValueInstantiator(config);
    }

    static class Nested {
        @JsonCreator
        public Nested() {
            inner = new Nested.Inner(0);
        }

        public Nested(int i) {
            inner = new Nested.Inner(i);
        }

        public Nested.Inner inner;

        public static class Inner {
            final int i;

            public Inner(int i) {
                this.i = i;
            }
        }
    }
}