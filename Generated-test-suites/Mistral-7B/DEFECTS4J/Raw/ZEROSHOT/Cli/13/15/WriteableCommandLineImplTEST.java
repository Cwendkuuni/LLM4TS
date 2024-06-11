import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 @Test
 public void testAddOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("optionName").build(), new ArrayList<>());
 writeableCommandLine.addOption(new Option.Builder("optionName").build());
 assertEquals(1, writeableCommandLine.getOptions().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionDuplicate() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("optionName").build(), new ArrayList<>());
 writeableCommandLine.addOption(new Option.Builder("optionName").build());
 writeableCommandLine.addOption(new Option.Builder("optionName").build());
 }

 @Test
 public void testAddValue() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("optionName").build(), new ArrayList<>());
 writeableCommandLine.addOption(new Option.Builder("optionName").build());
 writeableCommandLine.addValue(new Option.Builder("optionName").build(), "value1");
 writeableCommandLine.addValue(new Option.Builder("optionName").build(), "value2");
 List values = writeableCommandLine.getValues(new Option.Builder("optionName").build(), null);
 assertEquals(2, values.size());
 }

 @Test
 public void testGetOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("optionName").build(), new ArrayList<>());
 Option option = new Option.Builder("optionName").build();
 writeableCommandLine.addOption(option);
 Option result = writeableCommandLine.getOption("optionName");
 assertEquals(option, result);
 }

 @Test
 public void testGetValues() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("optionName").build(), new ArrayList<>());
 Option option = new Option.Builder("optionName").build();
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(2, values.size());
 }

 @Test
 public void testGetSwitch() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("optionName").build(), new ArrayList<>());
 Option option = new Option.Builder("optionName").build();
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 Boolean switchValue = writeableCommandLine.getSwitch(option, false);
 assertTrue(switchValue);
 }

 @Test
 public void testHasOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("optionName").build(), new ArrayList<>());
 Option option = new Option.Builder("optionName").build();
 writeableCommandLine.addOption(option);
 assertTrue(writeableCommandLine.hasOption(option));
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("optionName").build(), new ArrayList<>());
 assertTrue(writeableCommandLine.looksLikeOption("optionName"));
 }

 @Test
 public void testToString() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("optionName").build(), new ArrayList<>());
 Option option = new Option.Builder("optionName").build();
 writeableCommandLine.addOption(option);
 List arguments = new ArrayList();
 arguments.add("arg1");
 arguments.add("arg2");
 writeableCommandLine.addAllArguments(arguments);
 String result = writeableCommandLine.toString();
 String expected = "arg1 arg2";
 assertEquals(expected, result);
 }

 @Test
 public void testGetProperties() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("optionName").build(), new ArrayList<>());
 writeableCommandLine.addProperty("propertyName1", "propertyValue1");
 writeableCommandLine.addProperty("propertyName2", "propertyValue2");
 Set properties = writeableCommandLine.getProperties();
 assertEquals(2, properties.size());
 }
}