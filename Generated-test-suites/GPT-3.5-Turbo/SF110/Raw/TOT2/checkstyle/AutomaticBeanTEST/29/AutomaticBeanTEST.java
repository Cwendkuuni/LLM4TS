import static org.junit.Assert.*;
import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        // Typical use case
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        
        // Edge case - config with no attributes
        Configuration emptyConfig = new Configuration();
        automaticBean.configure(emptyConfig);
        
        // Error scenario - invalid attribute key
        Configuration invalidConfig = new Configuration();
        invalidConfig.addAttribute("invalidKey", "value");
        try {
            automaticBean.configure(invalidConfig);
            fail("Exception not thrown for invalid attribute key");
        } catch (CheckstyleException e) {
            // Exception expected
        }
    }

    @Test
    public void testContextualize() {
        // Typical use case
        Context context = new Context();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.contextualize(context);
        
        // Edge case - context with no attributes
        Context emptyContext = new Context();
        automaticBean.contextualize(emptyContext);
        
        // Error scenario - invalid context key
        Context invalidContext = new Context();
        invalidContext.put("invalidKey", "value");
        try {
            automaticBean.contextualize(invalidContext);
            fail("Exception not thrown for invalid context key");
        } catch (CheckstyleException e) {
            // Exception expected
        }
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        // Typical use case
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        String input = "one,two,three";
        String[] expectedOutput = {"one", "two", "three"};
        assertArrayEquals(expectedOutput, (String[]) converter.convert(String[].class, input));
        
        // Edge case - empty input string
        String emptyInput = "";
        String[] expectedEmptyOutput = {};
        assertArrayEquals(expectedEmptyOutput, (String[]) converter.convert(String[].class, emptyInput));
        
        // Error scenario - invalid input format
        String invalidInput = "1,2,3";
        try {
            converter.convert(String[].class, invalidInput);
            fail("Exception not thrown for invalid input format");
        } catch (Exception e) {
            // Exception expected
        }
    }
}