package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        // Test with annotations
        BeanDescription beanDesc = new BeanDescription(Object.class);
        AnnotatedMember member = new StubAnnotatedMember("testProperty", Object[].class);
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType declaredType = JavaType.construct(Object[].class);
        JsonSerialize annotation = new StubJsonSerialize();
        member.addAnnotation(annotation);
        JavaType serializationType = propertyBuilder.findSerializationType(member, true, declaredType);
        assertEquals(declaredType, serializationType);

        // Test with no annotations
        member.clearAnnotations();
        assertNull(propertyBuilder.findSerializationType(member, true, declaredType));

        // Test with static typing
        member.clearAnnotations();
        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            protected JavaType findSerializationType(Annotated a, boolean useStaticTyping, JavaType declaredType) {
                return declaredType;
            }
        };
        serializationType = propertyBuilder.findSerializationType(member, false, declaredType);
        assertEquals(declaredType, serializationType);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        // Test with no serializer or type serializer provided
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object valueToSuppress = null;
        JsonInclude.Value inclusion = JsonInclude.Value.builder().build();
        JsonInclude.Include inclusionType = inclusion.getValueInclusion();

        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, JavaType.construct(String.class), null, null, null, null, false, valueToSuppress) ;
        assertNull(writer.getNullSerializer());
        assertNull(writer.getUnwrappingWriter());

        // Test with serializer provided but no type serializer
        SerializerProvider serializerProvider = new StubSerializerProvider();
        JsonSerializer ser = new StubJsonSerializer();
        writer = propertyBuilder.buildWriter(serializerProvider, null, JavaType.construct(String.class), ser, null, null, null, false, valueToSuppress);
        assertNull(writer.getNullSerializer());
        assertNull(writer.getUnwrappingWriter());
        assertEquals(ser, writer.getSerializer());

        // Test with type serializer provided but no serializer
        TypeSerializer contentTypeSer = new StubTypeSerializer();
        writer = propertyBuilder.buildWriter(null, null, JavaType.construct(String.class), null, contentTypeSer, null, null, false, valueToSuppress);
        assertNull(writer.getSerializer());
        assertEquals(contentTypeSer, writer.getTypeSerializer());

        // Test with serializer and type serializer provided
        JsonSerializer ser2 = new StubJsonSerializer();
        TypeSerializer typeSer = new StubTypeSerializer();
        writer = propertyBuilder.buildWriter(serializerProvider, null, JavaType.construct(String.class), ser2, null, typeSer, null, false, valueToSuppress);
        assertEquals(ser2, writer.getSerializer());
        assertEquals(typeSer, writer.getTypeSerializer());

        // Test non-null suppress flag
        JsonInclude.Include inclusion2 = JsonInclude.Include.NON_NULL;
        inclusion = JsonInclude.Value.builder().include(inclusion2).build();
        inclusionType = inclusion.getValueInclusion();
        PropertyBuilder propertyBuilder2 = new PropertyBuilder(config, beanDesc) {
            @Override
            protected Object getDefaultValue(JavaType type) {
                return null;
            }
        };
        valueToSuppress = null;
        writer = propertyBuilder2.buildWriter(serializerProvider, null, JavaType.construct(String.class), ser2, null, typeSer, null, true, valueToSuppress);
        assertNull(writer.getValueToSuppress());
    }

    @Test
    public void testFindSerializationTypeWithSerializedTypeAnnotation() throws JsonMappingException {
        Class<?> testClass = new ClassTest();
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDescription(testClass);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember member = new StubAnnotatedMember("testProperty", String.class);
        JavaType declaredType = JavaType.construct(String.class);
        SerializerProvider serializerProvider = new StubSerializerProvider();
        JsonSerializer ser = new StubJsonSerializer();
        member.addAnnotation(new JsonSerialize.SerializerModifier() {
            @Override
            public JsonSerialize.Typing getSerializationTyping(Annotated a, Typer typer) {
                return JsonSerialize.Typing.STATIC;
            }
        });
        member.addAnnotation(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
        member.addAnnotation(new JsonSerialize(String.class));

        JsonInclude.Value inclusion = JsonInclude.Value.builder().build();
        JsonInclude.Include inclusionType = inclusion.getValueInclusion();

        JavaType serializationType = propertyBuilder.findSerializationType(member, true, declaredType);
        assertEquals(declaredType.getRawClass(), serializationType.getRawClass());
        assertEquals(JsonSerialize.Typing.STATIC, serializationType.getStaticTyping());

        JavaType actualType = (serializationType == null) ? declaredType : serializationType;
        assertEquals(JsonInclude.Include.NON_DEFAULT, inclusionType);
    }

    @Test
    public void testFindSerializationTypeWithNoSerializedTypeAnnotation() throws JsonMappingException {
        Class<?> testClass = new ClassTest();
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDescription(testClass);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember member = new StubAnnotatedMember("testProperty", String.class);
        JavaType declaredType = JavaType.construct(String.class);
        SerializerProvider serializerProvider = new StubSerializerProvider();
        JsonSerializer ser = new StubJsonSerializer();
        member.addAnnotation(new JsonInclude(JsonInclude.Include.NON_DEFAULT));

        JsonInclude.Value inclusion = JsonInclude.Value.builder().build();
        JsonInclude.Include inclusionType = inclusion.getValueInclusion();

        JavaType serializationType = propertyBuilder.findSerializationType(member, true, declaredType);
        assertNull(serializationType);

        JavaType actualType = (serializationType == null) ? declaredType : serializationType;
        assertEquals(JsonInclude.Include.NON_DEFAULT, inclusionType);
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        beanDesc = new BeanDescription(new DefaultBean());
        propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);
        defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription(new DefaultBean());
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();

        String name = "testProperty";
        Object value = propertyBuilder.getPropertyDefaultValue(name, new StubAnnotatedMember(name, String.class), JavaType.construct(String.class));
        assertEquals("", value);

        name = "listProperty";
        Object listValue = propertyBuilder.getPropertyDefaultValue(name, new StubAnnotatedMember(name, ArrayList.class), JavaType.construct(ArrayList.class));
        assertEquals(JsonInclude.Include.NON_EMPTY, listValue);

        name = "mapProperty";
        Object mapValue = propertyBuilder.getPropertyDefaultValue(name, new StubAnnotatedMember(name, HashMap.class), JavaType.construct(HashMap.class));
        assertEquals(JsonInclude.Include.NON_EMPTY, mapValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithDefaultBeanNull() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        String name = "testProperty";
        Object value = propertyBuilder.getPropertyDefaultValue(name, new StubAnnotatedMember(name, String.class), JavaType.construct(String.class));
        assertNull(value);
    }

    /**
     * Stub class for testing {@link PropertyBuilder#buildWriter(SerializerProvider, BeanPropertyDefinition, JavaType, JsonSerializer, TypeSerializer, TypeSerializer, AnnotatedMember, boolean)}
     */
    static class StubBeanPropertyWriter extends BeanPropertyWriter {
        @Override
        public void writeValue(JsonGenerator gen, Object value) throws IOException {
        }
    }

    /**
     * Stub class for testing {@link PropertyBuilder#findSerializationType(Annotated, boolean, JavaType)}
     */
    static class StubPropertyBuilder extends PropertyBuilder {
        @Override
        protected JavaType findSerializationType(Annotated a, boolean useStaticTyping, JavaType declaredType) throws JsonMappingException {
            return null;
        }
    }

    /**
     * Stub class for testing {@link PropertyBuilder#buildWriter(SerializerProvider, BeanPropertyDefinition, JavaType, JsonSerializer, TypeSerializer, TypeSerializer, AnnotatedMember, boolean)}
     */
    static class StubSerializerProvider implements SerializerProvider {
        private JsonSerializer _serializer = null;
        private TypeSerializer _typeSerializer = null;

        @Override
        public <T extends JsonSerializer<?>> T serializerInstance(Annotated a, Class<T> serializerType) {
            return _serializer;
        }

        @Override
        public <T extends TypeSerializer> T typeSerializerInstance(Annotated a, Class<T> typeSerializerType) {
            return _typeSerializer;
        }

        @Override
        public void setSerializerProviderDefaults(SerializationConfig config) {
        }

        public void setSerializer(JsonSerializer serializer) {
            _serializer = serializer;
        }

        public void setTypeSerializer(TypeSerializer typeSerializer) {
            _typeSerializer = typeSerializer;
        }
    }

    /**
     * Stub class for testing {@link PropertyBuilder#buildWriter(SerializerProvider, BeanPropertyDefinition, JavaType, JsonSerializer, TypeSerializer, TypeSerializer, AnnotatedMember, boolean)}
     */
    static class StubJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        }
    }

    /**
     * Stub class for testing {@link PropertyBuilder#buildWriter(SerializerProvider, BeanPropertyDefinition, JavaType, JsonSerializer, TypeSerializer, TypeSerializer, AnnotatedMember, boolean)}
     */
    static class StubTypeSerializer extends TypeSerializer {
        @Override
        public void writeTypePrefix(JsonGenerator gen, Object value) throws IOException {
        }

        @Override
        public void writeTypeSuffix(JsonGenerator gen, Object value) throws IOException {
        }

        @Override
        public void writeTypePrefixForArray(JsonGenerator gen, Object value) throws IOException {
        }

        @Override
        public void writeTypeSuffixForArray(JsonGenerator gen, Object value) throws IOException {
        }
    }

    static class StubAnnotatedMember implements AnnotatedMember {
        private String name;
        private Class<?> type;

        public StubAnnotatedMember(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public AnnotationInfo[] getMemberAnnotations() {
            return new AnnotationInfo[0];
        }

        @Override
        public AnnotationInfo[] findAnnotations(Class<?> annotationClass) {
            if (annotationClass == JsonInclude.class) {
                return new AnnotationInfo[] { new StubAnnotationInfo(annotationClass) };
            }
            return new AnnotationInfo[0];
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<?> getDeclaredMemberType() {
            return type;
        }

        @Override
        public Class<?> getRawType() {
            return type;
        }

        static class StubAnnotationInfo implements AnnotationInfo {
            private Class<?> annotationClass;

            public StubAnnotationInfo(Class<?> annotationClass) {
                this.annotationClass = annotationClass;
            }

            @Override
            public AnnotationInstance getAnnotationInstance() {
                return null;
            }

            @Override
            public Class<?> getAnnotationType() {
                return annotationClass;
            }

            @Override
            public boolean isAnnotationPresent(Class<?> annotationType) {
                return annotationType.isAssignableFrom(annotationClass);
            }
        }
    }

    static class DefaultBean {
        private String testProperty = "";
        private List<String> listProperty = new ArrayList<>();
        private Map<String, String> mapProperty = new HashMap<>();
    }
}