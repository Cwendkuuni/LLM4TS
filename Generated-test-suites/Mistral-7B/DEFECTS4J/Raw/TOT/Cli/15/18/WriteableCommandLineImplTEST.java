import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // given
        Options options = new Options();
        options.addOption("a", "alpha", true, "Set alpha value");
        options.addOption("b", "beta", false, "Set beta value");
        options.addOption("c", "chunkSize", 5, "Set chunk size");
        options.addOption("d", "debug", false, "Enable debug mode");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"-a", "1", "-b", "--debug"};
        WriteableCommandLineImpl cmdLine = (WriteableCommandLineImpl) parser.parse(options, args);

        // when

        // Test hasOption
        Assert.assertTrue(cmdLine.hasOption(options.getOption("a")));
        Assert.assertTrue(cmdLine.hasOption(options.getOption("d")));
        Assert.assertFalse(cmdLine.hasOption(options.getOption("z")));

        // Test getOption
        Option aOption = options.getOption("a");
        Option dOption = options.getOption("d");

        Assert.assertEquals(aOption, cmdLine.getOption("a"));
        Assert.assertEquals(dOption, cmdLine.getOption("d"));
        Assert.assertNull(cmdLine.getOption("z"));

        // Test getValues
        List<String> aValues = cmdLine.getValues(aOption, null);
        Assert.assertEquals("1", aValues.get(0));

        // Test getUndefaultedValues
        List<String> aUndefaultedValues = cmdLine.getUndefaultedValues(aOption);
        Assert.assertEquals(Collections.emptyList(), aUndefaultedValues);

        // Test getSwitch
        Boolean dSwitch = cmdLine.getSwitch(dOption, false);
        Assert.assertTrue(dSwitch);

        // Test getProperty
        Option pOption = new Option("p", "prefix", true, "Set prefix");
        cmdLine.addProperty(pOption, "test", "prefixValue");
        Assert.assertEquals("prefixValue", cmdLine.getProperty(pOption, "defaultValue"));

        // Test getProperties
        Set<String> pKeys = cmdLine.getProperties(pOption);
        Assert.assertEquals(Collections.singleton("prefix"), pKeys);

        // Test looksLikeOption
        Assert.assertTrue(cmdLine.looksLikeOption("--a"));
        Assert.assertFalse(cmdLine.looksLikeOption("--x"));

        // Test toString
        Assert.assertEquals("-a 1 --debug", cmdLine.toString());

        // Test getOptions
        List<Option> allOptions = cmdLine.getOptions();
        Assert.assertEquals(options.getOptions().size(), allOptions.size());
        Assert.assertEquals(options.getOption("a"), allOptions.get(0));
        Assert.assertEquals(options.getOption("b"), allOptions.get(1));
        Assert.assertEquals(options.getOption("c"), allOptions.get(2));
        Assert.assertEquals(options.getOption("d"), allOptions.get(3));
        Assert.assertEquals(options.getOption("p"), allOptions.get(4));

        // Test getOptionTriggers
        Set<String> allTriggers = cmdLine.getOptionTriggers();
        Assert.assertEquals(options.getOptionNames().size(), allTriggers.size());
        Assert.assertEquals("a", allTriggers.iterator().next());
        Assert.assertEquals("b", allTriggers.iterator().next());
        Assert.assertEquals("c", allTriggers.iterator().next());
        Assert.assertEquals("d", allTriggers.iterator().next());
        Assert.assertEquals("p", allTriggers.iterator().next());

        // Test setDefaultValues
        List<Integer> cDefaultValues = Arrays.asList(10, 20);
        cmdLine.setDefaultValues(options.getOption("c"), cDefaultValues);
        Assert.assertEquals(10, ((Number) cmdLine.getValues(options.getOption("c"), null).get(0)).intValue());

        // Test setDefaultSwitch
        cmdLine.setDefaultSwitch(options.getOption("d"), false);
        Assert.assertFalse(cmdLine.getSwitch(options.getOption("d"), null));

        // Test getNormalised
        List<String> normalised = cmdLine.getNormalised();
        Assert.assertEquals(args.length, normalised.size());
        for (int i = 0; i < args.length; i++) {
            Assert.assertEquals(args[i], normalised.get(i));
        }
    }
}