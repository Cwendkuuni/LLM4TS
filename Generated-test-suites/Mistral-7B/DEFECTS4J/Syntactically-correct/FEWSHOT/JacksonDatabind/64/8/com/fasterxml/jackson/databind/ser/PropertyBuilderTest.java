package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        // Test with no annotations
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        JavaType declaredType = mapper.getTypeFactory().constructType(MyClass.class);
        AnnotatedMember am = PropertyTestUtil.createAnnotatedMember(beanDesc, "myField");
        JavaType result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(result);

        // Test with JsonInclude annotation
        MyClass myClass = new MyClass();
        myClass.setMyAnnotation(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
        beanDesc = new BeanDescription(MyClass.class);
        declaredType = mapper.getTypeFactory().constructType(MyClass.class);
        am = PropertyTestUtil.createAnnotatedMember(beanDesc, "myField");
        JsonInclude annotation = am.getAnnotation(JsonInclude.class);
        result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(MyClass.class, result.getRawClass());

        // Test with JsonSerialize annotation
        MyClass subClass = new MySubClass();
        beanDesc = new BeanDescription(subClass.getClass());
        declaredType = mapper.getTypeFactory().constructType(subClass.getClass());
        am = PropertyTestUtil.createAnnotatedMember(beanDesc, "subMyField");
        JsonSerialize annotation2 = am.getAnnotation(JsonSerialize.class);
        result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(subClass.getClass(), result.getRawClass());

        // Test with JsonInclude and JsonSerialize annotations
        MyClass thirdClass = new MyThirdClass();
        beanDesc = new BeanDescription(thirdClass.getClass());
        declaredType = mapper.getTypeFactory().constructType(thirdClass.getClass());
        am = PropertyTestUtil.createAnnotatedMember(beanDesc, "thirdMyField");
        JsonInclude includeAnnotation = am.getAnnotation(JsonInclude.class);
        JsonSerialize serializeAnnotation = am.getAnnotation(JsonSerialize.class);
        result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(thirdClass.getClass(), result.getRawClass());
    }

    @Test
    public void testGetDefaultBean() throws JsonMappingException {
        // Test with a default constructor
        BeanDescription beanDesc = new BeanDescription(MyClassWithDefaultConstructor.class);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);

        // Test with a non-default constructor
        beanDesc = new BeanDescription(MyClassWithoutDefaultConstructor.class);
        defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        // Test with a non-instantiatable class
        beanDesc = new BeanDescription(MyNonInstantiatableClass.class);
        defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithDefaultValue.class);
        JavaType type = mapper.getTypeFactory().constructType(MyClassWithDefaultValue.class);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("defaultField", null, type);
        assertEquals(0, defaultValue.hashCode());

        MyClassWithDefaultValue instance = new MyClassWithDefaultValue();
        AnnotatedMember am = PropertyTestUtil.createAnnotatedMember(beanDesc, "defaultField");
        defaultValue = propertyBuilder.getPropertyDefaultValue("defaultField", am, type);
        assertEquals(instance.getDefaultField(), defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithException() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithExceptions.class);
        JavaType type = mapper.getTypeFactory().constructType(MyClassWithExceptions.class);
        propertyBuilder.getPropertyDefaultValue("defaultField", null, type);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JavaType declaredType = mapper.getTypeFactory().constructType(MyClass.class);
        String propertyName = "myField";
        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition(propertyName);
        JsonSerializer<?> serializer = PropertyTestUtil.createTestSerializer(MyClass.class, propertyName);
        TypeSerializer typeSerializer = mapper.getTypeSerializer(MyClass.class);
        TypeSerializer contentTypeSerializer = mapper.getTypeSerializer(int[].class);
        AnnotatedMember am = PropertyTestUtil.createAnnotatedMember(beanDesc, propertyName);
        boolean defaultUseStaticTyping = true;
        BeanPropertyWriter result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping);

        // Test with default useStaticTyping
        assertEquals(MyClass.class, result.getType().getRawClass());
        assertEquals(MyClass.class, result.getDeclaredType().getRawClass());
        assertEquals(propertyName, result.getName());
        assertEquals(am, result.getMember());
        assertEquals(beanDesc.getClassAnnotations(), result.getAnnotationValues());
        assertEquals(serializer, result.getSerializer());
        assertEquals(typeSerializer, result.getTypeSerializer());
        assertNull(result.getValueSerializer());
        assertNull(result.getNullSerializer());

        // Test with custom nullSerializer
        Annotations classAnnotations = PropertyTestUtil.createTestAnnotations(MyClass.class);
        result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping, classAnnotations);
        assertNotNull(result.getNullSerializer());

        // Test with custom unwrappingNameTransformer
        NameTransformer nameTransformer = new NameTransformer() {
            @Override
            public String transform(String name, TypeSerializer typeSerializer) {
                return "transformed_" + name;
            }
        };
        result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping, null, null, nameTransformer);
        assertEquals("transformed_myField", result.getName());
    }

    @Test
    public void testFindSerializationTypeWithContainerType() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyContainerClass.class);
        JavaType declaredType = mapper.getTypeFactory().constructType(MyContainerClass.class);
        AnnotatedMember am = PropertyTestUtil.createAnnotatedMember(beanDesc, "myList");
        JavaType result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(List.class, result.getRawClass());
    }

    @Test
    public void testBuildWriterWithContainerType() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyContainerClass.class);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JavaType declaredType = mapper.getTypeFactory().constructType(MyContainerClass.class);
        String propertyName = "myList";
        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition(propertyName);
        JsonSerializer<?> serializer = PropertyTestUtil.createTestSerializer(MyContainerClass.class, propertyName);
        TypeSerializer typeSerializer = mapper.getTypeSerializer(MyContainerClass.class);
        TypeSerializer contentTypeSerializer = mapper.getTypeSerializer(List.class);
        AnnotatedMember am = PropertyTestUtil.createAnnotatedMember(beanDesc, propertyName);
        boolean defaultUseStaticTyping = true;
        BeanPropertyWriter result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping);

        assertEquals(MyContainerClass.class, result.getType().getRawClass());
        assertEquals(List.class, result.getDeclaredType().getRawClass());
        assertEquals(propertyName, result.getName());
        assertEquals(am, result.getMember());
        assertEquals(beanDesc.getClassAnnotations(), result.getAnnotationValues());
        assertEquals(serializer, result.getSerializer());
        assertEquals(typeSerializer, result.getTypeSerializer());
        assertNotNull(result.getValueSerializer());
        assertNull(result.getValueSerializer().getValueSerializer());
        assertNull(result.getValueSerializer().getNullSerializer());
    }

    @Test
    public void testBuildWriterWithNullValueSerializer() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JavaType declaredType = mapper.getTypeFactory().constructType(MyClass.class);
        String propertyName = "myField";
        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition(propertyName);
        JsonSerializer<?> serializer = PropertyTestUtil.createTestSerializer(MyClass.class, propertyName);
        TypeSerializer typeSerializer = mapper.getTypeSerializer(MyClass.class);
        TypeSerializer contentTypeSerializer = mapper.getTypeSerializer(int.class);
        AnnotatedMember am = PropertyTestUtil.createAnnotatedMember(beanDesc, propertyName);
        boolean defaultUseStaticTyping = true;
        JsonSerializer<?> nullSerializer = PropertyTestUtil.createTestSerializer(MyNullSerializer.class, propertyName);
        BeanPropertyWriter result = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping, null, null, null, nullSerializer);

        assertEquals(MyClass.class, result.getType().getRawClass());
        assertEquals(MyClass.class, result.getDeclaredType().getRawClass());
        assertEquals(propertyName, result.getName());
        assertEquals(am, result.getMember());
        assertEquals(beanDesc.getClassAnnotations(), result.getAnnotationValues());
        assertEquals(serializer, result.getSerializer());
        assertEquals(typeSerializer, result.getTypeSerializer());
        assertEquals(nullSerializer, result.getNullSerializer());
    }
}

