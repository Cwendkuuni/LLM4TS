import static org.junit.Assert.*;
import org.junit.Test;

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
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.findSerializationType(null, false, null);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(null, null, null);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultValue = propertyBuilder.getDefaultValue(null);
        assertNull(defaultValue);
    }

    @Test
    public void testThrowWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder._throwWrapped(new Exception(), null, null);
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }
}