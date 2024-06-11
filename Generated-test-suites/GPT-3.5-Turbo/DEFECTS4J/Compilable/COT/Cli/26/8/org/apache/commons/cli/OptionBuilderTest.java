package org.apache.commons.cli;
import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOptValue");
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        OptionBuilder.hasArg(false);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argNameValue");
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        OptionBuilder.isRequired(false);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
    }

    @Test
    public void testHasArgsWithInt() {
        OptionBuilder.hasArgs(1);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
    }

    @Test
    public void testHasOptionalArgsWithInt() {
        OptionBuilder.hasOptionalArgs(1);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Object.class);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("descriptionValue");
    }

    @Test
    public void testCreateChar() {
        OptionBuilder.create('c');
    }

    @Test
    public void testCreate() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateString() {
        OptionBuilder.create("opt");
    }
}