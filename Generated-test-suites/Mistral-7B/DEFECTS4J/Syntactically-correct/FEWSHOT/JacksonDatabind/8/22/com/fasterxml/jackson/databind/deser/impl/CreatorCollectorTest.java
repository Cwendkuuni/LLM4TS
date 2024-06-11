package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonCreator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithMembers;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private static class TestBean {
        private static int defaultInt;
        private static String defaultString;
        private static boolean defaultBoolean;
        private String string;
        private int intVal;
        private long longVal;
        private double doubleVal;
        private boolean booleanVal;
        private CreatorCollectorDelegate delegate;
        private CreatorCollectorProperties properties[];

        @JsonCreator
        public TestBean(String string, int intVal, long longVal, double doubleVal, boolean booleanVal,
                CreatorCollectorDelegate delegate, CreatorCollectorProperties properties[]) {
            this.string = string;
            this.intVal = intVal;
            this.longVal = longVal;
            this.doubleVal = doubleVal;
            this.booleanVal = booleanVal;
            this.delegate = delegate;
            this.properties = properties;
        }

        public TestBean() {
            this(null, 0, 0, 0.0f, false, null, null);
        }

        @JsonCreator
        public static TestBean createFromString(String str) {
            return new TestBean(str);
        }

        @JsonCreator
        public static TestBean createFromInt(int intVal) {
            return new TestBean(null, intVal, 0, 0.0f, false, null, null);
        }

        @JsonCreator
        public static TestBean createFromLong(long longVal) {
            return new TestBean(null, 0, longVal, 0.0f, false, null, null);
        }

        @JsonCreator
        public static TestBean createFromDouble(double doubleVal) {
            return new TestBean(null, 0, 0, doubleVal, false, null, null);
        }

        @JsonCreator
        public static TestBean createFromBoolean(boolean booleanVal) {
            return new TestBean(null, 0, 0, 0.0f, booleanVal, null, null);
        }

        @JsonCreator
        public static TestBean createFromDelegate(CreatorCollectorDelegate delegate,
                CreatorCollectorProperties properties[]) {
            return new TestBean(null, 0, 0, 0.0f, false, delegate, properties);
        }

        @JsonCreator
        public static TestBean createFromProperties(CreatorCollectorProperties properties) {
            return new TestBean(null, 0, 0, 0.0f, false, null, properties);
        }
    }

    @Test
    public void testDefaultCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        collector.setDefaultCreator(null); // default is null
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        TestBean obj = inst.createValueInstance(null);
        assertNotNull(obj);
    }

    @Test
    public void testStringCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod stringCreator = new AnnotatedMethod(TestBean.class.getMethod("createFromString"), TestBean.class);
        collector.addStringCreator(stringCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        TestBean obj = inst.createValueInstance(new String[]{"test"});
        assertEquals("test", obj.string);
        assertNull(obj.intVal);
        assertNull(obj.longVal);
        assertNull(obj.doubleVal);
        assertNull(obj.booleanVal);
        assertNull(obj.delegate);
        assertNull(obj.properties);
    }

    @Test
    public void testIntCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod intCreator = new AnnotatedMethod(TestBean.class.getMethod("createFromInt"), TestBean.class);
        collector.addIntCreator(intCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        TestBean obj = inst.createValueInstance(new Integer(42));
        assertEquals(42, obj.intVal);
        assertNull(obj.string);
        assertNull(obj.longVal);
        assertNull(obj.doubleVal);
        assertNull(obj.booleanVal);
        assertNull(obj.delegate);
        assertNull(obj.properties);
    }

    @Test
    public void testLongCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod longCreator = new AnnotatedMethod(TestBean.class.getMethod("createFromLong"), TestBean.class);
        collector.addLongCreator(longCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        TestBean obj = inst.createValueInstance(new Long(42L));
        assertEquals(42L, obj.longVal);
        assertNull(obj.string);
        assertNull(obj.intVal);
        assertNull(obj.doubleVal);
        assertNull(obj.booleanVal);
        assertNull(obj.delegate);
        assertNull(obj.properties);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod doubleCreator = new AnnotatedMethod(TestBean.class.getMethod("createFromDouble"), TestBean.class);
        collector.addDoubleCreator(doubleCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        TestBean obj = inst.createValueInstance(new Double(42.0));
        assertEquals(42.0, obj.doubleVal, 0.0);
        assertNull(obj.string);
        assertNull(obj.intVal);
        assertNull(obj.longVal);
        assertNull(obj.booleanVal);
        assertNull(obj.delegate);
        assertNull(obj.properties);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod booleanCreator = new AnnotatedMethod(TestBean.class.getMethod("createFromBoolean"), TestBean.class);
        collector.addBooleanCreator(booleanCreator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        TestBean obj = inst.createValueInstance(new Boolean(true));
        assertEquals(true, obj.booleanVal);
        assertNull(obj.string);
        assertNull(obj.intVal);
        assertNull(obj.longVal);
        assertNull(obj.doubleVal);
        assertNull(obj.delegate);
        assertNull(obj.properties);
    }

    @Test
    public void testDelegateCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedClass delegateClass = new AnnotatedClass(CreatorCollectorDelegate.class);
        AnnotatedMethod delegateConstructor = new AnnotatedMethod(delegateClass.getDeclaredMethod("newInstance"), CreatorCollectorDelegate.class);

        AnnotatedMethod delegateCreator = new AnnotatedMethod(TestBean.class.getMethod("createFromDelegate"), TestBean.class);
        CreatorProperty[] injectables = new CreatorProperty[] { new AnnotatedParameter(delegateConstructor, 0) };
        collector.addDelegatingCreator(delegateCreator, true, injectables);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        TestBean obj = inst.createValueInstance(new Object[] { new CreatorCollectorDelegate() });
        assertEquals(obj.delegate, new CreatorCollectorDelegate());
        assertNull(obj.string);
        assertNull(obj.intVal);
        assertNull(obj.longVal);
        assertNull(obj.doubleVal);
        assertNull(obj.booleanVal);
        assertArrayEquals(new CreatorProperty[] { injectables[0] }, obj.properties);
    }

    @Test
    public void testPropertiesCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod propertiesCreator = new AnnotatedMethod(TestBean.class.getMethod("createFromProperties"), TestBean.class);
        CreatorProperty[] properties = new CreatorProperty[] {
                new AnnotatedParameter(propertiesCreator, 0),
                new AnnotatedParameter(propertiesCreator, 1),
                new AnnotatedParameter(propertiesCreator, 2)
        };
        collector.addPropertyCreator(propertiesCreator, true, properties);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        TestBean obj = inst.createValueInstance(new Object[] {
                new StringProperty("string1"),
                new IntProperty(42),
                new LongProperty(42L)
        });
        assertEquals("string1", obj.string);
        assertEquals(42, obj.intVal);
        assertEquals(42L, obj.longVal);
        assertNull(obj.doubleVal);
        assertNull(obj.booleanVal);
        assertArrayEquals(properties, obj.properties);
    }

    // Helper classes
    private static class StringProperty extends AnnotatedParameter {
        public StringProperty(String name) {
            super("", new BeanPropertyDefinition(TestBean.class, name), null);
        }
    }

    private static class IntProperty extends AnnotatedParameter {
        public IntProperty(int value) {
            super("", null, new Int(value));
        }
    }

    private static class LongProperty extends AnnotatedParameter {
        public LongProperty(long value) {
            super("", null, new Long(value));
        }
    }

    @SuppressWarnings("unchecked")
    private static class CreatorCollectorDelegate {
        public static CreatorCollectorDelegate newInstance() {
            return new CreatorCollectorDelegate();
        }
    }
}