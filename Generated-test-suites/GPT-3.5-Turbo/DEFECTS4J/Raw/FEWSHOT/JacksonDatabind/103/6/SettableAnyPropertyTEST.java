package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class SettableAnyPropertyTest {
    
    @Test
    public void testSettableAnyProperty_constructor_withValueDeserializer() {
        BeanProperty propertyMock = mock(BeanProperty.class);
        AnnotatedMember setterMock = mock(AnnotatedMember.class);
        JavaType typeMock = mock(JavaType.class);
        KeyDeserializer keyDeserializerMock = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializerMock = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializerMock = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(propertyMock, setterMock, typeMock, keyDeserializerMock, valueDeserializerMock, typeDeserializerMock);

        assertEquals(propertyMock, settableAnyProperty.getProperty());
        assertEquals(valueDeserializerMock, settableAnyProperty._valueDeserializer);
        assertEquals(typeMock, settableAnyProperty.getType());
        assertEquals(keyDeserializerMock, settableAnyProperty._keyDeserializer);
    }
    
    @Test
    public void testSettableAnyProperty_withValueDeserializer() {
        BeanProperty propertyMock = mock(BeanProperty.class);
        AnnotatedMember setterMock = mock(AnnotatedMember.class);
        JavaType typeMock = mock(JavaType.class);
        KeyDeserializer keyDeserializerMock = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializerMock = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializerMock = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(propertyMock, setterMock, typeMock, keyDeserializerMock, valueDeserializerMock, typeDeserializerMock);

        JsonDeserializer<Object> newDeserMock = mock(JsonDeserializer.class);
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(newDeserMock);

        assertEquals(propertyMock, newSettableAnyProperty.getProperty());
        assertEquals(newDeserMock, newSettableAnyProperty._valueDeserializer);
        assertEquals(typeMock, newSettableAnyProperty.getType());
        assertEquals(keyDeserializerMock, newSettableAnyProperty._keyDeserializer);
    }
  
    @Test
    public void testSettableAnyProperty_fixAccess() {
        DeserializationConfig configMock = mock(DeserializationConfig.class);
        BeanProperty propertyMock = mock(BeanProperty.class);
        AnnotatedMember setterMock = mock(AnnotatedMethod.class);
        JavaType typeMock = mock(JavaType.class);
        KeyDeserializer keyDeserializerMock = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializerMock = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializerMock = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyPropertyMock = mock(SettableAnyProperty.class);

        when(settableAnyPropertyMock._setter).thenReturn(setterMock);

        settableAnyPropertyMock.fixAccess(configMock);

        verify(setterMock, times(1)).fixAccess(configMock.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instanceMock = mock(Object.class);
        String propName = "";
        SettableAnyProperty settableAnyPropertyMock = mock(SettableAnyProperty.class);
        JsonParser pMock = mock(JsonParser.class);
        DeserializationContext ctxtMock = mock(DeserializationContext.class);

        doCallRealMethod().when(settableAnyPropertyMock).deserializeAndSet(any(), any(), any(), eq(propName));

        JsonDeserializer<Object> valueDeserializerMock = mock(JsonDeserializer.class);
        when(settableAnyPropertyMock.deserialize(any(), any())).thenReturn(valueDeserializerMock);

        settableAnyPropertyMock.deserializeAndSet(pMock, ctxtMock, instanceMock, propName);

        verify(settableAnyPropertyMock, times(1)).set(any(), any(), any());
    }
}