package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.BeanPropertyFinder;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, new ClassInfo(Foo.class));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = new TestAnnotatedMember(Foo.class, "field1");
        JavaType declaredType = new TypeReference<Foo>() {}.getType();
        JavaType serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        Assert.assertEquals(Foo.class, serializationType.getRawClass());

        am = new TestAnnotatedMember(Foo.class, "field2");
        declaredType = new TypeReference<Foo>() {}.getType();
        serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        Assert.assertEquals(Bar.class, serializationType.getRawClass());

        am = new TestAnnotatedMember(Foo.class, "field3");
        declaredType = new TypeReference<Foo>() {}.getType();
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNull(serializationType);

        // Test with array type
        am = new TestAnnotatedMember(Foo[].class, "field1");
        declaredType = new TypeReference<Foo[]>() {}.getType();
        serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        Assert.assertEquals(Foo[].class, serializationType.getRawClass());

        am = new TestAnnotatedMember(Foo[].class, "field2");
        declaredType = new TypeReference<Foo[]>() {}.getType();
        serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        Assert.assertEquals(Bar[].class, serializationType.getRawClass());
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, new ClassInfo(Foo.class));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider();
        prov.setConfig(config);

        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new PropertyName("field1"), Foo.class, new TypeReference<String>() {}, null, null);
        JavaType declaredType = new TypeReference<String>() {}.getType();
        JsonSerializer<String> ser = new StubJsonSerializer<>();
        TypeSerializer typeSer = new StubTypeSerializer<>();
        TypeSerializer contentTypeSer = new StubTypeSerializer<>();
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, null, false);
        Assert.assertNotNull(bpw);

        propDef = new BeanPropertyDefinition(new PropertyName("field1"), Foo.class, new TypeReference<Integer>() {}, null, null);
        JavaType intType = new TypeReference<Integer>() {}.getType();
        bpw = propertyBuilder.buildWriter(prov, propDef, intType, ser, typeSer, contentTypeSer, null, false);
        Assert.assertNotNull(bpw);

        propDef = new BeanPropertyDefinition(new PropertyName("field2"), Foo.class, new TypeReference<Bar>() {}.getType(), null, null);
        JavaType barType = new TypeReference<Bar>() {}.getType();
        bpw = propertyBuilder.buildWriter(prov, propDef, barType, ser, typeSer, contentTypeSer, null, false);
        Assert.assertNotNull(bpw);

        propDef = new BeanPropertyDefinition(new PropertyName("field3"), Foo.class, null, null, null);
        bpw = propertyBuilder.buildWriter(prov, propDef, null, ser, typeSer, contentTypeSer, null, false);
        Assert.assertNull(bpw);

        // Test with array type
        propDef = new BeanPropertyDefinition(new PropertyName("field1"), Foo[].class, new TypeReference<String[]>() {}.getType(), null, null);
        String[] strings = new String[]{"foo", "bar"};
        bpw = propertyBuilder.buildWriter(prov, propDef, strings.getClass(), ser, typeSer, contentTypeSer, null, false);
        Assert.assertNotNull(bpw);

        propDef = new BeanPropertyDefinition(new PropertyName("field2"), Foo[].class, new TypeReference<Bar[]>() {}.getType(), null, null);
        Bar[] bars = new Bar[]{new Bar(), new Bar()};
        bpw = propertyBuilder.buildWriter(prov, propDef, bars.getClass(), ser, typeSer, contentTypeSer, null, false);
        Assert.assertNotNull(bpw);
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, new ClassInfo(Foo.class));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Assert.assertNotNull(propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, new ClassInfo(Foo.class));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Assert.assertNotNull(propertyBuilder._annotationIntrospector);
    }

    @Test
    public void testGetDefaultBean() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, new ClassInfo(Foo.class));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);

        config.setCanOverrideAccessModifiers(true);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
        Assert.assertEquals(Foo.class, defaultBean.getClass());
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, new ClassInfo(Foo.class));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("field1", new TestAnnotatedMember(Foo.class, "field1"), new TypeReference<String>() {}.getType());
        Assert.assertNull(defaultValue);

        if (defaultBean instanceof Foo) {
            Foo defaultFoo = (Foo) defaultBean;
            defaultValue = propertyBuilder.getPropertyDefaultValue("field1", new TestAnnotatedMember(Foo.class, "field1"), new TypeReference<String>() {}.getType());
            Assert.assertEquals("default", defaultValue);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPropertyDefaultValueWithNullDefaultBean() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, new ClassInfo(Foo.class));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = null;
        propertyBuilder._defaultBean = defaultBean;
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("field1", new TestAnnotatedMember(Foo.class, "field1"), new TypeReference<String>() {}.getType());
    }

    @Test
    public void testGetDefaultValue() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        Object defaultValue = propertyBuilder.getDefaultValue(new TypeReference<String>() {}.getType());
        Assert.assertEquals("", defaultValue);

        defaultValue = propertyBuilder.getDefaultValue(new TypeReference<Integer>() {}.getType());
        Assert.assertEquals(0, defaultValue);

        defaultValue = propertyBuilder.getDefaultValue(new TypeReference<List<String>>() {}.getType());
        Assert.assertEquals(Arrays.asList(), defaultValue);

        defaultValue = propertyBuilder.getDefaultValue(new TypeReference<List<Integer>>() {}.getType());
        Assert.assertEquals(new ArrayList<>(), defaultValue);
    }

    @Test(expected = JsonMappingException.class)
    public void test_throwWrapped() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        propertyBuilder._throwWrapped(new RuntimeException("Test"), "field1", new Foo());
    }

    private static class Foo {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        String field1 = "default";
        @JsonSerialize(using = BarSerializer.class)
        Bar field2;
        transient String field3;

        public Foo() {
            field2 = new Bar();
        }

        public static class BarSerializer extends StubJsonSerializer<Bar> {
        }

        public static class Bar {
            String bar;
        }
    }

    private static class TestAnnotatedMember implements AnnotatedMember {
        private final Class<?> _clazz;
        private final String _name;
        private final Method _getter;

        public TestAnnotatedMember(Class<?> clazz, String name) {
            _clazz = clazz;
            _name = name;
        }

        @Override
        public Class<?> getAnnotated() {
            return _clazz;
        }

        @Override
        public String getName() {
            return _name;
        }

        @Override
        public Annotated getParent() {
            return null;
        }

        @Override
        public Annotation[] getValueAnnotations() {
            return new Annotation[0];
        }

        @Override
        public MapperFeature[] getSerializationConfigFeatures() {
            return new MapperFeature[0];
        }

        @Override
        public Annotation[] getSerializationConfigOverrides() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getDeserializationConfigOverrides() {
            return new Annotation[0];
        }

        @Override
        public BeanPropertyDefinition findPropertyDefinition() {
            return new BeanPropertyDefinition(new PropertyName(_name), _clazz, null, null, null);
        }

        @Override
        public Constructor<?> getConstructor() {
            return null;
        }

        @Override
        public Method getGetter() {
            try {
                return _clazz.getMethod("get" + Character.toUpperCase(_name.charAt(0)) + _name.substring(1));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Method[] getMethods() {
            return new Method[0];
        }

        @Override
        public Map<String, Method> getMethodsMap() {
            return new HashMap<>();
        }

        @Override
        public Class<?> getRawType() {
            return _clazz;
        }

        @Override
        public Class<?>[] getParameterTypes() {
            return new Class[0];
        }

        @Override
        public int getModifiers() {
            return 0;
        }

        @Override
        public boolean isSynthetic() {
            return false;
        }

        @Override
        public boolean isBridge() {
            return false;
        }

        @Override
        public boolean isTransient() {
            return _clazz.isAnnotationPresent(Transient.class);
        }

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annType) {
            return _clazz.isAnnotationPresent(annType);
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> annType) {
            return _clazz.getAnnotation(annType);
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
            return null;
        }

        @Override
        public void setAccessible(boolean accessible) {
        }

        @Override
        public Object invoke(Object obj) throws IllegalAccessException, InvocationTargetException {
            try {
                return getGetter().invoke(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestAnnotatedMember that = (TestAnnotatedMember) o;
            return _name.equals(that._name) && _clazz.equals(that._clazz);
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + (_name != null ? _name.hashCode() : 0);
            result = 31 * result + (_clazz != null ? _clazz.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "TestAnnotatedMember{" +
                    "name='" + _name + '\'' +
                    ", clazz=" + _clazz +
                    '}';
        }
    }

    private static class StubJsonSerializer<T> extends JsonSerializer<T> {

    }

    private static class StubTypeSerializer implements TypeSerializer {

    }
}