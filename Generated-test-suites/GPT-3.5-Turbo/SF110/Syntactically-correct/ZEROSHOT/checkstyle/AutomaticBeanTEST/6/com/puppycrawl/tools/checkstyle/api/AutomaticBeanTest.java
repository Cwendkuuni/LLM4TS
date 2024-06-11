import org.junit.*;
import static org.junit.Assert.*;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        // Test if createBeanUtilsBean method returns a non-null BeanUtilsBean object
        BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtilsBean);
    }

    @Test
    public void testConfigure() {
        // Test if the configuration object is set correctly
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration config = new Configuration();
        automaticBean.configure(config);
        assertEquals(config, automaticBean.getConfiguration());
    }

    @Test
    public void testTryCopyProperty() {
        // Test if tryCopyProperty method throws CheckstyleException when an invalid property is provided
        AutomaticBean automaticBean = new AutomaticBean();
        String moduleName = "testModule";
        String key = "invalidProperty";
        Object value = "invalidValue";
        boolean recheck = true;
        CheckstyleException exception = assertThrows(CheckstyleException.class, () -> {
            automaticBean.tryCopyProperty(moduleName, key, value, recheck);
        });
        String expectedMessage = String.format("Property '%s' in module %s does not exist, please check the documentation", key, moduleName);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testContextualize() {
        // Test if contextualize method copies attributes from the context correctly
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();
        String attributeName = "testAttribute";
        Object attributeValue = "testValue";
        context.put(attributeName, attributeValue);
        automaticBean.contextualize(context);
        assertEquals(attributeValue, automaticBean.getProperty(attributeName));
    }
}