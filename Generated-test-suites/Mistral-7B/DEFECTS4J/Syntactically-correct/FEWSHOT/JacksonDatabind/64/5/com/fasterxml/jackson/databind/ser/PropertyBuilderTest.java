package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.AnnotatedMember;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class PropertyBuilderTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testGetDefaultBean() throws Exception {
        Object defaultBean = new Object() {
            public String getDefaultProperty() {
                return "default_value";
            }
        };
        BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object actual = propertyBuilder.getDefaultBean();
        Assert.assertEquals(defaultBean, actual);

        // Test when defaultBean is null
        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            Object getDefaultBean() {
                return null;
            }
        };
        actual = propertyBuilder.getDefaultBean();
        Assert.assertNull(actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        Object defaultBean = new Object() {
            public String getDefaultProperty() {
                return "default_value";
            }
        };
        BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember member = new AnonymousClassMember() {};
        String name = "defaultProperty";
        JavaType type = TypeFactory.type(String.class);
        Object actual = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals("default_value", actual);

        // Test when getPropertyDefaultValue throws an exception
        member = new AnonymousClassMember() {
            @Override
            public Object getValue(Object obj) throws IllegalAccessException, InvocationTargetException {
                throw new RuntimeException("Exception thrown");
            }
        };
        actual = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.fail("Expected IllegalArgumentException");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindSerializationType() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = new Annotated() {
            @JsonSerialize(using = StringSerializer.class)
            Class<?> clazz = String.class;
        };
        JavaType declaredType = TypeFactory.type(int.class);
        JavaType serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertNull(serializationType);

        a = new Annotated() {
            @JsonSerialize(using = StringSerializer.class)
            Class<?> clazz = String.class;
        };
        declaredType = TypeFactory.type(String.class);
        serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertEquals(TypeFactory.type(String.class), serializationType);

        a = new Annotated() {
            @JsonSerialize(using = ArrayList.class)
            Class<?> clazz = ArrayList.class;
        };
        declaredType = TypeFactory.type(ArrayList.class);
        serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertEquals(TypeFactory.typeCollection(ArrayList.class, Object.class), serializationType);

        a = new Annotated() {
            @JsonSerialize(using = ArrayList.class)
            Class<?> clazz = ArrayList.class;
        };
        declaredType = TypeFactory.type(ArrayList.class, Integer.class);
        serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertEquals(TypeFactory.typeCollection(ArrayList.class, Integer.class), serializationType);

        a = new Annotated() {
            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            Class<?> clazz = String.class;
        };
        declaredType = TypeFactory.type(String.class);
        serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertEquals(TypeFactory.type(String.class), serializationType);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        ObjectName nameTransformer = new ObjectName();
        SerializerProvider provider = new SerializerProvider() {
            @Override
            public SerializerFinder findValueSerializer(Class<?> type, BeanPropertyWriter writer) {
                return new SimpleSerializerFinder() {};
            }

            @Override
            public JsonSerializer<?> findContentSerializer(Class<?> contentType, TypeSerializer contentTypeSerializer) {
                return new StubSerializer();
            }
        };
        JavaType declaredType = TypeFactory.type(String.class);
        SerializerProvider.BaseSerializerfinder serializerFinder =provider.getSerializerFinder();
        SerializerProvider.BaseSerializerfinder contentTypeSerializerFinder = provider.getContentTypeSerializerFinder();
        AnnotatedMember am = new AnonymousClassMember() {};
        String propertyName = "propertyName";
        JsonSerializer<?> ser = new StubSerializer();
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        BeanPropertyDefinition propDef = new BeanPropertyDefinition() {};
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(provider, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(bpw);
    }

    static class AnonymousClassMember implements AnnotatedMember {
        @Override
        public Class<?> getAnnotatedClass() {
            return null;
        }

        @Override
        public Annotated getAnnotated() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public JavaType getGenericType() {
            return null;
        }

        @Override
        public JavaType getDeclaringClassType() {
            return null;
        }

        @Override
        public JavaType getType() {
            return null;
        }

        @Override
        public boolean isMember() {
            return false;
        }

        @Override
        public boolean isSynthetic() {
            return false;
        }

        @Override
        public Object getValue(Object bean) throws IllegalAccessException, InvocationTargetException {
            return null;
        }

        @Override
        public void setValue(Object bean, Object value) throws IllegalAccessException, InvocationTargetException {

        }

        @Override
        public int getModifiers() {
            return 0;
        }
    }

    static class StubSerializer implements JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        }

        @Override
        public void serializeValue(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        }
    }

    static class ObjectName implements NameTransformer {
        @Override
        public String transformName(String name, TypeSerializer typeSerializer) {
            return name;
        }
    }
}