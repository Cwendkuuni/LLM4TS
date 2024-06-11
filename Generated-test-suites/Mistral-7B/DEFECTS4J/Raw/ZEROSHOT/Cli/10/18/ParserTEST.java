import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 // given
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");
 options.addOption("d", 4, "Option D");
 options.addOption("e", 5, "Option E");

 Parser parser = new DefaultParser();
 parser.setOptions(options);

 String[] arguments = new String[]{"--a", "-b", "valueB", "--c", "valueC", "-d", "valueD", "--e", "valueE", "arg1", "arg2"};
 CommandLine cmd = parser.parse(arguments);

 // when

 // then
 assertEquals(10, cmd.getArgs().size());
 assertEquals("valueB", cmd.getOptionValue("b"));
 assertEquals("valueC", cmd.getOptionValue("c"));
 assertEquals("valueD", cmd.getOptionValue("d"));
 assertEquals("valueE", cmd.getOptionValue("e"));
 assertFalse(cmd.hasOption("z"));
 assertSame(options.findOption("a"), cmd.getOption("a"));
 assertSame(options.findOption("b"), cmd.getOption("b"));
 assertSame(options.findOption("c"), cmd.getOption("c"));
 assertSame(options.findOption("d"), cmd.getOption("d"));
 assertSame(options.findOption("e"), cmd.getOption("e"));
 assertNull(cmd.getOptionValue("z"));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--z"};
 parser.parse(arguments);
 }

 @Test(expected = ParseException.class)
 public void testOneMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("a", true, "Option A");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--b"};
 parser.parse(arguments);
 }

 @Test(expected = ParseException.class)
 public void testAllRequiredOptionsMissing() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("a", true, "Option A");
 options.addRequiredOption("b", true, "Option B");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--c"};
 parser.parse(arguments);
 }

 @Test
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B", true, "Argument B");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--a", "--b"};
 try {
 parser.parse(arguments);
 } catch (ParseException e) {
 assertEquals("Missing argument for option: b", e.getMessage());
 }
 }

 @Test
 public void testOptionalArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B", false, "Argument B");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--a", "--b", "valueB"};
 CommandLine cmd = parser.parse(arguments);
 assertEquals("valueB", cmd.getOptionValue("b"));
 assertNull(cmd.getOptionValue("z"));
 }

 @Test
 public void testMultipleArguments() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B", true, "Argument B");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--a", "--b", "arg1", "arg2"};
 CommandLine cmd = parser.parse(arguments);
 assertEquals(2, cmd.getArgs().size());
 assertEquals("arg1", cmd.getArgs().get(0));
 assertEquals("arg2", cmd.getArgs().get(1));
 }

 @Test
 public void testLongOptions() throws ParseException {
 Options options = new Options();
 options.addOption("longOptionA", true, "Long Option A");
 options.addOption("longOptionB", 1, "Long Option B", true, "Long Argument B");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--long-option-a", "--long-option-b=longArgumentB"};
 CommandLine cmd = parser.parse(arguments);
 assertEquals("longArgumentB", cmd.getOptionValue("longOptionB"));
 assertNull(cmd.getOptionValue("shortOptionA"));
 }

 @Test
 public void testLongAndShortOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A (short)");
 options.addOption("longOptionA", true, "Long Option A");
 options.addOption("b", 1, "Option B (short)", true, "Argument B (short)");
 options.addOption("longOptionB", 1, "Long Option B", true, "Argument B (long)");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--a", "--long-option-a", "--b=valueBShort", "--long-option-b=valueBLong"};
 CommandLine cmd = parser.parse(arguments);
 assertEquals("valueBShort", cmd.getOptionValue("b"));
 assertEquals("valueBLong", cmd.getOptionValue("longOptionB"));
 assertNull(cmd.getOptionValue("z"));
 }

 @Test
 public void testOptionGroup() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group");
 group.setRequired(false);
 group.addOption("a", true, "Option A");
 group.addOption("b", true, "Option B");
 options.addOptionGroup(group);
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--group"};
 CommandLine cmd = parser.parse(arguments);
 assertTrue(group.isSelected());
 assertFalse(cmd.hasOption("z"));
 }

 @Test
 public void testOptionGroupRequired() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group");
 group.setRequired(true);
 group.addOption("a", true, "Option A");
 group.addOption("b", true, "Option B");
 options.addOptionGroup(group);
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--group"};
 try {
 parser.parse(arguments);
 } catch (ParseException e) {
 assertEquals("Missing group: group", e.getMessage());
 }
 }
}