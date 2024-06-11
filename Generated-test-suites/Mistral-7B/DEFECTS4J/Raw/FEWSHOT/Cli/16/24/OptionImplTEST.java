package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionImplConstruction() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .withDescription("Test Option")
                .withId(1)
                .create();
        OptionImpl optionImpl = (OptionImpl) option;
        assertEquals(1, optionImpl.getId());
        assertTrue(optionImpl.isRequired());
    }

    @Test
    public void testOptionImplWithRequiredFalse() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .withDescription("Test Option")
                .withId(1)
                .isRequired(false)
                .create();
        OptionImpl optionImpl = (OptionImpl) option;
        assertEquals(1, optionImpl.getId());
        assertFalse(optionImpl.isRequired());
    }

    @Test
    public void testOptionImplCanProcess() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .withDescription("Test Option")
                .withId(1)
                .create();
        OptionImpl optionImpl = (OptionImpl) option;
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

        assertFalse(optionImpl.canProcess(commandLine, arguments));

        arguments.add("option");
        assertTrue(optionImpl.canProcess(commandLine, arguments));

        arguments.add("--option");
        assertTrue(optionImpl.canProcess(commandLine, arguments));
    }

    @Test
    public void testOptionImplToString() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .withDescription("Test Option")
                .withId(1)
                .create();
        OptionImpl optionImpl = (OptionImpl) option;
        assertEquals("-option, --option\n", optionImpl.toString());
    }

    @Test
    public void testOptionImplEquals() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("Test Option 1")
                .withId(1)
                .create();
        OptionImpl option1Impl = (OptionImpl) option1;

        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("Test Option 2")
                .withId(1)
                .create();
        OptionImpl option2Impl = (OptionImpl) option2;

        Option option3 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("Test Option 1")
                .withId(1)
                .create();
        OptionImpl option3Impl = (OptionImpl) option3;

        assertEquals(option1, option1);
        assertEquals(option1, option1Impl);
        assertEquals(option1, option1);
        assertEquals(option1, option1Impl);
        assertEquals(option1, option3);
        assertEquals(option1, option3Impl);

        assertNotEquals(option1, option2);
        assertNotEquals(option1, option2Impl);
        assertNotEquals(option1, new Object());
        assertNotEquals(option1, null);
    }

    @Test
    public void testOptionImplHashCode() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .withDescription("Test Option")
                .withId(1)
                .create();
        OptionImpl optionImpl = (OptionImpl) option;

        Option optionWithSameProperties = new OptionBuilder()
                .withLongOpt("option")
                .withDescription("Test Option")
                .withId(1)
                .create();
        OptionImpl optionWithSamePropertiesImpl = (OptionImpl) optionWithSameProperties;

        Option optionWithDifferentId = new OptionBuilder()
                .withLongOpt("option")
                .withDescription("Test Option")
                .withId(2)
                .create();
        OptionImpl optionWithDifferentIdImpl = (OptionImpl) optionWithDifferentId;

        Option optionWithDifferentName = new OptionBuilder()
                .withLongOpt("differentOption")
                .withDescription("Test Option")
                .withId(1)
                .create();
        OptionImpl optionWithDifferentNameImpl = (OptionImpl) optionWithDifferentName;

        int hashCode1 = optionImpl.hashCode();
        int hashCode2 = optionWithSamePropertiesImpl.hashCode();
        int hashCode3 = optionWithDifferentIdImpl.hashCode();
        int hashCode4 = optionWithDifferentNameImpl.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testOptionImplFindOption() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("Test Option 1")
                .withId(1)
                .create();
        OptionImpl option1Impl = (OptionImpl) option1;

        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("Test Option 2")
                .withId(2)
                .create();
        OptionImpl option2Impl = (OptionImpl) option2;

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        Option foundOption = option1Impl.findOption("option1");
        assertSame(option1, foundOption);

        foundOption = option1Impl.findOption("option2");
        assertNull(foundOption);
    }

    @Test
    public void testOptionImplCheckPrefixes() {
        Option option = new OptionBuilder()
                .withLongOpt("option")
                .withDescription("Test Option")
                .withId(1)
                .create();
        OptionImpl optionImpl = (OptionImpl) option;

        Set<String> prefixes = java.util.Collections.singleton("opt");

        optionImpl.checkPrefixes(prefixes);

        ResourceHelper helper = ResourceHelper.getResourceHelper();
        String message = helper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, "option", prefixes.toString());
        IllegalArgumentException expectedException = new IllegalArgumentException(message);

        try {
            optionImpl.checkPrefixes(prefixes);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }

        prefixes = java.util.Collections.emptySet();
        optionImpl.checkPrefixes(prefixes);
    }
}