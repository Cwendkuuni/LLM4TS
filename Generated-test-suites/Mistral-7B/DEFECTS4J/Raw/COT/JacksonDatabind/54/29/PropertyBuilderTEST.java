package com.fasterxml.jackson.databind.ser; // Make sure the package is correct

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @BeforeClass
    public static void setupClass() {
        // Set up any static initialization or configuration if needed
    }

    @AfterClass
    public static void tearDownClass() {
        // Clean up any static resources or configurations if needed
    }

    @Before
    public void setup() {
        // Set up any instance variables or configurations for each test case
    }

    @After
    public void tearDown() {
        // Clean up any instance resources or configurations after each test case
    }

    

    @Test
    public void testPropertyBuilderConstructor() {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDefinition();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDefinition();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDefinition();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<String> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithInvalidType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDefinition();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", new TypeReference<>() {}.getType());
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<String> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        try {
            propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);

        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);

        JavaType primaryType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(declaredType, primaryType);

        JsonSerialize.Typing serializationTyping = Mockito.mock(JsonSerialize.Typing.class);
        JsonSerialize serializationAnnotation = Mockito.mock(JsonSerialize.class);
        when(a.getAnnotation(JsonSerialize.class)).thenReturn(serializationAnnotation);
        when(serializationAnnotation.getTyping()).thenReturn(serializationTyping);
        when(serializationTyping.getStaticType()).thenReturn(TypeFactory.defaultInstance().constructType(Integer.class));

        JavaType secondaryType = propertyBuilder.findSerializationType(a, true, declaredType);
        assertNotNull(secondaryType);
        assertFalse(secondaryType.isEqualTo(declaredType));
        assertEquals(Integer.class, secondaryType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithInvalidType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);

        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);

        try {
            propertyBuilder.findSerializationType(a, false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        String name = "testProperty";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        Object defaultBean = new Object();
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(propertyDefaultValue);

        Object defaultValue = "testValue";
        when(member.getValue(defaultBean)).thenReturn(defaultValue);

        propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals(defaultValue, propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);

        type = TypeFactory.defaultInstance().constructType(Integer.class);
        defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals(0, defaultValue);

        type = TypeFactory.defaultInstance().constructType(String[].class);
        defaultValue = propertyBuilder.getDefaultValue(type);
        assertArrayEquals(new Object[0], defaultValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDefaultValueWithInvalidType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        JavaType type = TypeFactory.defaultInstance().constructType(new TypeReference<>() {}.getType());
        propertyBuilder.getDefaultValue(type);
        fail("Expected IllegalStateException");
    }

    @Test
    public void testThrowWrapped() {
        Exception e = new RuntimeException("Test exception");
        String propName = "testProperty";
        Object defaultBean = new Object();

        Object thrownObject = propertyBuilder._throwWrapped(e, propName, defaultBean);
        assertEquals("Failed to get property 'testProperty' of default Object@7856d222 instance", thrownObject.toString());
    }

    @Test
    public void testThrowWrappedWithError() {
        Exception e = new IllegalArgumentException("Test exception");
        String propName = "testProperty";
        Object defaultBean = new Object();

        try {
            propertyBuilder._throwWrapped(e, propName, defaultBean);
            fail("Expected Error to be thrown");
        } catch (Error e2) {
            // Expected
        }
    }

    
}