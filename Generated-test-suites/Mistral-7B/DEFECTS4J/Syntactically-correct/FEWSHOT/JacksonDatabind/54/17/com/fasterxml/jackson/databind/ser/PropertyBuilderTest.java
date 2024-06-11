package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        // Test when annotation is present and is a subtype of declared type
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerialize() {});
        when(declaredType.getRawClass()).thenReturn(String.class);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(String.class, serializationType.getRawClass());

        // Test when annotation is present but not a subtype of declared type
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerialize() {});
        when(declaredType.getRawClass()).thenReturn(Integer.class);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(serializationType);

        // Test when annotation is not present but static typing is enabled
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(null);
        when(declaredType.getRawClass()).thenReturn(String.class);
        serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(String.class, serializationType.getRawClass());

        // Test when annotation is not present and static typing is not enabled
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(null);
        when(declaredType.getRawClass()).thenReturn(String.class);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testFindSerializationTyping() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated a = Mockito.mock(Annotated.class);

        JsonSerialize.Typing typing = propertyBuilder.findSerializationTyping(a);
        assertEquals(JsonSerialize.Typing.DEFAULT_TYPING, typing);

        when(a.getAnnotation(JsonSerialize.Typing.class)).thenReturn(new JsonSerialize.Typing() {});
        typing = propertyBuilder.findSerializationTyping(a);
        assertEquals(JsonSerialize.Typing.STATIC, typing);
    }

    @Test
    public void testBuildWriterWithNullContentTypeSerializer() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNull(bpw);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        when(propDef.getName()).thenReturn("name");
        when(am.getName()).thenReturn("name");
        when(declaredType.getRawClass()).thenReturn(String.class);

        JavaType serializationType = Mockito.mock(JavaType.class);
        when(typeSer.constructType(String[].class)).thenReturn(serializationType);

        Object valueToSuppress = new Object();

        JsonInclude.Value inclV = new JsonInclude.Value(JsonInclude.Include.NON_EMPTY);
        JsonInclude.Include inclusion = inclV.getValueInclusion();

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertEquals("name", bpw.getName());
        assertEquals(propDef, bpw.getPropertyDefinition());
        assertEquals(am, bpw.getMember());
        assertEquals(declaredType, bpw.getType());
        assertEquals(ser, bpw.getSerializer());
        assertEquals(typeSer, bpw.getTypeSerializer());
        assertEquals(serializationType, bpw.getSerializationType());
        assertEquals(false, bpw.suppressNulls());
        assertEquals(valueToSuppress, bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());
    }

    @Test
    public void testBuildWriterWithCustomNullSerializer() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        when(propDef.getName()).thenReturn("name");
        when(am.getName()).thenReturn("name");
        when(declaredType.getRawClass()).thenReturn(String.class);

        JsonSerializer<?> nullSerializer = Mockito.mock(JsonSerializer.class);
        when(am.getAnnotation(JsonSerialize.NullSerializer.class)).thenReturn(new JsonSerialize.NullSerializer() {});

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertEquals("name", bpw.getName());
        assertEquals(propDef, bpw.getPropertyDefinition());
        assertEquals(am, bpw.getMember());
        assertEquals(declaredType, bpw.getType());
        assertEquals(ser, bpw.getSerializer());
        assertEquals(typeSer, bpw.getTypeSerializer());
        assertNull(bpw.getSerializationType());
        assertNull(bpw.getValueToSuppress());
        assertEquals(nullSerializer, bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());
    }

    @Test
    public void testBuildWriterWithUnwrappingNameTransformer() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        when(propDef.getName()).thenReturn("name");
        when(am.getName()).thenReturn("name");
        when(declaredType.getRawClass()).thenReturn(String.class);

        NameTransformer unwrapper = Mockito.mock(NameTransformer.class);
        when(am.getAnnotation(JsonSerialize.PropertyOrder.class)).thenReturn(new JsonSerialize.PropertyOrder());
        when(am.getAnnotation(JsonSerialize.UnwrappedField.class)).thenReturn(new JsonSerialize.UnwrappedField());

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertEquals("name", bpw.getName());
        assertEquals(propDef, bpw.getPropertyDefinition());
        assertEquals(am, bpw.getMember());
        assertEquals(declaredType, bpw.getType());
        assertEquals(ser, bpw.getSerializer());
        assertEquals(typeSer, bpw.getTypeSerializer());
        assertNull(bpw.getSerializationType());
        assertNull(bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertEquals(unwrapper, bpw.getUnwrappingNameTransformer());
    }
}