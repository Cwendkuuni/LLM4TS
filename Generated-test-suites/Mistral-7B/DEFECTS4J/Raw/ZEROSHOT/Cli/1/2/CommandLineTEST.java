import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandLineTest {

 @BeforeEach
 public void setUp() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "Option A");
 options.addOption("b", "optionB", false, "Option B");
 }

 @Test
 public void testHasOption() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-a"});
 Assertions.assertTrue(commandLine.hasOption("a"));
 Assertions.assertFalse(commandLine.hasOption("b"));

 commandLine = new CommandLineParser().parse(options, new String[]{"-b"});
 Assertions.assertFalse(commandLine.hasOption("a"));
 Assertions.assertTrue(commandLine.hasOption("b"));
 }

 @Test
 public void testGetOptionValue() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-a", "value"});
 Assertions.assertEquals("value", commandLine.getOptionValue("a"));
 Assertions.assertNull(commandLine.getOptionValue("b"));
 }

 @Test
 public void testGetOptionValues() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-a", "value1", "value2"});
 String[] values = commandLine.getOptionValues("a");
 Assertions.assertArrayEquals(new String[]{"value1", "value2"}, values);
 }

 @Test
 public void testHasNoOption() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"test"});
 Assertions.assertFalse(commandLine.hasOption("a"));
 Assertions.assertFalse(commandLine.hasOption("b"));
 }

 @Test
 public void testGetArgs() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-a", "value", "test"});
 String[] args = commandLine.getArgs();
 Assertions.assertArrayEquals(new String[]{"test"}, args);
 }
}