import static org.junit.Assert.*;
import org.junit.*;

public class AutomaticBeanTest {

    @Test
    public void testConfigure_typicalUseCase() {
        // Arrange
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration config = new Configuration();
        config.setAttribute("attribute1", "value1");
        config.setAttribute("attribute2", "value2");

        // Act
        try {
            automaticBean.configure(config);
        } catch (CheckstyleException ex) {
            fail("Exception thrown: " + ex.getMessage());
        }

        // Assert
        // Verify that the attributes are correctly copied to the bean
        assertEquals("value1", automaticBean.getConfiguration().getAttribute("attribute1"));
        assertEquals("value2", automaticBean.getConfiguration().getAttribute("attribute2"));
    }

    @Test
    public void testConfigure_missingPropertyError() {
        // Arrange
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration config = new Configuration();
        config.setAttribute("missingProperty", "value");

        // Act
        try {
            automaticBean.configure(config);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException ex) {
            // Assert
            assertEquals("Property 'missingProperty' in module [module name] does not exist, please check the documentation", ex.getMessage());
        }
    }

    @Test
    public void testConfigure_illegalValueError() {
        // Arrange
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration config = new Configuration();
        config.setAttribute("attribute", "illegalValue");

        // Act
        try {
            automaticBean.configure(config);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException ex) {
            // Assert
            assertEquals("illegal value 'illegalValue' for property 'attribute' of module [module name]", ex.getMessage());
        }
    }

    @Test
    public void testContextualize_typicalUseCase() {
        // Arrange
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();
        context.add("key1", "value1");
        context.add("key2", "value2");

        // Act
        try {
            automaticBean.contextualize(context);
        } catch (CheckstyleException ex) {
            fail("Exception thrown: " + ex.getMessage());
        }

        // Assert
        // Verify that the context attributes are correctly copied to the bean
        assertEquals("value1", automaticBean.getConfiguration().getAttribute("key1"));
        assertEquals("value2", automaticBean.getConfiguration().getAttribute("key2"));
    }

    @Test
    public void testSetupChild_childConfigNotAllowed() {
        // Arrange
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = new Configuration();

        // Act
        try {
            automaticBean.setupChild(childConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException ex) {
            // Assert
            assertEquals("childConfig is not allowed as a child in [configuration name]", ex.getMessage());
        }
    }
}