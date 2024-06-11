package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class CreatorPropertyTest {

    @Test
    public void testCreatorProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test empty CreatorProperty
        CreatorProperty emptyProperty = mapper.findProperty("emptyProperty", String.class);
        assertNull(emptyProperty);

        // Test CreatorProperty with annotations
        class MyClass {
            @JsonProperty("name")
            public String name;
            @JsonProperty("age")
            public int age;

            @JsonDeserialize(using = CreatorProperty.class)
            public CreatorProperty creatorProperty;
        }

        MyClass obj = mapper.readValue(new ObjectMapper().writeValueAsString(new MyClass()), MyClass.class);
        CreatorProperty creatorProperty = obj.creatorProperty;

        assertEquals("name", creatorProperty.getName());
        assertEquals(String.class, creatorProperty.getType());
        assertEquals(MyClass.class, creatorProperty.getWrapperName().getRawClass());
        assertEquals(TypeDeserializer.empty, creatorProperty.getTypeDeser());
        assertNull(creatorProperty._annotated);
        assertEquals(-1, creatorProperty._creatorIndex);
        assertNull(creatorProperty._injectableValueId);
        assertNull(creatorProperty._fallbackSetter);

        // Test CreatorProperty with injectable value
        class MyClassWithInjectable {
            @JsonProperty("name")
            public String name;
            @JsonProperty("age")
            public int age;

            @JsonDeserialize(using = CreatorProperty.class)
            public CreatorProperty creatorProperty;

            @JsonProperty("injectValue")
            @JsonInject
            public void setInjectValue(String injectValue) {
                this.injectValue = injectValue;
            }

            public String injectValue;
        }

        MyClassWithInjectable objWithInjectable = mapper.readValue(new ObjectMapper().writeValueAsString(new MyClassWithInjectable()), MyClassWithInjectable.class);
        CreatorProperty creatorPropertyWithInjectable = objWithInjectable.creatorProperty;

        assertEquals("name", creatorPropertyWithInjectable.getName());
        assertEquals(String.class, creatorPropertyWithInjectable.getType());
        assertEquals(MyClassWithInjectable.class, creatorPropertyWithInjectable.getWrapperName().getRawClass());
        assertEquals(TypeDeserializer.empty, creatorPropertyWithInjectable.getTypeDeser());
        assertNull(creatorPropertyWithInjectable._annotated);
        assertEquals(-1, creatorPropertyWithInjectable._creatorIndex);
        assertNotNull(creatorPropertyWithInjectable._injectableValueId);
        assertEquals("injectValue", creatorPropertyWithInjectable._injectableValueId.toString());
        assertNull(creatorPropertyWithInjectable._fallbackSetter);

        // Test CreatorProperty with _fallbackSetter
        class MyClassWithFallback {
            @JsonProperty("name")
            public String name;
            @JsonProperty("age")
            public int age;

            @JsonDeserialize(using = CreatorProperty.class)
            public CreatorProperty creatorProperty;

            @JsonProperty("value")
            public int value;

            @JsonSetter("value")
            public void setValue(int value) {
                this.value = value;
                _fallbackSetter = this::setValue;
            }
        }

        MyClassWithFallback objWithFallback = mapper.readValue(new ObjectMapper().writeValueAsString(new MyClassWithFallback()), MyClassWithFallback.class);
        CreatorProperty creatorPropertyWithFallback = objWithFallback.creatorProperty;

        assertEquals("name", creatorPropertyWithFallback.getName());
        assertEquals(Integer.class, creatorPropertyWithFallback.getType());
        assertEquals(MyClassWithFallback.class, creatorPropertyWithFallback.getWrapperName().getRawClass());
        assertEquals(TypeDeserializer.empty, creatorPropertyWithFallback.getTypeDeser());
        assertNull(creatorPropertyWithFallback._annotated);
        assertEquals(-1, creatorPropertyWithFallback._creatorIndex);
        assertNull(creatorPropertyWithFallback._injectableValueId);
        assertNotNull(creatorPropertyWithFallback._fallbackSetter);
        assertEquals(MyClassWithFallback.class, creatorPropertyWithFallback._fallbackSetter.getDeclaringClass());
        assertEquals("value", creatorPropertyWithFallback._fallbackSetter.getName());

        // Test CreatorProperty with TypeDeserializer
        class MyClassTypeDeserializer {
            @JsonProperty("name")
            public String name;
            @JsonProperty("age")
            public int age;

            @JsonDeserialize(using = CreatorProperty.class)
            public CreatorProperty creatorProperty;
            @JsonDeserialize(using = CustomTypeDeserializer.class)
            public CustomType type;

            public static class CustomTypeDeserializer extends StdTypeDeserializer<CustomType> {
                public CustomTypeDeserializer() {
                    this(null);
                }

                public CustomTypeDeserializer(Class<?> vc) {
                    this(null, vc);
                }

                protected CustomTypeDeserializer(DeserializationConfig config, Class<?> vc) {
                    super(ConfigurableContext.getInstance(), vc);
                }

                @Override
                public CustomType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return new CustomType();
                }
            }
        }

        MyClassTypeDeserializer objWithTypeDeserializer = mapper.readValue(new ObjectMapper().writeValueAsString(new MyClassTypeDeserializer()), MyClassTypeDeserializer.class);
        CreatorProperty creatorPropertyWithTypeDeserializer = objWithTypeDeserializer.creatorProperty;

        assertEquals("name", creatorPropertyWithTypeDeserializer.getName());
        assertEquals(CustomType.class, creatorPropertyWithTypeDeserializer.getType());
        assertEquals(MyClassTypeDeserializer.class, creatorPropertyWithTypeDeserializer.getWrapperName().getRawClass());
        assertNotNull(creatorPropertyWithTypeDeserializer.getTypeDeser());
        assertNotNull(creatorPropertyWithTypeDeserializer.getTypeDeser().getClass().getSuperclass());
        assertInstanceOf(StdTypeDeserializer.class, creatorPropertyWithTypeDeserializer.getTypeDeser());
        assertEquals(CustomType.class, creatorPropertyWithTypeDeserializer.getTypeDeser().getValueType());
        assertNull(creatorPropertyWithTypeDeserializer._annotated);
        assertEquals(-1, creatorPropertyWithTypeDeserializer._creatorIndex);
        assertNull(creatorPropertyWithTypeDeserializer._injectableValueId);
        assertNull(creatorPropertyWithTypeDeserializer._fallbackSetter);

        // Test CreatorProperty with AnnotatedParameter
        class MyClassAnnotatedParameter {
            @JsonProperty("name")
            public String name;
            @JsonProperty("age")
            public int age;

            @JsonDeserialize(using = CreatorProperty.class)
            public CreatorProperty creatorProperty;

            @JsonProperty("value")
            public int value;

            @JsonSetter("value")
            public void setValue(int value) {
                this.value = value;
                _fallbackSetter = this::setValue;
            }

            @JsonProperty("annotation")
            @JsonDeserialize(using = AnnotatedParameter.class)
            public AnnotatedParameter annotation;
        }

        MyClassAnnotatedParameter objWithAnnotatedParameter = mapper.readValue(new ObjectMapper().writeValueAsString(new MyClassAnnotatedParameter()), MyClassAnnotatedParameter.class);
        CreatorProperty creatorPropertyWithAnnotatedParameter = objWithAnnotatedParameter.creatorProperty;
        AnnotatedParameter annotatedParameter = objWithAnnotatedParameter.annotation;

        assertEquals("name", creatorPropertyWithAnnotatedParameter.getName());
        assertEquals(Integer.class, creatorPropertyWithAnnotatedParameter.getType());
        assertEquals(MyClassAnnotatedParameter.class, creatorPropertyWithAnnotatedParameter.getWrapperName().getRawClass());
        assertNull(creatorPropertyWithAnnotatedParameter.getTypeDeser());
        assertNull(creatorPropertyWithAnnotatedParameter._annotated);
        assertEquals(-1, creatorPropertyWithAnnotatedParameter._creatorIndex);
        assertNull(creatorPropertyWithAnnotatedParameter._injectableValueId);
        assertNotNull(creatorPropertyWithAnnotatedParameter._fallbackSetter);
        assertEquals(MyClassAnnotatedParameter.class, creatorPropertyWithAnnotatedParameter._fallbackSetter.getDeclaringClass());
        assertEquals("value", creatorPropertyWithAnnotatedParameter._fallbackSetter.getName());
        assertEquals(MyClassAnnotatedParameter.class, annotatedParameter.getAnnotated().getClass());
        assertEquals(Annotation.class, annotatedParameter.getAnnotated().getClass().getInterfaces()[0]);
        assertEquals(MyClassAnnotatedParameter.class, annotatedParameter.getMember().getDeclaringClass());
        assertEquals("value", annotatedParameter.getMember().getName());
    }
}

class CustomType {
    // empty class
}