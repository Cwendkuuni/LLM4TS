package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.CreatorProperty.SettableBeanProperty;

/**
 * Test cases for CreatorProperty class
 */
public class CreatorPropertyTest extends BaseTest {

    // ###  ###

    // Test constructor with valid arguments
    @Test
    public void testCreatorPropertyConstructor() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertEquals("testProperty", property.getName().getSimple());
        assertEquals(String.class, property.getType().getRawClass());
        assertNull(property._fallbackSetter);
        assertEquals(0, property._creatorIndex);
        assertFalse(property._ignorable);
        assertEquals(injectableValueId, property._injectableValueId);
    }

    // Test constructor with null arguments
    @Test(expected = IllegalArgumentException.class)
    public void testCreatorPropertyConstructorNullArguments() throws Exception {
        PropertyName name = null;
        JavaType type = null;
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = -1;
        Object injectableValueId = null;
        new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
    }

    // Test setFallbackSetter method
    @Test
    public void testSetFallbackSetter() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);

        assertEquals(fallbackSetter, property._fallbackSetter);
    }

    // Test isIgnorable method
    @Test
    public void testIsIgnorable() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertFalse(property.isIgnorable());

        property.markAsIgnorable();

        assertTrue(property.isIgnorable());
    }

    // Test findInjectableValue method
    @Test
    public void testFindInjectableValue() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        Object injectedValue = "injectedValue";
        Object beanInstance = new Object();

        when(context.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn(injectedValue);

        assertEquals(injectedValue, property.findInjectableValue(context, beanInstance));
    }

    // Test inject method
    @Test
    public void testInject() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        Object injectedValue = "injectedValue";
        Object beanInstance = new Object();

        property.inject(context, beanInstance);

        assertEquals(injectedValue, property.get(beanInstance));
    }

    // Test deserializeAndSet method
    @Test
    public void testDeserializeAndSet() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        Object beanInstance = new Object();
        String inputValue = "inputValue";

        JsonParser p = createParser(mapper, "{\"testProperty\":\"" + inputValue + "\"}");

        property.deserializeAndSet(p, context, beanInstance);

        assertEquals(inputValue, property.get(beanInstance));
    }

    // Test deserializeSetAndReturn method
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        Object beanInstance = new Object();
        String inputValue = "inputValue";

        JsonParser p = createParser(mapper, "{\"testProperty\":\"" + inputValue + "\"}");

        when(property._fallbackSetter.setAndReturn(beanInstance, any())).thenReturn(beanInstance);

        Object returnedValue = property.deserializeSetAndReturn(p, context, beanInstance);

        assertEquals(inputValue, returnedValue.toString());
    }

    // Test set method
    @Test
    public void testSet() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        Object beanInstance = new Object();
        String inputValue = "inputValue";

        property.set(beanInstance, inputValue);

        assertEquals(inputValue, property.get(beanInstance));
    }

    // Test setAndReturn method
    @Test
    public void testSetAndReturn() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        Object beanInstance = new Object();
        String inputValue = "inputValue";

        when(property._fallbackSetter.setAndReturn(beanInstance, any())).thenReturn(beanInstance);

        Object returnedValue = property.setAndReturn(beanInstance, inputValue);

        assertEquals(inputValue, returnedValue.toString());
    }

    // Test toString method
    @Test
    public void testToString() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertEquals("[creator property, name 'testProperty'; inject id 'injectValueId']", property.toString());
    }

    // Test _verifySetter method
    @Test(expected = IOException.class)
    public void testVerifySetterNullFallbackSetter() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        property._verifySetter();
    }

    // Test _reportMissingSetter method
    @Test(expected = InvalidDefinitionException.class)
    public void testReportMissingSetter() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mapper.getTypeDeserializer();
        Annotations contextAnnotations = context.getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        JsonParser p = createParser(mapper, "{}");

        property._reportMissingSetter(p, context);
    }

    // ###  ###
}

class MyClass {
    @JsonCreator
    static public MyClass create(@JsonProperty("testProperty") String value) {
        return new MyClass(value);
    }

    private final String testProperty;

    public MyClass(String testProperty) {
        this.testProperty = testProperty;
    }
}