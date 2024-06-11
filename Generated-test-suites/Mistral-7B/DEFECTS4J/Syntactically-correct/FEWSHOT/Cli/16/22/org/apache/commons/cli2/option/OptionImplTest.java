package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.options.OptionBuilder;
import org.apache.commons.cli2.options.OptionsParser;
import org.junit.Test;

public class OptionImplTest {
    @Test
    public void testOptionImpl() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .withDescription("Test option")
                .isRequired()
                .create();

        OptionImpl optionImpl = (OptionImpl) option;

        assertEquals(1, optionImpl.getId());
        assertTrue(optionImpl.isRequired());
        assertNull(optionImpl.getPreferredName());
        assertNull(optionImpl.getDescription());
        assertNotNull(optionImpl.getPrefixes());
        assertFalse(optionImpl.getPrefixes().isEmpty());
        assertNull(optionImpl.getTriggers());

        assertEquals("Test option", optionImpl.getDescription());
        assertEquals("option", optionImpl.getPreferredName());
        assertEquals("[option]", optionImpl.getPrefixes().toString());
        assertEquals("option", optionImpl.getTriggers().get(0).toString());
    }

    @Test
    public void testCanProcess() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .isRequired()
                .create();

        OptionImpl optionImpl = (OptionImpl) option;

        WriteableCommandLine commandLine = new Parser().parse(OptionBuilder.defaults(), new String[]{"--option", "value"});

        assertTrue(optionImpl.canProcess(commandLine, commandLine.getArguments().listIterator()));

        WriteableCommandLine commandLine2 = new Parser().parse(OptionBuilder.defaults(), new String[]{"--optionValue"});

        assertFalse(optionImpl.canProcess(commandLine2, commandLine2.getArguments().listIterator()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithoutPrefix() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .isRequired()
                .create();

        OptionImpl optionImpl = (OptionImpl) option;

        Set<String> prefixes = OptionBuilder.noPrefixes();

        optionImpl.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithSamePrefix() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .withLongOpt("option2")
                .create();

        OptionImpl optionImpl = (OptionImpl) option;

        Set<String> prefixes = OptionBuilder.prefixes("opt");

        optionImpl.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithDifferentPrefix() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .isRequired()
                .create();

        OptionImpl optionImpl = (OptionImpl) option;

        Set<String> prefixes = OptionBuilder.prefixes("opt2");

        optionImpl.checkPrefixes(prefixes);
    }
}