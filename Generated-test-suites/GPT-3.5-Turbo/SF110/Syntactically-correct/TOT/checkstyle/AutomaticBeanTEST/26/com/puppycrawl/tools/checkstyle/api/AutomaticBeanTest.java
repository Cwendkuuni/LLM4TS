import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.*;

public class AutomaticBeanTest {

   private AutomaticBean automaticBean;
   private Configuration configuration;
   private Context context;

   @Before
   public void setup() {
      automaticBean = new AutomaticBean();
      configuration = new DefaultConfiguration("TestConfig");
      context = new DefaultContext();
      automaticBean.configure(configuration);
      automaticBean.contextualize(context);
   }

   @Test
   public void testConfigure_ValidConfiguration_CheckConfigurationSet() throws CheckstyleException {
      assertEquals(configuration, automaticBean.getConfiguration());
   }
   
   @Test
   public void testConfigure_InvalidPropertyName_ThrowsCheckstyleException() {
      Configuration invalidConfiguration = new DefaultConfiguration("InvalidConfig");
      invalidConfiguration.addAttribute("invalidKey", "invalidValue");

      try {
         automaticBean.configure(invalidConfiguration);
         fail("Should have thrown CheckstyleException");
      } catch (CheckstyleException e) {
         String expectedMessage = "Property 'invalidKey' in module InvalidConfig does not exist, please check the documentation";
         assertEquals(expectedMessage, e.getMessage());
      }
   }
   
   @Test
   public void testConfigure_InvalidPropertyValue_ThrowsCheckstyleException() {
      Configuration invalidConfiguration = new DefaultConfiguration("InvalidConfig");
      invalidConfiguration.addAttribute("key", "invalidValue");

      try {
         automaticBean.configure(invalidConfiguration);
         fail("Should have thrown CheckstyleException");
      } catch (CheckstyleException e) {
         String expectedMessage = "illegal value 'invalidValue' for property 'key' of module InvalidConfig";
         assertEquals(expectedMessage, e.getMessage());
      }
   }

   @Test
   public void testSetupChild_InvalidChildConfiguration_ThrowsCheckstyleException() {
      Configuration invalidChildConfig = new DefaultConfiguration("InvalidChildConfig");
      
      try {
         automaticBean.setupChild(invalidChildConfig);
         fail("Should have thrown CheckstyleException");
      } catch (CheckstyleException e) {
         String expectedMessage = "InvalidChildConfig is not allowed as a child in TestConfig";
         assertEquals(expectedMessage, e.getMessage());
      }
   }

   @Test
   public void testContextualize_ValidContext_CheckPropertyCopied() throws CheckstyleException {
      context.put("param", "value");
      automaticBean.contextualize(context);
      
      assertTrue(automaticBean.getProperty("param").equals("value"));
   }
   
   @Test
   public void testContextualize_InvalidPropertyName_ThrowsCheckstyleException() {
      context.put("invalidKey", "value");

      try {
         automaticBean.contextualize(context);
         fail("Should have thrown CheckstyleException");
      } catch (CheckstyleException e) {
         String expectedMessage = "Property 'invalidKey' in module com.puppycrawl.tools.checkstyle.api.AutomaticBean does not exist, please check the documentation";
         assertEquals(expectedMessage, e.getMessage());
      }
   }

   @Test
   public void testRelaxedStringArrayConverter_ValidValue_ReturnConvertedArray() {
      AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
      Object result = converter.convert(String[].class, "value1, value2, value3");
      
      assertTrue(result instanceof String[]);
      String[] convertedArray = (String[]) result;
      assertEquals("value1", convertedArray[0]);
      assertEquals("value2", convertedArray[1]);
      assertEquals("value3", convertedArray[2]);
   }
   
   @Test
   public void testRelaxedStringArrayConverter_EmptyValue_ReturnEmptyArray() {
      AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
      Object result = converter.convert(String[].class, "");
      
      assertTrue(result instanceof String[]);
      String[] convertedArray = (String[]) result;
      assertEquals(0, convertedArray.length);
   }

   @Test
   public void testRelaxedStringArrayConverter_NullValue_ReturnEmptyArray() {
      AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
      Object result = converter.convert(String[].class, null);
      
      assertTrue(result instanceof String[]);
      String[] convertedArray = (String[]) result;
      assertEquals(0, convertedArray.length);
   }

}