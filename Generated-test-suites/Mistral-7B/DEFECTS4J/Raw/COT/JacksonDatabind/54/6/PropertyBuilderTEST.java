package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedMember;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() throws Exception {
        JavaClass javaClass = new JavaClass(MyPojo.class);
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = config.findBeanDescription(javaClass);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    // Basic test cases for public methods

    @Test
    public void testGetClassAnnotations() {
        Annotations actual = propertyBuilder.getClassAnnotations();
        Annotations expected = beanDesc.getClassAnnotations();
        assertEquals("Class annotations should be the same", expected, actual);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        // TODO: Implement test case for buildWriter method
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        // TODO: Implement test case for findSerializationType method
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull("getDefaultBean should not be null", defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "name";
        AnnotatedMember member = new AnnotatedElement() {
            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[]{new JsonProperty()};
            }

            @Override
            public AnnotatedType getAnnotatedType() {
                return new SimpleType("String");
            }

            @Override
            public Annotated getDeclaringElement() {
                return null;
            }
        };
        JavaType type = TypeFactory.unresolvedType(String.class);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals("Property default value should be empty string", "", propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = TypeFactory.unresolvedType(int.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("Default value for primitive int type should be 0", 0, defaultValue);
        type = TypeFactory.unresolvedType(MyPojo.class);
        defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull("Default value for non-primitive type should be null", defaultValue);
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void test_throwWrapped_WhenPropNameIsNull() {
        String propName = null;
        Object defaultBean = new Object();
        propertyBuilder._throwWrapped(new IOException("Test"), propName, defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_throwWrapped_WhenDefaultBeanIsNull() {
        String propName = "name";
        Object defaultBean = null;
        propertyBuilder._throwWrapped(new IOException("Test"), propName, defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_throwWrapped_WhenCauseIsError() {
        String propName = "name";
        Object defaultBean = new Object();
        propertyBuilder._throwWrapped(new Error("Test"), propName, defaultBean);
    }

    @Test(expected = RuntimeException.class)
    public void test_throwWrapped_WhenCauseIsRuntimeException() {
        String propName = "name";
        Object defaultBean = new Object();
        propertyBuilder._throwWrapped(new RuntimeException("Test"), propName, defaultBean);
    }

    @Test
    public void testFindSerializationType_WhenAnnotatedTypeIsSubtype() throws JsonMappingException {
        MyChildPojo myChildPojo = new MyChildPojo();
        JavaClass javaClass = new JavaClass(MyChildPojo.class);
        Annotated a = new AnnotatedElement() {
            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[]{new JsonSerialize()};
            }

            @Override
            public AnnotatedType getAnnotatedType() {
                return new SimpleType(javaClass.getRawClass());
            }

            @Override
            public Annotated getDeclaringElement() {
                return null;
            }
        };
        JavaType declaredType = TypeFactory.unresolvedType(MyChildPojo.class);
        JsonSerialize.Typing typing = JsonSerialize.Typing.STATIC;
        JavaType type = propertyBuilder.findSerializationType(a, true, declaredType);
        assertEquals("Type should be the same as declared type", declaredType, type);
    }

    @Test
    public void testFindSerializationType_WhenAnnotatedTypeIsSupertype() throws JsonMappingException {
        JavaClass javaClass = new JavaClass(MySuperPojo.class);
        Annotated a = new AnnotatedElement() {
            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[]{new JsonSerialize()};
            }

            @Override
            public AnnotatedType getAnnotatedType() {
                return new SimpleType(javaClass.getRawClass());
            }

            @Override
            public Annotated getDeclaringElement() {
                return null;
            }
        };
        JavaType declaredType = TypeFactory.unresolvedType(MyPojo.class);
        JavaType type = propertyBuilder.findSerializationType(a, true, declaredType);
        assertTrue("Type should be a super type of declared type", declaredType.isAssignableFrom(type));
    }

    @Test
    public void testGetDefaultValue_WhenTypeIsArray() {
        JavaType type = TypeFactory.unresolvedType(int[].class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertArrayEquals("Default value for array type should be empty array", new int[0], defaultValue);
    }

    @Test
    public void testGetDefaultValue_WhenTypeIsMap() {
        JavaType type = TypeFactory.unresolvedType(HashMap.class, String.class, Integer.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("Default value for Map type should be an empty map", new HashMap<>(), defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WhenPropertyIsStatic() throws Exception {
        String name = "staticName";
        AnnotatedMember member = new AnnotatedElement() {
            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[]{new JsonProperty()};
            }

            @Override
            public AnnotatedType getAnnotatedType() {
                return new SimpleType(MyStaticPojo.class);
            }

            @Override
            public Annotated getDeclaringElement() {
                return null;
            }
        };
        JavaType type = TypeFactory.unresolvedType(MyStaticPojo.class);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull("Property default value should be null for static properties", propertyDefaultValue);
    }

    // Nested classes

    public static class MyPojo {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class MyChildPojo extends MyPojo {
    }

    public static class MySuperPojo {
    }

    public static class StaticPojo {
        public static String staticName;
    }
}