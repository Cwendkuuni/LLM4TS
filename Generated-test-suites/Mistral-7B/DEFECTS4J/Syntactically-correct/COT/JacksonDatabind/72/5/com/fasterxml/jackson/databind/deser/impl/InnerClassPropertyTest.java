package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(new SettableBeanProperty(new Object()), new Constructor<>());
        assertNotNull(innerClassProperty);
    }

    @Test
    public void testConstructorWithAnnotation() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(new SettableBeanProperty(new Object()), new AnnotatedConstructor(null, null, null, null));
        assertNotNull(innerClassProperty);
    }

    @Test
    public void testConstructorWithDeserializer() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(new SettableBeanProperty(new Object()), new JsonDeserializer<>());
        assertNotNull(innerClassProperty);
    }

    @Test
    public void testConstructorWithName() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(new SettableBeanProperty(new Object()), new PropertyName("test"));
        assertNotNull(innerClassProperty);
    }

    @Test
    public void testOverrideMethods() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty(new Object());
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, new Constructor<>());

        assertSame(delegate, innerClassProperty._delegate);
        assertSame(new Constructor<>(), innerClassProperty._creator);

        delegate = new SettableBeanProperty(new Object()).withValueDeserializer(new JsonDeserializer<>());
        innerClassProperty = new InnerClassProperty(delegate, new JsonDeserializer<>());

        assertSame(delegate, innerClassProperty._delegate);
        assertSame(new JsonDeserializer<>(), innerClassProperty._valueDeserializer);
        assertSame(new Constructor<>(), innerClassProperty._creator);

        delegate = new SettableBeanProperty(new Object()).withName(new PropertyName("test"));
        innerClassProperty = new InnerClassProperty(delegate, new PropertyName("test"));

        assertSame(delegate, innerClassProperty._delegate);
        assertSame(new PropertyName("test"), innerClassProperty.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCreator() throws Exception {
        new InnerClassProperty(new SettableBeanProperty(new Object()), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAnnotated() throws Exception {
        new InnerClassProperty(new SettableBeanProperty(new Object()), new AnnotatedConstructor(null, null, null, null));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": null}";
        InnerClassProperty innerClassProperty = new InnerClassProperty(new SettableBeanProperty(new Object()), new JsonDeserializer<>());
        mapper.registerDeserializer(innerClassProperty.getType(), innerClassProperty);

        Object bean = mapper.readValue(json, innerClassProperty.getType());
        innerClassProperty.deserializeAndSet(mapper.getFactory().createParser(json), new TestContext(), bean);

        assertNull(bean);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": null}";
        InnerClassProperty innerClassProperty = new InnerClassProperty(new SettableBeanProperty(new Object()), new JsonDeserializer<>());
        mapper.registerDeserializer(innerClassProperty.getType(), innerClassProperty);

        Object bean = mapper.readValue(json, innerClassProperty.getType());
        innerClassProperty.deserializeAndSet(mapper.getFactory().createParser(json), new TestContext(), bean);

        assertNull(bean);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": null}";
        InnerClassProperty innerClassProperty = new InnerClassProperty(new SettableBeanProperty(new Object()), new JsonDeserializer<>());
        mapper.registerDeserializer(innerClassProperty.getType(), innerClassProperty);

        Object bean = mapper.readValue(json, innerClassProperty.getType());
        Object result = innerClassProperty.deserializeSetAndReturn(mapper.getFactory().createParser(json), new TestContext(), bean);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": null}";
        InnerClassProperty innerClassProperty = new InnerClassProperty(new SettableBeanProperty(new Object()), new JsonDeserializer<>());
        mapper.registerDeserializer(innerClassProperty.getType(), innerClassProperty);

        Object bean = mapper.readValue(json, innerClassProperty.getType());
        innerClassProperty.set(bean, new Object());
    }

    @Test(expected = IOException.class)
    public void testSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": null}";
        InnerClassProperty innerClassProperty = new InnerClassProperty(new SettableBeanProperty(new Object()), new JsonDeserializer<>());
        mapper.registerDeserializer(innerClassProperty.getType(), innerClassProperty);

        Object bean = mapper.readValue(json, innerClassProperty.getType());
        Object result = innerClassProperty.setAndReturn(bean, new Object());
        assertNull(result);
    }

    @Test
    public void testReadResolve() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\": \"com.fasterxml.jackson.databind.deser.impl.InnerClassProperty\",\"innerClassProperty\":{\"delegate\":{\"class\":\"com.fasterxml.jackson.databind.PropertyName\",\"name\":\"test\"},\"creator\":{\"class\":\"java.lang.reflect.Constructor\",\"declaringClass\":{\"class\":\"com.fasterxml.jackson.databind.deser.impl.InnerClassProperty\"},\"name\":\"<init>\",\"parameterTypes\":[],\"exceptionTypes\":[]}}";
        InnerClassProperty innerClassProperty = mapper.readValue(json, InnerClassProperty.class);
        assertNotNull(innerClassProperty);
    }

    @Test
    public void testWriteReplace() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object innerClassProperty = new InnerClassProperty(new SettableBeanProperty(new Object()), new Constructor<>());
        String json = mapper.writeValueAsString(innerClassProperty);
        assertNotNull(json);
    }
}