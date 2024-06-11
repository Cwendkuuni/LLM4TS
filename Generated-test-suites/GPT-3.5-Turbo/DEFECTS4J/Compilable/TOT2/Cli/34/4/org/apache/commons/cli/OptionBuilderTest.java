package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test");
        assertEquals("test", builder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test").hasArg();
        assertEquals(1, builder.create().getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test").hasArg(true);
        assertEquals(1, builder.create().getArgs());
    }

    // Add more test cases for other methods...

}