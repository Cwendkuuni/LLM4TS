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
    public void testCreate_SingleOption() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());
    }

    @Test
    public void testCreate_SingleOptionWithArgument() {
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
    }

    @Test
    public void testCreate_RequiredOption() {
        patternBuilder.withPattern("!a");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());
        assertNull(option.getArgument());
    }

    @Test
    public void testCreate_MultipleOptions() {
        patternBuilder.withPattern("a:b:c");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());

        option = option.getNextOption();
        assertNotNull(option);
        assertEquals("b", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());

        option = option.getNextOption();
        assertNotNull(option);
        assertEquals("c", option.getShortName());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());
    }

    @Test
    public void testCreate_ComplexPattern() {
        patternBuilder.withPattern("!a@:b%c<:/");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        assertTrue(((ClassValidator) option.getArgument().getValidator()).isInstance());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());

        option = option.getNextOption();
        assertNotNull(option);
        assertEquals("b", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());

        option = option.getNextOption();
        assertNotNull(option);
        assertEquals("c", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof DateValidator);
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());

        option = option.getNextOption();
        assertNotNull(option);
        assertEquals("d", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) option.getArgument().getValidator()).isExisting());
        assertTrue(((FileValidator) option.getArgument().getValidator()).isFile());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());

        option = option.getNextOption();
        assertNotNull(option);
        assertEquals("e", option.getShortName());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());

        option = option.getNextOption();
        assertNotNull(option);
        assertEquals("f", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());

        assertNull(option.getNextOption());
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("a:b");
        patternBuilder.reset();

        Option option = patternBuilder.create();

        assertNull(option);
    }

}