import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        patternBuilder.reset();
        // Ensure options are cleared
        assertEquals(0, patternBuilder.options.size());
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("!@:>");

        Option option = patternBuilder.create();
        assertNotNull(option);

        // Verify option properties
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        assertTrue(((ClassValidator)option.getArgument().getValidator()).isInstance());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertTrue(((FileValidator)option.getArgument().getValidator()).getExisting());
        assertFalse(((FileValidator)option.getArgument().getValidator()).isFile());
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
    }
}