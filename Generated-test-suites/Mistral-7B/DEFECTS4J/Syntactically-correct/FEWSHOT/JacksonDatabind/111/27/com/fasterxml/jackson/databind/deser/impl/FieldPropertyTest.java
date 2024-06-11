package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NullValueProvider;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldPropertyTest {

    @Test
    public void testFieldProperty() throws Exception {
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(TestClass.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(TestClass.class, "testField", JsonType.LONG);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedField field = new AnnotatedField("testField", TestClass.class.getField("testField"), new JsonProperty());
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        TestClass instance = mapper.readValue(new ObjectMapper().writeValueAsString(new TestClass(0L)), type);

        // Test set and get
        Long expectedValue = 1L;
        Long value = 1L;
        property.set(instance, value);
        assertEquals(expectedValue, instance.getTestField());

        // Test deserialization
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new TestClass(null))));
        property.deserializeAndSet(parser, null, instance);
        assertEquals(null, instance.getTestField());

        parser = mapper.getFactory().createParser(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new TestClass(1L))));
        property.deserializeAndSet(parser, null, instance);
        assertEquals(expectedValue, instance.getTestField());

        // Test deserialization with type deserializer
        TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
        property = new FieldProperty(propDef, type, typeDeserializer, contextAnnotations, field);
        parser = mapper.getFactory().createParser(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new TestClass((Long) null))));
        property.deserializeAndSet(parser, null, instance);
        assertNull(instance.getTestField());

        parser = mapper.getFactory().createParser(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new TestClass((Long) 1))));
        property.deserializeAndSet(parser, null, instance);
        assertEquals(expectedValue, instance.getTestField());

        // Test deserialization with null provider
        NullValueProvider nullProvider = new StandardNullValueProvider();
        property = new FieldProperty(propDef, type, typeDeserializer, new StandardAnnotations(), field);
        property.setNullProvider(nullProvider);
        parser = mapper.getFactory().createParser(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new TestClass(null))));
        property.deserializeAndSet(parser, null, instance);
        assertEquals(null, instance.getTestField());

        parser = mapper.getFactory().createParser(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new TestClass(1L))));
        property.deserializeAndSet(parser, null, instance);
        assertEquals(expectedValue, instance.getTestField());

        // Test with value deserializer
        JsonDeserializer<Long> valueDeserializer = new StandardDeserializer<Long>(Long.class);
        property = new FieldProperty(propDef, type, typeDeserializer, contextAnnotations, field);
        property.setValueDeserializer(valueDeserializer);
        parser = mapper.getFactory().createParser(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new TestClass((Long) null))));
        property.deserializeAndSet(parser, null, instance);
        assertNull(instance.getTestField());

        parser = mapper.getFactory().createParser(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new TestClass("1"))));
        property.deserializeAndSet(parser, null, instance);
        assertEquals(1L, instance.getTestField());

        // Test with skipNulls
        NullValueProvider nullProvider2 = new StandardNullValueProvider();
        property = new FieldProperty(propDef, type, typeDeserializer, contextAnnotations, field);
        property.setNullProvider(nullProvider2);
        property.setSkipNulls(true);
        parser = mapper.getFactory().createParser(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new TestClass(null))));
        property.deserializeAndSet(parser, null, instance);
        assertNull(instance.getTestField());

        parser = mapper.getFactory().createParser(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new TestClass(0L))));
        property.deserializeAndSet(parser, null, instance);
        assertEquals(0L, instance.getTestField());

        // Test readResolve
        TestClass instance2 = mapper.readValue(new ObjectMapper().writeValueAsString(new TestClass(1L)), type);
        FieldProperty property2 = (FieldProperty) instance2.getClass().getDeclaredField("testFieldProperty")
                .get(instance2);
        assertNotNull(property2);
        TestClass instance3 = (TestClass) property2.readResolve();
        assertEquals(1L, instance3.getTestField());
    }

    static class TestClass {
        private static final long serialVersionUID = 1L;

        @JsonProperty
        Long testField;

        public TestClass(Long testField) {
            this.testField = testField;
        }

        public Long getTestField() {
            return testField;
        }
    }
}