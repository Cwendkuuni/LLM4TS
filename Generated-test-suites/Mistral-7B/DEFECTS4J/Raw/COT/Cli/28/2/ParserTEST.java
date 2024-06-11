import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest
{
    @Test
    public void testSetOptions()
    {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[0];
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithEmptyArgumentArray()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[0]);
        assertEquals(0, cmd.getArgList().size());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithSingleArgument()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[]{"arg"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"arg"});
        List<String> argList = cmd.getArgList();
        assertEquals(1, argList.size());
        assertEquals("arg", argList.get(0));
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithSingleOption()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[]{"-a"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"-a"});
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("a", option.getKey());
        assertTrue(option.isSet());
        assertFalse(option.getValues().hasNext());
    }

    @Test
    public void testParseWithSingleOptionAndValue()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[]{"-a", "value"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"-a", "value"});
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("a", option.getKey());
        assertTrue(option.isSet());
        assertEquals("value", option.getValue());
    }

    @Test
    public void testParseWithMultipleArguments()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[]{"arg1", "arg2"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});
        List<String> argList = cmd.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithMultipleOptions()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        options.addOption("b", true, "another help option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[]{"-a", "-b"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b"});
        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option optionA = optionsList.get(0);
        assertEquals("a", optionA.getKey());
        assertTrue(optionA.isSet());
        assertFalse(optionA.getValues().hasNext());
        Option optionB = optionsList.get(1);
        assertEquals("b", optionB.getKey());
        assertTrue(optionB.isSet());
        assertFalse(optionB.getValues().hasNext());
    }

    @Test
    public void testParseWithMultipleArgumentsAndOptions()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        options.addOption("b", true, "another help option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[]{"arg1", "-a", "arg2", "-b"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "-a", "arg2", "-b"});
        List<String> argList = cmd.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option optionA = optionsList.get(0);
        assertEquals("a", optionA.getKey());
        assertTrue(optionA.isSet());
        assertFalse(optionA.getValues().hasNext());
        Option optionB = optionsList.get(1);
        assertEquals("b", optionB.getKey());
        assertTrue(optionB.isSet());
        assertFalse(optionB.getValues().hasNext());
    }

    @Test
    public void testParseWithMissingOption()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[]{"-c"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[]{"-c"}));
    }

    @Test
    public void testParseWithMissingRequiredOption()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        options.addRequiredOption("b", true, "a required option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));
    }

    @Test
    public void testParseWithMissingRequiredOptionAndValue()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        options.addRequiredOption("b", true, "a required option with value", true, "default");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));
    }

    @Test
    public void testParseWithExtraArguments()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[]{"arg1", "arg2", "extra1", "extra2"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2", "extra1", "extra2"});
        List<String> argList = cmd.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
        List<Option> optionsList = cmd.getOptions();
        assertEquals(0, optionsList.size());
    }

    @Test
    public void testParseWithExtraArgumentsAndOptions()
    {
        Options options = new Options();
        options.addOption("a", true, "a help option");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return new String[]{"arg1", "-a", "arg2", "extra1", "-b"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "-a", "arg2", "extra1", "-b"});
        List<String> argList = cmd.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option optionA = optionsList.get(0);
        assertEquals("a", optionA.getKey());
        assertTrue(optionA.isSet());
        assertFalse(optionA.getValues().hasNext());
        Option optionB = optionsList.get(1);
        assertEquals("b", optionB.getKey());
        assertTrue(optionB.isSet());
        assertFalse(optionB.getValues().hasNext());
    }
}