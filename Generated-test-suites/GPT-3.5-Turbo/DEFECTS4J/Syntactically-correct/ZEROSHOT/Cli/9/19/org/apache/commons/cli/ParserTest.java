import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testParse() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        Parser parserSpy = Mockito.spy(parser);
        CommandLine cmd = new CommandLine();
        cmd.addOption("test");
        Mockito.doReturn(cmd).when(parserSpy).parse(options, new String[] {});
        assertEquals(cmd, parserSpy.parse(options, new String[] {}));
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        Parser parserSpy = Mockito.spy(parser);
        CommandLine cmd = new CommandLine();
        cmd.addOption("test");
        Mockito.doReturn(cmd).when(parserSpy).parse(options, new String[] {}, new Properties());
        assertEquals(cmd, parserSpy.parse(options, new String[] {}, new Properties()));
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        Parser parserSpy = Mockito.spy(parser);
        CommandLine cmd = new CommandLine();
        cmd.addOption("test");
        Mockito.doReturn(cmd).when(parserSpy).parse(options, new String[] {}, true);
        assertEquals(cmd, parserSpy.parse(options, new String[] {}, true));
    }

    @Test
    public void testParseWithPropertiesAndStopAtNonOption() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        Parser parserSpy = Mockito.spy(parser);
        CommandLine cmd = new CommandLine();
        cmd.addOption("test");
        Mockito.doReturn(cmd).when(parserSpy).parse(options, new String[] {}, new Properties(), true);
        assertEquals(cmd, parserSpy.parse(options, new String[] {}, new Properties(), true));
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        Option option = new Option("test", true, "");
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        Parser parserSpy = Mockito.spy(parser);
        CommandLine cmd = new CommandLine();
        cmd.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("test", "value");
        Mockito.doReturn(cmd).when(parserSpy).parse(options, new String[] {}, properties, false);
        parserSpy.parse(options, new String[] {}, properties, false);
        assertTrue(cmd.hasOption("test"));
        assertTrue(cmd.getOptionValue("test").equals("value"));
    }

    @Test
    public void testCheckRequiredOptions() {
        Options options = new Options();
        Option requiredOption = new Option("test", true, "");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        try {
            parser.parse(options, new String[] {});
            fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            // Exception expected
        }
    }

    @Test
    public void testProcessArgs() {
        Options options = new Options();
        Option option = new Option("test", true, "");
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        Parser parserSpy = Mockito.spy(parser);
        CommandLine cmd = new CommandLine();
        Mockito.doReturn(cmd).when(parserSpy).parse(options, new String[] {});
        ListIterator<String> iterator = Arrays.asList("value").listIterator();
        parserSpy.processArgs(option, iterator);
        assertTrue(cmd.hasOption("test"));
        assertTrue(cmd.getOptionValue("test").equals("value"));
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        Option option = new Option("test", true, "");
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        Parser parserSpy = Mockito.spy(parser);
        CommandLine cmd = new CommandLine();
        cmd.addOption("test");
        Mockito.doReturn(cmd).when(parserSpy).parse(options, new String[] {}, false);
        ListIterator<String> iterator = Arrays.asList("test").listIterator();
        parserSpy.processOption("test", iterator);
        assertTrue(cmd.hasOption("test"));
    }
}