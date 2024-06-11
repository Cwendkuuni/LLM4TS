package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test cases for InnerClassProperty class.
 */
public class InnerClassPropertyTest {

    @Mock
    private JsonDeserializer<Object> valueDeserializer;

    @Mock
    private AnnotatedConstructor<InnerClass> annotatedConstructor;

    @Mock
    private JsonDeserializer<InnerClass> innerClassDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private ObjectNode objectNode;

    @Mock
    private Object bean;

    @InjectMocks
    private InnerClassProperty innerClassProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<InnerClass> constructor = mock(Constructor.class);

        innerClassProperty = new InnerClassProperty(delegate, constructor);

        assertThat(innerClassProperty._delegate).isSameAs(delegate);
        assertThat(innerClassProperty._creator).isSameAs(constructor);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object value = new InnerClass();

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.nextTextValue()).thenReturn("InnerClassInstance");
        when(_creator.newInstance(bean)).thenReturn(value);
        when(valueDeserializer.deserialize(jsonParser, deserializationContext, value)).thenReturn(value);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(bean).set(eq(innerClassProperty), eq(value));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object value = new InnerClass();

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.nextTextValue()).thenReturn("InnerClassInstance");
        when(_creator.newInstance(bean)).thenReturn(value);
        when(valueDeserializer.deserialize(jsonParser, deserializationContext, value)).thenReturn(value);

        Object result = innerClassProperty.deserializeSetAndReturn(jsonParser, deserializationContext, bean);

        verify(bean).set(eq(innerClassProperty), eq(value));
        verify(innerClassProperty).setValue(eq(bean), eq(value));
        assertEquals(value, result);
    }

    @Test
    public void testSet() throws Exception {
        Object value = new InnerClass();

        innerClassProperty.setValue(bean, value);

        verify(bean).set(eq(innerClassProperty), eq(value));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object value = new InnerClass();

        Object result = innerClassProperty.setAndReturn(bean, value);

        verify(bean).set(eq(innerClassProperty), eq(value));
        assertEquals(value, result);
    }

    @Test
    public void testReadResolve() throws Exception {
        AnnotatedConstructor<InnerClass> annotatedConstructor = mock(AnnotatedConstructor.class);
        Object result = innerClassProperty.readResolve();

        verify(innerClassProperty).getCreator();
        verify(innerClassProperty).getAnnotatedConstructor(eq(InnerClass.class));
        assertEquals(new InnerClassProperty(innerClassProperty, annotatedConstructor), result);
    }

    @Test
    public void testWriteReplace() throws Exception {
        Object result = innerClassProperty.writeReplace();

        verify(innerClassProperty).getCreator();
        verify(innerClassProperty).getAnnotatedConstructor(eq(InnerClass.class));
        assertEquals(new InnerClassProperty(innerClassProperty, new AnnotatedConstructor(null, innerClassProperty._creator, null, null)), result);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws Exception {
        JsonDeserializer<InnerClass> innerClassDeserializer = mock(JsonDeserializer.class);
        Object value = new InnerClass();

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.nextTextValue()).thenReturn("InnerClassInstance");
        when(_creator.newInstance(bean)).thenReturn(value);
        when(innerClassDeserializer.deserialize(jsonParser, deserializationContext, value)).thenReturn(value);
        when(_valueTypeDeserializer).thenReturn(innerClassDeserializer);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(bean).set(eq(innerClassProperty), eq(value));
    }

    @Test
    public void testDeserializeWithNullValueDeserializer() throws Exception {
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        when(_valueDeserializer).thenReturn(null);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(bean).set(eq(innerClassProperty), eq(null));
    }

    @Test
    public void testSetWithNullValue() throws Exception {
        innerClassProperty.setValue(bean, null);

        verify(bean).set(eq(innerClassProperty), eq(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingConstructor() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<InnerClass> innerClassDeserializer = mock(JsonDeserializer.class);

        InnerClassProperty innerClassPropertyWithDeserializer = new InnerClassProperty(innerClassProperty, innerClassDeserializer);

        assertEquals(innerClassPropertyWithDeserializer._delegate, innerClassProperty._delegate);
        assertEquals(innerClassPropertyWithDeserializer._creator, innerClassProperty._creator);
        assertEquals(innerClassDeserializer, innerClassPropertyWithDeserializer._valueDeserializer);
    }

    @Test
    public void testWithName() {
        PropertyName newName = PropertyName.construct("newName");

        InnerClassProperty innerClassPropertyWithNewName = new InnerClassProperty(innerClassProperty, newName);

        assertEquals(innerClassPropertyWithNewName._delegate, innerClassProperty._delegate);
        assertEquals(innerClassPropertyWithNewName._creator, innerClassProperty._creator);
        assertEquals(innerClassPropertyWithNewName._name, newName);
    }

    @Test
    public void testWithNameAndValueDeserializer() {
        PropertyName newName = PropertyName.construct("newName");
        JsonDeserializer<InnerClass> innerClassDeserializer = mock(JsonDeserializer.class);

        InnerClassProperty innerClassPropertyWithNameAndDeserializer = new InnerClassProperty(innerClassProperty, newName, innerClassDeserializer);

        assertEquals(innerClassPropertyWithNameAndDeserializer._delegate, innerClassProperty._delegate);
        assertEquals(innerClassPropertyWithNameAndDeserializer._creator, innerClassProperty._creator);
        assertEquals(innerClassPropertyWithNameAndDeserializer._name, newName);
        assertEquals(innerClassDeserializer, innerClassPropertyWithNameAndDeserializer._valueDeserializer);
    }

    @Test
    public void testWithCreator() {
        Constructor<InnerClass> newCreator = mock(Constructor.class);

        InnerClassProperty innerClassPropertyWithNewCreator = new InnerClassProperty(innerClassProperty, newCreator);

        assertEquals(innerClassPropertyWithNewCreator._delegate, innerClassProperty._delegate);
        assertEquals(newCreator, innerClassPropertyWithNewCreator._creator);
        assertEquals(innerClassProperty._creator, innerClassPropertyWithNewCreator._creator.getAnnotated());
    }

    @Test
    public void testInnerClassWithNoArgumentConstructor() throws Exception {
        Object value = new InnerClassNoArg();

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.nextTextValue()).thenReturn("InnerClassNoArgInstance");
        when(_creator.newInstance(bean)).thenReturn(value);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(bean).set(eq(innerClassProperty), eq(value));
    }

    @Test
    public void testInnerClassWithSingleArgumentConstructor() throws Exception {
        Class<?> argumentType = String.class;
        InnerClassSingleArg innerClass = new InnerClassSingleArg("StringArgument");
        Object value = innerClass;

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.nextTextValue()).thenReturn("StringArgumentInstance");
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.nextTextValue()).thenReturn("StringArgumentValue");
        when(_creator.newInstance(bean, "StringArgumentValue"))
            .thenReturn(value);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(bean).set(eq(innerClassProperty), eq(value));
    }

    @Test
    public void testDeserializeWithNullAnnotatedConstructor() throws Exception {
        JsonDeserializer<InnerClass> innerClassDeserializer = mock(JsonDeserializer.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(null, innerClassDeserializer);

        try {
            innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static class InnerClass {
        @JsonDeserialize
        public static class InnerClassNoArg {
            // empty inner class without arguments
        }

        public static class InnerClassSingleArg {
            @JsonProperty("stringArgument")
            private final String stringArgument;

            public InnerClassSingleArg(String stringArgument) {
                this.stringArgument = stringArgument;
            }
        }
    }

    static class StdSerializerForInnerClassSingleArg
            extends StdSerializer<InnerClass.InnerClassSingleArg> {
        public StdSerializerForInnerClassSingleArg() {
            this(null);
        }

        public StdSerializerForInnerClassSingleArg(Class<InnerClass.InnerClassSingleArg> t) {
            this(null);
        }

        public StdSerializerForInnerClassSingleArg(Class<InnerClass.InnerClassSingleArg> t, boolean unquoted) {
            this();
        }

        public StdSerializerForInnerClassSingleArg(Class<InnerClass.InnerClassSingleArg> t, boolean unquoted, boolean canEmpty) {
            // empty constructor
        }

        @Override
        public void serializeWithEmpty(SerializerProvider p, ObjectGraphNode ng, SerializerSerializerBase ss)
                throws IOException, JsonProcessingException {
            // empty implementation
        }

        @Override
        public void serialize(InnerClass.InnerClassSingleArg innerClassSingleArg, JsonGenerator jg)
                throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeName("stringArgument");
            jg.writeValue(innerClassSingleArg.stringArgument);
            jg.writeEndObject();
        }
    }
}