package org.apache.commons.cli2.test;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.exceptions.IllegalArgumentException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addOption(option);
        Assert.assertEquals(1, cmd.getOptions().size());
    }

    @Test
    public void testAddValue() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        cmd.addOption(option);
        cmd.addValue(option, "test");
        List values = cmd.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addOption(option);
        cmd.addValue(option, "test1");
        cmd.addValue(option, "test2");
        List values = cmd.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test1", values.get(0));
        Assert.assertEquals("test2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.setDefaultSwitch(option, true);
        Option currentOption = cmd.getCurrentOption();
        Assert.assertEquals(option, currentOption);
        Assert.assertTrue(option.getValue().isPresent());
        Assert.assertEquals(Boolean.TRUE, option.getValue().get());
    }

    @Test
    public void testAddProperty() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addProperty(option, "property", "test");
        Option currentOption = cmd.getCurrentOption();
        Assert.assertEquals(option, currentOption);
        Assert.assertEquals("test", option.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Assert.assertTrue(cmd.looksLikeOption("--option"));
        Assert.assertTrue(cmd.looksLikeOption("o"));
        Assert.assertFalse(cmd.looksLikeOption("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueToNullOption() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Option option = null;
        cmd.addValue(option, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSwitchToNullOption() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Option option = null;
        cmd.addSwitch(option, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyToNullOption() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Option option = null;
        cmd.addProperty(option, "property", "test");
    }

    @Test
    public void testAddOptionWithValueSeparator() {
        CommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        cmd.addOption(option);
        cmd.addValue(option, "test1=");
        cmd.addValue(option, "test2=");
        List values = cmd.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test1", values.get(0));
        Assert.assertEquals("test2", values.get(1));
    }

}