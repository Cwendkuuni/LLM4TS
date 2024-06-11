import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Parser;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new CliParser().parse(new String[]{"-a", "--optionA", "valueA", "--optionB=valueB", "--optionC=valueC"});
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("test").longOpt("testOption").required(false).build();
 commandLine.addOption(option);

 Parser parser = new DefaultParser();
 List<Option> options = commandLine.getOptions();

 try {
 parser.parse(Arrays.asList("--test".split(" ")), commandLine);
 } catch (ParseException e) {
 // Expected
 }

 List<String> args = commandLine.getArgList();
 List<String> testArgs = Arrays.asList("--test");

 assert(args.containsAll(testArgs));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("option").longOpt("option").valueSeparator('=').numberOfValues(2).required(false).build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");

 List<String> result = commandLine.getUndefaultedValues(option);
 List<String> expected = Arrays.asList("value1", "value2");

 assert(result.equals(expected));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("switch").longOpt("switch").build();

 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("switch").longOpt("switch").build();
 commandLine.setDefaultSwitch(option, false);

 assert(!option.isSet());
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("property").longOpt("property").build();
 commandLine.addProperty(option, "name", "value");

 String result = commandLine.getOptionValue(option, "name");
 String expected = "value";

 assert(result.equals(expected));
 }

 @Test
 public void testLooksLikeOption() {
 assert(commandLine.looksLikeOption("--option"));
 assert(!commandLine.looksLikeOption("valueA"));
 }
}