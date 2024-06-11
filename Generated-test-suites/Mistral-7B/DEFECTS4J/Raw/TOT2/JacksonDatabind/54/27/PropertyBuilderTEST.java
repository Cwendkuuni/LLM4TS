package com.example.junit4;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializerProvider serializerProvider;
    private BeanPropertyDefinition propDef;
    private AnnotatedMember am;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDescription = new BeanDescription(Object.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
        serializerProvider = mock(SerializerProvider.class);
        propDef = mock(BeanPropertyDefinition.class);
        am = mock(AnnotatedMember.class);
        this.objectMapper = objectMapper;
    }

    @Test
    public void testGetClassAnnotations() {
        // Testing the getClassAnnotations() method
        Class<?>[] annotations = propertyBuilder.getClassAnnotations().getAnnotations();
        assertNotNull(annotations);
        assertTrue(annotations.length > 0);
    }

    @Test
    public void testBuildWriter_ValidArguments() {
        // Testing the buildWriter() method with valid arguments
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);

        when(objectMapper.findAnnotation(propDef, JsonSerialize.class)).thenReturn(ser);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, true);

        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType_JsonSerializeAnnotation() {
        // Testing the findSerializationType() method with JsonSerialize annotation
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        boolean useStaticTyping = true;

        when(a.getMember()).thenReturn(mock(AnnotatedMember.class));
        when(a.getJavaType()).thenReturn(declaredType);
        when(a.getAnnotation(JsonSerialize.class)).thenReturn(mock(JsonSerialize.class));

        JavaType secondary = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        assertEquals(declaredType, secondary);
    }

    @Test
    public void testFindSerializationType_NoJsonSerializeAnnotation() {
        // Testing the findSerializationType() method with no JsonSerialize annotation
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        boolean useStaticTyping = true;

        when(a.getJavaType()).thenReturn(declaredType);

        JavaType secondary = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        assertNull(secondary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_InvalidAnnotations() {
        // Testing the findSerializationType() method with invalid annotations
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        boolean useStaticTyping = true;

        JavaType secondary = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        assertNull(secondary);
    }

    @Test
    public void testBuildWriter_NullTypeSerializer() {
        // Testing the buildWriter() method with null TypeSerializer
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<String> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, true);

        assertNull(beanPropertyWriter);
    }

    @Test
    public void testBuildWriter_InvalidDefaultUseStaticTyping() {
        // Testing the buildWriter() method with invalid defaultUseStaticTyping
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<String> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);

        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);

        assertNotNull(serializationType);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNull(beanPropertyWriter);
    }

    // Add more test cases for edge cases and error scenarios here
}