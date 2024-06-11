package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class CreatorPropertyTest {

    private ObjectBean objectBean;
    private CreatorProperty creatorProperty;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        AnnotatedClass ac = factory.constructAnnotatedClass(ObjectBean.class);
        ObjectReader reader = mapper.readerFor(ObjectBean.class).forType(new TypeReference<List<ObjectBean>>() {});
        objectBean = reader.readValue(factory.createParser("[\n" +
                "  {\"id\": 1, \"name\": \"John\", \"age\": 25},\n" +
                "  {\"id\": 2, \"name\": \"Jane\", \"age\": 30}\n" +
                "]"));

        PropertyName name = PropertyName.construct("name");
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        PropertyName wrapperName = PropertyName.construct("list");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = "name";
        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
    }

    @Test
    public void testConstructor() {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(creatorProperty._annotated);
        assertFalse(creatorProperty.isIgnorable());
        assertNull(creatorProperty._fallbackSetter);
        assertEquals(0, creatorProperty._creatorIndex);

        CreatorProperty creatorProperty1 = new CreatorProperty(null, null, null, null, null, null, 0, "name", null);
        assertNotNull(creatorProperty1._annotated);
        assertFalse(creatorProperty1.isIgnorable());
        assertNull(creatorProperty1._fallbackSetter);
        assertEquals(0, creatorProperty1._creatorIndex);

        CreatorProperty creatorProperty2 = new CreatorProperty(null, null, null, null, null, null, 0, null, null, new PropertyMetadata());
        assertNotNull(creatorProperty2._annotated);
        assertFalse(creatorProperty2.isIgnorable());
        assertNull(creatorProperty2._fallbackSetter);
        assertEquals(0, creatorProperty2._creatorIndex);

        CreatorProperty creatorProperty3 = new CreatorProperty(null, null, null, null, null, null, 0, null, null, null, new PropertyMetadata());
        assertNotNull(creatorProperty3._annotated);
        assertFalse(creatorProperty3.isIgnorable());
        assertNull(creatorProperty3._fallbackSetter);
        assertEquals(0, creatorProperty3._creatorIndex);
    }

    @Test
    public void testConstructorWithValues() throws Exception {
        PropertyName name = PropertyName.construct("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("list");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = new AnnotatedParameter(new Constructor<ObjectBean>() {}.getParameterTypes()[0], ObjectBean.class, "_name");
        int index = 0;
        Object injectableValueId = "name";
        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertEquals("name", creatorProperty.getName().asString());
        assertEquals(String.class, creatorProperty.getType().getRawClass());
        assertEquals(null, creatorProperty._annotated);
        assertFalse(creatorProperty.isIgnorable());
        assertNull(creatorProperty._fallbackSetter);
        assertEquals(0, creatorProperty._creatorIndex);
        assertEquals("name", creatorProperty.getInjectableValueId().toString());
    }

    @Test
    public void testCopyConstructor() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        CreatorProperty copiedCreatorProperty = new CreatorProperty(creatorProperty, null);

        assertSame(creatorProperty, copiedCreatorProperty);
        assertSame(creatorProperty._annotated, copiedCreatorProperty._annotated);
        assertSame(creatorProperty._injectableValueId, copiedCreatorProperty._injectableValueId);
        assertSame(creatorProperty._fallbackSetter, copiedCreatorProperty._fallbackSetter);
        assertSame(creatorProperty._creatorIndex, copiedCreatorProperty._creatorIndex);
        assertSame(creatorProperty._ignorable, copiedCreatorProperty._ignorable);
    }

    @Test
    public void testCopyConstructorWithValues() throws Exception {
        PropertyName name = PropertyName.construct("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("list");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = new AnnotatedParameter(new Constructor<ObjectBean>() {}.getParameterTypes()[0], ObjectBean.class, "_name");
        int index = 0;
        Object injectableValueId = "name";
        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        CreatorProperty copiedCreatorProperty = new CreatorProperty(creatorProperty, null);

        assertEquals(creatorProperty, copiedCreatorProperty);
        assertEquals(creatorProperty.getName(), copiedCreatorProperty.getName());
        assertEquals(creatorProperty.getType(), copiedCreatorProperty.getType());
        assertEquals(creatorProperty._annotated, copiedCreatorProperty._annotated);
        assertEquals(creatorProperty._injectableValueId, copiedCreatorProperty._injectableValueId);
        assertEquals(creatorProperty._fallbackSetter, copiedCreatorProperty._fallbackSetter);
        assertEquals(creatorProperty._creatorIndex, copiedCreatorProperty._creatorIndex);
        assertEquals(creatorProperty._ignorable, copiedCreatorProperty._ignorable);
    }

    @Test
    public void testCopyConstructorWithDeserializer() throws Exception {
        PropertyName name = PropertyName.construct("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("list");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = new AnnotatedParameter(new Constructor<ObjectBean>() {}.getParameterTypes()[0], ObjectBean.class, "_name");
        int index = 0;
        Object injectableValueId = "name";
        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        JsonDeserializer<String> deserializer = new StdConverter.StdConverterDeserializer(new StdConverter<String, Integer>() {});
        CreatorProperty copiedCreatorProperty = new CreatorProperty(creatorProperty, deserializer);

        assertEquals(creatorProperty, copiedCreatorProperty);
        assertEquals(creatorProperty.getName(), copiedCreatorProperty.getName());
        assertEquals(creatorProperty.getType(), copiedCreatorProperty.getType());
        assertEquals(creatorProperty._annotated, copiedCreatorProperty._annotated);
        assertEquals(creatorProperty._injectableValueId, copiedCreatorProperty._injectableValueId);
        assertEquals(deserializer, copiedCreatorProperty._valueDeserializer);
        assertEquals(creatorProperty._fallbackSetter, copiedCreatorProperty._fallbackSetter);
        assertEquals(creatorProperty._creatorIndex, copiedCreatorProperty._creatorIndex);
        assertEquals(creatorProperty._ignorable, copiedCreatorProperty._ignorable);
    }

    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, creatorProperty._fallbackSetter);
    }

    @Test(expected = IOException.class)
    public void testSetFallbackSetterNull() {
        creatorProperty.setFallbackSetter(null);
    }

    @Test
    public void testIsIgnorable() {
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testMarkAsIgnorableTwice() {
        creatorProperty.markAsIgnorable();
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        Object value = creatorProperty.findInjectableValue(null, objectBean);
        assertNull(value);
    }

    @Test
    public void testFindInjectableValueNotNull() throws IOException {
        Object value = creatorProperty.findInjectableValue(null, new ObjectBean(1, "John", 25));
        assertEquals("John", value);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueNullBean() throws IOException {
        creatorProperty.findInjectableValue(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueNullInjectableValueId() throws IOException {
        creatorProperty._injectableValueId = null;
        creatorProperty.findInjectableValue(null, objectBean);
    }

    @Test
    public void testInject() throws IOException {
        creatorProperty.inject(null, objectBean);
        assertNull(objectBean.get(0).getName());

        String name = "John";
        objectBean.get(0).setName(name);
        creatorProperty.inject(null, objectBean);
        assertEquals(name, objectBean.get(0).getName());
    }

    @Test(expected = IOException.class)
    public void testInjectNullBean() throws IOException {
        creatorProperty.inject(null, null);
    }

    @Test(expected = IOException.class)
    public void testInjectNullValue() throws IOException {
        creatorProperty.inject(null, new ObjectBean(1, null, 25));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(ObjectBean.class);
        JsonParser p = reader.getParser();
        creatorProperty.deserializeAndSet(p, null, objectBean);
        assertEquals("John", objectBean.get(0).getName());
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetNullBean() throws IOException {
        creatorProperty.deserializeAndSet(null, null, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetNullValue() throws IOException {
        creatorProperty.deserializeAndSet(null, null, new ObjectBean());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(ObjectBean.class);
        JsonParser p = reader.getParser();
        Object value = creatorProperty.deserializeSetAndReturn(p, null, objectBean);
        assertEquals("John", value);
        assertEquals("John", objectBean.get(0).getName());
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnNullBean() throws IOException {
        creatorProperty.deserializeSetAndReturn(null, null, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnNullValue() throws IOException {
        creatorProperty.deserializeSetAndReturn(null, null, new ObjectBean());
    }

    @Test
    public void testSet() throws IOException {
        String name = "John";
        creatorProperty.set(objectBean, name);
        assertEquals(name, objectBean.get(0).getName());
    }

    @Test(expected = IOException.class)
    public void testSetNullBean() throws IOException {
        creatorProperty.set(null, "John");
    }

    @Test(expected = IOException.class)
    public void testSetNullValue() throws IOException {
        creatorProperty.set(objectBean, null);
    }

    @Test(expected = IOException.class)
    public void testSetNullBeanAndValue() throws IOException {
        creatorProperty.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        String name = "John";
        Object value = creatorProperty.setAndReturn(objectBean, name);
        assertEquals(name, value);
        assertEquals(name, objectBean.get(0).getName());
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnNullBean() throws IOException {
        creatorProperty.setAndReturn(null, "John");
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnNullValue() throws IOException {
        creatorProperty.setAndReturn(objectBean, null);
    }

    @Test
    public void testSetAndReturnNullBeanAndValue() throws IOException {
        creatorProperty.setAndReturn(null, null);
    }

    @Test
    public void testToString() {
        String toString = creatorProperty.toString();
        String expected = "[creator property, name 'name'; inject id 'name']";
        assertEquals(expected, toString);
    }

    @Test
    public void testGetAnnotation() {
        Annotation jsonProperty = new JsonProperty();
        creatorProperty._annotated = new AnnotatedElement() {
            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[]{jsonProperty};
            }
        };
        assertEquals(jsonProperty, creatorProperty.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetAnnotationNull() {
        assertNull(creatorProperty.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(creatorProperty._annotated, creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, creatorProperty.getCreatorIndex());
    }
}

class ObjectBean {

    private int id;
    private String name;
    private int age;

    public ObjectBean(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "ObjectBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}