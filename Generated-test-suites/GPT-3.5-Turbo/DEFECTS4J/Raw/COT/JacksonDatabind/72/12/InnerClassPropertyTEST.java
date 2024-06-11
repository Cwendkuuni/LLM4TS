
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);

        assertEquals(delegate, property._delegate);
        assertEquals(ctor, property._creator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAlternativeConstructorWithoutAnnotatedConstructor() {
        InnerClassProperty src = new InnerClassProperty();
        AnnotatedConstructor ann = null;
        InnerClassProperty property = new InnerClassProperty(src, ann);
    }

    @Test
    public void testAlternativeConstructorWithAnnotatedConstructor() {
        InnerClassProperty src = new InnerClassProperty();
        AnnotatedConstructor ann = new AnnotatedConstructor();
        InnerClassProperty property = new InnerClassProperty(src, ann);

        assertEquals(src, property._delegate);
        assertEquals(ann.getAnnotated(), property._creator);
    }

    @Test
    public void testConstructorWithDeserializer() {
        InnerClassProperty src = new InnerClassProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty property = new InnerClassProperty(src, deser);

        assertEquals(src._delegate.withValueDeserializer(deser), property._delegate);
        assertEquals(src._creator, property._creator);
    }

    @Test
    public void testConstructorWithPropertyName() {
        InnerClassProperty src = new InnerClassProperty();
        PropertyName newName = new PropertyName("name");
        InnerClassProperty property = new InnerClassProperty(src, newName);

        assertEquals(src._delegate.withName(newName), property._delegate);
        assertEquals(src._creator, property._creator);
    }

    @Test
    public void testAssignIndex() {
        InnerClassProperty property = new InnerClassProperty();
        property.assignIndex(0);
        assertEquals(0, property._delegate.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        InnerClassProperty property = new InnerClassProperty();
        assertEquals(property._delegate.getPropertyIndex(), property.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        InnerClassProperty property = new InnerClassProperty();
        Class<?> acls = Class.class;
        assertEquals(property._delegate.getAnnotation(acls), property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        InnerClassProperty property = new InnerClassProperty();
        assertEquals(property._delegate.getMember(), property.getMember());
    }

    @Test
    public void testDeserializeAndSetWithJsonValueNull() throws IOException {
        InnerClassProperty property = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();

        jp.setCurrentToken(JsonToken.VALUE_NULL);

        property.deserializeAndSet(jp, ctxt, bean);

        assertNull(property.get(bean));
    }

    @Test
    public void testDeserializeAndSetWithValueTypeDeserializer() throws IOException {
        InnerClassProperty property = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();

        JsonDeserializer<?> valueTypeDeserializer = new JsonDeserializer<>();
        jp.setCurrentToken(JsonToken.VALUE_STRING);

        property.deserializeAndSet(jp, ctxt, bean);

        assertEquals(property.get(bean), valueTypeDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserializeAndSetWithRegularValue() throws IOException {
        InnerClassProperty property = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();

        jp.setCurrentToken(JsonToken.VALUE_STRING);

        property.deserializeAndSet(jp, ctxt, bean);

        assertEquals(property.get(bean), jp.getText());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        InnerClassProperty property = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        jp.setCurrentToken(JsonToken.VALUE_STRING);

        assertEquals(property.deserialize(jp, ctxt), property.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test
    public void testSet() throws IOException {
        InnerClassProperty property = new InnerClassProperty();
        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);

        assertEquals(property.get(instance), value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        InnerClassProperty property = new InnerClassProperty();
        Object instance = new Object();
        Object value = new Object();

        assertEquals(property.get(instance), property.setAndReturn(instance, value));
    }

    @Test
    public void testReadResolve() {
        InnerClassProperty property = new InnerClassProperty();
        assertEquals(new InnerClassProperty(property, property._annotated), property.readResolve());
    }

    @Test
    public void testWriteReplaceWithAnnotatedConstructor() {
        InnerClassProperty property = new InnerClassProperty();
        InnerClassProperty res = (InnerClassProperty) property.writeReplace();

        assertEquals(property, res);
    }

    @Test
    public void testWriteReplaceWithoutAnnotatedConstructor() {
        InnerClassProperty property = new InnerClassProperty();
        AnnotatedConstructor ann = null;
        InnerClassProperty res = (InnerClassProperty) property.writeReplace();

        assertEquals(new InnerClassProperty(property, new AnnotatedConstructor(null, property._creator, null, null)), res);
    }
}

