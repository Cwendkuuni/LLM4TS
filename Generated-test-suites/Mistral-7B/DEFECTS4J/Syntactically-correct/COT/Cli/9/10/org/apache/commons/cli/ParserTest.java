package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for Parser class
 */
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
        List requiredOptions = Arrays.asList(new Option("a", true, "a help message"));
        options.addOptions(requiredOptions);
        parser.setOptions(options);

        Assert.assertSame(requiredOptions, parser.getRequiredOptions());
    }

    @Test
    public void testFlatten_nullArguments() {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        String[] arguments = null;
        Options options = new Options();

        try {
            CommandLine cmd = parser.parse(options, arguments);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        String[] actual = parser.flatten(options, arguments, false);
        Assert.assertArrayEqual(new String[0], actual);
    }

    @Test
    public void testParse_emptyArguments() {
        Parser parser = new DefaultParser();

        String[] arguments = new String[0];
        Options options = new Options();

        try {
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertNull(cmd.getArgs());
            Assert.assertTrue(cmd.getOptions().isEmpty());
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testParse_singleOption() {
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"--a"};
        Options options = new Options();
        options.addOption("a", true, "a help message");
        parser.setOptions(options);

        try {
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertArrayEqual(new String[0], cmd.getArgs());
            Assert.assertEquals(1, cmd.getOptions().size());
            Option option = cmd.getOption("a");
            Assert.assertTrue(option.isSet());
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testParse_singleArgument() {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"arg"};
            }
        };

        String[] arguments = new String[]{"arg"};
        Options options = new Options();

        try {
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertArrayEqual(new String[]{"arg"}, cmd.getArgs());
            Assert.assertTrue(cmd.getOptions().isEmpty());
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testParse_multipleOptions() {
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"--a", "--b"};
        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.addOption("b", true, "b help message");
        parser.setOptions(options);

        try {
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertArrayEqual(new String[0], cmd.getArgs());
            Assert.assertEquals(2, cmd.getOptions().size());
            Option aOption = cmd.getOption("a");
            Option bOption = cmd.getOption("b");
            Assert.assertTrue(aOption.isSet());
            Assert.assertTrue(bOption.isSet());
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testParse_multipleArguments() {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"arg1", "arg2"};
            }
        };

        String[] arguments = new String[]{"arg1", "arg2"};
        Options options = new Options();

        try {
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertArrayEqual(new String[]{"arg1", "arg2"}, cmd.getArgs());
            Assert.assertTrue(cmd.getOptions().isEmpty());
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testParse_mixedOptionsAndArguments() {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"--a", "arg1", "--b"};
            }
        };

        String[] arguments = new String[]{"--a", "arg1", "--b"};
        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.addOption("b", true, "b help message");
        parser.setOptions(options);

        try {
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertArrayEqual(new String[0], cmd.getArgs());
            Assert.assertEquals(2, cmd.getOptions().size());
            Option aOption = cmd.getOption("a");
            Option bOption = cmd.getOption("b");
            Assert.assertTrue(aOption.isSet());
            Assert.assertTrue(bOption.isSet());
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testParse_requiredOptions() {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        String[] arguments = new String[0];
        Options options = new Options();
        List<Option> requiredOptions = Arrays.asList(new Option("a", true, "a help message")
                .required(true));
        options.addOptions(requiredOptions);
        parser.setOptions(options);

        try {
            CommandLine cmd = parser.parse(options, arguments);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals(MissingOptionException.class, e.getClass());
            Assert.assertEquals("Missing required option: a", e.getMessage());
        }
    }

    @Test
    public void testParse_unrecognizedOption() {
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"--x"};
        Options options = new Options();
        options.addOption("a", true, "a help message");
        parser.setOptions(options);

        try {
            CommandLine cmd = parser.parse(options, arguments);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals(UnrecognizedOptionException.class, e.getClass());
            Assert.assertEquals("Unrecognized option: x", e.getMessage());
        }
    }

    @Test
    public void testParse_properties() {
        Parser parser = new DefaultParser();

        String[] arguments = new String[0];
        Properties properties = new Properties();
        properties.put("a", "valueA");
        properties.put("b", "valueB");

        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.addOption("b", true, "b help message");

        parser.setOptions(options);

        try {
            CommandLine cmd = parser.parse(options, arguments, properties);
            Option aOption = cmd.getOption("a");
            Option bOption = cmd.getOption("b");
            Assert.assertTrue(aOption.isSet());
            Assert.assertTrue(bOption.isSet());
            Assert.assertEquals("valueA", aOption.getValue());
            Assert.assertEquals("valueB", bOption.getValue());
        } catch (ParseException e) {
            Assert.fail();
        }
    }

}