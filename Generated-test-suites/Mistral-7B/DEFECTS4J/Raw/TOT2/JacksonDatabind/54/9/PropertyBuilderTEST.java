import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.BeanPropertyWriterBase;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.AnnotatedMethod;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = new BeanDescription(MyPojo.class);
        annotationIntrospector = config.getAnnotationIntrospector();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations actual = propertyBuilder.getClassAnnotations();
        // Add test for annotations present in MyPojo class
        Annotations expected = new StandardAnnotations();
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildWriter_WhenDefaultUseStaticTypingIsFalse_ThenCorrectSerializationTypeIsUsed() throws JsonMappingException {
        // Given
        AnnotatedMethod am = Mockito.mock(AnnotatedMethod.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);

        MyPojo pojo = new MyPojo();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(MyPojo.class);

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, declaredType, ser, typeSer, contentTypeSer, am, false);

        // Then
        JavaType actualSerializationType = writer.getType();
        JavaType expectedSerializationType = TypeFactory.defaultInstance()
                .constructType(MyPojo.class);
        assertEquals(expectedSerializationType, actualSerializationType);
    }

    @Test
    public void testBuildWriter_WhenDefaultUseStaticTypingIsTrue_ThenSuperTypeIsUsed() throws JsonMappingException {
        // Given
        AnnotatedMethod am = Mockito.mock(AnnotatedMethod.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);

        MyPojo pojo = new MyPojo();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(MyPojo.class);
        JavaType superClassType = TypeFactory.defaultInstance().constructType(MySuperPojo.class);

        MySuperPojo superPojo = new MySuperPojo();

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, declaredType, ser, typeSer, contentTypeSer, am, true);

        // Then
        JavaType actualSerializationType = writer.getType();
        JavaType expectedSerializationType = superClassType;
        assertEquals(expectedSerializationType, actualSerializationType);

        // Test if the writer correctly gets the default bean and the property value from it
        Object defaultBean = propertyBuilder.getDefaultBean();
        String propertyName = "name";
        AnnotatedMember amProperty = Mockito.mock(AnnotatedMember.class);
        Object propertyValue = BeanUtil.findPropertyValue(defaultBean, propertyName);

        Object actualPropertyValue = writer.getValueProvider().getValue(amProperty);
        assertEquals(propertyValue, actualPropertyValue);
    }

    @Test
    public void testBuildWriter_WhenFindSerializationTypeReturnsNull_ThenDeclaredTypeIsUsed() throws JsonMappingException {
        // Given
        AnnotatedMethod am = Mockito.mock(AnnotatedMethod.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);

        MyPojo pojo = new MyPojo();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(MyPojo.class);

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, declaredType, ser, typeSer, contentTypeSer, am, false);

        // Then
        JavaType actualSerializationType = writer.getType();
        assertEquals(declaredType, actualSerializationType);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriter_WhenContentTypeSerIsNotNullButSerializationTypeHasNoContentType_ThenExceptionIsThrown() throws JsonMappingException {
        // Given
        AnnotatedMethod am = Mockito.mock(AnnotatedMethod.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);

        MyPojo pojo = new MyPojo();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(MyPojo.class);

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, declaredType, ser, typeSer, contentTypeSer, am, false);

        // Then
        // The exception should be thrown here
    }

    @Test
    public void testFindSerializationType_WhenAnnotationsForceType_ThenReturnTypeAnnotatedBySerializer() throws JsonMappingException {
        // Given
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(MyPojo.class);

        // When
        JavaType actualType = propertyBuilder.findSerializationType(a, false, declaredType);

        // Then
        // Add test for the expected type based on the annotations present in MyPojo class
        JavaType expectedType = TypeFactory.defaultInstance().constructType(MyExpectedType.class);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindSerializationType_WhenNoAnnotationsOrDeclaredTypeIsUsed_ThenDeclaredTypeIsUsed() throws JsonMappingException {
        // Given
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(MyPojo.class);

        // When
        JavaType actualType = propertyBuilder.findSerializationType(a, false, declaredType);

        // Then
        assertEquals(declaredType, actualType);
    }

    @Test
    public void testFindSerializationType_WhenAnnotationsForceSuperType_ThenSuperTypeIsUsed() throws JsonMappingException {
        // Given
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(MyPojo.class);
        JavaType superType = TypeFactory.defaultInstance().constructType(MySuperPojo.class);

        // When
        JavaType actualType = propertyBuilder.findSerializationType(a, false, declaredType);

        // Then
        assertEquals(superType, actualType);
    }

    @Test
    public void testGetPropertyDefaultValue_WhenPropertyHasDefaultValue_ThenReturnDefaultValue() {
        String propertyName = "name";
        MyPojo pojo = new MyPojo();
        Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, TypeFactory.defaultInstance().constructType(MyPojo.class));

        // Add test for the expected default value of the property "name" in MyPojo class
        String expectedDefaultValue = "defaultValue";
        assertEquals(expectedDefaultValue, actualDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WhenPropertyHasNoDefaultValue_ThenReturnNull() {
        String propertyName = "age";
        MyPojo pojo = new MyPojo();
        Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, TypeFactory.defaultInstance().constructType(MyPojo.class));

        assertNull(actualDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WhenPropertyIsStatic_ThenReturnDefaultValueOfClass() {
        String propertyName = "staticProperty";
        MyPojo pojo = new MyPojo();
        Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, TypeFactory.defaultInstance().constructType(MyPojo.class));

        // Add test for the expected default value of the static property "staticProperty" in MyPojo class
        String expectedDefaultValue = "staticDefaultValue";
        assertEquals(expectedDefaultValue, actualDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WhenPropertyDoesNotExist_ThenReturnNull() {
        String propertyName = "nonExistentProperty";
        MyPojo pojo = new MyPojo();
        Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, TypeFactory.defaultInstance().constructType(MyPojo.class));

        assertNull(actualDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WhenPropertyIsStaticAndDoesNotExist_ThenReturnNull() {
        String propertyName = "nonExistentStaticProperty";
        MyPojo pojo = new MyPojo();
        Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, TypeFactory.defaultInstance().constructType(MyPojo.class));

        assertNull(actualDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WhenPropertyIsAnnotatedWithJsonSerialize_ThenReturnDefaultValueAnnotatedByJsonSerialize() {
        String propertyName = "name";
        MyPojo pojo = new MyPojo();
        Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, TypeFactory.defaultInstance().constructType(MyPojo.class));

        // Add test for the expected default value of the property "name" annotated with JsonSerialize in MyPojo class
        MyPojoDefaultValue defaultValueAnnotation = new MyPojoDefaultValue();
        Object expectedDefaultValue = defaultValueAnnotation.value();
        assertEquals(expectedDefaultValue, actualDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WhenPropertyIsAnnotatedWithDefaultValue_ThenReturnDefaultValueAnnotatedByDefaultValue() {
        String propertyName = "name";
        MyPojo pojo = new MyPojo();
        Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, TypeFactory.defaultInstance().constructType(MyPojo.class));

        // Add test for the expected default value of the property "name" annotated with @JsonDefaultValue in MyPojo class
        String expectedDefaultValue = "defaultValue";
        assertEquals(expectedDefaultValue, actualDefaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_WhenPropertyDoesNotHaveDefaultValue_ThenThrowException() {
        String propertyName = "name";
        MyPojo pojo = new MyPojo();
        propertyBuilder.getPropertyDefaultValue(propertyName, null, TypeFactory.defaultInstance().constructType(MyPojo.class));
    }

    // Add more test cases for other methods in PropertyBuilder class
}