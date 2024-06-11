import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;

import static org.junit.Assert.*;

public class OptionBuilderTest {

    private OptionBuilder optionBuilder;

    @Before
    public void setup() {
        optionBuilder = new OptionBuilder();
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilderResult = optionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", optionBuilderResult.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilderResult = optionBuilder.hasArg();
        assertEquals(1, optionBuilderResult.getArgs());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder optionBuilderResult = optionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, optionBuilderResult.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilderResult = optionBuilder.withArgName("testArgName");
        assertEquals("testArgName", optionBuilderResult.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilderResult = optionBuilder.isRequired();
        assertTrue(optionBuilderResult.isRequired());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder optionBuilderResult = optionBuilder.hasOptionalArg();
        assertEquals(1, optionBuilderResult.getArgs());
        assertTrue(optionBuilderResult.hasOptionalArg());
    }

    @Test
    public void testWithOptionalArgs() {
        OptionBuilder optionBuilderResult = optionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilderResult.getArgs());
        assertTrue(optionBuilderResult.hasOptionalArg());
    }

    @Test
    public void testWithOptionalArgsWithNum() {
        OptionBuilder optionBuilderResult = optionBuilder.hasOptionalArgs(5);
        assertEquals(5, optionBuilderResult.getArgs());
        assertTrue(optionBuilderResult.hasOptionalArg());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilderResult = optionBuilder.withDescription("testDescription");
        assertEquals("testDescription", optionBuilderResult.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option optionResult = optionBuilder.create('t');
        assertEquals("t", optionResult.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        optionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        Option optionResult = optionBuilder.create("testOpt");
        assertEquals("testOpt", optionResult.getOpt());
    }

}