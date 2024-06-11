package com.example; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class ValueInstantiatorTest {

    private DeserializationConfig config;
    private ObjectMapper mapper;
    private ValueInstantiator instantiator;

    @Before
    public void setup() {
        config = mock(DeserializationConfig.class);
        mapper = new ObjectMapper();
        mapper.setConfig(config);
        instantiator = new Base(String.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValueTypeDesc() {
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() {
        assertEquals(String.class, instantiator.getValueClass());
    }

    @Test
    public void testCanInstantiate() {
        assertFalse(instantiator.canInstantiate());
    }

    @Test
    public void testCanCreateFromString() {
        assertFalse(instantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        assertFalse(instantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        assertFalse(instantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        assertFalse(instantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        assertFalse(instantiator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() {
        assertFalse(instantiator.canCreateUsingDefault());

        ValueInstantiator stubInstantiator = mock(ValueInstantiator.class);
        when(stubInstantiator.getDefaultCreator()).thenReturn(mock(AnnotatedWithParams.class));
        instantiator = new Base(stubInstantiator) {
            @Override
            public AnnotatedWithParams getDefaultCreator() {
                return super.getDefaultCreator();
            }
        };

        assertTrue(instantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        assertFalse(instantiator.canCreateUsingDelegate());

        ValueInstantiator stubInstantiator = mock(ValueInstantiator.class);
        when(stubInstantiator.getDelegateCreator()).thenReturn(mock(AnnotatedWithParams.class));
        instantiator = new Base(stubInstantiator) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return super.getDelegateCreator();
            }
        };

        Set<JavaType> expectedTypes = new HashSet<>();
        expectedTypes.add(javaTypeOf(int.class));
        when(config.findValueDeserializer(eq(Integer.TYPE), eq(config)))
                .thenReturn(mock(JsonDeserializer.class));

        when(stubInstantiator.canCreateUsingDelegate())
                .thenReturn(true)
                .thenReturn(false);
        when(stubInstantiator.getDelegateType(config))
                .thenReturn(javaTypeOf(int.class));

        ValueInstantiator delegateInstantiator = mock(ValueInstantiator.class);
        instantiator = new Base(delegateInstantiator) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return super.getDelegateCreator();
            }

            @Override
            public JavaType getDelegateType(DeserializationConfig ctxt) {
                return super.getDelegateType(ctxt);
            }
        };

        assertTrue(instantiator.canCreateUsingDelegate());
        assertEquals(expectedTypes, instantiator.getDelegateType(config).getParameterTypes());
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        ValueInstantiator stubInstantiator = mock(ValueInstantiator.class);
        when(stubInstantiator.getDefaultCreator()).thenReturn(mock(AnnotatedWithParams.class));
        instantiator = new Base(stubInstantiator) {
            @Override
            public AnnotatedWithParams getDefaultCreator() {
                return super.getDefaultCreator();
            }
        };

        ArgumentCaptor<DeserializationContext> contextCaptor = ArgumentCaptor.forClass(DeserializationContext.class);
        Object result = instantiator.createUsingDefault(contextCaptor.capture());
        verify(stubInstantiator).getDefaultCreator();
        verify(contextCaptor.getValue()).handleMissingInstantiator(eq(String.class), eq(mapper.getParser()), isA(String.class));
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        Set<JavaType> expectedTypes = new HashSet<>();
        expectedTypes.add(javaTypeOf(int.class));
        expectedTypes.add(javaTypeOf(String.class));

        when(config.findValueDeserializer(eq(Integer.TYPE), eq(config)))
                .thenReturn(mock(JsonDeserializer.class));
        when(config.findValueDeserializer(eq(String.class), eq(config)))
                .thenReturn(mock(JsonDeserializer.class));

        when(config.findValueDeserializer(eq(Object[].class), eq(config)))
                .thenReturn(mock(JsonDeserializer.class));

        Object[] args = new Object[] { 1, "test" };
        when(config.handleAllSuppliedDeserializers(eq(args), eq(config)))
                .thenReturn(args);

        ValueInstantiator delegateInstantiator = mock(ValueInstantiator.class);
        instantiator = new Base(delegateInstantiator) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return super.getDelegateCreator();
            }

            @Override
            public JavaType getDelegateType(DeserializationConfig ctxt) {
                return super.getDelegateType(ctxt);
            }

            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig ctxt) {
                return new SettableBeanProperty[] { property("int", int.class), property("string", String.class) };
            }
        };

        Object result = instantiator.createFromObjectWith(config, args);
        verify(delegateInstantiator).createFromObjectWith(eq(config), eq(args));
        assertEquals(expectedTypes, instantiator.getDelegateType(config).getParameterTypes());
        assertEquals(args, result);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        Object delegate = new Object() {};
        ValueInstantiator delegateInstantiator = new StdValueInstantiator(String.class);

        when(config.findValueDeserializer(eq(String.class), eq(config)))
                .thenReturn(mock(JsonDeserializer.class));

        instantiator = new Base(delegateInstantiator) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return super.getDelegateCreator();
            }

            @Override
            public JavaType getDelegateType(DeserializationConfig ctxt) {
                return super.getDelegateType(ctxt);
            }

            @Override
            public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                return delegateInstantiator.createFromString(ctxt, "test");
            }
        };

        Object result = instantiator.createUsingDelegate(config, delegate);
        verify(delegateInstantiator).createFromString(eq(config), eq("test"));
        assertEquals("test", result);
    }

    @Test
    public void testCreateFromString() throws IOException {
        when(config.findValueDeserializer(eq(String.class), eq(config)))
                .thenReturn(mock(JsonDeserializer.class));

        String value = "test";
        Object result = instantiator.createFromString(config, value);
        verify(config).handleMissingInstantiator(eq(String.class), eq(mapper.getParser()), eq("test"));
        assertEquals(value, result);
    }

    @Test
    public void testCreateFromInt() throws IOException {
        when(config.findValueDeserializer(eq(Integer.TYPE), eq(config)))
                .thenReturn(mock(JsonDeserializer.class));

        int value = 1;
        Object result = instantiator.createFromInt(config, value);
        verify(config).handleMissingInstantiator(eq(String.class), eq(mapper.getParser()), eq(value));
        assertEquals(Integer.valueOf(value), result);
    }

    @Test
    public void testCreateFromLong() throws IOException {
        when(config.findValueDeserializer(eq(Long.TYPE), eq(config)))
                .thenReturn(mock(JsonDeserializer.class));

        long value = 1L;
        Object result = instantiator.createFromLong(config, value);
        verify(config).handleMissingInstantiator(eq(String.class), eq(mapper.getParser()), eq(value));
        assertEquals(Long.valueOf(value), result);
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        when(config.findValueDeserializer(eq(Double.TYPE), eq(config)))
                .thenReturn(mock(JsonDeserializer.class));

        double value = 1.0;
        Object result = instantiator.createFromDouble(config, value);
        verify(config).handleMissingInstantiator(eq(String.class), eq(mapper.getParser()), eq(value));
        assertEquals(Double.valueOf(value), result);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        when(config.findValueDeserializer(eq(Boolean.TYPE), eq(config)))
                .thenReturn(mock(JsonDeserializer.class));

        boolean value = true;
        Object result = instantiator.createFromBoolean(config, value);
        verify(config).handleMissingInstantiator(eq(String.class), eq(mapper.getParser()), eq(value));
        assertEquals(Boolean.valueOf(value), result);
    }

    private JavaType javaTypeOf(Class<?> clazz) {
        return mapper.getTypeFactory().constructType(clazz);
    }

    private SettableBeanProperty property(String name, Class<?> type) {
        return new SettableBeanProperty.Std(name, type);
    }
}