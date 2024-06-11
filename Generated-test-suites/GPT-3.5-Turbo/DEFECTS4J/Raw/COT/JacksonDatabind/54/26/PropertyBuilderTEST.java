package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private BeanDescription beanDesc;

    @Test
    public void testBuildWriter() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(provider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull(writer);
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();

        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("test", member, type);

        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = mock(JavaType.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertNull(defaultValue);
    }
}