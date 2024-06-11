import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", builder.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        assertEquals(1, builder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder builder = OptionBuilder.hasArg(true);
        assertEquals(1, builder.getNumberOfArgs());
        builder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, builder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", builder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        assertTrue(builder.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', builder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        assertEquals('=', builder.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder builder = OptionBuilder.isRequired(true);
        assertTrue(builder.isRequired());
        builder = OptionBuilder.isRequired(false);
        assertFalse(builder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, builder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder builder = OptionBuilder.hasArgs(3);
        assertEquals(3, builder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertEquals(1, builder.getNumberOfArgs());
        assertTrue(builder.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, builder.getNumberOfArgs());
        assertTrue(builder.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, builder.getNumberOfArgs());
        assertTrue(builder.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder builder = OptionBuilder.withType(String.class);
        assertEquals(String.class, builder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", builder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidChar() {
        OptionBuilder.create('#');
    }

    @Test
    public void testCreate() {
        OptionBuilder.builderWithLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("t");
        assertEquals("t", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidString() {
        OptionBuilder.create("#");
    }
}