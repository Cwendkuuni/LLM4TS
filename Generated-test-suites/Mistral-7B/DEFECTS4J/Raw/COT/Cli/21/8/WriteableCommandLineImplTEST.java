import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;

@RunWith(JUnit4.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl cmd;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        parser = new DefaultParser();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("opt").valueSeparator(':').build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        Assert.assertEquals(Collections.singletonList(option), cmd.getOptions());
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("opt").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addValue(option, "test");
        List values = cmd.getValues(option, Collections.emptyList());
        Assert.assertEquals("test", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addSwitch(option, true);
        Boolean switchValue = cmd.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o").longOpt("opt").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        Assert.assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o").longOpt("opt").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        Option returnedOption = cmd.getOption("opt");
        Assert.assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("o").longOpt("opt").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        List values = cmd.getValues(option, Collections.emptyList());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("opt").valueSeparator(':').build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addValue(option, "value1");
        List values = cmd.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addSwitch(option, true);
        Boolean switchValue = cmd.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchDefault() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        Boolean switchValue = cmd.getSwitch(option, null);
        Assert.assertNull(switchValue);
    }

    @Test
    public void testGetProperties() {
        Option option = Option.builder("o").longOpt("opt").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addProperty(option, "property", "value");
        Set properties = cmd.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains("property"));
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("o").longOpt("opt").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addProperty(option, "property", "value");
        String property = cmd.getProperty(option, "property", null);
        Assert.assertEquals("value", property);
    }

    @Test
    public void testToString() {
        Option option = Option.builder("o").longOpt("opt").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        String toString = cmd.toString();
        String expected = "value1 value2";
        Assert.assertEquals(expected, toString);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, Collections.emptyList());
        Assert.assertFalse(cmd.looksLikeOption("test"));
        Option option = Option.builder("o").longOpt("opt").build();
        cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        Assert.assertTrue(cmd.looksLikeOption("opt"));
    }

    @Test
    public void testParse() throws ParseException {
        Option option = Option.builder("o").longOpt("opt").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        OptionParser parser = new DefaultParser();
        ParseContext context = new ParseContext();
        parser.parse(option, new String[]{"--opt", "value1", "--opt", "value2"});
        cmd.parse(parser.getParsedOptions(), context);
        List values = cmd.getValues(option, Collections.emptyList());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }
}