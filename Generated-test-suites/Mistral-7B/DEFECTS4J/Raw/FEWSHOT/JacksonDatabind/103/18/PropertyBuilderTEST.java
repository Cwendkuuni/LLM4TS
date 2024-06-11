package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

    private AnnotationIntrospector _annotationIntrospector;
    private Object _defaultBean;

    @Before
    public void setUp() throws Exception {
        _annotationIntrospector = new AnnotationIntrospector();
        _defaultBean = new PropertyBuilderTestBean();
    }

    @Test
    public void testFindSerializationType_AnnotatedWithJsonSerialize() throws JsonMappingException {
        JavaType declaredType = TypeFactory.type(getClass(), PropertyBuilder.class);
        AnnotatedMember am = PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "buildWriter");

        JsonSerialize jsonSerializeAnnotation = am.getAnnotation(JsonSerialize.class);
        JavaType serializationType = new PropertyBuilder(_config, _beanDesc).findSerializationType(am, false, declaredType);
        assertEquals(JsonSerialize.Typing.WITH_TYPE, serializationType.getSerializationType());
    }

    @Test
    public void testFindSerializationType_NoJsonSerializeAnnotation() throws JsonMappingException {
        JavaType declaredType = TypeFactory.type(getClass(), PropertyBuilder.class);
        AnnotatedMember am = PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "getDefaultBean");

        JavaType serializationType = new PropertyBuilder(_config, _beanDesc).findSerializationType(am, false, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testBuildWriter_DefaultValueIsUsed() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = PropertyTestUtil.beanPropertyDefinition(PropertyBuilder.class, "useRealPropertyDefaults");
        JavaType declaredType = TypeFactory.type(getPropertyClass(), GetterSetter.class);
        JsonSerializer<?> ser = PropertyTestUtil.jsonSerializer(getPropertyClass(), GetterSetter.class);
        TypeSerializer typeSer = TypeSerializers.empty();
        TypeSerializer contentTypeSer = TypeSerializers.empty();
        AnnotatedMember am = PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "buildWriter");

        Object valueToSuppress = new PropertyBuilder(_config, _beanDesc).buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false).getValueToSuppress();
        assertNull(valueToSuppress);
    }

    @Test
    public void testBuildWriter_NoDefaultValueIsUsed() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = PropertyTestUtil.beanPropertyDefinition(PropertyBuilder.class, "useRealPropertyDefaults");
        JavaType declaredType = TypeFactory.type(getPropertyClass(), GetterSetter.class);
        JsonSerializer<?> ser = PropertyTestUtil.jsonSerializer(getPropertyClass(), GetterSetter.class);
        TypeSerializer typeSer = TypeSerializers.empty();
        TypeSerializer contentTypeSer = TypeSerializers.empty();
        AnnotatedMember am = PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "buildWriter");

        Object valueToSuppress = new PropertyBuilder(_config, _beanDesc).buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false).getValueToSuppress();
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, valueToSuppress);
    }

    @Test
    public void testFindSerializationType_NoJsonSerializeTypeAnnotation() throws JsonMappingException {
        JavaType declaredType = TypeFactory.type(getClass(), PropertyBuilder.class);
        AnnotatedMember am = PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "buildWriter");

        JavaType serializationType = new PropertyBuilder(_config, _beanDesc).findSerializationType(am, false, declaredType);
        assertNull(serializationType);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationType_IllegalConcreteTypeAnnotation() throws JsonMappingException {
        JavaType declaredType = TypeFactory.type(getClass(), PropertyBuilder.class);
        AnnotatedMember am = PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "buildWriter");

        JavaType serializationType = new PropertyBuilder(_config, _beanDesc).findSerializationType(am, false, declaredType);
        if (!declaredType.getRawClass().isAssignableFrom(serializationType.getRawClass())) {
            throw new JsonMappingException("Illegal concrete-type annotation for method 'buildWriter': class " + serializationType.getRawClass().getName() + " not a super-type of (declared) class " + declaredType.getRawClass().getName());
        }
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertEquals(_defaultBean, propertyBuilder.getDefaultBean());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetDefaultBean_NoDefaultConstructor() throws JsonMappingException, InstantiationException, IllegalAccessException, InvocationTargetException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Method instantiateBeanMethod = PropertyBuilder.class.getDeclaredMethod("instantiateBean", boolean.class);
        instantiateBeanMethod.setAccessible(true);
        instantiateBeanMethod.invoke(propertyBuilder, false);
        throw new JsonMappingException("Class " + _beanDesc.getClassInfo().getAnnotated().getName() + " has no default constructor; cannot instantiate default bean value to support 'properties=JsonSerialize.Inclusion.NON_DEFAULT' annotation");
    }

    @Test
    public void testGetPropertyDefaultValue_Getter() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Method getMethod = PropertyBuilderTestBean.class.getDeclaredMethod("getProperty");
        getMethod.setAccessible(true);
        Object defaultValue = getMethod.invoke(defaultBean);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("getProperty", PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "getPropertyDefaultValue"), TypeFactory.type(getPropertyClass(), PropertyBuilderTestBean.class));
        assertEquals(defaultValue, propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_Field() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Field field = PropertyBuilderTestBean.class.getDeclaredField("propertyField");
        field.setAccessible(true);
        Object defaultValue = field.get(defaultBean);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("propertyField", PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "getPropertyDefaultValue"), TypeFactory.type(getPropertyClass(), PropertyBuilderTestBean.class));
        assertEquals(defaultValue, propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_NoDefaultValue() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("nonExistentProperty", PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "getPropertyDefaultValue"), TypeFactory.type(getPropertyClass(), PropertyBuilderTestBean.class));
        assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_NullDefaultValue() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("nullProperty", PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "getPropertyDefaultValue"), TypeFactory.type(getPropertyClass(), PropertyBuilderTestBean.class));
        assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_JsonIncludeNonEmpty() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = BeanUtil.getDefaultValue(TypeFactory.type(getPropertyClass(), PropertyBuilderTestBean.class));

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("nonEmptyProperty", PropertyTestUtil.getAnnotatedMember(PropertyBuilder.class, "getPropertyDefaultValue"), TypeFactory.type(getPropertyClass(), PropertyBuilderTestBean.class));
        assertEquals(defaultValue, propertyDefaultValue);
    }

    @After
    public void tearDown() throws Exception {
        if (_defaultBean != null) {
            _beanDesc.getClassInfo().getAnnotated().getDeclaredField("_defaultBean").set(null);
        }
    }

    private static class PropertyBuilderTestBean {
        private String getProperty() {
            return "propertyValue";
        }

        private String propertyField;

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private String nonEmptyProperty = "nonEmptyValue";

        private String nullProperty;

        private String getterProperty;

        private String fieldProperty;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String nonEmptyGetterProperty;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String nonEmptyFieldProperty;

        public PropertyBuilderTestBean() {
            this.propertyField = "propertyValue";
            this.nonEmptyProperty = "nonEmptyValue";
            this.nullProperty = null;
            this.getterProperty = PropertyTestUtil.getValueFromAnnotation(PropertyBuilderTestBean.class, "getterProperty", JsonInclude.class);
            this.fieldProperty = PropertyTestUtil.getValueFromAnnotation(PropertyBuilderTestBean.class, "fieldProperty", JsonInclude.class);
            this.nonEmptyGetterProperty = "nonEmptyValue";
            this.nonEmptyFieldProperty = "nonEmptyValue";
        }
    }

    private static class GetterSetter {
    }

    private static class PropertyTestUtil {
        public static Class<?> getPropertyClass() {
            return PropertyBuilderTestBean.class;
        }

        public static BeanPropertyDefinition beanPropertyDefinition(Class<?> clazz, String propertyName) throws JsonMappingException {
            BeanDescription beanDesc = new BeanDescription(clazz);
            BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition(propertyName);
            return propDef;
        }

        public static AnnotatedMember getAnnotatedMember(Class<?> clazz, String methodName) throws JsonMappingException {
            AnnotatedMember am = _annotationIntrospector.findPropertyOrGetterAnnotation(_config, clazz, methodName);
            return am;
        }

        public static AnnotatedMember getAnnotatedMember(Class<?> clazz, String fieldName) throws JsonMappingException {
            AnnotatedMember am = _annotationIntrospector.findFieldAnnotation(_config, clazz, fieldName);
            return am;
        }

        public static JsonSerializer<?> jsonSerializer(Class<?> clazz, Class<?> genericType) throws JsonMappingException {
            ObjectWriter writer = new JsonFactory().createWriterForTreeModel(new ObjectMapper().readValue(new FasterXmlMapperTest().getClass().getResource("/test-each.json"), clazz));
            ObjectReader reader = new JsonFactory().createObjectReader(writer.getInputSource());
            return reader.findValueSerializer(clazz, genericType);
        }
    }
}

class FasterXmlMapperTest {
}