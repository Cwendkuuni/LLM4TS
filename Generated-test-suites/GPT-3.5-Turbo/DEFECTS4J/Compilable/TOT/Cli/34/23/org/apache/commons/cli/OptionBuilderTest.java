package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("testOption");
        assertNotNull(builder);
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        assertNotNull(builder);
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder builder = OptionBuilder.hasArg(true);
        assertNotNull(builder);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("argName");
        assertNotNull(builder);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        assertNotNull(builder);
    }

    @Test
    public void testWithSeparatorChar() {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        assertNotNull(builder);
    }

    @Test
    public void testWithSeparator() {
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        assertNotNull(builder);
    }

    @Test
    public void testIsRequireBoolean() {
        OptionBuilder builder = OptionBuilder.isRequired(true);
        assertNotNull(builder);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertNotNull(builder);
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder builder = OptionBuilder.hasArgs(3);
        assertNotNull(builder);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertNotNull(builder);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        assertNotNull(builder);
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(2);
        assertNotNull(builder);
    }

    @Test
    public void testWithType() {
        OptionBuilder builder = OptionBuilder.withType(String.class);
        assertNotNull(builder);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("option description");
        assertNotNull(builder);
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertNull(option.getOpt());
        assertEquals("testOption", option.getLongOpt());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("b");
        assertNotNull(option);
        assertEquals("b", option.getOpt());
        assertNull(option.getLongOpt());
    }

}