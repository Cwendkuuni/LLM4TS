package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdReferencePropertyTest {

    private SettableBeanProperty forward;
    private ObjectIdInfo objectIdInfo;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nullValueProvider;

    private ObjectIdReferenceProperty propertyUnderTest;

    @Before
    public void setUp() {
        forward = Mockito.mock(SettableBeanProperty.class);
        objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        deserializer = Mockito.mock(JsonDeserializer.class);
        nullValueProvider = Mockito.mock(NullValueProvider.class);

        propertyUnderTest = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testWithNullProvider_WithNullValueProvider_ShouldReturnNewObjectIdReferencePropertyWithNullProvider()
    {
        // Arrange
        ObjectIdReferenceProperty expectedProperty = new ObjectIdReferenceProperty(propertyUnderTest, deserializer, nullValueProvider);

        // Act
        SettableBeanProperty actualProperty = propertyUnderTest.withNullProvider(nullValueProvider);

        // Assert
        assertEquals(expectedProperty, actualProperty);
        assertEquals(nullValueProvider, actualProperty.getNullValueProvider());
    }

    @Test
    public void testWithNullProvider_WithSameNullValueProvider_ShouldReturnSameObjectIdReferenceProperty()
    {
        // Act
        SettableBeanProperty actualProperty = propertyUnderTest.withNullProvider(propertyUnderTest.getNullValueProvider());

        // Assert
        assertEquals(propertyUnderTest, actualProperty);
    }

    @Test
    public void testWithNullProvider_WithDifferentNullValueProvider_ShouldReturnNewObjectIdReferencePropertyWithDifferentNullProvider()
    {
        // Arrange
        NullValueProvider differentNullValueProvider = Mockito.mock(NullValueProvider.class);

        // Act
        SettableBeanProperty actualProperty = propertyUnderTest.withNullProvider(differentNullValueProvider);

        // Assert
        assertNotEquals(propertyUnderTest, actualProperty);
        assertEquals(differentNullValueProvider, actualProperty.getNullValueProvider());
    }

    @Test
    public void testWithNullProvider_WithNullNullValueProvider_ShouldReturnNewObjectIdReferencePropertyWithNullProvider()
    {
        // Arrang
        NullValueProvider nullNullValueProvider = null;
        ObjectIdReferenceProperty expectedProperty = new ObjectIdReferenceProperty(propertyUnderTest, deserializer, nullNullValueProvider);

        // Act
        SettableBeanProperty actualProperty = propertyUnderTest.withNullProvider(nullNullValueProvider);

        // Assert
        assertEquals(expectedProperty, actualProperty);
        assertEquals(nullNullValueProvider, actualProperty.getNullValueProvider());
    }

    @Test
    public void testWithValueDeserializer_WithValueDeserializerSameAsPropertyValueDeserializer_ShouldReturnSameObjectIdReferenceProperty()
    {
        // Act
        SettableBeanProperty actualProperty = propertyUnderTest.withValueDeserializer(propertyUnderTest.getValueDeserializer());

        // Assert
        assertEquals(propertyUnderTest, actualProperty);
    }

    @Test
    public void testWithValueDeserializer_WithValueDeserializerDifferentFromPropertyValueDeserializer_ShouldReturnNewObjectIdReferencePropertyWithValueDeserializer()
    {
        // Arrange
        ObjectIdReferenceProperty expectedProperty = new ObjectIdReferenceProperty(propertyUnderTest, deserializer, nullValueProvider);

        // Act
        SettableBeanProperty actualProperty = propertyUnderTest.withValueDeserializer(deserializer);

        // Assert
        assertEquals(expectedProperty, actualProperty);
        assertEquals(deserializer, actualProperty.getValueDeserializer());
    }

    @Test
    public void testWithCreatorIndex_ShouldReturnCreatorIndex()
    {
        // Arrange
        int creatorIndex = 1;

        // Act
        int actualCreatorIndex = propertyUnderTest.withCreatorIndex(creatorIndex).getCreatorIndex();

        // Assert
        assertEquals(creatorIndex, actualCreatorIndex);
    }

    @Test
    public void testGetAnnotation_ShouldReturnAnnotationFromForward()
    {
        // Arrange
        Class<Annotation> annotationClass = Annotation.class;
        Annotation expectedAnnotation = Mockito.mock(annotationClass);
        Mockito.when(forward.getAnnotation(annotationClass)).thenReturn(expectedAnnotation);

        // Act
        Annotation actualAnnotation = propertyUnderTest.getAnnotation(annotationClass);

        // Assert
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetMember_ShouldReturnMemberFromForward()
    {
        // Arrange
        AnnotatedMember expectedMember = Mockito.mock(AnnotatedMember.class);
        Mockito.when(forward.getMember()).thenReturn(expectedMember);

        // Act
        AnnotatedMember actualMember = propertyUnderTest.getMember();

        // Assert
        assertEquals(expectedMember, actualMember);
    }

    @Test
    public void testFixAccess_WithForwardNotNull_ShouldFixForwardAccess()
    {
        // Arrange
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);

        // Act
        propertyUnderTest.fixAccess(config);

        // Assert
        Mockito.verify(forward).fixAccess(config);
    }

    @Test
    public void testFixAccess_WithForwardNull_ShouldNotFixForwardAccess()
    {
        // Arrange
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);

        // Act
        propertyUnderTest = new ObjectIdReferenceProperty(null, objectIdInfo);
        propertyUnderTest.fixAccess(config);

        // Assert
        Mockito.verifyZeroInteractions(config);
    }

    @Test
    public void testDeserializeAndSet_ShouldDeserializeAndSetValues() throws IOException
    {
        // Arrange
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        Object expectedValue = new Object();
        Mockito.when(forward.deserializeSetAndReturn(parser, context, instance)).thenReturn(expectedValue);

        // Act
        propertyUnderTest.deserializeAndSet(parser, context, instance);

        // Assert
        Mockito.verify(forward).deserializeSetAndReturn(parser, context, instance);
        Mockito.verify(propertyUnderTest).setAndReturn(instance, expectedValue);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testDeserializeSetAndReturn_WithUnresolvedForwardReferenceAndNoIdentityInfo_ShouldThrowJsonMappingException() throws IOException
    {
        // Arrange
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        Mockito.when(forward.deserializeSetAndReturn(parser, context, propertyUnderTest)).thenThrow(UnresolvedForwardReference.class);
        Mockito.when(reference.getRoid()).thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(propertyUnderTest._valueDeserializer.getObjectIdReader()).thenReturn(null);

        try {
            // Act
            propertyUnderTest.deserializeSetAndReturn(parser, context, propertyUnderTest);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Assert
            assertEquals("Unresolved forward reference but no identity info", e.getMessage());
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testDeserializeSetAndReturn_WithUnresolvedForwardReferenceAndWithIdentityInfo_ShouldInvokeAppendReferring() throws IOException
    {
        // Arrange
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);
        Mockito.when(forward.deserializeSetAndReturn(parser, context, propertyUnderTest)).thenThrow(UnresolvedForwardReference.class);
        Mockito.when(reference.getRoid()).thenReturn(roid);
        Mockito.when(propertyUnderTest._valueDeserializer.getObjectIdReader()).thenReturn(Mockito.mock(ObjectIdReader.class));

        // Act
        propertyUnderTest.deserializeSetAndReturn(parser, context, propertyUnderTest);

        // Assert
        Mockito.verify(roid).appendReferring(Mockito.any(ObjectIdReferenceProperty.PropertyReferring.class));
    }

    @Test
    public void testDeserializeSetAndReturn_WithResolvedForwardReference_ShouldSetPropertyValue() throws IOException
    {
        // Arrange
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        Object expectedValue = new Object();
        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        Mockito.when(forward.deserializeSetAndReturn(parser, context, instance)).thenThrow(UnresolvedForwardReference.class);
        Mockito.when(reference.getRoid()).thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(propertyUnderTest._valueDeserializer.getObjectIdReader()).thenReturn(Mockito.mock(ObjectIdReader.class));
        Mockito.when(reference.getRoid().resolve()).thenReturn(expectedValue);

        // Act
        propertyUnderTest.deserializeSetAndReturn(parser, context, instance);

        // Assert
        assertEquals(expectedValue, propertyUnderTest.get(instance));
    }

    @Test
    public void testSet_ShouldSetValueUsingForwardProperty() throws IOException
    {
        // Arrange
        Object instance = new Object();
        Object value = new Object();

        // Act
        propertyUnderTest.set(instance, value);

        // Assert
        Mockito.verify(forward).set(instance, value);
    }

    @Test
    public void testSetAndReturn_ShouldSetValueAndReturnUsingForwardProperty() throws IOException
    {
        // Arrange
        Object instance = new Object();
        Object value = new Object();
        Object expectedValue = new Object();
        Mockito.when(forward.setAndReturn(instance, value)).thenReturn(expectedValue);

        // Act
        Object actualValue = propertyUnderTest.setAndReturn(instance, value);

        // Assert
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testPropertyReferring_HandleResolvedForwardReference_WithMatchingId_ShouldSetValue() throws IOException
    {
        // Arrange
        UnresolvedForwardReference ref = Mockito.mock(UnresolvedForwardReference.class);
        Class<?> type = ClassUtil.class;
        Object ob = new Object();
        PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(propertyUnderTest, ref, type, ob);
        Object value = new Object();
        Object id = new Object();
        Mockito.when(ref.hasId(id)).thenReturn(true);
        Mockito.when(propertyUnderTest.hasId(id)).thenReturn(true);

        // Act
        referring.handleResolvedForwardReference(id, value);

        // Assert
        Mockito.verify(propertyUnderTest).set(ob, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyReferring_HandleResolvedForwardReference_WithNonMatchingId_ShouldThrowIllegalArgumentException() throws IOException
    {
        // Arrange
        UnresolvedForwardReference ref = Mockito.mock(UnresolvedForwardReference.class);
        Class<?> type = ClassUtil.class;
        Object ob = new Object();
        PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(propertyUnderTest, ref, type, ob);
        Object value = new Object();
        Object id = new Object();
        Mockito.when(ref.hasId(id)).thenReturn(false);

        // Act
        referring.handleResolvedForwardReference(id, value);
    }
}