package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * Tests for {@link PropertyBuilder} class.
 */
public class PropertyBuilderTest {

    private static Object NO_DEFAULT_MARKER = Boolean.FALSE;

    @Test
    public void testPropertyBuilder() throws IOException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        AnnotationIntrospector annotationIntrospector = new StandardAnnotationIntrospector();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = new StandardSerializerProvider();

        // Test default values and inclusion
        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, false);
        assertEquals(JsonInclude.Include.ALWAYS, propertyWriter._inclusion);
        assertFalse(propertyWriter._useRealPropertyDefaults);

        // Test custom inclusion
        JsonInclude.Include customInclusion = JsonInclude.Include.CUSTOM;
        AnnotatedMember am = new AnnotatedMethod(MyClass.class, "myProperty");
        am.setRawType(String.class);
        JsonInclude inclusionAnnotation = new JsonInclude(customInclusion);
        am.setAnnotation(JsonInclude.class, inclusionAnnotation);

        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyWriter = propertyBuilder.buildWriter(prov, null, null, null, null, null, am, false);
        assertEquals(customInclusion, propertyWriter._inclusion);

        // Test use of real property defaults
        Object defaultBean = new MyClass();
        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            protected Object getDefaultBean() {
                return defaultBean;
            }
        };
        propertyWriter = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, false);
        Object defaultValue = propertyWriter.getValueToSuppress();
        assertEquals("default", defaultValue);

        // Test null suppression
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyWriter = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, true);
        assertNull(propertyWriter.getValueToSuppress());

        // Test serialization type determination
        JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
        JavaType secondaryType = new TypeFactory().constructFromAnnotation(JsonSerialize.class, MyClass.class, "myProperty", String[].class);
        assertEquals(secondaryType, propertyBuilder.findSerializationType(new AnnotatedMethod(MyClass.class, "myProperty"), false, javaType).getRawType());

        // Test exception handling
        Method method = MyClass.class.getMethod("getMyProperty");
        AnnotatedElement ae = new AnnotatedMethod(MyClass.class, "getMyProperty");
        ae.setElement(method);
        try {
            propertyBuilder = new PropertyBuilder(config, beanDesc);
            propertyWriter = propertyBuilder.buildWriter(prov, null, null, null, null, null, ae, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal concrete-type annotation for method 'getMyProperty': class [Ljava.lang.String; not a super-type of (declared) class class com.example.MyClass", e.getMessage());
        }
    }

    /**
     * Helper class for testing.
     */
    static class MyClass {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private String myProperty;

        @JsonSerialize(using = StringSerializer.class)
        public String getMyProperty() {
            return myProperty;
        }

        public void setMyProperty(String myProperty) {
            this.myProperty = myProperty;
        }

        public static class StringSerializer extends StdSerializer<String> {
            public StringSerializer() {
                this(null);
            }

            public StringSerializer(Class<String> t) {
                super(t);
            }
        }
    }
}