package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        String expectedLongOpt = "optionLong";
        assertEquals(expectedLongOpt, optionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        int expectedNumberOfArgs = 1;
        assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArg_withFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        int expectedNumberOfArgs = Option.UNINITIALIZED;
        assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        String expectedArgName = "argName";
        assertEquals(expectedArgName, optionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        boolean expectedRequired = true;
        assertEquals(expectedRequired, optionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        char expectedValueSeparator = ':';
        assertEquals(expectedValueSeparator, optionBuilder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator_empty() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        char expectedValueSeparator = '=';
        assertEquals(expectedValueSeparator, optionBuilder.getValueSeparator());
    }

    @Test
    public void testIsRequired_withFalse() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        boolean expectedRequired = false;
        assertEquals(expectedRequired, optionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        int expectedNumberOfArgs = Option.UNLIMITED_VALUES;
        assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgs_withNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        int expectedNumberOfArgs = 5;
        assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        boolean expectedOptionalArg = true;
        assertEquals(expectedOptionalArg, optionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        int expectedNumberOfArgs = Option.UNLIMITED_VALUES;
        assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs_withNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        int expectedNumberOfArgs = 3;
        assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(int.class);
        Class expectedType = int.class;
        assertEquals(expectedType, optionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        String expectedDescription = "Option description";
        assertEquals(expectedDescription, optionBuilder.getDescription());
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        String longOpt = "optionLong";
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(longOpt)
                .withDescription("Option description")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .hasArgs(3)
                .withType(int[].class);

        Option option = optionBuilder.create('o');

        String expectedLongOptValue = "optionLong";
        String expectedDescriptionValue = "Option description";
        boolean expectedHasArg = true;
        String expectedArgName = "argName";
        boolean expectedIsRequired = true;
        char expectedValueSeparator = ':';
        int expectedNumberOfArgs = 3;
        Class expectedType = int[].class;

        assertEquals(expectedLongOptValue, option.getLongOpt());
        assertEquals(expectedDescriptionValue, option.getDescription());
        assertEquals(expectedHasArg, option.hasArg());
        assertEquals(expectedArgName, option.getArgName());
        assertEquals(expectedIsRequired, option.isRequired());
        assertEquals(expectedValueSeparator, option.getValueSeparator());
        assertEquals(expectedNumberOfArgs, option.getArgs().length);
        assertEquals(expectedType, option.getType());
    }

    @Test
    public void testCreate_withEmptyDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                .hasArg()
                .withArgName("argName");

        Option option = optionBuilder.create('o');

        assertNull(option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg()
                .withArgName("argName");

        optionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withInvalidLongOptLength() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("")
                .hasArg()
                .withArgName("argName");

        optionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withNullLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null)
                .hasArg()
                .withArgName("argName");

        optionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withUninitializedOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                .hasArg();

        optionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withInvalidOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                .hasArg(true)
                .withArgName("argName");

        optionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withUninitializedNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                .hasArg();

        optionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withNegativeNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                .hasArgs(-1);

        optionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withNullType() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                .hasArg()
                .withType(null);

        optionBuilder.create('o');
    }
}