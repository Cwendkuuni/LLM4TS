import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import org.junit.Test;

public class AutomaticBeanTest {
    
    @Test
    public void testConfigure_typicalUseCase() {
        // Arrange
        AutomaticBean bean = new AutomaticBean();
        Configuration config = new Configuration();
        config.setAttribute("attribute1", "value1");
        config.setAttribute("attribute2", "value2");
        
        // Act
        try {
            bean.configure(config);
            
            // Assert
            // Check that attributes are correctly copied
            assertEquals("value1", bean.getConfiguration().getAttribute("attribute1"));
            assertEquals("value2", bean.getConfiguration().getAttribute("attribute2"));
        } catch (CheckstyleException ex) {
            fail("Exception thrown: " + ex.getMessage());
        }
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_errorScenario_nonExistingProperty() throws CheckstyleException {
        // Arrange
        AutomaticBean bean = new AutomaticBean();
        Configuration config = new Configuration();
        config.setAttribute("nonExistingProperty", "value");
        
        // Act
        bean.configure(config);
        
        // Assert
        // Expecting CheckstyleException to be thrown
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_errorScenario_invalidValue() throws CheckstyleException {
        // Arrange
        AutomaticBean bean = new AutomaticBean();
        Configuration config = new Configuration();
        config.setAttribute("attribute", "invalidValue");
        
        // Act
        bean.configure(config);
        
        // Assert
        // Expecting CheckstyleException to be thrown
    }
    
    @Test
    public void testContextualize_typicalUseCase() {
        // Arrange
        AutomaticBean bean = new AutomaticBean();
        Context context = new Context();
        context.setAttribute("attribute1", "value1");
        context.setAttribute("attribute2", "value2");
        
        // Act
        try {
            bean.contextualize(context);
            
            // Assert
            // Check that attributes are correctly copied
            assertEquals("value1", bean.getConfiguration().getAttribute("attribute1"));
            assertEquals("value2", bean.getConfiguration().getAttribute("attribute2"));
        } catch (CheckstyleException ex) {
            fail("Exception thrown: " + ex.getMessage());
        }
    }
    
    @Test
    public void testSetupChild_errorScenario_nonNullChildConfig() {
        // Arrange
        AutomaticBean bean = new AutomaticBean();
        Configuration childConfig = new Configuration();
        
        // Act
        try {
            bean.setupChild(childConfig);
            
            // Assert
            // Expecting CheckstyleException to be thrown
            fail("Expected CheckstyleException not thrown");
        } catch (CheckstyleException ex) {
            assertEquals(childConfig.getName() + " is not allowed as a child in " + bean.getConfiguration().getName(), ex.getMessage());
        }
    }

}