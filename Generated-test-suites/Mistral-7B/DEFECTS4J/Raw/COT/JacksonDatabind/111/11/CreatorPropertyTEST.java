package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * Test class for CreatorProperty
 */
public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        assertNotNull(property);
    }

    @Test
    public void testCopyConstructor() {
        CreatorProperty srcProperty = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        CreatorProperty destProperty = new CreatorProperty(srcProperty, null);
        assertEquals(srcProperty, destProperty);
    }

    @Test
    public void testWithName() {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        CreatorProperty newProperty = property.withName(PropertyName.construct("newName"));
        assertNotSame(property, newProperty);
        assertEquals("newName", newProperty.getName().asString());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        CreatorProperty newProperty = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new StandardNullValueProvider();
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        CreatorProperty newProperty = property.withNullProvider(nva);
        assertEquals(nva, newProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        property.fixAccess(null);
        assertNotNull(property._fallbackSetter);
    }

    @Test
    public void testSetFallbackSetter() {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        SettableBeanProperty fallbackSetter = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        property.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, property._fallbackSetter);
    }

    @Test(expected = IOException.class)
    public void testMissingSetter() throws IOException {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        property.set(null, null);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetterWithContext() throws IOException {
        JsonParser parser = null; // No need to initialize it
        DeserializationContext ctxt = new MiniDeserializationContext();
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        property.set(parser, ctxt, null);
    }

    @Test
    public void testFindInjectableValue() {
        Object injectableValueId = new Object();
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        property._injectableValueId = injectableValueId;

        DeserializationContext context = new MiniDeserializationContext();
        Object beanInstance = new Object();
        Object result = property.findInjectableValue(context, beanInstance);
        assertEquals(injectableValueId, result);
    }

    @Test
    public void testInject() throws IOException {
        Object injectableValue = new Object();
        Object beanInstance = new Object();
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        property._injectableValueId = null;
        property._fallbackSetter = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);

        property.inject(new MiniDeserializationContext(), beanInstance);
        assertEquals(injectableValue, property._fallbackSetter.get(beanInstance));
    }

    @JsonCreator
    @JsonDeserialize(using = CreatorPropertyDeserializer.class)
    static class TestBean {
        private String name;

        @JsonProperty
        public String getName() {
            return name;
        }

        @JsonProperty
        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void testCreateInstance() throws IOException {
        JsonParser parser = new MiniCborFactory().getParser();
        DeserializationContext context = new MiniDeserializationContext();
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(parser, TestBean.class);
        assertEquals("name", bean.getName());
    }

    @Test(expected = IOException.class)
    public void testInvalidDefinition() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().getParser();
        DeserializationContext context = new MiniDeserializationContext();
        context.forDeserializationProbing(TestBean.class);
        Object beanInstance = new Object();
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null);
        property.inject(context, beanInstance);
    }
}

class CreatorPropertyDeserializer extends JsonDeserializer<CreatorProperty> {

    @Override
    public CreatorProperty deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        Object injectableValueId = p.getValueAsToken().getValueAsInt();
        PropertyName name = PropertyName.construct("name");
        JavaType type = JavaType.construct(String.class);
        int creatorIndex = 0;
        Annotations contextAnnotations = new StandardAnnotationInstance();
        AnnotatedParameter param = new AnnotatedParameter(null, name.get(), String.class, null);
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, -1,
                null, null);
        property._injectableValueId = injectableValueId;
        return property;
    }
}

class MiniDeserializationContext extends DeserializationContextBase {

    @Override
    public Object findInjectableValue(ObjectId id, BeanProperty property, Object bean)
            throws JsonMappingException {
        return null;
    }

    @Override
    public Object instanceCreationHandler() {
        return null;
    }

    @Override
    public TypeDeserializer findValueDeserializer(BeanProperty property) {
        return null;
    }

    @Override
    public Object findValueHandler(DeserializationContext context, BeanProperty property)
            throws IOException {
        return null;
    }
}

class MiniCborFactory extends JsonFactory {

    @Override
    public JsonParser createParser() {
        return null;
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, ObjectFactory of) {
        return null;
    }
}