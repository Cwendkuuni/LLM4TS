package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArg_boolean() {
        OptionBuilder optionBuilder1 = OptionBuilder.hasArg(true);
        assertNotNull(optionBuilder1);

        OptionBuilder optionBuilder2 = OptionBuilder.hasArg(false);
        assertNotNull(optionBuilder2);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        assertNotNull(optionBuilder);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithValueSeparator_char() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testIsRequired_boolean() {
        OptionBuilder optionBuilder1 = OptionBuilder.isRequired(true);
        assertNotNull(optionBuilder1);

        OptionBuilder optionBuilder2 = OptionBuilder.isRequired(false);
        assertNotNull(optionBuilder2);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArgs_int() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Object.class);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        assertNotNull(optionBuilder);
    }

    @Test
    public void testCreate_char() {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
        assertEquals('t', option.getOpt());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertNotNull(option.getLongOpt());
    }

    @Test
    public void testCreate_String() {
        Option option = OptionBuilder.create("testOpt");
        assertNotNull(option);
        assertEquals("testOpt", option.getOpt());
    }
}