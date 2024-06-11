import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    
    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }
    
    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        try {
            automaticBean.configure(config);
        } catch (CheckstyleException e) {
            fail(e.getMessage());
        }
        assertNotNull(automaticBean.getConfiguration());
    }
    
    @Test
    public void testContextualize() {
        Context context = new Context();
        try {
            automaticBean.contextualize(context);
        } catch (CheckstyleException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testGetConfiguration() {
        automaticBean.configure(new Configuration());
        assertNotNull(automaticBean.getConfiguration());
    }
    
    @Test
    public void testFinishLocalSetup() {
        try {
            automaticBean.finishLocalSetup();
        } catch (CheckstyleException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testSetupChild() {
        Configuration childConf = new Configuration();
        try {
            automaticBean.setupChild(childConf);
        } catch (CheckstyleException e) {
            assertEquals(childConf.getName() + " is not allowed as a child in " + automaticBean.getConfiguration().getName(), e.getMessage());
        }
    }
}