package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor1() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);

        assertEquals(delegate, property._delegate);
        assertEquals(ctor, property._creator);
    }

    @Test
    public void testConstructor2() {
        InnerClassProperty src = new InnerClassProperty();
        AnnotatedConstructor ann = new AnnotatedConstructor(null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(src, ann);

        assertEquals(src._delegate, property._delegate);
        assertEquals(ann, property._annotated);
        assertEquals(ann.getAnnotated(), property._creator);
    }

    @Test
    public void testConstructor3() {
        InnerClassProperty src = new InnerClassProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty property = new InnerClassProperty(src, deser);

        assertEquals(src._delegate.withValueDeserializer(deser), property._delegate);
        assertEquals(src._creator, property._creator);
    }

    @Test
    public void testConstructor4() {
        InnerClassProperty src = new InnerClassProperty();
        PropertyName newName = new PropertyName();
        InnerClassProperty property = new InnerClassProperty(src, newName);

        assertEquals(src._delegate.withName(newName), property._delegate);
        assertEquals(src._creator, property._creator);
    }

    @Test
    public void testWithName() {
        InnerClassProperty src = new InnerClassProperty();
        PropertyName newName = new PropertyName();
        InnerClassProperty property = src.withName(newName);

        assertEquals(src._delegate.withName(newName), property._delegate);
        assertEquals(src._creator, property._creator);
    }

    @Test
    public void testWithValueDeserializer() {
        InnerClassProperty src = new InnerClassProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty property = src.withValueDeserializer(deser);

        assertEquals(src._delegate.withValueDeserializer(deser), property._delegate);
        assertEquals(src._creator, property._creator);
    }

    @Test
    public void testAssignIndex() {
        InnerClassProperty property = new InnerClassProperty();
        int index = 1;

        property.assignIndex(index);
        assertEquals(index, property._delegate.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        InnerClassProperty property = new InnerClassProperty();
        int index = 1;

        property._delegate.assignIndex(index);
        assertEquals(index, property.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        InnerClassProperty property = new InnerClassProperty();
        Class<A> acls = A.class;
        A annotation = new A();

        property._delegate.setAnnotation(acls, annotation);
        assertEquals(annotation, property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        InnerClassProperty property = new InnerClassProperty();
        AnnotatedMember member = new AnnotatedMember();

        property._delegate.setMember(member);
        assertEquals(member, property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        InnerClassProperty property = new InnerClassProperty();

        // TODO: add test code
    }

    @Test
    public void testDeserializeSetAndReturn() {
        InnerClassProperty property = new InnerClassProperty();

        // TODO: add test code
    }

    @Test
    public void testSet() {
        InnerClassProperty property = new InnerClassProperty();

        // TODO: add test code
    }

    @Test
    public void testSetAndReturn() {
        InnerClassProperty property = new InnerClassProperty();

        // TODO: add test code
    }

    @Test
    public void testReadResolve() {
        InnerClassProperty property = new InnerClassProperty();

        // TODO: add test code
    }

    @Test
    public void testWriteReplace() {
        InnerClassProperty property = new InnerClassProperty();

        // TODO: add test code
    }
}