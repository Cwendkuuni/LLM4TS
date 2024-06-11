package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test");
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
        OptionBuilder builder = OptionBuilder.withArgName("testArg");
        assertNotNull(builder);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        assertNotNull(builder);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("Test description");
        assertNotNull(builder);
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("testOption");
        assertNotNull(option);
    }
}