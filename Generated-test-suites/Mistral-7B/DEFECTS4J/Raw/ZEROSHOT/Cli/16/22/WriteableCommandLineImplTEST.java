import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .valueSeparator(' ')
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("o"));
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .valueSeparator(' ')
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .valueSeparator(' ')
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(Collections.emptyList(), commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }

    @Test
    public void testGetProperties() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals(Arrays.asList("property1=value1", "property2=value2"), commandLine.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertTrue(commandLine.looksLikeOption("o"));
        assertTrue(commandLine.looksLikeOption("option"));
        assertFalse(commandLine.looksLikeOption("x"));
    }

    @Test
    public void testToString() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals("arg1 arg2 -o option=value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(Arrays.asList("o1", "option1", "o2", "option2"), commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .valueSeparator(' ')
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
}