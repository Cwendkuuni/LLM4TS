import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp(){
        patternBuilder = new PatternBuilder();
    }

    // Test for createOption() method
    @Test
    public void testCreateOption() {
        patternBuilder.createOption(' ', false, 'a'); // No argument
        patternBuilder.createOption('@', true, 'b'); // ClassValidator with instance check
        patternBuilder.createOption(':', false, 'c'); // String argument
        patternBuilder.createOption('%', false, 'd'); // NumberValidator
        patternBuilder.createOption('#', false, 'e'); // DateValidator
        patternBuilder.createOption('<', true, 'f'); // FileValidator with existing file check
        patternBuilder.createOption('>', false, 'g'); // FileValidator
        patternBuilder.createOption('*', false, 'h'); // FileValidator
        patternBuilder.createOption('/', false, 'i'); // UrlValidator
    }

    // Test for withPattern() method
    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("!@:abc#def<g>h/i");
    }

    // Test for create() method when only one option is added
    @Test
    public void testCreateWithSingleOption() {
        patternBuilder.withPattern("@a");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("@a", option.toString());
    }

    // Test for create() method when multiple options are added
    @Test
    public void testCreateWithMultipleOptions() {
        patternBuilder.withPattern("!abc");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("!a,b,c", option.toString());
    }

    // Test for reset() method
    @Test
    public void testReset() {
        patternBuilder.withPattern("!abc");
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }

}