import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testSetOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.setOptions(options);
 assertSame(options, parser.getOptions());
 }

 @Test
 public void testGetOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.setOptions(options);
 assertSame(options, parser.getOptions());
 }

 @Test
 public void testGetRequiredOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 List requiredOptions = parser.getRequiredOptions();
 assertTrue(requiredOptions.isEmpty());

 options.addRequiredOption("option1", false, null, "value1");
 requiredOptions = parser.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals("option1", requiredOptions.get(0).getKey());
 assertFalse(requiredOptions.get(0).isOptional());
}

 @Test
 public void testFlatten_simple() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = {"arg1", "arg2", "--opt"};
 List<String> result = parser.flatten(options, arguments, false);
 assertEquals(3, result.size());
 assertEquals("arg1", result.get(0));
 assertEquals("arg2", result.get(1));
 assertEquals("--opt", result.get(2));
}

 @Test
 public void testParse_noOptions() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = new String[0];
 CommandLine cmd = parser.parse(new Options(), arguments);
 assertTrue(cmd.getArgs().isEmpty());
}

 @Test
 public void testParse_singleOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("opt", false, null, "value");
 String[] arguments = {"--opt"};
 CommandLine cmd = parser.parse(options, arguments);
 assertTrue(cmd.getArgs().isEmpty());
 assertTrue(cmd.getOptions().size() > 0);
 Option option = cmd.getOptionByName("opt");
 assertEquals("opt", option.getKey());
 assertFalse(option.hasArg());
}

 @Test
 public void testParse_optionWithArg() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("opt", true, null, "value");
 String[] arguments = {"--opt", "arg"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("arg", cmd.getArgs().get(0));
 assertTrue(cmd.getOptions().size() > 0);
 Option option = cmd.getOptionByName("opt");
 assertEquals("opt", option.getKey());
 assertTrue(option.hasArg());
 assertEquals("arg", option.getValue());
}

 @Test(expected = MissingOptionException.class)
 public void testParse_missingRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("opt", false, null, "value");
 String[] arguments = new String[0];
 CommandLine cmd = parser.parse(options, arguments);
 fail("MissingOptionException expected");
}

 @Test(expected = UnrecognizedOptionException.class)
 public void testParse_unrecognizedOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("opt", false, null, "value");
 String[] arguments = {"--unk"};
 CommandLine cmd = parser.parse(options, arguments);
 fail("UnrecognizedOptionException expected");
}

 @Test
 public void testParse_mixedOptionsAndArgs() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("opt1", false, null, "value1");
 options.addOption("opt2", false, null, "value2");
 options.addOption("opt3", true, null, "value3");
 String[] arguments = {"--opt1", "--opt2", "arg1", "--opt3", "arg2"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(2, cmd.getArgs().size());
 assertEquals("arg1", cmd.getArgs().get(0));
 assertEquals("arg2", cmd.getArgs().get(1));
 assertTrue(cmd.getOptions().size() > 2);
 Option opt1 = cmd.getOptionByName("opt1");
 assertEquals("opt1", opt1.getKey());
 assertFalse(opt1.hasArg());
 Option opt2 = cmd.getOptionByName("opt2");
 assertEquals("opt2", opt2.getKey());
 assertFalse(opt2.hasArg());
 Option opt3 = cmd.getOptionByName("opt3");
 assertEquals("opt3", opt3.getKey());
 assertTrue(opt3.hasArg());
 assertEquals("arg2", opt3.getValue());
}

 @Test
 public void testParse_properties() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Properties properties = new Properties();
 properties.setProperty("opt1", "arg1");
 properties.setProperty("opt2.arg", "arg2");
 CommandLine cmd = parser.parse(options, (String[]) null, properties);
 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.getOptions().size() > 2);
 Option opt1 = cmd.getOptionByName("opt1");
 assertEquals("opt1", opt1.getKey());
 assertTrue(opt1.hasArg());
 assertEquals("arg1", opt1.getValue());
 Option opt2 = cmd.getOptionByName("opt2");
 assertEquals("opt2", opt2.getKey());
 assertTrue(opt2.hasOptionalArg());
 Option opt3 = cmd.getOptionByName("opt3");
 assertNull(opt3);
 Option opt4 = cmd.getOptionByName("opt2.arg");
 assertNotNull(opt4);
 assertEquals("arg2", opt4.getValue());
}

 @Test
 public void testParse_stopAtNonOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = {"arg1", "--opt1", "arg2", "--opt2"};
 CommandLine cmd = parser.parse(options, arguments, true);
 assertEquals(3, cmd.getArgs().size());
 assertEquals("arg1", cmd.getArgs().get(0));
 assertEquals("--opt1", cmd.getArgs().get(1));
 assertEquals("arg2", cmd.getArgs().get(2));
}

}