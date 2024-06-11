
package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;
        
        PropertyBuilder propertyBuilder = new PropertyBuilder(Mockito.mock(SerializationConfig.class), Mockito.mock(BeanDescription.class));
        BeanPropertyWriter result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        
        assertNotNull(result);
    }
}
