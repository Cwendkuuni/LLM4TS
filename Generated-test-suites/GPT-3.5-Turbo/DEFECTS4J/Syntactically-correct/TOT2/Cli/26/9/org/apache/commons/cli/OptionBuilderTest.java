import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    private OptionBuilder instance;

    @Before
    public void setUp() {
        instance = new OptionBuilder();
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder result = OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", result.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder result = OptionBuilder.hasArg();
        assertEquals(1, result.getNumberOfArgs());
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder result = OptionBuilder.hasArg(true);
        assertEquals(1, result.getNumberOfArgs());

        result = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, result.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder result = OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", result.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder result = OptionBuilder.isRequired();
        assertTrue(result.isRequired());
    }

    // Include tests for other methods here

}