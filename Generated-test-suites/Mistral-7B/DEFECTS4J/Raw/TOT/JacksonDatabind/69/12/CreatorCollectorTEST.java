import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonCreatorProperty;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Test for {@link CreatorCollector}.
 */
public class CreatorCollectorTest {

    private ObjectMapper mapper;
    private DeserializationConfig config;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        config = mapper.getDeserializationConfig();
    }

    @Test
    public void testDefaultCreator() throws Exception {
        final BeanDescription beanDesc = mapper.introspect(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        // Test with no arguments constructor
        TestBean obj1 = inst.createValueInstance(null);
        assertEquals(0, obj1.getIntValue());
        assertEquals(false, obj1.isBooleanValue());

        // Test with no arguments factory method
        TestBean obj2 = inst.createValueInstance(TestFactory.class);
        assertEquals(10, obj2.getIntValue());
        assertEquals(true, obj2.isBooleanValue());
    }

    @Test
    public void testStringCreator() throws Exception {
        final BeanDescription beanDesc = mapper.introspect(TestStringBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedMethod creator = beanDesc.findCreator(String.class);
        collector.addStringCreator(creator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        TestStringBean obj1 = inst.createValueInstance(null);
        assertEquals("default", obj1.getStringValue());

        TestStringBean obj2 = inst.createValueInstance("test");
        assertEquals("test", obj2.getStringValue());
    }

    @Test
    public void testIntCreator() throws Exception {
        final BeanDescription beanDesc = mapper.introspect(TestIntBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedMethod creator = beanDesc.findCreator(int.class);
        collector.addIntCreator(creator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        TestIntBean obj1 = inst.createValueInstance(null);
        assertEquals(42, obj1.getIntValue());

        TestIntBean obj2 = inst.createValueInstance(43);
        assertEquals(43, obj2.getIntValue());
    }

    @Test
    public void testLongCreator() throws Exception {
        final BeanDescription beanDesc = mapper.introspect(TestLongBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedMethod creator = beanDesc.findCreator(long.class);
        collector.addLongCreator(creator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        TestLongBean obj1 = inst.createValueInstance(null);
        assertEquals(1L, obj1.getLongValue());

        TestLongBean obj2 = inst.createValueInstance(123L);
        assertEquals(123L, obj2.getLongValue());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        final BeanDescription beanDesc = mapper.introspect(TestDoubleBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedMethod creator = beanDesc.findCreator(double.class);
        collector.addDoubleCreator(creator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        TestDoubleBean obj1 = inst.createValueInstance(null);
        assertEquals(3.14, obj1.getDoubleValue(), 0.0);

        TestDoubleBean obj2 = inst.createValueInstance(3.14);
        assertEquals(3.14, obj2.getDoubleValue(), 0.0);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        final BeanDescription beanDesc = mapper.introspect(TestBooleanBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedMethod creator = beanDesc.findCreator(boolean.class);
        collector.addBooleanCreator(creator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        TestBooleanBean obj1 = inst.createValueInstance(null);
        assertTrue(obj1.isBooleanValue());

        TestBooleanBean obj2 = inst.createValueInstance(true);
        assertTrue(obj2.isBooleanValue());
    }

    @Test
    public void testDelegateCreator() throws Exception {
        final BeanDescription beanDesc = mapper.introspect(TestDelegateBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedMethod creator = beanDesc.findCreator(TestDelegate.class);
        collector.addDelegatingCreator(creator, true, null);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        TestDelegateBean obj1 = inst.createValueInstance(null);
        assertNull(obj1.getDelegate());

        TestDelegate obj2 = new TestDelegate("test");
        TestDelegateBean obj3 = inst.createValueInstance(obj2);
        assertEquals(obj2, obj3.getDelegate());
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        final BeanDescription beanDesc = mapper.introspect(TestArrayDelegateBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedMethod creator = beanDesc.findCreator(TestArrayDelegate.class);
        collector.addDelegatingCreator(creator, true, new SettableBeanProperty[]{new TestBeanProperty()});

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        TestArrayDelegateBean obj1 = inst.createValueInstance(null);
        assertNull(obj1.getDelegate());

        TestArrayDelegate obj2 = new TestArrayDelegate(new TestBean[]{new TestBean()});
        TestArrayDelegateBean obj3 = inst.createValueInstance(obj2);
        assertArrayEquals(new TestBean[]{new TestBean()}, obj3.getDelegate());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        final BeanDescription beanDesc = mapper.introspect(TestPropertyBasedBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedMethod creator = beanDesc.findCreator(TestPropertyBasedBean.TestProperty.class);
        BeanPropertyDefinition propDef = AnnotationIntrospector.findPropertyDefinition(creator, beanDesc);
        collector.addPropertyCreator(creator, true, new SettableBeanProperty[]{new TestBeanProperty(propDef)});

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        TestPropertyBasedBean obj1 = inst.createValueInstance(null);
        assertNull(obj1.getProperty());

        TestPropertyBasedBean.TestProperty prop = new TestPropertyBasedBean.TestProperty("test");
        TestPropertyBasedBean obj2 = inst.createValueInstance(prop);
        assertEquals("test", obj2.getProperty().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        final BeanDescription beanDesc = mapper.introspect(TestDuplicateCreatorBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedMethod creator1 = beanDesc.findCreator(TestDuplicateCreatorBean.TestProperty1.class);
        collector.addPropertyCreator(creator1, true, null);

        AnnotatedMethod creator2 = beanDesc.findCreator(TestDuplicateCreatorBean.TestProperty2.class);
        collector.addPropertyCreator(creator2, true, null);

        ValueInstantiator inst = collector.constructValueInstantiator(config);
    }

    /**
     * Test bean with default constructor.
     */
    static class TestBean {
        private int intValue;
        private boolean booleanValue;

        TestBean() {
            intValue = 0;
            booleanValue = false;
        }

        int getIntValue() {
            return intValue;
        }

        boolean isBooleanValue() {
            return booleanValue;
        }
    }

    /**
     * Test bean with factory method.
     */
    static class TestFactory {
        public static TestBean create(int i, boolean b) {
            return new TestBean(i, b);
        }

        private TestBean(int i, boolean b) {
            intValue = i;
            booleanValue = b;
        }

        private int intValue;
        private boolean booleanValue;
    }

    /**
     * Test bean with string creator.
     */
    static class TestStringBean {
        private String stringValue;

        TestStringBean() {
            stringValue = "default";
        }

        TestStringBean(String value) {
            stringValue = value;
        }

        String getStringValue() {
            return stringValue;
        }
    }

    /**
     * Test bean with int creator.
     */
    static class TestIntBean {
        private int intValue;

        TestIntBean() {
            intValue = 0;
        }

        TestIntBean(int i) {
            intValue = i;
        }

        int getIntValue() {
            return intValue;
        }
    }

    /**
     * Test bean with long creator.
     */
    static class TestLongBean {
        private long longValue;

        TestLongBean() {
            longValue = 0L;
        }

        TestLongBean(long l) {
            longValue = l;
        }

        long getLongValue() {
            return longValue;
        }
    }

    /**
     * Test bean with double creator.
     */
    static class TestDoubleBean {
        private double doubleValue;

        TestDoubleBean() {
            doubleValue = 0.0;
        }

        TestDoubleBean(double d) {
            doubleValue = d;
        }

        double getDoubleValue() {
            return doubleValue;
        }
    }

    /**
     * Test bean with boolean creator.
     */
    static class TestBooleanBean {
        private boolean booleanValue;

        TestBooleanBean() {
            booleanValue = false;
        }

        TestBooleanBean(boolean b) {
            booleanValue = b;
        }

        boolean isBooleanValue() {
            return booleanValue;
        }
    }

    /**
     * Test bean with delegate creator.
     */
    static class TestDelegateBean {
        private TestDelegate delegate;

        TestDelegateBean() {}

        TestDelegateBean(TestDelegate delegate) {
            this.delegate = delegate;
        }

        TestDelegate getDelegate() {
            return delegate;
        }
    }

    /**
     * Test delegate class.
     */
    static class TestDelegate {
        private String delegate;

        TestDelegate(String delegate) {
            this.delegate = delegate;
        }

        String getDelegate() {
            return delegate;
        }
    }

    /**
     * Test delegate array.
     */
    static class TestArrayDelegate {
        private TestBean[] delegates;

        TestArrayDelegate(TestBean[] delegates) {
            this.delegates = delegates;
        }

        TestBean[] getDelegate() {
            return delegates;
        }
    }

    /**
     * Test property based creator.
     */
    static class TestPropertyBasedBean {
        private TestProperty property;

        TestPropertyBasedBean() {}

        TestPropertyBasedBean(TestProperty property) {
            this.property = property;
        }

        TestProperty getProperty() {
            return property;
        }

        static class TestProperty {
            private String value;

            TestProperty(String value) {
                this.value = value;
            }

            String getValue() {
                return value;
            }
        }

        @JsonCreator
        static TestPropertyBasedBean create(@JsonCreatorProperty TestProperty property) {
            return new TestPropertyBasedBean(property);
        }
    }

    /**
     * Test bean with duplicate creators.
     */
    static class TestDuplicateCreatorBean {
        private TestProperty1 property1;
        private TestProperty2 property2;

        TestDuplicateCreatorBean() {}

        TestDuplicateCreatorBean(TestProperty1 property1) {
            this.property1 = property1;
        }

        TestDuplicateCreatorBean(TestProperty2 property2) {
            this.property2 = property2;
        }

        TestProperty1 getProperty1() {
            return property1;
        }

        TestProperty2 getProperty2() {
            return property2;
        }

        static class TestProperty1 {
            private String value1;

            TestProperty1(String value1) {
                this.value1 = value1;
            }

            String getValue1() {
                return value1;
            }
        }

        static class TestProperty2 {
            private String value2;

            TestProperty2(String value2) {
                this.value2 = value2;
            }

            String getValue2() {
                return value2;
            }
        }

        @JsonCreator
        static TestDuplicateCreatorBean create(@JsonCreatorProperty TestProperty1 property1) {
            return new TestDuplicateCreatorBean(property1);
        }

        @JsonCreator
        static TestDuplicateCreatorBean create(@JsonCreatorProperty TestProperty2 property2) {
            return new TestDuplicateCreatorBean(property2);
        }
    }
}