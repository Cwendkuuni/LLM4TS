package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingArrayDelegate());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromLong());
        assertFalse(instantiator.canCreateFromDouble());
        assertFalse(instantiator.canCreateFromBoolean());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getArrayDelegateCreator());
        assertNull(instantiator.getWithArgsCreator());
        assertNull(instantiator.getIncompleteParameter());

        Object obj = instantiator.createUsingDefault(new TestContext());
        assertNotNull(obj);
        assertEquals("", obj.toString());
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        // This test case is not applicable as the method returns null
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertNull(instantiator.getFromObjectArguments(new TestConfig()));
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        // This test case is not applicable as the method returns null
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertNull(instantiator.getDelegateType(new TestConfig()));
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        // This test case is not applicable as the method returns null
        ValueInstantiator instantiator = new ValueInstantiator.Base(String[].class);
        assertNull(instantiator.getArrayDelegateType(new TestConfig()));
    }

    @Test
    public void testCreateFromInt() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer.class);
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateUsingArrayDelegate());
        assertFalse(instantiator.canCreateFromString());
        assertTrue(instantiator.canCreateFromInt());

        Object obj = instantiator.createFromInt(new TestContext(), 42);
        assertNotNull(obj);
        assertEquals(42, obj.intValue());
    }

    @Test
    public void testCreateFromLong() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Long.class);
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateUsingArrayDelegate());
        assertFalse(instantiator.canCreateFromString());
        assertTrue(instantiator.canCreateFromLong());

        Object obj = instantiator.createFromLong(new TestContext(), 42L);
        assertNotNull(obj);
        assertEquals(42L, obj.longValue());
    }

    @Test
    public void testCreateFromDouble() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Double.class);
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateUsingArrayDelegate());
        assertFalse(instantiator.canCreateFromString());
        assertTrue(instantiator.canCreateFromDouble());

        Object obj = instantiator.createFromDouble(new TestContext(), 42.0);
        assertNotNull(obj);
        assertEquals(42.0, obj.doubleValue(), 0.0);
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Boolean.class);
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateUsingArrayDelegate());
        assertFalse(instantiator.canCreateFromString());
        assertTrue(instantiator.canCreateFromBoolean());

        Object objTrue = instantiator.createFromBoolean(new TestContext(), true);
        Object objFalse = instantiator.createFromBoolean(new TestContext(), false);
        assertNotNull(objTrue);
        assertTrue(objTrue.booleanValue());
        assertNotNull(objFalse);
        assertFalse(objFalse.booleanValue());
    }

    @Test
    public void testCreateFromString() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateUsingArrayDelegate());
        assertTrue(instantiator.canCreateFromString());

        Object obj = instantiator.createFromString(new TestContext(), "test");
        assertNotNull(obj);
        assertEquals("test", obj.toString());
    }

    @Test
    public void testCreateFromObjectWithNull() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(TestClass.class);
        SettableBeanProperty[] prop = new SettableBeanProperty[1];
        prop[0] = new TestSettableBeanProperty();
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("prop", "test");

        try {
            instantiator.createFromObjectWith(new TestContext(), prop, node);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFromObjectWithValid() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(TestClass.class);
        SettableBeanProperty[] prop = new SettableBeanProperty[1];
        prop[0] = new TestSettableBeanProperty();
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("prop", "test");

        Object obj = instantiator.createFromObjectWith(new TestContext(), prop, node);
        assertNotNull(obj);
        assertEquals("test", ((TestClass) obj).getProp());
    }

    private static class TestContext extends DeserializationContext {
        TestContext() {
            super(null, null, null, null, null);
        }

        @Override
        public ObjectReader _getRootValueFactory() {
            return null;
        }

        @Override
        public ObjectReader _createValueHandler(BeanProperty property) {
            return null;
        }

        @Override
        public JavaType _handleValueType(JavaType type, DeserializationConfig config) {
            return null;
        }

        @Override
        public DeserializationConfig getConfig() {
            return null;
        }
    }

    private static class TestSettableBeanProperty extends BeanProperty {
        TestSettableBeanProperty() {
            super("prop", Object.class, null, null);
        }
    }

    private static class TestClass {
        private String prop;

        String getProp() {
            return prop;
        }

        void setProp(String prop) {
            this.prop = prop;
        }
    }
}