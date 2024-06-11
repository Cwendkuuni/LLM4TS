import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for Parser.
 */
public class ParserTest {

    /**
     * Test parse method with valid options and arguments.
     */
    @Test
    public void testParseValidOptionsAndArguments() {
        String[] args = new String[]{"-a", "1", "--optionB", "2", "--optionC", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.assertNotNull(cmd);
            Assert.assertEquals(3, cmd.getArgs().size());
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertTrue(cmd.hasOption("b"));
            Assert.assertTrue(cmd.hasOption("c"));
            Assert.assertEquals("1", cmd.getOptionValue("a"));
            Assert.assertEquals("2", cmd.getOptionValue("b"));
            Assert.assertEquals("3", cmd.getOptionValue("c"));
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test parse method with missing required option.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() {
        String[] args = new String[]{"--optionA", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A", true);
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    /**
     * Test parse method with invalid option.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() {
        String[] args = new String[]{"--invalidOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    /**
     * Test parse method with extra arguments.
     */
    @Test
    public void testParseExtraArguments() {
        String[] args = new String[]{"-a", "1", "--optionB", "2", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.assertNotNull(cmd);
            Assert.assertEquals(2, cmd.getArgs().size());
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertTrue(cmd.hasOption("b"));
            Assert.assertEquals("1", cmd.getOptionValue("a"));
            Assert.assertEquals("2", cmd.getOptionValue("b"));
            Assert.assertEquals("3", cmd.getArg(0));
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test parse method with long options.
     */
    @Test
    public void testParseLongOptions() {
        String[] args = new String[]{"--longOptionA", "1", "--long-optionB", "2"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.assertNotNull(cmd);
            Assert.assertEquals(0, cmd.getArgs().size());
            Assert.assertTrue(cmd.hasOption("longOptionA"));
            Assert.assertTrue(cmd.hasOption("longOptionB"));
            Assert.assertEquals("1", cmd.getOptionValue("longOptionA"));
            Assert.assertEquals("2", cmd.getOptionValue("longOptionB"));
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test parse method with options and arguments in any order.
     */
    @Test
    public void testParseOptionsAndArgumentsAnyOrder() {
        String[] args = new String[]{"1", "--optionA", "3", "--optionB"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.assertNotNull(cmd);
            Assert.assertEquals(1, cmd.getArgs().size());
            Assert.assertEquals("1", cmd.getArg(0));
            Assert.assertTrue(cmd.hasOption("optionA"));
            Assert.assertTrue(cmd.hasOption("optionB"));
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test parse method with an option with an optional argument.
     */
    @Test
    public void testParseOptionalArgument() {
        String[] args = new String[]{"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A", "value");

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.assertNotNull(cmd);
            Assert.assertEquals(0, cmd.getArgs().size());
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertEquals("value", cmd.getOptionValue("a"));
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test parse method with an option with a required argument.
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseRequiredArgument() {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A", "value");

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    /**
     * Test parse method with an option group.
     */
    @Test
    public void testParseOptionGroup() {
        String[] args = new String[]{"--groupA", "--option1"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("groupA", "Group A");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption(new Option("option1", false, "Option 1 in Group A"));

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.assertNotNull(cmd);
            Assert.assertEquals(0, cmd.getArgs().size());
            Assert.assertTrue(cmd.hasOption("groupA.option1"));
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test parse method with an option group and required options.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseOptionGroupWithRequiredOptions() {
        String[] args = new String[]{"--groupA"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("groupA", "Group A");
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption(new Option("option1", false, "Option 1 in Group A"));
        group.addOption(new Option("option2", false, "Option 2 in Group A"));

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    /**
     * Test parse method with a property file.
     */
    @Test
    public void testParsePropertyFile() {
        String[] args = new String[]{"--file", "test.properties"};
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB.value", "valueB");

        Options options = new Options();
        options.addOption("file", true, "File containing options and values");
        options.addOption("optionA", true, "Option A", true);
        options.addOption("optionB", true, "Option B", true);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args, properties);
            Assert.assertNotNull(cmd);
            Assert.assertEquals(0, cmd.getArgs().size());
            Assert.assertTrue(cmd.hasOption("optionA"));
            Assert.assertEquals("valueA", cmd.getOptionValue("optionA"));
            Assert.assertTrue(cmd.hasOption("optionB"));
            Assert.assertEquals("valueB", cmd.getOptionValue("optionB.value"));
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test parse method with an empty property file.
     */
    @Test
    public void testParseEmptyPropertyFile() {
        String[] args = new String[]{"--file", "test.properties"};
        Properties properties = new Properties();

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(new Options(), args, properties);
            Assert.assertEquals(0, cmd.getArgs().size());
            Assert.assertNull(cmd);
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test parse method with an invalid property file.
     */
    @Test(expected = IOException.class)
    public void testParseInvalidPropertyFile() {
        String[] args = new String[]{"--file", "test.properties"};
        Properties properties = new Properties();

        try {
            parser.parse(new Options(), args, properties);
            fail("Should have thrown an IOException for an invalid property file");
        } catch (IOException e) {
            // expected
        }
    }
}