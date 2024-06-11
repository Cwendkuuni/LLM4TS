import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output directory").required(false);
        List arguments = new ArrayList();
        arguments.add("test");
        arguments.add("--output");
        arguments.add("/tmp");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertEquals("--output /tmp test", writeableCommandLine.toString());

        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertEquals(option, writeableCommandLine.getOption("output"));

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals("/tmp", values.get(0));
        Assert.assertTrue(values.isEmpty() || values.size() > 1);

        Assert.assertFalse(writeableCommandLine.getSwitch(option, false));
        writeableCommandLine.addSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));

        Option propertyOption = new Option("p", "property", true, "Property name");
        writeableCommandLine.addProperty(propertyOption, "my.property", "value");
        Map<String, String> properties = writeableCommandLine.getProperties(propertyOption);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value", properties.get("my.property"));

        Assert.assertEquals("--output /tmp test", writeableCommandLine.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDefaultValuesTwice() {
        Option option = new Option("o", "output", true, "Output directory").required(false);
        List arguments = new ArrayList();
        arguments.add("test");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.setDefaultValues(option, new ArrayList() {{
            add("dir1");
            add("dir2");
        }});
        writeableCommandLine.setDefaultValues(option, new ArrayList() {{
            add("dir3");
            add("dir4");
        }});
    }

    @Test
    public void testToStringWithEmptyArguments() {
        Option option = new Option("o", "output", true, "Output directory").required(false);
        List arguments = new ArrayList();

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertEquals("", writeableCommandLine.toString());
    }

    @Test
    public void testToStringWithNoOptions() {
        List arguments = new ArrayList();
        arguments.add("test");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, arguments);

        Assert.assertEquals("test", writeableCommandLine.toString());
    }
}