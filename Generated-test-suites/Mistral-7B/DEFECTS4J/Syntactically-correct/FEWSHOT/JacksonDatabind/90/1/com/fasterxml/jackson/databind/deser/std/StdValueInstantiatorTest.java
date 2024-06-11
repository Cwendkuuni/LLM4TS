package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test cases for {@link StdValueInstantiator}.
 */
@JacksonStdImpl
public class StdValueInstantiatorTest {

    @Test
    public void testDefaultConstructor() {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object.class);
        assertEquals("Object", inst.getValueTypeDesc());
        assertNull(inst._defaultCreator);
    }

    @Test
    public void testDefaultConstructorWithJavaType() {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object.class.getType());
        assertEquals("java.lang.Object", inst.getValueTypeDesc());
        assertNull(inst._defaultCreator);
    }

    @Test
    public void testCopyConstructor() {
        StdValueInstantiator src = new StdValueInstantiator(new DeserializationConfig(), Object.class) {
            {
                configureFromObjectSettings(
                        new AnnotatedWithParams(Foo.class, new AnnotatedConstructor(Foo.class)),
                        null, null, null, null, null);
            }
        };

        StdValueInstantiator inst = new StdValueInstantiator(src);
        assertEquals("java.lang.Object", inst.getValueTypeDesc());
        assertNull(inst._defaultCreator);
        assertEquals(src._constructorArguments, inst._constructorArguments);
    }

    @Test
    public void testConfigureFromObjectSettings() {
        AnnotatedWithParams creator = new AnnotatedWithParams(Foo.class, new AnnotatedConstructor(Foo.class));
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object.class);
        inst.configureFromObjectSettings(creator, null, null, null, null, null);
        assertEquals(creator, inst._defaultCreator);
        assertNull(inst._constructorArguments);
        assertNull(inst._withArgsCreator);
        assertNull(inst._delegateType);
        assertNull(inst._delegateCreator);
        assertNull(inst._delegateArguments);
        assertNull(inst._arrayDelegateType);
        assertNull(inst._arrayDelegateCreator);
        assertNull(inst._arrayDelegateArguments);
        assertNull(inst._fromStringCreator);
        assertNull(inst._fromIntCreator);
        assertNull(inst._fromLongCreator);
        assertNull(inst._fromDoubleCreator);
        assertNull(inst._fromBooleanCreator);
        assertNull(inst._incompleteParameter);
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("foo", "bar");

        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), String.class);
        inst.configureFromObjectSettings(
                new AnnotatedWithParams(String.class, new AnnotatedConstructor(String.class)),
                null, null, null, null, null);

        String value = inst.createUsingDefault(new DeserializationContext(new ObjectMapper()) {
            {
                setContextInitializer(new MinimalContextInitializer());
            }
        });

        assertEquals("bar", value);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Foo.class) {
            {
                configureFromObjectSettings(
                        new AnnotatedWithParams(Foo.class, new AnnotatedConstructor(Foo.class)),
                        null, null, null, null, null);
            }
        };

        Foo delegate = new Foo();
        Object value = inst.createUsingDelegate(new DeserializationContext(new ObjectMapper()), delegate);
        assertEquals("Foo created using delegate", value.getMessage());
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Foo[].class) {
            {
                configureFromArraySettings(
                        new AnnotatedWithParams(Foo[].class, new AnnotatedArrayConstructor(Foo[].class)),
                        null, null);
            }
        };

        Foo[] delegates = new Foo[2];
        Object value = inst.createUsingArrayDelegate(new DeserializationContext(new ObjectMapper()), delegates);
        assertEquals(2, value.length);
        for (int i = 0; i < value.length; ++i) {
            assertEquals("Foo created using array delegate", ((Foo) value[i]).getMessage());
        }
    }

    @Test
    public void testCreateFromString() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), String.class) {
            {
                configureFromStringCreator(new AnnotatedWithParams(String.class, new StringCreator()));
            }
        };

        String value = inst.createFromString(new DeserializationContext(new ObjectMapper()), "test");
        assertEquals("test", value);
    }

    @Test
    public void testCreateFromInt() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Integer.class) {
            {
                configureFromIntCreator(new AnnotatedWithParams(Integer.class, new IntCreator()));
            }
        };

        Integer value = inst.createFromInt(new DeserializationContext(new ObjectMapper()), 123);
        assertEquals(123, value.intValue());
    }

    @Test
    public void testCreateFromLong() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Long.class) {
            {
                configureFromLongCreator(new AnnotatedWithParams(Long.class, new LongCreator()));
            }
        };

        Long value = inst.createFromLong(new DeserializationContext(new ObjectMapper()), 123L);
        assertEquals(123L, value.longValue());
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Double.class) {
            {
                configureFromDoubleCreator(new AnnotatedWithParams(Double.class, new DoubleCreator()));
            }
        };

        Double value = inst.createFromDouble(new DeserializationContext(new ObjectMapper()), 123.5);
        assertEquals(123.5, value.doubleValue(), 0);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Boolean.class) {
            {
                configureFromBooleanCreator(new AnnotatedWithParams(Boolean.class, new BooleanCreator()));
            }
        };

        Boolean value = inst.createFromBoolean(new DeserializationContext(new ObjectMapper()), true);
        assertTrue(value.booleanValue());
    }

    @Test
    public void testCreateUsingDelegateWithNullDelegate() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object.class) {
            {
                configureFromObjectSettings(
                        new AnnotatedWithParams(Object.class, new AnnotatedConstructor(Object.class)),
                        null, null, null, null, null);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> inst.createUsingDelegate(new DeserializationContext(new ObjectMapper()), null));
    }

    @Test
    public void testCreateUsingArrayDelegateWithNullArray() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object[].class) {
            {
                configureFromArraySettings(
                        new AnnotatedWithParams(Object[].class, new AnnotatedArrayConstructor(Object[].class)),
                        null, null);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> inst.createUsingArrayDelegate(new DeserializationContext(new ObjectMapper()), null));
    }

    @Test
    public void testCreateFromObjectWithNullCreator() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object.class) {
            {
                configureFromObjectSettings(
                        null, null, null, null, null, null);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> inst.createFromObjectWith(new DeserializationContext(new ObjectMapper()), new Object[0]));
    }

    @Test
    public void testCreateFromObjectWithNullArgs() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object.class) {
            {
                configureFromObjectSettings(
                        new AnnotatedWithParams(Object.class, new AnnotatedConstructor(Object.class)),
                        null, null, null, null, null);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> inst.createFromObjectWith(new DeserializationContext(new ObjectMapper()), null));
    }

    @Test
    public void testCreateUsingDelegateWithNullDelegateAndNullArguments() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object.class) {
            {
                configureFromObjectSettings(
                        null, null, null, null, null, null);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> inst.createUsingDelegate(new DeserializationContext(new ObjectMapper()), null, new Object[0]));
    }

    @Test
    public void testCreateUsingArrayDelegateWithNullDelegateAndNullArguments() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object[].class) {
            {
                configureFromArraySettings(
                        null, null, null);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> inst.createUsingArrayDelegate(new DeserializationContext(new ObjectMapper()), null, new Object[0]));
    }

    @Test
    public void testCreateUsingDelegateWithNullDelegateAndPartialArguments() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object.class) {
            {
                configureFromObjectSettings(
                        null, null, null, null, null, null);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> inst.createUsingDelegate(new DeserializationContext(new ObjectMapper()), null, new Object[] { new Object() }));
    }

    @Test
    public void testCreateUsingArrayDelegateWithNullDelegateAndPartialArguments() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(new DeserializationConfig(), Object[].class) {
            {
                configureFromArraySettings(
                        null, null, null);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> inst.createUsingArrayDelegate(new DeserializationContext(new ObjectMapper()), null, new Object[] { new Object(), new Object() }));
    }

    private static class Foo {
        @JsonCreator
        public Foo(@JsonProperty("message") String message) {
            this.message = message;
        }

        private String message;

        @Override
        public String toString() {
            return "Foo created using " + (getClass().getName()) + ": " + message;
        }
    }

    private static class StringCreator {
        @JsonCreator
        public String create(@JsonProperty("value") String value) {
            return value;
        }
    }

    private static class IntCreator {
        @JsonCreator
        public Integer create(@JsonProperty("value") int value) {
            return value;
        }
    }

    private static class LongCreator {
        @JsonCreator
        public Long create(@JsonProperty("value") long value) {
            return value;
        }
    }

    private static class DoubleCreator {
        @JsonCreator
        public Double create(@JsonProperty("value") double value) {
            return value;
        }
    }

    private static class BooleanCreator {
        @JsonCreator
        public Boolean create(@JsonProperty("value") boolean value) {
            return value;
        }
    }

    private static class MinimalContextInitializer
            implements DeserializationConfig.ContextInitializer {
        @Override
        public void initialize(DeserializationConfig config) {
            config.setBeanDescriptionVisibility(AnnotatedClass.Visibility.ANY);
        }
    }
}