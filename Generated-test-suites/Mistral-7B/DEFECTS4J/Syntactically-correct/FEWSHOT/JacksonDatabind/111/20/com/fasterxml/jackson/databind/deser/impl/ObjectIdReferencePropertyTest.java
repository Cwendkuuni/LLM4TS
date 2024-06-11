package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferenceProperty() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testWithName() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty newProperty = property.withName(new PropertyName("newName"));

        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertNotSame(property, newProperty);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonDeserializer<Object> deserializer = new SimpleObjectDeserializer(_type);
        ObjectIdReferenceProperty newProperty = property.withValueDeserializer(deserializer);

        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertSame(deserializer, newProperty._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        NullValueProvider nva = new StandardNullValueProvider();
        ObjectIdReferenceProperty newProperty = property.withNullProvider(nva);

        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertSame(nva, newProperty._nullProvider);
    }

    @Test
    public void testFixAccess() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);

        assertNotNull(property._forward);
        assertNotNull(property._objectIdInfo);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Annotation annotation = Mockito.mock(Annotation.class);
        assertEquals(annotation, property.getAnnotation(Annotation.class));

        Mockito.verify(annotation).getClass();
    }

    @Test
    public void testGetMember() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        assertEquals(member, property.getMember());

        Mockito.verify(member).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Object deserializedValue = new Object();
        ObjectName value = new ObjectName("value");

        when(jsonParser.getValueAsToken()).thenReturn(context.readValueAs(ObjectName.class));
        doReturn(deserializedValue).when(property).deserialize(jsonParser, context);

        property.deserializeAndSet(jsonParser, context, testInstance);

        assertEquals(deserializedValue, testInstance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Object deserializedValue = new Object();
        ObjectName value = new ObjectName("value");

        when(jsonParser.getValueAsToken()).thenReturn(context.readValueAs(ObjectName.class));
        doReturn(deserializedValue).when(property).deserialize(jsonParser, context);

        Object result = property.deserializeSetAndReturn(jsonParser, context, testInstance);

        assertNull(result);
        assertEquals(deserializedValue, testInstance);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();
        Object deserializedValue = new Object();

        property.set(testInstance, deserializedValue);

        assertEquals(deserializedValue, testInstance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();
        Object deserializedValue = new Object();

        Object result = property.setAndReturn(testInstance, deserializedValue);

        assertNull(result);
        assertEquals(deserializedValue, testInstance);
    }

    @Test
    public void testPropertyReferring() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectId(_type -> true, _ -> null);
        SettableBeanProperty forward = new SimpleValueProperty.Std(_type, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        ObjectName referencedValue = new ObjectName("referencedValue");
        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        Object pojo = new Object();

        when(jsonParser.getValueAsToken()).thenReturn(context.readValueAs(ObjectName.class));
        doReturn(referencedValue).when(reference).getValue();

        PropertyReferring referring = property.deserializeSetAndReturn(jsonParser, context, pojo)
                .getReferring();

        assertEquals(property, referring._parent);
        assertEquals(pojo, referring._pojo);
        assertEquals(property._type.getRawClass(), referring.getType().getRawClass());
    }
}