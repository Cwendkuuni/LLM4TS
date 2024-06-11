package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanDescription;
import com.fasterxml.jackson.databind.util.ArrayBuilders;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuildWriter() {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        try {
            BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer,
                    contentTypeSer, am, defaultUseStaticTyping);
            assertNotNull(bpw);
        } catch (JsonMappingException e) {
            assertNull(e);
        }
    }

    @Test
    public void testFindSerializationType() {
        try {
            Annotated a = new Annotated();
            boolean useStaticTyping = true;
            JavaType declaredType = new JavaType();
            JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            assertNull(e);
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        try {
            String name = "property";
            AnnotatedMember member = new AnnotatedMember();
            JavaType type = new JavaType();

            Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
            assertNull(defaultValue);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = new JavaType(Object.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNotNull(defaultValue);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    private static class SerializerProvider {

        public JsonSerializer<?> serializerInstance(AnnotatedMember am, Object serDef) {
            return new JsonSerializer<>();
        }
    }

    private static class BeanPropertyDefinition {

    }

    private static class JavaType {

        public Class<?> getRawClass() {
            return Object.class;
        }

        public boolean isContainerType() {
            return false;
        }

        public boolean isReferenceType() {
            return false;
        }

        public JavaType getContentType() {
            return null;
        }

        public JavaType withContentTypeHandler(TypeSerializer contentTypeSer) {
            return this;
        }

        public JavaType withStaticTyping() {
            return this;
        }
    }

    private static class JsonSerializer<T> {

    }

    private static class TypeSerializer {

    }

    private static class AnnotatedMember {

        public Object getValue(Object defaultBean) {
            return null;
        }
    }

    private static class BeanPropertyWriter {

        public static final Object MARKER_FOR_EMPTY = new Object();

        public BeanPropertyWriter(BeanPropertyDefinition propDef, AnnotatedMember am, Annotations classAnnotations,
                JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serializationType,
                boolean suppressNulls, Object valueToSuppress) {

        }

        public void assignNullSerializer(JsonSerializer<?> serDef) {

        }

        public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper) {
            return this;
        }
    }

    private static class ClassUtil {

        public static Class<?> primitiveType(Class<?> cls) {
            return null;
        }

        public static Object defaultValue(Class<?> cls) {
            if (cls == int.class || cls == Integer.class) {
                return 0;
            }
            return null;
        }
    }

    private static class Annotations {

    }

    private static class BeanDescription {

        public Object instantiateBean(boolean canOverrideAccessModifiers) {
            return null;
        }

        public Annotations getClassAnnotations() {
            return new Annotations();
        }
    }

    private static class Annotated {

        public String getName() {
            return "annotated";
        }
    }
}