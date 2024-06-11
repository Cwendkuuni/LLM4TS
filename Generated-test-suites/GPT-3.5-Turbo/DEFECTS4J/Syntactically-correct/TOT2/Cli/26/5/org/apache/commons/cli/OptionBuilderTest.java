import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder option = OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", option.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder option = OptionBuilder.hasArg();
        assertEquals(1, option.create().getArgs());
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder option = OptionBuilder.hasArg(true);
        assertEquals(1, option.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder option = OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", option.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder option = OptionBuilder.isRequired();
        assertTrue(option.create().isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder option = OptionBuilder.withValueSeparator(':');
        assertEquals(':', option.create().getValueSeparator());
    }

    @Test
    public void testIsRequireBoolean() {
        OptionBuilder option = OptionBuilder.isRequired(true);
        assertTrue(option.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder option = OptionBuilder.hasArgs();
        assertEquals(Integer.MAX_VALUE, option.create().getArgs());
    }

    @Test
    public void testHasArgsNumber() {
        OptionBuilder option = OptionBuilder.hasArgs(3);
        assertEquals(3, option.create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder option = OptionBuilder.hasOptionalArg();
        assertTrue(option.create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder option = OptionBuilder.hasOptionalArgs();
        assertEquals(Integer.MAX_VALUE, option.create().getArgs());
        assertTrue(option.create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsNumber() {
        OptionBuilder option = OptionBuilder.hasOptionalArgs(4);
        assertEquals(4, option.create().getArgs());
        assertTrue(option.create().hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder option = OptionBuilder.withType(String.class);
        assertEquals(String.class, option.create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder option = OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", option.create().getDescription());
    }

    @Test
    public void testCreateChar() {
        OptionBuilder option = OptionBuilder.create('t');
        assertEquals('t', option.create().getOpt().charAt(0));
    }

    @Test
    public void testCreateString() {
        OptionBuilder option = OptionBuilder.create("testCreateString");
        assertEquals("testCreateString", option.create().getOpt());
    }

}