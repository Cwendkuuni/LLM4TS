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
    public void testCreate_ValidOption_ReturnsOptionInstance() {
        patternBuilder.withPattern("!@: ");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }
    
    @Test
    public void testReset_EmptyBuilder_OptionSetIsEmpty() {
        patternBuilder.reset();
        Set options = (Set)FieldUtils.readField(patternBuilder, "options", true);
        assertTrue(options.isEmpty());
    }
    
    @Test
    public void testReset_ExistingOptions_OptionSetIsEmpty() {
        patternBuilder.withPattern("!@: ");
        patternBuilder.reset();
        Set options = (Set)FieldUtils.readField(patternBuilder, "options", true);
        assertTrue(options.isEmpty());
    }
    
    @Test
    public void testWithPattern_ValidPattern_OptionsCreated() {
        patternBuilder.withPattern("!@:#/<>");
        Set options = (Set)FieldUtils.readField(patternBuilder, "options", true);
        assertEquals(5, options.size());
    }
    
    @Test
    public void testValidator_ClassType_ReturnsClassValidator() {
        Validator validator = patternBuilder.validator('@');
        assertTrue(validator instanceof ClassValidator);
    }
    
    @Test
    public void testValidator_NumberType_ReturnsNumberValidator() {
        Validator validator = patternBuilder.validator('%');
        assertTrue(validator instanceof NumberValidator);
    }
    
    @Test
    public void testValidator_InvalidType_ReturnsNull() {
        Validator validator = patternBuilder.validator('$');
        assertNull(validator);
    }

}