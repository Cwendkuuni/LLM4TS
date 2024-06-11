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
        // Test if create method returns a non-null Option instance
        assertNotNull(patternBuilder.create());
    }

    @Test
    public void testReset() {
        // Test if reset method clears the options set
        patternBuilder.reset();
        assertTrue(patternBuilder.getOptions().isEmpty());
    }

    @Test
    public void testWithPattern() {
        // Test if withPattern method creates options correctly
        patternBuilder.withPattern("abc");
        assertEquals(1, patternBuilder.getOptions().size());
        assertEquals('a', patternBuilder.getOptions().iterator().next().getShortName());

        patternBuilder.withPattern("!:abc");
        assertEquals(1, patternBuilder.getOptions().size());
        assertTrue(patternBuilder.getOptions().iterator().next().isRequired());

        patternBuilder.withPattern("abc:xyz");
        assertEquals(2, patternBuilder.getOptions().size());
        assertFalse(patternBuilder.getOptions().iterator().next().getArgument().isMultiple());

        patternBuilder.withPattern("abc*:xyz");
        assertEquals(2, patternBuilder.getOptions().size());
        assertTrue(patternBuilder.getOptions().iterator().next().getArgument().isMultiple());
    }

    @Test
    public void testValidator() {
        // Test if validator method returns the correct type of validator
        assertEquals(ClassValidator.class, patternBuilder.validator('@').getClass());
        assertEquals(ClassValidator.class, patternBuilder.validator('+').getClass());
        assertEquals(NumberValidator.class, patternBuilder.validator('%').getClass());
        assertEquals(DateValidator.class, patternBuilder.validator('#').getClass());
        assertEquals(FileValidator.class, patternBuilder.validator('<').getClass());
        assertEquals(FileValidator.class, patternBuilder.validator('>').getClass());
        assertEquals(FileValidator.class, patternBuilder.validator('*').getClass());
        assertEquals(UrlValidator.class, patternBuilder.validator('/').getClass());
        assertNull(patternBuilder.validator(':'));
    }
}