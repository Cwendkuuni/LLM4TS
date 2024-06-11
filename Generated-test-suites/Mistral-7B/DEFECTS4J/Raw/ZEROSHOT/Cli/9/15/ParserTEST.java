import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParseEmpty() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0]);
 assertEquals(0, cmd.getArgs().size());
 assertFalse(cmd.hasOption("option"));
 }

 @Test
 public void testParseOneArg() throws ParseException {
 Options options = new Options();
 options.addOption("option", true, "o");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"o"});
 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.hasOption("option"));
 }

 @Test
 public void testParseTwoArgs() throws ParseException {
 Options options = new Options();
 options.addOption("option", true, "o");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"o", "arg"});
 assertEquals(1, cmd.getArgs().size());
 assertEquals("arg", cmd.getArgs().get(0));
 assertTrue(cmd.hasOption("option"));
 }

 @Test
 public void testParseOneRequiredOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("option", true, "o");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 assertThrows(MissingOptionException.class, () -> {
 parser.parse(options, new String[0]);
 });
 CommandLine cmd = parser.parse(options, new String[]{"o"});
 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.hasOption("option"));
 }

 @Test
 public void testParseTwoRequiredOptions() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("option1", true, "o1");
 options.addRequiredOption("option2", true, "o2");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 assertThrows(MissingOptionException.class, () -> {
 parser.parse(options, new String[0]);
 });
 CommandLine cmd = parser.parse(options, new String[]{"o1", "o2"});
 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.hasOption("option1"));
 assertTrue(cmd.hasOption("option2"));
 }

 @Test
 public void testParseOneOptionalOption() throws ParseException {
 Options options = new Options();
 options.addOption("option", true, "o");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"o"});
 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.hasOption("option"));
 CommandLine cmd2 = parser.parse(options, new String[]{"o", "arg"});
 assertEquals(1, cmd2.getArgs().size());
 assertEquals("arg", cmd2.getArgs().get(0));
 assertTrue(cmd2.hasOption("option"));
 }

 @Test
 public void testParseOneOptionalOptionWithValue() throws ParseException {
 Options options = new Options();
 options.addOption("option", true, "o");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"o", "value"});
 assertEquals(1, cmd.getArgs().size());
 assertEquals("value", cmd.getArgs().get(0));
 assertTrue(cmd.hasOption("option"));
 }

 @Test
 public void testParseOneOptionalOptionGroup() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group");
 group.addRequiredOption("option1", true, "o1");
 group.addRequiredOption("option2", true, "o2");
 options.addOptionGroup(group);
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 assertThrows(MissingOptionException.class, () -> {
 parser.parse(options, new String[0]);
 });
 CommandLine cmd = parser.parse(options, new String[]{"o1", "o2"});
 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.hasOption("option1"));
 assertTrue(cmd.hasOption("option2"));
 }

 @Test
 public void testParseOneUnrecognizedOption() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 assertThrows(UnrecognizedOptionException.class, () -> {
 parser.parse(options, new String[]{"x"});
 });
 }

 @Test
 public void testParseMultipleUnrecognizedOptions() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 assertThrows(UnrecognizedOptionException.class, () -> {
 parser.parse(options, new String[]{"x", "y"});
 });
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("option", true, "o");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 Properties properties = new Properties();
 properties.setProperty("option", "value");
 CommandLine cmd = parser.parse(options, new String[0], properties);
 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.hasOption("option"));
 assertEquals("value", cmd.getOptionValue("option"));
 }
}