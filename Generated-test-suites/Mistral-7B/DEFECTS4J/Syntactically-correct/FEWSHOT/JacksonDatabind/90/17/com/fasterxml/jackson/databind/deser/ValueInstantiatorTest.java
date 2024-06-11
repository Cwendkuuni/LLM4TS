package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.ParserArgs;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValueInstantiatorTest {

    @Test
    public void testAbstractValueInstantiator() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.Base(String.class);
        Assert.assertEquals(String.class.getName(), instantiator.getValueTypeDesc());
        Assert.assertEquals(String.class, instantiator.getValueClass());
        Assert.assertFalse(instantiator.canCreateUsingDefault());
        Assert.assertFalse(instantiator.canCreateUsingDelegate());
        Assert.assertFalse(instantiator.canCreateFromObjectWith());
    }

    @Test
    public void testCanCreateFromString() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.StringCreator();
        Assert.assertFalse(instantiator.canCreateUsingDefault());
        Assert.assertFalse(instantiator.canCreateUsingDelegate());
        Assert.assertTrue(instantiator.canCreateFromString());

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectNode node = mapper.readTree("\"foo\"");
        Object result = instantiator.createFromString(ctxt, node.getValueAsString());
        Assert.assertEquals("foo", result.toString());
    }

    @Test(expected = IOException.class)
    public void testCreateFromStringWithInvalidInput() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.StringCreator();
        DeserializationContext ctxt = new TestDeserializationContext();
        instantiator.createFromString(ctxt, "invalid");
    }

    @Test
    public void testCreateFromInt() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.IntCreator();
        Assert.assertFalse(instantiator.canCreateUsingDefault());
        Assert.assertFalse(instantiator.canCreateUsingDelegate());
        Assert.assertFalse(instantiator.canCreateFromObjectWith());
        Assert.assertTrue(instantiator.canCreateFromInt());

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectNode node = mapper.readTree("42");
        Object result = instantiator.createFromInt(ctxt, node.getInt(0));
        Assert.assertEquals(42, result.hashCode());
    }

    @Test(expected = IOException.class)
    public void testCreateFromIntWithInvalidInput() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.IntCreator();
        DeserializationContext ctxt = new TestDeserializationContext();
        instantiator.createFromInt(ctxt, -1);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.BooleanCreator();
        Assert.assertFalse(instantiator.canCreateUsingDefault());
        Assert.assertFalse(instantiator.canCreateUsingDelegate());
        Assert.assertFalse(instantiator.canCreateFromObjectWith());
        Assert.assertTrue(instantiator.canCreateFromBoolean());

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectNode nodeTrue = mapper.readTree("true");
        ObjectNode nodeFalse = mapper.readTree("false");
        Object resultTrue = instantiator.createFromBoolean(ctxt, nodeTrue.booleanValue());
        Object resultFalse = instantiator.createFromBoolean(ctxt, nodeFalse.booleanValue());
        Assert.assertEquals(true, resultTrue);
        Assert.assertEquals(false, resultFalse);
    }

    @Test(expected = IOException.class)
    public void testCreateFromBooleanWithInvalidInput() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.BooleanCreator();
        DeserializationContext ctxt = new TestDeserializationContext();
        instantiator.createFromBoolean(ctxt, 1);
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.DefaultCreator();
        Assert.assertTrue(instantiator.canCreateUsingDefault());
        Assert.assertFalse(instantiator.canCreateUsingDelegate());
        Assert.assertFalse(instantiator.canCreateFromObjectWith());

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectNode node = mapper.readTree("{}");
        Object result = instantiator.createUsingDefault(ctxt);
        Assert.assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testCreateUsingDefaultWithInvalidInput() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.DefaultCreator();
        DeserializationContext ctxt = new TestDeserializationContext();
        instantiator.createUsingDefault(ctxt);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.DelegateCreator();
        DeserializationContext ctxt = new TestDeserializationContext();
        Object delegate = new Object();
        Assert.assertTrue(instantiator.canCreateUsingDelegate());
        Assert.assertFalse(instantiator.canCreateUsingDefault());
        Assert.assertFalse(instantiator.canCreateFromObjectWith());

        ObjectMapper mapper = new ObjectMapper();
        instantiator.createUsingDelegate(ctxt, delegate);
    }

    @Test(expected = IOException.class)
    public void testCreateUsingDelegateWithInvalidInput() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.DelegateCreator();
        DeserializationContext ctxt = new TestDeserializationContext();
        instantiator.createUsingDelegate(ctxt, null);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.ObjectCreator();
        SettableBeanProperty[] props = new SettableBeanProperty[]{
                new SettableBeanProperty("foo", String.class),
                new SettableBeanProperty("bar", Integer.class)
        };
        PropertyValueBuffer buffer = new PropertyValueBuffer(props, new ArrayList<>());
        Object[] args = buffer.getParameters(props);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectNode node = mapper.readTree("{\"foo\": \"foo\", \"bar\": 42}");
        Assert.assertTrue(instantiator.canCreateUsingDefault());
        Assert.assertTrue(instantiator.canCreateUsingDelegate());
        Assert.assertTrue(instantiator.canCreateFromObjectWith());

        Object result = instantiator.createFromObjectWith(ctxt, args);
        Assert.assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testCreateFromObjectWithWithInvalidInput() throws IOException {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.ObjectCreator();
        SettableBeanProperty[] props = new SettableBeanProperty[]{
                new SettableBeanProperty("foo", String.class),
                new SettableBeanProperty("bar", Integer.class)
        };
        PropertyValueBuffer buffer = new PropertyValueBuffer(props, new ArrayList<>());
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectNode node = mapper.readTree("{\"foo\": \"foo\", \"bar\": \"invalid\"}");
        instantiator.createFromObjectWith(ctxt, buffer.getParameters(props));
    }

    @Test
    public void testGetDefaultCreator() {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.DefaultCreator();
        Assert.assertNull(instantiator.getDefaultCreator());
    }

    @Test
    public void testGetDelegateCreator() {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.DelegateCreator();
        AnnotatedWithParams creator = instantiator.getDelegateCreator();
        Assert.assertNotNull(creator);
    }

    @Test
    public void testGetArrayDelegateCreator() {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.ArrayDelegateCreator();
        AnnotatedWithParams creator = instantiator.getArrayDelegateCreator();
        Assert.assertNotNull(creator);
    }

    @Test
    public void testGetWithArgsCreator() {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.WithArgsCreator();
        AnnotatedWithParams creator = instantiator.getWithArgsCreator();
        Assert.assertNotNull(creator);
    }

    @Test
    public void testGetIncompleteParameter() {
        AbstractValueInstantiator instantiator = new AbstractValueInstantiator.WithArgsCreator();
        AnnotatedParameter parameter = instantiator.getIncompleteParameter();
        Assert.assertNull(parameter);
    }

    static class TestDeserializationContext extends DeserializationContext {
        public TestDeserializationContext() {
            super(new ObjectMapper(), new TestParser());
        }

        /* Override to avoid the parsing of unwanted data */
        @Override
        public ObjectReader _parseValue(final Class<?> valueType, final ObjectCodec codec, final ParsedName name, final CharArrayBuilder sb)
                throws IOException {
            throw new IOException("Parsing not supported");
        }
    }

    static class TestParser extends StdParser {
        @Override
        public boolean nextToken() throws IOException {
            throw new IOException("Parsing not supported");
        }

        @Override
        public void skipChildren() throws IOException {
            throw new IOException("Parsing not supported");
        }

        @Override
        public void skipValue(ParserParsed numTokens, boolean parsedRoot) throws IOException {
            throw new IOException("Parsing not supported");
        }

        @Override
        public void skipValue(ParserParsed numTokens, boolean parsedRoot, boolean _fromStructMap) throws IOException {
            throw new IOException("Parsing not supported");
        }

        @Override
        public void skipValue(ParserParsed numTokens, boolean parsedRoot, boolean _fromStructMap, boolean _fromRoot) throws IOException {
            throw new IOException("Parsing not supported");
        }

        @Override
        public void skipValue(ParserParsed numTokens, boolean parsedRoot, boolean _fromStructMap, boolean _fromRoot, boolean _inRoot) throws IOException {
            throw new IOException("Parsing not supported");
        }

        @Override
        public void skipValue(ParserParsed numTokens, boolean parsedRoot, boolean _fromStructMap, boolean _fromRoot, boolean _inRoot, boolean _hasBindings) throws IOException {
            throw new IOException("Parsing not supported");
        }

        @Override
        public void skipValue(ParserParsed numTokens, boolean parsedRoot, boolean _fromStructMap, boolean _fromRoot, boolean _inRoot, boolean _hasBindings, boolean _isExpectedStartVector) throws IOException {
            throw new IOException("Parsing not supported");
        }

        @Override
        public void _finishValue() throws IOException {
            throw new IOException("Parsing not supported");
        }
    }
}