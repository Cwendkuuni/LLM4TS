import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AutomaticBeanTest {
    
    private AutomaticBean automaticBean;
    
    @Before
    public void setUp() throws Exception {
        automaticBean = new AutomaticBean();
    }
    
    @Test
    public void testConfigure() throws Exception {
        Configuration config = new Configuration();
        automaticBean.configure(config);
        
        //Assertions
        assertEquals(config, automaticBean.getConfiguration());
    }
    
    @Test
    public void testContextualize() throws Exception {
        Context context = new Context();
        context.setAttribute("attribute1", "value1");
        context.setAttribute("attribute2", 123);
        
        automaticBean.contextualize(context);
        
        //Assertions
        assertEquals("value1", automaticBean.getAttribute1());
        assertEquals(123, automaticBean.getAttribute2());
    }
    
    @Test
    public void testFinishLocalSetup() throws Exception {
        // No specific behavior defined in finishLocalSetup()
        // Nothing to test
    }
    
    @Test
    public void testSetupChild() throws Exception {
        Configuration childConf = new Configuration();
        
        try {
            automaticBean.setupChild(childConf);
            fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            // Exception thrown as expected
        }
        
        //Assertions
        assertEquals(childConf.getName() + " is not allowed as a child in " + automaticBean.getConfiguration().getName(), e.getMessage());
    }
}