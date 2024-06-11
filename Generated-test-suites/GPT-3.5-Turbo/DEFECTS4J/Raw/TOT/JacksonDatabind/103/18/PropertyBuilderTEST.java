import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        try {
            BeanPropertyWriter writer = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            assertNotNull(writer);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType() {
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        PropertyBuilder builder = new PropertyBuilder();
        Annotated a = new Annotated();

        try {
            JavaType serializationType = builder.findSerializationType(a, true, new JavaType());
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder();
        String name = "propertyName";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        try {
            Object defaultValue = builder.getPropertyDefaultValue(name, member, type);
            assertNull(defaultValue);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder();
        JavaType type = new JavaType();

        Object defaultValue = builder.getDefaultValue(type);
        assertNull(defaultValue);
    }

}