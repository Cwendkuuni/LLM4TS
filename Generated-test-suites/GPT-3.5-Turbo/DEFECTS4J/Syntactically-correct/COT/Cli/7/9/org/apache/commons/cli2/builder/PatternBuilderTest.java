import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option1 = patternBuilder.obuilder.create();
        assertNotNull(option1);
        
        patternBuilder.gbuilder.reset();
        option1 = patternBuilder.gbuilder.create();
        assertNotNull(option1);
        
        patternBuilder.abuilder.reset();
        Argument argument1 = patternBuilder.abuilder.create();
        assertNotNull(argument1);
        
        Option option2 = patternBuilder.create();
        assertNotNull(option2);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        // verify that reset() does not throw any exception
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("abc");
        Option option1 = patternBuilder.obuilder.create();
        assertNotNull(option1);

        patternBuilder.reset();

        patternBuilder.withPattern("!@:");
        Option option2 = patternBuilder.obuilder.create();
        assertNotNull(option2);

        patternBuilder.reset();

        patternBuilder.withPattern("@+#");
        Option option3 = patternBuilder.obuilder.create();
        assertNotNull(option3);

        patternBuilder.reset();

        patternBuilder.withPattern("<>*");
        Option option4 = patternBuilder.obuilder.create();
        assertNotNull(option4);

        patternBuilder.reset();

        patternBuilder.withPattern("/#");
        Option option5 = patternBuilder.obuilder.create();
        assertNotNull(option5);
    }

    @Test
    public void testValidator() {
        char c1 = '@';
        Validator validator1 = PatternBuilder.validator(c1);
        assertNotNull(validator1);

        char c2 = '+';
        Validator validator2 = PatternBuilder.validator(c2);
        assertNotNull(validator2);

        char c3 = ':';
        Validator validator3 = PatternBuilder.validator(c3);
        assertNull(validator3);

        char c4 = '%';
        Validator validator4 = PatternBuilder.validator(c4);
        assertNotNull(validator4);

        char c5 = '#';
        Validator validator5 = PatternBuilder.validator(c5);
        assertNotNull(validator5);

        char c6 = '<';
        Validator validator6 = PatternBuilder.validator(c6);
        assertNotNull(validator6);

        char c7 = '>';
        Validator validator7 = PatternBuilder.validator(c7);
        assertNotNull(validator7);

        char c8 = '*';
        Validator validator8 = PatternBuilder.validator(c8);
        assertNotNull(validator8);

        char c9 = '/';
        Validator validator9 = PatternBuilder.validator(c9);
        assertNotNull(validator9);

        char c10 = ' ';
        Validator validator10 = PatternBuilder.validator(c10);
        assertNull(validator10);
    }
}