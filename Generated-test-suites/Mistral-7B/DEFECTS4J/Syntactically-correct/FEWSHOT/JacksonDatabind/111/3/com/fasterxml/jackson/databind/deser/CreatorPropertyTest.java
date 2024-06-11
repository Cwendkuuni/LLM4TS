package com.fasterxml.jackson.databind.deser;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectToTestAnnotation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.EnumUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.ReflectionUtils;

public class CreatorPropertyTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testBasicFunctionality() throws Exception {
        CreatorProperty property = new CreatorProperty(
                new PropertyName("property"),
                JavaType.of(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                null
        );

        assertNull(property.getFallbackSetter());
        assertFalse(property.isIgnorable());

        property.markAsIgnorable();
        assertTrue(property.isIgnorable());

        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        when(introspector.findAnnotationValue(any(), any())).thenReturn("value");

        BeanDescription description = mock(BeanDescription.class);
        when(description.getAnnotationIntrospector()).thenReturn(introspector);

        Object testInstance = new Object() {
            @JsonCreator
            public Object creatorMethod(@JsonProperty("name") String name) {
                return name;
            }

            @JsonProperty("property")
            private String property;
        };

        Constructor<?> constructor = ReflectionUtils.constructorsFor(testInstance.getClass())
                .findFirst(c -> c.isAnnotationPresent(JsonCreator.class));
        AnnotatedParameter parameter = ReflectionUtils.findParameterAnnotations(constructor, 0);

        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        property = new CreatorProperty(
                new PropertyName("property"),
                JavaType.of(String.class),
                null,
                typeDeserializer,
                null,
                parameter,
                -1,
                null,
                null
        );

        assertEquals("property", property.getName());
        assertEquals(JavaType.of(String.class), property.getType());
        assertEquals(typeDeserializer, property.getTypeDeserializer());
        assertEquals(parameter, property._annotated);
        assertEquals(-1, property._creatorIndex);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);

        assertEquals("property", property.getBeanPropertyName());
        assertEquals("property", property.getNameAsString());
        assertEquals("property", property.getShortName());
        assertEquals("property", property.getSerializationName());

        assertEquals("property", property.getFullName());
        assertEquals("property", property.getRawName());
        assertEquals("property", property.getSimpleName());

        assertEquals(0, property.hashCode());

        property.setFallbackSetter(new CreatorProperty.SettableBeanProperty() {
            @Override
            public void set(Object instance, Object value) throws IOException {}

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return null;
            }
        });

        assertEquals(property.getFallbackSetter(), property.getSettableBeanProperty());

        assertNotNull(property._injectableValueId);
        assertNull(property.findInjectableValue(null, testInstance));

        when(introspector.findAnnotationValue(parameter, JsonProperty.PropertyAnnotation.class)).thenReturn("injectId");
        property.inject(objectMapper.getDeserializationContext(), testInstance);
        assertEquals("value", property.findInjectableValue(objectMapper.getDeserializationContext(), testInstance));

        String json = "{\"name\":\"value\"}";
        Object result = property.deserializeAndSet(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext(), testInstance);
        assertEquals("value", result);

        when(introspector.findAnnotationValue(parameter, JsonProperty.PropertyAnnotation.class)).thenReturn("jsonProperty");
        JsonProperty jsonPropertyAnnotation = convertObjectToTestAnnotation(JsonProperty.class, "jsonProperty");
        property.inject(objectMapper.getDeserializationContext(), testInstance, jsonPropertyAnnotation);
        assertEquals("jsonProperty", property.getName());

        Class<? extends Annotation> annotationType = EnumUtil.findCreatorAnnotation(constructor, JsonCreator.class);
        when(introspector.findAnnotationValues(parameter, annotationType)).thenReturn(new Object[]{jsonPropertyAnnotation});
        String injectedName = (String) property.findInjectableValue(objectMapper.getDeserializationContext(), testInstance)[0];
        assertEquals("jsonProperty", injectedName);

        when(introspector.findAnnotationValues(parameter, JsonProperty.PropertyAnnotation.class)).thenReturn(new Object[]{null});
        when(introspector.findAnnotationValue(parameter, NameTransformer.PropertyNameTransformer.class)).thenReturn("transformedName");
        String transformedName = (String) property.findInjectableValue(objectMapper.getDeserializationContext(), testInstance)[0];
        assertEquals("transformedName", transformedName);

        when(introspector.findAnnotationValues(parameter, JsonProperty.PropertyAnnotation.class)).thenReturn(new Object[]{null});
        when(introspector.findAnnotationValue(parameter, NameTransformer.PropertyNameTransformer.class)).thenReturn(NameTransformer.DefaultNameTransformer.INSTANCE);
        String transformedName2 = (String) property.findInjectableValue(objectMapper.getDeserializationContext(), testInstance)[0];
        assertEquals("property", transformedName2);

        when(typeDeserializer.createContextualHandler(any(), any())).thenReturn(deserializer);
        String deserializedValue = property.deserialize(objectMapper.getFactory().createParser("testValue"), objectMapper.getDeserializationContext());
        assertEquals("testValue", deserializedValue);

        when(typeDeserializer.createContextualHandler(any(), any())).thenThrow(new IOException());
        try {
            property.deserialize(objectMapper.getFactory().createParser("testValue"), objectMapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // expected
        }

        when(typeDeserializer.createContextualHandler(any(), any())).thenThrow(new InvalidDefinitionException("test"));
        try {
            property.deserialize(objectMapper.getFactory().createParser("testValue"), objectMapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // expected
        }

        when(typeDeserializer.createContextualHandler(any(), any())).thenReturn(deserializer);
        when(deserializer.createContextual(null, objectMapper.getDeserializationContext())).thenReturn(new Object() {});
        when(deserializer.deserialize(any(), any(), any(), any()))
                .thenReturn(new Object() {});
        when(deserializer.getValueAs(any(), any())).thenReturn("deserializedValue");

        String json2 = "{\"name\":\"testValue\"}";
        Object deserializedObject = property.deserializeSetAndReturn(objectMapper.getFactory().createParser(json2), objectMapper.getDeserializationContext(), testInstance);
        assertEquals("deserializedValue", deserializedObject);

        when(deserializer.createContextual(null, objectMapper.getDeserializationContext())).thenReturn(null);
        Object deserializedObject2 = property.deserializeSetAndReturn(objectMapper.getFactory().createParser(json2), objectMapper.getDeserializationContext(), testInstance);
        assertNull(deserializedObject2);

        property.set(testInstance, "testValue");
        assertEquals("testValue", property.get(testInstance));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json3 = mapper.writeValueAsString(testInstance);
        Object deserializedObject3 = mapper.readValue(json3, testInstance.getClass());
        assertEquals(testInstance, deserializedObject3);

        Converter<String, Integer> converter = mock(Converter.class);
        typeDeserializer = new CreatorProperty.TypeDeserializer();
        when(typeDeserializer.createContextualHandler(any(), any())).thenReturn(deserializer);
        when(deserializer.createContextual(null, objectMapper.getDeserializationContext())).thenReturn(converter);

        when(converter.convertNull(_any(), _eq(String.class), _any()))
                .thenReturn(null);
        when(converter.convert(_any(), _eq(String.class), _any()))
                .thenReturn(123);

        JsonParser parser = objectMapper.getFactory().createParser("\"testValue\"");
        Object deserializedObject4 = property.deserializeSetAndReturn(parser, objectMapper.getDeserializationContext(), testInstance);
        assertEquals(123, deserializedObject4);
    }
}