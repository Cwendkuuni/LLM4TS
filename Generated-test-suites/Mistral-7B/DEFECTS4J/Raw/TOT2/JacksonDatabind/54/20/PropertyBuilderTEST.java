import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyPOJO.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();

        // Test default inclusion
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter actual = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertEquals("Expected BeanPropertyWriter instance", actual, propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping));

        // Test with custom inclusion
        JsonInclude.Include inclusion = JsonInclude.Include.NON_EMPTY;
        JsonInclude.Value inclV = new JsonInclude.Value(inclusion);
        _defaultInclusion = new JsonInclude.Value(_defaultInclusion.getValue(), inclV);
        PropertyBuilder propertyBuilderWithCustomInclusion = new PropertyBuilder(config, beanDesc);

        BeanPropertyWriter actualWithCustomInclusion = propertyBuilderWithCustomInclusion.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotEquals("Expected different BeanPropertyWriter instances", actual, actualWithCustomInclusion);
    }

    // Add test cases for other methods and edge cases here
}