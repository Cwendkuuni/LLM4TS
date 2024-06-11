import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        Annotations annotations = pb.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        BeanPropertyWriter b = pb.buildWriter(null, null, null, null, null, null, null, false);
        assertNotNull(b);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        JavaType type = pb.findSerializationType(null, false, null);
        assertNull(type);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        Object defaultValue = pb.getPropertyDefaultValue(null, null, null);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        Object defaultValue = pb.getDefaultValue(null);
        assertNull(defaultValue);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_throwWrapped() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        pb._throwWrapped(new Exception(), null, null);
    }

}