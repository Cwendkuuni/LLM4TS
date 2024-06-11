import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder(null, null);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;

        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "testProperty";
        AnnotatedMember member = null;
        JavaType type = null;

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = null;

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }

}