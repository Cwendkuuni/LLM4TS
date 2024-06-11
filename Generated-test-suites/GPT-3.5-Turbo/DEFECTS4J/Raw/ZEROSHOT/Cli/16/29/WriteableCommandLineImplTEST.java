import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

   @Test
   public void testAddOption() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addOption(option);
       assertTrue(commandLine.getOptions().contains(option));
   }

   @Test
   public void testAddValue() {
       Option option = new Argument("testArgument");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addValue(option, "value1");
       commandLine.addValue(option, "value2");
       assertEquals(2, commandLine.getValues(option, null).size());
   }

   @Test
   public void testAddSwitch() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addSwitch(option, true);
       assertTrue(commandLine.getSwitch(option, null));
   }

   @Test
   public void testHasOption() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addOption(option);
       assertTrue(commandLine.hasOption(option));
   }

   @Test
   public void testGetOption() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addOption(option);
       assertEquals(option, commandLine.getOption("testOption"));
   }

   @Test
   public void testGetValues() {
       Option option = new Argument("testArgument");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addValue(option, "value1");
       commandLine.addValue(option, "value2");
       assertEquals(2, commandLine.getValues(option, null).size());
   }

   @Test
   public void testGetUndefaultedValues() {
       Option option = new Argument("testArgument");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addValue(option, "value1");
       commandLine.addValue(option, "value2");
       assertEquals(2, commandLine.getUndefaultedValues(option).size());
   }

   @Test
   public void testGetSwitch() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addSwitch(option, true);
       assertTrue(commandLine.getSwitch(option, false));
   }

   @Test
   public void testGetProperty() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addProperty(option, "property1", "value1");
       assertEquals("value1", commandLine.getProperty(option, "property1", null));
   }

   @Test
   public void testAddProperty() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addProperty(option, "property1", "value1");
       assertTrue(commandLine.getProperties(option).contains("property1"));
   }

   @Test
   public void testHasProperty() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addProperty(option, "property1", "value1");
       assertTrue(commandLine.getProperties().contains("property1"));
   }

   @Test
   public void testLooksLikeOption() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       assertTrue(commandLine.looksLikeOption("-testOption"));
   }

   @Test
   public void testToString() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addValue(option, "value1");
       commandLine.addValue(option, "value2");
       assertEquals("value1 value2", commandLine.toString());
   }

   @Test
   public void testGetOptions() {
       Option option1 = new Option("testOption1");
       Option option2 = new Option("testOption2");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
       commandLine.addOption(option1);
       commandLine.addOption(option2);
       assertEquals(2, commandLine.getOptions().size());
   }

   @Test
   public void testGetOptionTriggers() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.addOption(option);
       assertTrue(commandLine.getOptionTriggers().contains("testOption"));
   }

   @Test
   public void testSetDefaultValues() {
       Option option = new Argument("testArgument");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       List<String> defaults = new ArrayList<>();
       defaults.add("default1");
       defaults.add("default2");
       commandLine.setDefaultValues(option, defaults);
       assertEquals(2, commandLine.getValues(option, null).size());
   }

   @Test
   public void testSetDefaultSwitch() {
       Option option = new Option("testOption");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
       commandLine.setDefaultSwitch(option, true);
       assertTrue(commandLine.getSwitch(option, null));
   }

   @Test
   public void testGetNormalised() {
       Option option = new Option("testOption");
       ArrayList<String> arguments = new ArrayList<>();
       arguments.add("argument1");
       arguments.add("argument2");
       WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
       assertEquals(arguments, commandLine.getNormalised());
   }
}