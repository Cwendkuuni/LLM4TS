package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper();
        JsonAnnotationIntrospector introspector = mapper.getAnnotationIntrospector();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(javaType);

        // Test with no annotations
        JavaType result = propertyBuilder.findSerializationType(am, false, null);
        assertNull(result);

        // Test with JsonSerialize annotation
        JsonSerialize jsonSerializeAnnotation = Mockito.mock(JsonSerialize.class);
        when(jsonSerializeAnnotation.type()).thenReturn(javaType);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);

        result = propertyBuilder.findSerializationType(am, false, null);
        assertEquals(javaType, result);

        // Test with JsonTypeId annotation
        JsonTypeId jsonTypeIdAnnotation = Mockito.mock(JsonTypeId.class);
        when(jsonTypeIdAnnotation.value()).thenReturn("id");
        when(am.getAnnotation(JsonTypeId.class)).thenReturn(jsonTypeIdAnnotation);

        result = propertyBuilder.findSerializationType(am, false, null);
        assertNotNull(result);
        assertEquals(TypeSerializer.empty, result.getValueSerializer());

        // Test with JsonTypeName annotation
        JsonTypeName jsonTypeNameAnnotation = Mockito.mock(JsonTypeName.class);
        when(jsonTypeNameAnnotation.value()).thenReturn("name");
        when(am.getAnnotation(JsonTypeName.class)).thenReturn(jsonTypeNameAnnotation);

        result = propertyBuilder.findSerializationType(am, false, null);
        assertNotNull(result);
        assertEquals(BeanPropertyWriter.NAME_TRANSFORMER_TYPE, result.getContentTypeHandler());

        // Test with JsonProperty annotation
        JsonProperty jsonPropertyAnnotation = Mockito.mock(JsonProperty.class);
        when(jsonPropertyAnnotation.value()).thenReturn("prop");
        when(am.getAnnotation(JsonProperty.class)).thenReturn(jsonPropertyAnnotation);

        result = propertyBuilder.findSerializationType(am, false, null);
        assertNotNull(result);
        assertEquals(jsonPropertyAnnotation.value(), result.getSerializationPropertyName());

        // Test with JsonInclude annotation
        JsonInclude jsonIncludeAnnotation = Mockito.mock(JsonInclude.class);
        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_DEFAULT);
        when(am.getAnnotation(JsonInclude.class)).thenReturn(jsonIncludeAnnotation);

        result = propertyBuilder.findSerializationType(am, false, null);
        assertNotNull(result);
        assertTrue(result.isContainerType());

        // Test with both JsonSerialize and JsonTypeId annotations
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);
        when(am.getAnnotation(JsonTypeId.class)).thenReturn(jsonTypeIdAnnotation);

        result = propertyBuilder.findSerializationType(am, false, null);
        assertEquals(javaType.withContentTypeHandler(TypeSerializer.empty), result);

        // Test with both JsonTypeId and JsonTypeName annotations
        JsonTypeName jsonTypeNameAnnotation2 = Mockito.mock(JsonTypeName.class);
        when(jsonTypeNameAnnotation2.value()).thenReturn("name2");
        when(am.getAnnotation(JsonTypeName.class)).thenReturn(jsonTypeNameAnnotation2);

        result = propertyBuilder.findSerializationType(am, false, null);
        assertEquals(BeanPropertyWriter.NAME_TRANSFORMER_TYPE, result.getContentTypeHandler());

        // Test with JsonInclude annotation and a null type
        javaType = null;
        result = propertyBuilder.findSerializationType(am, false, javaType);
        assertNull(result);

        // Test with JsonInclude annotation and a null annotation
        when(am.getAnnotation(JsonInclude.class)).thenReturn(null);
        result = propertyBuilder.findSerializationType(am, false, javaType);
        assertNull(result);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper();
        JsonAnnotationIntrospector introspector = mapper.getAnnotationIntrospector();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();

        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("prop", javaType);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(javaType);

        JsonSerialize ser = Mockito.mock(JsonSerializer.class);
        when(ser.getSerializerProvider()).thenReturn(prov);
        when(am.getAnnotations()).thenReturn(new Annotation[0]);

        // Test with no annotations
        BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertNull(result.getSerializerProvider());
        assertNull(result.getSerializerProvider().getSerializerInstance(javaType));
        assertNull(result.getSerializerProvider().getSerializerProvider());

        // Test with JsonSerialize annotation
        JsonSerialize jsonSerializeAnnotation = Mockito.mock(JsonSerialize.class);
        when(jsonSerializeAnnotation.contentSerializer()).thenReturn(ser);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);

        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertEquals(ser, result.getContentSerializer());
        assertSame(prov, result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertNull(result.getSerializerProvider());
        assertSame(prov, result.getSerializerProvider());
        assertEquals(ser, result.getSerializerProvider().getSerializerInstance(javaType));
        assertSame(prov, result.getSerializerProvider().getSerializerProvider());

        // Test with JsonTypeId annotation
        JsonTypeId jsonTypeIdAnnotation = Mockito.mock(JsonTypeId.class);
        when(jsonTypeIdAnnotation.value()).thenReturn("id");
        when(am.getAnnotation(JsonTypeId.class)).thenReturn(jsonTypeIdAnnotation);

        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertSame(TypeSerializer.empty, result.getValueSerializer());
        assertSame(TypeSerializer.empty, result.getValueSerializerProvider());
        assertSame(BeanPropertyWriter.NAME_TRANSFORMER_TYPE, result.getSerializerProvider().getContentTypeHandler());
        assertSame(prov, result.getSerializerProvider());

        // Test with JsonTypeName annotation
        JsonTypeName jsonTypeNameAnnotation = Mockito.mock(JsonTypeName.class);
        when(jsonTypeNameAnnotation.value()).thenReturn("name");
        when(am.getAnnotation(JsonTypeName.class)).thenReturn(jsonTypeNameAnnotation);

        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertSame(BeanPropertyWriter.NAME_TRANSFORMER_TYPE, result.getSerializerProvider().getContentTypeHandler());
        assertSame(prov, result.getSerializerProvider());

        // Test with JsonProperty annotation
        JsonProperty jsonPropertyAnnotation = Mockito.mock(JsonProperty.class);
        when(jsonPropertyAnnotation.value()).thenReturn("prop");
        when(am.getAnnotation(JsonProperty.class)).thenReturn(jsonPropertyAnnotation);

        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertEquals("prop", result.getSerializationPropertyName());
        assertSame(prov, result.getSerializerProvider());

        // Test with JsonInclude annotation
        JsonInclude jsonIncludeAnnotation = Mockito.mock(JsonInclude.class);
        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_DEFAULT);
        when(am.getAnnotation(JsonInclude.class)).thenReturn(jsonIncludeAnnotation);

        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertNull(result.getValueSerializer());
        assertTrue(result.suppressNulls);
        assertSame(BeanPropertyWriter.MARKER_FOR_EMPTY, result.getValueSerializerProvider().getValueSerializerInstance());
        assertSame(prov, result.getSerializerProvider());

        // Test with JsonInclude annotation and a null valueToSuppress
        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertNull(result.getValueSerializer());
        assertFalse(result.suppressNulls);
        assertNull(result.getValueSerializerProvider().getValueSerializerInstance());
        assertSame(prov, result.getSerializerProvider());

        // Test with JsonInclude annotation and a non-empty valueToSuppress
        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_EMPTY);
        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertNull(result.getValueSerializer());
        assertFalse(result.suppressNulls);
        assertNull(result.getValueSerializerProvider().getValueSerializerInstance());
        assertSame(prov, result.getSerializerProvider());

        // Test with JsonInclude annotation and a non-null valueToSuppress
        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_NULL);
        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertNull(result.getValueSerializer());
        assertFalse(result.suppressNulls);
        assertNull(result.getValueSerializerProvider().getValueSerializerInstance());
        assertSame(prov, result.getSerializerProvider());

        // Test with JsonInclude annotation and a null jsonSerializerDef
        JsonSerializer jsonSerializer = Mockito.mock(JsonSerializer.class);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);
        when(jsonSerializeAnnotation.contentSerializer()).thenReturn(jsonSerializer);
        when(am.getAnnotation(JsonInclude.class)).thenReturn(jsonIncludeAnnotation);
        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_DEFAULT);
        when(jsonSerializer.getSerializerProvider()).thenReturn(null);

        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertNull(result.getValueSerializer());
        assertTrue(result.suppressNulls);
        assertNull(result.getValueSerializerProvider().getValueSerializerInstance());
        assertSame(prov, result.getSerializerProvider());

        // Test with JsonInclude annotation and a non-null jsonSerializerDef and a non-empty valueToSuppress
        when(jsonSerializer.getSerializerProvider()).thenReturn(prov);
        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertNull(result.getValueSerializer());
        assertFalse(result.suppressNulls);
        assertSame(BeanPropertyWriter.MARKER_FOR_EMPTY, result.getValueSerializerProvider().getValueSerializerInstance());
        assertSame(prov, result.getSerializerProvider());

        // Test with JsonInclude annotation and a non-null jsonSerializerDef and a non-null valueToSuppress
        JsonSerializer jsonSerializer2 = Mockito.mock(JsonSerializer.class);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);
        when(jsonSerializeAnnotation.contentSerializer()).thenReturn(jsonSerializer);
        when(jsonSerializer.getSerializerProvider()).thenReturn(prov);
        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_NULL);
        when(am.getValue()).thenReturn("value");

        result = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, am, false);
        assertNotNull(result);
        assertEquals(javaType, result.getDeclaredType());
        assertNull(result.getContentSerializer());
        assertNull(result.getContentSerializerProvider());
        assertNull(result.getValueSerializer());
        assertNull(result.getValueSerializerProvider());
        assertNull(result.getValueSerializer());
        assertFalse(result.suppressNulls);
        assertNull(result.getValueSerializerProvider().getValueSerializerInstance());
        assertSame(prov, result.getSerializerProvider());
        assertSame("value", result.getValueSerializerProvider().getSerializationPropertyName());
    }
}