package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.commandline.Option;
import org.apache.commons.cli2.commandline.OptionBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.exceptions.OptionException;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;
    private WriteableCommandLine cmd;
    private Parser parser;

    @Before
    public void setup() {
        option = OptionBuilder.withLongOpt("test")
                .withDescription("this is a test option")
                .withId(1)
                .create();

        cmd = new WriteableCommandLine();
        parser = new DefaultParser();
    }

    @Test
    public void testProcess() throws OptionException {
        option.process(cmd, cmd.listIterator(1));
        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testDefaults() {
        option.defaults(cmd);
        assertNull(cmd.getOptionValue("test"));
    }

    @Test
    public void testCanProcess() throws OptionException {
        assertTrue(option.canProcess(cmd, "test"));
        assertFalse(option.canProcess(cmd, "arg"));
    }

    @Test
    public void testCanProcessWithIterator() throws OptionException {
        List<String> args = Arrays.asList("test", "arg");
        Iterator<String> it = args.iterator();
        assertTrue(option.canProcess(cmd, it));
        assertFalse(option.canProcess(cmd, it));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        assertTrue(triggers.contains("test"));
        assertFalse(triggers.contains("arg"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        assertTrue(prefixes.contains("-"));
        assertFalse(prefixes.contains("--"));
    }

    @Test
    public void testValidate() throws OptionException {
        option.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithBadArgument() throws OptionException {
        cmd.addOption("test", true);
        option.validate(cmd);
    }

    @Test
    public void testHelpLines() {
        List<Option> options = new ArrayList<>();
        options.add(option);
        List<HelpLine> helpLines = option.helpLines(0, Collections.emptySet(), null);
        assertTrue(helpLines.size() > 0);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, Collections.emptySet(), null);
        assertTrue(buffer.toString().contains("test\tthis is a test option"));
    }

    @Test
    public void testGetPreferredName() {
        String name = option.getPreferredName();
        assertTrue(name.equals("test"));
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();
        assertTrue(description.equals("this is a test option"));
    }

    @Test
    public void testgetId() {
        int id = option.getId();
        assertTrue(id == 1);
    }

    @Test
    public void testFindOption() {
        Option otherOption = OptionBuilder.withLongOpt("other")
                .withDescription("this is another test option")
                .withId(2)
                .create();
        cmd.addOption(option);
        cmd.addOption(otherOption);
        Option foundOption = option.findOption("test");
        assertSame(option, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option requiredOption = OptionBuilder.withLongOpt("required")
                .withDescription("required option")
                .withId(3)
                .isRequired(true)
                .create();
        assertFalse(requiredOption.isRequired());
        requiredOption.defaults(cmd);
        assertFalse(cmd.hasOption("required"));
        try {
            parser.parse(cmd, null);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testGetParent() {
        Option parentOption = OptionBuilder.withLongOpt("parent")
                .withDescription("parent option")
                .withId(4)
                .create();
        option.setParent(parentOption);
        Option childOption = OptionBuilder.withLongOpt("child")
                .withDescription("child option")
                .withId(5)
                .create();
        parentOption.addOption(option);
        parentOption.addOption(childOption);
        Option foundOption = option.findOption("test");
        assertSame(option, foundOption);
        Option foundChildOption = childOption.findOption("child");
        assertSame(childOption, foundChildOption);
    }
}