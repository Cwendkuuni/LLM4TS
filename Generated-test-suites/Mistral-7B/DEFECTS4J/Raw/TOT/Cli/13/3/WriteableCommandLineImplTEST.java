import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option optionA = new Option("a", "anOptionA")
                .longOpt("anOptionALong")
                .valueSeparator('=')
                .requiredArg()
                .desc("An option A")
                .build();

        Option optionB = new Option("b", "anOptionB")
                .longOpt("anOptionBLong")
                .noArg()
                .desc("An option B")
                .build();

        Option rootOption = new Option("root", "rootOption")
                .desc("Root option")
                .build();

        rootOption.addOption(optionA);
        rootOption.addOption(optionB);

        List arguments = new ArrayList() {{
            add("--anOptionA=valueA");
            add("--anOptionB");
            add("--anOptionALong");
            add("anArgument");
        }};

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Test getting options
        Option anOptionA = commandLine.getOption("anOptionA");
        Option anOptionB = commandLine.getOption("anOptionB");
        Assert.assertNotNull(anOptionA);
        Assert.assertNotNull(anOptionB);

        // Test getting values
        List valuesA = commandLine.getValues(optionA, null);
        Assert.assertEquals("valueA", valuesA.get(0));

        // Test getting switches
        Boolean switchB = commandLine.getSwitch(optionB, null);
        Assert.assertTrue(switchB);

        // Test getting properties
        Assert.assertEquals("valueA", commandLine.getProperty("anOptionA", ""));

        // Test getting options list
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(optionA));
        Assert.assertTrue(options.contains(optionB));

        // Test looking like option
        Assert.assertTrue(commandLine.looksLikeOption("--anOptionA"));
        Assert.assertTrue(commandLine.looksLikeOption("-a"));
        Assert.assertFalse(commandLine.looksLikeOption("--anOtherOption"));

        // Test toString
        Assert.assertEquals("--anOptionA=valueA --anOptionB --anOptionALong anArgument", commandLine.toString());
    }

    @Test(expected = ParseException.class)
    public void testParseException() {
        Option optionA = new Option("a", "anOptionA")
                .longOpt("anOptionALong")
                .valueSeparator('=')
                .requiredArg()
                .desc("An option A")
                .build();

        Option rootOption = new Option("root", "rootOption")
                .desc("Root option")
                .build();

        rootOption.addOption(optionA);

        List arguments = new ArrayList() {{
            add("--anOptionA");
        }};

        WriteableCommandLineImpl commandLine = null;

        try {
            commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        } catch (ParseException e) {
            // expected
        }

        Assert.assertNull(commandLine);
    }
}