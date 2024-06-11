package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiatorAbstractMethods() {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertNotNull(instantiator);
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
        assertFalse(instantiator.canInstantiate());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethodsThrowException() {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        instantiator.createUsingDefault(null);
    }

    @Test
    public void testCanCreateFromString() {
        ValueInstantiator instantiator = new StringValueInstantiator();
        assertTrue(instantiator.canCreateFromString());
        String value = instantiator.createFromString(null, "test");
        assertNotNull(value);
        assertEquals("test", value);
    }

    @Test
    public void testCanCreateFromInt() {
        ValueInstantiator instantiator = new IntValueInstantiator();
        assertTrue(instantiator.canCreateFromInt());
        Object value = instantiator.createFromInt(null, 42);
        assertNotNull(value);
        assertEquals(42, value.hashCode());
    }

    @Test
    public void testCanCreateFromLong() {
        ValueInstantiator instantiator = new LongValueInstantiator();
        assertTrue(instantiator.canCreateFromLong());
        Object value = instantiator.createFromLong(null, 42L);
        assertNotNull(value);
        assertEquals(42L, value.hashCode());
    }

    @Test
    public void testCanCreateFromDouble() {
        ValueInstantiator instantiator = new DoubleValueInstantiator();
        assertTrue(instantiator.canCreateFromDouble());
        Object value = instantiator.createFromDouble(null, 3.14);
        assertNotNull(value);
        assertEquals(3.14, value.hashCode(), 0.001);
    }

    @Test
    public void testCanCreateFromBoolean() {
        ValueInstantiator instantiator = new BooleanValueInstantiator();
        assertTrue(instantiator.canCreateFromBoolean());
        Object value = instantiator.createFromBoolean(null, true);
        assertNotNull(value);
        assertTrue(value instanceof Boolean);
        assertEquals(true, ((Boolean) value).booleanValue());
    }

    @Test
    public void testCanCreateUsingDefault() {
        ValueInstantiator instantiator = new StringValueInstantiator();
        assertTrue(instantiator.canCreateUsingDefault());
        Object value = instantiator.createUsingDefault(null);
        assertNotNull(value);
        assertEquals("", value.toString());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        ValueInstantiator instantiator = new StringValueInstantiator();
        assertFalse(instantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        ValueInstantiator instantiator = new ArrayStringValueInstantiator();
        assertTrue(instantiator.canCreateUsingArrayDelegate());
        Object value = instantiator.createUsingArrayDelegate(null, new String[]{"test"});
        assertNotNull(value);
        assertEquals("test", value.toString());
    }

    @Test
    public void testCanCreateFromObjectWith() {
        ValueInstantiator instantiator = new ObjectWithStringValueInstantiator();
        assertTrue(instantiator.canCreateFromObjectWith());
        Object[] args = new Object[]{new Integer(1), new Double(2.0)};
        Object value = instantiator.createFromObjectWith(null, args);
        assertNotNull(value);
        assertEquals(1, value.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCannotCreateFromObjectWithNullArguments() {
        ValueInstantiator instantiator = new ObjectWithStringValueInstantiator();
        instantiator.createFromObjectWith(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotCreateFromObjectWithInvalidArguments() {
        ValueInstantiator instantiator = new ObjectWithInvalidArgumentValueInstantiator();
        Object[] args = new Object[]{new Integer(1), new String("test")};
        instantiator.createFromObjectWith(null, args);
    }

    @Test
    public void testGetFromObjectArguments() {
        ValueInstantiator instantiator = new ObjectWithTwoStringValuesInstantiator();
        DeserializationConfig config = new DeserializationConfig();
        SettableBeanProperty[] properties = instantiator.getFromObjectArguments(config);
        assertEquals(2, properties.length);
        assertEquals("property1", properties[0].getName());
        assertEquals("java.lang.String", properties[0].getType().getName());
        assertEquals("property2", properties[1].getName());
        assertEquals("java.lang.String", properties[1].getType().getName());
    }

    @Test(expected = NullPointerException.class)
    public void testGetFromObjectArgumentsNullConfig() {
        ValueInstantiator instantiator = new ObjectWithTwoStringValuesInstantiator();
        instantiator.getFromObjectArguments(null);
    }

    @Test
    public void testGetDelegateType() {
        ValueInstantiator instantiator = new ArrayStringValueInstantiator();
        DeserializationConfig config = new DeserializationConfig();
        JavaType delegateType = instantiator.getDelegateType(config);
        assertNotNull(delegateType);
        assertEquals("java.lang.String[]", delegateType.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testGetDelegateTypeNullConfig() {
        ValueInstantiator instantiator = new ArrayStringValueInstantiator();
        instantiator.getDelegateType(null);
    }

    @Test
    public void testCreateFromObjectWithDelegate() throws IOException {
        ValueInstantiator instantiator = new ObjectWithTwoStringValuesInstantiator();
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("property1", "test1");
        node.put("property2", "test2");
        Object value = instantiator.createFromObjectWithDelegate(new ObjectMapper(), node);
        assertNotNull(value);
        assertEquals("test1", value.getProperty("property1").asText());
        assertEquals("test2", value.getProperty("property2").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromObjectWithDelegateInvalidJson() throws IOException {
        ValueInstantiator instantiator = new ObjectWithTwoStringValuesInstantiator();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("property1", 123);
        node.put("property2", new Object());
        instantiator.createFromObjectWithDelegate(mapper, node);
    }
}

class StringValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateFromString() {
        return true;
    }

    @Override
    public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
        return value;
    }
}

class IntValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateFromInt() {
        return true;
    }

    @Override
    public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
        return Integer.valueOf(value);
    }
}

class LongValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateFromLong() {
        return true;
    }

    @Override
    public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
        return Long.valueOf(value);
    }
}

class DoubleValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateFromDouble() {
        return true;
    }

    @Override
    public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
        return Double.valueOf(value);
    }
}

class BooleanValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateFromBoolean() {
        return true;
    }

    @Override
    public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
        return Boolean.valueOf(value);
    }
}

class ArrayStringValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateUsingArrayDelegate() {
        return true;
    }

    @Override
    public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
        return ((String[]) delegate)[0];
    }
}

class ObjectWithStringValueInstantiator extends ValueInstantiator {
    @Override
    public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
        return new SettableBeanProperty[]{
                new AnnotatedStandardProperty("property1", String.class),
                new AnnotatedStandardProperty("property2", String.class)
        };
    }

    @Override
    public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
        return new ObjectWithStringValues((String)args[0], (String)args[1]);
    }
}

class ObjectWithInvalidArgumentValueInstantiator extends ValueInstantiator {
    @Override
    public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
        return new SettableBeanProperty[]{
                new AnnotatedStandardProperty("property1", String.class),
                new AnnotatedStandardProperty("property2", Integer.class)
        };
    }

    @Override
    public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
        throw new IllegalArgumentException("Invalid argument type for property 'property2'");
    }
}

class ObjectWithTwoStringValuesInstantiator extends ValueInstantiator {
    @Override
    public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
        return new ObjectWithTwoStringValues((String)args[0], (String)args[1]);
    }

    @Override
    public JavaType getDelegateType(DeserializationConfig config) {
        return PropertyValueBuffer.standardAnnotatedClass(String[].class);
    }
}

class ObjectWithTwoStringValues {
    final String property1;
    final String property2;

    ObjectWithTwoStringValues(String p1, String p2) {
        property1 = p1;
        property2 = p2;
    }

    public String getProperty(String name) {
        if ("property1".equals(name)) {
            return property1;
        } else if ("property2".equals(name)) {
            return property2;
        }
        throw new IllegalArgumentException("Unknown property: " + name);
    }
}