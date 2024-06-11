import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder result = OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder result = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder result = OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        result = OptionBuilder.hasArg(false);
        assertEquals(-1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder result = OptionBuilder.withArgName("test");
        assertEquals("test", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder result = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder result = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithDefaultValueSeparator() {
        OptionBuilder result = OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder result = OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.isRequired());
        result = OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder result = OptionBuilder.hasArgs();
        assertEquals(Integer.MAX_VALUE, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder result = OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder result = OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder result = OptionBuilder.hasOptionalArgs();
        assertEquals(Integer.MAX_VALUE, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder result = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder result = OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder result = OptionBuilder.withDescription("test");
        assertEquals("test", OptionBuilder.getDescription());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithChar() {
        OptionBuilder result = OptionBuilder.create('t');
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder result = OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        OptionBuilder.setLongopt("test");
        OptionBuilder result = OptionBuilder.create("t");
        assertEquals("t", result.getOpt());
        assertEquals("test", result.getLongOpt());
    }

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.getDescription());
        assertNull(OptionBuilder.getArgName());
        assertNull(OptionBuilder.getLongopt());
        assertNull(OptionBuilder.getType());
        assertFalse(OptionBuilder.isRequired());
        assertEquals(-1, OptionBuilder.getNumberOfArgs());
        assertFalse(OptionBuilder.isOptionalArg());
        assertEquals((char) 0, OptionBuilder.getValuesep());
    }
}