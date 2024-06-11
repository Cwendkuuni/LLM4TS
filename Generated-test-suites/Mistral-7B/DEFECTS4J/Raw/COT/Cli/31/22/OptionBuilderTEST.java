package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().numberOfArgs());
    }

    @Test
    public void testHasArg_WithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getNumberOfArgs());
    }

    @Test
    public void testHasArgs_WithSpecifiedNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        Assert.assertEquals(2, option.getArgs().getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getNumberOfArgs());
        Assert.assertTrue(option.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create();
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Description");
        Option option = optionBuilder.create();
        Assert.assertEquals("Description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_WhenLongOptIsNull() {
        OptionBuilder optionBuilder = OptionBuilder.create();
        OptionBuilder.reset(); // Reset OptionBuilder to its initial state
        throw new IllegalArgumentException("Expected an IllegalArgumentException when creating an Option without setting longOpt");
    }

    @Test
    public void testCreate_WithLongOptAndArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().numberOfArgs());
    }

    @Test
    public void testCreate_WithLongOptAndArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testCreate_WithLongOptAndArgNameAndDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .withDescription("Description");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals("Description", option.getDescription());
    }

    @Test
    public void testCreate_WithLongOptAndArgAndDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withDescription("Description");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().numberOfArgs());
        Assert.assertEquals("Description", option.getDescription());
    }

    @Test
    public void testCreate_WithLongOptAndArgNameAndDescriptionAndType() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .withDescription("Description")
                .withType(Integer.class);
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testCreate_WithLongOptAndArgAndType() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withType(Integer.class);
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().numberOfArgs());
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testCreate_WithLongOptAndArgAndTypeAndDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withType(Integer.class)
                .withDescription("Description");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().numberOfArgs());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testCreate_WithLongOptAndArgNameAndValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreate_WithLongOptAndArgNameAndDescriptionAndValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .withDescription("Description")
                .withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreate_WithLongOptAndArgNameAndDescriptionAndTypeAndValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .withDescription("Description")
                .withType(Integer.class)
                .withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreate_WithLongOptAndArgAndDescriptionAndValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withDescription("Description")
                .withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().numberOfArgs());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreate_WithLongOptAndArgAndDescriptionAndTypeAndValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withDescription("Description")
                .withType(Integer.class)
                .withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().numberOfArgs());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreate_WithLongOptAndArgNameAndDescriptionAndTypeAndValueSeparatorAndIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .withDescription("Description")
                .withType(Integer.class)
                .withValueSeparator(':')
                .isRequired();
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testCreate_WithLongOptAndArgAndDescriptionAndTypeAndValueSeparatorAndIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withDescription("Description")
                .withType(Integer.class)
                .withValueSeparator(':')
                .isRequired();
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().numberOfArgs());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testCreate_WithLongOptAndArgNameAndDescriptionAndTypeAndValueSeparatorAndIsRequired_WithOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .withDescription("Description")
                .withType(Integer.class)
                .withValueSeparator(':')
                .hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertTrue(option.isOptionalArg());
    }

    @Test
    public void testCreate_WithLongOptAndArgAndDescriptionAndTypeAndValueSeparatorAndIsRequired_WithOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withDescription("Description")
                .withType(Integer.class)
                .withValueSeparator(':')
                .hasOptionalArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getNumberOfArgs());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertTrue(option.isOptionalArg());
    }

    @Test
    public void testCreate_WithLongOptAndArgNameAndDescriptionAndTypeAndValueSeparatorAndIsRequired_WithSpecifiedNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName")
                .withDescription("Description")
                .withType(Integer.class)
                .withValueSeparator(':')
                .hasArgs(2);
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(2, option.getArgs().getNumberOfArgs());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertFalse(option.isOptionalArg());
    }

    @Test
    public void testCreate_WithLongOptAndArgAndDescriptionAndTypeAndValueSeparatorAndIsRequired_WithSpecifiedNumberOfArgs_WithOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withDescription("Description")
                .withType(Integer.class)
                .withValueSeparator(':')
                .hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getNumberOfArgs());
        Assert.assertEquals("Description", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertTrue(option.isOptionalArg());
    }

    
}