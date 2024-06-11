
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParse1() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "value1", "-b", "value2"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals("value1", cmd.getOptionValue("a"));
        Assert.assertEquals("value2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParse2() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "value1", "-b", "value2", "-c"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testParse3() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--input", "file.txt"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals("file.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testParse4() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "-b"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParse5() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("--", cmd.getArgs()[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse6() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "-b", "-c"};
        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse7() throws ParseException {
        Options options = new Options();
        Option inputOption = new Option("i", "input", true, "input file");
        inputOption.setRequired(true);
        options.addOption(inputOption);
        CommandLineParser parser = new DefaultParser();
        String[] args = {};
        CommandLine cmd = parser.parse(options, args);
    }

    @Test
    public void testParse8() throws ParseException {
        Options options = new Options();
        Option inputOption = new Option("i", "input", true, "input file");
        inputOption.setRequired(true);
        options.addOption(inputOption);
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-i", "file.txt"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals("file.txt", cmd.getOptionValue("input"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse9() throws ParseException {
        Options options = new Options();
        Option inputOption = new Option("i", "input", true, "input file");
        inputOption.setRequired(true);
        options.addOption(inputOption);
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-o", "output.txt"};
        CommandLine cmd = parser.parse(options, args);
    }

}
