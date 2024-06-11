package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.impl.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testDefaultConstruction() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        assertNotNull(builder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConfig() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder builder = new PropertyBuilder(null, beanDesc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBeanDesc() {
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder builder = new PropertyBuilder(config, null);
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        beanDesc.setClassAnnotations(new Annotations());
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        assertEquals(beanDesc.getClassAnnotations(), builder.getClassAnnotations());
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Class<?> testClass = TestClass.class;
        SerializationConfig config = new SerializationConfig();
        JsonFactory jsonFactory = new JsonFactory();
        BeanDescription beanDesc = BeanDescription.createDescription(jsonFactory, testClass);
        ObjectMapper mapper = new ObjectMapper(config);
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        JavaType declaredType = mapper.getTypeFactory().constructType(testClass);
        JavaType serializationType = builder.findSerializationType(beanDesc.findProperty("name"), true, declaredType);
        assertEquals(TestClassWithName.class, serializationType.getRawClass());

        // Test with custom serialization
        JsonSerializer<TestClassWithName> serializer = new TestClassWithNameSerializer();
        AnnotatedMember am = null;
        PropertyBuilder builder2 = new PropertyBuilder(config, beanDesc);
        JavaType serializationType2 = builder2.findSerializationType(am, true, declaredType);
        assertNull(serializationType2);

        TestClassWithNameSerializer annotatedSerializer = new TestClassWithNameSerializer();
        AnnotatedMember annotatedMember = new AnnotatedGenericMethod(TestClassWithNameSerializer.class, "serialize", TestClassWithName.class);
        annotatedMember.setDeclaredAnnotations(new JsonSerialize.With(annotatedSerializer));
        JavaType serializationType3 = builder2.findSerializationType(annotatedMember, true, declaredType);
        assertEquals(TestClassWithNameSerializer.class, serializationType3.getRawType());
    }

    @Test
    public void testGetDefaultBean() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = builder.getDefaultBean();
        assertNull(defaultBean);

        beanDesc.setDefaultInstance(new TestClass());
        builder = new PropertyBuilder(config, beanDesc);
        defaultBean = builder.getDefaultBean();
        assertEquals(TestClass.class, defaultBean.getClass());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(TestClassWithName.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = builder.getDefaultBean();
        Object nameDefault = builder.getPropertyDefaultValue("name", null, Object.class);
        assertEquals("", nameDefault);

        // Test with custom getter
        TestClassWithName defaultBean2 = new TestClassWithName();
        defaultBean2.setName("defaultName");
        beanDesc.setDefaultInstance(defaultBean2);
        builder = new PropertyBuilder(config, beanDesc);
        Object nameDefault2 = builder.getPropertyDefaultValue("name", null, Object.class);
        assertEquals("defaultName", nameDefault2);

        // Test with custom setter
        TestClassWithName defaultBean3 = new TestClassWithName();
        defaultBean3.setName("defaultName");
        TestClassWithNameSetter setter = new TestClassWithNameSetter();
        setter.setInstance(defaultBean3);
        beanDesc.setDefaultInstance(setter);
        builder = new PropertyBuilder(config, beanDesc);
        Object nameDefault3 = builder.getPropertyDefaultValue("name", null, Object.class);
        assertEquals("defaultName", nameDefault3);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = new SerializerProvider();
        ObjectMapper mapper = new ObjectMapper(config);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(TestClass.class, "name", Object.class, null, null, null, null, null, null);
        JavaType declaredType = mapper.getTypeFactory().constructType(TestClass.class);
        JsonSerializer<?> ser = mapper.findValueSerializer(TestClass.class, "name");
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = true;
        BeanPropertyWriter writer = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationTypeWithNullDeclaredType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        JavaType actualType = builder.findSerializationType(null, true, null);
        assertNull(actualType);
    }

    @Test
    public void testSuppressionOfNullValues() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = new SerializerProvider();
        ObjectMapper mapper = new ObjectMapper(config);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(TestClass.class, "nullValue", Boolean.TYPE, null, null, null, null, null, null);
        JavaType declaredType = mapper.getTypeFactory().constructType(TestClass.class);
        JsonSerializer<?> ser = mapper.findValueSerializer(TestClass.class, "nullValue");
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = true;
        Object nullValue = null;
        BeanPropertyWriter writer = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNull(writer.getNullValue());

        nullValue = Boolean.TRUE;
        writer = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNull(writer.getNullValue());

        nullValue = Boolean.FALSE;
        writer = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertEquals(nullValue, writer.getNullValue());
    }
}

class TestClass {
}

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class TestClassWithName {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultName() {
        return "defaultName";
    }
}

class TestClassWithNameSetter extends TestClassWithName {
    private TestClassWithName instance;

    public void setInstance(TestClassWithName instance) {
        this.instance = instance;
    }

    @JsonIgnore
    public String getName() {
        return instance.getName();
    }

    @JsonProperty
    public void setName(String name) {
        instance.setName(name);
    }
}

class TestClassWithNameSerializer extends JsonSerializer<TestClassWithName> {
    @Override
    public void serialize(TestClassWithName value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString("TestClassWithNameSerializer");
    }
}