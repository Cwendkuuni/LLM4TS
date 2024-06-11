import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Parser;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 commandLine = new CliBuilder().build();
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("output").hasArg(true).build();
 commandLine.addOption(option);
 commandLine.addValue(option, "output.txt");
 List<String> values = commandLine.getUndefaultedValues(option);
 assert values.size() == 1;
 assert values.get(0).equals("output.txt");
 }

 @Test
 public void testAddValueToOptionWithSingleValue() {
 Option option = Option.builder("o").longOpt("output").hasArg(true).build();
 commandLine.addOption(option);
 commandLine.addValue(option, "output.txt");
 Option parsedOption = Parser.parse(commandLine, new String[]{"-o", "output.txt"}).getOptionNamed("output");
 assert parsedOption.getValue().equals("output.txt");
 }

 @Test
 public void testAddValueToOptionWithMultipleValues() {
 Option option = Option.builder("i").longOpt("input").hasArg(true).isMultipleValuesSelectable(true).build();
 commandLine.addOption(option);
 commandLine.addValue(option, "input1.txt");
 commandLine.addValue(option, "input2.txt");
 List<String> values = commandLine.getUndefaultedValues(option);
 assert values.size() == 2;
 assert values.get(0).equals("input1.txt");
 assert values.get(1).equals("input2.txt");
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("i").longOpt("input").hasArg(true).isRequired(false).build();
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, new ArrayList<>());
 commandLine.addValue(option, "input1.txt");
 commandLine.addValue(option, "input2.txt");
 List<String> values = commandLine.getUndefaultedValues(option);
 assert values.size() == 2;
 assert values.get(0).equals("input1.txt");
 assert values.get(1).equals("input2.txt");

 Option optionWithNoValues = Option.builder("d").longOpt("debug").isRequired(false).build();
 commandLine.addOption(optionWithNoValues);
 List<String> emptyValues = commandLine.getUndefaultedValues(optionWithNoValues);
 assert emptyValues.isEmpty();
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("output").hasArg(true).build();
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, new ArrayList<>());
 commandLine.setDefaultValues(option, new ArrayList<>()); // should not throw an error

 List<String> values = commandLine.getUndefaultedValues(option);
 assert values.isEmpty();
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("d").longOpt("debug").hasArg(false).build();
 commandLine.addOption(option);
 assert !option.getValue().isPresent();
 commandLine.setDefaultSwitch(option, true);
 assert option.getValue().isPresent() && option.getValue().get().equals(Boolean.TRUE);
 commandLine.addSwitch(option, false);
 assert !option.getValue().isPresent();
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("p").longOpt("property").hasArg(true).build();
 commandLine.addOption(option);
 commandLine.addProperty(option, "name", "value1");
 commandLine.addProperty(option, "name", "value2");
 List<String> values = commandLine.getUndefaultedValues(option);
 assert values.size() == 1;
 assert values.get(0).equals("value2"); // last value overrides the previous one
 }

 @Test
 public void testLooksLikeOption() {
 assert commandLine.looksLikeOption("--output");
 assert !commandLine.looksLikeOption("--input");
 }

 @Test
 public void testSetCurrentOption() {
 Option option = Option.builder("o").longOpt("output").isRequired(false).build();
 commandLine.addOption(option);
 Option parsedOption = Parser.parse(commandLine, new String[]{"--output", "output.txt"}).getOptionNamed("output");
 commandLine.setCurrentOption(parsedOption);
 Option currentOption = commandLine.getCurrentOption();
 assert currentOption.equals(parsedOption);
 }
}