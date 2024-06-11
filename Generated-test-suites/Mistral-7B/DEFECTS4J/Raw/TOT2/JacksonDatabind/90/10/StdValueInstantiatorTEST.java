package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JacksonAnnotationIntrosPECTor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Test cases for the {@link StdValueInstantiator} class.
 */
@JacksonStdImpl
public class StdValueInstantiatorTest {

    private DeserializationConfig config;
    private StdValueInstantiator valueInstantiator;

    @Before
    public void setup() throws Exception {
        config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        valueInstantiator = mapper.findValueInstantiator(String.class);
    }

    /**
     * Test constructor with no arguments.
     */
    @Test
    public void testCreateUsingDefault() throws IOException {
        Object result = valueInstantiator.createUsingDefault(config.getContext());
        assertNotNull(result);
    }

    /**
     * Test constructor with arguments.
     */
    @Test
    public void testCreateFromObjectWith() throws IOException {
        AnnotatedClass<MyClass> annotatedClass = JacksonAnnotationIntrosPECTor.findAnnotated(MyClass.class);
        AnnotatedConstructor constructor = annotatedClass.getDeclaredConstructor();
        Object[] args = new Object[]{new Object(), "test"};

        Object result = valueInstantiator.createFromObjectWith(config.getContext(), args);
        assertInstanceOf(MyClass.class, result);

        assertThat(result.toString(), is("MyClass(objectInstance=testObject, stringInstance=test)"));
    }

    /**
     * Test delegate instantiation.
     */
    @Test
    public void testCreateUsingDelegate() throws IOException {
        Class<MyDelegate> delegateClass = MyDelegate.class;
        Object delegateInstance = new MyDelegate();

        Object result = valueInstantiator.createUsingDelegate(config.getContext(), delegateInstance);
        assertInstanceOf(MyDelegate.class, result);

        assertEquals(delegateInstance.getValue(), result.getValue());
    }

    /**
     * Test array delegate instantiation.
     */
    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        Class<MyArrayDelegate> arrayDelegateClass = MyArrayDelegate[].class;
        Object arrayDelegateInstance = new MyArrayDelegate[]{new MyArrayDelegate(), new MyArrayDelegate()};

