package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = new DefaultWriteableCommandLine();
        option = new MockOption();
    }

    @Test
    public void testProcess() throws OptionException {
        List<String> args = Arrays.asList("arg1", "arg2", "arg3").listIterator();

        option.process(commandLine, args);

        assertEquals("arg1", commandLine.getOptionValues("optionName").get(0));
        assertEquals("arg2", commandLine.getOptionValues("optionName2").get(0));
        assertNull(commandLine.getOptionValues("nonExistentOption"));
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);

        assertNull(commandLine.getOptionValues("optionName"));
        assertNull(commandLine.getOptionValues("optionName2"));
        assertEquals("defaultValue", commandLine.getOptionValue("optionName3"));
    }

    @Test
    public void testCanProcess() throws OptionException {
        String argument = "arg";

        assertTrue(option.canProcess(commandLine, argument));

        argument = "--optionName";
        assertTrue(option.canProcess(commandLine, argument));

        argument = "optionName=";
        assertTrue(option.canProcess(commandLine, argument));

        argument = "--optionName value";
        assertTrue(option.canProcess(commandLine, argument));

        argument = "otherOption";
        assertFalse(option.canProcess(commandLine, argument));

        List<String> args = Arrays.asList("arg1", "arg2", "arg3").listIterator();

        assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();

        assertEquals(Collections.singleton("optionName"), triggers);
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();

        assertEquals(Collections.singleton("-"), prefixes);
    }

    @Test
    public void testValidate() throws OptionException {
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testInvalidValidate() throws OptionException {
        option = new InvalidMockOption();
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<Option> comp = Comparator.comparingInt(Option::getId);

        List<HelpLine> helpLines = option.helpLines(1, helpSettings, comp);

        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("--optionName", helpLine.getOptionSpec().getLongOpt());
        assertEquals("OptionName", helpLine.getOptionSpec().getOpt());
        assertEquals("Option description", helpLine.getHelp());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        option.appendUsage(buffer, helpSettings, comp);

        assertEquals("--optionName, -o\n" + "Option description\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("optionName", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Option description", option.getDescription());
    }

    @Test
    public void testgetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option anotherOption = new MockOption();
        Option group = new OptionGroup();
        group.addOption(option);
        group.addOption(anotherOption);

        Option foundOption = option.findOption("optionName");

        assertSame(option, foundOption);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testGetParent() {
        OptionGroup group = new OptionGroup();
        option.setParent(group);

        assertSame(group, option.getParent());
    }

    @Test
    public void testSetParent() {
        OptionGroup group = new OptionGroup();

        option.setParent(group);

        assertSame(group, option.getParent());
    }

}

class MockOption implements Option {

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args)
        throws OptionException {
        // empty implementation
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        commandLine.setOptionValue("optionName3", "defaultValue");
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return true;
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
        return true;
    }

    @Override
    public Set<String> getTriggers() {
        return new HashSet<>(Arrays.asList("optionName"));
    }

    @Override
    public Set<String> getPrefixes() {
        return new HashSet<>(Arrays.asList("-"));
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
        // empty implementation
    }

    @Override
    public List helpLines(int depth, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
        return new ArrayList<>();
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
        // empty implementation
    }

    @Override
    public String getPreferredName() {
        return "optionName";
    }

    @Override
    public String getDescription() {
        return "Option description";
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public Option findOption(String trigger) {
        return this;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public OptionGroup getParent() {
        return null;
    }

    @Override
    public void setParent(OptionGroup parent) {
        // empty implementation
    }
}

class InvalidMockOption implements Option {

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args)
        throws OptionException {
        throw new OptionException("Invalid Mock Option");
    }

    // other methods empty implementation
}