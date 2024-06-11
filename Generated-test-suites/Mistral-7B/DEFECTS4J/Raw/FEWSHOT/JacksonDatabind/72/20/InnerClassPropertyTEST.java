package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.annotation.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassProperty() {
        ObjectMapper mapper = new ObjectMapper();

        // Test constructor with delegate and creator
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotatedClass(AnnotatedClass.class), "foo");
        Constructor<?> creator = AnnotatedClass.class.getDeclaredConstructor(String.class);
        InnerClassProperty property = new InnerClassProperty(delegate, creator);
        assertEquals(delegate, property._delegate);
        assertEquals(creator, property._creator);

        // Test constructor with JDK serialization
        property = new InnerClassProperty(property, null);
        assertEquals(property._delegate, property._delegate);
        assertNull(property._creator);

        // Test constructor with JsonDeserializer
        JsonDeserializer<AnnotatedClass> deserializer = mapper.deserializerForClass(AnnotatedClass.class);
        property = new InnerClassProperty(property, deserializer);
        assertEquals(property._delegate, property._delegate);
        assertEquals(deserializer, property._valueDeserializer);
        assertNull(property._creator);

        // Test constructor with PropertyName
        PropertyName name = PropertyName.constructor0("foo");
        property = new InnerClassProperty(property, name);
        assertEquals(property._delegate, new InnerClassProperty(property, name)._delegate);
        assertEquals(property._name, name);

        // Test getters and setters
        Object bean = new AnnotatedClass();
        Object innerBean = new InnerClass();

        property.setValueDeserializer(deserializer);
        property.assignIndex(0);

        // Test deserialization
        JsonParser jp = mapper.parserForObject("{\"foo\":\"innerClassValue\"}", new StandardDeserializationContext());
        property.deserializeAndSet(jp, ctxt, bean);
        assertEquals("innerClassValue", ((AnnotatedClass) bean).getFoo().getValue());

        // Test set and return
        Object returnedValue = property.setAndReturn(bean, innerBean);
        assertEquals(innerBean, returnedValue);
        assertEquals(innerBean, ((AnnotatedClass) bean).getFoo().getValue());

        // Test JDK serialization
        Object writable = mapper.writerForObject(bean).writeValueAsBytes(new ByteArrayOutputStream());
        Object readable = mapper.readerForObject(new ByteArrayInputStream(writable));
        assertEquals(bean, readable);

        // Test getters and setters with annotation
        property = new InnerClassProperty(property, AnnotatedClassWithAnnotation.class.getDeclaredField("foo"), null, null);
        AnnotatedClassWithAnnotation annotatedObject = new AnnotatedClassWithAnnotation();
        Object annotatedValue = new AnnotatedInnerClassWithAnnotation();

        property.setValueDeserializer(deserializer);
        property.assignIndex(0);

        jp = mapper.parserForObject("{\"foo\":\"innerClassValueWithAnnotation\"}", new StandardDeserializationContext());
        property.deserializeAndSet(jp, ctxt, annotatedObject);
        assertEquals("innerClassValueWithAnnotation", ((AnnotatedClassWithAnnotation) annotatedObject).getFoo().getValue().getName());

        property.set(annotatedObject, annotatedValue);
        assertEquals(annotatedValue, ((AnnotatedClassWithAnnotation) annotatedObject).getFoo().getValue());
    }
}

class InnerClass {
    String value;
}

class AnnotatedClass {
    @JsonProperty
    InnerClass foo;
}

class AnnotatedClassWithAnnotation {
    @JsonProperty
    @JsonSerialize(using = AnnotatedInnerClassSerializer.class)
    AnnotatedInnerClassWithAnnotation foo;
}

class AnnotatedInnerClassWithAnnotation {
    @JsonValue
    String name;
}

class AnnotatedInnerClassSerializer extends JsonSerializer<AnnotatedInnerClassWithAnnotation> {
    @Override
    public void serialize(AnnotatedInnerClassWithAnnotation value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value.name);
    }
}