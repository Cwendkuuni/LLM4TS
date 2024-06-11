import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Before
 public void setUp() {
 option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(option, "file.txt");
 assertEquals(1, commandLine.size());
 assertEquals("file.txt", commandLine.getValues(option, Collections.emptyList()).get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option result = commandLine.getOption("o");
 assertEquals(option, result);
 }

 @Test
 public void testgetProperty() {
 commandLine.addProperty("file.name", "file.txt");
 assertEquals("file.txt", commandLine.getProperty("file.name", "defaultValue"));
 }

 @Test
 public void testGetProperties() {
 commandLine.addProperty("file.name", "file.txt");
 Set properties = commandLine.getProperties();
 assertTrue(properties.contains("file.name"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("o"));
 }

 @Test
 public void testToString() {
 commandLine.addValue(option, "file.txt");
 assertEquals("o output file.txt", commandLine.toString());
 }

 @Test
 public void testGetOptions() {
 assertEquals(1, commandLine.getOptions().size());
 }

 @Test
 public void testGetOptionTriggers() {
 Set triggers = commandLine.getOptionTriggers();
 assertTrue(triggers.contains("o"));
 }

 @Test
 public void testSetDefaultValues() {
 List defaults = Collections.singletonList("file1.txt");
 commandLine.setDefaultValues(option, defaults);
 assertEquals(1, commandLine.getValues(option, Collections.emptyList()).size());
 assertEquals("file1.txt", commandLine.getValues(option, Collections.emptyList()).get(0));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }
}