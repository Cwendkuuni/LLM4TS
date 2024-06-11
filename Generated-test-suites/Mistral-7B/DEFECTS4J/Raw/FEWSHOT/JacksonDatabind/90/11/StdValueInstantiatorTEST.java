package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test cases for {@link StdValueInstantiator} class.
 */
@JacksonStdImpl
public class StdValueInstantiatorTest {

    private DeserializationConfig createContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getConfig();
    }

    @Test
    public void testConfigureFromObjectSettings() {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, ValueType.class);
        AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);
        AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
        JavaType delegateType = mock(JavaType.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[0];
        AnnotatedWithParams withArgsCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] constructorArguments = new SettableBeanProperty[0];

        instantiator.configureFromObjectSettings(defaultCreator, delegateCreator, delegateType, delegateArguments,
                withArgsCreator, constructorArguments);

        assertEquals(defaultCreator, instantiator._defaultCreator);
        assertEquals(delegateCreator, instantiator._delegateCreator);
        assertEquals(delegateType, instantiator._delegateType);
        assertArrayEquals(delegateArguments, instantiator._delegateArguments);
        assertEquals(withArgsCreator, instantiator._withArgsCreator);
        assertArrayEquals(constructorArguments, instantiator._constructorArguments);
    }

    @Test
    public void testGetValueTypeDesc() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertEquals("ValueType", instantiator.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertEquals(ValueType.class, instantiator.getValueClass());
    }

    @Test
    public void testCanCreateFromString() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertFalse(instantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertFalse(instantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertFalse(instantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertFalse(instantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertFalse(instantiator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertFalse(instantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertFalse(instantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertFalse(instantiator.canCreateUsingArrayDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertFalse(instantiator.canCreateFromObjectWith());
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, ValueType.class);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);

        when(creator.call()).thenReturn(new ValueType());
        instantiator._defaultCreator = creator;

        assertEquals(new ValueType(), instantiator.createUsingDefault(context));
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, ValueType.class);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        Object[] args = new Object[]{new ObjectNode(), new ObjectNode()};

        when(creator.call(args)).thenReturn(new ValueType("arg1", "arg2"));
        instantiator._withArgsCreator = creator;

        assertEquals(new ValueType("arg1", "arg2"), instantiator.createFromObjectWith(context, args));
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, ValueType.class);
        AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
        JavaType delegateType = mock(JavaType.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[0];

        when(delegateCreator.call()).thenReturn(new ValueType());
        instantiator._delegateCreator = delegateCreator;
        instantiator._delegateType = delegateType;

        assertEquals(new ValueType(), instantiator.createUsingDelegate(context, new Object()));
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, ValueType[].class);
        AnnotatedWithParams arrayDelegateCreator = mock(AnnotatedWithParams.class);
        JavaType arrayDelegateType = mock(JavaType.class);
        SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[0];

        when(arrayDelegateCreator.call()).thenReturn(new ValueType[2]);
        instantiator._arrayDelegateCreator = arrayDelegateCreator;
        instantiator._arrayDelegateType = arrayDelegateType;

        assertEquals(new ValueType[2], instantiator.createUsingArrayDelegate(context, new Object()));
    }

    @Test
    public void testCreateFromString() throws IOException {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, String.class);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);

        when(creator.call1("test")).thenReturn("test");
        instantiator._fromStringCreator = creator;

        assertEquals("test", instantiator.createFromString(context, "test"));
    }

    @Test
    public void testCreateFromInt() throws IOException {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, Integer.class);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);

        when(creator.call1(Integer.valueOf(1))).thenReturn(1);
        instantiator._fromIntCreator = creator;

        assertEquals(1, instantiator.createFromInt(context, 1));
    }

    @Test
    public void testCreateFromLong() throws IOException {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, Long.class);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);

        when(creator.call1(Long.valueOf(1L))).thenReturn(1L);
        instantiator._fromLongCreator = creator;

        assertEquals(1L, instantiator.createFromLong(context, 1L));
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, Double.class);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);

        when(creator.call1(Double.valueOf(1.0))).thenReturn(1.0);
        instantiator._fromDoubleCreator = creator;

        assertEquals(1.0, instantiator.createFromDouble(context, 1.0), 0.001);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, Boolean.class);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);

        when(creator.call1(Boolean.TRUE)).thenReturn(true);
        instantiator._fromBooleanCreator = creator;

        assertEquals(true, instantiator.createFromBoolean(context, true));
    }

    @Test
    public void testGetDelegateCreator() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertNull(instantiator.getDelegateCreator());
    }

    @Test
    public void testGetArrayDelegateCreator() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType[].class);
        assertNull(instantiator.getArrayDelegateCreator());
    }

    @Test
    public void testgetDefaultCreator() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertNull(instantiator.getDefaultCreator());
    }

    @Test
    public void testGetWithArgsCreator() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertNull(instantiator.getWithArgsCreator());
    }

    @Test
    public void testGetIncompleteParameter() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, ValueType.class);
        assertNull(instantiator.getIncompleteParameter());
    }

    @Test
    public void testCreateFromObjectWith_NullArguments() throws IOException {
        DeserializationConfig context = createContext();
        StdValueInstantiator instantiator = new StdValueInstantiator(context, ValueType.class);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        Object[] args = null;

        when(creator.call(args)).thenReturn(new ValueType());
        instantiator._withArgsCreator = creator;

        assertThrows(IllegalArgumentException.class, () -> instantiator.createFromObjectWith(context, args));
    }
}

class ValueType {

    @JsonCreator
    public static ValueType valueOf(@JsonProperty("arg1") String arg1, @JsonProperty("arg2") String arg2) {
        return new ValueType(arg1, arg2);
    }

    @JsonProperty
    private final String arg1;
    @JsonProperty
    private final String arg2;

    public ValueType() {
        this(null, null);
    }

    public ValueType(String arg1, String arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public String toString() {
        return ToStringSerializer.instance.serializeValuesAsStrings(this);
    }
}