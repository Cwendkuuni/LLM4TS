
import org.junit.Test;
import static org.junit.Assert.*;

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
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        try {
            builder.findSerializationType(null, false, null);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNull(builder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNull(builder.getPropertyDefaultValue("", null, null));
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNull(builder.getDefaultValue(null));
    }

    @Test
    public void testThrowWrapped() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        try {
            builder._throwWrapped(new Exception(), "", null);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }
}
