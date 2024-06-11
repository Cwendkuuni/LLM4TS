package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        // add assertions here
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // add assertions here
    }

    @Test
    public void testHasArgWithArg() {
        OptionBuilder.hasArg(true);
        // add assertions here
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        // add assertions here
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // add assertions here
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // add assertions here
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // add assertions here
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        // add assertions here
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // add assertions here
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(2);
        // add assertions here
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // add assertions here
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // add assertions here
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(2);
        // add assertions here
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Object.class);
        // add assertions here
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        // add assertions here
    }

    @Test
    public void testCreateChar() {
        OptionBuilder.create('t');
        // add assertions here
    }

    @Test
    public void testCreate() {
        OptionBuilder.create();
        // add assertions here
    }

    @Test
    public void testCreateString() {
        OptionBuilder.create("test");
        // add assertions here
    }
}