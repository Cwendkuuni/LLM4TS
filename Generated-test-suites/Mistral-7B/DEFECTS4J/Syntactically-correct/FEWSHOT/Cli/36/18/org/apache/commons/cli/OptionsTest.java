package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testCreateOptions() {
        Options options = new Options();
        assertEquals(Collections.emptyMap(), options.getShortOpts());
        assertEquals(Collections.emptyMap(), options.getLongOpts());
        assertEquals(Collections.emptyList(), options.getRequiredOpts());
        assertEquals(Collections.emptyMap(), options.getOptionGroups());
    }

    @Test
    public void testAddShortOption() {
        Options options = new Options();
        Option opt = new Option("f", "force", true, "Force execution of command.");
        options.addOption(opt);
        assertEquals("f", options.getShortOpts().keySet().iterator().next());
        assertEquals(opt, options.getOption("f"));
    }

    @Test
    public void testAddShortOptionWithDescription() {
        Options options = new Options();
        Option opt = new Option("f", "force", true, "Force execution of command.");
        options.addOption(opt);
        assertEquals("Force execution of command.", options.getOption("f").getDescription());
    }

    @Test
    public void testAddShortOptionWithArgument() {
        Options options = new Options();
        Option opt = new Option("f", "file", true, "File name or path.");
        options.addOption(opt);
        assertEquals("file", opt.getLongOpt());
        assertEquals(true, opt.hasArg());
        assertEquals("File name or path.", opt.getDescription());
    }

    @Test
    public void testAddShortOptionWithArgumentAndDescription() {
        Options options = new Options();
        Option opt = new Option("f", "file", true, "File name or path.");
        options.addOption(opt);
        assertEquals("File name or path.", options.getOption("f").getDescription());
    }

    @Test
    public void testAddShortAndLongOption() {
        Options options = new Options();
        Option optShort = new Option("f", "force", true, "Force execution of command.");
        Option optLong = new Option("force", null, true, "Force execution of command.");
        options.addOption(optShort);
        options.addOption(optLong);
        assertEquals(Collections.singletonMap("f", optShort), options.getShortOpts());
        assertEquals(Collections.singletonMap("force", optLong), options.getLongOpts());
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group options.");
        options.addOptionGroup(group);
        assertEquals(Collections.singletonMap("g", group), options.getOptionGroups());
    }

    @Test
    public void testAddRequiredOption() {
        Options options = new Options();
        Option option = new Option("f", "force", true, "Force execution of command.");
        options.addOption(option);
        options.addOption("--force", true, "Force execution of command.");
        List<Object> required = options.getRequiredOpts();
        assertEquals(2, required.size());
        assertTrue(required.get(0) instanceof Option);
        assertTrue(required.get(1) instanceof String);
        assertSame(option, required.get(0));
        assertEquals("--force", required.get(1).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateOption() {
        Options options = new Options();
        Option opt = new Option("f", "force", true, "Force execution of command.");
        options.addOption(opt);
        options.addOption(opt);
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option opt = new Option("f", "force", true, "Force execution of command.");
        options.addOption(opt);
        List<Option> optionsList = options.getOptions();
        assertEquals(Collections.singletonList(opt), optionsList);
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option opt1 = new Option("f", "force", true, "Force execution of command.");
        Option opt2 = new Option("g", "group", false, "Group the command together.");
        Option opt3 = new Option("h", "help", false, "Display this help message.");
        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);
        options.addOptionGroup(new OptionGroup("g", "Group")
                .addOption(opt1)
                .addOption(opt2)
        );
        options.addOption("--help", false, "Display this help message.");
        List<Option> requiredOptions = options.getRequiredOptions();
        assertEquals(Collections.singletonList(opt1), requiredOptions);
    }

    @Test
    public void testGetOptionWithArg() {
        Options options = new Options();
        Option opt = new Option("f", "file", true, "File name or path.");
        options.addOption(opt);
        Option parsedOption = options.parse("-f testFile.txt");
        assertEquals(opt, parsedOption);
        assertEquals("testFile.txt", parsedOption.getArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        Option opt = new Option("f", "file", true, "File name or path.");
        options.addOption(opt);
        options.parse("--invalid");
    }

    @Test
    public void testGetLongOption() {
        Options options = new Options();
        Option opt = new Option("f", "force", true, "Force execution of command.");
        Option longOpt = new Option("force", null, true, "Force execution of command.");
        options.addOption(opt);
        options.addOption(longOpt);
        Option parsedOption = options.parse("--force");
        assertEquals(longOpt, parsedOption);
    }

    @Test
    public void testGetShortOption() {
        Options options = new Options();
        Option opt = new Option("f", "force", true, "Force execution of command.");
        Option longOpt = new Option("force", null, true, "Force execution of command.");
        options.addOption(opt);
        options.addOption(longOpt);
        Option parsedOption = options.parse("-f");
        assertEquals(opt, parsedOption);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option opt1 = new Option("f", "force", true, "Force execution of command.");
        Option opt2 = new Option("forc", "force with arg", true, "Force execution of command with arg.");
        Option opt3 = new Option("fo", "force long", true, "Force execution of command long.");
        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);
        List<String> matchingOptions = options.getMatchingOptions("for");
        assertEquals(2, matchingOptions.size());
        assertEquals("forc", matchingOptions.get(0));
        assertEquals("fo", matchingOptions.get(1));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option opt = new Option("f", "force", true, "Force execution of command.");
        Option longOpt = new Option("force", null, true, "Force execution of command.");
        options.addOption(opt);
        options.addOption(longOpt);
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("force"));
        assertFalse(options.hasOption("invalid"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option opt = new Option("f", "force", true, "Force execution of command.");
        Option longOpt = new Option("force", null, true, "Force execution of command.");
        options.addOption(opt);
        options.addOption(longOpt);
        assertTrue(options.hasLongOption("force"));
        assertFalse(options.hasLongOption("invalid"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option opt = new Option("f", "force", true, "Force execution of command.");
        Option longOpt = new Option("force", null, true, "Force execution of command.");
        options.addOption(opt);
        options.addOption(longOpt);
        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("invalid"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group options.");
        Option opt = new Option("f", "force", true, "Force execution of command.");
        Option opt2 = new Option("h", "help", false, "Display this help message.");
        group.addOption(opt);
        group.addOption(opt2);
        options.addOptionGroup(group);
        Option parsedOption = options.parse("--help");
        assertEquals(opt2, parsedOption);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option opt1 = OptionBuilder.withLongOpt("log").withDescription("Specify log file.")
                .withValueNames("FILE").build();
        Option opt2 = OptionBuilder.withShortNames("d").withDescription("Delete files.")
                .withValueNames("FILE").build();
        Option opt3 = OptionBuilder.withLongOpt("help").withDescription("Print this help message and exit.")
                .build();
        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);
        assertEquals("[ Options: [ short {\"d\",null}=Delete files., \"log\",log=Specify log file.} ] [ long {help=Print this help message and exit.} ]]",
                options.toString());
    }
}