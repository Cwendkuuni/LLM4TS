import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 WriteableCommandLineImpl commandLine;
 List<Option> options;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 options = new ArrayList<>();
 }

 @Test
 public void testAddOption() {
 Option optionA = new Option("optionA", "Option A", false);
 commandLine.addOption(optionA);
 options.add(optionA);

 assertEquals(1, commandLine.getOptions().size());
 assertEquals(optionA, commandLine.getOption(optionA.getPreferredName()));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 Option optionA = new Option("optionA", "Option A", false);
 commandLine.addOption(optionA);
 commandLine.addOption(optionA);
 }

 @Test
 public void testAddValue() {
 Option optionA = new Option("optionA", "Option A", false);
 commandLine.addOption(optionA);
 commandLine.addValue(optionA, "value1");

 assertEquals("value1", commandLine.getValues(optionA, null).get(0));
 }

 @Test
 public void testAddSwitch() {
 Option optionA = new Option("optionA", "Option A", false);
 commandLine.addOption(optionA);
 commandLine.addSwitch(optionA, true);

 assertTrue(commandLine.getSwitch(optionA, false));
 }

 @Test
 public void testHasOption() {
 Option optionA = new Option("optionA", "Option A", false);
 commandLine.addOption(optionA);

 assertTrue(commandLine.hasOption(optionA));
 }

 @Test
 public void testGetOption() {
 Option optionA = new Option("optionA", "Option A", false);
 commandLine.addOption(optionA);

 assertEquals(optionA, commandLine.getOption(optionA.getPreferredName()));
 }

 @Test
 public void testGetValues() {
 Option optionA = new Option("optionA", "Option A", false);
 commandLine.addOption(optionA);
 commandLine.addValue(optionA, "value1");
 commandLine.addValue(optionA, "value2");

 assertEquals(2, commandLine.getValues(optionA, null).size());
 assertEquals("value1", commandLine.getValues(optionA, null).get(0));
 assertEquals("value2", commandLine.getValues(optionA, null).get(1));
 }

 @Test
 public void testGetSwitch() {
 Option optionA = new Option("optionA", "Option A", false);
 commandLine.addOption(optionA);
 commandLine.addSwitch(optionA, true);

 assertTrue(commandLine.getSwitch(optionA, false));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property", ""));
 }

 @Test
 public void testAddPropertyWithNullValue() {
 commandLine.addProperty("property", null);
 assertNull(commandLine.getProperty("property", ""));
 }

 @Test
 public void testGetProperties() {
 commandLine.addProperty("property1", "value1");
 commandLine.addProperty("property2", "value2");

 Set<String> properties = commandLine.getProperties();
 assertEquals(2, properties.size());
 assertTrue(properties.contains("property1"));
 assertTrue(properties.contains("property2"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("optionA"));
 }

 @Test
 public void testLooksLikeOptionWithPrefix() {
commandLine.setPrefixes(Collections.singletonList("--"));
 assertTrue(commandLine.looksLikeOption("--optionA"));
 }

 @Test
 public void testToString() {
 commandLine.addOption(new Option("optionA", "Option A", false));
 commandLine.addOption(new Option("optionB", "Option B", false));
 commandLine.addOption(new Option("optionC", "Option C", false, "arg1 arg2 arg3"));
 commandLine.addValue(new Option("optionC"), "value1");
 commandLine.addValue(new Option("optionC"), "value2");
 commandLine.addValue(new Option("optionC"), "value3");
 commandLine.addValue(new Option("optionC"), "value4");
 commandLine.addSwitch(new Option("optionB"), true);
 commandLine.addProperty("property", "value");

 assertEquals("optionA optionB --optionC value1 value2 value3 value4 arg1 arg2 arg3 value", commandLine.toString());
 }
}