import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testCreateOptionWithDefaultValues() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create('optionName');

 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertFalse(option.hasArg());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getValueSeparator());
 assertNull(option.getArgName());
 }

 @Test
 public void testCreateOptionWithLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("--longOpt")
 .create('optionName');

 assertEquals("--longOpt", option.getLongOpt());
 assertFalse(option.isRequired());
 assertFalse(option.hasArg());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getValueSeparator());
 assertNull(option.getArgName());
 }

 @Test
 public void testCreateOptionRequired() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.isRequired()
 .create('optionName');

 assertFalse(option.isRequired()); // initial value is false
 assertTrue(option.isRequired()); // set to true
 }

 @Test
 public void testCreateOptionWithArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArg()
 .create('optionName');

 assertTrue(option.hasArg());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 }

 @Test
 public void testCreateOptionWithArgName() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withArgName("argName")
 .create('optionName');

 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testCreateOptionWithOptionalArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasOptionalArg()
 .create('optionName');

 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testCreateOptionWithArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArgs(2)
 .create('optionName');

 assertEquals(2, option.getArgs());
 }

 @Test
 public void testCreateOptionWithValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withValueSeparator(';')
 .create('optionName');

 assertEquals(';', option.getValueSeparator());
 }

 @Test
 public void testCreateOptionWithType() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withType(String.class)
 .create('optionName');

 assertEquals(String.class, option.getType());
 }

 @Test
 public void testCreateOptionWithDescription() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withDescription("Option description")
 .create('optionName');

 assertEquals("Option description", option.getDescription());
 }

 @Test
 public void testCreateOptionWithAllProperties() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.isRequired()
 .withLongOpt("--longOpt")
 .hasArg()
 .withArgName("argName")
 .hasOptionalArg()
 .withValueSeparator(';')
 .withType(String[].class)
 .withDescription("Option description")
 .create('optionName');

 assertTrue(option.isRequired());
 assertEquals("--longOpt", option.getLongOpt());
 assertTrue(option.hasArg());
 assertEquals("argName", option.getArgName());
 assertTrue(option.hasOptionalArg());
 assertEquals(';', option.getValueSeparator());
 assertEquals(String[].class, option.getType());
 assertEquals("Option description", option.getDescription());
 }

 @Test
 public void testCreateOptions() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Options options = new Options();
 Option option1 = optionBuilder.withLongOpt("--option1")
 .create('o');
 Option option2 = optionBuilder.isRequired()
 .withLongOpt("--option2")
 .hasArg()
 .create('p');
 Option option3 = optionBuilder.withLongOpt("--option3")
 .create('x');

 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);

 Options parsedOptions = new CommandLineParser().parse(options, new String[]{"-o", "value1", "--option2", "value2"});

 assertEquals(Option.valueOf('o'), parsedOptions.getOptions().get(0).getValueName());
 assertEquals("value1", parsedOptions.getOptions().get(0).getValue());
 assertEquals(Option.VALUE_PRESENT, parsedOptions.getOptions().get(0).getArgs().length);
 assertEquals(Option.VALUE_PRESENT, parsedOptions.getOptions().get(1).getArgs().length);
 assertFalse(parsedOptions.getOptions().get(1).getValueName().equals("p")); // value name and option name are different
 assertEquals("value2", parsedOptions.getOptions().get(1).getValue());
 assertEquals(Option.VALUE_ABSENT, parsedOptions.getOptions().get(2).getArgs().length);
 }
}