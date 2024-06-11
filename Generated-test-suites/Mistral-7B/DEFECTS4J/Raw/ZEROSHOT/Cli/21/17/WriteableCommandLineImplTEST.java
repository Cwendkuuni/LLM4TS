import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl cmdLine;
 private Option option;

 @Test
 public void testAddOption() {
 option = new Option("o", "output", true, "output file");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 assertEquals(option, cmdLine.getOption("o"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 option = new Option("o", "output", true, "output file");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 cmdLine.addOption(option);
 }

 @Test
 public void testGetOption() {
 option = new Option("o", "output", true, "output file");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 assertEquals(option, cmdLine.getOption("o"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionNull() {
 cmdLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 cmdLine.getOption(null);
 }

 @Test
 public void testGetOptions() {
 option = new Option("o", "output", true, "output file");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 List options = cmdLine.getOptions();
 assertEquals(1, options.size());
 assertEquals(option, options.get(0));
 }

 @Test
 public void testLooksLikeOption() {
 cmdLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file"), Collections.emptyList());
 assertTrue(cmdLine.looksLikeOption("o"));
 assertTrue(cmdLine.looksLikeOption("output"));
 assertTrue(cmdLine.looksLikeOption("--output"));
 assertFalse(cmdLine.looksLikeOption("input"));
 }

 @Test
 public void testHasOption() {
 option = new Option("o", "output", true, "output file");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 assertTrue(cmdLine.hasOption(option));
 }

 @Test
 public void testToString() {
 option = new Option("o", "output", true, "output file");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 List args = Arrays.asList("arg1", "arg2");
 cmdLine.setNormalised(args);
 assertEquals("arg1 arg2", cmdLine.toString());
 }

 @Test
 public void testGetProperty() {
 option = new Option("p", "property", true, "property value");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 cmdLine.addProperty(option, "property", "value");
 assertEquals("value", cmdLine.getProperty(option, "default"));
 }

 @Test
 public void testSetDefaultValues() {
 option = new Option("v", "value", true, "value to set");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 List defaults = Arrays.asList("default1", "default2");
 cmdLine.setDefaultValues(option, defaults);
 List values = cmdLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("default1", values.get(0));
 assertEquals("default2", values.get(1));
 }

 @Test
 public void testAddValue() {
 option = new Option("a", "append", false, "append mode");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 cmdLine.addValue(option, "value1");
 List values = cmdLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 option = new Option("s", "silent", false, "silent mode");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 cmdLine.addSwitch(option, true);
 assertTrue(cmdLine.getSwitch(option, false));
 }

 @Test
 public void testGetSwitch() {
 option = new Option("s", "silent", false, "silent mode");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 cmdLine.addSwitch(option, true);
 assertTrue(cmdLine.getSwitch(option, null));
 }

 @Test
 public void testGetUndefaultedValues() {
 option = new Option("a", "append", false, "append mode");
 cmdLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 cmdLine.addOption(option);
 List values = cmdLine.getUndefaultedValues(option);
 assertTrue(values.isEmpty());
 }
}