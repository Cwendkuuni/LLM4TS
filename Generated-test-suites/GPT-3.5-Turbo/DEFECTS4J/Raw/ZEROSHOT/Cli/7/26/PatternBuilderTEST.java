import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        Option option1 = patternBuilder.create();
        assertNull(option1);
        
        patternBuilder.withPattern("!a");
        Option option2 = patternBuilder.create();
        assertNotNull(option2);
        assertEquals(1, option2.getShortName().length());
        assertTrue(option2.isRequired());
        assertNotNull(option2.getArgument());
        
        patternBuilder.reset();
        Option option3 = patternBuilder.create();
        assertNull(option3);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("a");
        Option option1 = patternBuilder.create();
        assertNotNull(option1);
        
        patternBuilder.reset();
        Option option2 = patternBuilder.create();
        assertNull(option2);
    }
    
    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("a");
        Option option1 = patternBuilder.create();
        assertNotNull(option1);
        assertEquals("a", option1.getShortName());
        assertFalse(option1.isRequired());
        assertNull(option1.getArgument());
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("!a");
        Option option2 = patternBuilder.create();
        assertNotNull(option2);
        assertEquals("a", option2.getShortName());
        assertTrue(option2.isRequired());
        assertNotNull(option2.getArgument());
        
        patternBuilder.reset();
        
        patternBuilder.withPattern(":a");
        Option option3 = patternBuilder.create();
        assertNotNull(option3);
        assertEquals("a", option3.getShortName());
        assertFalse(option3.isRequired());
        assertNotNull(option3.getArgument());
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("%a");
        Option option4 = patternBuilder.create();
        assertNotNull(option4);
        assertEquals("a", option4.getShortName());
        assertFalse(option4.isRequired());
        assertNotNull(option4.getArgument());
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("#a");
        Option option5 = patternBuilder.create();
        assertNotNull(option5);
        assertEquals("a", option5.getShortName());
        assertFalse(option5.isRequired());
        assertNotNull(option5.getArgument());
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("<a");
        Option option6 = patternBuilder.create();
        assertNotNull(option6);
        assertEquals("a", option6.getShortName());
        assertFalse(option6.isRequired());
        assertNotNull(option6.getArgument());
        
        patternBuilder.reset();
        
        patternBuilder.withPattern(">a");
        Option option7 = patternBuilder.create();
        assertNotNull(option7);
        assertEquals("a", option7.getShortName());
        assertFalse(option7.isRequired());
        assertNotNull(option7.getArgument());
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("*a");
        Option option8 = patternBuilder.create();
        assertNotNull(option8);
        assertEquals("a", option8.getShortName());
        assertFalse(option8.isRequired());
        assertNotNull(option8.getArgument());
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("/a");
        Option option9 = patternBuilder.create();
        assertNotNull(option9);
        assertEquals("a", option9.getShortName());
        assertFalse(option9.isRequired());
        assertNotNull(option9.getArgument());
    }

    @Test
    public void testValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        Validator validator1 = patternBuilder.validator('@');
        assertTrue(validator1 instanceof ClassValidator);
        ClassValidator classValidator1 = (ClassValidator) validator1;
        assertTrue(classValidator1.isInstance());
        
        Validator validator2 = patternBuilder.validator('+');
        assertTrue(validator2 instanceof ClassValidator);
        ClassValidator classValidator2 = (ClassValidator) validator2;
        assertFalse(classValidator2.isInstance());
        
        Validator validator3 = patternBuilder.validator(':');
        assertNull(validator3);
        
        Validator validator4 = patternBuilder.validator('%');
        assertTrue(validator4 instanceof NumberValidator);
        
        Validator validator5 = patternBuilder.validator('#');
        assertTrue(validator5 instanceof DateValidator);
        
        Validator validator6 = patternBuilder.validator('<');
        assertTrue(validator6 instanceof FileValidator);
        FileValidator fileValidator1 = (FileValidator) validator6;
        assertTrue(fileValidator1.isExisting());
        assertTrue(fileValidator1.isFile());
        
        Validator validator7 = patternBuilder.validator('>');
        assertTrue(validator7 instanceof FileValidator);
        FileValidator fileValidator2 = (FileValidator) validator7;
        assertFalse(fileValidator2.isExisting());
        assertFalse(fileValidator2.isFile());
        
        Validator validator8 = patternBuilder.validator('*');
        assertTrue(validator8 instanceof FileValidator);
        
        Validator validator9 = patternBuilder.validator('/');
        assertTrue(validator9 instanceof UrlValidator);
    }
}