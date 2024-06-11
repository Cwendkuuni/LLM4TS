package com.fasterxml.jackson.databind.deser;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;
    private DeserializationConfig config;

    @Before
    public void setUp() {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeser = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        
        settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);

        config = Mockito.mock(DeserializationConfig.class);
        Mockito.when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
    }

    @Test
    public void testWithNullValueDeserializer() {
        settableAnyProperty.withValueDeserializer(null);

        //TODO: Perform assertion here
    }

    @Test
    public void testFixAccess() {
        settableAnyProperty.fixAccess(config);

        //TODO: Perform assertion here
    }

    @Test
    public void testGetProperty() {
        BeanProperty property = settableAnyProperty.getProperty();

        //TODO: Perform assertion here
    }

    @Test
    public void testHasValueDeserializer() {
        boolean hasValueDeserializer = settableAnyProperty.hasValueDeserializer();

        //TODO: Perform assertion here
    }

    @Test
    public void testGetType() {
        JavaType type = settableAnyProperty.getType();

        //TODO: Perform assertion here
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        String propName = "test";

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        //TODO: Perform assertion here
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Object value = settableAnyProperty.deserialize(p, ctxt);

        //TODO: Perform assertion here
    }

    @Test
    public void testSet() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object propName = "test";
        Object value = Mockito.mock(Object.class);

        settableAnyProperty.set(instance, propName, value);

        //TODO: Perform assertion here
    }

    @Test
    public void testThrowAsIOE() throws IOException {
        Exception e = Mockito.mock(Exception.class);
        Object propName = "test";
        Object value = Mockito.mock(Object.class);

        settableAnyProperty._throwAsIOE(e, propName, value);

        //TODO: Perform assertion here
    }

    @Test
    public void testToString() {
        String string = settableAnyProperty.toString();

        //TODO: Perform assertion here
    }

    private static class AnySetterReferringTest extends Referring {

        public AnySetterReferringTest(UnresolvedForwardReference reference, Class<?> type) {
            super(reference, type);
        }

        @Override
        public void handleResolvedForwardReference(Object id, Object value)
                throws IOException {

        }
    }
}