package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        // Assert here
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Assert here
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        // Assert here
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        // Assert here
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Assert here
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // Assert here
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // Assert here
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        // Assert here
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Assert here
    }

    @Test
    public void testHasArgsWithInt() {
        OptionBuilder.hasArgs(3);
        // Assert here
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Assert here
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Assert here
    }

    @Test
    public void testHasOptionalArgsWithInt() {
        OptionBuilder.hasOptionalArgs(2);
        // Assert here
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Object.class);
        // Assert here
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        // Assert here
    }

    @Test
    public void testCreateChar() {
        OptionBuilder.create('o');
        // Assert here
    }

    @Test
    public void testCreate() {
        OptionBuilder.create();
        // Assert here
    }

    @Test
    public void testCreateString() {
        OptionBuilder.create("opt");
        // Assert here
    }

}