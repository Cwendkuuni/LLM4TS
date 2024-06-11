package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        Option option = new OptionBuilder().withLongOpt("option1").build();
        OptionImpl optionImpl = (OptionImpl) option;
        assertEquals(1, optionImpl.getId());
        assertFalse(optionImpl.isRequired());
    }

    @Test
    public void testCanProcess() {
        Option option = new OptionBuilder().withLongOpt("option1").build();
        OptionImpl optionImpl = (OptionImpl) option;
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--option1", "arg");
        ListIterator<String> argumentsIterator = arguments.listIterator();

        assertFalse(optionImpl.canProcess(commandLine, argumentsIterator));

        commandLine.parseArgument(argumentsIterator.next());
        assertTrue(optionImpl.canProcess(commandLine, argumentsIterator));
    }

    @Test
    public void testToString() {
        Option option = new OptionBuilder().withLongOpt("option1").build();
        OptionImpl optionImpl = (OptionImpl) option;

        assertEquals("--option1", optionImpl.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new OptionBuilder().withLongOpt("option1").build();
        Option option2 = new OptionBuilder().withLongOpt("option1").build();

        OptionImpl option1Impl = (OptionImpl) option1;
        OptionImpl option2Impl = (OptionImpl) option2;

        assertTrue(option1Impl.equals(option1));
        assertTrue(option1Impl.equals(option2));
        assertTrue(option2Impl.equals(option1));
        assertTrue(option1Impl.equals(option2Impl));
    }

    @Test
    public void testHashCode() {
        Option option = new OptionBuilder().withLongOpt("option1").build();
        OptionImpl optionImpl = (OptionImpl) option;

        Option option2 = new OptionBuilder().withLongOpt("option2").build();
        OptionImpl option2Impl = (OptionImpl) option2;

        int hashCode1 = optionImpl.hashCode();
        int hashCode2 = option2Impl.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        assertEquals(hashCode1, optionImpl.hashCode());
        assertEquals(hashCode2, option2Impl.hashCode());

        Option option3 = new OptionBuilder().withLongOpt("option1").build();
        OptionImpl option3Impl = (OptionImpl) option3;

        assertEquals(hashCode1, option3Impl.hashCode());
    }

    @Test
    public void testFindOption() {
        Option option1 = new OptionBuilder().withLongOpt("option1").build();
        OptionImpl option1Impl = (OptionImpl) option1;

        Option option2 = new OptionBuilder().withLongOpt("option2").build();
        OptionImpl option2Impl = (OptionImpl) option2;

        Set<String> triggers = new HashSet<>();
        triggers.add("option1");
        triggers.add("option2");

        assertEquals(option1Impl, option1Impl.findOption("option1"));
        assertEquals(option1Impl, option1Impl.findOption("option1."));
        assertEquals(option1Impl, option1Impl.findOption("--option1"));
        assertEquals(option1Impl, option1Impl.findOption("--option1."));
        assertEquals(null, option1Impl.findOption("option2"));
        assertEquals(null, option1Impl.findOption("option3"));

        Set<String> prefixes = new HashSet<>();
        prefixes.add("--opt");
        option1Impl.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithNoPrefix() {
        Option option1 = new OptionBuilder().withLongOpt("option1").build();
        OptionImpl option1Impl = (OptionImpl) option1;

        Set<String> prefixes = new HashSet<>();
        option1Impl.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidPrefix() {
        Option option1 = new OptionBuilder().withLongOpt("option1").build();
        OptionImpl option1Impl = (OptionImpl) option1;

        Set<String> prefixes = new HashSet<>();
        prefixes.add("option1");

        option1Impl.checkPrefixes(prefixes);
    }

    @Test
    public void testDefaults() {
        Option option1 = new OptionBuilder().withLongOpt("option1").build();
        OptionImpl option1Impl = (OptionImpl) option1;

        WriteableCommandLine commandLine = new WriteableCommandLine();
        option1Impl.defaults(commandLine);

        assertTrue(commandLine.hasOption("option1"));
    }
}