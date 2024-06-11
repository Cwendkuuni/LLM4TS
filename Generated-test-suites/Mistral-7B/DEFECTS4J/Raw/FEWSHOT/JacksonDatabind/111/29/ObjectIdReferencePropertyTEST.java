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

    private static final ObjectNode EMPTY_NODE = JsonNodeFactory.instance.objectNode();

    @Test
    public void testObjectIdReferencePropertyConstruction() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo);
        assertNull(property.getForward());
        assertEquals(ObjectIdReferenceProperty.class, property.getClass());
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyCopy() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty copied = new ObjectIdReferenceProperty(property, null);
        assertEquals(property.getForward(), copied.getForward());
        assertEquals(property.getClass(), copied.getClass());
        assertEquals(property._objectIdInfo, copied._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyWithName() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty propertyWithName = property.withName(new PropertyName("newName"));
        assertEquals(propertyWithName.getName(), "newName");
        assertEquals(property, propertyWithName.getBackReference());
        assertEquals(property.getClass(), propertyWithName.getClass());
        assertEquals(property._objectIdInfo, propertyWithName._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyWithValueDeserializer() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonDeserializer<String> deserializer = new StringDeserializer();
        ObjectIdReferenceProperty propertyWithValueDeserializer = new ObjectIdReferenceProperty(property, deserializer, null);
        assertEquals(property, propertyWithValueDeserializer.getBackReference());
        assertEquals(property.getClass(), propertyWithValueDeserializer.getClass());
        assertEquals(property._objectIdInfo, propertyWithValueDeserializer._objectIdInfo);
        assertEquals(deserializer, propertyWithValueDeserializer._valueDeserializer);
        assertNull(propertyWithValueDeserializer._nullProvider);
    }

    @Test
    public void testObjectIdReferencePropertyWithNullProvider() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        NullValueProvider nullValueProvider = new NullValueProvider();
        ObjectIdReferenceProperty propertyWithNullProvider = new ObjectIdReferenceProperty(property, null, nullValueProvider);
        assertEquals(property, propertyWithNullProvider.getBackReference());
        assertEquals(property.getClass(), propertyWithNullProvider.getClass());
        assertEquals(property._objectIdInfo, propertyWithNullProvider._objectIdInfo);
        assertNull(propertyWithNullProvider._valueDeserializer);
        assertEquals(nullValueProvider, propertyWithNullProvider._nullProvider);
    }

    @Test
    public void testFixAccess() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);
        assertNotNull(property.getBackReference());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Annotation annotation = Mockito.mock(Annotation.class);
        assertEquals(annotation, property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        assertEquals(member, property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(-1, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        MyClass instance = new MyClass();
        String value = "test";

        property.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
        assertEquals(value, instance.getName());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        MyClass instance = new MyClass();
        String value = "test";

        Object result = property.deserializeSetAndReturn(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
        assertEquals(value, result);
        assertEquals(value, instance.getName());
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        MyClass instance = new MyClass();
        String value = "test";

        property.set(instance, value);
        assertEquals(value, instance.getName());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        MyClass instance = new MyClass();
        String value = "test";

        Object result = property.setAndReturn(instance, value);
        assertEquals(value, result);
        assertEquals(value, instance.getName());
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator(true).generateIdInfo(MyClass.class, null);
        SettableBeanProperty forward = new SimpleBeanProperty("name", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        PropertyReferring referring = new PropertyReferring(property, reference, MyClass.class, new MyClass());
        property.handleResolvedForwardReference(1, EMPTY_NODE);
        assertEquals(1, referring.getId());
    }
}

class MyClass {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class SimpleBeanProperty extends SettableBeanProperty {
    public SimpleBeanProperty(String name, Class<?> propertyType) {
        super(name, propertyType);
    }
}

class StringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}

class PropertyReferring extends com.fasterxml.jackson.databind.deser.impl.Referring {
    private final ObjectIdReferenceProperty _parent;

    public PropertyReferring(ObjectIdReferenceProperty parent, UnresolvedForwardReference ref, Class<?> type, Object ob) {
        super(ref, type);
        _parent = parent;
    }

    @Override
    public void handleResolvedForwardReference(Object id, Object value) throws IOException {
        _parent.set(_pojo, value);
    }
}