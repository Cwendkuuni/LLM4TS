import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter_DefaultInclusionNonDefault() {
        // Given
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(config, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Then
        assertNotNull(writer);
    }

    @Test
    public void testBuildWriter_DefaultInclusionNonAbsent() {
        // Given
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(config, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Then
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() {
        // Given
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        // When
        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        // Then
        assertNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Given
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        String name = "property";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        // When
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Then
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        // Given
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        JavaType type = new JavaType();

        // When
        Object defaultValue = propertyBuilder.getDefaultValue(type);

        // Then
        assertNull(defaultValue);
    }
}