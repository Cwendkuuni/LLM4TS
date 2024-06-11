package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for {@link CreatorCollector}
 */
public class CreatorCollectorTest {

    private static class TestBean {
        public TestBean() {}

        @SuppressWarnings("unused")
        public TestBean(String s) {}

        public TestBean(int i) {}

        public TestBean(long l) {}

        public TestBean(double d) {}

        public TestBean(boolean b) {}

        public TestBean(CreatorProperty cp) {}

        public TestBean(CreatorProperty[] cps) {}

        public TestBean(CreatorProperty[] cps, CreatorProperty incomplete) {}

        public TestBean delegateMethod(CreatorCollector collector) {
            return collector.newInstance(this.getClass(), null);
        }

        @SuppressWarnings("unused")
        public TestBean stringCreator(CreatorCollector collector) {
            return collector.constructValue(new MethodStringCreator(), null);
        }

        @SuppressWarnings("unused")
        public TestBean intCreator(CreatorCollector collector) {
            return collector.constructValue(new MethodIntCreator(), null);
        }

        @SuppressWarnings("unused")
        public TestBean longCreator(CreatorCollector collector) {
            return collector.constructValue(new MethodLongCreator(), null);
        }

        @SuppressWarnings("unused")
        public TestBean doubleCreator(CreatorCollector collector) {
            return collector.constructValue(new MethodDoubleCreator(), null);
        }

        @SuppressWarnings("unused")
        public TestBean booleanCreator(CreatorCollector collector) {
            return collector.constructValue(new MethodBooleanCreator(), null);
        }

        @SuppressWarnings("unused")
        public TestBean propertyCreator(CreatorCollector collector) {
            return collector.constructValue(new MethodPropertyCreator(), null);
        }

        @SuppressWarnings("unused")
        public TestBean incompleteParameter(CreatorCollector collector) {
            return collector.constructValue(new MethodIncompleteParameter(), null);
        }

        @SuppressWarnings("unused")
        public TestBean delegateWithArgs(CreatorCollector collector, CreatorProperty[] args) {
            return collector.constructValue(new MethodDelegateWithArgs(), args);
        }

        @SuppressWarnings("unused")
        public TestBean propertyBased(CreatorCollector collector, CreatorProperty[] properties) {
            return collector.constructValue(new MethodPropertyBased(), properties);
        }

        @SuppressWarnings("unused")
        public TestBean propertyBasedWithIncomplete(CreatorCollector collector, CreatorProperty incomplete) {
            return collector.constructValue(new MethodPropertyBasedWithIncomplete(), incomplete);
        }
    }

    private static class MethodStringCreator extends AbstractCreator {
        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return new TestBean(value);
        }
    }

    private static class MethodIntCreator extends AbstractCreator {
        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return new TestBean(value);
        }
    }

    private static class MethodLongCreator extends AbstractCreator {
        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return new TestBean(value);
        }
    }

    private static class MethodDoubleCreator extends AbstractCreator {
        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return new TestBean(value);
        }
    }

    private static class MethodBooleanCreator extends AbstractCreator {
        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return new TestBean(value);
        }
    }

    private static class MethodPropertyCreator extends AbstractCreator {
        @Override
        public Object createFromPropertyModel(DeserializationContext ctxt, PropertyModel propertyModel)
                throws IOException {
            return new TestBean(propertyModel.getRawValue());
        }
    }

    private static class MethodIncompleteParameter extends AnnotatedMember {
        @Override
        public JavaType getType() {
            return null;
        }

        @Override
        public Annotation[] getParameterAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Method getMember() {
            throw new UnsupportedOperationException();
        }
    }

    private static class MethodDelegateWithArgs extends AbstractCreator {
        @Override
        public Object createFromArguments(DeserializationContext ctxt, AnnotatedParameter[] params) throws IOException {
            return new TestBean();
        }
    }

    private static class MethodPropertyBased extends AbstractCreator {
        @Override
        public Object createFromProperties(DeserializationContext ctxt, CreatorProperty[] properties) throws IOException {
            return new TestBean();
        }
    }

    private static class MethodPropertyBasedWithIncomplete extends AbstractCreator {
        @Override
        public Object createFromProperties(DeserializationContext ctxt, CreatorProperty incomplete) throws IOException {
            return new TestBean();
        }
    }

    @Test
    public void testDefaultCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instance = valueInstantiator.createValueInstance(null);
        assertEquals(new TestBean(), instance);
    }

    @Test
    public void testStringCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        MethodStringCreator stringCreator = new MethodStringCreator();
        collector.addStringCreator(stringCreator, false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instance = valueInstantiator.createValueInstance(new String[]{"foo"});
        assertEquals(new TestBean("foo"), instance);
    }

    @Test
    public void testIntCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        MethodIntCreator intCreator = new MethodIntCreator();
        collector.addIntCreator(intCreator, false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instance = valueInstantiator.createValueInstance(new Integer(1));
        assertEquals(new TestBean(1), instance);
    }

    @Test
    public void testLongCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        MethodLongCreator longCreator = new MethodLongCreator();
        collector.addLongCreator(longCreator, false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instance = valueInstantiator.createValueInstance(new Long(1L));
        assertEquals(new TestBean(1L), instance);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        MethodDoubleCreator doubleCreator = new MethodDoubleCreator();
        collector.addDoubleCreator(doubleCreator, false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instance = valueInstantiator.createValueInstance(new Double(1.0));
        assertEquals(new TestBean(1.0), instance);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        MethodBooleanCreator booleanCreator = new MethodBooleanCreator();
        collector.addBooleanCreator(booleanCreator, false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instance = valueInstantiator.createValueInstance(new Boolean(true));
        assertEquals(new TestBean(true), instance);
    }

    @Test
    public void testDelegateCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        MethodDelegateWithArgs delegateCreator = new MethodDelegateWithArgs();
        CreatorProperty[] args = new CreatorProperty[0];
        collector.addDelegatingCreator(delegateCreator, false, args);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instance = valueInstantiator.createValueInstance(null);
        assertEquals(new TestBean(), instance);
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        MethodPropertyBased propertyCreator = new MethodPropertyBased();
        CreatorProperty[] properties = new CreatorProperty[0];
        collector.addPropertyCreator(propertyCreator, false, properties);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instance = valueInstantiator.createValueInstance(null);
        assertEquals(new TestBean(), instance);
    }
}