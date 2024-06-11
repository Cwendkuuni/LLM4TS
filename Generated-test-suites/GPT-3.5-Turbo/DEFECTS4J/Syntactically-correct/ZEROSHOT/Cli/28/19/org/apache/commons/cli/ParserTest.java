import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        parser.setOptions(options);
        
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        parser.setOptions(options);
        
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "input file");
        option.setRequired(true);
        options.addOption(option);
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        parser.setOptions(options);
        
        assertEquals(1, parser.getRequiredOptions().size());
        assertEquals(option, parser.getRequiredOptions().get(0));
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");
        options.addOption("d", "directory", true, "input directory");
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        String[] arguments = {"-f", "file.txt", "-d", "dir"};
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(arguments.length, flattened.length);
        assertArrayEquals(arguments, flattened);
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");
        options.addOption("d", "directory", true, "input directory");
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        String[] arguments = {"-f", "file.txt", "-d", "dir"};
        CommandLine cmd = parser.parse(options, arguments);
        
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("file"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("directory"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertEquals("file.txt", cmd.getOptionValue("file"));
        assertEquals("dir", cmd.getOptionValue("d"));
        assertEquals("dir", cmd.getOptionValue("directory"));
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");
        options.addOption("d", "directory", true, "input directory");
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        Properties properties = new Properties();
        properties.setProperty("file", "file.txt");
        properties.setProperty("directory", "dir");
        
        parser.setOptions(options);
        parser.processProperties(properties);
        
        assertTrue(parser.cmd.hasOption("f"));
        assertTrue(parser.cmd.hasOption("file"));
        assertTrue(parser.cmd.hasOption("d"));
        assertTrue(parser.cmd.hasOption("directory"));
        assertEquals("file.txt", parser.cmd.getOptionValue("f"));
        assertEquals("file.txt", parser.cmd.getOptionValue("file"));
        assertEquals("dir", parser.cmd.getOptionValue("d"));
        assertEquals("dir", parser.cmd.getOptionValue("directory"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        Option option = new Option("f", "file", false, "input file");
        option.setRequired(true);
        options.addOption(option);
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        List<String> argumentList = new ArrayList<>();
        argumentList.add("file1.txt");
        argumentList.add("file2.txt");

        parser.processArgs(option, argumentList.listIterator());
        
        assertEquals(2, option.getValues().length);
        assertEquals("file1.txt", option.getValues()[0]);
        assertEquals("file2.txt", option.getValues()[1]);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");
        options.addOption("d", "directory", false, "input directory");
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        List<String> argumentList = new ArrayList<>();
        argumentList.add("-f");
        argumentList.add("file.txt");
        argumentList.add("-d");
        
        parser.setOptions(options);
        parser.processOption("-f", argumentList.listIterator());
        
        assertEquals(1, parser.cmd.getOptions().length);
        assertTrue(parser.cmd.hasOption("f"));
        assertEquals("file.txt", parser.cmd.getOptionValue("f"));
        
        parser.processOption("-d", argumentList.listIterator());
        
        assertEquals(2, parser.cmd.getOptions().length);
        assertTrue(parser.cmd.hasOption("d"));
        assertNull(parser.cmd.getOptionValue("d"));
    }
}