package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * Test cases for CreatorCollector class.
 */
public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(sampleBeanClass());
        MapperConfig<?> config = new MapperFactory().configure(new AnnotationIntrospector(), beanDesc);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        DeserializationConfig deserializationConfig = new DeserializationConfig(config);
        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);

        assertNotNull(instantiator);

        // Test default creator
        Object obj1 = instantiator.createUsingDefault(mockContext());
        assertInstanceOf(Collection.class, obj1);
        assertTrue(obj1 instanceof List);
        assertTrue(obj1 instanceof ArrayList);

        // Test String creator
        AnnotatedClass<StringCreator> stringCreatorClass = new AnnotatedClass(StringCreator.class);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringCreatorClass, null, null, null);
        collector.addStringCreator(stringCreator, true);
        Object obj2 = instantiator.createUsingDefault(mockContext());
        assertInstanceOf(String[].class, obj2);
        assertEquals("created by String creator", obj2);

        // Test int creator
        AnnotatedMethod<IntegerCreator> intCreatorMethod = new AnnotatedMethod(IntegerCreator.class, "create", Integer.TYPE);
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intCreatorMethod, null, null, null);
        collector.addIntCreator(intCreator, true);
        obj1 = instantiator.createUsingDefault(mockContext());
        assertEquals(42, obj1.intValue());

        // Test long creator
        AnnotatedConstructor<LongCreator> longCreatorConstructor = new AnnotatedConstructor(LongCreator.class, Long.TYPE);
        AnnotatedWithParams longCreator = new AnnotatedWithParams(longCreatorConstructor, null, null, null);
        collector.addLongCreator(longCreator, true);
        obj1 = instantiator.createUsingDefault(mockContext());
        assertEquals(42L, obj1.longValue());

        // Test double creator
        AnnotatedMethod<DoubleCreator> doubleCreatorMethod = new AnnotatedMethod(DoubleCreator.class, "create", Double.TYPE);
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(doubleCreatorMethod, null, null, null);
        collector.addDoubleCreator(doubleCreator, true);
        obj1 = instantiator.createUsingDefault(mockContext());
        assertEquals(3.14, obj1.doubleValue(), 0.01);

        // Test boolean creator
        AnnotatedMethod<BooleanCreator> booleanCreatorMethod = new AnnotatedMethod(BooleanCreator.class, "create", Boolean.TYPE);
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(booleanCreatorMethod, null, null, null);
        collector.addBooleanCreator(booleanCreator, true);
        obj1 = instantiator.createUsingDefault(mockContext());
        assertTrue(obj1.booleanValue());

        // Test delegate creator
        AnnotatedMethod<DelegateCreator> delegateCreatorMethod = new AnnotatedMethod(DelegateCreator.class, "create", Object.class);
        SettableBeanProperty delegateArg = new SettableBeanProperty("delegate", Object.class);
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateCreatorMethod, null, null, new AnnotatedParameter[0], null, new Object[]{delegateArg});
        collector.addDelegatingCreator(delegateCreator, true, new SettableBeanProperty[0]);
        obj1 = instantiator.createUsingDefault(mockContext());
        assertNotNull(obj1);

        // Test property-based creator
        AnnotatedMethod<PropertyCreator> propertyCreatorMethod = new AnnotatedMethod(PropertyCreator.class, "create", Object.class);
        SettableBeanProperty arg1 = new SettableBeanProperty("arg1", Integer.class);
        SettableBeanProperty arg2 = new SettableBeanProperty("arg2", Long.class);
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(propertyCreatorMethod, null, null, new AnnotatedParameter[0], new Object[]{arg1, arg2});
        collector.addPropertyCreator(propertyCreator, true, new SettableBeanProperty[]{arg1, arg2});
        obj1 = instantiator.createUsingDefault(mockContext());
        assertEquals(42, obj1.getFieldValue("arg1").intValue());
        assertEquals(43L, obj1.getFieldValue("arg2").longValue());

        // Test array delegate creator
        AnnotatedClass<ArrayDelegateCreator> arrayDelegateCreatorClass = new AnnotatedClass(ArrayDelegateCreator.class);
        AnnotatedWithParams arrayDelegateCreator = new AnnotatedWithParams(arrayDelegateCreatorClass, null, null, null);
        SettableBeanProperty[] arrayDelegateArgs = new SettableBeanProperty[]{new SettableBeanProperty("arg", String[].class)};
        collector.addDelegatingCreator(arrayDelegateCreator, true, arrayDelegateArgs);
        obj1 = instantiator.createUsingDefault(mockContext());
        assertInstanceOf(String[][].class, obj1);
    }

    private static BeanDescription sampleBeanClass() {
        AnnotatedClass<SampleBean> sampleBeanClass = new AnnotatedClass(SampleBean.class);
        AnnotatedMember[] members = sampleBeanClass.findMembers();
        BeanProperty[] properties = new BeanProperty[members.length];
        for (int i = 0; i < members.length; ++i) {
            AnnotatedMember member = members[i];
            if (member instanceof AnnotatedMethod) {
                AnnotatedMethod method = (AnnotatedMethod) member;
                properties[i] = new BeanProperty(method.getName(), method.getParameterType(0));
            } else if (member instanceof AnnotatedField) {
                AnnotatedField field = (AnnotatedField) member;
                properties[i] = new BeanProperty(field.getName(), field.getType());
            } else {
                throw new AssertionError("Unexpected member type: " + member.getClass());
            }
        }
        return new BeanDescription(sampleBeanClass, null, null, null, null, null, null, null);
    }

    private static DeserializationContext mockContext() {
        return new MinimalDeserializationContext();
    }

    static class SampleBean {
        private int arg1;
        private long arg2;
        private Object delegate;

        public SampleBean(int arg1, long arg2, Object delegate) {
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.delegate = delegate;
        }

        public int getArg1() {
            return arg1;
        }

        public long getArg2() {
            return arg2;
        }

        public Object getDelegate() {
            return delegate;
        }
    }

    static class StringCreator {
        @JsonCreator
        public static String[] create(@JsonProperty("values") String[] values) {
            return values;
        }
    }

    static class IntegerCreator {
        @JsonCreator
        public static Integer create(@JsonProperty("value") int value) {
            return value;
        }
    }

    static class LongCreator {
        @JsonCreator
        public static Long create(@JsonProperty("value") long value) {
            return value;
        }
    }

    static class DoubleCreator {
        @JsonCreator
        public static Double create(@JsonProperty("value") double value) {
            return value;
        }
    }

    static class BooleanCreator {
        @JsonCreator
        public static Boolean create(@JsonProperty("value") boolean value) {
            return value;
        }
    }

    static class DelegateCreator {
        @JsonCreator
        public static Object create(@JsonProperty("delegate") Object delegate) {
            return delegate;
        }
    }

    static class PropertyCreator {
        @JsonCreator
        public static Object create(@JsonProperty("arg1") int arg1, @JsonProperty("arg2") long arg2) {
            Object obj = new Object();
            BeanPropertyAccessor accessor = BeanPropertyAccessor.findPropertyAccessor(obj.getClass(), "arg1");
            accessor.set(obj, arg1);
            accessor = BeanPropertyAccessor.findPropertyAccessor(obj.getClass(), "arg2");
            accessor.set(obj, arg2);
            return obj;
        }
    }

    static class ArrayDelegateCreator {
        @JsonCreator
        public static Object create(@JsonProperty("arg") String[] arg) {
            return arg;
        }
    }
}