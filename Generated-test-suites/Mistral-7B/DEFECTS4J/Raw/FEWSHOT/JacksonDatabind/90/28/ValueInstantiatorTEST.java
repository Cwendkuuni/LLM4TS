package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class ValueInstantiatorTest {

    private DeserializationContext context;
    private SettableBeanProperty[] properties;
    private ValueInstantiator valueInstantiator;

    @Before
    public void setup() {
        context = mock(DeserializationContext.class);
        valueInstantiator = new ValueInstantiator.Base(String.class);
        properties = new SettableBeanProperty[]{new SettableBeanProperty(), new SettableBeanProperty()};
    }

    @Test
    public void testGetValueClass() throws Exception {
        assertEquals(String.class, valueInstantiator.getValueClass());
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
    public void testCanCreateUsingDefault() throws Exception {
        assertFalse(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() throws Exception {
        assertFalse(valueInstantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        assertFalse(valueInstantiator.canCreateFromObjectWith());
    }

    @Test
    public void testGetFromObjectArguments() throws Exception {
        assertEquals(0, valueInstantiator.getFromObjectArguments(context).size());
    }

    @Test
    public void testGetDelegateType() throws Exception {
        assertNull(valueInstantiator.getDelegateType(context));
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        when(context.getParser()).thenReturn(mock(Parser.class));
        when(context.handleMissingInstantiator(any(), any(), anyString())).thenThrow(IOException.class);

        AnnotatedConstructor constructor = mock(AnnotatedConstructor.class);
        when(context.findConstractor(String.class, null)).thenReturn(constructor);

        Object result = valueInstantiator.createUsingDefault(context);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no default no-arguments constructor found"));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        assertNull(result);
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        when(context.getParser()).thenReturn(mock(Parser.class));
        when(context.handleMissingInstantiator(any(), any(), anyString())).thenThrow(IOException.class);

        AnnotatedWithParams params = mock(AnnotatedWithParams.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(params.getAnnotated()).thenReturn(member);
        when(member.getMember()).thenReturn(new Object(){});

        AnnotatedConstructor constructor = mock(AnnotatedConstructor.class);
        when(context.findConstractor(eq(String.class), any())).thenReturn(constructor);
        when(constructor.getParameterCount()).thenReturn(2);

        SettableBeanProperty[] args = new SettableBeanProperty[]{new SettableBeanProperty(), new SettableBeanProperty()};
        Object result = valueInstantiator.createFromObjectWith(context, args);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no creator with arguments specified"));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        verifyNoMoreInteractions(context.findConstractor(eq(String.class), any()));
        assertNull(result);

        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        when(context.findValueHandler(eq(SettableBeanProperty[].class))).thenReturn(mock(PropertyWriter.class));
        when(buffer.getParameters(args)).thenReturn(new Object[]{new Object(){}, new Object(){}});

        result = valueInstantiator.createFromObjectWith(context, properties, buffer);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no creator with arguments specified"));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        verifyNoMoreInteractions(context.findConstractor(eq(String.class), any()));
        verify(context).findValueHandler(eq(SettableBeanProperty[].class));
        verify(buffer).getParameters(args);
        verifyNoMoreInteractions(buffer);
        assertNull(result);

        when(context.getParser().readValueAs(String[].class, null)).thenReturn(new Object[]{new Object(){}, new Object(){}});
        when(constructor.call(anyArray())).thenReturn(new Object(){});

        result = valueInstantiator.createFromObjectWith(context, properties, buffer);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no creator with arguments specified"));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        verifyNoMoreInteractions(context.findConstractor(eq(String.class), any()));
        verify(context).findValueHandler(eq(SettableBeanProperty[].class));
        verify(buffer).getParameters(args);
        verify(buffer).resetAndStart();
        verify(buffer).writeValueAs(any(PropertyWriter.class), anyArray());
        verify(context.getParser()).readValueAs(String[].class, null);
        verify(constructor).call(anyArray());
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        verifyNoMoreInteractions(context.findConstractor(eq(String.class), any()));
        assertNotNull(result);
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        when(context.getParser()).thenReturn(mock(Parser.class));
        when(context.handleMissingInstantiator(any(), any(), anyString())).thenThrow(IOException.class);

        Object delegate = new Object(){};
        Object result = valueInstantiator.createUsingDelegate(context, delegate);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no delegate creator specified"));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        assertNull(result);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        when(context.getParser()).thenReturn(mock(Parser.class));
        when(context.handleMissingInstantiator(any(), any(), anyString())).thenThrow(IOException.class);

        Object delegate = new Object(){};
        Object result = valueInstantiator.createUsingArrayDelegate(context, delegate);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no array delegate creator specified"));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        assertNull(result);
    }

    @Test
    public void testCreateFromString() throws Exception {
        when(context.getParser()).thenReturn(mock(Parser.class));
        when(context.handleMissingInstantiator(any(), any(), anyString())).thenThrow(IOException.class);

        String value = "value";
        Object result = valueInstantiator.createFromString(context, value);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no String-argument constructor/factory method to deserialize from String value ('%s')", eq(value)));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        assertNull(result);
    }

    @Test
    public void testCreateFromInt() throws Exception {
        when(context.getParser()).thenReturn(mock(Parser.class));
        when(context.handleMissingInstantiator(any(), any(), anyString())).thenThrow(IOException.class);

        int value = 1;
        Object result = valueInstantiator.createFromInt(context, value);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no int/Int-argument constructor/factory method to deserialize from Number value (%s)", eq(value)));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        assertNull(result);
    }

    @Test
    public void testCreateFromLong() throws Exception {
        when(context.getParser()).thenReturn(mock(Parser.class));
        when(context.handleMissingInstantiator(any(), any(), anyString())).thenThrow(IOException.class);

        long value = 1L;
        Object result = valueInstantiator.createFromLong(context, value);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no long/Long-argument constructor/factory method to deserialize from Number value (%s)", eq(value)));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        assertNull(result);
    }

    @Test
    public void testCreateFromDouble() throws Exception {
        when(context.getParser()).thenReturn(mock(Parser.class));
        when(context.handleMissingInstantiator(any(), any(), anyString())).thenThrow(IOException.class);

        double value = 1.0;
        Object result = valueInstantiator.createFromDouble(context, value);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no double/Double-argument constructor/factory method to deserialize from Number value (%s)", eq(value)));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        assertNull(result);
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        when(context.getParser()).thenReturn(mock(Parser.class));
        when(context.handleMissingInstantiator(any(), any(), anyString())).thenThrow(IOException.class);

        boolean value = true;
        Object result = valueInstantiator.createFromBoolean(context, value);
        verify(context).handleMissingInstantiator(eq(String.class), any(), eq("no boolean/Boolean-argument constructor/factory method to deserialize from boolean value (%s)", eq(value)));
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(context.getParser());
        assertNull(result);
    }

    @Test
    public void testGetDefaultCreator() throws Exception {
        Class<?> defaultCreatorClass = Object.class;
        when(context.findConstractor(eq(defaultCreatorClass), null)).thenReturn(mock(AnnotatedConstructor.class));

        Object result = valueInstantiator.getDefaultCreator();
        assertNull(result);
        verify(context).findConstractor(eq(defaultCreatorClass), null);
        verifyNoMoreInteractions(context);
    }

    @Test
    public void testGetDelegateCreator() throws Exception {
        Object delegateCreator = new Object(){};
        AnnotatedWithParams delegateCreatorAnnotation = mock(AnnotatedWithParams.class);
        when(delegateCreatorAnnotation.getAnnotated()).thenReturn(mock(AnnotatedMember.class));
        doReturn(delegateCreator).when(delegateCreatorAnnotation).getAnnotatedMember();

        when(context.findConstractor(eq(delegateCreator.getClass()), null)).thenReturn(mock(AnnotatedConstructor.class));
        when(context.findConstractor(any(), any())).thenReturn(mock(AnnotatedConstructor.class));

        Object result = valueInstantiator.getDelegateCreator();
        assertNull(result);
        verify(context).findConstractor(eq(delegateCreator.getClass()), null);
        verify(context).findConstractor(any(), any());
        verifyNoMoreInteractions(context);
    }

    @Test
    public void testGetArrayDelegateCreator() throws Exception {
        Object arrayDelegateCreator = new Object(){};
        JavaType arrayDelegateType = mock(JavaType.class);
        when(context.findConstractor(eq(arrayDelegateType), null)).thenReturn(mock(AnnotatedConstructor.class));

        when(valueInstantiator.getDelegateType(context)).thenReturn(arrayDelegateType);

        Object result = valueInstantiator.getArrayDelegateCreator();
        assertNull(result);
        verify(valueInstantiator).getDelegateType(context);
        verify(context).findConstractor(eq(arrayDelegateType), null);
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(valueInstantiator);
    }

    @Test
    public void testGetWithArgsCreator() throws Exception {
        AnnotatedWithParams withArgsCreatorAnnotation = mock(AnnotatedWithParams.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(withArgsCreatorAnnotation.getAnnotated()).thenReturn(member);
        when(member.getMember()).thenReturn(new Constructor<Object>(String.class, String.class){});

        when(context.findConstractor(eq(String.class), any())).thenReturn(mock(AnnotatedConstructor.class));

        Object result = valueInstantiator.getWithArgsCreator();
        assertNull(result);
        verify(context).findConstractor(eq(String.class), any());
        verifyNoMoreInteractions(context);
    }

    @Test
    public void testGetIncompleteParameter() throws Exception {
        AnnotatedWithParams incompleteCreatorAnnotation = mock(AnnotatedWithParams.class);
        AnnotatedParameter incompleteParameter = mock(AnnotatedParameter.class);

        when(incompleteCreatorAnnotation.getAnnotated()).thenReturn(mock(AnnotatedMember.class));
        when(incompleteCreatorAnnotation.getParameter(0)).thenReturn(incompleteParameter);

        Object result = valueInstantiator.getIncompleteParameter();
        assertSame(incompleteParameter, result);
        verifyNoMoreInteractions(valueInstantiator);
    }
}