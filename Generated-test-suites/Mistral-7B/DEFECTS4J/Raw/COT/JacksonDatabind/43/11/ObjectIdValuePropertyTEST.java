package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty.
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.class, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(property);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.class, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = ObjectIdNode.ID_OBJECT;
        Object target = new Object();

        JsonParser p = new Mapper().parser();
        p.nextValue(); // consume ObjectIdNode
        property.deserializeAndSet(p, new TestContext(), target);

        ObjectIdNode expectedId = ObjectIdNode.createObjectId(1L);
        assertEquals(expectedId, p.readValueAs(ObjectIdNode.class));
        assertEquals(expectedId.idParts()[0], target.getClass().getName());
        assertEquals(expectedId.getId(), target.hashCode());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.class, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = ObjectIdNode.ID_OBJECT;
        Object target = new Object();

        JsonParser p = new Mapper().parser();
        p.nextValue(); // consume ObjectIdNode
        Object result = property.deserializeSetAndReturn(p, new TestContext(), target);

        ObjectIdNode expectedId = ObjectIdNode.createObjectId(1L);
        assertEquals(expectedId, p.readValueAs(ObjectIdNode.class));
        assertEquals(expectedId.idParts()[0], target.getClass().getName());
        assertEquals(expectedId.getId(), target.hashCode());
        assertEquals(target, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.class, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object target = new Object();
        property.set(target, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.class, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object target = new Object();
        Object result = property.setAndReturn(target, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithName() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.class, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        PropertyName newName = PropertyName.construct("newPropertyName");
        property.withName(newName);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithValueDeserializer() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.class, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<?> deser = new StandardDeserializer<>();
        property.withValueDeserializer(deser);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetNullId() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.class, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object target = new Object();

        JsonParser p = new Mapper().parser();
        p.nextValue(); // consume null
        property.deserializeAndSet(p, new TestContext(), target);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnNullId() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.class, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object target = new Object();

        JsonParser p = new Mapper().parser();
        p.nextValue(); // consume null
        property.deserializeSetAndReturn(p, new TestContext(), target);
    }

}