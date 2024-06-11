package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SettableAnyPropertyTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private SettableAnyProperty settableAnyProperty;
    private DeserializationContext context;
    private JsonParser parser;
    private DefaultSerializerProvider serializerProvider;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        context = mock(DeserializationContext.class);
        parser = mock(JsonParser.class);
        serializerProvider = mock(DefaultSerializerProvider.class);

        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotationMembers()).thenReturn(new AnnotatedMember[0]);
        BeanPropertyDefinition bpd = mock(BeanPropertyDefinition.class);
        when(bpd.getBeanProperty()).thenReturn(new BeanProperty(null, ac));
        AnnotatedMethod am = mock(AnnotatedMethod.class);
        when(am.getDeclaringClass()).thenReturn(Object.class);
        when(am.getName()).thenReturn("setProperty");
        when(am.getParameterType(0)).thenReturn(Map.class);
        when(am.getParameterTypes()).thenReturn(new Class[0]);
        AnnotatedField af = mock(AnnotatedField.class);
        when(af.getValue(any(Object.class))).thenReturn(new HashMap<>());
        when(af.getName()).thenReturn("testField");
        when(af.getDeclaringClass()).thenReturn(TestClass.class);

        Class<SettableAnyProperty> settableAnyPropertyClass = SettableAnyProperty.class;
        when(context.findBeanProperty(eq(settableAnyPropertyClass), anyString())).thenReturn(new BeanProperty(bpd, am));
        when(context.findAnnotatedMembers(any(Class.class), anyString(), anyBoolean())).thenReturn(new AnnotatedMember[0]);
        when(context.findDeserializerForProperty(any(BeanProperty.class))).thenReturn(null);
        when(context.handleUnknownProperty(any(Object.class), anyString())).thenReturn(null);
        when(context.handleUnknownProperty(any(Object.class), anyString(), any(Class.class))).thenReturn(null);
        when(context.findBeanProperty(any(Class.class), anyString())).thenReturn(null);

        settableAnyProperty = new SettableAnyProperty(new BeanProperty(bpd, am), am, new TypeReference<>() {}, null, null, null, null);
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws Exception {
        // Test case for deserializeAndSet() method with null value
        Object instance = new Object();
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        settableAnyProperty.deserializeAndSet(parser, context, instance, "testProperty");

        // No exception should be thrown
        verifyNoMoreInteractions(context);

        assertNull(instance);
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializer() throws Exception {
        // Test case for deserializeAndSet() method with value deserializer
        Object instance = new Object();
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.readValueAsTree()).thenReturn(JsonNode.instance("value", "testValue"));

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(parser, context)).thenReturn("testDeserializedValue");
        settableAnyProperty = settableAnyProperty.withValueDeserializer(valueDeserializer);

        settableAnyProperty.deserializeAndSet(parser, context, instance, "testProperty");

        assertEquals("testDeserializedValue", instance);
        verify(instance).getClass();
        verify(parser).nextToken();
        verify(parser).nextToken();
        verifyZeroInteractions(context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSet_UnresolvedForwardReference() throws Exception {
        // Test case for deserializeAndSet() method with unresolved forward reference
        Object instance = new Object();
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        ReadableObjectId roid = mock(ReadableObjectId.class);
        when(context.findDeserializerForProperty(any(BeanProperty.class))).thenReturn(new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new UnresolvedForwardReference(roid);
            }
        });

        settableAnyProperty.deserializeAndSet(parser, context, instance, "testProperty");

        // JsonMappingException should be thrown
        verify(context).handleUnresolvedForwardReference(any(Object.class), any(Object.class), any(Class.class), anyInt());
    }

    @Test
    public void testSet() throws Exception {
        // Test case for set() method
        Object instance = new Object();
        Object testPropName = "testProperty";
        Object testValue = "testValue";

        settableAnyProperty.set(instance, testPropName, testValue);

        Map testMap = (Map) instance;
        assertEquals(testValue, testMap.get(testPropName));
    }

    @Test
    public void testSet_SetterIsField() throws Exception {
        // Test case for set() method when setter is a field
        Object instance = new TestClass();
        Object testPropName = "testProperty";
        Object testValue = "testValue";

        settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new BeanPropertyDefinition(null, "testProperty", new AnnotatedField(TestClass.class, "testField")), null),
                new AnnotatedField(TestClass.class, "testField"), new TypeReference<>() {}, null, null, null, null);

        settableAnyProperty.set(instance, testPropName, testValue);

        Map testMap = (Map) instance;
        assertEquals(testValue, testMap.get(testPropName));
    }

    private static class TestClass {
        private Map<String, Object> testField;

        public Map<String, Object> getTestField() {
            return testField;
        }
    }
}