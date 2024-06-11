import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Options options;

    @Before
    public void setup() throws ParseException {
        options = new Options();
        options.addRequiredOption("a", "alpha", true, "Alpha value");
        options.addOption("b", "beta", false, "Beta value");
        options.addOption("c", "char", 'c', "Character value");
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"-a", "1", "-b", "2", "--char", "d"});
    }

    @Test
    public void testHasOption() {
        Assert.assertTrue(commandLine.hasOption(options.getOption("a")));
        Assert.assertTrue(commandLine.hasOption(options.getOption("b")));
        Assert.assertFalse(commandLine.hasOption(options.getOption("d")));
    }

    @Test
    public void testGetOption() {
        Assert.assertEquals(options.getOption("a"), commandLine.getOption("a"));
        Assert.assertEquals(options.getOption("b"), commandLine.getOption("b"));
        Assert.assertNull(commandLine.getOption("d"));
    }

    @Test
    public void testGetValues() {
        List values = commandLine.getValues(options.getOption("a"), null);
        Assert.assertEquals(values, new ArrayList(){{add("1");}});

        values = commandLine.getValues(options.getOption("b"), null);
        Assert.assertEquals(values, new ArrayList());

        values = commandLine.getValues(options.getOption("c"), null);
        Assert.assertEquals(values, Collections.EMPTY_LIST);
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = commandLine.getUndefaultedValues(options.getOption("a"));
        Assert.assertEquals(values, new ArrayList(){{add("1");}});

        values = commandLine.getUndefaultedValues(options.getOption("b"));
        Assert.assertEquals(values, Collections.EMPTY_LIST);
    }

    @Test
    public void testGetSwitch() {
        Boolean switchValue = commandLine.getSwitch(options.getOption("b"), false);
        Assert.assertFalse(switchValue);

        switchValue = commandLine.getSwitch(options.getOption("a"), null);
        Assert.assertNull(switchValue);
    }

    @Test
    public void testGetProperty() {
        Assert.assertNull(commandLine.getProperty("not.a.property"));

        commandLine.addProperty(options.getOption("a"), "propA", "valueA");
        Assert.assertEquals("valueA", commandLine.getProperty("a.propA"));
        Assert.assertEquals("valueA", commandLine.getProperty("propA"));
        Assert.assertEquals("valueA", commandLine.getProperty(options.getOption("a"), "defaultValue"));

        commandLine.addProperty(options.getOption("a"), "propB", "valueB");
        Assert.assertEquals("valueA", commandLine.getProperty("a.propA"));
        Assert.assertEquals("valueB", commandLine.getProperty("a.propB"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingSwitch() {
        commandLine.addSwitch(options.getOption("a"), true);
        commandLine.addSwitch(options.getOption("a"), false);
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(options.getOption("a"), "value1");
        Assert.assertEquals(new ArrayList(){{add("1"); add("value1");}}, commandLine.getValues(options.getOption("a"), null));

        commandLine.addValue(options.getOption("a"), "value2");
        Assert.assertEquals(new ArrayList(){{add("1"); add("value1"); add("value2");}}, commandLine.getValues(options.getOption("a"), null));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("-a 1 -b 2 --char d", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Assert.assertEquals(options.getOptions(), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Assert.assertEquals(options.getOptionNames(), commandLine.getOptionTriggers());
    }

    @Test
    public void testAddPropertyWithExistingProperty() {
        commandLine.addProperty(options.getOption("a"), "propA", "valueA");
        commandLine.addProperty(options.getOption("a"), "propA", "valueB");
        Assert.assertEquals("valueB", commandLine.getProperty("a.propA"));
    }
}