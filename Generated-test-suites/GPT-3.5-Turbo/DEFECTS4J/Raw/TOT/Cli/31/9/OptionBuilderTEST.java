import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    private OptionBuilder instance;

    @Before
    public void setUp() {
        instance = OptionBuilder.withLongOpt("testOption")
                .hasArg()
                .hasArgs(3)
                .isRequired()
                .withArgName("arg")
                .withValueSeparator(',')
                .withType(String.class)
                .withDescription("This is a test option")
                .create('t');
    }

    @Test
    public void testWithLongOpt() {
        assertEquals("testOption", instance.getLongOpt());
    }

    @Test
    public void testHasArg() {
        assertTrue(instance.hasArg());
    }

    @Test
    public void testHasArgs() {
        assertEquals(3, instance.getArgs());
    }

    @Test
    public void testIsRequired() {
        assertTrue(instance.isRequired());
    }

    @Test
    public void testWithArgName() {
        assertEquals("arg", instance.getArgName());
    }

    @Test
    public void testWithValueSeparator() {
        assertEquals(',', instance.getValueSeparator());
    }

    @Test
    public void testWithType() {
        assertEquals(String.class, instance.getType());
    }

    @Test
    public void testWithDescription() {
        assertEquals("This is a test option", instance.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
        assertEquals('t', option.getOpt());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertNull(option.getOpt());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOption");
        assertNotNull(option);
        assertEquals("testOption", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

}