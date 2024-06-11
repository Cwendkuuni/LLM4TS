package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_Constructor() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        List arguments = new ArrayList();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals(rootOption, writeableCommandLine.getOption(rootOption.getPreferredName()));
        assertEquals(Collections.emptyList(), writeableCommandLine.getOptions());
        assertEquals(Collections.emptySet(), writeableCommandLine.getOptionTriggers());
        assertNull(writeableCommandLine.getValues(null, null));
        assertNull(writeableCommandLine.getSwitch(null, null));
        assertNull(writeableCommandLine.getProperty(null));
        assertNull(writeableCommandLine.getProperty(null, null));
        assertNull(writeableCommandLine.getProperties(null));
        assertNull(writeableCommandLine.getProperties());
        assertFalse(writeableCommandLine.looksLikeOption("root"));
        assertEquals("", writeableCommandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_AddOption() {
        Option option1 = new Option("option1", "option1", false, "Option 1");
        Option option2 = new Option("option2", "option2", false, "Option 2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        assertEquals(2, writeableCommandLine.getOptions().size());
        assertEquals(option1, writeableCommandLine.getOption(option1.getPreferredName()));
        assertEquals(option2, writeableCommandLine.getOption(option2.getPreferredName()));
        assertEquals(2, writeableCommandLine.getOptionTriggers().size());
        assertEquals(Collections.unmodifiableSet(Set.of(option1.getPreferredName(), option2.getPreferredName())), writeableCommandLine.getOptionTriggers());
    }

    @Test
    public void testWriteableCommandLineImpl_AddValue() {
        Option option = new Option("option", "option", false, "Option");
        Argument argument = new Argument("argument", "Argument");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, argument);

        List values = writeableCommandLine.getValues(option, null);

        assertEquals(1, values.size());
        assertEquals(argument, values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_AddSwitch() {
        Option option = new Option("option", "option", false, "Option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_HasOption() {
        Option option = new Option("option", "option", false, "Option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOption() {
        Option option = new Option("option", "option", false, "Option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        Option retrievedOption = writeableCommandLine.getOption(option.getPreferredName());

        assertSame(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_GetValues() {
        Option option = new Option("option", "option", false, "Option");
        Argument argument1 = new Argument("argument1", "Argument 1");
        Argument argument2 = new Argument("argument2", "Argument 2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, argument1);
        writeableCommandLine.addValue(option, argument2);

        List values = writeableCommandLine.getValues(option, null);

        assertEquals(2, values.size());
        assertEquals(argument1, values.get(0));
        assertEquals(argument2, values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetUndefaultedValues() {
        Option option = new Option("option", "option", false, "Option");
        Argument argument1 = new Argument("argument1", "Argument 1");
        Argument argument2 = new Argument("argument2", "Argument 2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, argument1);

        List unfaultedValues = writeableCommandLine.getUndefaultedValues(option);

        assertEquals(1, unfaultedValues.size());
        assertEquals(argument1, unfaultedValues.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_GetSwitch() {
        Option option = new Option("option", "option", false, "Option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new PropertyOption("property", "Property", "defaultValue");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        writeableCommandLine.addProperty(option, "propertyValue");

        String propertyValue = writeableCommandLine.getProperty(option, "defaultValue");

        assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new PropertyOption("property1", "Property 1", "defaultValue1");
        Option option2 = new PropertyOption("property2", "Property 2", "defaultValue2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option2);

        writeableCommandLine.addProperty(option, "property1Value1");
        writeableCommandLine.addProperty(option, "property1Value2");
        writeableCommandLine.addProperty(option2, "property2Value1");

        Map properties = writeableCommandLine.getProperties(option);

        assertEquals(2, properties.size());
        assertEquals("property1Value1", properties.get("property1"));
        assertEquals("property1Value2", properties.get("property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        assertTrue(writeableCommandLine.looksLikeOption("--option"));
        assertTrue(writeableCommandLine.looksLikeOption("-o"));
        assertFalse(writeableCommandLine.looksLikeOption("--unrelated"));
        assertFalse(writeableCommandLine.looksLikeOption("-u"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option1 = new Option("option1", "option1", false, "Option 1");
        Option option2 = new Option("option2", "option2", false, "Option 2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List arguments = new ArrayList();
        arguments.add("argument1");
        arguments.add("argument2");

        writeableCommandLine = new WriteableCommandLineImpl(option1, arguments);

        String commandLineString = writeableCommandLine.toString();

        String expectedCommandLineString = String.join(" ", option1.getLongOpt(), "option1", "argument1", "argument2");
        assertEquals(expectedCommandLineString, commandLineString);
    }
}