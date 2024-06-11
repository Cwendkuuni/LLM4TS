import org.junit.Test;
import org.apache.commons.cli.CommandLine;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseOptionsStringArray() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help information");

        Parser parser = new DefaultParser();
        String[] args1 = {"-h"};
        String[] args2 = {"--help"};

        try {
            CommandLine cmd1 = parser.parse(options, args1);
            assertTrue(cmd1.hasOption("h"));

            CommandLine cmd2 = parser.parse(options, args2);
            assertTrue(cmd2.hasOption("help"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseOptionsStringArrayProperties() {
        Options options = new Options();
        options.addOption("u", "username", true, "Username");
        options.addOption("p", "password", true, "Password");

        Parser parser = new DefaultParser();

        String[] args = {"-u", "john", "-p", "123456"};
        Properties properties = new Properties();
        properties.setProperty("username", "jack");
        properties.setProperty("password", "abcdef");

        try {
            CommandLine cmd = parser.parse(options, args, properties);
            assertEquals("jack", cmd.getOptionValue("u"));
            assertEquals("abcdef", cmd.getOptionValue("p"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseOptionsStringArrayStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "File");
        options.addOption("q", "quiet", false, "Quiet mode");

        Parser parser = new DefaultParser();

        String[] args1 = {"myfile.txt", "-q"};
        String[] args2 = {"-f", "myfile.txt", "myfile2.txt"};

        try {
            CommandLine cmd1 = parser.parse(options, args1, true);
            assertEquals("myfile.txt", cmd1.getArgs()[0]);

            CommandLine cmd2 = parser.parse(options, args2, true);
            assertEquals("myfile.txt", cmd2.getOptionValue("f"));
            assertEquals("myfile2.txt", cmd2.getArgs()[0]);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseOptionsStringArrayPropertiesStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "all", false, "All");

        Parser parser = new DefaultParser();

        String[] args = {"-a", "myfile.txt", "-b", "yourfile.txt"};
        Properties properties = new Properties();
        properties.setProperty("b", "theirfile.txt");

        try {
            CommandLine cmd = parser.parse(options, args, properties, true);
            assertTrue(cmd.hasOption("a"));
            assertEquals("myfile.txt", cmd.getArgs()[0]);
            assertEquals("yourfile.txt", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("v", "verbose", false, "Verbose mode");

        Parser parser = new DefaultParser();

        String[] args = {"-v"};

        try {
            CommandLine cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("v"));
            assertTrue(cmd.hasOption("verbose"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOptionValue() {
        Options options = new Options();
        options.addOption("d", "debug", false, "Debug mode");
        options.addOption("l", "level", true, "Level");

        Parser parser = new DefaultParser();

        String[] args = {"-d", "-l", "info"};

        try {
            CommandLine cmd = parser.parse(options, args);
            assertNull(cmd.getOptionValue("d"));
            assertEquals("info", cmd.getOptionValue("l"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("v", "verbose", false, "Verbose mode");

        Parser parser = new DefaultParser();

        String[] args = {"-v"};

        try {
            CommandLine cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("verbose"));

            cmd.addOption(new Option("q", "quiet", false, "Quiet mode"));
            assertTrue(cmd.hasOption("quiet"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddArg() {
        Options options = new Options();
        options.addOption("f", "file", true, "File");

        Parser parser = new DefaultParser();

        String[] args = {"-f", "myfile.txt"};

        try {
            CommandLine cmd = parser.parse(options, args);
            assertEquals("myfile.txt", cmd.getOptionValue("f"));

            cmd.addArg("yourfile.txt");
            assertEquals("yourfile.txt", cmd.getArgs()[1]);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddOptionValue() {
        Options options = new Options();
        options.addOption("s", "size", true, "Size");

        Parser parser = new DefaultParser();

        String[] args = {"-s", "10"};

        try {
            CommandLine cmd = parser.parse(options, args);
            assertEquals("10", cmd.getOptionValue("s"));

            cmd.addOptionValue("size", "20");
            assertEquals("20", cmd.getOptionValue("s"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClearValues() {
        Options options = new Options();
        options.addOption("s", "size", true, "Size");

        Parser parser = new DefaultParser();

        String[] args = {"-s", "10"};

        try {
            CommandLine cmd = parser.parse(options, args);
            assertEquals("10", cmd.getOptionValue("s"));

            Option option = cmd.getOption("s");
            option.clearValues();

            assertNull(cmd.getOptionValue("s"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("u", "username", true, "Username");
        Option option2 = new Option("p", "password", true, "Password");

        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        Parser parser = new DefaultParser();

        String[] args = {"-u", "john"};

        try {
            CommandLine cmd = parser.parse(options, args);
            assertNull(options.getOptionGroup(option2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}