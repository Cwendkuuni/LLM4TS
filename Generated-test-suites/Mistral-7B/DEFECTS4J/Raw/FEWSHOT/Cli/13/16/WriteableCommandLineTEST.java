package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.apache.commons.cli2.BaseOption;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.ParseResult;
import org.apache.commons.cli2.Values;

@RunWith(Parameterized.class)
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Parameters(name = "{index}: Option={0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new BaseOption(null, "opt1", true, null, "Option 1")},
                {new BaseOption(null, "opt2", false, null, "Option 2")},
                {new BaseOption(null, "opt3", true, "val1,val2", "Option 3 with values")},
                {new BaseOption(null, "opt4", false, "val1,val2", "Option 4 with values")},
                {new BaseOption(null, "opt5", false, null, "Option 5 with no values")}
        });
    }

    public WriteableCommandLineTest(Option option) {
        this.commandLine = new DefaultParser().parse(OptionGroup.builder().build(), new String[0]);
        this.commandLine.addOption(option);
    }

    @Test
    public void testAddOption() {
        assertEquals(option.getLongOpt(), commandLine.getOptions().get(0).getLongOpt());
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value1");
        assertEquals(1, option.getValues().size());
        assertEquals("value1", option.getValues().get(0));

        commandLine.addValue(option, "value2");
        assertEquals(2, option.getValues().size());
        assertEquals("value2", option.getValues().get(1));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<Object> values = option.getValues();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = option.getValues();
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(option.isSet());

        try {
            commandLine.addSwitch(option, true);
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.isSet());

        commandLine.setDefaultSwitch(option, false);
        assertFalse(option.isSet());
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("prop1", "property1");
        assertEquals("property1", commandLine.getProperty("prop1"));

        commandLine.addProperty("prop2", null);
        assertNull(commandLine.getProperty("prop2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--opt1"));
        assertTrue(commandLine.looksLikeOption("-opt1"));
        assertTrue(commandLine.looksLikeOption("--opt1="));
        assertTrue(commandLine.looksLikeOption("-opt1="));

        assertFalse(commandLine.looksLikeOption("--opt1=" + OptionBuilder.BUILD_LONG_OPT_ARGUMENTS.getValue("--opt1")));
        assertFalse(commandLine.looksLikeOption("-opt1=" + OptionBuilder.BUILD_LONG_OPT_ARGUMENTS.getValue("-opt1")));

        assertFalse(commandLine.looksLikeOption("--opt1=" + OptionBuilder.BUILD_SHORT_OPT_ARGUMENT.getValue('o')));
        assertFalse(commandLine.looksLikeOption("-opt1=" + OptionBuilder.BUILD_SHORT_OPT_ARGUMENT.getValue('o')));

        assertFalse(commandLine.looksLikeOption("--opt1="));
        assertFalse(commandLine.looksLikeOption("-opt1="));

        assertFalse(commandLine.looksLikeOption("arg"));
        assertFalse(commandLine.looksLikeOption("--"));
    }
}