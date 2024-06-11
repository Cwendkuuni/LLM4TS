package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core. base.ParserUtils;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

    @Test
    public void testCreateFromObjectWith() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String json = "{\"str\": \"Hello\"}";
        Object result = instantiator.createFromObjectWith(ctxt, new Object[]{"str"});
        assertEquals("Hello", result);

        json = "{\"str\": null}";
        result = instantiator.createFromObjectWith(ctxt, new Object[]{"str"});
        assertNull(result);

        json = "{\"str\": [1, 2, 3]}";
        result = instantiator.createFromObjectWith(ctxt, new Object[]{"str"});
        assertNull(result);

        json = "{\"str\": \"Hello\", \"num\": 42}";
        result = instantiator.createFromObjectWith(ctxt, new Object[]{"str", "num"});
        assertEquals("Hello", result);
        assertEquals(42, instantiator.getDefaultContext().getDeserializationContext().getCurrentValue());
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String json = "\"Hello\"";
        Object result = instantiator.createUsingDefault(ctxt);
        assertEquals("Hello", result);

        json = "null";
        result = instantiator.createUsingDefault(ctxt);
        assertNull(result);

        json = "[1, 2, 3]";
        result = instantiator.createUsingDefault(ctxt);
        assertNull(result);
    }

    @Test
    public void testCanInstantiate() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        assertEquals(false, instantiator.canInstantiate());

        instantiator = new ValueInstantiator.Base(String.class) {
            @Override
            public boolean canCreateFromString() {
                return true;
            }
        };

        assertEquals(true, instantiator.canInstantiate());
    }

    @Test
    public void testGetValueClass() {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertEquals(String.class, instantiator.getValueClass());

        instantiator = new ValueInstantiator.Base(Integer.TYPE) {
            @Override
            public Class<?> getValueClass() {
                return Integer.class;
            }
        };

        assertEquals(Integer.class, instantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());

        instantiator = new ValueInstantiator.Base(Integer.TYPE) {
            @Override
            public String getValueTypeDesc() {
                return "java.lang.Integer";
            }
        };

        assertEquals("java.lang.Integer", instantiator.getValueTypeDesc());
    }

    @Test
    public void testGetDelegateType() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertEquals(null, instantiator.getDelegateType(null));

        instantiator = new ValueInstantiator.Base(String.class) {
            @Override
            public JavaType getDelegateType(DeserializationConfig config) {
                return config.constructType(String[].class);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType delegateType = instantiator.getDelegateType(ctxt);
        assertEquals(String[].class, delegateType.getRawClass());
    }

    @Test
    public void testCreateFromInt() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer.class) {
            @Override
            public boolean canCreateFromInt() {
                return true;
            }

            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return value;
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String json = "42";
        Object result = instantiator.createFromInt(ctxt, 42);
        assertEquals(42, result);

        json = "0";
        result = instantiator.createFromInt(ctxt, 0);
        assertEquals(0, result);
    }

    @Test
    public void testCreateFromStringFallbacks() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Boolean.class) {
            @Override
            protected Object _createFromStringFallbacks(DeserializationContext ctxt, String value)
                    throws IOException {
                return value.equalsIgnoreCase("true");
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String json = "true";
        Object result = instantiator.createFromString(ctxt, json);
        assertEquals(true, result);

        json = "false";
        result = instantiator.createFromString(ctxt, json);
        assertEquals(false, result);

        json = "123";
        result = instantiator.createFromString(ctxt, json);
        assertNull(result);
    }

    /**
     * Test for {@link ValueInstantiator#createUsingDelegate(DeserializationContext, Object)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testCreateUsingDelegate() throws IOException {
        class MyValueInstantiator extends ValueInstantiator {
            @Override
            public boolean canCreateUsingDelegate() {
                return true;
            }

            @Override
            public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                assertEquals(String.class, delegate.getClass());
                assertEquals("delegate", delegate.toString());
                return delegate;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String delegateJson = "\"delegate\"";
        Object delegate = mapper.readValue(delegateJson, String.class);
        ValueInstantiator instantiator = new MyValueInstantiator();

        String json = "{\"s\": " + ParserUtils.quote(delegateJson) + "}";
        Object result = instantiator.createUsingDelegate(ctxt, delegate);
        assertEquals(delegate, result);

        json = "{\"s\": null}";
        result = instantiator.createUsingDelegate(ctxt, null);
        assertNull(result);
    }
}