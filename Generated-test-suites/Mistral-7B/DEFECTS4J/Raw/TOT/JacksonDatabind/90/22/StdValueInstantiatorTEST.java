package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

    private DeserializationContext ctxt;
    private StdValueInstantiator stdValueInstantiator;

    @Before
    public void setup() {
        ctxt = new TestDeserializationContext();
        stdValueInstantiator = new StdValueInstantiator(ctxt, String.class);
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        Object result = stdValueInstantiator.createUsingDefault(ctxt);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("arg1", "value1");
        node.put("arg2", 123);

        SettableBeanProperty[] arguments = new SettableBeanProperty[]{
                new SettableBeanProperty("arg1", String.class),
                new SettableBeanProperty("arg2", Integer.class)
        };
        stdValueInstantiator.configureFromObjectSettings(null, null, null, null, null, arguments);

        Object result = stdValueInstantiator.createFromObjectWith(ctxt, new Object[]{node, "value2", 456});
        assertNotNull(result);
        assertEquals("value1", result.toString());

        // test error scenario: wrong number of arguments
        try {
            stdValueInstantiator.createFromObjectWith(ctxt, new Object[]{node, "value2"});
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Expected 2 arguments for constructor of StdValueInstantiator but found 1", e.getMessage());
        }
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        JavaType delegateType = new TypeFactory().constructType(Integer.class);
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(Integer.class, new Object[]{});
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[]{null};

        stdValueInstantiator.configureFromDelegateSettings(delegateType, delegateCreator, delegateArguments);

        Object result = stdValueInstantiator.createUsingDelegate(ctxt, new Integer(1));
        assertEquals(Integer.class, result.getClass());
        assertEquals(1, ((Number) result).intValue());

        // test error scenario: wrong type of delegate
        try {
            stdValueInstantiator.createUsingDelegate(ctxt, new String("test"));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Cannot instantiate value of type [simple type, class java.lang.String] from JSON String; no String instantiator found", e.getMessage());
        }
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        JavaType arrayDelegateType = new TypeFactory().constructType(int[].class);
        AnnotatedWithParams arrayDelegateCreator = new AnnotatedWithParams(int[].class, new Object[]{});
        SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[]{null};

        stdValueInstantiator.configureFromArraySettings(arrayDelegateCreator, arrayDelegateType, arrayDelegateArguments);

        Object result = stdValueInstantiator.createUsingArrayDelegate(ctxt, new int[]{1, 2, 3});
        assertArrayEquals(new int[]{1, 2, 3}, (int[]) result);

        // test error scenario: wrong type of array delegate
        try {
            stdValueInstantiator.createUsingArrayDelegate(ctxt, new String[]{"test"});
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Cannot instantiate value of type [simple type, class [Ljava.lang.String;]] from JSON String; no String[] instantiator found", e.getMessage());
        }
    }

    @Test
    public void testCreateFromString() throws IOException {
        stdValueInstantiator.configureFromStringCreator(new AnnotatedWithParams("createFromString", String.class, new Object[]{"value"}));

        Object result = stdValueInstantiator.createFromString(ctxt, "test");
        assertEquals(String.class, result.getClass());
        assertEquals("test", result.toString());
    }

    @Test
    public void testCreateFromInt() throws IOException {
        stdValueInstantiator.configureFromIntCreator(new AnnotatedWithParams("createFromInt", Integer.class, new Object[]{new Integer(1)}));

        Object result = stdValueInstantiator.createFromInt(ctxt, 42);
        assertEquals(Integer.class, result.getClass());
        assertEquals(42, ((Number) result).intValue());
    }

    @Test
    public void testCreateFromLong() throws IOException {
        stdValueInstantiator.configureFromLongCreator(new AnnotatedWithParams("createFromLong", Long.class, new Object[]{new Long(1)}));

        Object result = stdValueInstantiator.createFromLong(ctxt, 42L);
        assertEquals(Long.class, result.getClass());
        assertEquals(42L, ((Number) result).longValue());
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        stdValueInstantiator.configureFromDoubleCreator(new AnnotatedWithParams("createFromDouble", Double.class, new Object[]{new Double(1.0)}));

        Object result = stdValueInstantiator.createFromDouble(ctxt, 3.14);
        assertEquals(Double.class, result.getClass());
        assertEquals(3.14, ((Number) result).doubleValue(), 0.001);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        stdValueInstantiator.configureFromBooleanCreator(new AnnotatedWithParams("createFromBoolean
    }
    {createFromBoolean", Boolean.class, new Object[]{new Boolean(true)}));

        Object result = stdValueInstantiator.createFromBoolean(ctxt, false);
        assertEquals(Boolean.class, result.getClass());
        assertEquals(false, ((Boolean) result).booleanValue());
    }
}