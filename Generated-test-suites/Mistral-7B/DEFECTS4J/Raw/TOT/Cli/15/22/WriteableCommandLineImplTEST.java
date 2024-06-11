package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() throws ParseException {
        Options options = new Options();
        Option optionA = options.option("a", "a", true, "Option A");
        Option optionB = options.option("b", "b", false, "Option B");
        Option optionC = options.option("c", "c", false, "Option C");
        Option optionD = options.option("d", "d", true, "Option D", 1, "value");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(optionA, Collections.singletonList("arg1"));
        commandLine.addOption(optionB);
        commandLine.addSwitch(optionC, true);
        commandLine.addValue(optionD, "value1");

        CommandLine parsed = options.parse(commandLine.getNormalised().toArray(new String[0]));

        Assert.assertTrue(parsed.hasOption(optionA));
        Assert.assertTrue(parsed.hasOption(optionB));
        Assert.assertTrue(parsed.isSet(optionC));
        Assert.assertEquals(1, parsed.getValues(optionD).size());
        Assert.assertEquals("value1", parsed.getValues(optionD).get(0));

        // Test property option
        commandLine = new WriteableCommandLineImpl(optionD, Collections.singletonList("arg1"));
        commandLine.addProperty(optionD, "property", "value2");

        parsed = options.parse(commandLine.getNormalised().toArray(new String[0]));

        Assert.assertEquals("value2", parsed.getProperty("property"));
    }

    @Test(expected = ParseException.class)
    public void testWriteableCommandLineImplWithInvalidOption() throws ParseException {
        Options options = new Options();
        Option optionA = options.option("a", "a", true, "Option A");
        Option optionB = new Option("b", "b") {{
            this.setRequired(true);
        }};

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(optionA, Collections.singletonList("arg1"));
        commandLine.addOption(optionB);

        options.parse(commandLine.getNormalised().toArray(new String[0]));
    }
}