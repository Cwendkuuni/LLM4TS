
import org.junit.*;
import static org.junit.Assert.*;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration config = new Configuration();
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = new Context();
        automaticBean.contextualize(context);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        automaticBean.finishLocalSetup();
        // No specific functionality to test
    }

    @Test
    public void testSetupChild() throws CheckstyleException {
        Configuration childConf = null;
        try {
            automaticBean.setupChild(childConf);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            assertEquals("null is not allowed as a child in null", e.getMessage());
        }
    }
}
