package org.apache.commons.cli;
import org.junit.*;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOption");
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArg");
        assertNotNull(optionBuilder);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArgsWithNum() {
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
    public void testHasOptionalArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test option description");
        assertNotNull(optionBuilder);
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("testOption", option.getLongOpt());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("t");
        assertNotNull(option);
        assertEquals("t", option.getOpt());
    }
}