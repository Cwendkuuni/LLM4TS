import static org.junit.Assert.*;
import org.junit.Test;

public class AutomaticBeanTest {
    @Test
    public void testConfigure() {
        // Test with empty configuration
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        try {
            bean.configure(config);
        } catch (CheckstyleException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertNull(bean.getConfiguration());
    }
    
    @Test
    public void testContextualize() {
        // Test with empty context
        Context context = new Context();
        AutomaticBean bean = new AutomaticBean();
        try {
            bean.contextualize(context);
        } catch (CheckstyleException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testTryCopyProperty() {
        AutomaticBean bean = new AutomaticBean();
        
        // Test with valid property and value
        String moduleName = "";
        String key = "property";
        Object value = "value";
        try {
            bean.tryCopyProperty(moduleName, key, value, true);
        } catch (CheckstyleException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test with invalid property
        key = "invalidProperty";
        try {
            bean.tryCopyProperty(moduleName, key, value, true);
            fail("Exception not thrown");
        } catch (CheckstyleException e) {
            assertEquals("Property 'invalidProperty' in module  does not exist, please check the documentation", e.getMessage());
        }
        
        // Test with illegal value for property
        key = "property";
        value = 123;
        try {
            bean.tryCopyProperty(moduleName, key, value, true);
            fail("Exception not thrown");
        } catch (CheckstyleException e) {
            assertEquals("illegal value '123' for property 'property' of module ", e.getMessage());
        }
    }
    
    @Test
    public void testFinishLocalSetup() {
        AutomaticBean bean = new AutomaticBean();
        try {
            bean.finishLocalSetup();
        } catch (CheckstyleException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetupChild() {
        AutomaticBean bean = new AutomaticBean();
        Configuration childConf = new Configuration();
        try {
            bean.setupChild(childConf);
            fail("Exception not thrown");
        } catch (CheckstyleException e) {
            assertEquals(" is not allowed as a child in ", e.getMessage());
        }
    }
    
    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        
        // Test with comma separated string
        Class<?> type = String[].class;
        Object value = "1, 2, 3";
        Object result = converter.convert(type, value);
        assertTrue(result instanceof String[]);
        String[] expected = {"1", "2", "3"};
        assertArrayEquals(expected, (String[])result);
    }
}