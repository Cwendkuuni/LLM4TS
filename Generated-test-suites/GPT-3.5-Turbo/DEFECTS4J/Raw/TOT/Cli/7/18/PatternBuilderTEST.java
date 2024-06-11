import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        // test case 1: creating an Option with one option
        patternBuilder.reset();
        patternBuilder.withPattern("a");
        Option option1 = patternBuilder.create();
        assertNotNull(option1);
        assertEquals(1, option1.getOptions().size());
        
        // test case 2: creating an Option with multiple options
        patternBuilder.reset();
        patternBuilder.withPattern("ab");
        Option option2 = patternBuilder.create();
        assertNotNull(option2);
        assertEquals(2, option2.getOptions().size());
        
        // test case 3: creating an Option with required argument
        patternBuilder.reset();
        patternBuilder.withPattern("a!");
        Option option3 = patternBuilder.create();
        assertNotNull(option3);
        assertTrue(option3.isRequired());
        
        // test case 4: creating an Option with optional argument
        patternBuilder.reset();
        patternBuilder.withPattern("a*");
        Option option4 = patternBuilder.create();
        assertNotNull(option4);
        assertFalse(option4.isRequired());
        
        // test case 5: creating an Option with integer argument
        patternBuilder.reset();
        patternBuilder.withPattern("a%");
        Option option5 = patternBuilder.create();
        assertNotNull(option5);
        assertEquals(Validator.NumberValidator.class, option5.getValidator().getClass());
        
        // test case 6: creating an Option with date argument
        patternBuilder.reset();
        patternBuilder.withPattern("a#");
        Option option6 = patternBuilder.create();
        assertNotNull(option6);
        assertEquals(Validator.DateValidator.class, option6.getValidator().getClass());
        
        // test case 7: creating an Option with existing file argument
        patternBuilder.reset();
        patternBuilder.withPattern("a<");
        Option option7 = patternBuilder.create();
        assertNotNull(option7);
        assertTrue(option7.getValidator().validate("myfile.txt"));
        
        // test case 8: creating an Option with file argument
        patternBuilder.reset();
        patternBuilder.withPattern("a>");
        Option option8 = patternBuilder.create();
        assertNotNull(option8);
        assertTrue(option8.getValidator().validate("myfile.txt"));
        
        // test case 9: creating an Option with URL argument
        patternBuilder.reset();
        patternBuilder.withPattern("a/");
        Option option9 = patternBuilder.create();
        assertNotNull(option9);
        assertTrue(option9.getValidator().validate("https://www.example.com"));
    }

}