package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;

/**
 * Unit tests for {@link StdValueInstantiator} class.
 */
@JacksonStdImpl
public class StdValueInstantiatorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Test for default constructor and {@code canCreateUsingDefault()} method.
     */
    @Test
    public void testCanCreateUsingDefault() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                String.class);
        assertTrue(instantiator.canCreateUsingDefault());
    }

    /**
     * Test for constructor with arguments and {@code canCreateFromObjectWith()} method.
     */
    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        TestClassWithInjectableConstructor testClass = new TestClassWithInjectableConstructor();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                TestClassWithInjectableConstructor.class);
        instantiator.configureFromObjectSettings(new AnnotatedWithParams(TestClassWithInjectableConstructor.class
                        .getDeclaredConstructor(String.class, int.class)), null, null, null, null, null);
        assertTrue(instantiator.canCreateFromObjectWith());
    }

    /**
     * Test for delegate constructor and {@code canCreateUsingDelegate()} method.
     */
    @Test
    public void testCanCreateUsingDelegate() throws Exception {
        TestClassWithDelegateConstructor testClass = new TestClassWithDelegateConstructor();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                TestClassWithDelegateConstructor.class);
        instantiator.configureFromObjectSettings(null, new AnnotatedWithParams(
                TestClassWithDelegateConstructor.class.getDeclaredConstructor(TestDelegate.class)),
                TestClassWithDelegateConstructor.class.getDeclaredField("delegate").getType(),
                new SettableBeanProperty[0], null, null);
        assertTrue(instantiator.canCreateUsingDelegate());
    }

    /**
     * Test for array delegate constructor and {@code canCreateUsingArrayDelegate()} method.
     */
    @Test
    public void testCanCreateUsingArrayDelegate() throws Exception {
        TestClassWithArrayDelegateConstructor testClass = new TestClassWithArrayDelegateConstructor();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                TestClassWithArrayDelegateConstructor.class);
        instantiator.configureFromArraySettings(new AnnotatedWithParams(
                TestClassWithArrayDelegateConstructor.class.getDeclaredConstructor(int[].class)),
                JavaClassWithArray.class.getComponentType(), new SettableBeanProperty[0]);
        assertTrue(instantiator.canCreateUsingArrayDelegate());
    }

    /**
     * Test for string value constructor and {@code canCreateFromString()} method.
     */
    @Test
    public void testCanCreateFromString() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                String.class);
        instantiator.configureFromStringCreator(new AnnotatedWithParams(String.class.getDeclaredConstructor(
                        String.class), new Annotation[0]));
        assertTrue(instantiator.canCreateFromString());
    }

    /**
     * Test for int value constructor and {@code canCreateFromInt()} method.
     */
    @Test
    public void testCanCreateFromInt() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                Integer.class);
        instantiator.configureFromIntCreator(new AnnotatedWithParams(
                Integer.class.getDeclaredConstructor(Integer.TYPE), new Annotation[0]));
        assertTrue(instantiator.canCreateFromInt());
    }

    /**
     * Test for long value constructor and {@code canCreateFromLong()} method.
     */
    @Test
    public void testCanCreateFromLong() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                Long.class);
        instantiator.configureFromLongCreator(new AnnotatedWithParams(
                Long.class.getDeclaredConstructor(Long.TYPE), new Annotation[0]));
        assertTrue(instantiator.canCreateFromLong());
    }

    /**
     * Test for double value constructor and {@code canCreateFromDouble()} method.
     */
    @Test
    public void testCanCreateFromDouble() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                Double.class);
        instantiator.configureFromDoubleCreator(new AnnotatedWithParams(
                Double.class.getDeclaredConstructor(Double.TYPE), new Annotation[0]));
        assertTrue(instantiator.canCreateFromDouble());
    }

    /**
     * Test for boolean value constructor and {@code canCreateFromBoolean()} method.
     */
    @Test
    public void testCanCreateFromBoolean() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                Boolean.class);
        instantiator.configureFromBooleanCreator(new AnnotatedWithParams(
                Boolean.class.getDeclaredConstructor(Boolean.TYPE), new Annotation[0]));
        assertTrue(instantiator.canCreateFromBoolean());
    }

    /**
     * Test for incomplete parameter constructor and {@code getIncompleteParameter()} method.
     */
    @Test
    public void testGetIncompleteParameter() throws Exception {
        TestClassWithIncompleteParameter testClass = new TestClassWithIncompleteParameter();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                TestClassWithIncompleteParameter.class);
        instantiator.configureIncompleteParameter(new AnnotatedParameter(
                TestClassWithIncompleteParameter.class.getDeclaredField("incomplete")));
        assertEquals(new AnnotatedParameter(TestClassWithIncompleteParameter.class.getDeclaredField(
                "incomplete")), instantiator.getIncompleteParameter());
    }

    /**
     * Test for creation from default constructor.
     */
    @Test
    public void testCreateUsingDefault() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                String.class);
        String result = instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertEquals("", result);
    }

    /**
     * Test for creation from constructor with arguments.
     */
    @Test
    public void testCreateFromObjectWith() throws Exception {
        TestClassWithInjectableConstructor testClass = new TestClassWithInjectableConstructor();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                TestClassWithInjectableConstructor.class);
        instantiator.configureFromObjectSettings(new AnnotatedWithParams(
                TestClassWithInjectableConstructor.class.getDeclaredConstructor(String.class, int.class)),
                null, null, null, null, null);
        TestClassWithInjectableConstructor result = (TestClassWithInjectableConstructor) instantiator
                .createFromObjectWith(mapper.getDeserializationContext(), new Object[] {"test", 42});
        assertEquals("test", result.stringField);
        assertEquals(42, result.intField);
    }

    /**
     * Test for creation using a delegate constructor.
     */
    @Test
    public void testCreateUsingDelegate() throws Exception {
        TestClassWithDelegateConstructor testClass = new TestClassWithDelegateConstructor();
        TestDelegate testDelegate = mapper.readValue(mapper.createArrayBuffer(
                mapper.writeValueAsBytes(new Object[] {new TestDelegate()})), TestDelegate[].class)[0];
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                TestClassWithDelegateConstructor.class);
        instantiator.configureFromObjectSettings(null, new AnnotatedWithParams(
                TestClassWithDelegateConstructor.class.getDeclaredConstructor(TestDelegate.class)),
                TestClassWithDelegateConstructor.class.getDeclaredField("delegate").getType(),
                new SettableBeanProperty[0], null, null);
        TestClassWithDelegateConstructor result = (TestClassWithDelegateConstructor) instantiator
                .createUsingDelegate(mapper.getDeserializationContext(), testDelegate);
        assertEquals(testDelegate, result.delegate);
    }

    /**
     * Test for creation using an array delegate constructor.
     */
    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        int[] array = new int[] {1, 2, 3};
        TestClassWithArrayDelegateConstructor testClass = new TestClassWithArrayDelegateConstructor();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                TestClassWithArrayDelegateConstructor.class);
        instantiator.configureFromArraySettings(new AnnotatedWithParams(
                TestClassWithArrayDelegateConstructor.class.getDeclaredConstructor(int[].class)),
                JavaClassWithArray.class.getComponentType(), new SettableBeanProperty[0]);
        TestClassWithArrayDelegateConstructor result = (TestClassWithArrayDelegateConstructor) instantiator
                .createUsingArrayDelegate(mapper.getDeserializationContext(), array);
        assertArrayEquals(array, result.array);
    }

    /**
     * Test for creation from a string value constructor.
     */
    @Test
    public void testCreateFromString() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                String.class);
        instantiator.configureFromStringCreator(new AnnotatedWithParams(
                String.class.getDeclaredConstructor(String.class), new Annotation[0]));
        String result = (String) instantiator.createFromString(mapper.getDeserializationContext(), "test");
        assertEquals("test", result);
    }

    /**
     * Test for creation from an int value constructor.
     */
    @Test
    public void testCreateFromInt() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                Integer.class);
        instantiator.configureFromIntCreator(new AnnotatedWithParams(
                Integer.class.getDeclaredConstructor(Integer.TYPE), new Annotation[0]));
        Integer result = (Integer) instantiator.createFromInt(mapper.getDeserializationContext(), 42);
        assertEquals(42, result.intValue());
    }

    /**
     * Test for creation from a long value constructor.
     */
    @Test
    public void testCreateFromLong() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                Long.class);
        instantiator.configureFromLongCreator(new AnnotatedWithParams(
                Long.class.getDeclaredConstructor(Long.TYPE), new Annotation[0]));
        Long result = (Long) instantiator.createFromLong(mapper.getDeserializationContext(), 42L);
        assertEquals(42L, result);
    }

    /**
     * Test for creation from a double value constructor.
     */
    @Test
    public void testCreateFromDouble() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                Double.class);
        instantiator.configureFromDoubleCreator(new AnnotatedWithParams(
                Double.class.getDeclaredConstructor(Double.TYPE), new Annotation[0]));
        Double result = (Double) instantiator.createFromDouble(mapper.getDeserializationContext(), 42.0D);
        assertEquals(42.0D, result);
    }

    /**
     * Test for creation from a boolean value constructor.
     */
    @Test
    public void testCreateFromBoolean() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                Boolean.class);
        instantiator.configureFromBooleanCreator(new AnnotatedWithParams(
                Boolean.class.getDeclaredConstructor(Boolean.TYPE), new Annotation[0]));
        Boolean result = (Boolean) instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        assertTrue(result);
    }

    /**
     * Test for creation with an incomplete parameter.
     */
    @Test(expected = JsonMappingException.class)
    public void testCreateWithIncompleteParameter() throws Exception {
        TestClassWithIncompleteParameter testClass = new TestClassWithIncompleteParameter();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                TestClassWithIncompleteParameter.class);
        instantiator.configureIncompleteParameter(new AnnotatedParameter(
                TestClassWithIncompleteParameter.class.getDeclaredField("incomplete")));
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    /**
     * Test class with a constructor that accepts two arguments: a String and an int.
     */
    static class TestClassWithInjectableConstructor {
        @JsonProperty("stringField")
        private String stringField;
        @JsonProperty("intField")
        private int intField;

        @JsonCreator
        public TestClassWithInjectableConstructor(@JsonProperty("stringField") String stringField,
                @JsonProperty("intField") int intField) {
            this.stringField = stringField;
            this.intField = intField;
        }
    }

    /**
     * Test class with a delegate constructor.
     */
    static class TestClassWithDelegateConstructor {
        @JsonProperty("delegate")
        private TestDelegate delegate;

        @JsonCreator
        public TestClassWithDelegateConstructor(@JsonProperty("delegate") TestDelegate delegate) {
            this.delegate = delegate;
        }
    }

    /**
     * Test class with an array delegate constructor.
     */
    static class TestClassWithArrayDelegateConstructor {
        @JsonProperty("array")
        private int[] array;

        @JsonCreator
        public TestClassWithArrayDelegateConstructor(@JsonProperty("array") int[] array) {
            this.array = array;
        }
    }

    /**
     * Test class with an incomplete parameter.
     */
    static class TestClassWithIncompleteParameter {
        @JsonProperty("incomplete")
        private TestIncompleteParameter incomplete;

        @JsonCreator
        public TestClassWithIncompleteParameter(@JsonProperty("incomplete") TestIncompleteParameter incomplete) {
            this.incomplete = incomplete;
        }

        private static class TestIncompleteParameter {}
    }

    /**
     * Test class for a delegate.
     */
    static class TestDelegate {}

    /**
     * Test class for a Java class with an array component type.
     */
    static class JavaClassWithArray {
        private int[] array;
    }
}