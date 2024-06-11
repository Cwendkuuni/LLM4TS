import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
    }

    @Test
    public void testBuildWriter_ValidArgs() throws JsonProcessingException {
        // Given
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        // When
        BeanPropertyWriter actual = propertyBuilder.buildWriter(mock(SerializerProvider.class), beanDesc, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Then
        assert(actual != null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriter_NullContentTypeSer() throws JsonProcessingException {
        // Given
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        // When
        propertyBuilder.buildWriter(mock(SerializerProvider.class), beanDesc, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testFindSerializationType_ValidAnnotationsAndDeclaredType() throws JsonMappingException {
        // Given
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);

        // When
        JavaType actual = propertyBuilder.findSerializationType(a, true, declaredType);

        // Then
        assertEquals(declaredType, actual);
    }

    @Test
    public void testFindSerializationType_CustomSerializationTypeAnnotation() throws JsonMappingException {
        // Given
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType customSerializationType = TypeFactory.defaultInstance().constructType(Integer.class);

        // When
        JavaType actual = propertyBuilder.findSerializationType(a, true, declaredType);

        // Then
        assertNull(actual);
        verify(a).hasAnnotation(JsonSerialize.With.class);
        verify(a).hasAnnotation(JsonSerialize.class);
        verify(a).hasAnnotation(JsonSerialize.SerializerByType.class);
        verify(a).hasAnnotation(JsonSerialize.SerializerByType.class.getDeclaredField("value"));
        verifyZeroInteractions(a.getRawType().getDeclaredMethods());
        verify(a.getRawType().getDeclaredFields());
        verify(a.getRawType().getDeclaredConstructors());

        Object value = a.getRawType().getDeclaredField("value").get(a);
        assertEquals(customSerializationType.getTypeName(), value.toString());
        assertEquals(customSerializationType, propertyBuilder.findSerializationType(a, true, declaredType));
    }

    // Add more test cases for other methods and edge cases
}