package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testCreatorProperty() {
        // Test default constructor
        CreatorProperty property = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        assertNull(property._annotated);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(-1, property._creatorIndex);
        assertFalse(property._ignorable);

        // Test constructor with annotated parameter
        AnnotatedParameter annotatedParameter = Mockito.mock(AnnotatedParameter.class);
        when(annotatedParameter.getName()).thenReturn(new PropertyName("testParameter"));
        when(annotatedParameter.getParameter()).thenReturn(new AnonymousClassInnerClass());
        when(annotatedParameter.getAnnotations()).thenReturn(new Annotations());
        CreatorProperty propertyWithAnnotated = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                annotatedParameter,
                0,
                null,
                null);
        assertEquals("testParameter", propertyWithAnnotated.getName().getSimpleName());
        assertEquals(AnnotatedParameter.class, propertyWithAnnotated._annotated.getClass());
        assertNull(propertyWithAnnotated._injectableValueId);
        assertNull(propertyWithAnnotated._fallbackSetter);
        assertEquals(0, propertyWithAnnotated._creatorIndex);
        assertFalse(propertyWithAnnotable._ignorable);

        // Test constructor with injectable value id
        when(annotatedParameter.getAnnotation(InjectableValues.class)).thenReturn(new InjectableValues());
        CreatorProperty propertyWithInjectableValueId = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                annotatedParameter,
                0,
                new Object(),
                null);
        assertEquals("testProperty", propertyWithInjectableValueId.getName().getSimpleName());
        assertEquals(AnnotatedParameter.class, propertyWithInjectableValueId._annotated.getClass());
        assertEquals(new Object(), propertyWithInjectableValueId._injectableValueId);
        assertNull(propertyWithInjectableValueId._fallbackSetter);
        assertEquals(0, propertyWithInjectableValueId._creatorIndex);
        assertFalse(propertyWithInjectableValueId._ignorable);
    }

    @Test
    public void testSetFallbackSetter() {
        CreatorProperty property = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);
        SettableBeanProperty fallbackSetter = Mockito.mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        assertSame(fallbackSetter, property._fallbackSetter);
    }

    @Test(expected = IOException.class)
    public void testMissingSetter() throws IOException {
        CreatorProperty property = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);
        property._verifySetter();
        fail("Expected IOException");
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        CreatorProperty property = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                0,
                new ObjectId("testId"),
                null);
        when(context.findInjectableValue(eq(new ObjectId("testId")), eq(property), eq(beanInstance))).thenReturn(new Object());
        Object injectedValue = property.findInjectableValue(context, beanInstance);
        assertEquals(new Object(), injectedValue);
    }

    @Test
    public void testInject() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        CreatorProperty property = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                0,
                new ObjectId("testId"),
                null);
        when(context.findInjectableValue(eq(new ObjectId("testId")), eq(property), eq(beanInstance))).thenReturn(new Object());
        property.inject(context, beanInstance);
        assertEquals(new Object(), property.get(beanInstance));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueException() throws JsonMappingException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        CreatorProperty property = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);
        when(context.findInjectableValue(eq(null), eq(property), eq(beanInstance))).thenThrow(new JsonMappingException());
        property.inject(context, beanInstance);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testToString() {
        CreatorProperty property = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);
        assertEquals("[creator property, name 'testProperty']", property.toString());
    }
}

class AnonymousClassInnerClass {
    // empty class
}

class ObjectId {
    final String id;

    ObjectId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectId that = (ObjectId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}