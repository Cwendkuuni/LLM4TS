import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.cli2.test.TestUtils.createMockCommandLine;
import static org.apache.commons.cli2.test.TestUtils.createMockOption;
import static org.apache.commons.cli2.test.TestUtils.createMockParser;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        Option option = createMockOption("--test2", null);

        cmd.addOption(option);

        Assert.assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        Option option = createMockOption("--test", new Argument("[arg]"));
        Object value = "value";

        cmd.addValue(option, value);

        List values = cmd.getValues(option, null);
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        Option option = createMockOption("--test", null);

        cmd.addSwitch(option, true);

        Boolean switchValue = cmd.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        Option option = createMockOption("--test");

        Assert.assertEquals(option, cmd.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), new ArrayList() {{
            add("arg1");
            add("arg2");
        }});

        List values = cmd.getValues(createMockOption("--test"), null);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("arg1", values.get(0));
        Assert.assertEquals("arg2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), new ArrayList() {{
            add("arg1");
            add("arg2");
        }});

        List values = cmd.getUndefaultedValues(createMockOption("--test"));

        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        Option option = createMockOption("--test", null);
        Boolean defaultValue = false;

        cmd.addSwitch(option, true);

        Boolean switchValue = cmd.getSwitch(option, defaultValue);

        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new PropertyOption("--test", "property"), null);
        cmd.addProperty(cmd.getOption("--test"), "property", "value");

        String propertyValue = cmd.getProperty(cmd.getOption("--test"), "defaultValue");

        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new PropertyOption("--test", "property"), null);

        cmd.addProperty("property", "value");

        Set properties = cmd.getProperties(cmd.getOption("--test"));

        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("property", properties.iterator().next());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);

        Assert.assertTrue(cmd.looksLikeOption("--test"));
        Assert.assertFalse(cmd.looksLikeOption("--test2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchException() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        Option option = createMockOption("--test", null);

        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        cmd.addOption(createMockOption("--test2"));

        Assert.assertEquals("[--test, --test2]", cmd.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        Option option1 = createMockOption("--test1");
        Option option2 = createMockOption("--test2");

        cmd.addOption(option1);
        cmd.addOption(option2);

        List options = cmd.getOptions();

        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        Option option1 = createMockOption("--test1");
        Option option2 = createMockOption("--test2");

        cmd.addOption(option1);
        cmd.addOption(option2);

        Set optionTriggers = cmd.getOptionTriggers();

        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertEquals(option1.getPreferredName(), optionTriggers.iterator().next());
        Assert.assertEquals(option2.getPreferredName(), optionTriggers.iterator().next());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        Option option = createMockOption("--test", new Argument("[arg]"));
        List defaults = new ArrayList() {{
            add("arg1");
            add("arg2");
        }};

        cmd.setDefaultValues(option, defaults);

        List values = cmd.getValues(option, null);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("arg1", values.get(0));
        Assert.assertEquals("arg2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        Option option = createMockOption("--test", null);

        cmd.setDefaultSwitch(option, true);

        Boolean switchValue = cmd.getSwitch(option, false);

        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(createMockOption("--test"), null);
        cmd.addOption(createMockOption("--test2", new Argument("[arg]")));
        cmd.addOption(createMockOption("--test3", new Argument("[arg]")));

        List normalised = cmd.getNormalised();

        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("--test", normalised.get(0));
        Assert.assertEquals("--test2", normalised.get(1));
        Assert.assertEquals("--test3", normalised.get(2));
    }
}