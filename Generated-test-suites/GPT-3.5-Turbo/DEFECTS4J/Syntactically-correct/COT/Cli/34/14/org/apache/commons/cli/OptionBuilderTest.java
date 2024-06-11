import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOpt");
        assertEquals("testOpt", optionBuilder.withLongOpt("testOpt").longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.hasArg.numberOfArgs);
    }

    @Test
    public void testHasArgWithArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, optionBuilder.hasArg.numberOfArgs);
    }

    @Test
    public void testHasArgWithoutArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, optionBuilder.hasArg.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArg");
        assertEquals("testArg", optionBuilder.withArgName("testArg").argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired.required);
    }

    @Test
    public void testWithOptionalValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.withValueSeparator().valuesep);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.withValueSeparator(':').valuesep);
    }

    @Test
    public void testIsRequiredWithRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(optionBuilder.isRequired.required);
    }

    @Test
    public void testIsRequiredWithoutRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(optionBuilder.isRequired.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.hasArgs.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        assertEquals(5, optionBuilder.hasArgs.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, optionBuilder.hasOptionalArg.numberOfArgs);
        assertTrue(optionBuilder.hasOptionalArg.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.hasOptionalArgs.numberOfArgs);
        assertTrue(optionBuilder.hasOptionalArgs.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, optionBuilder.hasOptionalArgs.numberOfArgs);
        assertTrue(optionBuilder.hasOptionalArgs.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, optionBuilder.withType(String.class).type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", optionBuilder.withDescription("testDescription").description);
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testCreateWithoutOpt() {
        try {
            Option option = OptionBuilder.create();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("testOpt");
        assertEquals("testOpt", option.getOpt());
    }

    @Test
    public void testCreateWithDescription() {
        Option option = OptionBuilder.create("testOpt").withDescription("testDescription");
        assertEquals("testOpt", option.getOpt());
        assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testCreateWithLongOpt() {
        Option option = OptionBuilder.create("testOpt").withLongOpt("testLongOpt");
        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testCreateWithRequired() {
        Option option = OptionBuilder.create("testOpt").isRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateWithOptionalArg() {
        Option option = OptionBuilder.create("testOpt").hasOptionalArg();
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testCreateWithNumberOfArgs() {
        Option option = OptionBuilder.create("testOpt").hasArgs(3);
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testCreateWithArgName() {
        Option option = OptionBuilder.create("testOpt").withArgName("testArg");
        assertEquals("testArg", option.getArgName());
    }

    @Test
    public void testCreateWithType() {
        Option option = OptionBuilder.create("testOpt").withType(Integer.class);
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testCreateWithValueSeparator() {
        Option option = OptionBuilder.create("testOpt").withValueSeparator('|');
        assertEquals('|', option.getValueSeparator());
    }
}