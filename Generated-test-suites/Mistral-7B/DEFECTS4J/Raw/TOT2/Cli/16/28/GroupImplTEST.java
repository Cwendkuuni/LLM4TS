package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionContext;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private List<Option> options;
    private Group group;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        options.add(createOption("option1", "desc1", false));
        options.add(createOption("option2", "desc2", true, 2));
        options.add(createOption("option3", "desc3"));
        group = new GroupImpl(options, "group", "desc", 0, 3);
    }

    @Test
    public void testBasicFunctionality() {
        CommandLine commandLine = new CommandLine();
        group.process(commandLine, commandLine.getArgs().listIterator());

        assertEquals(3, commandLine.getOptions().size());
        assertNotNull(commandLine.getOptionValue("option1"));
        assertNotNull(commandLine.getOptionValue("option2"));
        assertNull(commandLine.getOptionValue("option3"));
    }

    @Test(expected = ParseException.class)
    public void testMinimumValue() throws ParseException {
        CommandLine commandLine = new CommandLine();
        commandLine.getArgs().add("--option1");
        group.process(commandLine, commandLine.getArgs().listIterator());
        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testMaximumValue() throws ParseException {
        CommandLine commandLine = new CommandLine();
        commandLine.getArgs().addAll(Arrays.asList("--option1", "--option1"));
        group.process(commandLine, commandLine.getArgs().listIterator());
        Assert.fail();
    }

    @Test
    public void testOptionalWithValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.getArgs().add("--option1=value1");
        group.process(commandLine, commandLine.getArgs().listIterator());

        assertEquals("value1", commandLine.getOptionValue("option1"));
    }

    @Test(expected = ParseException.class)
    public void testOptionalWithoutValue() throws ParseException {
        CommandLine commandLine = new CommandLine();
        group.process(commandLine, commandLine.getArgs().listIterator());

        assertFalse(commandLine.hasOption("option1"));
    }

    @Test
    public void testRequired() {
        CommandLine commandLine = new CommandLine();
        commandLine.getArgs().add("--option1");
        group.process(commandLine, commandLine.getArgs().listIterator());

        assertTrue(commandLine.hasOption("option1"));
    }

    @Test(expected = ParseException.class)
    public void testRequiredWithoutValue() throws ParseException {
        CommandLine commandLine = new CommandLine();
        group.process(commandLine, commandLine.getArgs().listIterator());

        Assert.fail();
    }

    @Test
    public void testAnonymousOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.getArgs().add("--anonymous");
        group.process(commandLine, commandLine.getArgs().listIterator());

        assertEquals(1, commandLine.getAnonymousValues().size());
        assertEquals("anonymous", commandLine.getAnonymousValues().get(0));
    }

    @Test
    public void testAnonymousOptionWithValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.getArgs().add("--anonymous=value");
        group.process(commandLine, commandLine.getArgs().listIterator());

        assertEquals(1, commandLine.getAnonymousValues().size());
        assertEquals("value", commandLine.getAnonymousValues().get(0));
    }

    @Test(expected = ParseException.class)
    public void testAnonymousOptionWithValueTwice() throws ParseException {
        CommandLine commandLine = new CommandLine();
        commandLine.getArgs().addAll(Arrays.asList("--anonymous=value1", "--anonymous=value2"));
        group.process(commandLine, commandLine.getArgs().listIterator());

        Assert.fail();
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "test", true, "test", group, null);
        List<String> lines = Arrays.asList(formatter.getHelpLines());

        assertEquals(11, lines.size());
        assertTrue(lines.contains("group (desc)"));
        assertTrue(lines.contains(" OPTIONS"));
        assertTrue(lines.contains("--option1"));
        assertTrue(lines.contains("--option1=value"));
        assertTrue(lines.contains("--option2"));
        assertTrue(lines.contains("--option2=value"));
        assertTrue(lines.contains("--option3"));
        assertTrue(lines.contains("--anonymous"));
        assertTrue(lines.contains("--anonymous=value"));
        assertTrue(lines.contains("--help"));
        assertTrue(lines.contains("--version"));
    }

    @Test
    public void testToString() {
        assertEquals("GroupImpl{name='group', description='desc', minimum=0, maximum=3, options=[" +
                        "OptionImpl{name='option1', description='desc1', optional=true, value=null, args=[], children=[]}, " +
                        "OptionImpl{name='option2', description='desc2', optional=false, value=null, args=[], children=[]}, " +
                        "OptionImpl{name='option3', description='desc3', optional=true, value=null, args=[], children=[]}, " +
                        "ArgumentImpl{name='anonymous', description=null, optional=false, value=null, children=[]}, " +
                        "ArgumentImpl{name='anonymous', description=null, optional=false, value=null, children=[]}, " +
                        "ArgumentImpl{name='anonymous', description=null, optional=false, value=null, children=[]}]}",
                     group.toString());
    }

    private Option createOption(final String name, final String description, final boolean optional) {
        return OptionBuilder.withName(name)
                .withDescription(description)
                .withOptionalArg(optional)
                .create(null);
    }

    private Option createOption(final String name, final String description, final int minimum, final int maximum) {
        return OptionBuilder.withName(name)
                .withDescription(description)
                .withMinValues(minimum)
                .withMaxValues(maximum)
                .create(null);
    }

    private Option createOption(final String name, final String description, final boolean optional, final int minimum, final int maximum) {
        return OptionBuilder.withName(name)
                .withDescription(description)
                .withMinValues(minimum)
                .withMaxValues(maximum)
                .withOptionalArg(optional)
                .create(null);
    }
}