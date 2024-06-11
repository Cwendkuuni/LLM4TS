
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample option
        Option option = new Option();
        cli.addOption(option);

        // Check if option was added successfully
        assertTrue(cli.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample option
        Option option = new Argument();
        cli.addOption(option);

        // Add value to the option
        String value = "sample value";
        cli.addValue(option, value);

        // Check if value was added successfully
        assertTrue(cli.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample option
        Option option = new Option();
        cli.addOption(option);

        // Add switch to the option
        boolean switchVal = true;
        cli.addSwitch(option, switchVal);

        // Check if switch value was added successfully
        assertEquals(switchVal, cli.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample option
        Option option = new Option();
        cli.addOption(option);

        // Check if option exists
        assertTrue(cli.hasOption(option));
    }

    @Test
    public void testGetOption() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample option
        Option option = new Option();
        cli.addOption(option);

        // Check if correct option is returned
        assertEquals(option, cli.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample option
        Option option = new Argument();
        cli.addOption(option);

        // Add values to the option
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        cli.addValue(option, values.get(0));
        cli.addValue(option, values.get(1));

        // Check if correct values are returned
        assertEquals(values, cli.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample option
        Option option = new Argument();
        cli.addOption(option);

        // Add values to the option
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        cli.addValue(option, values.get(0));
        cli.addValue(option, values.get(1));

        // Check if correct values are returned
        assertEquals(values, cli.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample option
        Option option = new Option();
        cli.addOption(option);

        // Add switch to the option
        boolean switchVal = true;
        cli.addSwitch(option, switchVal);

        // Check if correct switch value is returned
        assertEquals(switchVal, cli.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample property
        String property = "sampleProperty";
        String value = "sampleValue";
        cli.addProperty(property, value);

        // Check if correct property value is returned
        assertEquals(value, cli.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample property
        String property = "sampleProperty";
        String value = "sampleValue";
        cli.addProperty(property, value);

        // Check if property was added successfully
        assertTrue(cli.getProperties().contains(property));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample property
        String property = "sampleProperty";
        String defaultValue = "defaultValue";
        String value = "sampleValue";
        cli.addProperty(property, value);

        // Check if correct property value is returned
        assertEquals(value, cli.getProperty(new PropertyOption(), property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample property
        String property = "sampleProperty";
        cli.addProperty(property, "value");

        // Check if property exists
        assertTrue(cli.getProperties().contains(property));
    }

    @Test
    public void testSetDefaultValues() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample option
        Option option = new Option();
        cli.addOption(option);

        // Set default values for the option
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        cli.setDefaultValues(option, defaultValues);

        // Check if default values were set successfully
        assertEquals(defaultValues, cli.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample option
        Option option = new Option();
        cli.addOption(option);

        // Set default switch value for the option
        boolean defaultSwitch = true;
        cli.setDefaultSwitch(option, defaultSwitch);

        // Check if default switch value was set successfully
        assertEquals(defaultSwitch, cli.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), new ArrayList());

        // Add sample arguments
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        cli.addNormalised(arguments);

        // Check if correct arguments are returned
        assertEquals(arguments, cli.getNormalised());
    }
}
