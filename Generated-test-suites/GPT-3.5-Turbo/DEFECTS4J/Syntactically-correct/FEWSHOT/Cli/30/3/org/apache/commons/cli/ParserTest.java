
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    private Parser parser = new ParserImpl();

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").required(true).build());

        String[] args = { "-a", "-b" };
        CommandLine cmd = parser.parse(options, args);

        assertEquals(args.length, cmd.getOptions().length);
        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.hasOption("b"));
        assertEquals(false, cmd.hasOption("c"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required(true).build());
        options.addOption(Option.builder("b").required(true).build());
        options.addOption(Option.builder("c").required(true).build());

        String[] args = { "-a", "-b" };
        parser.parse(options, args);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        Properties properties = new Properties();
        properties.setProperty("a", "value_a");
        properties.setProperty("b", "value_b");

        String[] args = { "-c" };
        CommandLine cmd = parser.parse(options, args, properties);

        assertEquals(args.length, cmd.getOptions().length);
        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.hasOption("b"));
        assertEquals(true, cmd.hasOption("c"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        String[] args = { "-c" };
        parser.parse(options, args);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = Option.builder("a").build();
        ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();

        parser.processArgs(option, iterator);

        assertEquals(2, option.getValues().length);
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArg() throws ParseException {
        Option option = Option.builder("a").build();
        ListIterator<String> iterator = Arrays.asList().listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testUpdateRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required(true).build());
        options.addOption(Option.builder("b").required(true).build());

        OptionGroup group = new OptionGroup();
        group.addOption(Option.builder("c").build());
        group.addOption(Option.builder("d").required(true).build());
        options.addOptionGroup(group);

        CommandLine cmd = new CommandLine();

        Option optionA = options.getOption("a");
        Option optionB = options.getOption("b");
        Option optionC = options.getOption("c");
        Option optionD = options.getOption("d");

        parser.updateRequiredOptions(optionA);
        parser.updateRequiredOptions(optionD);
        assertEquals(1, parser.getRequiredOptions().size());
        assertEquals(group, parser.getRequiredOptions().get(0));

        parser.updateRequiredOptions(optionB);
        assertEquals(0, parser.getRequiredOptions().size());

        parser.updateRequiredOptions(optionC);
        assertNotEquals(0, parser.getRequiredOptions().size());

        parser.updateRequiredOptions(optionD);
        assertEquals(0, parser.getRequiredOptions().size());
    }
    
    private static class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}
