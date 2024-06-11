package com.example.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.WriterMarker;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDescription(Object.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        assertNotNull(propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter_Null() {
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testBuildWriter_Default() {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class, null, false, null);
        JavaType declaredType = TypeFactory.type(String[].class);
        Object defaultValue = "";
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, null, null, null, null, false);
        assertNotNull(bpw);
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());
    }

    @Test
    public void testFindSerializationType_Default() {
        Annotated a = new Annotated() {
            @Override
            public String getName() {
                return "testMethod";
            }
        };
        JavaType declaredType = TypeFactory.type(String.class);
        JavaType secondary = TypeFactory.type(String[].class);
        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(result);
    }

    @Test
    public void testFindSerializationType_AnnotationPresent() {
        Annotated a = new Annotated() {
            @Override
            public String getName() {
                return "testMethod";
            }

            @JsonSerialize(using = StdSerializer.class)
            public static class TestSerializer {}
        };
        JavaType declaredType = TypeFactory.type(String.class);
        JavaType serializationType = TypeFactory.type(PropertyBuilderTest.TestSerializer.class);
        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNotNull(result);
        assertSame(serializationType, result);
    }

    @Test
    public void testFindSerializationType_StaticTyping() {
        Annotated a = new Annotated() {
            @Override
            public String getName() {
                return "testMethod";
            }
        };
        JavaType declaredType = TypeFactory.type(PropertyBuilderTest.TestClass.class);
        JavaType serializationType = TypeFactory.type(PropertyBuilderTest.TestClass.StaticInnerClass.class);
        JsonSerialize.Typing typing = JsonSerialize.Typing.STATIC;
        JavaType result = propertyBuilder.findSerializationType(a, true, declaredType);
        assertNotNull(result);
        assertSame(serializationType, result);
    }

    @Test
    public void testFindSerializationType_SuperType() {
        Annotated a = new Annotated() {
            @Override
            public String getName() {
                return "testMethod";
            }

            @JsonSerialize(using = PropertyBuilderTest.TestSerializer.class)
            public static class TestSerializer extends Object {}
        };
        JavaType declaredType = TypeFactory.type(Object.class);
        JavaType serializationType = TypeFactory.type(PropertyBuilderTest.TestSerializer.class);
        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNotNull(result);
        assertSame(serializationType, result);
    }

    @Test
    public void testFindSerializationType_TypeMismatch() {
        Annotated a = new Annotated() {
            @Override
            public String getName() {
                return "testMethod";
            }

            @JsonSerialize(using = PropertyBuilderTest.TestSerializer.class)
            public static class TestSerializer extends Integer {}
        };
        JavaType declaredType = TypeFactory.type(String.class);
        try {
            propertyBuilder.findSerializationType(a, false, declaredType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetDefaultBean() {
        assertNull(propertyBuilder.getDefaultBean());

        propertyBuilder = new PropertyBuilder(config, new BeanDescription(PropertyBuilderTest.TestClass.class));
        propertyBuilder.getDefaultBean();
        assertNotNull(propertyBuilder._defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue_Primitive() {
        JavaType type = TypeFactory.type(int.class);
        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", null, type);
        assertEquals(0, result);
    }

    @Test
    public void testGetPropertyDefaultValue_Wrapper() {
        JavaType type = TypeFactory.type(Integer.class);
        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", null, type);
        assertNotNull(result);
        assertEquals(0, ((Number) result).intValue());
    }

    @Test
    public void testGetPropertyDefaultValue_EmptyString() {
        JavaType type = TypeFactory.type(String.class);
        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", null, type);
        assertEquals("", result);
    }

    @Test
    public void testGetPropertyDefaultValue_ContainerType() {
        JavaType type = TypeFactory.type(ArrayList.class);
        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", null, type);
        assertSame(WriterMarker.EMPTY_ARRAY, result);
    }

    @Test
    public void testGetPropertyDefaultValue_ReferenceType() {
        JavaType type = TypeFactory.type(Object.class);
        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", null, type);
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue_PropertyAndGlobalDefaults() {
        BeanDescription beanDesc = new BeanDescription(PropertyBuilderTest.TestClassWithDefaults.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", new AnnotatedMember() {}, TypeFactory.type(String.class));
        assertEquals("", result);

        propertyBuilder = new PropertyBuilder(config, new BeanDescription(Object.class));
        result = propertyBuilder.getPropertyDefaultValue("testProperty", new AnnotatedMember() {}, TypeFactory.type(String.class));
        assertNull(result);
    }

    @Test
    public void testGetDefaultValue_Primitive() {
        JavaType type = TypeFactory.type(int.class);
        Object result = propertyBuilder.getDefaultValue(type);
        assertEquals(0, result);
    }

    @Test
    public void testGetDefaultValue_Wrapper() {
        JavaType type = TypeFactory.type(Integer.class);
        Object result = propertyBuilder.getDefaultValue(type);
        assertNotNull(result);
        assertEquals(0, ((Number) result).intValue());
    }

    @Test
    public void testGetDefaultValue_String() {
        JavaType type = TypeFactory.type(String.class);
        Object result = propertyBuilder.getDefaultValue(type);
        assertEquals("", result);
    }

    @Test
    public void testGetDefaultValue_ContainerType() {
        JavaType type = TypeFactory.type(ArrayList.class);
        Object result = propertyBuilder.getDefaultValue(type);
        assertSame(WriterMarker.EMPTY_ARRAY, result);
    }

    @Test
    public void testGetDefaultValue_ReferenceType() {
        JavaType type = TypeFactory.type(Object.class);
        Object result = propertyBuilder.getDefaultValue(type);
        assertNull(result);
    }

    @Test
    public void testGetDefaultValue_CustomConverter() {
        JavaType type = TypeFactory.type(PropertyBuilderTest.TestCustomClass.class);
        Converter<String, PropertyBuilderTest.TestCustomClass> converter = new StdConverter<String, PropertyBuilderTest.TestCustomClass>() {
            @Override
            public PropertyBuilderTest.TestCustomClass convert(String value, JavaType propertyType) {
                return new PropertyBuilderTest.TestCustomClass();
            }
        };
        SerializerProvider.Base serializerProvider = new SerializerProvider() {};
        SerializerProvider prov = new SerializerProvider(serializerProvider, new JsonSerializer<?>[] { converter });
        Object result = propertyBuilder.getDefaultValue(type);
        assertNotNull(result);
        assertInstanceOf(PropertyBuilderTest.TestCustomClass.class, result);
    }

    @Test
    public void testThrowWrapped_Null() {
        try {
            propertyBuilder._throwWrapped(null, "testProperty", new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testThrowWrapped_Exception() {
        Throwable e = new RuntimeException("Test Exception");
        try {
            propertyBuilder._throwWrapped(e, "testProperty", new Object());
            fail("Expected RuntimeException");
        } catch (RuntimeException ex) {
            assertEquals("Test Exception", ex.getMessage());
        }
    }

    public static class TestClass {
        public static class StaticInnerClass {}
    }

    public static class TestClassWithDefaults {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public String testProperty;
    }

    public static class TestCustomClass {}

    public static class TestSerializer extends StdSerializer<PropertyBuilderTest.TestCustomClass> {}

    public static class Annotated {}

}