import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.DefaultOptionBuilder;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new DefaultOptionBuilder().withName("root").create();
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = mock(Option.class);
        commandLine.addOption(option);

        assert commandLine.getOptions().contains(option);
    }

    @Test
    public void testAddValue() {
        Argument argument = mock(Argument.class);
        Option option = argument;
        Object value = "value";
        commandLine.addValue(option, value);

        assert commandLine.getValues(option, null).contains(value);
    }

    @Test
    public void testAddSwitch() {
        Option option = mock(Option.class);
        commandLine.addSwitch(option, true);

        assert commandLine.getSwitch(option, null) == true;
    }

    @Test
    public void testHasOption() {
        Option option = mock(Option.class);
        commandLine.addOption(option);

        assert commandLine.hasOption(option) == true;
    }

    @Test
    public void testGetOption() {
        Option option = mock(Option.class);
        when(option.getPreferredName()).thenReturn("test");
        commandLine.addOption(option);

        assert commandLine.getOption("test") == option;
    }

    @Test
    public void testGetValues() {
        Argument argument = mock(Argument.class);
        Option option = argument;
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assert commandLine.getValues(option, null).equals(values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = mock(Argument.class);
        Option option = argument;
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assert commandLine.getUndefaultedValues(option).equals(values);
    }

    @Test
    public void testGetSwitch() {
        Option option = mock(Option.class);
        commandLine.addSwitch(option, true);

        assert commandLine.getSwitch(option, false) == true;
    }

    @Test
    public void testGetProperty() {
        String property = "test";
        commandLine.addProperty(property, "value");

        assert commandLine.getProperty(property).equals("value");
    }

    @Test
    public void testAddProperty() {
        Option option = mock(Option.class);
        commandLine.addProperty(option, "property", "value");

        assert commandLine.getProperties(option).contains("property");
    }

    @Test
    public void testGetPropertyWithDefault() {
        Option option = mock(Option.class);
        commandLine.addProperty(option, "property", "value");

        assert commandLine.getProperty(option, "property", "default").equals("value");
    }

    @Test
    public void testLooksLikeOption() {
        assert commandLine.looksLikeOption("-test") == true;
        assert commandLine.looksLikeOption("test") == false;
    }

    @Test
    public void testToString() {
        Argument argument = mock(Argument.class);
        Option option = argument;
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(option, arguments);

        assert commandLine.toString().equals("arg1 arg2");
    }

    @Test
    public void testGetOptions() {
        Option option = mock(Option.class);
        commandLine.addOption(option);

        assert commandLine.getOptions().contains(option);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = mock(Option.class);
        when(option.getPreferredName()).thenReturn("test");
        commandLine.addOption(option);

        assert commandLine.getOptionTriggers().contains("test");
    }

    @Test
    public void testSetDefaultValues() {
        Argument argument = mock(Argument.class);
        Option option = argument;
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        commandLine.setDefaultValues(option, values);

        assert commandLine.getValues(option, null).equals(values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = mock(Option.class);
        commandLine.setDefaultSwitch(option, true);

        assert commandLine.getSwitch(option, null) == true;
    }

    @Test
    public void testGetNormalised() {
        Argument argument = mock(Argument.class);
        Option option = argument;
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(option, arguments);

        assert commandLine.getNormalised().equals(arguments);
    }

}