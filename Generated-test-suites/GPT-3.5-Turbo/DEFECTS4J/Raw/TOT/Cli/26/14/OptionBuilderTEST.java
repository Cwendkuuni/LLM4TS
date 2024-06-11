import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {
    private OptionBuilder optionBuilder;

    @Before
    public void setUp() {
        optionBuilder = new OptionBuilder();
    }

    // Test withLongOpt method

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.getLongOpt());
    }

    // Test hasArg method

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    // Test hasArg(boolean hasArg) method

    @Test
    public void testHasArgWithTrue() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder.hasArg(false);
        assertEquals(-1, OptionBuilder.getNumberOfArgs());
    }

    // Test withArgName method

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        assertEquals("test", OptionBuilder.getArgName());
    }

    // Test isRequired method

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
    }

    // Test withValueSeparator(char sep) method

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValueSeparator());
    }

    // Test withValueSeparator() method

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValueSeparator());
    }

    // Test isRequired(boolean newRequired) method

    @Test
    public void testIsRequiredWithTrue() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testIsRequiredWithFalse() {
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.isRequired());
    }

    // Test hasArgs() method

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(-2, OptionBuilder.getNumberOfArgs());
    }

    // Test hasArgs(int num) method

    @Test
    public void testHasArgsWithValidNum() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }

    // Test hasOptionalArg() method

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.hasOptionalArg());
    }

    // Test hasOptionalArgs() method

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(-2, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.hasOptionalArg());
    }

    // Test hasOptionalArgs(int numArgs) method

    @Test
    public void testHasOptionalArgsWithValidNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.hasOptionalArg());
    }

    // Test withType method

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.getType());
    }

    // Test withDescription method

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        assertEquals("test", OptionBuilder.getDescription());
    }

    // Test create(char opt) method

    @Test
    public void testCreateWithValidOpt() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        OptionBuilder.reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidOpt() {
        OptionBuilder.create('-');
    }

    // Test create() method

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    // Test create(String opt) method

    @Test
    public void testCreateWithValidOptString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
        OptionBuilder.reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidOptString() {
        OptionBuilder.create("-");
    }

    // Test create method for comprehensive test

    @Test
    public void testCreateComprehensive() {
        OptionBuilder
            .withLongOpt("test")
            .hasArg()
            .withArgName("testArg")
            .isRequired()
            .withValueSeparator(':')
            .hasOptionalArg()
            .withType(Integer.class)
            .withDescription("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
        assertEquals(1, option.getArgs());
        assertEquals("testArg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(':', option.getValueSeparator());
        assertTrue(option.hasOptionalArg());
        assertEquals(Integer.class, option.getType());
        assertEquals("test", option.getDescription());
        OptionBuilder.reset();
    }

}