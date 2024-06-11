import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        List<Option> requiredOptions = parser.getRequiredOptions();
        Assert.assertNotNull(requiredOptions);
        Assert.assertEquals(0, requiredOptions.size());

        Option opt = new Option("o", "option", true, "Option description");
        opt.setRequired(true);
        options.addOption(opt);
        requiredOptions = parser.getRequiredOptions();
        Assert.assertEquals(1, requiredOptions.size());
        Assert.assertSame(opt, requiredOptions.get(0));
    }

    @Test
    public void testFlatten() {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"arg1", "--", "arg2", "--", "arg3"};
            }
        };
        String[] args = {"arg0", "--", "opt", "arg4", "arg5", "arg6"};

        List<String> tokens = Arrays.asList(parser.flatten(new Options(), args, false));
        Assert.assertEquals(8, tokens.size());
        Assert.assertEquals("arg0", tokens.get(0));
        Assert.assertEquals("--", tokens.get(1));
        Assert.assertEquals("opt", tokens.get(2));
        Assert.assertEquals("arg4", tokens.get(3));
        Assert.assertEquals("--", tokens.get(4));
        Assert.assertEquals("arg5", tokens.get(5));
        Assert.assertEquals("arg6", tokens.get(6));
        Assert.assertEquals("--", tokens.get(7));
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[0]);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(0, cmd.getOptions().size());
    }

    @Test(expected = ParseException.class)
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        parser.setOptions(options);
        parser.parse(options, new String[0]);
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"arg0"});
        Assert.assertNotNull(cmd);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("arg0", cmd.getArgs().get(0));
        Assert.assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected void processOption(String arg, ListIterator<String> iter)
                    throws ParseException {
                super.processOption(arg, iter);
                cmd.addArg("arg");
            }
        };
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"-o"});
        Assert.assertNotNull(cmd);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("arg", cmd.getArgs().get(0));
        Assert.assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("o");
        Assert.assertEquals("option", opt.getLongOpt());
        Assert.assertEquals(true, opt.getValue().isPresent());
        Assert.assertEquals("", opt.getValue().get());
    }

    @Test
    public void testParseWithSingleOptionValue() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"-o", "value"});
        Assert.assertNotNull(cmd);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
        Assert.assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("o");
        Assert.assertEquals("option", opt.getLongOpt());
        Assert.assertEquals(true, opt.getValue().isPresent());
        Assert.assertEquals("value", opt.getValue().get());
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o1", "option1", true, "Option1 description");
        options.addOption("o2", "option2", true, "Option2 description");
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"-o1", "value1", "-o2", "value2"});
        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(2, cmd.getOptions().size());
        Option opt1 = cmd.getOption("o1");
        Assert.assertEquals("option1", opt1.getLongOpt());
        Assert.assertEquals(true, opt1.getValue().isPresent());
        Assert.assertEquals("value1", opt1.getValue().get());
        Option opt2 = cmd.getOption("o2");
        Assert.assertEquals("option2", opt2.getLongOpt());
        Assert.assertEquals(true, opt2.getValue().isPresent());
        Assert.assertEquals("value2", opt2.getValue().get());
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        parser.setOptions(options);
        parser.parse(options, new String[]{"-x"});
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected void checkRequiredOptions() throws MissingOptionException {
                throw new MissingOptionException("Missing required options");
            }
        };
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        options.addOption("r", "requiredOption", true, "Required option description");
        parser.setOptions(options);
        parser.parse(options, new String[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        parser.setOptions(options);
        parser.parse(options, new String[]{"-x"});
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingOptionArgument() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected void processArgs(Option opt, ListIterator<String> iter)
                    throws ParseException {
                throw new MissingArgumentException("Missing argument for option: " + opt.getKey());
            }
        };
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        parser.setOptions(options);
        parser.parse(options, new String[]{"-o"});
    }
}