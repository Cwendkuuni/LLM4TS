package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        // Test case for finding serialization type using annotations
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                new AnnotatedMember(FieldTest.class, "field1"), new BeanDescription(FieldTest.class));
        Annotated a = new AnnotatedMember(FieldTest.class, "field1");
        AnnotationIntrospector introspector = Mockito.mock(AnnotationIntrospector.class);
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config,
                new BeanDescription(FieldTest.class));

        when(introspector.findSerializationType(eq(config), eq(a), eq(JavaType.of(FieldTest.class))))
                .thenReturn(JavaType.of(String.class));
        when(introspector.refineSerializationType(eq(config), eq(a), eq(JavaType.of(FieldTest.class)))).thenCallRealMethod();

        JavaType result = propertyBuilder.findSerializationType(a, false, JavaType.of(FieldTest.class));
        assertEquals(JavaType.of(String.class), result);

        verify(introspector).findSerializationType(eq(config), eq(a), eq(JavaType.of(FieldTest.class)));
        verify(introspector, times(1)).refineSerializationType(eq(config), eq(a), eq(JavaType.of(FieldTest.class)));

        // Test case for returning declared type when static typing is enabled
        when(introspector.findSerializationType(eq(config), eq(a), eq(JavaType.of(FieldTest.class))))
                .thenReturn(null);
        when(introspector.findSerializationTyping(a)).thenReturn(JsonSerialize.Typing.STATIC);

        result = propertyBuilder.findSerializationType(a, true, JavaType.of(FieldTest.class));
        assertEquals(JavaType.of(FieldTest.class), result.withStaticTyping());

        // Test case for returning null when neither annotations nor declared type provide type information
        when(introspector.findSerializationType(eq(config), eq(a), eq(JavaType.of(FieldTest.class))))
                .thenReturn(null);
        when(introspector.findSerializationTyping(a)).thenReturn(JsonSerialize.Typing.DEFAULT_TYPING);

        result = propertyBuilder.findSerializationType(a, false, JavaType.of(FieldTest.class));
        assertNull(result);

        verify(introspector, times(1)).findSerializationType(eq(config), eq(a), eq(JavaType.of(FieldTest.class)));
        verify(introspector, times(1)).findSerializationTyping(a);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        // Test case for constructing BeanPropertyWriter with default values
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(am, new BeanDescription(FieldTest.class));
        JavaType declaredType = JavaType.of(String.class);
        JsonSerializer<String> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        Annotations classAnnotations = Mockito.mock(Annotations.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(
                prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertEquals(ser, writer.getSerializer());
        assertEquals(typeSer, writer.getTypeSerializer());
        assertNull(writer.getContentTypeSerializer());
        assertEquals(declaredType, writer.getDeclaredType());
        assertEquals(propDef, writer.getPropertyDefinition());
        assertEquals(am, writer.getMember());
        assertEquals(classAnnotations, writer.getAnnotations());

        // Test case for constructing BeanPropertyWriter with custom null serializer
        Object nullSerializer = Mockito.mock(JsonSerializer.class);
        when(am.getMemberName()).thenReturn("field1");
        when(propDef.getAnnotations()).thenReturn(classAnnotations);

        writer = propertyBuilder.buildWriter(
                prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        JsonSerializer<Object> serializedNull = writer.getNullSerializer();
        assertEquals(serializedNull, prov.serializerInstance(am, nullSerializer));

        // Test case for constructing BeanPropertyWriter with unwrapping name transformer
        NameTransformer unwrapper = Mockito.mock(NameTransformer.class);
        when(am.getName()).thenReturn("field1_unwrapped");

        writer = propertyBuilder.buildWriter(
                prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        BeanPropertyWriter unwrappingWriter = writer.unwrappingWriter(unwrapper);
        assertEquals(unwrappingWriter, writer);
        assertEquals(unwrapper, unwrappingWriter.getNameTransformer());
    }

    @Test
    public void testFindInclusion() {
        // Test case for determining default inclusion value
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(FieldTest.class));

        JsonInclude.Value inclusion = propertyBuilder._defaultInclusion;
        assertEquals(JsonInclude.Include.NON_DEFAULT, inclusion.getValueInclusion());
        assertEquals(JsonInclude.Value.empty(), inclusion.getOverride());

        // Test case for determining inclusion value based on property annotation
        JsonInclude jsonIncludeAnnotation = Mockito.mock(JsonInclude.class);
        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.ALWAYS);

        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedMember(FieldTest.class, "field1"), new BeanDescription(FieldTest.class));
        propDef.setAnnotation(JsonInclude.class, jsonIncludeAnnotation);

        inclusion = propertyBuilder._defaultInclusion;
        assertEquals(JsonInclude.Include.ALWAYS, inclusion.getValueInclusion());
        assertEquals(jsonIncludeAnnotation, inclusion.getOverride());

        // Test case for determining inclusion value based on type annotation
        JsonIncludeType jsonIncludeTypeAnnotation = Mockito.mock(JsonIncludeType.class);
        when(jsonIncludeTypeAnnotation.value()).thenReturn(JsonInclude.Include.NON_NULL);

        JavaType propertyType = JavaType.of(String.class);
        when(new BeanDescription(FieldTest.class).getType()).thenReturn(JavaType.of(FieldTest.class));
        when(new BeanDescription(FieldTest.class).findPropertyAnnotation(JsonInclude.class)).thenReturn(jsonIncludeAnnotation);
        when(new BeanDescription(FieldTest.class).findPropertyAnnotation(JsonIncludeType.class)).thenReturn(jsonIncludeTypeAnnotation);

        inclusion = propertyBuilder._defaultInclusion;
        assertEquals(JsonInclude.Include.NON_NULL, inclusion.getValueInclusion());
        assertNull(inclusion.getOverride());

        // Test case for determining inclusion value based on type override
        when(config.getDefaultPropertyInclusion(eq(FieldTest.class), eq(String.class), JsonInclude.Value.empty())).thenReturn(JsonInclude.Include.NON_EMPTY);

        inclusion = propertyBuilder._defaultInclusion;
        assertEquals(JsonInclude.Include.NON_EMPTY, inclusion.getValueInclusion());
        assertNull(inclusion.getOverride());
    }

    @Test
    public void testPropertyWriterWithEmptyDefaultValue() throws JsonMappingException {
        // Test case for handling empty default value for properties with inclusion type of NON_EMPTY
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(am, new BeanDescription(FieldTest.class));
        JavaType declaredType = JavaType.of(String.class);
        JsonSerializer<String> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(FieldTest.class));
        BeanPropertyWriter writer = propertyBuilder.buildWriter(
                prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        Object valueToSuppress = writer.getValueToSuppress();
        assertNull(valueToSuppress);

        // Test case for handling empty default value for properties with inclusion type of NON_NULL
        when(propDef.getAnnotations()).thenReturn(new Annotations());
        when(propDef.findViews()).thenReturn(new Class[0]);
        when(propDef.getAnnotation(JsonIncludeType.class)).thenReturn(JsonIncludeType.of(JsonInclude.Include.NON_NULL));

        writer = propertyBuilder.buildWriter(
                prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        valueToSuppress = writer.getValueToSuppress();
        assertNull(valueToSuppress);

        // Test case for handling empty default value for properties with inclusion type of NON_DEFAULT
        when(propDef.getAnnotation(JsonInclude.class)).thenReturn(JsonInclude.Value.empty());
        when(propDef.getAnnotation(JsonIncludeType.class)).thenReturn(null);
        when(propDef.getBeanDescription().getDefaultValue()).thenReturn("");

        writer = propertyBuilder.buildWriter(
                prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        valueToSuppress = writer.getValueToSuppress();
        assertNotNull(valueToSuppress);
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, valueToSuppress);
    }

    @Test
    public void testPropertyWriterWithNullDefaultValue() throws JsonMappingException {
        // Test case for handling null default value for properties with inclusion type of NON_NULL
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(am, new BeanDescription(FieldTest.class));
        JavaType declaredType = JavaType.of(String.class);
        JsonSerializer<String> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(FieldTest.class));
        BeanPropertyWriter writer = propertyBuilder.buildWriter(
                prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        Object valueToSuppress = writer.getValueToSuppress();
        assertNull(valueToSuppress);

        // Test case for handling null default value for properties with inclusion type of NON_EMPTY
        when(propDef.getAnnotations()).thenReturn(new Annotations());
        when(propDef.findViews()).thenReturn(new Class[0]);
        when(propDef.getAnnotation(JsonIncludeType.class)).thenReturn(JsonIncludeType.of(JsonInclude.Include.NON_EMPTY));

        writer = propertyBuilder.buildWriter(
                prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        valueToSuppress = writer.getValueToSuppress();
        assertNull(valueToSuppress);

        // Test case for handling null default value for properties with inclusion type of NON_DEFAULT
        when(propDef.getAnnotation(JsonInclude.class)).thenReturn(JsonInclude.Value.empty());
        when(propDef.getAnnotation(JsonIncludeType.class)).thenReturn(null);
        when(propDef.getBeanDescription().getDefaultValue()).thenReturn(null);

        writer = propertyBuilder.buildWriter(
                prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        valueToSuppress = writer.getValueToSuppress();
        assertNull(valueToSuppress);

        // Test case for handling null default value for properties with inclusion type of CUSTOM
        when(propDef.getAnnotation(JsonIncludeType.class)).thenReturn(JsonIncludeType.of(JsonInclude.Include.CUSTOM));
        when(propDef.getAnnotation(JsonInclude.class)).thenReturn(JsonInclude.Value.empty());

        writer = propertyBuilder.buildWriter(
                prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        valueToSuppress = writer.getValueToSuppress();
        assertNull(valueToSuppress);
    }
}