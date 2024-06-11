package org.apache.commons.cli;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("newLongopt");
        // assert statements
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // assert statements
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(true);
        // assert statements
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        // assert statements
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // assert statements
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // assert statements
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // assert statements
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        // assert statements
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // assert statements
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(2);
        // assert statements
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // assert statements
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // assert statements
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(3);
        // assert statements
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Object.class);
        // assert statements
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("newDescription");
        // assert statements
    }

    @Test
    public void testCreateChar() {
        OptionBuilder.create('O');
        // assert statements
    }

    @Test
    public void testCreate() {
        OptionBuilder.create();
        // assert statements
    }

    @Test
    public void testCreateString() {
        OptionBuilder.create("option");
        // assert statements
    }
}