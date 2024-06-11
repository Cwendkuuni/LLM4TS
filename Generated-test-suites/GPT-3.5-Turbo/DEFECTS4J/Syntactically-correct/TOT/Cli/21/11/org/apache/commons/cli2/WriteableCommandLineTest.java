import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

   private WriteableCommandLine writeableCommandLine;

   @Before
   public void setUp() {
      writeableCommandLine = new WriteableCommandLineImpl();
   }

   @Test
   public void testAddOption() {
      Option option = new OptionImpl("option1");
      writeableCommandLine.addOption(option);

      assertTrue(writeableCommandLine.hasOption("option1"));
   }

   @Test
   public void testAddValue() {
      Option option = new OptionImpl("option1");
      writeableCommandLine.addOption(option);

      writeableCommandLine.addValue(option, "value1");

      assertEquals("value1", writeableCommandLine.getValue("option1"));
   }

   @Test
   public void testGetUndefaultedValues() {
      Option option = new OptionImpl("option1");
      writeableCommandLine.addOption(option);

      writeableCommandLine.addValue(option, "value1");
      writeableCommandLine.addValue(option, "value2");

      List<String> undefaultedValues = writeableCommandLine.getUndefaultedValues(option);

      assertEquals(2, undefaultedValues.size());
      assertTrue(undefaultedValues.contains("value1"));
      assertTrue(undefaultedValues.contains("value2"));
   }

   @Test
   public void testSetDefaultValues() {
      Option option = new OptionImpl("option1");
      writeableCommandLine.addOption(option);

      List<String> defaultValues = new ArrayList<>();
      defaultValues.add("default1");
      defaultValues.add("default2");
      writeableCommandLine.setDefaultValues(option, defaultValues);

      List<String> undefaultedValues = writeableCommandLine.getUndefaultedValues(option);

      assertEquals(2, undefaultedValues.size());
      assertTrue(undefaultedValues.contains("default1"));
      assertTrue(undefaultedValues.contains("default2"));
   }

   @Test
   public void testAddSwitch() {
      Option option = new OptionImpl("option1");
      writeableCommandLine.addOption(option);

      writeableCommandLine.addSwitch(option, true);

      assertTrue(writeableCommandLine.isSwitchedOn("option1"));
   }

   @Test(expected = IllegalStateException.class)
   public void testAddSwitch_ThrowsIllegalStateException() {
      Option option = new OptionImpl("option1");
      writeableCommandLine.addOption(option);

      writeableCommandLine.addSwitch(option, true);
      writeableCommandLine.addSwitch(option, false);
   }

   @Test
   public void testSetDefaultSwitch() {
      Option option = new OptionImpl("option1");
      writeableCommandLine.addOption(option);

      writeableCommandLine.setDefaultSwitch(option, true);

      assertTrue(writeableCommandLine.isSwitchedOn("option1"));
   }

   @Test
   public void testAddProperty() {
      Option option = new OptionImpl("option1");
      writeableCommandLine.addOption(option);

      writeableCommandLine.addProperty(option, "property1", "value1");

      assertEquals("value1", writeableCommandLine.getProperty("option1", "property1"));
   }

   @Test
   public void testAddProperty_DefaultPropertySet() {
      writeableCommandLine.addProperty("property1", "value1");

      assertEquals("value1", writeableCommandLine.getProperty(null, "property1"));
   }

   @Test
   public void testLooksLikeOption() {
      assertTrue(writeableCommandLine.looksLikeOption("-option1"));
      assertFalse(writeableCommandLine.looksLikeOption("value1"));
   }

   @Test
   public void testSetCurrentOption() {
      Option option = new OptionImpl("option1");
      writeableCommandLine.setCurrentOption(option);

      assertEquals(option, writeableCommandLine.getCurrentOption());
   }

   private class WriteableCommandLineImpl implements WriteableCommandLine {
      // Implement all the interface methods here
   }

}