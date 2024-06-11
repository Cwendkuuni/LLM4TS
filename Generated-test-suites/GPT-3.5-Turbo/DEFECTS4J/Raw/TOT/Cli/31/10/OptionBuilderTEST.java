import org.junit.*;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder ob = OptionBuilder.withLongOpt("longOpt");
        assertNotNull(ob);
        assertEquals("longOpt", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder ob = OptionBuilder.hasArg();
        assertNotNull(ob);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder ob = OptionBuilder.hasArg(true);
        assertNotNull(ob);
        assertEquals(1, OptionBuilder.getNumberOfArgs());

        ob = OptionBuilder.hasArg(false);
        assertNotNull(ob);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder ob = OptionBuilder.withArgName("argName");
        assertNotNull(ob);
        assertEquals("argName", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder ob = OptionBuilder.isRequired();
        assertNotNull(ob);
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder ob = OptionBuilder.withValueSeparator(':');
        assertNotNull(ob);
        assertEquals(':', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder ob = OptionBuilder.withValueSeparator();
        assertNotNull(ob);
        assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder ob = OptionBuilder.isRequired(true);
        assertNotNull(ob);
        assertTrue(OptionBuilder.isRequired());

        ob = OptionBuilder.isRequired(false);
        assertNotNull(ob);
        assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder ob = OptionBuilder.hasArgs();
        assertNotNull(ob);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder ob = OptionBuilder.hasArgs(2);
        assertNotNull(ob);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder ob = OptionBuilder.hasOptionalArg();
        assertNotNull(ob);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder ob = OptionBuilder.hasOptionalArgs();
        assertNotNull(ob);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder ob = OptionBuilder.hasOptionalArgs(2);
        assertNotNull(ob);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder ob = OptionBuilder.withType(Integer.class);
        assertNotNull(ob);
        assertEquals(Integer.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder ob = OptionBuilder.withDescription("description");
        assertNotNull(ob);
        assertEquals("description", OptionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('c');
        assertNotNull(option);
        assertEquals("c", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithCharException() {
        OptionBuilder.create(' ');
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("longOpt");
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertNull(option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOptException() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("opt");
        assertNotNull(option);
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithStringException() {
        OptionBuilder.create(null);
    }

    @After
    public void tearDown() {
        OptionBuilder.reset();
    }
}