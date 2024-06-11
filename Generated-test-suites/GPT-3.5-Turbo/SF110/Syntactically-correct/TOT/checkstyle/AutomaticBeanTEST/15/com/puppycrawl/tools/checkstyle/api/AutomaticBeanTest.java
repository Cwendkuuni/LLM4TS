
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class AutomaticBeanTest {

  @Test
  public void testConfigure() {
    Configuration config = new Configuration();
    AutomaticBean bean = new AutomaticBean();

    try {
      bean.configure(config);
      assertNotNull(bean.getConfiguration());
    } catch (CheckstyleException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testConfigure_NullConfiguration() {
    AutomaticBean bean = new AutomaticBean();

    try {
      bean.configure(null);
      assertNotNull(bean.getConfiguration());
    } catch (CheckstyleException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testConfigure_AttributeDoesNotExist() {
    Configuration config = new Configuration();
    AutomaticBean bean = new AutomaticBean();

    try {
      config.addAttribute("attribute1", "value1");
      bean.configure(config);
    } catch (CheckstyleException e) {
      assertEquals("Property 'attribute1' in module moduleName does not exist, please check the documentation", e.getMessage());
    }
  }

  @Test
  public void testTryCopyProperty() {
    AutomaticBean bean = new AutomaticBean();

    try {
      bean.tryCopyProperty("moduleName", "property", "value", true);
      // Assert copying property successful
      assertEquals("value", bean.getProperty());
    } catch (CheckstyleException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testTryCopyProperty_PropertyDoesNotExist() {
    AutomaticBean bean = new AutomaticBean();

    try {
      bean.tryCopyProperty("moduleName", "property", "value", true);
      fail("Exception should be thrown");
    } catch (CheckstyleException e) {
      assertEquals("Property 'property' in module moduleName does not exist, please check the documentation", e.getMessage());
    }
  }

  @Test
  public void testTryCopyProperty_InvocationTargetException() {
    AutomaticBean bean = new AutomaticBean();

    try {
      bean.tryCopyProperty("moduleName", "property", "value", true);
      fail("Exception should be thrown");
    } catch (CheckstyleException e) {
      assertEquals("Cannot set property 'property' to 'value' in module moduleName", e.getMessage());
    }
  }

  @Test
  public void testTryCopyProperty_IllegalArgumentException() {
    AutomaticBean bean = new AutomaticBean();

    try {
      bean.tryCopyProperty("moduleName", "property", "illegalValue", true);
      fail("Exception should be thrown");
    } catch (CheckstyleException e) {
      assertEquals("illegal value 'illegalValue' for property 'property' of module moduleName", e.getMessage());
    }
  }

  @Test
  public void testContextualize() {
    Context context = new Context();
    context.setAttribute("attribute1", "value1");
    AutomaticBean bean = new AutomaticBean();

    try {
      bean.contextualize(context);
      // Assert copying attribute successful
      assertNotNull(bean.getConfiguration());
    } catch (CheckstyleException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testSetupChild() {
    Configuration childConfig = new Configuration();
    AutomaticBean bean = new AutomaticBean();

    try {
      bean.setupChild(childConfig);
      fail("Exception should be thrown");
    } catch (CheckstyleException e) {
      assertEquals("childConfig is not allowed as a child in configurationName", e.getMessage());
    }
  }

}
