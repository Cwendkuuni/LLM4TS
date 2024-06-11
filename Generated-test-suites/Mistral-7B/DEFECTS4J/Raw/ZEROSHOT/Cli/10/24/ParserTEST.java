import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", 3, "Option C with argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = new String[]{"-a", "true", "-b", "valueB", "--c", "valueC"};
        CommandLine cmd = parser.parse(args);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("true", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b", "defaultValueB"));
        Assert.assertEquals(3, cmd.getOptions().size());
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));

        options.addRequiredOption("d", "optionD", false, "Option D is required");

        try {
            parser.parse(new String[0][], options);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: d", e.getMessage());
        }

        String[] argsWithD = new String[]{"-d", "-a", "true", "-b", "valueB", "--c", "valueC"};
        cmd = parser.parse(argsWithD, options);
        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("true", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b", "defaultValueB"));
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));
        Assert.assertEquals(true, cmd.hasOption("d"));
        Assert.assertNull(cmd.getOptionValue("d"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = new String[]{"-z", "valueZ"};
        parser.parse(args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = new String[]{"-x", "valueX"};
        parser.parse(args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A with no argument");
        options.addOption("c", "optionC", 1, "Option C with required argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = new String[]{"-a", "-c"};
        parser.parse(args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingGroupArgument() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption("g1", "groupOption1");
        group.addOption("g2", "groupOption2");
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("g", "group", true, "Option group");
        options.addOptions(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = new String[]{"-g"};
        parser.parse(args);
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", 3, "Option C with argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = new String[]{"--", "-a", "true", "-b", "valueB", "--c", "valueC"};
        CommandLine cmd = parser.parse(args, options, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("true", cmd.getOptionValue("a"));
        Assert.assertEquals(false, cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));
        Assert.assertEquals(3, cmd.getOptions().size());
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));
    }

    @Test
    public void testParseStopAtNonOptionWithProperty() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", 3, "Option C with argument");

        Properties properties = new Properties();
        properties.put("a", "true");
        properties.put("b", "valueB");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = new String[]{"--", "--property-file", "file.properties"};
        CommandLine cmd = parser.parse(args, options, properties, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("true", cmd.getOptionValue("a"));
        Assert.assertEquals(false, cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));
        Assert.assertEquals(3, cmd.getOptions().size());
        Assert.assertEquals("valueB", cmd.getOptionValue("b", "defaultValueB"));
    }
}