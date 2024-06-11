package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ArgumentParser;

public class WriteableCommandLineImplTest {
    private Option rootOption;
    private WriteableCommandLineImpl commandLine;

    @org.junit.Before
    public void setUp() {
        Option option1 = OptionBuilder.withName("option1").withShortName('o').create();
        Option option2 = OptionBuilder.withName("option2").withShortName('p').create();
        Option arg1 = new Argument("arg1").setMetavar("FILE").create();
        Option arg2 = new Argument("arg2").setMetavar("NUMBER").create();
        this.rootOption = OptionBuilder.withName("root").create();

        this.commandLine = new WriteableCommandLineImpl(this.rootOption, Arrays.asList(arg1, arg2));
        this.commandLine.addOption(option1);
        this.commandLine.addOption(option2);
    }

    @org.junit.Test
    public void testAddOption() {
        Option option3 = OptionBuilder.withName("option3").create();
        this.commandLine.addOption(option3);
        assertSame(option3, this.commandLine.getOption("option3"));
        assertTrue(this.commandLine.getOptionTriggers().contains("option3"));
    }

    @org.junit.Test
    public void testAddValue() {
        Argument arg = new Argument("arg");
        this.commandLine.addValue(arg, "value1");
        List<String> values = this.commandLine.getValues(arg, null);
        assertEquals(Arrays.asList("value1"), values);

        this.commandLine.addValue(arg, "value2");
        values = this.commandLine.getValues(arg, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @org.junit.Test
    public void testAddSwitch() {
        Option option = OptionBuilder.withName("option").create();
        this.commandLine.addSwitch(option, true);
        assertTrue(this.commandLine.getSwitch(option, false));

        this.commandLine.addSwitch(option, false);
        assertFalse(this.commandLine.getSwitch(option, false));
    }

    @org.junit.Test
    public void testHasOption() {
        assertTrue(this.commandLine.hasOption(option1));
        assertFalse(this.commandLine.hasOption(option2));
    }

    @org.junit.Test
    public void testGetOption() {
        assertSame(option1, this.commandLine.getOption("option1"));
        assertSame(option2, this.commandLine.getOption("option2"));
    }

    @org.junit.Test
    public void testGetValues() {
        Argument arg = new Argument("arg");
        this.commandLine.addValue(arg, "value1");
        List<String> values = this.commandLine.getValues(arg, null);
        assertEquals(Arrays.asList("value1"), values);

        this.commandLine.addValue(arg, "value2");
        values = this.commandLine.getValues(arg, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @org.junit.Test
    public void testGetUndefaultedValues() {
        Argument arg = new Argument("arg");
        this.commandLine.addValue(arg, "value1");
        List<String> values = this.commandLine.getUndefaultedValues(arg);
        assertSame(values, Arrays.asList("value1"));

        this.commandLine.setDefaultValues(arg, Arrays.asList("default1", "default2"));
        values = this.commandLine.getUndefaultedValues(arg);
        assertSame(values, Collections.emptyList());
    }

    @org.junit.Test
    public void testGetSwitch() {
        Option option = OptionBuilder.withName("option").create();
        this.commandLine.addSwitch(option, true);
        assertTrue(this.commandLine.getSwitch(option, false));

        this.commandLine.addSwitch(option, false);
        assertFalse(this.commandLine.getSwitch(option, false));
    }

    @org.junit.Test
    public void testgetProperty() {
        Option option = OptionBuilder.withName("option").create();
        this.commandLine.addProperty(option, "property", "value");
        assertEquals("value", this.commandLine.getProperty(option, "default"));
    }

    @org.junit.Test
    public void testaddProperty() {
        Option option = OptionBuilder.withName("option").create();
        this.commandLine.addProperty(option, "property1", "value1");
        this.commandLine.addProperty(option, "property2", "value2");
        Map<String, String> properties = this.commandLine.getProperties(option);
        assertEquals(Arrays.asList("property1=value1", "property2=value2"), new ArrayList<>(properties.keySet()));
    }

    @org.junit.Test
    public void testgetPropertyWithDefault() {
        Option option = OptionBuilder.withName("option").create();
        this.commandLine.addProperty(option, "property", "default");
        assertEquals("default", this.commandLine.getProperty(option, null));
        assertEquals("default", this.commandLine.getProperty(option, ""));
    }

    @org.junit.Test
    public void testToString() {
        assertEquals("-o arg1 FILE arg2 NUMBER", this.commandLine.toString());
    }

    @org.junit.Test
    public void testGetOptions() {
        assertEquals(Arrays.asList(option1, option2), this.commandLine.getOptions());
    }

    @org.junit.Test
    public void testGetOptionTriggers() {
        assertEquals(Arrays.asList("option1", "option2"), this.commandLine.getOptionTriggers());
    }

    @org.junit.Test
    public void testSetDefaultValues() {
        Argument arg = new Argument("arg");
        List<String> defaults = Arrays.asList("default1", "default2");
        this.commandLine.setDefaultValues(arg, defaults);

        List<String> values = this.commandLine.getValues(arg, null);
        assertEquals(Arrays.asList("default1", "default2"), values);
    }

    @org.junit.Test
    public void testSetDefaultSwitch() {
        Option option = OptionBuilder.withName("option").create();
        this.commandLine.setDefaultSwitch(option, true);
        assertTrue(this.commandLine.getSwitch(option, false));
    }

    @org.junit.Test
    public void testGetNormalised() {
        assertEquals(Arrays.asList("arg1", "arg2"), this.commandLine.getNormalised());
    }

    @org.junit.Test
    public void testLooksLikeOption() {
        assertTrue(this.commandLine.looksLikeOption("-o"));
        assertFalse(this.commandLine.looksLikeOption("-x"));
    }

    @org.junit.Test
    public void testParseWithParsedValues() {
        ArgumentParser parser = new ArgumentParser();
        parser.parse(this.commandLine.getOptions().toArray(new Option[0]), new String[]{"-o", "arg1", "FILE", "-p", "arg2", "3"});

        WriteableCommandLine parsedCommandLine = parser.getCommandLine();
        Argument arg1 = parsedCommandLine.getOption("option1");
        Argument arg2 = parsedCommandLine.getOption("option2");
        assertEquals("arg1", arg1.getValue());
        assertEquals(3, ((Number)arg2.getValue()).intValue());
    }

    @org.junit.Test
    public void testParseWithMissingValues() {
        ArgumentParser parser = new ArgumentParser();
        try {
            parser.parse(this.commandLine.getOptions().toArray(new Option[0]), new String[]{"-o", "arg1", "-p"});
            fail("Expected exception");
        } catch (Exception e) {
            // expected
        }
    }
}