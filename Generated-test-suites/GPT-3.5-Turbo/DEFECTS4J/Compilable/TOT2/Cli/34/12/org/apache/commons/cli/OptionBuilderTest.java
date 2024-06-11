package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void withLongOptTest() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void hasArgTest() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void hasArgsTest() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void createWithLongOptTest() {
        OptionBuilder.withLongOpt("longOptTest");
        assertEquals("longOptTest", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void createMissingLongOptTest() {
        try {
            OptionBuilder.create();
            fail("Expected IllegalArgumentException for missing longopt");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void createWithDescriptionTest() {
        OptionBuilder.withDescription("descriptionTest");
        assertEquals("descriptionTest", OptionBuilder.create().getDescription());
    }

}