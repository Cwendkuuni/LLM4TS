import static org.junit.Assert.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNotNull(builder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        try {
            builder.buildWriter(null, null, null, null, null, null, null, false);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        try {
            builder.findSerializationType(null, false, null);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        try {
            builder.getPropertyDefaultValue(null, null, null);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        try {
            builder.getDefaultValue(null);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testThrowWrapped() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        try {
            builder._throwWrapped(new Exception(), null, null);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

}