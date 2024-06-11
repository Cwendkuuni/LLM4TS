import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parse.ParseResult;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option optionA;
 private Option optionB;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLineImpl(new Option.Builder("a")
 .longOpt("option-a")
 .requiredArg()
 .build(), new String[]{"argA"});
 optionA = new Option.Builder("a")
 .longOpt("option-a")
 .hasArg()
 .build();
 optionB = new Option.Builder("b")
 .longOpt("option-b")
 .noArg()
 .build();
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(optionA);
 commandLine.addOption(optionB);
 assertNotNull(commandLine.getOptions().get(0));
 assertNotNull(commandLine.getOptions().get(1));
 }

 @Test
 public void testAddValue() {
 commandLine.addOption(optionA);
 commandLine.addValue(optionA, "valueA");
 commandLine.addValue(optionA, "valueB");
 List values = commandLine.getValues(optionA, null);
 assertEquals(2, values.size());
 assertEquals("valueA", values.get(0));
 assertEquals("valueB", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addOption(optionB);
 commandLine.addSwitch(optionB, true);
 assertTrue((Boolean) commandLine.getSwitch(optionB, false));
 }

 @Test
 public void testHasOption() {
 commandLine.addOption(optionA);
 assertTrue(commandLine.hasOption(optionA));
 }

 @Test
 public void testGetOption() {
 commandLine.addOption(optionA);
 Option option = commandLine.getOption("option-a");
 assertEquals(optionA, option);
 }

 @Test
 public void testGetValues() {
 commandLine.addOption(optionA);
 commandLine.addValue(optionA, "valueA");
 commandLine.addValue(optionA, "valueB");
 List values = commandLine.getValues(optionA, null);
 assertEquals(2, values.size());
 assertEquals("valueA", values.get(0));
 assertEquals("valueB", values.get(1));
 }

 @Test
 public void testGetSwitch() {
 commandLine.addOption(optionB);
 commandLine.addSwitch(optionB, true);
 assertTrue((Boolean) commandLine.getSwitch(optionB, false));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property", "defaultValue"));
 }

 @Test
 public void testToString() {
 commandLine.addOption(optionA);
 commandLine.addValue(optionA, "argA");
 assertEquals("argA", commandLine.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine.addOption(optionB);
 commandLine.addSwitch(optionB, true);
 commandLine.addSwitch(optionB, false);
 }

 @Test
 public void testParse() {
 ParseResult parseResult = commandLine.parse(new String[]{"-option-a", "argC"});
 assertFalse(commandLine.hasOption(optionA));
 assertFalse(commandLine.hasOption(optionB));
 assertNull(commandLine.getValues(optionA, null));
 assertNull(commandLine.getValues(optionB, null));
 assertNull(commandLine.getSwitch(optionB, null));
 assertEquals(1, parseResult.getOptions().size());
 assertEquals(1, parseResult.getValues().size());
 assertEquals("argC", parseResult.getValues().get(0));
 }
}