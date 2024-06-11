package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.BeanPropertyWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private BeanDescription beanDescription;
    private SerializationConfig serializationConfig;

    @Before
    public void setUp() {
        beanDescription = mock(BeanDescription.class);
        serializationConfig = mock(SerializationConfig.class);
        propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
    }

    @Test
    public void testBuildWriter() throws Exception {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.unknownType();
        JsonSerializer serializer = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = false;

        propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, serializer, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        verify(propDef, times(1)).getName();
        verify(serializerProvider, times(1)).reportBadPropertyDefinition(any(), any(), anyString());
    }
}