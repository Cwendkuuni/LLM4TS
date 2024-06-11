
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;
    private BeanPropertyDefinition propDef;
    private SimpleType type;
    private TypeDeserializer typeDeser;
    private Method method;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private Object instance;

    @Before
    public void setup() throws NoSuchMethodException, SecurityException {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(SimpleType.class);
        typeDeser = mock(TypeDeserializer.class);
        method = SetterlessProperty.class.getDeclaredMethod("withName", String.class);
        objectMapper = mock(ObjectMapper.class);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        instance = new Object();

        AnnotatedMethod annotated = mock(AnnotatedMethod.class);
        when(annotated.getAnnotated()).thenReturn(method);

        setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, objectMapper);
        setterlessProperty._annotated = annotated;
    }

    @Test
    public void testWithName() {
        SetterlessProperty newProperty = setterlessProperty.withName("newName");
        assertEquals("newName", newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        SetterlessProperty newProperty = setterlessProperty.withValueDeserializer(deser);
        assertEquals(deser, newProperty._valueDeserializer);
        assertEquals(setterlessProperty._nullProvider, newProperty._nullProvider);

        SetterlessProperty anotherNewProp = setterlessProperty.withNullProvider(nva);
        assertEquals(setterlessProperty._nullProvider, anotherNewProp._nullProvider);
    }

    @Test
    public void testFixAccess() throws IOException {
        DeserializationConfig config = mock(DeserializationConfig.class);
        setterlessProperty.fixAccess(config);

        verify(setterlessProperty._annotated).fixAccess(anyBoolean());
        verify(config).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
    }

    @Test
    public void testGetAnnotation() {
        Class<?> acls = mock(Class.class);
        assertNull(setterlessProperty.getAnnotation(acls));
        verify(setterlessProperty._annotated).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        assertEquals(setterlessProperty._annotated, setterlessProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object emptyInstance = new Object();
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, emptyInstance);

        verify(jsonParser).getCurrentToken();
        verify(jsonParser).skipChildren();
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object emptyInstance = new Object();
        Object instanceReturned = setterlessProperty.deserializeSetAndReturn(jsonParser, deserializationContext, emptyInstance);

        assertEquals(emptyInstance, instanceReturned);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(instance, new Object());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instanceReturned = setterlessProperty.setAndReturn(instance, new Object());

        assertEquals(instance, instanceReturned);
    }
}
