package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link PropertyBuilder} class.
 */
public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null);
        AnnotationIntrospector annotationIntrospector = new NoOpAnnotationIntrospector();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._annotationIntrospector = annotationIntrospector;

        // Test with null serializer, type serializer and content type serializer
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null, null, null, null, null);
        JavaType declaredType = TypeFactory.defaultInstance();
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, null, null, null, null, false);
        Assert.assertNull(bpw);

        // Test with serializer, type serializer and content type serializer
        JsonSerializer<?> ser = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider prov) throws IOException {
                jgen.writeString("test");
            }
        };
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        Boolean useStaticTyping = false;
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, useStaticTyping);
        Assert.assertNotNull(bpw);

        // Test with serializer, type serializer and content type serializer with container type
        TypeSerializer contentTypeSerContainer = new TypeSerializer() {
            @Override
            public void serializeWithType(Object value, JsonGenerator jgen, SerializerProvider prov, TypeSerializer ts)
                    throws IOException {
                jgen.writeValue(value);
            }

            @Override
            public void serializeEmptyArray(JsonGenerator jgen, SerializerProvider prov) throws IOException {
                jgen.writeNull();
            }
        };
        JavaType serializationTypeContainer = TypeFactory.type(declaredType, String[].class);
        JavaType ctContainer = serializationTypeContainer.getContentType();
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSerContainer, am, useStaticTyping);
        Assert.assertNotNull(bpw);
        Assert.assertEquals(String[].class, bpw.getType().getRawClass());
        Assert.assertEquals(contentTypeSerContainer, bpw.getTypeSerializer());

        // Test with different serialization types
        JsonSerializer<Object> ser1 = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider prov) throws IOException {
                jgen.writeNumber(1);
            }
        };
        JsonSerializer<Object> ser2 = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider prov) throws IOException {
                jgen.writeString("two");
            }
        };
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser1, typeSer, contentTypeSer, am, useStaticTyping);
        BeanPropertyWriter bpw1 = propertyBuilder.buildWriter(prov, propDef, declaredType, ser2, typeSer, contentTypeSer, am, useStaticTyping);
        Assert.assertNotSame(bpw1, bpw);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = new NoOpAnnotationIntrospector();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        propertyBuilder._annotationIntrospector = annotationIntrospector;

        JavaType declaredType = TypeFactory.type(String[].class);
        Annotated a = new Annotated() {
            @Override
            public Serializable getAnnotatedObject() {
                return null;
            }

            @Override
            public Class<?> getRawType() {
                return String.class;
            }

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public JavaType getType() {
                return declaredType;
            }
        };
        JavaType secondary = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertEquals(declaredType, secondary);

        JsonSerialize annotation = new JsonSerialize() {
            @JsonSerialize.Inside
            @JsonSerialize.Using(JsonStringSerializer.instance)
            public void serializeWithChildHandler(JsonGenerator jgen, SerializerProvider prov, Object value) throws IOException {
                jgen.writeString("test");
            }
        };
        Annotated aWithAnnotation = new Annotated() {
            @Override
            public Serializable getAnnotatedObject() {
                return null;
            }

            @Override
            public Class<?> getRawType() {
                return String.class;
            }

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public JavaType getType() {
                return declaredType;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[]{annotation};
            }
        };
        JavaType secondaryWithAnnotation = propertyBuilder.findSerializationType(aWithAnnotation, true, declaredType);
        Assert.assertEquals(declaredType, secondaryWithAnnotation);

        // Test when annotation type is not a subtype of declared type
        Annotated aWithUnrelatedAnnotation = new Annotated() {
            @Override
            public Serializable getAnnotatedObject() {
                return null;
            }

            @Override
            public Class<?> getRawType() {
                return Integer.class;
            }

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public JavaType getType() {
                return declaredType;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[]{new JsonSerialize() {}};
            }
        };
        JsonMappingException e = assertThrows(JsonMappingException.class, () -> propertyBuilder.findSerializationType(aWithUnrelatedAnnotation, true, declaredType));
        Assert.assertEquals("Illegal concrete-type annotation for method 'test': class class [Ljava.lang.String;' not a super-type of (declared) class class [Ljava.lang.String;]", e.getMessage());
    }
}

class NoOpAnnotationIntrospector extends AnnotationIntrospector {
    @Override
    public Object findAnnotationValue(Object annotated, Annotation a) {
        return null;
    }

    @Override
    public void findSerializationType(SerializationConfig config, Annotated a, JavaType declaredType, JsonSerializer<?> ser,
            TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotationIntrospector introspector,
            PropertyName propertyName, BeanPropertyDefinition propDef) throws JsonMappingException {
        // empty implementation
    }

    @Override
    public void findNullSerializer(Annotated a, JsonSerializer<?> nullSerializer) {
        // empty implementation
    }

    @Override
    public void findUnwrappingNameTransformer(Annotated a, NameTransformer unwrapper) {
        // empty implementation
    }

    @Override
    public Annotation findPropertyAnnotation(Annotated a, Annotation ann, JavaType propertyType) {
        return null;
    }
}