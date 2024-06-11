package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithMembers;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        // Test default constructor
        AnnotatedWithParams defaultConstructor = createTestDefaultConstructor(beanDesc);
        collector.setDefaultCreator(defaultConstructor);

        // Test String constructor
        AnnotatedWithParams stringConstructor = createTestStringConstructor(beanDesc);
        collector.addStringCreator(stringConstructor, true);

        // Test int constructor
        AnnotatedWithParams intConstructor = createTestIntConstructor(beanDesc);
        collector.addIntCreator(intConstructor, true);

        // Test long constructor
        AnnotatedWithParams longConstructor = createTestLongConstructor(beanDesc);
        collector.addLongCreator(longConstructor, true);

        // Test double constructor
        AnnotatedWithParams doubleConstructor = createTestDoubleConstructor(beanDesc);
        collector.addDoubleCreator(doubleConstructor, true);

        // Test boolean constructor
        AnnotatedWithParams booleanConstructor = createTestBooleanConstructor(beanDesc);
        collector.addBooleanCreator(booleanConstructor, true);

        // Test delegate constructor
        AnnotatedWithParams delegateConstructor = createTestDelegateConstructor(beanDesc);
        collector.addDelegatingCreator(delegateConstructor, null, null);

        // Test property-based constructor
        AnnotatedWithParams propertyConstructor = createTestPropertyConstructor(beanDesc);
        collector.addPropertyCreator(propertyConstructor, true, null);

        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Test instantiator with default constructor
        Class<? extends TestBean> clazz = TestBean.class;
        TestBean obj1 = instantiator.createValueInstance(clazz, null);
        assertNotNull(obj1);

        // Test instantiator with String constructor
        String str = "test";
        TestBean obj2 = instantiator.createValueInstance(clazz, new Object[] { str });
        assertEquals(str, obj2.getStringProperty());

        // Test instantiator with int constructor
        int intVal = 123;
        TestBean obj3 = instantiator.createValueInstance(clazz, new Object[] { intVal });
        assertEquals(intVal, obj3.getIntProperty());

        // Test instantiator with long constructor
        long longVal = 456;
        TestBean obj4 = instaniator.createValueInstance(clazz, new Object[] { longVal });
        assertEquals(longVal, obj4.getLongProperty());

        // Test instantiator with double constructor
        double doubleVal = 789.123;
        TestBean obj5 = instantiator.createValueInstance(clazz, new Object[] { doubleVal });
        assertEquals(doubleVal, obj5.getDoubleProperty(), 0.0001);

        // Test instantiator with boolean constructor
        boolean booleanVal = true;
        TestBean obj6 = instantiator.createValueInstance(clazz, new Object[] { booleanVal });
        assertEquals(booleanVal, obj6.isBooleanProperty());

        // Test instantiator with delegate constructor
        TestDelegate delegate = new TestDelegate();
        TestBean obj7 = instantiator.createValueInstance(clazz, new Object[] { delegate });
        assertEquals(delegate, obj7.getDelegate());

        // Test instantiator with property-based constructor
        TestBeanProperty prop = new TestBeanProperty();
        TestBean obj8 = instantiator.createValueInstance(clazz, new Object[] { prop });
        assertEquals(prop.getValue(), obj8.getProperty());
    }

    private BeanDescription createTestBeanDescription() {
        return new BeanDescription(TestBean.class, null, null);
    }

    private AnnotatedWithParams createTestDefaultConstructor(BeanDescription beanDesc) {
        Method defaultMethod = TestBean.class.getDeclaredMethod("defaultConstructor");
        return new AnnotatedWithParams(defaultMethod, new AnnotatedWithMembers(beanDesc.findConstractor(defaultMethod)));
    }

    private AnnotatedWithParams createTestStringConstructor(BeanDescription beanDesc) {
        Method stringMethod = TestBean.class.getDeclaredMethod("stringConstructor", String.class);
        return new AnnotatedWithParams(stringMethod, new AnnotatedWithMembers(beanDesc.findConstructor(stringMethod)));
    }

    private AnnotatedWithParams createTestIntConstructor(BeanDescription beanDesc) {
        Method intMethod = TestBean.class.getDeclaredMethod("intConstructor", int.class);
        return new AnnotatedWithParams(intMethod, new AnnotatedWithMembers(beanDesc.findConstructor(intMethod)));
    }

    private AnnotatedWithParams createTestLongConstructor(BeanDescription beanDesc) {
        Method longMethod = TestBean.class.getDeclaredMethod("longConstructor", long.class);
        return new AnnotatedWithParams(longMethod, new AnnotatedWithMembers(beanDesc.findConstructor(longMethod)));
    }

    private AnnotatedWithParams createTestDoubleConstructor(BeanDescription beanDesc) {
        Method doubleMethod = TestBean.class.getDeclaredMethod("doubleConstructor", double.class);
        return new AnnotatedWithParams(doubleMethod, new AnnotatedWithMembers(beanDesc.findConstructor(doubleMethod)));
    }

    private AnnotatedWithParams createTestBooleanConstructor(BeanDescription beanDesc) {
        Method booleanMethod = TestBean.class.getDeclaredMethod("booleanConstructor", boolean.class);
        return new AnnotatedWithParams(booleanMethod, new AnnotatedWithMembers(beanDesc.findConstructor(booleanMethod)));
    }

    private AnnotatedWithParams createTestDelegateConstructor(BeanDescription beanDesc) {
        Class<?> delegateClass = TestDelegate.class;
        Method delegateConstructor = TestBean.class.getDeclaredMethod("delegateConstructor", delegateClass);
        return new AnnotatedWithParams(delegateConstructor, new AnnotatedWithMembers(beanDesc.findConstructor(delegateConstructor)));
    }

    private AnnotatedWithParams createTestPropertyConstructor(BeanDescription beanDesc) {
        PropertyDescriptor propertyDesc = beanDesc.findPropertyDescriptor("property");
        Method propertyMethod = propertyDesc.getWriteMethod();
        AnnotatedField propertyField = propertyDesc.getAnnotations().get(JsonProperty.class.getName());
        return new AnnotatedWithParams(propertyMethod, new AnnotatedWithMembers(beanDesc.findMethod(propertyMethod))) {
            @Override
            public AnnotatedParameter[] getParameters() {
                return new AnnotatedParameter[] { new AnnotatedParameter(propertyField) };
            }
        };
    }

    private static class TestBean {
        @JsonProperty
        private String stringProperty;

        @JsonProperty
        private int intProperty;

        @JsonProperty
        private long longProperty;

        @JsonProperty
        private double doubleProperty;

        @JsonProperty
        private boolean booleanProperty;

        @JsonProperty
        private TestDelegate delegate;

        @JsonProperty
        private TestBeanProperty property;

        TestBean() {
            // default constructor
        }

        TestBean(String stringArg) {
            this.stringProperty = stringArg;
        }

        TestBean(int intArg) {
            this.intProperty = intArg;
        }

        TestBean(long longArg) {
            this.longProperty = longArg;
        }

        TestBean(double doubleArg) {
            this.doubleProperty = doubleArg;
        }

        TestBean(boolean booleanArg) {
            this.booleanProperty = booleanArg;
        }

        TestBean(TestDelegate delegateArg) {
            this.delegate = delegateArg;
        }

        TestBean(TestBeanProperty propertyArg) {
            this.property = propertyArg;
        }

        @JsonDeserialize(using = TestBeanStringConverter.class)
        public String getStringProperty() {
            return stringProperty;
        }

        public int getIntProperty() {
            return intProperty;
        }

        public long getLongProperty() {
            return longProperty;
        }

        public double getDoubleProperty() {
            return doubleProperty;
        }

        public boolean isBooleanProperty() {
            return booleanProperty;
        }

        public TestDelegate getDelegate() {
            return delegate;
        }

        public TestBeanProperty getProperty() {
            return property;
        }

        static class TestBeanStringConverter extends StdConverter<String, TestBean> {
            @Override
            public TestBean convert(String value, DeserializationContext ctxt) throws IOException {
                return new TestBean(value);
            }
        }
    }

    private static class TestBeanProperty {
        private Object value;

        public TestBeanProperty(Object valueArg) {
            this.value = valueArg;
        }

        public Object getValue() {
            return value;
        }
    }

    private static class TestDelegate {
        // empty
    }
}