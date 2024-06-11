import org.junit.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Annotations annotations = propertyBuilder.getClassAnnotations();

        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);

        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        JavaType javaType = propertyBuilder.findSerializationType(null, false, null);

        assertNotNull(javaType);
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object defaultBean = propertyBuilder.getDefaultBean();

        assertNotNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(null, null, null);

        assertNotNull(propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object defaultValue = propertyBuilder.getDefaultValue(null);

        assertNotNull(defaultValue);
    }
}