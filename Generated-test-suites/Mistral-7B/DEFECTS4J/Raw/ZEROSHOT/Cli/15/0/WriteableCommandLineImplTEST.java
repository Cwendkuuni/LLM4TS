import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file path");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertFalse(commandLine.hasOption(option));
        Assert.assertNull(commandLine.getOption("nonExistentOption"));
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption("output"));
        Assert.assertEquals(1, commandLine.getValues(option).size());
        Assert.assertEquals("test.txt", commandLine.getValues(option).get(0));
        Assert.assertEquals(false, commandLine.getSwitch(option, false));
        Assert.assertNull(commandLine.getProperty("nonExistentProperty"));
        Assert.assertEquals(Collections.emptySet(), commandLine.getProperties());
        Assert.assertTrue(commandLine.looksLikeOption("o"));
        Assert.assertEquals("test.txt", commandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImplWithDefaultValues() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run test"), option);

        commandLine.addSwitch(option, true);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        commandLine.addValues(option, new ArrayList() {{
            add("value3");
            add("value4");
        }});

        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertEquals(true, commandLine.getSwitch(option, false));
        Assert.assertEquals(4, commandLine.getValues(option).size());
        Assert.assertEquals("value3", commandLine.getValues(option).get(0));
        Assert.assertEquals("value4", commandLine.getValues(option).get(1));
        Assert.assertEquals("value1", commandLine.getProperty(option, ""));
        Assert.assertEquals("value2", commandLine.getProperty(option, "property2", "defaultValue"));
        Assert.assertEquals(2, commandLine.getProperties(option).size());
        Assert.assertTrue(commandLine.looksLikeOption("output"));
        Assert.assertEquals("--output, -o, output, Output file path", commandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImplWithDefaultValuesFromOption() {
        Option option = new Option("o", "output", true, "Output file path")
                .setDefaultValues(new ArrayList() {{
                    add("defaultValue1");
                    add("defaultValue2");
                }});

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option);

        Assert.assertEquals(2, commandLine.getValues(option).size());
        Assert.assertEquals("defaultValue1", commandLine.getValues(option).get(0));
        Assert.assertEquals("defaultValue2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testWriteableCommandLineImplWithDefaultSwitches() {
        Option option = new Option("o", "output", false, "Output flag");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option);

        Assert.assertEquals(false, commandLine.getSwitch(option, null));
        commandLine.setDefaultSwitch(option, true);
        Assert.assertEquals(true, commandLine.getSwitch(option, null));
    }
}