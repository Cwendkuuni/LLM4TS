import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new CliBuilder().build();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addOption(option);
        Assert.assertNotNull(cmd.getOption("o"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new CliBuilder().build();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        cmd.addOption(option);
        cmd.addValue(option, "test");
        List values = cmd.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine cmd = new CliBuilder()
                .addOption(Option.builder("o").longOpt("option").valueSeparator('=').build())
                .parse("--option=test1 --option=test2")
                .build();
        List values = cmd.getUndefaultedValues(Option.builder("o").longOpt("option").build());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test1", values.get(0));
        Assert.assertEquals("test2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLine cmd = new CliBuilder().build();
        Option option = Option.builder("s").longOpt("switch").build();
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new CliBuilder().build();
        Option option = Option.builder("s").longOpt("switch").build();
        cmd.addSwitch(option, true);
        Assert.assertTrue(option.getValue().isPresent());
        Assert.assertEquals(true, option.getValue().get());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new CliBuilder().build();
        Option option = Option.builder("o").longOpt("option").build();
        List values = new ArrayList<>();
        values.add("default1");
        values.add("default2");
        cmd.setDefaultValues(option, values);
        List valuesFromCmd = cmd.getUndefaultedValues(option);
        Assert.assertEquals(values.size(), valuesFromCmd.size());
        for (int i = 0; i < values.size(); i++) {
            Assert.assertEquals(values.get(i), valuesFromCmd.get(i));
        }
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new CliBuilder().build();
        Option option = Option.builder("p").longOpt("property").build();
        cmd.addProperty(option, "name", "value");
        Assert.assertEquals("value", cmd.getOption("p").getValue().get());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new CliBuilder().build();
        Assert.assertTrue(cmd.looksLikeOption("--option"));
        Assert.assertFalse(cmd.looksLikeOption("--value"));
        Assert.assertFalse(cmd.looksLikeOption("-o"));
        Assert.assertFalse(cmd.looksLikeOption("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNull() {
        WriteableCommandLine cmd = new CliBuilder().build();
        cmd.addOption(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNull() {
        WriteableCommandLine cmd = new CliBuilder().build();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addOption(option);
        cmd.addValue(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesNull() {
        WriteableCommandLine cmd = new CliBuilder().build();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.setDefaultValues(option, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyNull() {
        WriteableCommandLine cmd = new CliBuilder().build();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addProperty(option, null, "value");
    }
}