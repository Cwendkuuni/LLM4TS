package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import java.lang.reflect.*;

import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object a = new AnnotatedClassWithSerializationType();

        // Test with no annotations
        JavaType declaredType = JavaType.construct(AnnotatedClassWithSerializationType.class, null);
        JavaType resultType = propertyBuilder.findSerializationType(null, false, declaredType);
        assertNull(resultType);

        // Test with annotation
        AnnotatedMember am = new AnnotatedMember(AnnotatedClassWithSerializationType.class.getDeclaredField("fieldWithAnnotation"), null, null);
        resultType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(JavaType.construct(String.class, null), resultType);

        // Test with no annotations but static typing enabled
        resultType = propertyBuilder.findSerializationType(null, true, declaredType);
        assertEquals(declaredType, resultType);

        // Test with annotation and static typing enabled
        resultType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(JavaType.construct(String.class, null), resultType);

        // Test with annotation that is not applicable (different type)
        am = new AnnotatedMember(AnnotatedClassWithSerializationType.class.getDeclaredField("fieldWithWrongAnnotation"), null, null);
        resultType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(resultType);

        // Test with annotation that is not applicable but static typing enabled
        resultType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(declaredType, resultType);
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object beanInstance = new BeanInstance();

        // Test getting default bean
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
        assertEquals(BeanInstance.class, defaultBean.getClass());

        // Test getting null default bean
        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            Object getDefaultBean() {
                return null;
            }
        };
        defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object beanInstance = new BeanInstance();

        // Test getting default value of primitive type
        String propName = "intField";
        AnnotatedMember member = new AnnotatedMember(BeanInstance.class.getDeclaredField(propName), null, null);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, JavaType.construct(int.class, null));
        assertEquals(0, defaultValue);

        // Test getting default value of wrapper type for primitive
        propName = "integerField";
        member = new AnnotatedMember(BeanInstance.class.getDeclaredField(propName), null, null);
        defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, JavaType.construct(Integer.class, null));
        assertEquals(0, defaultValue.intValue());

        // Test getting default value of String
        propName = "stringField";
        member = new AnnotatedMember(BeanInstance.class.getDeclaredField(propName), null, null);
        defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, JavaType.construct(String.class, null));
        assertEquals("", defaultValue);

        // Test getting default value of map
        propName = "mapField";
        member = new AnnotatedMember(BeanInstance.class.getDeclaredField(propName), null, null);
        defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, JavaType.construct(HashMap.class, JavaType.construct(String.class, JavaType.construct(String.class, null))));
        assertTrue(defaultValue instanceof Map);

        // Test getting default value of collection
        propName = "listField";
        member = new AnnotatedMember(BeanInstance.class.getDeclaredField(propName), null, null);
        defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, JavaType.construct(ArrayList.class, JavaType.construct(String.class, null)));
        assertTrue(defaultValue instanceof Collection);

        // Test getting default value of array
        propName = "arrayField";
        member = new AnnotatedMember(BeanInstance.class.getDeclaredField(propName), null, null);
        defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, JavaType.construct(String[].class, null));
        assertArrayEquals(new String[0], (String[]) defaultValue);

        // Test getting default value of reference type
        propName = "beanField";
        member = new AnnotatedMember(BeanInstance.class.getDeclaredField(propName), null, null);
        defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, JavaType.construct(BeanInstance.class, null));
        assertNotNull(defaultValue);
        assertEquals(BeanInstance.class, defaultValue.getClass());

        // Test getting default value of null
        propName = "nullField";
        member = new AnnotatedMember(BeanInstance.class.getDeclaredField(propName), null, null);
        defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, JavaType.construct(String.class, null));
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test getting default value of primitive type
        assertEquals(0, propertyBuilder.getDefaultValue(JavaType.construct(int.class, null)).intValue());

        // Test getting default value of wrapper type for primitive
        assertEquals(0, propertyBuilder.getDefaultValue(JavaType.construct(Integer.class, null)).intValue());

        // Test getting default value of String
        assertEquals("", propertyBuilder.getDefaultValue(JavaType.construct(String.class, null)).toString());

        // Test getting default value of map
        assertTrue(propertyBuilder.getDefaultValue(JavaType.construct(HashMap.class, JavaType.construct(String.class, JavaType.construct(String.class, null)))).isEmpty());

        // Test getting default value of collection
        assertTrue(((Collection) propertyBuilder.getDefaultValue(JavaType.construct(ArrayList.class, JavaType.construct(String.class, null)))).isEmpty());

        // Test getting default value of array
        assertArrayEquals(new String[0], (String[]) propertyBuilder.getDefaultValue(JavaType.construct(String[].class, null)));

        // Test getting default value of reference type
        assertNull(propertyBuilder.getDefaultValue(JavaType.construct(BeanInstance.class, null)));
    }
}

class AnnotatedClassWithSerializationType {
    @JsonSerialize(using = StringSerializer.class)
    private String fieldWithAnnotation;
}

class AnnotatedMember {
    private Annotated annotated;
    private JsonType annotationType;
    private JavaType javaType;

    public AnnotatedMember(Annotated annotated, JsonType jsonType, JavaType javaType) {
        this.annotated = annotated;
        this.annotationType = jsonType;
        this.javaType = javaType;
    }

    public Annotated getAnnotated() {
        return annotated;
    }

    public JsonType getAnnotationType() {
        return annotationType;
    }

    public JavaType getJavaType() {
        return javaType;
    }
}

class Annotated {
}

class BeanInstance {
    private int intField = 0;
    private Integer integerField;
    private String stringField = "";
    private Map<String, String> mapField = new HashMap<>();
    private Collection<String> listField = new ArrayList<>();
    private String[] arrayField;
    private BeanInstance beanField;
    private Object nullField;

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public Integer getIntegerField() {
        return integerField;
    }

    public void setIntegerField(Integer integerField) {
        this.integerField = integerField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public Map<String, String> getMapField() {
        return mapField;
    }

    public void setMapField(Map<String, String> mapField) {
        this.mapField = mapField;
    }

    public Collection<String> getListField() {
        return listField;
    }

    public void setListField(Collection<String> listField) {
        this.listField = listField;
    }

    public String[] getArrayField() {
        return arrayField;
    }

    public void setArrayField(String[] arrayField) {
        this.arrayField = arrayField;
    }

    public BeanInstance getBeanField() {
        return beanField;
    }

    public void setBeanField(BeanInstance beanField) {
        this.beanField = beanField;
    }

    public Object getNullField() {
        return nullField;
    }

    public void setNullField(Object nullField) {
        this.nullField = nullField;
    }
}

class BeanDescription {
    private BeanDescription() {
        // Empty constructor
    }

    public BeanDescription instantiateBean(boolean canOverrideAccessModifiers) throws InstantiationException, IllegalAccessException {
        BeanInstance beanInstance = new BeanInstance();
        return new BeanDescription();
    }
}