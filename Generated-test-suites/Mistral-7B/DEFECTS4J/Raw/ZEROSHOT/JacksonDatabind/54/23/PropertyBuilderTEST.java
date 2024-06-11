package com.yourpackagename.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.test.BaseTest;

public class PropertyBuilderTest extends BaseTest {

    private PropertyBuilder propertyBuilder;
    private AnnotatedMember member;
    private BeanDescription beanDescription;
    private SerializationConfig config;

    @Before
    public void setUp() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "bar");
        ObjectReader reader = mapper.reader(node.getClass());
        Object bean = reader.readValueAs(YourBean.class);

        config = mapper.getSerializationConfig();
        beanDescription = mapper.constructType(YourBean.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);

        JavaType declaredType = mapper.typeFactory().constructType(YourBean.class);
        member = new SimpleBeanPropertyDefinition("foo", beanDescription.findPropertyDefinition("foo"));
    }

    @Test
    public void testBuildWriter_withDefaultValues() throws IOException {
        JsonSerializer<Object> nullSerializer = mapper.serializerFactory().findNullSerializer(null, null);
        TypeSerializer contentTypeSerializer = mapper.serializers().findContentSerializer(TypeFactory.defaultInstance(), null);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializationProvider(), member, declaredType, nullSerializer, null, contentTypeSerializer, member, false);

        assertNotNull(beanPropertyWriter);
        assertEquals("foo", beanPropertyWriter.getName());
    }

    @Test
    public void testBuildWriter_withSerializationType() throws IOException {
        JsonSerializer<Object> nullSerializer = mapper.serializerFactory().findNullSerializer(null, null);
        TypeSerializer contentTypeSerializer = mapper.serializers().findContentSerializer(YourOtherBean.class, null);

        JavaType serializationType = mapper.typeFactory().constructType(YourOtherBean.class);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializationProvider(), member, declaredType, nullSerializer, null, contentTypeSerializer, member, false);

        assertEquals(serializationType, beanPropertyWriter.getTypeSerializer());
    }

    @Test
    public void testBuildWriter_withNullSer() throws IOException {
        JsonSerializer<Object> nullSerializer = null;
        TypeSerializer contentTypeSerializer = mapper.serializers().findContentSerializer(TypeFactory.defaultInstance(), null);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializationProvider(), member, declaredType, nullSerializer, null, contentTypeSerializer, member, false);

        assertNotNull(beanPropertyWriter);
        assertNull(beanPropertyWriter.getNullSerializer());
    }

    @Test
    public void testBuildWriter_withUnwrappingNameTransformer() throws IOException {
        JsonSerializer<Object> nullSerializer = mapper.serializerFactory().findNullSerializer(null, null);
        NameTransformer unwrapper = mapper.annotationIntrospector().findUnwrappingNameTransformer(member);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializationProvider(), member, declaredType, nullSerializer, null, null, member, false);

        BeanPropertyWriter unwrappingWriter = beanPropertyWriter.unwrappingWriter(unwrapper);

        assertNotNull(unwrappingWriter);
        assertEquals("foo", unwrappingWriter.getName());
    }

    @Test
    public void testFindSerializationType() throws IOException {
        Annotated a = BeanDescription.StandardDescForIntegrationTesting.forClass(YourBean.class).findProperty("foo");

        JavaType expectedType = mapper.typeFactory().constructType(YourOtherBean.class);
        JavaType actualType = propertyBuilder.findSerializationType(a, false, declaredType);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindSerializationType_withStaticTyping() throws IOException {
        Annotated a = BeanDescription.StandardDescForIntegrationTesting.forClass(YourBean.class).findProperty("foo");

        propertyBuilder.useStaticTyping(true);

        JavaType expectedType = mapper.typeFactory().constructType(YourOtherBean.class);
        JavaType actualType = propertyBuilder.findSerializationType(a, false, declaredType);

        assertEquals(expectedType, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_withInvalidType() throws IOException {
        Annotated a = BeanDescription.StandardDescForIntegrationTesting.forClass(YourBean.class).findProperty("foo");

        JavaType expectedType = mapper.typeFactory().constructType(Object.class);
        propertyBuilder.findSerializationType(a, false, declaredType);
    }

    @Test
    public void testGetClassAnnotations() {
        assertEquals(YourBean.class.getAnnotationClasses(), propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testGetDefaultBean() throws IOException {
        Object defaultBean = propertyBuilder.getDefaultBean();

        assertTrue(defaultBean instanceof YourBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws IOException {
        Object defaultBean = propertyBuilder.getDefaultBean();
        String defaultValue = (String) propertyBuilder.getPropertyDefaultValue("foo", member, declaredType);

        assertEquals("", defaultValue);

        propertyBuilder = new PropertyBuilder(config, beanDescription);
        propertyBuilder._defaultBean = defaultBean;

        String propertyDefaultValue = (String) propertyBuilder.getPropertyDefaultValue("foo", member, declaredType);

        assertEquals("bar", propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_withNullDefaultBean() {
        propertyBuilder._defaultBean = null;

        String propertyDefaultValue = (String) propertyBuilder.getPropertyDefaultValue("foo", member, declaredType);

        assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() throws IOException {
        Object defaultValue = propertyBuilder.getDefaultValue(declaredType);

        if (declaredType.isReferenceType()) {
            assertTrue(defaultValue instanceof YourOtherBean);
        } else if (declaredType.isContainerType()) {
            assertTrue(defaultValue instanceof JsonInclude.Include);
            assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
        } else if (declaredType.isPrimitiveBoolean()) {
            assertTrue(defaultValue instanceof Boolean);
            assertEquals(false, defaultValue);
        }
    }

    @Test
    public void testGetDefaultValue_withNullType() {
        Object defaultValue = propertyBuilder.getDefaultValue(null);

        assertNull(defaultValue);
    }

    @Test
    public void test_throwWrapped() throws IOException {
        Exception e = new IOException("Test Exception");
        String propName = "foo";
        Object defaultBean = propertyBuilder.getDefaultBean();

        Object thrownObject = propertyBuilder._throwWrapped(e, propName, defaultBean);

        assertEquals("Failed to get property 'foo' of default YourBean instance", thrownObject.getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_throwWrapped_withNullPropName() throws IOException {
        Exception e = new IOException("Test Exception");
        Object defaultBean = propertyBuilder.getDefaultBean();

        propertyBuilder._throwWrapped(e, null, defaultBean);
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonSerialize(using = YourSerializer.class)
    static class YourBean {
        String foo;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class YourOtherBean {

    }

    static class YourSerializer extends StdSerializer<YourBean> {
        public YourSerializer() {
            this(null);
        }

        public YourSerializer(Class<YourBean> t, JsonSerializer.Typing typing) {
            super(t, typing);
        }

        @Override
        public void serialize(YourBean bean, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString("YourBeanValue");
        }
    }
}