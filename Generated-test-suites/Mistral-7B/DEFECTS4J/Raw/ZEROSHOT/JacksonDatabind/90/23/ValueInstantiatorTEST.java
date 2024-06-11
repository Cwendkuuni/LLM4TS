package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.StdName;

public class ValueInstantiatorTest {

    private ValueInstantiator valueInstantiator;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        valueInstantiator = mapper.getTypeFactory().constructValueType(ValueInstantiator.class, String.class);
    }

    @Test
    public void testGetValueClass() throws Exception {
        assertEquals(Object.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() throws Exception {
        assertEquals(String.class.getName(), valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() throws Exception {
        assertFalse(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCanCreateFromString() throws Exception {
        assertFalse(valueInstantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() throws Exception {
        assertFalse(valueInstantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() throws Exception {
        assertFalse(valueInstantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() throws Exception {
        assertFalse(valueInstantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() throws Exception {
        assertFalse(valueInstantiator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        assertFalse(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() throws Exception {
        assertFalse(valueInstantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(StringWithInt.class);
        valueInstantiator = (ValueInstantiator) mapper.getTypeFactory().constructValueType(ValueInstantiator.class, javaType);

        SettableBeanProperty[] settableBeanProperties = valueInstantiator.getFromObjectArguments(mapper.getConfig());
        assertEquals(2, settableBeanProperties.length);

        assertNotNull(settableBeanProperties[0]);
        assertEquals(String.class, settableBeanProperties[0].getPropertyType());
        assertEquals(StdName.FIELD_NAME, settableBeanProperties[0].getName());

        assertNotNull(settableBeanProperties[1]);
        assertEquals(Integer.class, settableBeanProperties[1].getPropertyType());
        assertEquals(StdName.FIELD_NAME_1, settableBeanProperties[1].getName());

        assertTrue(valueInstantiator.canCreateFromObjectWith());
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(StringWithInt.class);
        valueInstantiator = (ValueInstantiator) mapper.getTypeFactory().constructValueType(ValueInstantiator.class, javaType);

        Object obj = valueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { "test", 123 });
        assertEquals("test", obj.getClass().getMethod("getString").invoke(obj));
        assertEquals(123, obj.getClass().getMethod("getInt").invoke(obj));
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        assertFalse(valueInstantiator.canCreateUsingDelegate());

        JavaType javaType = mapper.getTypeFactory().constructType(StringWithInt.class);
        JavaType delegateType = mapper.getTypeFactory().constructType(StringWithIntDelegate.class);

        ValueInstantiator delegateInstantiator = mapper.getTypeFactory().constructValueType(ValueInstantiator.class, delegateType);
        delegateInstantiator.createUsingDelegate(mapper.getDeserializationContext(), new StringWithIntDelegate());

        valueInstantiator = (ValueInstantiator) mapper.getTypeFactory().constructValueType(ValueInstantiator.class, javaType);
        valueInstantiator.setDelegateType(mapper.getConfig(), delegateType);

        assertTrue(valueInstantiator.canCreateUsingDelegate());

        Object obj = valueInstantiator.createUsingDelegate(mapper.getDeserializationContext(), new StringWithIntDelegate());
        assertEquals("test", obj.getClass().getMethod("getString").invoke(obj));
        assertEquals(123, obj.getClass().getMethod("getInt").invoke(obj));
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        assertFalse(valueInstantiator.canCreateUsingArrayDelegate());

        JavaType javaType = mapper.getTypeFactory().constructType(StringWithIntArray.class);
        JavaType delegateType = mapper.getTypeFactory().constructType(StringWithIntArrayDelegate.class);

        ValueInstantiator delegateInstantiator = mapper.getTypeFactory().constructValueType(ValueInstantiator.class, delegateType);
        delegateInstantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), new StringWithIntArrayDelegate());

        valueInstantiator = (ValueInstantiator) mapper.getTypeFactory().constructValueType(ValueInstantiator.class, javaType);
        valueInstantiator.setArrayDelegateType(mapper.getConfig(), delegateType);

        assertTrue(valueInstantiator.canCreateUsingArrayDelegate());

        Object obj = valueInstantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), new StringWithIntArrayDelegate());
        assertEquals("test", obj.getClass().getMethod("getString").invoke(obj));
        assertArrayEquals(new Integer[] { 1, 2, 3 }, obj.getClass().getMethod("getInts").invoke(obj));
    }

    @Test
    public void testCreateFromString() throws Exception {
        ValueInstantiator stringValueInstantiator = mapper.getTypeFactory().constructValueType(ValueInstantiator.class, String.class);

        String test = "test";
        Object obj = stringValueInstantiator.createFromString(mapper.getDeserializationContext(), test);
        assertEquals(test, obj);
    }

    @Test
    public void testCreateFromInt() throws Exception {
        ValueInstantiator intValueInstantiator = mapper.getTypeFactory().constructValueType(ValueInstantiator.class, Integer.class);

        int test = 123;
        Object obj = intValueInstantiator.createFromInt(mapper.getDeserializationContext(), test);
        assertEquals(test, obj);
    }

    @Test
    public void testCreateFromLong() throws Exception {
        ValueInstantiator longValueInstantiator = mapper.getTypeFactory().constructValueType(ValueInstantiator.class, Long.class);

        long test = 123L;
        Object obj = longValueInstantiator.createFromLong(mapper.getDeserializationContext(), test);
        assertEquals(test, obj);
    }

    @Test
    public void testCreateFromDouble() throws Exception {
        ValueInstantiator doubleValueInstantiator = mapper.getTypeFactory().constructValueType(ValueInstantiator.class, Double.class);

        double test = 123.45;
        Object obj = doubleValueInstantiator.createFromDouble(mapper.getDeserializationContext(), test);
        assertEquals(test, obj);
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        ValueInstantiator booleanValueInstantiator = mapper.getTypeFactory().constructValueType(ValueInstantiator.class, Boolean.class);

        boolean test = true;
        Object obj = booleanValueInstantiator.createFromBoolean(mapper.getDeserializationContext(), test);
        assertEquals(test, obj);
    }

    private static class StringWithInt {
        @JsonProperty
        private String string;

        @JsonProperty
        private int intValue;

        public String getString() {
            return string;
        }

        public int getInt() {
            return intValue;
        }
    }

    private static class StringWithIntDelegate {
        @JsonCreator
        public static StringWithInt create(@JsonProperty("str") String str, @JsonProperty("intVal") int intVal) {
            return new StringWithInt(str, intVal);
        }

        private StringWithInt stringWithInt;

        public StringWithInt getStringWithInt() {
            return stringWithInt;
        }

        @JsonProperty
        public String getString() {
            return stringWithInt.getString();
        }

        @JsonProperty
        public int getInt() {
            return stringWithInt.getInt();
        }

        public StringWithInt(String string, int intVal) {
            this.stringWithInt = new StringWithInt();
            BeanUtil.setProperty(this.stringWithInt, "string", string);
            BeanUtil.setProperty(this.stringWithInt, "intValue", intVal);
        }
    }

    private static class StringWithIntArray {
        @JsonProperty
        private String string;

        @JsonProperty
        private int[] ints;

        public String getString() {
            return string;
        }

        public int[] getInts() {
            return ints;
        }
    }

    private static class StringWithIntArrayDelegate {
        @JsonCreator
        public static StringWithIntArray create(@JsonProperty("str") String str, @JsonProperty("ints") int[] ints) {
            return new StringWithIntArray();
        }

        private StringWithIntArray stringWithIntArray;

        public StringWithIntArray getStringWithIntArray() {
            return stringWithIntArray;
        }

        @JsonProperty
        public String getString() {
            return stringWithIntArray.getString();
        }

        @JsonProperty
        public int[] getInts() {
            return stringWithIntArray.getInts();
        }

        public StringWithIntArray() {
            this.stringWithIntArray = new StringWithIntArray();
            BeanUtil.setProperty(this.stringWithIntArray, "string", str);
            if (ints != null) {
                BeanUtil.setProperty(this.stringWithIntArray, "ints", ints);
            }
        }
    }
}

class StringWithIntArray {

}

class StringWithIntArrayDelegate {

    @JsonCreator
    public static StringWithIntArray create(String str, int[] ints) {
        return new StringWithIntArray();
    }

    @JsonProperty
    public String getString() {
        return null;
    }

    @JsonProperty
    public int[] getInts() {
        return ints;
    }

    @JsonProperty
    public void setString(String str) {
    }

    @JsonProperty
    public void setInts(int[] ints) {
    }

    private StringWithIntArray _instance;

    public StringWithIntArray getInstance() {
        if (_instance == null) {
            _instance = new StringWithIntArray();
        }
        return _instance;
    }
}