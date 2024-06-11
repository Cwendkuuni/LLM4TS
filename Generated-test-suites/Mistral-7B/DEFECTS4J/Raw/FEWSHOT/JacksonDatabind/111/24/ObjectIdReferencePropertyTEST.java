package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdReferencePropertyTest {
    private static class TestBean {
        private ObjectIdReferenceProperty idRef;

        public ObjectIdReferenceProperty getIdRef() {
            return idRef;
        }

        public void setIdRef(ObjectIdReferenceProperty idRef) {
            this.idRef = idRef;
        }
    }

    @Test
    public void testConstructorWithForwardAndObjectIdInfo() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, "id", "idField", null, false, null);
        SettableBeanProperty forward = new StdSerializer<String>(String.class).withoutProperties();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testConstructorWithSameProperties() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, "id", "idField", null, false, null);
        SettableBeanProperty forward = new StdSerializer<String>(String.class).withoutProperties();
        ObjectIdReferenceProperty property1 = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty property2 = new ObjectIdReferenceProperty(property1, null, null, null);

        assertEquals(property1, property2);
    }

    @Test
    public void testConstructorWithNewName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, "id", "idField", null, false, null);
        SettableBeanProperty forward = new StdSerializer<String>(String.class).withoutProperties();
        PropertyName newName = PropertyName.construct("newName");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, newName, objectIdInfo);

        assertEquals(forward, property._forward);
        assertEquals(newName, property.getName());
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testSettableBeanPropertyMethods() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, "id", "idField", null, false, null);
        SettableBeanProperty forward = new StdSerializer<String>(String.class).withoutProperties();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object id = new Object();
        Object value = new Object();

        // test set and deserializeSetAndReturn methods
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TestBean bean = new TestBean();
        property.deserializeAndSet(p, ctxt, bean);
        property.set(bean, value);
        assertEquals(value, bean.getIdRef().get(bean));

        // test setAndReturn method
        Object returnedValue = property.setAndReturn(bean, value);
        assertEquals(value, returnedValue);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, "id", "idField", null, false, null);
        SettableBeanProperty forward = new StdSerializer<String>(String.class).withoutProperties();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object id = new Object();
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, reference, Object.class, new TestBean());

        property.handleResolvedForwardReference(id, new Object());
        assertEquals(new Object(), property.getIdRef().get(new TestBean()));
    }
}