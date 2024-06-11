package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        try {
            JavaType result = propertyBuilder.findSerializationType(am, false, declaredType);
            fail("Expected IllegalArgumentException");
        } catch (JsonMappingException e) {
            //Expected
        }

        JsonInclude inclusionAnnotation = Mockito.mock(JsonInclude.class);
        when(am.getAnnotation(JsonInclude.class)).thenReturn(inclusionAnnotation);
        when(inclusionAnnotation.value()).thenReturn(JsonInclude.Include.ALWAYS);
        when(declaredType.getRawClass()).thenReturn(String.class);

        JavaType result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(String.class, result.getRawClass());

        when(declaredType.getRawClass()).thenReturn(Integer.class);
        result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(Integer.class, result.getRawClass());

        when(declaredType.getRawClass()).thenReturn(List.class);
        when(am.getGenericType()).thenReturn(new TypeReference<List<Integer>>() {}.getType());
        JavaType contentType = Mockito.mock(JavaType.class);
        when(contentType.getContentType()).thenReturn(IntegerType.getInstance());
        result = propertyBuilder.findSerializationType(am, false, declaredType, null, null, contentType, am, false);
        assertEquals(TypeFactory.collectionType(JavaType.getDefaultInstance(), IntegerType.getInstance()), result);

        JsonSerialize serializationAnnotation = Mockito.mock(JsonSerialize.class);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(serializationAnnotation);
        when(serializationAnnotation.type()).thenReturn(null);
        when(serializationAnnotation.using()).thenReturn(null);

        when(declaredType.getRawClass()).thenReturn(TestBean.class);
        result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(result);

        when(serializationAnnotation.type()).thenReturn(ValueType.class);
        result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(ValueType.class, result.getRawClass());

        when(serializationAnnotation.using()).thenReturn(JsonSerializer.None.class);
        result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(result);

        JsonTypeSerializer typeSerializerMock = Mockito.mock(JsonTypeSerializer.class);
        when(typeSerializerMock.serializeValue(any(), anyInt(), anyBoolean())).thenReturn(new byte[0][0]);
        TypeSerializer typeSerializer = Mockito.spy(typeSerializerMock);

        when(declaredType.getContentType()).thenReturn(Mockito.mock(JavaType.class));
        when(declaredType.getRawClass()).thenReturn(Map.class);
        JavaType keyType = Mockito.mock(JavaType.class);
        when(keyType.getRawClass()).thenReturn(String.class);
        JavaType valueType = Mockito.mock(JavaType.class);
        when(valueType.getRawClass()).thenReturn(Integer.class);
        when(declaredType.withContentTypeHandler(any())).thenReturn(declaredType);

        result = propertyBuilder.findSerializationType(am, false, declaredType, null, null, null, am, false);
        assertEquals(TypeFactory.mapType(keyType, valueType), result);

        when(declaredType.withContentTypeHandler(typeSerializer)).thenReturn(declaredType);
        result = propertyBuilder.findSerializationType(am, false, declaredType, null, null, null, am, false);
        assertEquals(TypeFactory.mapType(keyType, valueType.withContentTypeHandler(typeSerializer)), result);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider prov = mapper.getSerializerProvider();
        ObjectReader reader = mapper.readerForClass(TestBean.class);
        TestBean defaultBean = reader.readValue(new ObjectWriter(mapper).writeValueAsString(new TestBean()));

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        String name = "testName";
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        JsonInclude jsonIncludeAnnotation = Mockito.mock(JsonInclude.class);
        when(am.getAnnotation(JsonInclude.class)).thenReturn(jsonIncludeAnnotation);
        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_EMPTY);

        BeanPropertyWriter result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(result);

        when(am.getName()).thenReturn(name);
        when(declaredType.getRawClass()).thenReturn(String.class);
        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_DEFAULT);

        result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(result);

        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_EMPTY);
        when(ser.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(false);
        result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(result);

        result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, true);
        assertNotNull(result);

        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_EMPTY);
        when(ser.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(true);
        result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(result);

        when(declaredType.getRawClass()).thenReturn(List.class);
        when(am.getGenericType()).thenReturn(new TypeReference<List<Integer>>() {}.getType());
        result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(result);

        when(am.getGenericType()).thenReturn(new TypeReference<List<String>>() {}.getType());
        when(ser.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(false);
        result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(result);

        when(am.getGenericType()).thenReturn(new TypeReference<List<String>>() {}.getType());
        when(ser.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(true);
        result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(result);

        when(declaredType.getRawClass()).thenReturn(Map.class);
        when(am.getGenericType()).thenReturn(new TypeReference<Map<String, Integer>>() {}.getType());
        result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(result);

        when(am.getGenericType()).thenReturn(new TypeReference<Map<String, String>>() {}.getType());
        result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(result);

        when(declaredType.getRawClass()).thenReturn(TestBean.class);
        when(am.getName()).thenReturn("testName");
        when(ser.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);
        Object propDefaultValue = new TestBean();
        when(am.getValue(defaultBean)).thenReturn(propDefaultValue);
        result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(TestBean.class, result.getType());
        assertEquals((BeanPropertyWriter) result, result.getValueWriter());
        assertEquals(ser, result.getSerializer());
        assertEquals(typeSer, result.getTypeSerializer());
        assertEquals(contentTypeSer, result.getContentSerializer());
        assertEquals(propDefaultValue, result.getDefaultValue());
    }

    @Test
    public void testgetDefaultBean() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(TestBean.class);
        TestBean defaultBean = reader.readValue(new ObjectWriter(mapper).writeValueAsString(new TestBean()));

        assertEquals(null, propertyBuilder.getDefaultBean());

        PropertyBuilder propertyBuilder2 = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        when(beanDesc.instantiateBean(_config.canOverrideAccessModifiers())).thenReturn(defaultBean);
        assertEquals(defaultBean, propertyBuilder2.getDefaultBean());

        when(beanDesc.instantiateBean(_config.canOverrideAccessModifiers())).thenThrow(new InstantiationException());
        PropertyBuilder propertyBuilder3 = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        assertEquals(null, propertyBuilder3.getDefaultBean());

        PropertyBuilder propertyBuilder4 = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        when(beanDesc.instantiateBean(_config.canOverrideAccessModifiers())).thenThrow(new SecurityException());
        assertEquals(null, propertyBuilder4.getDefaultBean());
    }

    @Test
    public void testgetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(TestBean.class);
        TestBean defaultBean = reader.readValue(new ObjectWriter(mapper).writeValueAsString(new TestBean()));

        assertNull(propertyBuilder.getPropertyDefaultValue("testName", null, null));

        when(beanDesc.getClassInfo().getAnnotated()).thenReturn(TestBean.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getName()).thenReturn("testName");
        JavaType javaType = Mockito.mock(JavaType.class);
        when(javaType.getRawClass()).thenReturn(TestBean.class);
        when(am.getValue(defaultBean)).thenReturn(new TestBean());

        assertEquals(new TestBean(), propertyBuilder.getPropertyDefaultValue("testName", am, javaType));

        when(am.getValue(defaultBean)).thenThrow(new InstantiationException());
        assertEquals(null, propertyBuilder.getPropertyDefaultValue("testName", am, javaType));

        when(am.getValue(defaultBean)).thenThrow(new SecurityException());
        assertEquals(null, propertyBuilder.getPropertyDefaultValue("testName", am, javaType));

        when(am.getValue(defaultBean)).thenReturn(null);
        assertEquals(null, propertyBuilder.getPropertyDefaultValue("testName", am, javaType));
    }

    @Test
    public void testgetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), null);

        assertEquals(null, propertyBuilder.getDefaultValue(null));

        assertEquals("", propertyBuilder.getDefaultValue(String.class));

        assertEquals(new int[0], propertyBuilder.getDefaultValue(int[].class));

        assertEquals(new String[0], propertyBuilder.getDefaultValue(String[][].class));

        assertEquals(new Map.Entry[0], propertyBuilder.getDefaultValue(Map.Entry[].class));

        assertEquals(new TestBean(), propertyBuilder.getDefaultValue(TestBean.class));

        assertEquals(null, propertyBuilder.getDefaultValue(List.class));
    }
}