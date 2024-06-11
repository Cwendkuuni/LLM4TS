package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import java.lang.reflect.*;
import java.util.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() throws JsonMappingException, IllegalAccessException, InstantiationException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field1"), null);
        JavaType declaredType = TypeFactory.type(MyClass.class, Object.class);
        JavaType expectedType = TypeFactory.type(MyClass.class, String.class);
        JavaType actualType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindSerializationType_NoAnnotation() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field2"), null);
        JavaType declaredType = TypeFactory.type(MyClass.class, Object.class);
        JavaType actualType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(actualType);
    }

    @Test
    public void testFindSerializationType_DifferentClass() throws JsonMappingException, InstantiationException, IllegalAccessException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field3"), null);
        JavaType declaredType = TypeFactory.type(MyClass.class, MyOtherClass.class);
        JavaType actualType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(actualType);
    }

    @Test
    public void testFindSerializationType_SameClassButDifferentType() throws JsonMappingException, InstantiationException, IllegalAccessException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field4"), null);
        JavaType declaredType = TypeFactory.type(MyClass.class, String[].class);
        JavaType expectedType = TypeFactory.type(MyClass.class, String[].class);
        JavaType actualType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindSerializationType_StaticTypingDisabled() throws JsonMappingException, InstantiationException, IllegalAccessException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field1"), null);
        JavaType declaredType = TypeFactory.type(MyClass.class, Object.class);
        JavaType actualType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(actualType);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field1"), null);
        SerializerProvider prov = new SerializerProvider();
        JsonSerializer<Object> serializer = prov.serializerFor(MyClass.class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, null, null, serializer, typeSer, contentTypeSer, am, false);
        assertNull(actualWriter);
    }

    @Test
    public void testBuildWriter_NullSerializer() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field1"), null);
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, null, null, null, null, null, am, false);
        assertNull(actualWriter);
    }

    @Test
    public void testBuildWriter_NullPropertyDefinition() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field1"), null);
        SerializerProvider prov = new SerializerProvider();
        JsonSerializer<Object> serializer = prov.serializerFor(MyClass.class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, null, null, serializer, typeSer, contentTypeSer, am, false);
        assertNull(actualWriter);
    }

    @Test
    public void testBuildWriter_DifferentDeclaredType() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field1"), null);
        SerializerProvider prov = new SerializerProvider();
        JsonSerializer<Object> serializer = prov.serializerFor(MyClass.class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        JavaType declaredType = TypeFactory.type(MyClass.class, MyOtherClass.class);
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, null, null, serializer, typeSer, contentTypeSer, am, false);
        assertNull(actualWriter);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field1"), null);
        SerializerProvider prov = new SerializerProvider();
        JsonSerializer<Object> serializer = prov.serializerFor(MyClass.class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, null, null, serializer, typeSer, contentTypeSer, am, false);
        assertNotNull(actualWriter);
    }

    @Test
    public void testBuildWriter_SuppressedNulls() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field1"), null);
        SerializerProvider prov = new SerializerProvider();
        JsonSerializer<Object> serializer = prov.serializerFor(MyClass.class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        Boolean suppressNulls = true;
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, null, null, serializer, typeSer, contentTypeSer, am, suppressNulls);
        assertNotNull(actualWriter);
        // Test that nulls are suppressed
        Object valueToSuppress = null;
        assertEquals(BeanPropertyWriter.MARKER_FOR_NULL, actualWriter.getNullValue());
        assertFalse(actualWriter.isSuppressNulls());
        // Test that empty collections are suppressed when WriteEmptyJsonArrays is false
        Object valueToSuppress2 = BeanPropertyWriter.MARKER_FOR_EMPTY;
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, actualWriter.getNullValueForEmpty());
        assertFalse(actualWriter.isSuppressNullsForEmpty());
    }

    @Test
    public void testBuildWriter_SuppressedEmptyCollections() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        AnnotatedMember am = new AnnotatedClassMember(new FieldInfo(MyClass.class, "field1"), null);
        SerializerProvider prov = new SerializerProvider();
        JsonSerializer<Object> serializer = prov.serializerFor(MyClass.class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        Boolean suppressNulls = false;
        Boolean suppressEmptyCollections = true;
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, null, null, serializer, typeSer, contentTypeSer, am, suppressNulls);
        assertNotNull(actualWriter);
        // Test that nulls are not suppressed
        Object valueToSuppress = null;
        assertNull(actualWriter.getNullValue());
        assertFalse(actualWriter.isSuppressNulls());
        // Test that empty collections are suppressed
        Object valueToSuppress2 = BeanPropertyWriter.MARKER_FOR_EMPTY;
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, actualWriter.getNullValueForEmpty());
        assertTrue(actualWriter.isSuppressNullsForEmpty());
    }

    @Test
    public void testPropertyDefaultValue() throws JsonMappingException, InstantiationException, IllegalAccessException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        AnnotatedMember member = new AnnotatedField(new FieldInfo(MyClass.class, "field1"));
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("field1", member, TypeFactory.type(MyClass.class, Integer.class));
        assertEquals(0, propertyDefaultValue);
    }

    @Test
    public void testGetDefaultBean() throws InstantiationException, IllegalAccessException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        AnnotatedMember member = new AnnotatedField(new FieldInfo(MyClass.class, "field1"));
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("field1", member, TypeFactory.type(MyClass.class, Integer.class));
        assertEquals(0, propertyDefaultValue);

        MyClass defaultInstance = beanDesc.instantiateBean(true);
        propertyBuilder._defaultBean = defaultInstance;
        defaultBean = propertyBuilder.getDefaultBean();
        assertEquals(defaultInstance, defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultBean_NoDefaultConstructor() throws InstantiationException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithNoDefaultConstructor.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
        propertyBuilder.getDefaultBean();
    }

    @Test
    public void testGetDefaultValue() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);

        Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.type(MyClass.class, Integer.class));
        assertEquals(0, defaultValue);
    }

    public static class MyClass {
        public int field1;
        public String field2;
        public MyOtherClass field3;
        public List<String> field4;

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public int field5;

        @JsonSerialize(using = CustomSerializer.class)
        public int field6;

        @JsonProperty("customField")
        public int customField;

        public static class CustomSerializer extends JsonSerializer<MyClass> {
            @Override
            public void serialize(MyClass value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeNumber(value.field5);
            }
        }
    }

    public static class MyOtherClass {
    }

    public static class MyClassWithNoDefaultConstructor {
        public MyClassWithNoDefaultConstructor() {
            throw new IllegalStateException("Cannot create instance of this class");
        }
    }
}