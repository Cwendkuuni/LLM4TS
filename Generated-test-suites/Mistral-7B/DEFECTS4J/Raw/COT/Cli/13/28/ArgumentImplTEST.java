import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

 private CommandLine cmd;
 private Option option;

 @Before
 public void setUp() {
 cmd = new CommandLine();
 option = OptionBuilder.withLongOpt("myOption")
 .withDescription("This is a test option")
 .withArgName("value")
 .withMinValues(1)
 .withMaxValues(3)
 .withValueSeparator('=')
 .withValidator(new Validator() {
 @Override
 public void validate(List values) throws InvalidArgumentException {
 // validator logic here
 }
 })
 .create();
 }

 @Test
 public void testArgumentImplBasic() {
 ArgumentImpl arg = new ArgumentImpl("myOption", "", 1, 3, '=', '=', null, "--", null, Option.NO_ARG);
 arg.validate(cmd);
 List<String> values = cmd.getValues(option);
 assertEquals(1, values.size());
 assertNotNull(values.get(0));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testArgumentMinMaxInvalid() {
 ArgumentImpl arg = new ArgumentImpl("myOption", "", -1, 3, '=', '=', null, "--", null, Option.NO_ARG);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testArgumentMinMaxMaxInvalid() {
 ArgumentImpl arg = new ArgumentImpl("myOption", "", 1, 0, '=', '=', null, "--", null, Option.NO_ARG);
 }

 @Test
 public void testArgumentConsumeRemaining() {
 ArgumentImpl arg = new ArgumentImpl("myOption", "", 1, 3, '=', '=', null, "--", null, Option.NO_ARG);
 arg.validate(cmd);
 cmd.parseArgument("--consume");
 List<String> values = cmd.getValues(option);
 assertEquals(2, values.size());
 assertEquals("--consume", values.get(0));
 assertEquals("", values.get(1));
 }

 @Test
 public void testArgumentSubsequentSplit() {
 ArgumentImpl arg = new ArgumentImpl("myOption", "", 1, 3, '=', ';', null, "--", null, Option.NO_ARG);
 arg.validate(cmd);
 cmd.parseArgument("value1;value2;value3");
 List<String> values = cmd.getValues(option);
 assertEquals(3, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 assertEquals("value3", values.get(2));
 }

 @Test (expected = OptionException.class)
 public void testArgumentInvalidValue() {
 ArgumentImpl arg = new ArgumentImpl("myOption", "", 1, 3, '=', '=', null, "--", null, Option.NO_ARG);
 arg.validate(cmd);
 cmd.parseArgument("--option=invalid");
 }

 @Test
 public void testArgumentDefaultValues() {
 ArgumentImpl arg = new ArgumentImpl("myOption", "", 1, 3, '=', '=', null, "--", Arrays.asList("default1", "default2"), Option.NO_ARG);
 arg.validate(cmd);
 cmd.parseArgument("--option");
 List<String> values = cmd.getValues(option);
 assertEquals(2, values.size());
 assertEquals("default1", values.get(0));
 assertEquals("default2", values.get(1));
 }

 @Test
 public void testArgumentHelp() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp("Test", Arrays.asList("myOption"), true);
 }
}