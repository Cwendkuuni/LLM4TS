package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(2);
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(2);
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Object.class);
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testCreateChar() {
        OptionBuilder.create('t');
        assertNotNull(OptionBuilder.create());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("testLongOpt");
        assertNotNull(OptionBuilder.create());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCreateNull() {
        OptionBuilder.create(null);
    }

}