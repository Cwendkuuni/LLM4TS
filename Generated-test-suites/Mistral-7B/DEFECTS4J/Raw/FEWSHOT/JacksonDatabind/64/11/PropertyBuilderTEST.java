package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.std.BeanPropertyWriterStd;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private ObjectMapper _mapper;
    private PropertyBuilder _propertyBuilder;

    @Before
    public void setup() throws Exception {
        _config = new SerializationConfig();
        _mapper = new ObjectMapper(_config);
        _propertyBuilder = new PropertyBuilder(_config, _mapper.findPropertyDefinition("testBean"));
    }

    @Test
    public void testBuildWriter_NullSer() throws JsonMappingException {
        BeanPropertyWriter expected = new BeanPropertyWriter(null, null, null, null, null, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NullTypeSer() throws JsonMappingException {
        BeanPropertyWriter expected = new BeanPropertyWriter(null, null, null, null, null, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindSerializationType_NoAnnotations() throws JsonMappingException {
        Assert.assertNull(_propertyBuilder.findSerializationType(null, false, null));
    }

    @Test
    public void testFindSerializationType_Annotations() throws JsonMappingException, ClassNotFoundException {
        Class<?> clazz = Class.forName("com.fasterxml.jackson.databind.ser.TestSerializationTypeAnnotation");
        Annotated a = AnnotatedClass.construct(clazz, null);
        JavaType expected = TypeFactory.defaultInstance().constructType(clazz);
        JavaType actual = _propertyBuilder.findSerializationType(a, false, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindSerializationType_Annotations_Subtype() throws JsonMappingException, ClassNotFoundException {
        Class<?> clazz = Class.forName("com.fasterxml.jackson.databind.ser.TestSerializationTypeAnnotationSubtype");
        Annotated a = AnnotatedClass.construct(clazz, null);
        JavaType expected = TypeFactory.defaultInstance().constructType(clazz);
        JavaType actual = _propertyBuilder.findSerializationType(a, false, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindSerializationType_StaticTyping() throws JsonMappingException {
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType expected = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType actual = _propertyBuilder.findSerializationType(null, true, declaredType);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testgetDefaultBean() throws JsonMappingException {
        Object defaultBean = _propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);
    }

    @Test
    public void testgetPropertyDefaultValue() throws JsonMappingException {
        Class<?> clazz = TestBean.class;
        String name = "name";
        AnnotatedMember member = AnnotatedClassMember.construct(clazz, name);
        JavaType type = TypeFactory.defaultInstance().constructType(clazz);
        Object defaultBean = _propertyBuilder.getDefaultBean();
        Object defaultValue = _propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals("", defaultValue);
    }

    @Test
    public void testgetPropertyDefaultValue_Null() throws JsonMappingException {
        Class<?> clazz = TestBean.class;
        String name = "name";
        AnnotatedMember member = null;
        JavaType type = TypeFactory.defaultInstance().constructType(clazz);
        Object defaultBean = _propertyBuilder.getDefaultBean();
        Object defaultValue = _propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertNull(defaultValue);
    }

    @Test
    public void testgetPropertyDefaultValue_CustomDefaultValue() throws JsonMappingException {
        Class<?> clazz = TestBeanCustomDefaultValue.class;
        String name = "customInt";
        AnnotatedMember member = AnnotatedClassMember.construct(clazz, name);
        JavaType type = TypeFactory.defaultInstance().constructType(clazz);
        Object defaultBean = _propertyBuilder.getDefaultBean();
        Object defaultValue = _propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals(42, defaultValue.intValue());
    }

    @Test
    public void testgetDefaultValue() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(String[].class);
        Object defaultValue = _propertyBuilder.getDefaultValue(type);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testBuildWriter_NullDeclaredType() throws JsonMappingException {
        BeanPropertyWriter expected = new BeanPropertyWriter(null, null, null, null, null, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_EmptyType() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        BeanPropertyWriter expected = new BeanPropertyWriter(null, null, null, null, null, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, type, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NonEmptyType() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(TestBeanNonEmpty.class);
        BeanPropertyWriter expected = BeanPropertyWriterStd.std(type, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, type, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NullPropDef() throws JsonMappingException {
        BeanPropertyWriter expected = new BeanPropertyWriter(null, null, null, null, null, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NonSerializableType() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        BeanPropertyWriter expected = new BeanPropertyWriter(null, null, null, null, null, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, type, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NullContentTypeSer() throws JsonMappingException {
        JavaType declaredType = TypeFactory.defaultInstance().constructType(TestBeanNonEmpty.class);
        BeanPropertyWriter expected = BeanPropertyWriterStd.std(declaredType, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, declaredType, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_EmptyContentType() throws JsonMappingException {
        JavaType declaredType = TypeFactory.defaultInstance().constructType(TestBeanEmptyContentType.class);
        BeanPropertyWriter expected = BeanPropertyWriterStd.std(declaredType, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, declaredType, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NonEmptyContentType() throws JsonMappingException {
        JavaType declaredType = TypeFactory.defaultInstance().constructType(TestBeanNonEmpty.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(TestBeanContentType.class);
        BeanPropertyWriter expected = BeanPropertyWriterStd.std(declaredType, null, null, false, contentType);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, declaredType, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NullAnnotationIntrospector() throws JsonMappingException {
        BeanPropertyWriter expected = new BeanPropertyWriter(null, null, null, null, null, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NullDefaultBean() throws JsonMappingException {
        BeanPropertyWriter expected = new BeanPropertyWriter(null, null, null, null, null, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NullDefaultInclusion() throws JsonMappingException {
        BeanPropertyWriter expected = new BeanPropertyWriter(null, null, null, null, null, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NullUseRealPropertyDefaults() throws JsonMappingException {
        BeanPropertyWriter expected = new BeanPropertyWriter(null, null, null, null, null, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NonDefaultBean() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, _mapper.findPropertyDefinition("testBean"));
        TestBean testBean = new TestBean();
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object actualValue = propertyBuilder.getPropertyDefaultValue("name", null, TypeFactory.defaultInstance().constructType(TestBean.class));
        Assert.assertNotSame(testBean, defaultBean);
        BeanPropertyWriter expected = _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false, defaultBean);
        Assert.assertNotSame(expected, actual);
    }

    @Test
    public void testBuildWriter_NullNameTransformer() throws JsonMappingException {
        JavaType declaredType = TypeFactory.defaultInstance().constructType(TestBeanNonEmpty.class);
        BeanPropertyWriter expected = BeanPropertyWriterStd.std(declaredType, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, declaredType, null, null, null, null, false, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_NullNullSerializer() throws JsonMappingException {
        JavaType declaredType = TypeFactory.defaultInstance().constructType(TestBeanNonEmpty.class);
        BeanPropertyWriter expected = BeanPropertyWriterStd.std(declaredType, null, null, false, null);
        BeanPropertyWriter actual = _propertyBuilder.buildWriter(null, null, declaredType, null, null, null, null, false, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testThrowWrappedException() throws Exception {
        Exception e = new Exception("TestException");
        String propName = "testProperty";
        Object defaultBean = new Object();
        Throwable t = _propertyBuilder._throwWrapped(e, propName, defaultBean);
        Assert.assertEquals(e, t);
    }

    public static class TestBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TestBeanCustomDefaultValue {
        private int customInt;

        public int getCustomInt() {
            return customInt;
        }

        public void setCustomInt(int customInt) {
            this.customInt = customInt;
        }
    }

    public static class TestBeanNonEmpty {
    }

    public static class TestBeanEmptyContentType {
    }

    public static class TestBeanContentType {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class TestSerializationTypeAnnotation {
        @JsonSerialize(using = TestSerializationTypeAnnotationSerializer.class)
        private int testInt;
    }

    public static class TestSerializationTypeAnnotationSubtype extends TestSerializationTypeAnnotation {
    }
}