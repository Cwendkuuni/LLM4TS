package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.generate.OptionBuilder;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionImplConstruction() {
        final OptionImpl option = new OptionBuilder().withLongOpt("exampleOption")
                .withDescription("An example option").build();

        assertEquals(Option.LONG_OPT, option.getType());
        assertEquals("exampleOption", option.getPreferredName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getId());
    }

    @Test
    public void testOptionImplConstructionWithprefixes() {
        final OptionImpl option = new OptionBuilder().withLongOpt("exampleOption")
                .withDescription("An example option")
                .withPrefix("--example")
                .build();

        assertEquals(Option.LONG_OPT, option.getType());
        assertEquals("exampleOption", option.getPreferredName());
        assertEquals("--example", option.getPrefixes().toString());
        assertFalse(option.isRequired());
        assertEquals(1, option.getId());
    }

    @Test
    public void testOptionImplConstructionWithTriggers() {
        final OptionImpl option = new OptionBuilder().withLongOpt("exampleOption")
                .withDescription("An example option")
                .withTriggers("example1", "example2")
                .build();

        assertEquals(Option.LONG_OPT, option.getType());
        assertEquals("exampleOption", option.getPreferredName());
        assertEquals(Arrays.asList("example1", "example2"), option.getTriggers());
        assertFalse(option.isRequired());
        assertEquals(1, option.getId());
    }

    @Test
    public void testOptionImplConstructionWithLongOptAndShortOpt() {
        final OptionImpl option = new OptionBuilder().withLongOpt("exampleOption")
                .withDescription("An example option")
                .withShortOpt('e')
                .build();

        assertEquals(Option.LONG_OPT, option.getType());
        assertEquals("exampleOption", option.getPreferredName());
        assertEquals("e", option.getShortOpt());
        assertEquals(Arrays.asList("exampleOption"), option.getTriggers());
        assertFalse(option.isRequired());
        assertEquals(1, option.getId());
    }

    @Test
    public void testOptionImplCanProcess() {
        final OptionImpl option = new OptionBuilder().withLongOpt("exampleOption")
                .withDescription("An example option")
                .build();

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final List<String> arguments = Arrays.asList("example", "--exampleOption");

        assertTrue(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test
    public void testOptionImplCannotProcess() {
        final OptionImpl option = new OptionBuilder().withLongOpt("exampleOption")
                .withDescription("An example option")
                .build();

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final List<String> arguments = Arrays.asList("example");

        assertFalse(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test
    public void testOptionImplEquals() {
        final OptionImpl option1 = new OptionBuilder().withLongOpt("exampleOption1")
                .withDescription("An example option 1")
                .build();
        final OptionImpl option2 = new OptionBuilder().withLongOpt("exampleOption2")
                .withDescription("An example option 2")
                .build();

        assertFalse(option1.equals(option2));

        final OptionImpl option3 = new OptionBuilder().withLongOpt("exampleOption1")
                .withDescription("An example option 1")
                .build();

        assertTrue(option1.equals(option3));
    }

    @Test
    public void testOptionImplHashCode() {
        final OptionImpl option1 = new OptionBuilder().withLongOpt("exampleOption1")
                .withDescription("An example option 1")
                .build();

        final OptionImpl option2 = new OptionBuilder().withLongOpt("exampleOption2")
                .withDescription("An example option 2")
                .build();

        assertNotEquals(option1.hashCode(), option2.hashCode());

        final OptionImpl option3 = new OptionBuilder().withLongOpt("exampleOption1")
                .withDescription("An example option 1")
                .build();

        assertEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testOptionImplFindOption() {
        final OptionImpl option1 = new OptionBuilder().withLongOpt("exampleOption1")
                .withDescription("An example option 1")
                .build();

        final OptionImpl option2 = new OptionBuilder().withLongOpt("exampleOption2")
                .withDescription("An example option 2")
                .build();

        assertNull(option1.findOption("exampleOption2"));
        assertSame(option1, option1.findOption("exampleOption1"));
        assertSame(option2, option2.findOption("exampleOption2"));
    }

    @Test
    public void testOptionImplIsRequired() {
        final OptionImpl option1 = new OptionBuilder().withLongOpt("exampleOption1")
                .withDescription("An example option 1")
                .build();

        assertFalse(option1.isRequired());

        final OptionImpl option2 = new OptionBuilder().withLongOpt("exampleOption2")
                .withDescription("An example option 2")
                .withRequired(true)
                .build();

        assertTrue(option2.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionImplCheckPrefixesNoPrefix() {
        final OptionImpl option = new OptionBuilder().withLongOpt("exampleOption")
                .withDescription("An example option")
                .build();

        final Set<String> prefixes = new HashSet<>();

        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionImplCheckPrefixesNoMatch() {
        final OptionImpl option = new OptionBuilder().withLongOpt("exampleOption")
                .withDescription("An example option")
                .withPrefix("--example")
                .build();

        final Set<String> prefixes = new HashSet<>(Arrays.asList("--anotherPrefix"));

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testOptionImplCheckPrefixesMatch() {
        final OptionImpl option = new OptionBuilder().withLongOpt("exampleOption")
                .withDescription("An example option")
                .withPrefix("--example")
                .build();

        final Set<String> prefixes = new HashSet<>(Arrays.asList("--example"));

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testOptionImplCheckPrefixesMultiple() {
        final OptionImpl option = new OptionBuilder().withLongOpt("exampleOption")
                .withDescription("An example option")
                .withPrefix("--example")
                .withTriggers("example1", "example2")
                .build();

        final Set<String> prefixes = new HashSet<>(Arrays.asList("--example1", "--example2"));

        option.checkPrefixes(prefixes);
    }
}