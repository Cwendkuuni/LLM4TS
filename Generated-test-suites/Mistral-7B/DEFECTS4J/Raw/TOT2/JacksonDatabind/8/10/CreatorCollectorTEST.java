import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private BeanDescription beanDescription;
    private DeserializationConfig config;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException {
        config = new DeserializationConfig();
        beanDescription = new BeanDescription(Object.class);
        creatorCollector = new CreatorCollector(beanDescription, true);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        creatorCollector.setDefaultCreator(new AnnotatedWithParams(new MethodTestDefaultConstructorMethod()));
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        Object createdObject = valueInstantiator.createValue(config, null);
        assertEquals(new Object(), createdObject);
    }

    @Test
    public void testStringCreator() throws Exception {
        Object[] args = new Object[]{"test"};
        creatorCollector.addStringCreator(new AnnotatedWithParams(new MethodTestStringConstructorMethod(), true), true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        Object createdObject = valueInstantiator.createValue(config, null, "test");
        assertEquals("test", createdObject);
    }

    @Test
    public void testIntCreator() throws Exception {
        Object[] args = new Object[]{5};
        creatorCollector.addIntCreator(new AnnotatedWithParams(new MethodTestIntConstructorMethod(), true), true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        Object createdObject = valueInstantiator.createValue(config, null, 5);
        assertEquals(5, createdObject);
    }

    @Test
    public void testLongCreator() throws Exception {
        Object[] args = new Object[]{10L};
        creatorCollector.addLongCreator(new AnnotatedWithParams(new MethodTestLongConstructorMethod(), true), true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        Object createdObject = valueInstantiator.createValue(config, null, 10L);
        assertEquals(10L, createdObject);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        Object[] args = new Object[]{3.14};
        creatorCollector.addDoubleCreator(new AnnotatedWithParams(new MethodTestDoubleConstructorMethod(), true), true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        Object createdObject = valueInstantiator.createValue(config, null, 3.14);
        assertEquals(3.14, createdObject);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        Object[] args = new Object[]{true};
        creatorCollector.addBooleanCreator(new AnnotatedWithParams(new MethodTestBooleanConstructorMethod(), true), true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        Object createdObject = valueInstantiator.createValue(config, null, true);
        assertEquals(true, createdObject);
    }

    @Test(expected = IllegalStateException.class)
    public void testDupCreator() throws Exception {
        creatorCollector.addStringCreator(new AnnotatedWithParams(new MethodTestStringConstructorMethod(), true), true);
        creatorCollector.addStringCreator(new AnnotatedWithParams(new MethodTestStringConstructorMethod(), true), true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        Object[] delegatingArgs = new Object[]{"delegate"};
        Object[] delegateArgs = new Object[]{new Object()};
        creatorCollector.addDelegatingCreator(new AnnotatedWithParams(new MethodTestDelegatingConstructorMethod(), true), true, delegateArgs);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        Object createdObject = valueInstantiator.createValue(config, null, "delegate");
        assertEquals("delegate", createdObject);
    }

    @Test
    public void testPropertyCreator() throws Exception {
        String[] propertyNames = new String[]{"name", "age"};
        CreatorProperty[] propertyArgs = new CreatorProperty[2];
        Arrays.stream(propertyArgs).forEach(p -> p = new CreatorProperty("name", Object.class));
        creatorCollector.addPropertyCreator(new AnnotatedWithParams(new MethodTestPropertyConstructorMethod(), true), true, propertyArgs);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        Object createdObject = valueInstantiator.createValue(config, null, "test", 5);
        Map<String, Object> expectedMap = new LinkedHashMap<String, Object>() {{
            put("name", "test");
            put("age", 5);
        }};
        assertEquals(expectedMap, createdObject);
    }

    @Test
    public void testNullCreator() throws Exception {
        creatorCollector.setDefaultCreator(null);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        Object createdObject = valueInstantiator.createValue(config, null);
        assertEquals(null, createdObject);
    }

    private static class MethodTestDefaultConstructorMethod {
        public MethodTestDefaultConstructorMethod() {
        }
    }

    private static class MethodTestStringConstructorMethod {
        public MethodTestStringConstructorMethod(String arg) {
            this.arg = arg;
        }

        private final String arg;
    }

    private static class MethodTestIntConstructorMethod {
        public MethodTestIntConstructorMethod(int arg) {
            this.arg = arg;
        }

        private final int arg;
    }

    private static class MethodTestLongConstructorMethod {
        public MethodTestLongConstructorMethod(long arg) {
            this.arg = arg;
        }

        private final long arg;
    }

    private static class MethodTestDoubleConstructorMethod {
        public MethodTestDoubleConstructorMethod(double arg) {
            this.arg = arg;
        }

        private final double arg;
    }

    private static class MethodTestBooleanConstructorMethod {
        public MethodTestBooleanConstructorMethod(boolean arg) {
            this.arg = arg;
        }

        private final boolean arg;
    }

    private static class MethodTestDelegatingConstructorMethod {
        public MethodTestDelegatingConstructorMethod(Object delegate) {
            this.delegate = delegate;
        }

        private final Object delegate;
    }

    private static class MethodTestPropertyConstructorMethod {
        public MethodTestPropertyConstructorMethod(String name, Class<?> propertyType) {
            this.name = name;
            this.propertyType = propertyType;
        }

        private final String name;
        private final Class<?> propertyType;
    }
}