package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.setName("mygroup");
        group.setDescription("My Group");

        Options options = new Options();
        options.addOptionGroup(group);

        Option option = new Option("a", "g", false, "Option A in Group");
        group.addOption(option);

        assertTrue(options.hasOption("g"));
        assertNotNull(options.getOption("g"));
        assertEquals("Option A in Group", options.getOption("g").getDescription());
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup();
        group1.setName("group1");
        group1.setDescription("Group 1");

        OptionGroup group2 = new OptionGroup();
        group2.setName("group2");
        group2.setDescription("Group 2");

        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        List<OptionGroup> groups = options.getOptionGroups();

        assertEquals(2, groups.size());
        assertTrue(groups.contains(group1));
        assertTrue(groups.contains(group2));
    }

    @Test
    public void testAddShortOption() {
        Options options = new Options();

        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        List<Option> optionsList = options.helpOptions();
        assertEquals(2, optionsList.size());

        Option optionA = optionsList.get(0);
        Option optionB = optionsList.get(1);

        assertEquals("Option A", optionA.getDescription());
        assertTrue(optionA.hasArg());
        assertFalse(optionA.isRequired());

        assertEquals("Option B", optionB.getDescription());
        assertFalse(optionB.hasArg());
        assertFalse(optionB.isRequired());
    }

    @Test
    public void testAddShortOptionWithArg() {
        Options options = new Options();

        options.addOption("a", "x", true, "Option A with argument X");

        List<Option> optionsList = options.helpOptions();
        assertEquals(1, optionsList.size());

        Option optionA = optionsList.get(0);

        assertEquals("Option A with argument X", optionA.getDescription());
        assertTrue(optionA.hasArg());
        assertFalse(optionA.isRequired());
    }

    @Test
    public void testAddShortOptionThatIsRequired() {
        Options options = new Options();

        options.addOption("a", true, "Option A");
        options.addOption("a", true, "Option A (required)");

        List<Object> requiredOptions = options.getRequiredOptions();

        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.get(0) instanceof String);
        assertEquals("a", (String) requiredOptions.get(0));
    }

    @Test
    public void testAddLongOption() {
        Options options = new Options();

        options.addOption("action", true, "Action option");
        options.addOption("help", false, "Help option");

        List<Option> optionsList = options.helpOptions();
        assertEquals(2, optionsList.size());

        Option optionAction = optionsList.get(0);
        Option optionHelp = optionsList.get(1);

        assertEquals("Action option", optionAction.getDescription());
        assertTrue(optionAction.hasArg());
        assertFalse(optionAction.isRequired());

        assertEquals("Help option", optionHelp.getDescription());
        assertFalse(optionHelp.hasArg());
        assertFalse(optionHelp.isRequired());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();

        Option optionA = new Option("a", "x", true, "Option A with argument X");
        options.addOption(optionA);

        Option optionB = new Option("b", false, "Option B");
        options.addOption(optionB);

        Option optionC = new Option("c", "y", true, "Option C with argument Y");
        options.addOption(optionC);

        assertEquals(optionA, options.getOption("x"));
        assertEquals(optionB, options.getOption("b"));
        assertNull(options.getOption("z"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();

        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", false, "Option B");
        Option optionC = new Option("c--foo", false, "Option C with long name foo");
        Option optionD = new Option("d--bar", false, "Option D with long name bar");

        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        options.addOption(optionD);

        List<String> optionsList = options.getMatchingOptions("--");
        assertEquals(0, optionsList.size());

        optionsList = options.getMatchingOptions("a");
        assertEquals(1, optionsList.size());
        assertEquals("a", optionsList.get(0));

        optionsList = options.getMatchingOptions("b");
        assertEquals(1, optionsList.size());
        assertEquals("b", optionsList.get(0));

        optionsList = options.getMatchingOptions("c");
        assertEquals(1, optionsList.size());
        assertEquals("c--foo", optionsList.get(0));

        optionsList = options.getMatchingOptions("d");
        assertEquals(1, optionsList.size());
        assertEquals("d--bar", optionsList.get(0));

        optionsList = options.getMatchingOptions("foo");
        assertEquals(1, optionsList.size());
        assertEquals("c--foo", optionsList.get(0));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();

        Option optionA = new Option("a", "x", true, "Option A with argument X");
        Option optionB = new Option("b", false, "Option B");
        Option optionC = new Option("c--foo", false, "Option C with long name foo");
        Option optionD = new Option("d--bar", false, "Option D with long name bar");

        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        options.addOption(optionD);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("x"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("c--foo"));
        assertTrue(options.hasOption("d--bar"));
        assertFalse(options.hasOption("z"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();

        Option optionA = new Option("a", "x", true, "Option A with argument X");
        Option optionC = new Option("c--foo", false, "Option C with long name foo");

        options.addOption(optionA);
        options.addOption(optionC);

        assertTrue(options.hasLongOption("c--foo"));
        assertFalse(options.hasLongOption("z"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();

        Option optionA = new Option("a", true, "Option A");
        Option optionB = new Option("b", false, "Option B");

        options.addOption(optionA);
        options.addOption(optionB);

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("z"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.setName("mygroup");
        group.setDescription("My Group");

        Option optionA = new Option("a", "g", false, "Option A in Group");
        group.addOption(optionA);

        Options options = new Options();
        options.addOptionGroup(group);

        OptionGroup optionGroup = options.getOptionGroup(optionA);

        assertEquals(group, optionGroup);
    }

    @Test
    public void testToString() {
        Options options = new Options();

        Option optionA = new Option("a", "x", true, "Option A with argument X");
        Option optionB = new Option("b", false, "Option B");
        Option optionC = new Option("c--foo", false, "Option C with long name foo");

        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);

        assertEquals("[ Options: [ short {a=x} ], [ long {c--foo} ] ]", options.toString());
    }
}