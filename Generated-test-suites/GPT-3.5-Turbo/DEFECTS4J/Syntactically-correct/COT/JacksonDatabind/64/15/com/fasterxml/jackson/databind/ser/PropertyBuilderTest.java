
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Test
    public void testGetClassAnnotations() {
        propertyBuilder = new PropertyBuilder(null, null);
        assertNotNull(propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter() {
        propertyBuilder = new PropertyBuilder(null, null);
        assertNotNull(propertyBuilder.buildWriter(null, null, null, null, null, null, null, false));
    }

    @Test
    public void testFindSerializationType() {
        propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.findSerializationType(null, false, null));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getPropertyDefaultValue(null, null, null));
    }

    @Test
    public void testGetDefaultValue() {
        propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultValue(null));
    }

    @Test
    public void testThrowWrapped() {
        propertyBuilder = new PropertyBuilder(null, null);
        Exception e = new Exception();
        assertNull(propertyBuilder._throwWrapped(e, null, null));
    }
}