        Object result = valueInstantiator.createUsingArrayDelegate(config.getContext(), arrayDelegateInstance);
        assertArrayEquals(new MyArrayDelegate[]{new MyArrayDelegate(), new MyArrayDelegate()}, (MyArrayDelegate[]) result);
    }

    /**
     * Test string instantiation.
     */
    @Test
    public void testCreateFromString() throws IOException {
        ObjectNode node = ObjectMapper.createObjectNode();
        node.put("value", "testString");

        Object result = valueInstantiator.createFromString(config.getContext(), node.toString());
        assertInstanceOf(String.class, result);

        assertEquals("testString", result);
    }

    /**
     * Test integer instantiation.
     */
    @Test
    public void testCreateFromInt() throws IOException {
        ObjectNode node = ObjectMapper.createObjectNode();
        node.put("value", 42);

        Object result = valueInstantiator.createFromInt(config.getContext(), 42);
        assertInstanceOf(Integer.class, result);

        assertEquals(42, result.intValue());
    }

    /**
     * Test long instantiation.
     */
    @Test
    public void testCreateFromLong() throws IOException {
        ObjectNode node = ObjectMapper.createObjectNode();
        node.put("value", 42L);

        Object result = valueInstantiator.createFromLong(config.getContext(), 42L);
        assertInstanceOf(Long.class, result);

        assertEquals(42L, result.longValue());
    }

    /**
     * Test double instantiation.
     */
    @Test
    public void testCreateFromDouble() throws IOException {
        ObjectNode node = ObjectMapper.createObjectNode();
        node.put("value", 3.14);

        Object result = valueInstantiator.createFromDouble(config.getContext(), 3.14);
        assertInstanceOf(Double.class, result);

        assertEquals(3.14, result.doubleValue(), 0.001);
    }

    /**
     * Test boolean instantiation.
     */
    @Test
    public void testCreateFromBoolean() throws IOException {
        ObjectNode node = ObjectMapper.createObjectNode();
        node.put("value", true);

        Object result = valueInstantiator.createFromBoolean(config.getContext(), true);
        assertInstanceOf(Boolean.class, result);

        assertTrue(result.booleanValue());
    }

    /**
     * Test incomplete parameter instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithIncompleteParameter() throws IOException {
        ObjectNode node = ObjectMapper.createObjectNode();
        node.put("incompleteParameter", new Object());

        Object result = valueInstantiator.createUsingDelegate(config.getContext(), node);
    }

    /**
     * Test null value instantiation.
     */
    @Test
    public void testCreateFromNull() throws IOException {
        Object result = valueInstantiator.createUsingDefault(config.getContext());
        assertNull(result);
    }

    /**
     * Test exception handling.
     */
    @Test(expected = JsonMappingException.class)
    public void testCreateFromInvalidJson() throws IOException {
        ObjectNode node = ObjectMapper.createObjectNode();
        node.put("invalidJson", "invalidJsonValue");

        valueInstantiator.createFromString(config.getContext(), node.toString());
    }

    /**
     * Test null delegate instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingNullDelegate() throws IOException {
        Object result = valueInstantiator.createUsingDelegate(config.getContext(), null);
    }

    /**
     * Test null delegate arguments instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingNullDelegateArguments() throws IOException {
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(Object.class);
        Object result = valueInstantiator.createUsingDelegate(config.getContext(), delegateCreator, null);
    }

    /**
     * Test missing delegate arguments instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingMissingDelegateArguments() throws IOException {
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(Object[].class, new AnnotatedParameter[]{});
        Object[] delegateArguments = new Object[]{"missingArgument"};
        Object result = valueInstantiator.createUsingDelegate(config.getContext(), delegateCreator, delegateArguments);
    }

    /**
     * Test illegal delegate arguments instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingIllegalDelegateArguments() throws IOException {
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(Object[].class, new AnnotatedParameter[]{
            new AnnotatedParameter() {
                @Override
                public AnnotatedType getType() {
                    return null;
                }
            }
        });
        Object[] delegateArguments = new Object[]{new Object()};
        Object result = valueInstantiator.createUsingDelegate(config.getContext(), delegateCreator, delegateArguments);
    }

    /**
     * Test empty array delegate instantiation.
     */
    @Test
    public void testCreateUsingEmptyArrayDelegate() throws IOException {
        Object result = valueInstantiator.createUsingArrayDelegate(config.getContext(), new Object[]{});
        assertArrayEquals(new Object[0], (Object[]) result);
    }

    /**
     * Test non-matching array delegate type instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingIncorrectArrayDelegateType() throws IOException {
        Object result = valueInstantiator.createUsingArrayDelegate(config.getContext(), new Object[]{new Object()});
    }

    /**
     * Test null array delegate arguments instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingNullArrayDelegateArguments() throws IOException {
        AnnotatedWithParams arrayDelegateCreator = new AnnotatedWithParams(Object[].class);
        Object result = valueInstantiator.createUsingArrayDelegate(config.getContext(), arrayDelegateCreator, null);
    }

    /**
     * Test missing array delegate arguments instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingMissingArrayDelegateArguments() throws IOException {
        AnnotatedWithParams arrayDelegateCreator = new AnnotatedWithParams(Object[].class, new AnnotatedParameter[]{});
        Object[] arrayDelegateArguments = new Object[]{"missingArgument"};
        Object result = valueInstantiator.createUsingArrayDelegate(config.getContext(), arrayDelegateCreator, arrayDelegateArguments);
    }

    /**
     * Test illegal array delegate arguments instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingIllegalArrayDelegateArguments() throws IOException {
        AnnotatedWithParams arrayDelegateCreator = new AnnotatedWithParams(Object[].class, new AnnotatedParameter[]{
            new AnnotatedParameter() {
                @Override
                public AnnotatedType getType() {
                    return null;
                }
            }
        });
        Object[] arrayDelegateArguments = new Object[]{new Object()};
        Object result = valueInstantiator.createUsingArrayDelegate(config.getContext(), arrayDelegateCreator, arrayDelegateArguments);
    }

    /**
     * Test null strings instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromNullStrings() throws IOException {
        Object result = valueInstantiator.createFromString(config.getContext(), null);
    }

    /**
     * Test null integers instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromNullInts() throws IOException {
        Object result = valueInstantiator.createFromInt(config.getContext(), null);
    }

    /**
     * Test null longs instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromNullLongs() throws IOException {
        Object result = valueInstantiator.createFromLong(config.getContext(), null);
    }

    /**
     * Test null doubles instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromNullDoubles() throws IOException {
        Object result = valueInstantiator.createFromDouble(config.getContext(), null);
    }

    /**
     * Test null booleans instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromNullBooleans() throws IOException {
        Object result = valueInstantiator.createFromBoolean(config.getContext(), null);
    }

    /**
     * Test null objects instantiation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromNullObjects() throws IOException {
        Object result = valueInstantiator.createUsingDefault(config.getContext());
    }

    /**
     * Helper class for testing creation using delegate.
     */
    static class MyDelegate {
        private int value;

        MyDelegate() {
            this(0);
        }

        MyDelegate(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Helper class for testing creation using array delegate.
     */
    static class MyArrayDelegate {
        private int value;

        MyArrayDelegate() {
            this(0);
        }

        MyArrayDelegate(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Helper class for testing instantiation from JSON strings.
     */
    static class MyClass {
        private Object objectInstance;
        private String stringInstance;

        MyClass(Object objectInstance, String stringInstance) {
            this.objectInstance = objectInstance;
            this.stringInstance = stringInstance;
        }

        @Override
        public String toString() {
            return "MyClass(objectInstance=" + objectInstance + ", stringInstance=" + stringInstance + ")";
        }
    }
}