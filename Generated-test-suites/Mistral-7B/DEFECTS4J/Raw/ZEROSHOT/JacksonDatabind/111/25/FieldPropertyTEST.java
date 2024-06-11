package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class FieldPropertyTest {

    @Test
    public void testFieldProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        JsonParser parser = mapper.getFactory().createParser("{\"field1\":1}");

        BeanPropertyDefinition propertyDefinition = new BeanPropertyDefinition("field1", TestBean.class, int.class, null, null, null);
        JavaType javaType = TypeFactory.defaultInstance().constructType(int.class);
        TypeDeserializer typeDeserializer = null;
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedField annotatedField = new AnnotatedField(new AnnotatedMember(TestBean.class.getField("field1")), contextAnnotations);
        FieldProperty fieldProperty = new FieldProperty(propertyDefinition, javaType, typeDeserializer, contextAnnotations, annotatedField);

        // test deserializeAndSet
        fieldProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), bean);
        assertEquals(1, bean.getField1());

        // test deserializeSetAndReturn
        Object result = fieldProperty.deserializeSetAndReturn(parser, mapper.getDeserializationContext(), bean);
        assertEquals(bean, result);
        assertEquals(1, bean.getField1());

        // test set
        Object setValue = new Integer(2);
        fieldProperty.set(bean, setValue);
        assertEquals(2, bean.getField1());

        // test setAndReturn
        Object resultSetAndReturn = fieldProperty.setAndReturn(bean, setValue);
        assertEquals(bean, resultSetAndReturn);
        assertEquals(2, bean.getField1());

        // test getter methods
        assertEquals(fieldProperty.getName(), "field1");
        assertEquals(fieldProperty.getBeanClass(), TestBean.class);
        assertEquals(fieldProperty.getType(), javaType);
        assertEquals(fieldProperty.getValueDeserializer(), null);
        assertEquals(fieldProperty.getNullProvider(), null);

        // test withName
        PropertyName newName = PropertyName.construct("newFieldName");
        SettableBeanProperty newFieldProperty = fieldProperty.withName(newName);
        assertEquals(newName, newFieldProperty.getName());
        assertNotSame(fieldProperty, newFieldProperty);

        // test withValueDeserializer
        JsonDeserializer<String> valueDeserializer = mapper.findValueDeserializer(String.class);
        SettableBeanProperty newFieldPropertyWithValueDeserializer = fieldProperty.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, newFieldPropertyWithValueDeserializer.getValueDeserializer());
        assertNotSame(fieldProperty, newFieldPropertyWithValueDeserializer);

        // test withNullProvider
        NullValueProvider nullValueProvider = mapper.getConfiguration().getDefaultNullValueProvider();
        SettableBeanProperty newFieldPropertyWithNullProvider = fieldProperty.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, newFieldPropertyWithNullProvider.getNullProvider());
        assertNotSame(fieldProperty, newFieldPropertyWithNullProvider);

        // test fixAccess
        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
        fieldProperty.fixAccess(deserializationConfig);
        assertTrue(deserializationConfig.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
        TestBean testBeanWithPrivateField = new TestBean();
        Field privateField = TestBean.class.getDeclaredField("privateField");
        privateField.setAccessible(true);
        privateField.set(testBeanWithPrivateField, new Object());
        fieldProperty.set(testBeanWithPrivateField, "test");
        assertNull(testBeanWithPrivateField.getPrivateField());

        // test getAnnotation
        assertEquals(TestAnnotation.class, fieldProperty.getAnnotation(TestAnnotation.class));
        assertNull(fieldProperty.getAnnotation(NoSuchElementException.class));

        // test getMember
        assertEquals(annotatedField, fieldProperty.getMember());

        parser.close();
    }

    static class TestBean {
        private int field1;
        private int privateField;

        @TestAnnotation
        public int getField1() {
            return field1;
        }

        @TestAnnotation
        public void setField1(int field1) {
            this.field1 = field1;
        }

        @TestAnnotation
        public int getPrivateField() {
            return privateField;
        }

        @TestAnnotation
        public void setPrivateField(int privateField) {
            this.privateField = privateField;
        }
    }

    @TestAnnotation
    static class TestAnnotation {}
}