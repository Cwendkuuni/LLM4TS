import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt");
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder builder = OptionBuilder.hasArg(true);
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("argName");
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder builder = OptionBuilder.isRequired(true);
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder builder = OptionBuilder.hasArgs(2);
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testWithType() {
        OptionBuilder builder = OptionBuilder.withType(Integer.class);
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("description");
        assertTrue(builder == OptionBuilder.getInstance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithCharThrowsException() {
        OptionBuilder.create('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateThrowsException() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
}