import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class PatternBuilderTest {
    
    private PatternBuilder patternBuilder;
    
    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }
    
    @Test
    public void testCreateOption() {
        patternBuilder.reset();
        patternBuilder.withPattern("+<:%");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }
    
    @Test
    public void testReset() {
        patternBuilder.withPattern(":%");
        patternBuilder.reset();
        assertEquals(0, patternBuilder.reset().size());
    }
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    
    @Test
    public void testWithInvalidPattern() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid pattern character");
        
        patternBuilder.withPattern("$@");
    }
    
    @Test
    public void testWithMultipleOptions() {
        patternBuilder.withPattern("%@:#");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }
}