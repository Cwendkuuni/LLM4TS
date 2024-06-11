import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 @Test
 public void testCreateWriteableCommandLine() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 assertEquals("optionA", writeableCommandLine.getRootName());
 assertNull(writeableCommandLine.getParsedArgs());
 assertFalse(writeableCommandLine.hasOption(new Option("optionA")));
 assertNull(writeableCommandLine.getOption("optionA"));
 assertNull(writeableCommandLine.getValues(new Option("optionA"), null));
 assertFalse(writeableCommandLine.getSwitch(new Option("optionA"), false));
 assertNull(writeableCommandLine.getProperty("property", null));
 assertNull(writeableCommandLine.toString());
 assertFalse(writeableCommandLine.looksLikeOption("optionA"));
 assertNull(writeableCommandLine.getOptions());
 assertNull(writeableCommandLine.getOptionTriggers());
 }

 @Test
 public void testAddOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 writeableCommandLine.addOption(new Option("optionB"));

 assertEquals("optionA", writeableCommandLine.getRootName());
 assertEquals(2, writeableCommandLine.getOptions().size());
 assertNotNull(writeableCommandLine.getOption("optionA"));
 assertNotNull(writeableCommandLine.getOption("optionB"));
}

 @Test
 public void testAddValue() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 Option optionA = new Option("optionA", "value");
 writeableCommandLine.addOption(optionA);
 writeableCommandLine.addValue(optionA, "test");

 assertEquals(1, writeableCommandLine.getValues(optionA, null).size());
 assertEquals("test", writeableCommandLine.getValues(optionA, null).get(0));
 }

 @Test
 public void testAddSwitch() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 Option optionA = new Option("optionA", true);
 writeableCommandLine.addOption(optionA);
 writeableCommandLine.addSwitch(optionA, true);

 assertTrue(writeableCommandLine.getSwitch(optionA, false));
 }

 @Test
 public void testHasOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 writeableCommandLine.addOption(new Option("optionA"));

 assertTrue(writeableCommandLine.hasOption(new Option("optionA")));
 }

 @Test
 public void testGetOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 Option optionA = new Option("optionA");
 writeableCommandLine.addOption(optionA);

 assertEquals(optionA, writeableCommandLine.getOption("optionA"));
 }

 @Test
 public void testGetValues() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 Option optionA = new Option("optionA", "value");
 writeableCommandLine.addOption(optionA);
 writeableCommandLine.addValue(optionA, "test1");
 writeableCommandLine.addValue(optionA, "test2");

 assertEquals(2, writeableCommandLine.getValues(optionA, null).size());
 assertEquals("test1", writeableCommandLine.getValues(optionA, null).get(0));
 assertEquals("test2", writeableCommandLine.getValues(optionA, null).get(1));
 }

 @Test
 public void testGetSwitch() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 Option optionA = new Option("optionA", true);
 writeableCommandLine.addOption(optionA);
 writeableCommandLine.addSwitch(optionA, true);
 writeableCommandLine.addSwitch(optionA, false);

 assertTrue(writeableCommandLine.getSwitch(optionA, false));
 assertFalse(writeableCommandLine.getSwitch(optionA, true));
 }

 @Test
 public void testgetProperty() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 Option optionA = new Option("optionA", true);
 writeableCommandLine.addOption(optionA);
 writeableCommandLine.addProperty(optionA, "property", "value");

 assertEquals("value", writeableCommandLine.getProperty(optionA, ""));
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 Option optionA = new Option("optionA", true);
 writeableCommandLine.addOption(optionA);
 Properties properties = new Properties();
 properties.setProperty("property", "value");
 writeableCommandLine.addProperty(optionA, "property", properties);

 assertEquals(1, writeableCommandLine.getProperties(optionA).size());
 assertEquals("property", writeableCommandLine.getProperties(optionA).toArray()[0].toString());
 assertEquals("value", writeableCommandLine.getProperty(optionA, "").toString());
 }

 @Test
 public void testToString() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 writeableCommandLine.addOption(new Option("optionA", true));
 writeableCommandLine.addOption(new Option("optionB", true));
 writeableCommandLine.addValue(new Option("optionA"), "test1");
 writeableCommandLine.addValue(new Option("optionA"), "test2");

 String expected = "optionA true test1 test2 optionB";
 assertEquals(expected, writeableCommandLine.toString());
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 writeableCommandLine.addOption(new Option("optionA", true));

 assertTrue(writeableCommandLine.looksLikeOption("optionA"));
 }

 @Test
 public void testGetOptions() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 writeableCommandLine.addOption(new Option("optionA"));
 writeableCommandLine.addOption(new Option("optionB"));

 assertFalse(writeableCommandLine.getOptions().isEmpty());
 }

 @Test
 public void testGetOptionTriggers() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
 writeableCommandLine.addOption(new Option("optionA"));
 writeableCommandLine.addOption(new Option("optionB"));

 assertFalse(writeableCommandLine.getOptionTriggers().isEmpty());
 }
}