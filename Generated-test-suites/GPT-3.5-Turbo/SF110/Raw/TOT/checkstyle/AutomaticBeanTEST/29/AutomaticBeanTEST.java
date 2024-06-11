import org.junit.Test;
import static org.junit.Assert.*;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
  
    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }
  
    @Test
    public void testConfigure() {
        Configuration configuration = new Configuration();
        
        // typical use case
        configuration.setAttribute("key1", "value1");
        automaticBean.configure(configuration);
        assertNotNull(automaticBean.getConfiguration());
      
        // edge case - empty configuration
        Configuration emptyConfig = new Configuration();
        automaticBean.configure(emptyConfig);
        assertNotNull(automaticBean.getConfiguration());
      
        // error scenario - non-existing property
        Configuration nonExistingConfig = new Configuration();
        nonExistingConfig.setAttribute("nonExistingKey", "value");
        try {
            automaticBean.configure(nonExistingConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException ex) {
            assertTrue(ex.getMessage().contains("Property 'nonExistingKey' in module"));
        }
    }

    @Test
    public void testTryCopyProperty() {
        Configuration configuration = new Configuration();
        automaticBean.configure(configuration);
        
        // typical use case
        try {
            automaticBean.tryCopyProperty("moduleName", "key", "value", true);
        } catch (CheckstyleException ex) {
            fail("Unexpected CheckstyleException");
        }
      
        // error scenario - illegal value
        try {
            automaticBean.tryCopyProperty("moduleName", "key", "illegalValue", true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException ex) {
            assertTrue(ex.getMessage().contains("illegal value 'illegalValue' for property 'key' of module"));
        }
        
        // error scenario - non-existing property
        try {
            automaticBean.tryCopyProperty("moduleName", "nonExistingKey", "value", true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException ex) {
            assertTrue(ex.getMessage().contains("Property 'nonExistingKey' in module"));
        }
    }

    @Test
    public void testContextualize() {
        Context context = new Context();
        
        // typical use case
        context.setAttribute("key1", "value1");
        automaticBean.contextualize(context);
        assertNotNull(automaticBean.getConfiguration());
      
        // error scenario - non-existing property
        Context nonExistingContext = new Context();
        nonExistingContext.setAttribute("nonExistingKey", "value");
        try {
            automaticBean.contextualize(nonExistingContext);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException ex) {
            assertTrue(ex.getMessage().contains("Property 'nonExistingKey' in module"));
        }
    }

    @Test
    public void testFinishLocalSetup() {
        // no test cases required as the method does not have any logic
    }

    @Test
    public void testSetupChild() {
        Configuration childConfiguration = new Configuration();
      
        // error scenario - not allowed as child
        try {
            automaticBean.setupChild(childConfiguration);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException ex) {
            assertTrue(ex.getMessage().contains("is not allowed as a child in"));
        }
    }
}