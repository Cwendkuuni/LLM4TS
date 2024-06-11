import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 @Test
 public void testWriteableCommandLineImpl_AddOption() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file path"), new ArrayList());
 commandLine.addOption(new Option("i", "input", true, "input file path"));

 commandLine.addOption(new Option("o", "output2", true, "output file path2")); // duplicated option with same key

 Assert.assertEquals(2, commandLine.getOptions().size());
 Assert.assertEquals(1, commandLine.getOptions().get(0).getTriggers().size());
 Assert.assertEquals("output", commandLine.getOptions().get(0).getLongOpt());
 Assert.assertEquals("output file path", commandLine.getOptions().get(0).getDescription());
 Assert.assertEquals(true, commandLine.getOptions().get(0).isRequired());

 commandLine.addOption(new Option("p", "port", int.class, "port number")); // option with different type

 Assert.assertEquals(3, commandLine.getOptions().size());
 }

 @Test
 public void testWriteableCommandLineImpl_AddValue() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file path"), new ArrayList());
 commandLine.addOption(new Option("i", "input", true, "input file path"));
 commandLine.addOption(new Option("i", null, true, "input file name")); // option without long opt

 commandLine.addOption(new Option("o", "output", true, "output file path"));

 commandLine.addValue(commandLine.getOption("i"), "test.txt");
 commandLine.addValue(commandLine.getOption("i"), "test2.txt"); // multiple values for same option

 commandLine.addValue(commandLine.getOption("i"), null); // null value for option

 Assert.assertEquals("test.txt,test2.txt", commandLine.getValues(commandLine.getOption("i"), null).toString());
 }

 @Test
 public void testWriteableCommandLineImpl_AddSwitch() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("v", "verbose", false, "verbose mode"), new ArrayList());
 commandLine.addOption(new Option("q", "quiet", false, "quiet mode"));
 commandLine.addOption(new Option("v", "verbose", false, "verbose mode")); // duplicated option with same key

 commandLine.addSwitch(commandLine.getOption("v"), true); // switch set to true
 commandLine.addSwitch(commandLine.getOption("q"), false); // switch set to false

 Assert.assertEquals(true, commandLine.getSwitch(commandLine.getOption("v"), false));
 Assert.assertEquals(false, commandLine.getSwitch(commandLine.getOption("q"), true));
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteableCommandLineImpl_AddSwitch_AlreadySet() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("v", "verbose", false, "verbose mode"), new ArrayList());
 commandLine.addOption(new Option("v", "verbose", false, "verbose mode")); // duplicated option with same key

 commandLine.addSwitch(commandLine.getOption("v"), true); // switch set to true
 commandLine.addSwitch(commandLine.getOption("v"), true); // trying to set it again
 }

 @Test
 public void testWriteableCommandLineImpl_HasOption() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file path"), new ArrayList());
 commandLine.addOption(new Option("i", "input", true, "input file path"));

 Assert.assertTrue(commandLine.hasOption(commandLine.getOption("o")));
 Assert.assertTrue(commandLine.hasOption(commandLine.getOption("i")));
 Assert.assertFalse(commandLine.hasOption(new Option("x", "extended", false, "extended mode")));
 }

 @Test
 public void testWriteableCommandLineImpl_GetOption() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file path"), new ArrayList());
 commandLine.addOption(new Option("i", "input", true, "input file path"));
 commandLine.addOption(new Option("v", "verbose", false, "verbose mode"));

 Option option = commandLine.getOption("o");

 Assert.assertEquals("output", option.getLongOpt());
 Assert.assertEquals("output file path", option.getDescription());
 Assert.assertFalse(option.isRequired());
 }

 @Test
 public void testWriteableCommandLineImpl_GetValues() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file path"), new ArrayList());
 commandLine.addOption(new Option("i", "input", true, "input file path"));
 commandLine.addOption(new Option("o", "output", true, "output file path"));
 commandLine.addValue(commandLine.getOption("i"), "test.txt");
 commandLine.addValue(commandLine.getOption("i"), "test2.txt");
 commandLine.addValue(commandLine.getOption("o"), "output.txt");

 List values = commandLine.getValues(commandLine.getOption("i"), null);

 Assert.assertEquals("test.txt,test2.txt", values.toString());
 }

 @Test
 public void testWriteableCommandLineImpl_GetUndefaultedValues() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file path"), new ArrayList());
 commandLine.addOption(new Option("i", "input", true, "input file path"));
 commandLine.addOption(new Option("o", "output", true, "output file path"));
 commandLine.addValue(commandLine.getOption("i"), "test.txt");
 commandLine.addValue(commandLine.getOption("i"), "test2.txt");
 commandLine.addValue(commandLine.getOption("o"), "output.txt");

 List values = commandLine.getUndefaultedValues(commandLine.getOption("i"));

 Assert.assertEquals(2, values.size());
 Assert.assertEquals("test.txt", values.get(0).toString());
 Assert.assertEquals("test2.txt", values.get(1).toString());
 }

 @Test
 public void testWriteableCommandLineImpl_GetSwitch() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("v", "verbose", false, "verbose mode"), new ArrayList());
 commandLine.addOption(new Option("q", "quiet", false, "quiet mode"));
 commandLine.addOption(new Option("v", "verbose", false, "verbose mode")); // duplicated option with same key
 commandLine.addSwitch(commandLine.getOption("v"), true); // switch set to true

 Assert.assertEquals(true, commandLine.getSwitch(commandLine.getOption("v"), false));
 }

 @Test
 public void testWriteableCommandLineImpl_getProperty() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file path"), new ArrayList());
 commandLine.addOption(new Option("i", "input", true, "input file path"));
 commandLine.addOption(new PropertyOption("p", "prefix", "prefix", "prefix for logs"));
 commandLine.addProperty(commandLine.getOption("p"), "testPrefix");

 Assert.assertEquals("testPrefix", commandLine.getProperty("p"));
 Assert.assertEquals("prefix for logs", commandLine.getOption("p").getDescription());
 }

 @Test
 public void testWriteableCommandLineImpl_setDefaultValues() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file path"), new ArrayList());
 commandLine.addOption(new Option("i", "input", true, "input file path"));
 commandLine.addOption(new Option("o", "output", true, "output file path"));
 commandLine.setDefaultValues(commandLine.getOption("i"), Arrays.asList("defaultInput1.txt", "defaultInput2.txt"));

 List values = commandLine.getValues(commandLine.getOption("i"), null);

 Assert.assertEquals("defaultInput1.txt,defaultInput2.txt", values.toString());
 }

 @Test
 public void testWriteableCommandLineImpl_setDefaultSwitch() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("v", "verbose", false, "verbose mode"), new ArrayList());
 commandLine.addOption(new Option("q", "quiet", false, "quiet mode"));
 commandLine.addOption(new Option("v", "verbose", false, "verbose mode")); // duplicated option with same key
 commandLine.setDefaultSwitch(commandLine.getOption("v"), true); // switch set to true

 Assert.assertEquals(true, commandLine.getSwitch(commandLine.getOption("v"), false));
 }
}