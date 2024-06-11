package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class StdValueInstantiatorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static class TestBean {
        @JsonDeserialize(using = StdValueInstantiator.class)
        private String stringField;
        @JsonDeserialize(using = StdValueInstantiator.class)
        private int intField;
        @JsonDeserialize(using = StdValueInstantiator.class)
        private long longField;
        @JsonDeserialize(using = StdValueInstantiator.class)
        private double doubleField;
        @JsonDeserialize(using = StdValueInstantiator.class)
        private boolean booleanField;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public int getIntField() {
            return intField;
        }

        public void setIntField(int intField) {
            this.intField = intField;
        }

        public long getLongField() {
            return longField;
        }

        public void setLongField(long longField) {
            this.longField = longField;
        }

        public double getDoubleField() {
            return doubleField;
        }

        public void setDoubleField(double doubleField) {
            this.doubleField = doubleField;
        }

        public boolean isBooleanField() {
            return booleanField;
        }

        public void setBooleanField(boolean booleanField) {
            this.booleanField = booleanField;
        }
    }

    private StdValueInstantiator stdValueInstantiator;

    @Before
    public void setup() throws Exception {
        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), TestBean.class);
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        TestBean testBean = stdValueInstantiator.createUsingDefault(mapper.getDeserializationContext());
        assertNotNull(testBean);
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        TestBean testBean = new TestBean();
        final AnnotatedConstructor<?> constructor = TestBean.class.getDeclaredConstructor(String.class);
        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), TestBean.class) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return new AnnotatedWithParams(constructor);
            }
        };
        testBean = stdValueInstantiator.createUsingDelegate(mapper.getDeserializationContext(), testBean);
        assertNotNull(testBean);
        assertEquals("default", testBean.getStringField());
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        TestBean[] testBeans = new TestBean[2];
        final AnnotatedConstructor<?> constructor = TestBean.class.getDeclaredConstructor(String.class);
        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), TestBean[].class) {
            @Override
            public AnnotatedWithParams getArrayDelegateCreator() {
                return new AnnotatedWithParams(constructor);
            }
        };
        testBeans = stdValueInstantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), new Object[2]);
        assertArrayEquals(new TestBean[]{new TestBean(), new TestBean()}, testBeans);
    }

    @Test
    public void testCreateUsingFromString() throws Exception {
        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), String.class);
        String stringValue = stdValueInstantiator.createFromString(mapper.getDeserializationContext(), "hello");
        assertEquals("hello", stringValue);
    }

    @Test
    public void testCreateUsingFromInt() throws Exception {
        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), Integer.class);
        int intValue = stdValueInstantiator.createFromInt(mapper.getDeserializationContext(), 1);
        assertEquals(1, intValue);
    }

    @Test
    public void testCreateUsingFromLong() throws Exception {
        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), Long.class);
        long longValue = stdValueInstantiator.createFromLong(mapper.getDeserializationContext(), 1L);
        assertEquals(1L, longValue);
    }

    @Test
    public void testCreateUsingFromDouble() throws Exception {
        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), Double.class);
        double doubleValue = stdValueInstantiator.createFromDouble(mapper.getDeserializationContext(), 1.0);
        assertEquals(1.0, doubleValue, 0.0);
    }

    @Test
    public void testCreateUsingFromBoolean() throws Exception {
        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), Boolean.class);
        boolean booleanValue = stdValueInstantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        assertTrue(booleanValue);
    }

    @Test
    public void testCreateUsingDelegateWithArgs() throws Exception {
        TestBean testBean = new TestBean();
        TestBean testBean2 = new TestBean();
        TestBean testBean3 = new TestBean();

        final BeanPropertyDefinition prop1 = TestBean.class.findProperty("stringField");
        final BeanPropertyDefinition prop2 = TestBean.class.findProperty("intField");
        final BeanPropertyDefinition prop3 = TestBean.class.findProperty("longField");
        final BeanPropertyDefinition prop4 = TestBean.class.findProperty("doubleField");
        final BeanPropertyDefinition prop5 = TestBean.class.findProperty("booleanField");

        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), TestBean.class) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return new AnnotatedWithParams(TestBean.class.getDeclaredConstructor(
                        prop1, prop2, prop3, prop4, prop5));
            }

            @Override
            public SettableBeanProperty[] getDelegateArguments() {
                return new SettableBeanProperty[]{prop1, prop2, prop3, prop4, prop5};
            }
        };

        testBean = stdValueInstantiator.createUsingDelegate(mapper.getDeserializationContext(), testBean, testBean2, testBean3);
        assertNotNull(testBean);
        assertEquals("default", testBean.getStringField());
        assertEquals(1, testBean.getIntField());
        assertEquals(1L, testBean.getLongField());
        assertEquals(1.0, testBean.getDoubleField(), 0.0);
        assertTrue(testBean.isBooleanField());

        testBean2 = stdValueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{testBean, 1, 1L, 1.0, true});
        assertNotNull(testBean2);
        assertEquals("default", testBean2.getStringField());
        assertEquals(1, testBean2.getIntField());
        assertEquals(1L, testBean2.getLongField());
        assertEquals(1.0, testBean2.getDoubleField(), 0.0);
        assertTrue(testBean2.isBooleanField());
    }

    @Test
    public void testGettersAndSetters() throws Exception {
        TestBean testBean = new TestBean();
        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), TestBean.class);

        testBean = stdValueInstantiator.createUsingDefault(mapper.getDeserializationContext());
        assertNotNull(testBean);
        assertNull(testBean.getStringField());
        assertEquals(0, testBean.getIntField());
        assertEquals(0L, testBean.getLongField());
        assertEquals(0.0, testBean.getDoubleField(), 0.0);
        assertFalse(testBean.isBooleanField());

        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), TestBean.class);
        testBean.setStringField("hello");
        testBean.setIntField(10);
        testBean.setLongField(10L);
        testBean.setDoubleField(10.5);
        testBean.setBooleanField(true);

        String stringValue = stdValueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{testBean});
        assertEquals("hello", stringValue);

        int intValue = stdValueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{testBean, 5});
        assertEquals(15, intValue);

        long longValue = stdValueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{testBean, 5L});
        assertEquals(15L, longValue);

        double doubleValue = stdValueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{testBean, 5.5});
        assertEquals(15.5, doubleValue, 0.0);

        boolean booleanValue = stdValueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{testBean, true});
        assertTrue(booleanValue);
    }
}