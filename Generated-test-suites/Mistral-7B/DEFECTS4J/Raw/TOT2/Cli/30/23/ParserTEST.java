import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithSingleRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String option = "optionA";
        String arg = "valueA";

        options.addOption(option, true, "Option A");

        String[] arguments = {option + ":{" + arg + "}"};

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals(arg, cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithMultipleRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String optionA = "optionA";
        String optionB = "optionB";
        String argA = "valueA";
        String argB = "valueB";

        options.addOption(optionA, true, "Option A");
        options.addOption(optionB, true, "Option B");

        String[] arguments = {optionA + ":{" + argA + "}", optionB + ":{" + argB + "}"};

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals(argA, cmd.getArgs().get(0));
        Assert.assertEquals(argB, cmd.getArgs().get(1));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String option = "optionA";

        options.addOption(option, true, "Option A");

        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);
        Assert.fail(); // MissingOptionException should be thrown
    }

    @Test
    public void testParseWithSingleOptionalOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String option = "optionA";
        String arg = "valueA";

        options.addOption(option, true, "Option A");

        String[] arguments = {"--optionA"};

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals(arg, cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithMultipleOptionalOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String optionA = "optionA";
        String optionB = "optionB";
        String argA = "valueA";
        String argB = "valueB";

        options.addOption(optionA, true, "Option A");
        options.addOption(optionB, true, "Option B");

        String[] arguments = {"--optionA", "--optionB"};

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(argA, cmd.getOptionValue(optionA));
        Assert.assertEquals(argB, cmd.getOptionValue(optionB));
    }

    @Test
    public void testParseWithMixedOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String optionA = "optionA";
        String optionB = "optionB";
        String argA = "valueA";
        String argB = "valueB";

        options.addOption(optionA, true, "Option A");
        options.addOption(optionB, true, "Option B");

        String[] arguments = {"--optionA", "valueC", "--optionB", "valueD"};

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("valueC", cmd.getArgs().get(0));
        Assert.assertEquals("valueD", cmd.getArgs().get(1));
        Assert.assertEquals(argA, cmd.getOptionValue(optionA));
        Assert.assertEquals(argB, cmd.getOptionValue(optionB));
    }
}