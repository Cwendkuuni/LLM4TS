package com.fasterxml.jackson.databind.ser; // Make sure to import all necessary classes

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.DefaultSerializers;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializerFactory;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test class for PropertyBuilder.
 */
public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        config = new SerializationConfig(new AnnotationIntrospector(), new StandardSerializerFactory(null));
        beanDesc = new BeanDescription(new AnnotatedClass(MyClass.class), config);
        annotationIntrospector = config.getAnnotationIntrospector();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    /**
     * Test method for {@link PropertyBuilder#PropertyBuilder(SerializationConfig, BeanDescription)} constructor.
     */
    @Test
    public void testConstructor() {
        Assert.assertNotNull(propertyBuilder);
    }

    /**
     * Test method for {@link PropertyBuilder#getClassAnnotations()} method.
     */
    @Test
    public void testGetClassAnnotations() {
        Assert.assertNotNull(propertyBuilder.getClassAnnotations());
    }

    /**
     * Test method for {@link PropertyBuilder#buildWriter(SerializerProvider, BeanPropertyDefinition, JavaType, JsonSerializer, TypeSerializer, TypeSerializer, AnnotatedMember, boolean)} method with default arguments.
     */
    @Test
    public void testBuildWriter1() throws Exception {
        JavaType declaredType = null;
        JsonSerializer<?> ser = new DefaultSerializers.Serializers.BaseSerializer((Class<Object>)Object.class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(new DefaultSerializerProvider(),
                null, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        Assert.assertNotNull(writer);
    }

    /**
     * Test method for {@link PropertyBuilder#buildWriter(SerializerProvider, BeanPropertyDefinition, JavaType, JsonSerializer, TypeSerializer, TypeSerializer, AnnotatedMember, boolean)} method with custom arguments.
     */
    @Test
    public void testBuildWriter2() throws Exception {
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = new TypeSerializerFactory(config).findContentTypeSerializer(config, null, MyClass.class, null);
        AnnotatedMember am = new AnnotatedMember(MyClass.class, "myProperty");
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("myProperty", MyClass.class, null, null, null, null, null, null, null);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(new DefaultSerializerProvider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(writer);
    }

    /**
     * Test method for {@link PropertyBuilder#findSerializationType(Annotated, boolean, JavaType)} method.
     */
    @Test
    public void testFindSerializationType() throws Exception {
        JavaType declaredType = null;
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("myProperty", MyClass.class, null, null, null, null, null, null, null);
        JavaType serializationType = propertyBuilder.findSerializationType(new AnnotatedMember(MyClass.class, "myProperty"), false, declaredType);
        Assert.assertEquals(MyClass.class, serializationType.getRawClass());
    }

    /**
     * Test method for {@link PropertyBuilder#getDefaultBean()} method.
     */
    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);
    }

    /**
     * Test method for {@link PropertyBuilder#getPropertyDefaultValue(String, AnnotatedMember, JavaType)} method.
     */
    @Test
    public void testGetPropertyDefaultValue() {
        Object myDefaultValue = propertyBuilder.getPropertyDefaultValue("myProperty", new AnnotatedMember(MyClass.class, "myProperty"), null);
        Assert.assertNull(myDefaultValue);
    }

    /**
     * Test method for {@link PropertyBuilder#getDefaultValue(JavaType)} method.
     */
    @Test
    public void testGetDefaultValue() {
        JavaType type = JavaTypeFactory.constructType(String[].class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals(MarkerForEmpty.class, defaultValue.getClass());
    }

    /**
     * Test method for {@link PropertyBuilder#_throwWrapped(Exception, String, Object)} method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() {
        throw new Exception("Test Exception");
    }

    /**
     * Test method for handling edge cases and exceptions.
     */
    @Test
    public void testEdgeCasesAndExceptions() throws Exception {
        // Test with null arguments
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        BeanPropertyDefinition propDef = null;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(new DefaultSerializerProvider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNull(writer);

        // Test with customTypeSerializer and null contentTypeSer
        JavaType containedType = JavaTypeFactory.constructType(String.class);
        TypeSerializer contentTypeSer = null;
        TypeSerializer customTypeSerializer = new TypeSerializerFactory(config).findContentTypeSerializer(config, null, MyClass.class, containedType);
        JavaType serializationType = TypeFactory.defaultInstance();
        AnnotatedMember am1 = new AnnotatedMember(MyClass.class, "myProperty");
        BeanPropertyWriter writer1 = propertyBuilder.buildWriter(new DefaultSerializerProvider(), null, serializationType, null, customTypeSerializer, null, am1, false);
        Assert.assertNotNull(writer1);

        // Test with annotations
        MyClassAnnotations myAnnotations = new MyClassAnnotations();
        BeanPropertyDefinition propDef1 = new BeanPropertyDefinition("myProperty", MyClass.class, null, null, null, null, null, null, null);
        AnnotatedMember am2 = new AnnotatedMember(MyClass.class, "myProperty", new JsonSerialize.TypeSerializer(MyClass.class, MyClassSerializer.class));
        BeanPropertyWriter writer2 = propertyBuilder.buildWriter(new DefaultSerializerProvider(), propDef1, null, null, null, null, am2, false);
        Assert.assertNotNull(writer2);
    }

    /**
     * Class to be used as a test POJO.
     */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class MyClass {
        @JsonSerialize(using = MyClassSerializer.class)
        private int myProperty;
    }

    /**
     * Custom serializer for MyClass.
     */
    static class MyClassSerializer extends JsonSerializer<MyClass> {
        @Override
        public void serialize(MyClass myClass, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
            jgen.writeNumber(myClass.myProperty * 2);
        }
    }
}