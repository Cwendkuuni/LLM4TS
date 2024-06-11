import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.GroupImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.builder.CommandBuilder;
import org.apache.commons.cli2.option.DefaultOptionBuilder;
import org.apache.commons.cli2.util.HelpFormatter;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new DefaultOptionBuilder().create(), null);
        Argument arg1 = new DefaultOptionBuilder().withLongName("arg1").create();

        commandLine.addOption(arg1);

        assertTrue(commandLine.hasOption(arg1));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new DefaultOptionBuilder().create(), null);
        Argument arg1 = new DefaultOptionBuilder().withLongName("arg1").create();

        commandLine.addValue(arg1, "value");

        assertEquals("value", commandLine.getUndefaultedValues(arg1).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new DefaultOptionBuilder().create(), null);
        Option opt1 = new DefaultOptionBuilder().create();

        commandLine.addSwitch(opt1, true);

        assertTrue(commandLine.getSwitch(opt1, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new DefaultOptionBuilder().create(), null);
        Argument arg1 = new DefaultOptionBuilder().withLongName("arg1").create();

        commandLine.addProperty(arg1, "key", "value");

        assertEquals("value", commandLine.getProperty(arg1, "key", "default"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new DefaultOptionBuilder().create(), null);

        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new DefaultOptionBuilder().create(), null);
        Option opt1 = new DefaultOptionBuilder().create();
        Option opt2 = new DefaultOptionBuilder().create();

        commandLine.addOption(opt1);
        commandLine.addOption(opt2);

        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new DefaultOptionBuilder().create(), null);
        commandLine.addOption(new DefaultOptionBuilder().withLongName("arg1").create());
        commandLine.addOption(new DefaultOptionBuilder().withLongName("arg2").create());

        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new DefaultOptionBuilder().create(), null);
        Argument arg1 = new DefaultOptionBuilder().withLongName("arg1").create();
        commandLine.addOption(arg1);

        commandLine.setDefaultValues(arg1, null);

        assertEquals(0, commandLine.getValues(arg1, null).size());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new DefaultOptionBuilder().create(), null);
        Option opt1 = new DefaultOptionBuilder().create();

        commandLine.setDefaultSwitch(opt1, true);

        assertTrue(commandLine.getSwitch(opt1, false));
    }

}