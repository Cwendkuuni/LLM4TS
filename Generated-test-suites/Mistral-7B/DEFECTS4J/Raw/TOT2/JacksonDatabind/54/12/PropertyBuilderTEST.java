import com.fasterxml.jackson.core.SerializationConfig;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.MarkerForEmpty;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilder() throws Exception {
        ObjectNode node = new ObjectMapper().createObjectNode();

        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new ClassInfo(String.class));

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test default inclusion
        String defaultString = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false).getName();
        JsonInclude.Value defaultValue = propertyBuilder._defaultInclusion;
        assertNotNull(defaultValue);
        JsonInclude.Include inclusion = defaultValue.getValueInclusion();
        assertEquals(JsonInclude.Include.ALWAYS, inclusion);
        assertEquals("", defaultString);

        // Test non-default inclusion with property value
        String nonDefaultString = "test";
        Object nonDefaultBean = new Object() {
            private String prop = nonDefaultString;
        };
        AnnotatedMember annotatedMember = new StubAnnotatedMember("testProp", nonDefaultBean.getClass(), null, null);
        BeanPropertyDefinition propDef = new StubBeanPropertyDefinition("testProp", null, null, null, null, null, null, null, null, null, null, null);
        JavaType declaredType = TypeFactory.type(String.class);
        JsonSerializer<String> ser = new StubJsonSerializer<>();
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        MarkerForEmpty markerForEmpty = null;
        boolean defaultUseStaticTyping = false;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, annotatedMember, defaultUseStaticTyping);
        assertTrue(writer.includeValue(nonDefaultString));

        // Test non-default inclusion with default value
        String propName = "testProp";
        Object defaultValueBean = new Object() {
        };
        Object defaultValueObject = propertyBuilder.getPropertyDefaultValue(propName, new StubAnnotatedMember(propName, defaultValueBean.getClass(), null, null), declaredType);
        assertNull(defaultValueObject); // default value is not available for this test case
        markerForEmpty = new MarkerForEmpty();
        suppress.on(PropertyBuilder.class).method("getPropertyDefaultValue").andThrow(new IllegalStateException("getPropertyDefaultValue should not be called for non-default inclusion with default value"));
        try {
            Object propDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, new StubAnnotatedMember(propName, defaultValueBean.getClass(), null, null), declaredType);
            fail("getPropertyDefaultValue should not return a value for non-default inclusion with default value");
        } catch (IllegalStateException e) {
            // expected
        }

        // Test non-default inclusion with empty collection
        JavaType javaType = TypeFactory.constructCollectionType(List.class, Integer.class);
        JsonInclude.Include inclusionType = JsonInclude.Include.NON_EMPTY;
        Object emptyCollection = new ArrayList<>();
        Object defaultEmptyCollection = new ArrayList<>();
        Object emptyValue = new Integer(0);
        Object defaultValue = new Integer(1);
        AnnotatedMember emptyAnnotatedMember = new StubAnnotatedMember("testProp", javaType.getRawClass(), null, null);
        AnnotatedMember defaultAnnotatedMember = new StubAnnotatedMember("testProp", javaType.getRawClass(), null, null);
        BeanPropertyDefinition emptyPropDef = new StubBeanPropertyDefinition("testProp", null, null, null, null, null, null, null, null, null, null, null);
        BeanPropertyDefinition defaultPropDef = new StubBeanPropertyDefinition("testProp", null, null, null, null, null, null, null, null, null, null, null);
        writer = propertyBuilder.buildWriter(null, emptyPropDef, javaType, ser, typeSer, contentTypeSer, emptyAnnotatedMember, false);
        assertFalse(writer.includeValue(emptyCollection));
        writer = propertyBuilder.buildWriter(null, defaultPropDef, javaType, ser, typeSer, contentTypeSer, defaultAnnotatedMember, false);
        assertTrue(writer.includeValue(defaultValue));

        // Test null suppression
        Object nullValue = null;
        Object nullDefaultValue = null;
        JsonInclude.Include inclusionNull = JsonInclude.Include.NON_NULL;
        Object nullAnnotatedMember = new StubAnnotatedMember("testProp", String.class, null, null);
        BeanPropertyDefinition nullPropDef = new StubBeanPropertyDefinition("testProp", null, null, null, null, null, null, null, null, null, null, null);
        writer = propertyBuilder.buildWriter(null, nullPropDef, String.class, ser, typeSer, contentTypeSer, nullAnnotatedMember, false);
        assertFalse(writer.includeValue(nullValue));
        writer = propertyBuilder.buildWriter(null, nullPropDef, String.class, ser, typeSer, contentTypeSer, nullAnnotatedMember, true);
        assertTrue(writer.includeValue(nullDefaultValue));

        // Test empty reference type
        Object emptyReference = new Object();
        Object emptyReferenceDefault = new Object();
        JsonInclude.Include inclusionEmptyReference = JsonInclude.Include.NON_EMPTY;
        AnnotatedMember emptyReferenceAnnotatedMember = new StubAnnotatedMember("testProp", Object.class, null, null);
        BeanPropertyDefinition emptyReferencePropDef = new StubBeanPropertyDefinition("testProp", null, null, null, null, null, null, null, null, null, null, null);
        writer = propertyBuilder.buildWriter(null, emptyReferencePropDef, Object.class, ser, typeSer, contentTypeSer, emptyReferenceAnnotatedMember, false);
        assertFalse(writer.includeValue(emptyReference));
        writer = propertyBuilder.buildWriter(null, emptyReferencePropDef, Object.class, ser, typeSer, contentTypeSer, emptyReferenceAnnotatedMember, true);
        assertTrue(writer.includeValue(emptyReferenceDefault));

        // Test exception handling
        String errorMessage = "Error message";
        Throwable throwable = new Exception(errorMessage);
        Class<?> exceptionClass = Throwable.class;
        AnnotatedMember errorAnnotatedMember = new StubAnnotatedMember("testProp", String.class, null, null);
        BeanPropertyDefinition errorPropDef = new StubBeanPropertyDefinition("testProp", null, null, null, null, null, null, null, null, null, null, null);
        JsonSerializer<String> errorSerializer = new StubJsonSerializer<>();
        TypeSerializer errorTypeSerializer = null;
        TypeSerializer errorContentTypeSerializer = null;
        MarkerForEmpty errorMarkerForEmpty = null;
        BeanPropertyWriter errorWriter = propertyBuilder.buildWriter(null, errorPropDef, String.class, errorSerializer, errorTypeSerializer, errorContentTypeSerializer, errorAnnotatedMember, false);
        try {
            errorWriter.serializeValue(null, null, null, null);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals(errorMessage, e.getMessage());
            assertEquals(exceptionClass, e.getCause().getClass());
        }

        // Test unwrapping
        Object unwrappedValue = new Object();
        NameTransformer nameTransformer = new StubNameTransformer("testTransformer");
        AnnotatedMember unwrappedAnnotatedMember = new StubAnnotatedMember("testProp", String.class, null, null);
        BeanPropertyDefinition unwrappedPropDef = new StubBeanPropertyDefinition("testProp", null, null, null, null, null, null, null, null, null, null, null);
        BeanPropertyWriter unwrappedWriter = propertyBuilder.buildWriter(null, unwrappedPropDef, String.class, ser, typeSer, contentTypeSer, unwrappedAnnotatedMember, false).unwrappingWriter(nameTransformer);
        assertTrue(unwrappedWriter.isEnabledForSerialization(unwrappedValue));

        // Test custom null serializer
        JsonSerializer<String> customNullSerializer = new StubJsonSerializer<>();
        Object customNullValue = null;
        AnnotatedMember customNullAnnotatedMember = new StubAnnotatedMember("testProp", String.class, null, null);
        BeanPropertyDefinition customNullPropDef = new StubBeanPropertyDefinition("testProp", null, null, null, null, null, null, null, null, null, null, null);
        BeanPropertyWriter customNullWriter = propertyBuilder.buildWriter(null, customNullPropDef, String.class, ser, typeSer, contentTypeSer, customNullAnnotatedMember, false);
        customNullWriter.assignNullSerializer(customNullSerializer);
        assertTrue(customNullWriter.includeValue(customNullValue));

        // Test custom name transformer
        NameTransformer customNameTransformer = new StubNameTransformer("testTransformer");
        AnnotatedMember customNameAnnotatedMember = new StubAnnotatedMember("testProp", String.class, null, null);
        BeanPropertyDefinition customNamePropDef = new StubBeanPropertyDefinition("testProp", null, null, null, null, null, null, null, null, null, null, null);
        BeanPropertyWriter customNameWriter = propertyBuilder.buildWriter(null, customNamePropDef, String.class, ser, typeSer, contentTypeSer, customNameAnnotatedMember, false);
        customNameWriter.unwrappingWriter(customNameTransformer);
        String customName = "customName";
        String expectedCustomName = customNameTransformer.transform(customName, null, null);
        assertEquals(expectedCustomName, customNameWriter.getName());

        // Test serialization type override
        JavaType serializationType = TypeFactory.type(String.class, Integer.class);
        Annotated a = new StubAnnotated("testAnnotated");
        Boolean useStaticTyping = true;
        BeanPropertyWriter serializationTypeWriter = propertyBuilder.buildWriter(null, null, serializationType, null, null, null, null, useStaticTyping);
        assertEquals(serializationType, serializationTypeWriter.getType());
    }

    private static class StubAnnotatedMember implements AnnotatedMember {

        private final String name;
        private final Class<?> declaringClass;
        private final Annotation[] annotations;
        private final boolean isStatic;

        public StubAnnotatedMember(String name, Class<?> declaringClass, Annotation[] annotations, boolean isStatic) {
            this.name = name;
            this.declaringClass = declaringClass;
            this.annotations = annotations;
            this.isStatic = isStatic;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<?> getDeclaringClass() {
            return declaringClass;
        }

        @Override
        public Annotation[] getAnnotations() {
            return annotations;
        }

        @Override
        public boolean isStatic() {
            return isStatic;
        }
    }

    private static class StubBeanPropertyDefinition implements BeanPropertyDefinition {

        private final String name;
        private final JsonSerializer<?> serializer;
        private final JsonDeserializer<?> deserializer;
        private final Annotation[] annotations;
        private final JsonProperty property;
        private final BeanDescription beanDescription;
        private final BeanPropertyType propertyType;
        private final int propertyIndex;
        private final int propertyId;
        private final PropertyName propertyName;
        private final SerializationConfig serializationConfig;
        private final ObjectMapper objectMapper;
        private final BeanSerializerFactory beanSerializerFactory;

        public StubBeanPropertyDefinition(String name, JsonSerializer<?> serializer, JsonDeserializer<?> deserializer, Annotation[] annotations,
                                          JsonProperty property, BeanDescription beanDescription, BeanPropertyType propertyType,
                                          int propertyIndex, int propertyId, PropertyName propertyName,
                                          SerializationConfig serializationConfig, ObjectMapper objectMapper, BeanSerializerFactory beanSerializerFactory) {
            this.name = name;
            this.serializer = serializer;
            this.deserializer = deserializer;
            this.annotations = annotations;
            this.property = property;
            this.beanDescription = beanDescription;
            this.propertyType = propertyType;
            this.propertyIndex = propertyIndex;
            this.propertyId = propertyId;
            this.propertyName = propertyName;
            this.serializationConfig = serializationConfig;
            this.objectMapper = objectMapper;
            this.beanSerializerFactory = beanSerializerFactory;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public JsonSerializer<?> getSerializer() {
            return serializer;
        }

        @Override
        public JsonDeserializer<?> getDeserializer() {
            return deserializer;
        }

        @Override
        public Annotation[] getAnnotations() {
            return annotations;
        }

        @Override
        public JsonProperty getProperty() {
            return property;
        }

        @Override
        public BeanDescription getBeanDescription() {
            return beanDescription;
        }

        @Override
        public BeanPropertyType getBeanPropertyType() {
            return propertyType;
        }

        @Override
        public int getPropertyIndex() {
            return propertyIndex;
        }

        @Override
        public int getPropertyId() {
            return propertyId;
        }

        @Override
        public PropertyName getName() {
            return propertyName;
        }

        @Override
        public SerializationConfig getSerializationConfig() {
            return serializationConfig;
        }

        @Override
        public ObjectMapper getObjectMapper() {
            return objectMapper;
        }

        @Override
        public BeanSerializerFactory getBeanSerializerFactory() {
            return beanSerializerFactory;
        }
    }

    private static class StubJsonSerializer<T> implements JsonSerializer<T> {

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // empty implementation
        }
    }

    private static class StubNameTransformer implements NameTransformer {

        private final String transformerName;

        public StubNameTransformer(String transformerName) {
            this.transformerName = transformerName;
        }

        @Override
        public String transform(String name, Class<?> declaredClass, Object value) throws IllegalArgumentException {
            return transformerName;
        }
    }
}