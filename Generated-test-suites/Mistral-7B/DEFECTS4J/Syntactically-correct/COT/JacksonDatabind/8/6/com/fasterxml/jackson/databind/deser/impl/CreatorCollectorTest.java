package com.fasterxml.jackson.databind.deser.impl; // Import necessary packages

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test class for CreatorCollector.
 */
public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        // Test case for the constructor
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(TestClass.class), null, null);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Basic functionality test for the methods
        testDefaultCreator(creatorCollector);
        testStringCreator(creatorCollector);
        testIntCreator(creatorCollector);
        testLongCreator(creatorCollector);
        testDoubleCreator(creatorCollector);
        testBooleanCreator(creatorCollector);
        testDelegateCreator(creatorCollector);
        testPropertyBasedCreator(creatorCollector);

        // Test edge cases and exception handling
        testMissingMethodCreator(creatorCollector);
        testDuplicateCreators(creatorCollector);
        testImplicitCreator(creatorCollector);
        testPrivateCreator(creatorCollector);
        testSameClassCreator(creatorCollector);
        testNullCreator(creatorCollector);
        testThrowingCreator(creatorCollector);
        testIncompleteParameter(creatorCollector);
    }

    // Basic functionality test cases

    private void testDefaultCreator(CreatorCollector creatorCollector) {
        assertNull(creatorCollector.hasDefaultCreator());
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "defaultConstructor"), null);
        creatorCollector.setDefaultCreator(creator);
        assertNotNull(creatorCollector.hasDefaultCreator());
    }

    private void testStringCreator(CreatorCollector creatorCollector) {
        String stringValue = "test";
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "stringConstructor", String.class), null);
        creatorCollector.addStringCreator(creator, false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"test\":\"test\"}";
        TestClass testClass = mapper.readValue(jsonString, TestClass.class);
        assertNotNull(testClass);
        assertEquals(stringValue, testClass.getStringValue());
    }

    private void testIntCreator(CreatorCollector creatorCollector) {
        int intValue = 1;
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "intConstructor", int.class), null);
        creatorCollector.addIntCreator(creator, false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"int\":1}";
        TestClass testClass = mapper.readValue(jsonString, TestClass.class);
        assertNotNull(testClass);
        assertEquals(intValue, testClass.getIntValue());
    }

    private void testLongCreator(CreatorCollector creatorCollector) {
        long longValue = 1L;
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "longConstructor", long.class), null);
        creatorCollector.addLongCreator(creator, false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"long\":1}";
        TestClass testClass = mapper.readValue(jsonString, TestClass.class);
        assertNotNull(testClass);
        assertEquals(longValue, testClass.getLongValue());
    }

    private void testDoubleCreator(CreatorCollector creatorCollector) {
        double doubleValue = 1.0;
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "doubleConstructor", double.class), null);
        creatorCollector.addDoubleCreator(creator, false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"double\":1.0}";
        TestClass testClass = mapper.readValue(jsonString, TestClass.class);
        assertNotNull(testClass);
        assertEquals(doubleValue, testClass.getDoubleValue(), 0.0);
    }

    private void testBooleanCreator(CreatorCollector creatorCollector) {
        boolean booleanValue = true;
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "booleanConstructor", boolean.class), null);
        creatorCollector.addBooleanCreator(creator, false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"boolean\":true}";
        TestClass testClass = mapper.readValue(jsonString, TestClass.class);
        assertNotNull(testClass);
        assertEquals(booleanValue, testClass.isBooleanValue());
    }

    private void testDelegateCreator(CreatorCollector creatorCollector) {
        String delegateValue = "test";
        Class<TestDelegate> delegateClass = TestDelegate.class;
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(TestClass.class, "delegateConstructor", delegateClass), null);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty(new AnnotatedMethod(TestDelegate.class, "delegateConstructor", String.class), null);
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"delegate\":\"test\"}";
        TestClass testClass = mapper.readValue(jsonString, TestClass.class);
        assertNotNull(testClass);
        TestDelegate testDelegate = testClass.getDelegate();
        assertNotNull(testDelegate);
        assertEquals(delegateValue, testDelegate.getDelegateValue());
    }

    private void testPropertyBasedCreator(CreatorCollector creatorCollector) {
        String propertyValue = "test";
        Class<TestProperty> propertyClass = TestProperty.class;
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(TestClass.class, "propertyBasedConstructor"), null);
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = new CreatorProperty(new AnnotatedMethod(TestProperty.class, "propertyValueConstructor", String.class), null);
        creatorCollector.addPropertyCreator(creator, false, properties);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"property\":\"test\"}";
        TestClass testClass = mapper.readValue(jsonString, TestClass.class);
        assertNotNull(testClass);
        TestProperty testProperty = testClass.getProperty();
        assertNotNull(testProperty);
        assertEquals(propertyValue, testProperty.getPropertyValue());
    }

    // Edge case and exception handling test cases

    private void testMissingMethodCreator(CreatorCollector creatorCollector) {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "missingMethodCreator"), null);
        creatorCollector.addStringCreator(creator, false); // Should not compile
    }

    private void testDuplicateCreators(CreatorCollector creatorCollector) {
        AnnotatedWithParams creator1 = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "duplicateCreator1"), null);
        creatorCollector.addStringCreator(creator1, false);
        AnnotatedWithParams creator2 = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "duplicateCreator2"), null);
        creatorCollector.addStringCreator(creator2, false); // Should throw exception
    }

    private void testImplicitCreator(CreatorCollector creatorCollector) {
        AnnotatedParameter parameter = new AnnotatedParameter(new AnnotatedClass(TestClass.class), "intValue");
        creatorCollector.addIncompeteParameter(parameter);
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "<init>", new JavaType[] { IntType.getInstance() }), null);
        creatorCollector.addIntCreator(creator, true); // Should use implicit creator
    }

    private void testPrivateCreator(CreatorCollector creatorCollector) {
        AnnotatedMethod privateMethod = new AnnotatedMethod(TestClass.class, "privateConstructor", new JavaType[] {});
        privateMethod.setAccess(Member.PRIVATE);
        AnnotatedWithParams creator = new AnnotatedWithParams(privateMethod, null);
        creatorCollector.addStringCreator(creator, false); // Should not use private creator
    }

    private void testSameClassCreator(CreatorCollector creatorCollector) {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "sameClassConstructor", TestClass.class), null);
        creatorCollector.addStringCreator(creator, false); // Should not use same class creator
    }

    private void testNullCreator(CreatorCollector creatorCollector) {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "nullCreator", String.class), null);
        creatorCollector.addStringCreator(creator, false); // Should not use null creator
    }

    private void testThrowingCreator(CreatorCollector creatorCollector) {
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "throwingConstructor", Throwable.class), null);
        creatorCollector.addStringCreator(creator, false); // Should not use throwing creator
    }

    private void testIncompleteParameter(CreatorCollector creatorCollector) {
        AnnotatedParameter incompleteParameter = new AnnotatedParameter(new AnnotatedClass(TestClass.class), "incompleteParameter");
        creatorCollector.addIncompeteParameter(incompleteParameter);
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod("TestClass", "<init>", new JavaType[] {}), null);
        creatorCollector.addStringCreator(creator, false); // Should use incomplete parameter
    }

    // Helper classes and methods

    private static class TestClass {
        private String stringValue;
        private int intValue;
        private long longValue;
        private double doubleValue;
        private boolean booleanValue;
        private TestDelegate delegate;
        private TestProperty property;

        @JsonCreator(mode = Mode.PROPERTIES)
        public TestClass(@JsonProperty("string") String stringValue,
                         @JsonProperty("int") int intValue,
                         @JsonProperty("long") long longValue,
                         @JsonProperty("double") double doubleValue,
                         @JsonProperty("boolean") boolean booleanValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
            this.longValue = longValue;
            this.doubleValue = doubleValue;
            this.booleanValue = booleanValue;
        }

        @JsonCreator(mode = Mode.DELEGATING)
        public TestClass(@JsonProperty("delegate") TestDelegate delegate) {
            this.delegate = delegate;
        }

        @JsonCreator(mode = Mode.PROPERTY)
        public TestClass(@JsonProperty("property") TestProperty property) {
            this.property = property;
        }

        public String getStringValue() {
            return stringValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public boolean isBooleanValue() {
            return booleanValue;
        }

        public TestDelegate getDelegate() {
            return delegate;
        }

        public TestProperty getProperty() {
            return property;
        }
    }

    private static class TestDelegate {
        private String delegateValue;

        @JsonCreator
        public TestDelegate(@JsonProperty("delegate") String delegateValue) {
            this.delegateValue = delegateValue;
        }

        public String getDelegateValue() {
            return delegateValue;
        }
    }

    private static class TestProperty {
        private String propertyValue;

        @JsonCreator
        public TestProperty(@JsonProperty("property") String propertyValue) {
            this.propertyValue = propertyValue;
        }

        public String getPropertyValue() {
            return propertyValue;
        }
    }

    private static class IntType extends JavaType {
        private static final long serialVersionUID = 1L;

        public IntType() {
            this(ObjectMapper.getDefaultInstance().getTypeFactory());
        }

        public IntType(TypeFactory typeFactory) {
            super(typeFactory.constructType(int.class));
        }
    }
}