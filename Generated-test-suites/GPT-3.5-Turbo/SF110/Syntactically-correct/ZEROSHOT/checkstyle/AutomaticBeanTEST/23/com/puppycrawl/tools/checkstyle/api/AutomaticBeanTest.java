import static org.junit.Assert.*;
import org.junit.Test;

public class AutomaticBeanTest {
    
    @Test
    public void testConfigure() {
        // create test instance of AutomaticBean
        AutomaticBean bean = new AutomaticBean();
        
        // create test configuration
        Configuration config = new Configuration();
        config.setAttribute("attribute1", "value1");
        config.setAttribute("attribute2", "value2");
        
        try {
            // test configure method
            bean.configure(config);
            
            // check if attributes are copied correctly
            assertEquals("value1", bean.getConfiguration().getAttribute("attribute1"));
            assertEquals("value2", bean.getConfiguration().getAttribute("attribute2"));
        } catch (CheckstyleException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testContextualize() {
        // create test instance of AutomaticBean
        AutomaticBean bean = new AutomaticBean();
        
        // create test context
        Context context = new Context();
        context.setAttribute("attribute1", "value1");
        context.setAttribute("attribute2", "value2");
        
        try {
            // test contextualize method
            bean.contextualize(context);
            
            // check if attributes are copied correctly
            assertEquals("value1", bean.getConfiguration().getAttribute("attribute1"));
            assertEquals("value2", bean.getConfiguration().getAttribute("attribute2"));
        } catch (CheckstyleException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}