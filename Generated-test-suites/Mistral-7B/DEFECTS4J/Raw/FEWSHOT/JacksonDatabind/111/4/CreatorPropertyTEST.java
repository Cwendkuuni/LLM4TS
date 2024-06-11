package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testCreatorProperty() {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertNull(property.getWrapperName());
        assertNull(property.getTypeDeserializer());
        assertNull(property.getContextAnnotations());
        assertNull(property.getMetadata());
        assertNull(property._annotated);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertNull(property._creatorIndex);
        assertFalse(property._ignorable);

        contextAnnotations = new StandardAnnotations();
        Annotation[] annotations = contextAnnotations.getAnnotationMembers();
        param = new AnnotatedParameter(new AnnotatedElement(ClassUtil.classOf(CreatorPropertyTest.class), "testMethod"), 0);
        injectableValueId = new Object();
        property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertNotNull(property.getTypeDeserializer());
        assertArrayEquals(annotations, property.getContextAnnotations().getAnnotationMembers());
        assertNotNull(property._annotated);
        assertEquals(injectableValueId, property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(index, property._creatorIndex);
        assertFalse(property._ignorable);

        property = new CreatorProperty(property, new PropertyName("newName"));
        assertEquals("newName", property.getName().getShortName());
        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(metadata, property.getMetadata());
        assertEquals(param, property._annotated);
        assertEquals(injectableValueId, property._injectableValueId);
        assertEquals(fallbackSetter, property._fallbackSetter);
        assertEquals(index, property._creatorIndex);
        assertEquals(ignorable, property._ignorable);

        property = new CreatorProperty(property, new JsonDeserializer<>(){});
        assertEquals(property.getValueDeserializer(), property.getValueDeserializer());
        assertNotNull(property._valueDeserializer);

        property = new CreatorProperty(property, new NullValueProvider() {});
        assertEquals(property.getNullProvider(), property.getNullProvider());
        assertNotNull(property._nullProvider);

        property.fixAccess(null);
        assertNotNull(property._fallbackSetter);

        property.setFallbackSetter(null);
        try {
            property.setFallbackSetter(new CreatorProperty(property, new PropertyName("newName")));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            property.markAsIgnorable();
            property.set(null, null);
            fail();
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            property.inject(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            property.findInjectableValue(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAnnotations() {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedParameter param = new AnnotatedParameter(new AnnotatedElement(ClassUtil.classOf(CreatorPropertyTest.class), "testMethod"), 0);
        int index = 0;
        Object injectableValueId = null;
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        Annotation annotation1 = new MyAnnotation1();
        Annotation annotation2 = new MyAnnotation2();
        Annotations newContextAnnotations = new StandardAnnotations();
        newContextAnnotations.annotate(property, annotation1);
        newContextAnnotations.annotate(property, annotation2);
        assertEquals(annotation1, property.getAnnotation(MyAnnotation1.class));
        assertEquals(annotation2, property.getAnnotation(MyAnnotation2.class));

        AnnotatedMember member = property.getMember();
        Annotation[] annotations = member.getAnnotationMembers();
        assertEquals(annotation1, annotations[0]);
        assertEquals(annotation2, annotations[1]);
    }

    @Test
    public void testGetCreatorIndex() {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 1;
        Object injectableValueId = null;
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        assertEquals(index, property.getCreatorIndex());
    }

    @Test
    public void testIsIgnorable() {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        assertFalse(property.isIgnorable());

        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = new Object();
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        DeserializationContext context = null;
        Object beanInstance = new Object();
        Object value = property.findInjectableValue(context, beanInstance);
        assertNull(value);

        context = new TestDeserializationContext();
        PropertyName injectableValueName = PropertyName.construct("injectableValue");
        Object injectableValue = new Object();
        context.injectValues(new Object[] { injectableValueId, injectableValue }, new String[] { "injectableValueId", injectableValueName.getShortName() });
        value = property.findInjectableValue(context, beanInstance);
        assertEquals(injectableValue, value);
    }

    @Test
    public void testInject() throws IOException {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = new Object();
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        DeserializationContext context = null;
        Object beanInstance = new Object();
        Object value = new Object();
        property.inject(context, beanInstance);
        assertEquals(value, property.get(beanInstance));

        context = new TestDeserializationContext();
        Object injectableValue = new Object();
        context.injectValues(new Object[] { injectableValueId, injectableValue }, new String[] { "injectableValueId", injectableValue.getClass().getSimpleName() });
        property.inject(context, beanInstance);
        assertEquals(injectableValue, property.get(beanInstance));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        Object value = new Object();
        property.deserializeAndSet(p, ctxt, instance);
        assertEquals(value, property.get(instance));

        ctxt = new TestDeserializationContext();
        String json = "{\"name\":\"value\"}";
        p = ctxt.getFactory().createParser(json);
        value = new Object();
        property.deserializeAndSet(p, ctxt, instance);
        assertEquals(value, property.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        Object value = new Object();
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(value, result);

        ctxt = new TestDeserializationContext();
        String json = "{\"name\":\"value\"}";
        p = ctxt.getFactory().createParser(json);
        value = new Object();
        result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(value, result);
    }

    @Test
    public void testSet() throws IOException {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
        assertEquals(value, property.get(instance));

        property.set(null, value);
        throw new AssertionError("Should not reach this point");
    }

    @Test
    public void testSetAndReturn() throws IOException {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        Object instance = new Object();
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);
        assertEquals(value, result);

        property.setAndReturn(null, value);
        throw new AssertionError("Should not reach this point");
    }

    @Test
    public void testToString() {
        PropertyName name = PropertyName.construct("name");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        String expected = "[creator property, name 'name'; inject id null]";
        assertEquals(expected, property.toString());

        contextAnnotations = new StandardAnnotations();
        Annotation[] annotations = contextAnnotations.getAnnotationMembers();
        param = new AnnotatedParameter(new AnnotatedElement(ClassUtil.classOf(CreatorPropertyTest.class), "testMethod"), 0);
        injectableValueId = new Object();
        property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        expected = "[creator property, name 'name'; inject id " + injectableValueId.toString() + "]";
        assertEquals(expected, property.toString());
    }

    static class MyAnnotation1 {}
    static class MyAnnotation2 {}

    static class TestDeserializationContext extends DeserializationContext {
        @Override
        public Object findInjectableValue(Object id, Object bean, BeanProperty property) {
            switch (id.getClass().getName()) {
                case "java.lang.String":
                    return PropertyName.construct((String) id).getShortName();
                default:
                    return super.findInjectableValue(id, bean, property);
            }
        }

        @Override
        public void injectValues(Object[] ids, String[] names) throws IOException {
            for (int i = 0; i < ids.length; i++) {
                Object id = ids[i];
                String name = names[i];
                setInjectableValueId(id, PropertyName.construct(name));
            }
        }
    }
}