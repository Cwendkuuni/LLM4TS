import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for WriteableCommandLineImpl class
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("Test option for WriteableCommandLineImpl")
                .withValueSeparator('=')
                .withValues("valueOne, valueTwo")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        // When - option is present in command line arguments
        cmd.parse("--testOption valueOne --testOption valueTwo testArg");

        // Then
        Assert.assertTrue(cmd.hasOption(option));
        Assert.assertEquals(2, cmd.getValues(option, null).size());
        Assert.assertEquals("valueOne", cmd.getValues(option, null).get(0));
        Assert.assertEquals("valueTwo", cmd.getValues(option, null).get(1));

        // When - option is not present in command line arguments
        cmd = new WriteableCommandLineImpl(option, null);
        cmd.parse(new String[]{"testArg"});

        // Then
        Assert.assertFalse(cmd.hasOption(option));
        Assert.assertNull(cmd.getValues(option, null));

        // When - option is present with incorrect value separator
        cmd = new WriteableCommandLineImpl(option, null);
        cmd.parse("--testOption valueOne valueTwo testArg");

        // Then
        Assert.assertTrue(cmd.hasOption(option));
        Assert.assertEquals(2, cmd.getValues(option, null).size());
        Assert.assertEquals("valueOne", cmd.getValues(option, null).get(0));
        Assert.assertEquals("valueTwo testArg", cmd.getValues(option, null).get(1));

        // When - option value is empty
        cmd = new WriteableCommandLineImpl(option, null);
        cmd.parse("--testOption");

        // Then
        Assert.assertTrue(cmd.hasOption(option));
        Assert.assertEquals(0, cmd.getValues(option, null).size());

        // When - option switch is present
        Option switchOption = OptionBuilder.withLongOpt("switchOption")
                .withDescription("Test switch option for WriteableCommandLineImpl")
                .withValueSeparator(Option.NO_VALUE_SEPARATOR)
                .create();
        cmd = new WriteableCommandLineImpl(option, null);
        cmd.parse("--testOption --switchOption");

        // Then
        Assert.assertTrue(cmd.hasOption(option));
        Assert.assertEquals(0, cmd.getValues(option, null).size());
        Assert.assertTrue(cmd.getSwitch(switchOption, false));

        // When - option switch is not present
        cmd = new WriteableCommandLineImpl(option, null);
        cmd.parse("--testOption");

        // Then
        Assert.assertTrue(cmd.hasOption(option));
        Assert.assertEquals(0, cmd.getValues(option, null).size());
        Assert.assertFalse(cmd.getSwitch(switchOption, false));

        // When - option switch with value is present
        cmd = new WriteableCommandLineImpl(option, null);
        cmd.parse("--testOption --switchOption true");

        // Then
        Assert.assertTrue(cmd.hasOption(option));
        Assert.assertEquals(0, cmd.getValues(option, null).size());
        Assert.assertTrue(cmd.getSwitch(switchOption, false));
        Assert.assertEquals("true", cmd.getProperty("switchOption"));

        // When - option is present multiple times
        cmd = new WriteableCommandLineImpl(option, null);
        cmd.parse("--testOption valueOne --testOption valueTwo --testOption");

        // Then
        Assert.assertTrue(cmd.hasOption(option));
        Assert.assertEquals(2, cmd.getValues(option, null).size());
        Assert.assertEquals("valueOne", cmd.getValues(option, null).get(0));
        Assert.assertEquals("valueTwo", cmd.getValues(option, null).get(1));
    }
}