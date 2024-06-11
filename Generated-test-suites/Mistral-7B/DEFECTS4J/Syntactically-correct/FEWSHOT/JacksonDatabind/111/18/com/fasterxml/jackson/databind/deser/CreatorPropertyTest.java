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

public class CreatorPropertyTest {

    @Test
    public void testCreatorProperty() {
        // Test empty constructor
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(property._annotated);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(0, property._creatorIndex);
        assertFalse(property._ignorable);

        // Test constructor with parameters
        AnnotatedParameter param = createMockAnnotatedParameter();
        CreatorProperty propertyWithParam = new CreatorProperty(
                new PropertyName("test"), JavaType.of(String.class), null, null,
                new Annotations(), param, 0, null, null);
        assertEquals("test", propertyWithParam.getName().asString());
        assertEquals(String.class, propertyWithParam.getType().getRawClass());
        assertEquals(param, propertyWithParam._annotated);
        assertNull(propertyWithParam._injectableValueId);
        assertNull(propertyWithParam._fallbackSetter);
        assertEquals(0, propertyWithParam._creatorIndex);
        assertFalse(propertyWithParam._ignorable);
    }

    @Test
    public void testCreatorPropertyWithInjectableValueId() {
        String injectableValueId = "myInjectedValueId";
        CreatorProperty property = new CreatorProperty(
                new PropertyName("test"), JavaType.of(String.class), null, null,
                new Annotations(), null, 0, injectableValueId, null);
        assertEquals("test", property.getName().asString());
        assertEquals(String.class, property.getType().getRawClass());
        assertNull(property._annotated);
        assertEquals(injectableValueId, property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(0, property._creatorIndex);
        assertFalse(property._ignorable);
    }

    @Test
    public void testCreatorPropertyWithFallbackSetter() {
        String propertyName = "test";
        PropertyName wrapperName = new PropertyName("wrapper");
        SettableBeanProperty fallbackSetter = createMockSettableBeanProperty();
        CreatorProperty property = new CreatorProperty(
                new PropertyName(propertyName), JavaType.of(String.class), wrapperName,
                null, new Annotations(), null, 0, null, fallbackSetter);
        assertEquals(propertyName, property.getName().asString());
        assertEquals(String.class, property.getType().getRawClass());
        assertNull(property._annotated);
        assertNull(property._injectableValueId);
        assertEquals(fallbackSetter, property._fallbackSetter);
        assertEquals(0, property._creatorIndex);
        assertFalse(property._ignorable);
    }

    @Test
    public void testCreatorPropertyWithCreatorIndex() {
        String propertyName = "test";
        CreatorProperty property = new CreatorProperty(
                new PropertyName(propertyName), JavaType.of(String.class), null, null,
                new Annotations(), null, 5, null, null);
        assertEquals(propertyName, property.getName().asString());
        assertEquals(String.class, property.getType().getRawClass());
        assertNull(property._annotated);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(5, property._creatorIndex);
        assertFalse(property._ignorable);
    }

    @Test
    public void testCreatorPropertyWithIgnorableFlag() {
        String propertyName = "test";
        CreatorProperty property = new CreatorProperty(
                new PropertyName(propertyName), JavaType.of(String.class), null, null,
                new Annotations(), null, 0, null, null);
        property.markAsIgnorable();
        assertTrue(property._ignorable);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testCreatorPropertyWithoutSetter() throws IOException {
        String propertyName = "test";
        CreatorProperty property = new CreatorProperty(
                new PropertyName(propertyName), JavaType.of(String.class), null, null,
                new Annotations(), null, 0, null, null);
        property.setFallbackSetter(null);
        property.deserializeAndSet(null, null, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValue() throws IOException {
        String propertyName = "test";
        CreatorProperty property = new CreatorProperty(
                new PropertyName(propertyName), JavaType.of(String.class), null, null,
                new Annotations(), null, 0, null, null);
        Object instance = new Object();
        property.setFallbackSetter(new PropertyName("fallback"));
        JsonParser p = createMockJsonParser();
        property.deserializeAndSet(p, null, instance);
        assertNull(instance.getClass().getDeclaredField("fallback").get(instance));
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        String propertyName = "test";
        Object beanInstance = new Object();
        CreatorProperty property = new CreatorProperty(
                new PropertyName(propertyName), JavaType.of(String.class), null, null,
                new Annotations(), null, 0, "injectValueId", null);
        Object injectedValue = "injectedValue";
        DeserializationContext context = createMockDeserializationContext();
        when(context.findInjectableValue("injectValueId", property, beanInstance)).thenReturn(injectedValue);
        Object result = property.findInjectableValue(context, beanInstance);
        assertEquals(injectedValue, result);
    }

    @Test
    public void testInject() throws IOException {
        String propertyName = "test";
        Object beanInstance = new Object();
        CreatorProperty property = new CreatorProperty(
                new PropertyName(propertyName), JavaType.of(String.class), null, null,
                new Annotations(), null, 0, "injectValueId", null);
        String injectedValue = "injectedValue";
        DeserializationContext context = createMockDeserializationContext();
        when(context.findInjectableValue("injectValueId", property, beanInstance)).thenReturn(injectedValue);
        property.inject(context, beanInstance);
        assertEquals(injectedValue, beanInstance.getClass().getDeclaredField(propertyName.getShortName()).get(beanInstance));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueWithNullInjectableValueId() throws IOException {
        String propertyName = "test";
        Object beanInstance = new Object();
        CreatorProperty property = new CreatorProperty(
                new PropertyName(propertyName), JavaType.of(String.class), null, null,
                new Annotations(), null, 0, null, null);
        DeserializationContext context = createMockDeserializationContext();
        property.findInjectableValue(context, beanInstance);
    }

    private AnnotatedParameter createMockAnnotatedParameter() {
        return mock(AnnotatedParameter.class);
    }

    private SettableBeanProperty createMockSettableBeanProperty() {
        return mock(SettableBeanProperty.class);
    }

    private DeserializationContext createMockDeserializationContext() {
        return mock(DeserializationContext.class);
    }

    private JsonParser createMockJsonParser() {
        return mock(JsonParser.class);
    }
}