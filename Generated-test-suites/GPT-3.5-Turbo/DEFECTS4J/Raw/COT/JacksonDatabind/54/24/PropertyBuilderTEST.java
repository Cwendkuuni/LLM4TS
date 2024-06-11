
import org.junit.Test;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder.getClassAnnotations();
    }

    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        } catch (Exception e) {
            // Handle exception
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.findSerializationType(null, false, null);
        } catch (Exception e) {
            // Handle exception
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder.getPropertyDefaultValue(null, null, null);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder.getDefaultValue(null);
    }

    @Test
    public void testThrowWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._throwWrapped(null, null, null);
    }
}
