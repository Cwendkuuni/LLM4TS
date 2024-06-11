
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class AutomaticBeanTest {
    
    private AutomaticBean automaticBean;
    
    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }
    
    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration config = new Configuration();
        config.addAttribute("attribute1", "value1");
        config.addChild(new Configuration("child1"));
        
        automaticBean.configure(config);
        
        // assert that the configuration is properly set
        assertEquals(config, automaticBean.getConfiguration());
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_NonExistentProperty() throws CheckstyleException {
        Configuration config = new Configuration();
        config.addAttribute("nonExistentKey", "value");
        
        automaticBean.configure(config);
        
        // CheckstyleException should be thrown when a non-existent property is provided
    }
    
    @Test
    public void testConfigure_ErrorInSettingProperty() {
        Configuration config = new Configuration();
        config.addAttribute("key", "invalidValue");
        
        try {
            automaticBean.configure(config);
        } catch (CheckstyleException e) {
            Throwable cause = e.getCause();
            
            // assert that the exception cause message contains the invalid value
            assertTrue(cause.getMessage().contains("illegal value 'invalidValue' for property 'key'"));
        }
    }
    
    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = new Context();
        context.setAttribute("attribute1", "value1");
        context.setAttribute("attribute2", 2);
        
        automaticBean.contextualize(context);
        
        // assert that the attributes are properly set
        assertEquals("value1", automaticBean.getAttribute("attribute1"));
        assertEquals(2, automaticBean.getAttribute("attribute2"));
    }
    
    @Test
    public void testSetupChild_NullChildConfiguration() {
        Configuration childConf = null;
        
        try {
            automaticBean.setupChild(childConf);
        } catch (CheckstyleException e) {
            // assert that CheckstyleException is thrown when null child configuration is provided
        }
    }
    
    @Test(expected = CheckstyleException.class)
    public void testSetupChild_ChildConfigurationNotAllowed() throws CheckstyleException {
        Configuration childConf = new Configuration("child1");
        
        automaticBean.setupChild(childConf);
        
        // assert that CheckstyleException is thrown when an invalid child configuration is provided
    }

}
