import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the abstract OptionImpl class.
 */
public class OptionImplTest {

    /**
     * Test for the basic functionality of OptionImpl.
     */
    @Test
    public void testBasicFunctionality() {
        final Option option = OptionBuilder.withLongOpt("input")
                .withDescription("an input file")
                .isRequired()
                .create();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new BasicParserCommandLine();

        try {
            parser.parse(option.canProcess(cmd, null), cmd, null);
            fail("Option should not be processed with no arguments");
        } catch (ParseException e) {
            // expected
        }

        cmd.addOption("--input", "test.txt");
        parser.parse(option.canProcess(cmd, null), cmd, null);
        assertTrue(cmd.hasOption("input"));
        assertEquals("test.txt", cmd.getOptionValue("input"));
    }

    /**
     * Test for the equals and hashCode methods.
     */
    @Test
    public void testEqualsAndHashCode() {
        final Option option1 = OptionBuilder.withLongOpt("input1")
                .withDescription("an input file 1")
                .create();
        final Option option2 = OptionBuilder.withLongOpt("input2")
                .withDescription("an input file 2")
                .create();

        assertEquals(option1, option1);
        assertEquals(option1, option1);
        assertEquals(option1.hashCode(), option1.hashCode());
        assertEquals(option1.hashCode(), option1.hashCode());

        assertEquals(option1, option2);
        assertEquals(option1.hashCode(), option2.hashCode()); // should be different

        assertNotEquals(option1, null);
        assertNotEquals(option1, new Object());

        assertNotEquals(option1, option2);
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    /**
     * Test for the findOption method.
     */
    @Test
    public void testFindOption() {
        final Option option = OptionBuilder.withLongOpt("input")
                .withDescription("an input file")
                .create();

        Set prefixes = java.util.Collections.singleton("--i");

        Option foundOption = option.findOption(prefixes, "input");
        assertEquals(option, foundOption);

        foundOption = option.findOption(prefixes, "output");
        assertNull(foundOption);
    }

    /**
     * Test for the checkPrefixes method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        final Option option = OptionBuilder.withLongOpt("input")
                .withDescription("an input file")
                .create();

        Set prefixes = java.util.Collections.singleton("--o");

        option.checkPrefixes(prefixes);
    }

    /**
     * Test for the defaults method.
     */
    @Test
    public void testDefaults() {
        final Option option = OptionBuilder.withLongOpt("input")
                .withDescription("an input file")
                .create();

        WriteableCommandLine cmd = new BasicParserCommandLine();
        option.defaults(cmd);

        assertFalse(cmd.hasOption("input"));
    }
}