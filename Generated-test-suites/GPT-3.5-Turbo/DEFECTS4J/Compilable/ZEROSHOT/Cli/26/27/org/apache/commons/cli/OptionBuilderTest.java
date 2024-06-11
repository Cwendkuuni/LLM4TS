package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertEquals(true, option.hasArg());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertEquals(true, option.hasArg());

        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();
        assertEquals(false, option.hasArg());
    }

    // Add test methods for other OptionBuilder methods
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        assertEquals(true, option.isRequired());

        OptionBuilder.isRequired(false);
        option = OptionBuilder.create();
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(5);
        Option option = OptionBuilder.create();
        assertEquals(5, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertEquals(true, option.hasArg());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(5);
        Option option = OptionBuilder.create();
        assertEquals(5, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
    }

}