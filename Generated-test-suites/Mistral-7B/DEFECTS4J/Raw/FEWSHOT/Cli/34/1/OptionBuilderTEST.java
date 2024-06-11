package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.longopt = "longopt";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 1;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasArg_hasArgFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = Option.UNINITIALIZED;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.argName = "argName";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.required = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.valuesep = ',';
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithValueSeparator_noArg() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.valuesep = '=';
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testIsRequired_noArg() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.required = false;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = Option.UNLIMITED_VALUES;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasArgs_numArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 2;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 1;
        expectedOptionBuilder.optionalArg = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = Option.UNLIMITED_VALUES;
        expectedOptionBuilder.optionalArg = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasOptionalArgs_numArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 3;
        expectedOptionBuilder.optionalArg = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.type = String.class;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.description = "description";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .withDescription("description")
                .hasArg()
                .withValueSeparator(',')
                .isRequired()
                .hasArgs(2)
                .withType(Integer.class);

        Option option = optionBuilder.create('o');

        Option expectedOption = new Option("longopt", "description", true, 2, "argName", Integer.class, ',');
        assertEquals(expectedOption, option);
    }

    @Test
    public void testCreate_noDescription() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .hasArg()
                .withValueSeparator(',')
                .isRequired()
                .hasArgs(2)
                .withType(Integer.class);

        Option option = optionBuilder.create('o');

        Option expectedOption = new Option("longopt", null, true, 2, null, Integer.class, ',');
        assertEquals(expectedOption, option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description")
                .hasArg()
                .withValueSeparator(',')
                .isRequired()
                .hasArgs(2)
                .withType(Integer.class);

        Option option = optionBuilder.create(' ');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt_withLongOpt() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .withDescription("description")
                .create(' ');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_requiredArg_noArg() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.isRequired()
                .hasOptionalArg()
                .create(' ');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_requiredArg_optionalArg() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.isRequired()
                .hasOptionalArgs()
                .create(' ');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_wrongValueSeparator() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .hasArg()
                .withValueSeparator('a')
                .create('o');
    }
}