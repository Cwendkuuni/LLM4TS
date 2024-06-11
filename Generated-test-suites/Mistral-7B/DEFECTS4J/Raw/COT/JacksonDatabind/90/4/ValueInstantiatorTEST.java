package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValueInstantiatorTest {

    private ValueInstantiator valueInstantiator;
    private DeserializationContext context;
    private AnnotatedClass ac;

    @Before
    public void setup() throws Exception {
        valueInstantiator = new ValueInstantiator.Base(String.class);
        context = mock(DeserializationContext.class);
        ac = mock(AnnotatedClass.class);
    }

    @Test
    public void testGetValueClass() throws Exception {
        assertEquals(String.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() throws Exception {
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testGetDefaultCreator() throws Exception {
        assertNull(valueInstantiator.getDefaultCreator());
    }

    @Test
    public void testCanInstantiate() throws Exception {
        assertFalse(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        assertFalse(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        assertFalse(valueInstantiator.canCreateFromObjectWith());
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

    @Test(expected = AssertionError.class)
    public void testCreateFromUnknownSource() throws Exception {
        valueInstantiator.createFromObjectWith(context, new Object[]{}, new PropertyValueBuffer());
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        when(context.handleMissingInstantiator(String.class, Mockito.any(DeserializationParser.class), Mockito.anyString()))
                .thenReturn(new Object());

        assertEquals(new Object(), valueInstantiator.createUsingDefault(context));
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromObjectWithNullArguments() throws Exception {
        valueInstantiator.createFromObjectWith(context, null);
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromObjectWithEmptyArguments() throws Exception {
        valueInstantiator.createFromObjectWith(context, new Object[0]);
    }

    @Test
    public void testCreateFromObjectWithProperties() throws Exception {
        AnnotatedConstructor ac = mock(AnnotatedConstructor.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        ObjectNode node = mock(ObjectNode.class);

        when(ac.getParameters()).thenReturn(new AnnotatedParameter[0]);
        when(ac.getDeclaredAnnotationClasses()).thenReturn(new Class[0]);
        when(propDef.getName()).thenReturn("propertyName");
        when(node.get("propertyName")).thenReturn(Mockito.anyString());

        Set<BeanPropertyDefinition> props = new LinkedHashSet<>();
        props.add(propDef);
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        buffer.setProperties(props);

        ValueInstantiatorInstancer vii = mock(ValueInstantiatorInstancer.class);
        when(vii.createInstance(ac, context)).thenReturn(new Object());

        when(context.findCreator(ac)).thenReturn(vii);

        when(context.handleMissingInstantiator(String.class, Mockito.any(DeserializationParser.class), Mockito.anyString()))
                .thenReturn(new Object());

        Object result = valueInstantiator.createFromObjectWith(context, new Object[0], buffer);
        assertEquals(new Object(), result);
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        when(context.handleMissingInstantiator(String.class, Mockito.any(DeserializationParser.class), Mockito.anyString()))
                .thenReturn(new Object());

        AnnotatedConstructor ac = mock(AnnotatedConstructor.class);
        ValueInstantiatorInstancer vii = mock(ValueInstantiatorInstancer.class);

        when(ac.getParameters()).thenReturn(new AnnotatedParameter[0]);
        when(ac.getDeclaredAnnotationClasses()).thenReturn(new Class[]{JsonDeserialize.class});
        when(ac.getDeclaredAnnotation(JsonDeserialize.class)).thenReturn(new JsonDeserialize());
        when(vii.createInstance(ac, context)).thenReturn(new Object());

        when(context.findDelegateCreator(String.class, ac)).thenReturn(vii);

        Object result = valueInstantiator.createUsingDelegate(context, new Object());
        assertEquals(new Object(), result);
    }

    @Test
    public void testCreateUsingDelegateWithNullDelegate() throws Exception {
        when(context.handleMissingInstantiator(String.class, Mockito.any(DeserializationParser.class), Mockito.anyString()))
                .thenReturn(new Object());

        Object result = valueInstantiator.createUsingDelegate(context, null);
        assertEquals(new Object(), result);
    }

    @Test
    public void testCreateUsingDelegateWithInvalidDelegate() throws Exception {
        when(context.handleMissingInstantiator(String.class, Mockito.any(DeserializationParser.class), Mockito.anyString()))
                .thenReturn(new Object());

        AnnotatedConstructor ac = mock(AnnotatedConstructor.class);
        ValueInstantiatorInstancer vii = mock(ValueInstantiatorInstancer.class);

        when(ac.getParameters()).thenReturn(new AnnotatedParameter[0]);
        when(ac.getDeclaredAnnotationClasses()).thenReturn(new Class[0]);
        when(ac.getDeclaredAnnotation(JsonDeserialize.class)).thenReturn(new JsonDeserialize());

        when(vii.createInstance(ac, context)).thenReturn(Mockito.mock(Object.class));

        when(context.findDelegateCreator(String.class, ac)).thenReturn(vii);
        when(context.getParser().nextValue()).thenReturn(null);

        try {
            valueInstantiator.createUsingDelegate(context, new Object());
            fail();
        } catch (InvocationTargetException e) {
            assertEquals("no delegate creator specified", e.getMessage());
        }
    }

    @Test
    public void testCreateFromInt() throws IOException {
        Object result = valueInstantiator.createFromInt(context, 42);
        assertNull(result);
    }

    @Test
    public void testCreateFromLong() throws IOException {
        Object result = valueInstantiator.createFromLong(context, 42L);
        assertNull(result);
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        Object result = valueInstantiator.createFromDouble(context, 42.0d);
        assertNull(result);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        Object result = valueInstantiator.createFromBoolean(context, true);
        assertNull(result);
    }

    @Test
    public void testCreateFromStringWithNullString() throws IOException {
        Object result = valueInstantiator.createFromString(context, null);
        assertNull(result);
    }

    @Test
    public void testCreateFromStringWithEmptyString() throws IOException {
        Object result = valueInstantiator.createFromString(context, "");
        assertNull(result);
    }

    @Test
    public void testCreateFromStringWithBooleanTrue() throws IOException {
        Object result = valueInstantiator.createFromString(context, "true");
        assertNull(result);
    }

    @Test
    public void testCreateFromStringWithBooleanFalse() throws IOException {
        Object result = valueInstantiator.createFromString(context, "false");
        assertNull(result);
    }

    @Test
    public void testCreateFromStringWithStringValue() throws IOException {
        String value = "test";
        Object result = valueInstantiator.createFromString(context, value);
        assertEquals(value, result);
    }

    @Test
    public void testGetDelegateType() throws IOException {
        JavaType result = valueInstantiator.getDelegateType(context);
        assertNull(result);
    }

    @Test
    public void testGetArrayDelegateType() throws IOException {
        JavaType result = valueInstantiator.getArrayDelegateType(context);
        assertNull(result);
    }

    @Test
    public void testGetFromObjectArguments() throws IOException {
        BeanPropertyDefinition prop1 = mock(BeanPropertyDefinition.class);
        BeanPropertyDefinition prop2 = mock(BeanPropertyDefinition.class);
        BeanPropertyDefinition[] properties = {prop1, prop2};
        SettableBeanProperty[] settableProperties = new SettableBeanProperty[2];
        settableProperties[0] = new SettableBeanProperty(prop1);
        settableProperties[1] = new SettableBeanProperty(prop2);

        when(context.findValueDeserializer(prop1)).thenReturn(mock(StdValueDeserializer.class));
        when(context.findValueDeserializer(prop2)).thenReturn(mock(StdValueDeserializer.class));
        when(context.getParser().nextValue()).thenReturn(Mockito.mock(Object.class));

        assertEquals(Arrays.asList(settableProperties), valueInstantiator.getFromObjectArguments(context));
    }
}