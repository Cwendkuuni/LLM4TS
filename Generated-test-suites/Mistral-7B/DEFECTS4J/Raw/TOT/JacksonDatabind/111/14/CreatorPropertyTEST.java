package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testCreatorProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test creating a CreatorProperty directly
        JavaType type = mapper.getTypeFactory().constructType(CreatorProperty.class);
        CreatorProperty property = mapper.readerForType(type).readValue(getClass().getResource("/data.json"));
        assertEquals("name1", property.getName());
        assertEquals(String.class, property.getType());
        assertNull(property.getFallbackSetter());
        assertFalse(property.isIgnorable());
        assertNull(property._injectableValueId);
        assertNull(property._annotated);
        assertEquals(0, property._creatorIndex);

        // Test creating a CreatorProperty using a constructor
        JavaType constructorType = mapper.getTypeFactory().constructType(CreatorProperty.class, PropertyName.class, JavaType.class, PropertyName.class, TypeDeserializer.class, Annotations.class, AnnotatedParameter.class, int.class, Object.class, PropertyMetadata.class);
        AnnotatedParameter param = new AnnotatedParameter(mapper.getAnnotationIntrospector().findParameterAnnotation(MyClass.class, "param1"));
        property = mapper.readerForType(constructorType).readValue(getClass().getResource("/data.json"));
        assertEquals("name2", property.getName());
        assertEquals(String.class, property.getType());
        assertNotNull(property._annotated);
        assertEquals(1, property._creatorIndex);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertFalse(property.isIgnorable());

        // Test creating a CreatorProperty using a static factory method
        JavaType staticFactoryType = mapper.getTypeFactory().constructType(CreatorProperty.class, PropertyName.class, JavaType.class, PropertyName.class, TypeDeserializer.class, Annotations.class, String[].class, int.class, Object.class, PropertyMetadata.class);
        String[] annotations = new String[]{"Annotation1", "Annotation2"};
        property = mapper.readerForType(staticFactoryType).readValue(getClass().getResource("/data.json"));
        assertEquals("name3", property.getName());
        assertEquals(Integer.class, property.getType());
        assertNotNull(property._annotated);
        assertEquals(2, property._creatorIndex);
        assertNotNull(property._injectableValueId);
        assertNotNull(property._fallbackSetter);
        assertFalse(property.isIgnorable());

        // Test injectable value
        MyClass bean = new MyClass();
        property = new CreatorProperty(mapper.getAnnotationIntrospector().findPropertyAnnotation(MyClass.class, "property1"), String.class, PropertyName.class, null, null, null, 0, null, null);
        property.setIgnorable(true);
        property.setFallbackSetter(new CreatorProperty(mapper.getAnnotationIntrospector().findPropertyAnnotation(MyClass.class, "property2"), String.class, PropertyName.class, null, null, null, 1, null, null));
        Object value = mapper.readValue(getClass().getResource("/data.json"), MyClass.class);
        assertNotNull(value);
        assertEquals("injectedValue1", property.findInjectableValue(mapper.getDeserializationContext(), value).toString());
        property.inject(mapper.getDeserializationContext(), value);
        assertEquals("injectedValue1", value.getProperty1());

        // Test ignore property
        property = new CreatorProperty(mapper.getAnnotationIntrospector().findPropertyAnnotation(MyClass.class, "property3"), String.class, PropertyName.class, null, null, null, 0, null, null);
        property.setIgnorable(true);
        property.setFallbackSetter(new CreatorProperty(mapper.getAnnotationIntrospector().findPropertyAnnotation(MyClass.class, "property4"), String.class, PropertyName.class, null, null, null, 1, null, null));
        value = mapper.readValue(getClass().getResource("/data.json"), MyClass.class);
        assertNotNull(value);
        try {
            property.deserializeAndSet(mapper.getFactory().createParser(getClass().getResource("/data.json").openStream()), mapper.getDeserializationContext(), value);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test missing setter
        property = new CreatorProperty(mapper.getAnnotationIntrospector().findPropertyAnnotation(MyClass.class, "property5"), String.class, PropertyName.class, null, null, null, 0, null, null);
        property.setIgnorable(false);
        property.setFallbackSetter(new CreatorProperty(mapper.getAnnotationIntrospector().findPropertyAnnotation(MyClass.class, "property6"), String.class, PropertyName.class, null, null, null, 1, null, null));
        value = mapper.readValue(getClass().getResource("/data.json"), MyClass.class);
        assertNotNull(value);
        try {
            property.deserializeAndSet(mapper.getFactory().createParser(getClass().getResource("/data.json").openStream()), mapper.getDeserializationContext(), value);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test null value
        property = new CreatorProperty(mapper.getAnnotationIntrospector().findPropertyAnnotation(MyClass.class, "property7"), String.class, PropertyName.class, null, null, null, 0, null, null);
        property.setIgnorable(false);
        property.setFallbackSetter(new CreatorProperty(mapper.getAnnotationIntrospector().findPropertyAnnotation(MyClass.class, "property8"), String.class, PropertyName.class, null, null, null, 1, null, null));
        property.markAsIgnorable();
        value = mapper.readValue(getClass().getResource("/data.json"), MyClass.class);
        assertNotNull(value);
        try {
            property.deserializeAndSet(mapper.getFactory().createParser(getClass().getResource("/data.json").openStream()), mapper.getDeserializationContext(), value);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test unknown type
        property = new CreatorProperty(mapper.getAnnotationIntrospector().findPropertyAnnotation(MyClass.class, "property9"), Double.class, PropertyName.class, null, null, null, 0, null, null);
        property.setIgnorable(false);
        property.setFallbackSetter(new CreatorProperty(mapper.getAnnotationIntrospector().findPropertyAnnotation(MyClass.class, "property10"), String.class, PropertyName.class, null, null, null, 1, null, null));
        value = mapper.readValue(getClass().getResource("/data.json"), MyClass.class);
        assertNotNull(value);
        try {
            property.deserializeAndSet(mapper.getFactory().createParser(getClass().getResource("/data.json").openStream()), mapper.getDeserializationContext(), value);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    static class MyClass {
        @JsonProperty(value = "property1")
        private String property1;

        @JsonProperty(value = "property2")
        private String property2;

        @JsonProperty(value = "property3")
        @JsonIgnore
        private String property3;

        @JsonProperty(value = "property4")
        private String property4;

        @JsonProperty(value = "property5")
        private Integer property5;

        @JsonProperty(value = "property6")
        private Integer property6;

        @JsonProperty(value = "property7")
        private String property7;

        @JsonProperty(value = "property8")
        private Integer property8;

        @JsonProperty(value = "property9")
        private Double property9;

        @JsonProperty(value = "property10")
        private String property10;

        public String getProperty1() { return property1; }
        public void setProperty1(String value) { property1 = value; }

        public String getProperty2() { return property2; }
        public void setProperty2(String value) { property2 = value; }

        public String getProperty3() { return property3; }

        public String getProperty4() { return property4; }

        public Integer getProperty5() { return property5; }
        public void setProperty5(Integer value) { property5 = value; }

        public Integer getProperty6() { return property6; }
        public void setProperty6(Integer value) { property6 = value; }

        public String getProperty7() { return property7; }

        public Integer getProperty8() { return property8; }

        public Double getProperty9() { return property9; }
    }
}