class MyClass {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int myField;
}

class MySubClass extends MyClass {
    @JsonSerialize(using = MySubSerializer.class)
    private int subMyField;
}

class MyThirdClass {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private MyOtherClass thirdMyField;
}

class MyOtherClass {
}

class MyClassWithDefaultConstructor {
    public MyClassWithDefaultConstructor() {
    }
}

class MyClassWithoutDefaultConstructor {
    private MyClassWithoutDefaultConstructor() {
    }
}

class MyNonInstantiatableClass {
    private MyNonInstantiatableClass() {
        throw new RuntimeException("Can't instantiate this class");
    }
}

class MyClassWithExceptions {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String defaultField;

    @JsonSetter(value = "defaultField")
    public void setDefaultField(String s) {
        throw new IllegalArgumentException("Can't set defaultField");
    }
}

class MyContainerClass {
    @JsonSerialize(using = MyContainerSerializer.class)
    private List<Integer> myList;
}

class MyNullSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNull();
    }
}

class PropertyTestUtil {
    public static AnnotatedMember createAnnotatedMember(BeanDescription beanDesc, String propertyName) {
        return ReflectionUtil.findAnnotatedMember(beanDesc, propertyName);
    }

    public static Annotations createTestAnnotations(Class<?> clazz) {
        return new Annotations();
    }

    public static JsonSerializer<?> createTestSerializer(Class<?> clazz, String propertyName) {
        return new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(propertyName);
            }
        };
    }
}