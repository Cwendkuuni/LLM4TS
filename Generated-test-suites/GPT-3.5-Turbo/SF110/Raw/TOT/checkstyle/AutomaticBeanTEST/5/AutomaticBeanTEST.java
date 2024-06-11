import org.junit.*;
import static org.junit.Assert.*;

public class AutomaticBeanTest {

    @Test
    public void testConfigure_TypicalCase() {
        // Typical case where configuration is passed successfully
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.configure(config);
        } catch (CheckstyleException e) {
            fail("Exception should not be thrown");
        }
        assertEquals(config, automaticBean.getConfiguration());
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_InvalidProperty() throws CheckstyleException {
        // Error case where an invalid property is passed
        Configuration config = new Configuration();
        config.setAttribute("invalidProperty", "invalidValue");
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
    }
    
    @Test
    public void testContextualize_TypicalCase() {
        // Typical case where context is passed successfully
        Context context = new Context();
        context.setAttribute("key1", "value1");
        context.setAttribute("key2", "value2");
        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.contextualize(context);
        } catch (CheckstyleException e) {
            fail("Exception should not be thrown");
        }
        assertNull(automaticBean.getConfiguration());
    }
    
    @Test
    public void testContextualize_NullAttribute() {
        // Error case where a null attribute is passed
        Context context = new Context();
        context.setAttribute("key", null);
        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.contextualize(context);
        } catch (CheckstyleException e) {
            fail("Exception should not be thrown");
        }
        assertNull(automaticBean.getConfiguration());
    }
    
    @Test
    public void testSetupChild_InvalidChild() {
        // Error case where an invalid child configuration is passed
        Configuration childConfig = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.setupChild(childConfig);
            fail("Exception should be thrown");
        } catch (CheckstyleException e) {
            assertEquals(childConfig.getName() + " is not allowed as a child in " + automaticBean.getConfiguration().getName(), e.getMessage());
        }
    }
    
    @Test
    public void testRelaxedStringArrayConverter_ConvertStringArray() {
        // Typical case where a string array is converted successfully
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        String input = "value1, value2 ,value3";
        String[] expectedOutput = {"value1", "value2", "value3"};
        assertArrayEquals(expectedOutput, (String[])converter.convert(String[].class, input));
    }
    
    @Test
    public void testRelaxedStringArrayConverter_ConvertEmptyString() {
        // Error case where an empty string is passed
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        String input = "";
        String[] expectedOutput = {};
        assertArrayEquals(expectedOutput, (String[])converter.convert(String[].class, input));
    }
}