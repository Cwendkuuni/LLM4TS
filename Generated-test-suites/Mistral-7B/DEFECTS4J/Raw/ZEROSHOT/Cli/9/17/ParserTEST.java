import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParseWithNoArguments() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[0]);
 assertEquals(0, cmd.getArgs().size());
 assertEquals("output", cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("nonExistentOption"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() throws Exception {
 Options options = new Options();
 options.addRequiredOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 String[] args = new String[0];
 parser.parse(options, args);
 }

 @Test
 public void testParseWithSingleArgument() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 String[] args = new String[]{"outputFile.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("outputFile.txt", cmd.getArgs().get(0));
 assertEquals("outputFile.txt", cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("nonExistentOption"));
 }

 @Test
 public void testParseWithSingleOptionalArgument() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 String[] args = new String[]{"outputFile.txt", "-o", "outputFile2.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("outputFile2.txt", cmd.getArgs().get(0));
 assertEquals("outputFile2.txt", cmd.getOptionValue("output"));
 assertEquals("outputFile.txt", cmd.getOptionValue("output", true));
 }

 @Test
 public void testParseWithMultipleArguments() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 String[] args = new String[]{"outputFile1.txt", "outputFile2.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(2, cmd.getArgs().size());
 assertEquals("outputFile1.txt", cmd.getArgs().get(0));
 assertEquals("outputFile2.txt", cmd.getArgs().get(1));
 assertNull(cmd.getOptionValue("nonExistentOption"));
 }

 @Test
 public void testParseWithMultipleOptions() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 options.addOption("h", "help", false, "print help message");
 Parser parser = new DefaultParser();
 String[] args = new String[]{"-h", "-o", "outputFile.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("help"));
 assertTrue(cmd.hasOption("output"));
 assertEquals("outputFile.txt", cmd.getOptionValue("output"));
 assertFalse(cmd.hasOption("nonExistentOption"));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseWithUnrecognizedOption() throws Exception {
 Options options = new Options();
 Parser parser = new DefaultParser();
 String[] args = new String[]{"-x", "outputFile.txt"};
 parser.parse(options, args);
 }

 @Test
 public void testParseWithOptionGroup() throws Exception {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group options");
 group.setExclusive(false);
 group.addOption(new Option("o", "output", true, "output file"));
 group.addOption(new Option("p", "prefix", true, "prefix for output file"));
 options.addOptionGroup(group);
 Parser parser = new DefaultParser();
 String[] args = new String[]{"-o", "outputFile.txt", "-p", "prefix_"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("outputFile.txt", cmd.getArgs().get(0));
 assertEquals("prefix_outputFile.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithOptionGroupAndExclusiveOptions() throws Exception {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group options");
 group.setExclusive(true);
 group.addOption(new Option("o", "output", true, "output file"));
 group.addOption(new Option("p", "prefix", true, "prefix for output file"));
 options.addOptionGroup(group);
 Parser parser = new DefaultParser();
 String[] args = new String[]{"-o", "outputFile.txt", "-p", "prefix_"};
 CommandLine cmd = parser.parse(options, args);
 assertFalse(cmd.hasOption("output"));
 assertFalse(cmd.hasOption("prefix"));
 assertNull(cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("prefix"));
 assertEquals(1, cmd.getArgs().size());
 assertEquals("outputFile.txt", cmd.getArgs().get(0));
 }

 @Test
 public void testParseWithMultipleOptionsInOneGroup() throws Exception {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group options");
 group.setExclusive(false);
 group.addOption(new Option("o", "output", true, "output file"));
 group.addOption(new Option("p", "prefix", true, "prefix for output file"));
 options.addOptionGroup(group);
 Parser parser = new DefaultParser();
 String[] args = new String[]{"-o", "outputFile.txt", "-p", "prefix_", "-o", "outputFile2.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("outputFile2.txt", cmd.getArgs().get(0));
 assertEquals("prefix_outputFile2.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithMultipleOptionGroups() throws Exception {
 Options options = new Options();
 OptionGroup group1 = new OptionGroup("group1", "group1 options");
 group1.addOption(new Option("o1", "option1", true, "option1 value"));
 OptionGroup group2 = new OptionGroup("group2", "group2 options");
 group2.addOption(new Option("o2", "option2", true, "option2 value"));
 options.addOptionGroup(group1);
 options.addOptionGroup(group2);
 Parser parser = new DefaultParser();
 String[] args = new String[]{"-o1", "value1", "-o2", "value2"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(0, cmd.getArgs().size());
 assertEquals("value1", cmd.getOptionValue("option1"));
 assertEquals("value2", cmd.getOptionValue("option2"));
 }

 @Test
 public void testParseWithMultipleOptionGroupsAndExclusiveOptions() throws Exception {
 Options options = new Options();
 OptionGroup group1 = new OptionGroup("group1", "group1 options");
 group1.setExclusive(true);
 group1.addOption(new Option("o1", "option1", true, "option1 value"));
 OptionGroup group2 = new OptionGroup("group2", "group2 options");
 group2.setExclusive(true);
 group2.addOption(new Option("o2", "option2", true, "option2 value"));
 options.addOptionGroup(group1);
 options.addOptionGroup(group2);
 Parser parser = new DefaultParser();
 String[] args = new String[]{"-o1", "value1", "-o2", "value2"};
 CommandLine cmd = parser.parse(options, args);
 assertFalse(cmd.hasOption("option1"));
 assertFalse(cmd.hasOption("option2"));
 assertNull(cmd.getOptionValue("option1"));
 assertNull(cmd.getOptionValue("option2"));
 }

 @Test
 public void testParseWithOptionValueContainingSpace() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 String[] args = new String[]{"outputFile.txt with spaces.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("outputFile.txt with spaces.txt", cmd.getArgs().get(0));
 assertEquals("outputFile.txt with spaces.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithSingleDashOption() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 String[] args = new String[]{"-o", "outputFile.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("outputFile.txt", cmd.getArgs().get(0));
 assertEquals("outputFile.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithSingleDashAndTwoArguments() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 String[] args = new String[]{"-o", "outputFile1.txt", "outputFile2.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("outputFile1.txt", cmd.getArgs().get(0));
 assertNull(cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithTwoSingleDashOptions() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 String[] args = new String[]{"--", "outputFile1.txt", "outputFile2.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(2, cmd.getArgs().size());
 assertEquals("outputFile1.txt", cmd.getArgs().get(0));
 assertEquals("outputFile2.txt", cmd.getArgs().get(1));
 assertNull(cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithTwoSingleDashOptionsAndValues() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 String[] args = new String[]{"--outputFile1.txt=value1", "--outputFile2.txt=value2"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(0, cmd.getArgs().size());
 assertEquals("value1", cmd.getOptionValue("outputFile1.txt"));
 assertEquals("value2", cmd.getOptionValue("outputFile2.txt"));
 }

 @Test
 public void testParseWithTwoSingleDashOptionsAndValuesWithSpace() throws Exception {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 String[] args = new String[]{"--outputFile1.txt=value with spaces", "--outputFile2.txt=value2"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals(0, cmd.getArgs().size());
 assertEquals("value with spaces", cmd.getOptionValue("outputFile1.txt"));
 assertEquals("value2", cmd.getOptionValue("outputFile2.txt"));
 }
}