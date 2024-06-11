package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

public class PropertyBuilderTest {

    private ObjectMap<String, Object> defaultInstances;
    private PropertyBuilder propertyBuilder;
    private BeanDescription beanDescription;
    private SerializationConfig serializationConfig;

    @Before
    public void setUp() throws Exception {
        serializationConfig = new SerializationConfig();
        serializationConfig.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        beanDescription = new BeanDescription(SomeClass.class);
        propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
        defaultInstances = new ObjectMapper(serializationConfig).readValue(ObjectMapperTest.DEFAULT_INSTANCE_JSON, ObjectMap.class);
    }

    @After
    public void tearDown() throws Exception {
        propertyBuilder = null;
        beanDescription = null;
        serializationConfig = null;
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);

        // Test with no annotations
        JavaType declaredType = TypeFactory.type(int[].class);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(serializationType);

        // Test with JsonTypeId annotation
        AnnotationIntrospector introspector = Mockito.mock(AnnotationIntrospector.class);
        when(introspector.findSerializationType(am, declaredType)).thenReturn(TypeFactory.type(String.class));
        propertyBuilder._annotationIntrospector = introspector;
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(TypeFactory.type(String.class), serializationType);

        // Test with @JsonSerialize annotation
        AnnotationIntrospector introspector2 = Mockito.mock(AnnotationIntrospector.class);
        JsonSerialize serAnnot = Mockito.mock(JsonSerialize.class);
        when(serAnnot.type()).thenReturn(TypeFactory.type(String.class));
        when(introspector2.findAnnotation(am, JsonSerialize.class)).thenReturn(serAnnot);
        propertyBuilder._annotationIntrospector = introspector2;
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(TypeFactory.type(String.class), serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException, IllegalAccessException, InstantiationException {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getName()).thenReturn("testProperty");

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", am, TypeFactory.type(int.class));
        assertEquals(0, defaultValue);

        defaultBean = defaultInstances.get(" SomeClass ");
        propertyBuilder._defaultBean = defaultBean;
        defaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", am, TypeFactory.type(int.class));
        assertEquals(42, defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType intType = TypeFactory.type(int.class);
        JavaType stringType = TypeFactory.type(String.class);
        JavaType mapType = TypeFactory.type(Map.class, String.class, int.class);
        JavaType listType = TypeFactory.typeCollectionType(List.class, int.class);

        assertEquals(0, propertyBuilder.getDefaultValue(intType).intValue());
        assertEquals("", propertyBuilder.getDefaultValue(stringType));
        assertNull(propertyBuilder.getDefaultValue(mapType));
        assertNull(propertyBuilder.getDefaultValue(listType));
    }

    @Test
    public void testBuildWriter() throws JsonMappingException, IOException {
        SerializedStringSerializer stringSerializer = new SerializedStringSerializer();
        TypeSerializer intTypeSerializer = Mockito.mock(TypeSerializer.class);
        TypeSerializer stringTypeSerializer = Mockito.mock(TypeSerializer.class);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getName()).thenReturn("testProperty");

        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn("testProperty");

        when(intTypeSerializer.createSerializer(propertyBuilder._config, propDef, PropertyBuilder.class, int[].class, null, null, null)).thenReturn(stringSerializer);

        JavaType declaredType = TypeFactory.type(int[].class);
        JsonSerializer<int[]> ser = Mockito.mock(JsonSerializer.class);
        when(ser.getSerializerProvider().findValueSerializer(int[].class, null)).thenReturn(stringSerializer);

        TypeSerializer serializationType = Mockito.mock(TypeSerializer.class);
        when(serializationType.createSerializer(propertyBuilder._config, propDef, PropertyBuilder.class, int[].class, intTypeSerializer, null, null)).thenReturn(intTypeSerializer);

        AnnotatedMember amUnwrapped = Mockito.mock(AnnotatedMember.class);
        NameTransformer unwrapper = Mockito.mock(NameTransformer.class);
        when(amUnwrapped.getName()).thenReturn("testPropertyUnwrapped");
        when(unwrapper.transform(propertyBuilder._config, "testProperty")).thenReturn("testPropertyUnwrapped");

        try {
            BeanPropertyWriter bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, intTypeSerializer, serializationType, am, false);
            assertNotNull(bpw);
            assertEquals("testProperty", bpw.getName());
            assertEquals(int[].class, bpw.getType());
            assertEquals("testProperty", bpw.getMember());
            assertNull(bpw.getValueSerializer());
            assertNull(bpw.getNullSerializer());
            assertNull(bpw.getUnwrappingWriter());

            BeanPropertyWriter bpwUnwrapped = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, intTypeSerializer, serializationType, amUnwrapped, false);
            assertNotNull(bpwUnwrapped);
            assertEquals("testPropertyUnwrapped", bpwUnwrapped.getName());
            assertEquals(int[].class, bpwUnwrapped.getType());
            assertEquals("testPropertyUnwrapped", bpwUnwrapped.getMember());
            assertNotNull(bpwUnwrapped.getValueSerializer());
            assertNull(bpwUnwrapped.getNullSerializer());
            assertNotNull(bpwUnwrapped.getUnwrappingWriter());
            assertEquals(unwrapper, bpwUnwrapped.getUnwrappingWriter());
        } finally {
            reset(intTypeSerializer, stringTypeSerializer, am, propDef, ser, serializationType, amUnwrapped, unwrapper);
        }
    }
}

class SomeClass {
    private int testProperty;
}