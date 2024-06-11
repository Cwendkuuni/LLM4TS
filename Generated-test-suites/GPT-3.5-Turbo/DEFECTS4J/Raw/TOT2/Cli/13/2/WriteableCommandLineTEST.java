import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class WriteableCommandLineTest {

   @Test
   public void testAddOption() {
       WriteableCommandLine cmd = new WriteableCommandLineImpl();
       Option option = new Option("testOption", "Test option");
       cmd.addOption(option);
       assertTrue(cmd.hasOption("testOption"));
   }

   @Test
   public void testAddValue() {
       WriteableCommandLine cmd = new WriteableCommandLineImpl();
       Option option = new Option("testOption", "Test option");
       cmd.addOption(option);
       cmd.addValue(option, "testValue");
       List values = cmd.getValues(option);
       assertEquals("testValue", values.get(0));
   }

   @Test
   public void testSetDefaultValues() {
       WriteableCommandLine cmd = new WriteableCommandLineImpl();
       Option option = new Option("testOption", "Test option");
       List defaultValues = new ArrayList();
       defaultValues.add("defaultValue1");
       defaultValues.add("defaultValue2");
       cmd.setDefaultValues(option, defaultValues);
       List values = cmd.getValues(option);
       assertEquals("defaultValue1", values.get(0));
       assertEquals("defaultValue2", values.get(1));
   }

   @Test
   public void testAddSwitch() {
       WriteableCommandLine cmd = new WriteableCommandLineImpl();
       Option option = new Option("testSwitch", "Test switch", true);
       cmd.addSwitch(option, true);
       assertTrue(cmd.getSwitch(option));
   }

   @Test
   public void testSetDefaultSwitch() {
       WriteableCommandLine cmd = new WriteableCommandLineImpl();
       Option option = new Option("testSwitch", "Test switch", true);
       cmd.setDefaultSwitch(option, false);
       assertFalse(cmd.getSwitch(option));
   }

   @Test
   public void testAddProperty() {
       WriteableCommandLine cmd = new WriteableCommandLineImpl();
       cmd.addProperty("testProperty", "testValue");
       assertEquals("testValue", cmd.getProperty("testProperty"));
   }

   @Test
   public void testLooksLikeOption() {
       WriteableCommandLine cmd = new WriteableCommandLineImpl();
       assertTrue(cmd.looksLikeOption("-testOption"));
   }

   private class WriteableCommandLineImpl implements WriteableCommandLine {
       // implement all methods according to the interface
   }
}