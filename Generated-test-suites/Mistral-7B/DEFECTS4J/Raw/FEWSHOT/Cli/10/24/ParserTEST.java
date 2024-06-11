package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@RunWith(Parameterized.class)
public class ParserTest {

    private Parser parser;
    private Options options;
    private String[] arguments;

    public ParserTest(Parser parser, Options options, String[] arguments) {
        this.parser = parser;
        this.options = options;
        this.arguments = arguments;
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new DefaultParser(), new Options(), new String[0]},
                {new DefaultParser(), new Options(), new String[]{"--help"}},
                {new DefaultParser(), new Options(), new String[]{"-a", "-b"}},
                {new DefaultParser(), new Options(), new String[]{"-a", "argA", "-b", "argB"}},
                {new DefaultParser(), new Options(), new String[]{"--foo", "-a", "argA", "--bar=argB"}},
                {new DefaultParser(), new Options(), new String[]{"--help", "-a", "argA", "--bar=argB"}},
                {new PosixParser(), new Options(), new String[0]},
                {new PosixParser(), new Options(), new String[]{"--help"}},
                {new PosixParser(), new Options(), new String[]{"-a", "-b"}},
                {new PosixParser(), new Options(), new String[]{"-a", "argA", "-b", "argB"}},
                {new PosixParser(), new Options(), new String[]{"--foo", "-a", "argA", "--bar=argB"}},
                {new PosixParser(), new Options(), new String[]{"--help", "-a", "argA", "--bar=argB"}}
        });
    }

    @Test
    public void testParse() throws ParseException {
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertEquals(arguments.length, cmd.getArgs().length);

        for (int i = 0; i < arguments.length; i++) {
            assertEquals(arguments[i], cmd.getArgs()[i]);
        }

        assertEquals(options.getOptions().size(), cmd.getOptions().size());

        for (Option option : options.getOptions()) {
            Option cmdOption = cmd.getOption(option.getKey());
            assertSame(option, cmdOption);
            assertEquals(option.getValueCount(), cmdOption.getValueCount());
            if (option.hasValues()) {
                assertArrayEquals(option.getValueStrings(), cmdOption.getValues());
            }
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "argA").required(true);
        option.setLongOpt("longOptionA");
        options.addOption(option);

        parser.setOptions(options);

        Properties properties = new Properties();
        properties.put("longOptionA", "argValue");

        CommandLine cmd = parser.parse(options, arguments, properties);

        assertNotNull(cmd);
        assertArrayEquals(new String[]{"argValue"}, cmd.getOptionValues("a"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "argA");
        options.addOption(option);

        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, arguments, true);

        assertEquals(arguments.length, cmd.getArgs().length);
        assertEquals(Arrays.stream(arguments).count(s -> s.startsWith("-")), cmd.getOptions().size());
    }

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "argA").required(false);
        Option option2 = new Option("b", "argB").required(false);
        Option option3 = new Option("c", "argC").required(false);
        Option option4 = new Option("d", "argD").required(false);
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);

        parser.setOptions(options);

        String[] result = parser.flatten(options, new String[]{"a", "b", "c--", "d", "--"});

        assertArrayEquals(new String[]{"a", "b", "c--", "d"}, result);
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        Option optionLongOption1 = new Option("longOption1", "argA");
        Option optionLongOption2 = new Option("longOption2", "argB");
        options.addOption(optionLongOption1);
        options.addOption(optionLongOption2);

        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"--longOption1", "argValueA", "--longOption2=argValueB"});

        assertEquals(2, cmd.getOptions().size());
        assertEquals("argValueA", cmd.getOptionValues("longOption1")[0]);
        assertEquals("argValueB", cmd.getOptionValues("longOption2")[0]);
    }

    @Test
    public void testParseWithOptionalArgs() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "argA").optionalArg(true).hasArg(true);
        options.addOption(option);

        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"--a=argValueA"});
        assertEquals("argValueA", cmd.getOptionValues("a")[0]);

        cmd = parser.parse(options, new String[]{"--a"});
        assertNull(cmd.getOptionValues("a"));
    }

    @Test
    public void testParseWithRequiredArgs() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "argA").requiredArg();
        options.addOption(option);

        assertThrows(MissingArgumentException.class, () -> parser.parse(options, new String[0]));

        parser.parse(options, new String[]{"--a=argValueA"});
        assertEquals("argValueA", cmd.getOptionValues("a")[0]);
    }

    @Test
    public void testParseWithGroupOptions() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group1");
        group.setRequired(true);

        Option option1 = new Option("a", "argA").setLongOpt("longOptionA1")
                .setOptionalArg(true).setValueSeparator(' ')
                .setOptionGroup(group);
        Option option2 = new Option("b", "argB").setLongOpt("longOptionB1")
                .setOptionalArg(true).setValueSeparator(' ')
                .setOptionGroup(group);

        options.addOption(option1);
        options.addOption(option2);

        parser.setOptions(options);

        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));

        parser.parse(options, new String[]{"--group1", "argA", "argB"});

        OptionGroup cmdGroup = cmd.getOptionGroup("group1");
        assertNotNull(cmdGroup);
        Option cmdOption1 = cmd.getOption(option1.getKey());
        Option cmdOption2 = cmd.getOption(option2.getKey());
        assertEquals(option1, cmdOption1);
        assertEquals(option2, cmdOption2);
        assertEquals(Arrays.asList("argA", "argB"), cmd.getValuesForOption("group1"));
    }

    @Test
    public void testParseWithMultipleValues() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "argA").requiredArg().setValueSeparator(',');
        options.addOption(option);

        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"--a", "argValue1,argValue2"});
        assertArrayEquals(new String[]{"argValue1", "argValue2"}, cmd.getOptionValues("a"));
    }

    @Test
    public void testParseWithValuesFromProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "argA").requiredArg().setValueSeparator(',');
        options.addOption(option);

        parser.setOptions(options);

        Properties properties = new Properties();
        properties.put("a", "argValue1,argValue2");

        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertArrayEquals(new String[]{"argValue1", "argValue2"}, cmd.getOptionValues("a"));
    }
}