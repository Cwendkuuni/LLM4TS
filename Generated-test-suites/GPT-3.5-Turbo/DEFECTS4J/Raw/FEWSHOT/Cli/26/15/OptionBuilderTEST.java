import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {
    private Option option;

    @Before
    public void setup() {
        OptionBuilder.reset();
        option = null;
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        option = OptionBuilder.create();
        assertEquals(1, option.getArgs());

        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        option = OptionBuilder.create();
        assertEquals("test", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValuesSeparator() {
        OptionBuilder.withValueSeparator(':');
        option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValuesSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        option = OptionBuilder.create();
        assertTrue(option.isRequired());

        OptionBuilder.isRequired(false);
        option = OptionBuilder.create();
        assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithInt() {
        OptionBuilder.hasArgs(2);
        option = OptionBuilder.create();
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasOptionalArgsWithInt() {
        OptionBuilder.hasOptionalArgs(3);
        option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        option = OptionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        option = OptionBuilder.create();
        assertEquals("test", option.getDescription());
    }

    @Test
    public void testCreateWithCharacter() {
        option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithCharacterInvalid() {
        option = OptionBuilder.create('*');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        option = OptionBuilder.create();
    }
}