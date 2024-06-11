
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        OptionBuilder.create();

        assertEquals("test", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        OptionBuilder.create();

        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithHasArg() {
        OptionBuilder.hasArg(true);
        OptionBuilder.create();

        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithoutHasArg() {
        OptionBuilder.hasArg(false);
        OptionBuilder.create();

        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        OptionBuilder.create();

        assertEquals("test", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        OptionBuilder.create();

        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.create();

        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithDefaultSeparator() {
        OptionBuilder.withValueSeparator();
        OptionBuilder.create();

        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredWithRequired() {
        OptionBuilder.isRequired(true);
        OptionBuilder.create();

        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testIsRequiredWithoutRequired() {
        OptionBuilder.isRequired(false);
        OptionBuilder.create();

        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        OptionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(2);
        OptionBuilder.create();

        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        OptionBuilder.create();

        assertTrue(OptionBuilder.optionalArg);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        OptionBuilder.create();

        assertTrue(OptionBuilder.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.hasOptionalArgs(2);
        OptionBuilder.create();

        assertTrue(OptionBuilder.optionalArg);
        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder.withType(type);
        OptionBuilder.create();

        assertEquals(type, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        OptionBuilder.create();

        assertEquals("test", OptionBuilder.description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');

        assertNotNull(option);
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("test");

        assertNotNull(option);
        assertEquals("test", option.getOpt());
    }
}
