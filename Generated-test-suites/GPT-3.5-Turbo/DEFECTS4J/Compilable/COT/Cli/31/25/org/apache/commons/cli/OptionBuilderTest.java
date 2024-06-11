package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        // TODO: Add test assertion
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // TODO: Add test assertion
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        // TODO: Add test assertion
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        // TODO: Add test assertion
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // TODO: Add test assertion
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // TODO: Add test assertion
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // TODO: Add test assertion
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        // TODO: Add test assertion
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // TODO: Add test assertion
    }

    @Test
    public void testHasArgsWithInt() {
        OptionBuilder.hasArgs(3);
        // TODO: Add test assertion
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // TODO: Add test assertion
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // TODO: Add test assertion
    }

    @Test
    public void testHasOptionalArgsWithInt() {
        OptionBuilder.hasOptionalArgs(2);
        // TODO: Add test assertion
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        // TODO: Add test assertion
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        // TODO: Add test assertion
    }

    @Test
    public void testCreateChar() {
        OptionBuilder.create('c');
        // TODO: Add test assertion
    }

    @Test
    public void testCreate() {
        OptionBuilder.create();
        // TODO: Add test assertion
    }

    @Test
    public void testCreateString() {
        OptionBuilder.create("opt");
        // TODO: Add test assertion
    }
}