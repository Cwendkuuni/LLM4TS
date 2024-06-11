package com.example; // Replace this with the actual package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.Parser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanPropertyDescriptor;
import com.fasterxml.jackson.databind.deser.CreatorProvider;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.ObjectDeserializer;
import com.fasterxml.jackson.databind.deser.StdValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.CreatorProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyName;
import com.fasterxml.jackson.databind.deser.std.StdCreatorProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class ValueInstantiatorTest {

    private DeserializationContext context;
    private ObjectMapper objectMapper;
    private ValueInstantiator valueInstantiator;
    private CreatorProperty creatorProperty;

    @Before
    public void setup() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.context = mock(DeserializationContext.class);
        when(context.findValueDeserializer(Object.class)).thenReturn(null);
        when(context.getParser()).thenReturn(mock(Parser.class));
        when(context.handleMissingValue(Object.class)).thenReturn(null);
        when(context.handleMissingInstantiator(Object.class, context.getParser(),
                "no default constructor found for class [{}]"))
                .thenThrow(RuntimeException.class);

        this.valueInstantiator = mock(ValueInstantiator.class);
        when(valueInstantiator.canCreateUsingDefault()).thenReturn(false);

        this.creatorProperty = new CreatorProperty(mock(BeanPropertyDefinition.class),
                new StdCreatorProperty().name("creatorProperty"));
    }

    @Test
    public void testGetValueClass() {
        ValueInstantiator testInstance = new ValueInstantiator.Base(String.class);
        assertEquals(String.class, testInstance.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() {
        ValueInstantiator testInstance = new ValueInstantiator.Base(String.class);
        assertEquals("java.lang.String", testInstance.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate_Default() {
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        assertFalse(testInstance.canInstantiate());
    }

    @Test
    public void testCanInstantiate_WithDelegate() {
        ValueInstantiator testInstance = new ValueInstantiator() {
            @Override
            public boolean canCreateUsingDelegate() {
                return true;
            }
        };
        assertFalse(testInstance.canInstantiate());
    }

    @Test
    public void testCanInstantiate_WithDelegate_WhenDelegateExists() {
        ValueInstantiator testInstance = new ValueInstantiator() {
            @Override
            public boolean canCreateUsingDelegate() {
                return true;
            }

            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return mock(AnnotatedWithParams.class);
            }
        };
        assertTrue(testInstance.canInstantiate());
    }

    @Test(expected = RuntimeException.class)
    public void testCreateUsingDefault() throws IOException {
        when(valueInstantiator.getDefaultCreator()).thenReturn(mock(AnnotatedConstructor.class));
        Object value = valueInstantiator.createUsingDefault(context);
        assertNull(value);
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class,
                String.class, Integer.class);
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        ObjectDeserializer deserializer = mock(ObjectDeserializer.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        CreatorProvider creatorProvider = mock(CreatorProvider.class);
        DeserializationProblemHandler deserializationProblemHandler = mock(
                DeserializationProblemHandler.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Type type = mock(Type.class);

        when(valueInstantiator.getDefaultCreator()).thenReturn(null);
        when(valueInstantiator.canCreateFromObjectWith()).thenReturn(true);
        when(valueInstantiator.getFromObjectArguments(context)).thenReturn(new SettableBeanProperty[] { creatorProperty });
        when(valueInstantiator.createFromObjectWith(context, creatorProperty)).thenReturn(new Object() {});

        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedClass.getConstructors()).thenReturn(new AnnotatedConstructor[] { annotatedMethod });
        when(annotatedMethod.getParameters()).thenReturn(new AnnotatedParameter[] { creatorProperty });

        when(deserializer.getParameterDescriptors()).thenReturn(new BeanPropertyDescriptor[0]);
        when(deserializer.getValueInstantiator()).thenReturn(valueInstantiator);
        when(objectMapper.getValueFactory()).thenReturn(new ValueFactoryImpl());
        when(creatorProvider.findValueInstantiator(javaType)).thenReturn(deserializer);

        ValueFactory valueFactory = objectMapper.getTypeFactory();
        Object value = testInstance.createFromObjectWith(context, valueFactory.constructValueContainer(Map.class));
        assertEquals(new Object(), value);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateFromObjectWith_NoCreator() throws IOException {
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        testInstance.createFromObjectWith(context, null);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateFromObjectWith_NullArguments() throws IOException {
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        testInstance.createFromObjectWith(context, new Object[0]);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateFromObjectWith_NullCreatorProperty() throws IOException {
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        testInstance.createFromObjectWith(context, null, new Object[0]);
    }

    @Test
    public void testCreateFromObjectWith_NoCreatorSpecified() throws Exception {
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        testInstance.createFromObjectWith(context, new Object[0], new Object[0]);
    }

    @Test(expected = IOException.class)
    public void testCreateFromString() throws IOException {
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        when(testInstance.canCreateFromString()).thenReturn(false);
        String value = "test";
        testInstance.createFromString(context, value);
    }

    @Test
    public void testCreateFromString_StringToBoolean() throws IOException {
        ValueInstantiator testInstance = new ValueInstantiator() {
            @JsonCreator
            @JsonProperty
            public static Boolean createFromString(DeserializationContext ctxt, String value) throws IOException {
                return Boolean.parseBoolean(value);
            }

            @Override
            public boolean canCreateFromString() {
                return true;
            }
        };
        assertTrue(testInstance.canInstantiate());
        boolean value = testInstance.createFromString(context, "true");
        assertEquals(true, value);
    }

    @Test(expected = IOException.class)
    public void testCreateFromInt() throws IOException {
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        when(testInstance.canCreateFromInt()).thenReturn(false);
        int value = 5;
        testInstance.createFromInt(context, value);
    }

    @Test
    public void testCreateFromInt_IntToBoolean() throws IOException {
        ValueInstantiator testInstance = new ValueInstantiator() {
            @JsonCreator
            @JsonProperty
            public static Boolean createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return value > 0;
            }

            @Override
            public boolean canCreateFromInt() {
                return true;
            }
        };
        assertTrue(testInstance.canInstantiate());
        boolean value = testInstance.createFromInt(context, 5);
        assertEquals(true, value);
    }

    @Test(expected = IOException.class)
    public void testCreateFromLong() throws IOException {
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        when(testInstance.canCreateFromLong()).thenReturn(false);
        long value = 10L;
        testInstance.createFromLong(context, value);
    }

    @Test
    public void testCreateFromLong_LongToBoolean() throws IOException {
        ValueInstantiator testInstance = new ValueInstantiator() {
            @JsonCreator
            @JsonProperty
            public static Boolean createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return value > 0;
            }

            @Override
            public boolean canCreateFromLong() {
                return true;
            }
        };
        assertTrue(testInstance.canInstantiate());
        boolean value = testInstance.createFromLong(context, 10L);
        assertEquals(true, value);
    }

    @Test(expected = IOException.class)
    public void testCreateFromDouble() throws IOException {
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        when(testInstance.canCreateFromDouble()).thenReturn(false);
        double value = 3.14;
        testInstance.createFromDouble(context, value);
    }

    @Test
    public void testCreateFromDouble_DoubleToBoolean() throws IOException {
        ValueInstantiator testInstance = new ValueInstantiator() {
            @JsonCreator
            @JsonProperty
            public static Boolean createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                return value > 0.0;
            }

            @Override
            public boolean canCreateFromDouble() {
                return true;
            }
        };
        assertTrue(testInstance.canInstantiate());
        boolean value = testInstance.createFromDouble(context, 3.14);
        assertEquals(true, value);
    }

    @Test(expected = IOException.class)
    public void testCreateFromBoolean() throws IOException {
        ValueInstantiator testInstance = mock(ValueInstantiator.class);
        when(testInstance.canCreateFromBoolean()).thenReturn(false);
        boolean value = true;
        testInstance.createFromBoolean(context, value);
    }

    @Test
    public void testCreateFromBoolean_BooleanToBoolean() throws IOException {
        ValueInstantiator testInstance = new ValueInstantiator() {
            @JsonCreator
            @JsonProperty
            public static Boolean createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
                return value;
            }

            @Override
            public boolean canCreateFromBoolean() {
                return true;
            }
        };
        assertTrue(testInstance.canInstantiate());
        boolean value = testInstance.createFromBoolean(context, true);
        assertEquals(true, value);
    }
}