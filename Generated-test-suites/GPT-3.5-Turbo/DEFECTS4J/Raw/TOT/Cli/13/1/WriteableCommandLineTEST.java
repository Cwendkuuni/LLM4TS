import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.*;
import java.util.List;

public class WriteableCommandLineTest {

   @Test
   public void testAddOption() {
      WriteableCommandLine commandLine = new WriteableCommandLineImpl();
      Option option = new OptionImpl("o");
      commandLine.addOption(option);
      
      assertTrue(commandLine.hasOption("o"));
      assertFalse(commandLine.hasOption("x"));
   }
   
   @Test
   public void testAddValue() {
      WriteableCommandLine commandLine = new WriteableCommandLineImpl();
      Option option = new OptionImpl("o");
      commandLine.addValue(option, "value1");
      commandLine.addValue(option, "value2");
      
      List<String> values = commandLine.getValues(option);
      
      assertEquals(2, values.size());
      assertEquals("value1", values.get(0));
      assertEquals("value2", values.get(1));
   }
   
   @Test
   public void testGetValues() {
      WriteableCommandLine commandLine = new WriteableCommandLineImpl();
      Option option1 = new OptionImpl("o1");
      Option option2 = new OptionImpl("o2");
      Option option3 = new OptionImpl("o3");
      
      commandLine.addValue(option1, "value1");
      commandLine.addValue(option2, "value2");
      commandLine.addValue(option1, "value3");
      
      List<String> values1 = commandLine.getValues(option1);
      List<String> values2 = commandLine.getValues(option2);
      List<String> values3 = commandLine.getValues(option3);
      
      assertEquals(2, values1.size());
      assertEquals("value1", values1.get(0));
      assertEquals("value3", values1.get(1));
      
      assertEquals(1, values2.size());
      assertEquals("value2", values2.get(0));
      
      assertTrue(values3.isEmpty());
   }
   
   @Test
   public void testSetDefaultValues() {
      WriteableCommandLine commandLine = new WriteableCommandLineImpl();
      Option option = new OptionImpl("o");
      List<String> defaultValues = new ArrayList<>();
      defaultValues.add("default1");
      defaultValues.add("default2");
      
      commandLine.setDefaultValues(option, defaultValues);
      
      List<String> values = commandLine.getValues(option);
      
      assertEquals(2, values.size());
      assertEquals("default1", values.get(0));
      assertEquals("default2", values.get(1));
   }
   
   @Test
   public void testAddSwitch() {
      WriteableCommandLine commandLine = new WriteableCommandLineImpl();
      Option option = new OptionImpl("s");
      
      commandLine.addSwitch(option, true);
      
      assertTrue(commandLine.getSwitch(option));
   }
   
   @Test(expected=IllegalStateException.class)
   public void testAddSwitchTwice() {
      WriteableCommandLine commandLine = new WriteableCommandLineImpl();
      Option option = new OptionImpl("s");
      
      commandLine.addSwitch(option, true);
      commandLine.addSwitch(option, false);
   }
   
   @Test
   public void testSetDefaultSwitch() {
      WriteableCommandLine commandLine = new WriteableCommandLineImpl();
      Option option = new OptionImpl("s");
      
      commandLine.setDefaultSwitch(option, true);
      
      assertTrue(commandLine.getSwitch(option));
   }
   
   @Test
   public void testAddProperty() {
      WriteableCommandLine commandLine = new WriteableCommandLineImpl();
      commandLine.addProperty("prop1", "value1");
      commandLine.addProperty("prop2", "value2");
      
      assertEquals("value1", commandLine.getProperty("prop1"));
      assertEquals("value2", commandLine.getProperty("prop2"));
      assertNull(commandLine.getProperty("prop3"));
   }
   
   @Test
   public void testLooksLikeOption() {
      WriteableCommandLine commandLine = new WriteableCommandLineImpl();
      
      assertTrue(commandLine.looksLikeOption("-o"));
      assertTrue(commandLine.looksLikeOption("--option"));
      assertFalse(commandLine.looksLikeOption("value"));
   }
}