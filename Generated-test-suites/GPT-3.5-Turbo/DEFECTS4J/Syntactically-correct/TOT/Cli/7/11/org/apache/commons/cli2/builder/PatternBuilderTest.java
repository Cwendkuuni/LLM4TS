import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setup() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate_SingleOption_ReturnsOption() {
        // Arrange
        String pattern = "a";
        
        // Act
        patternBuilder.withPattern(pattern);
        Option option = patternBuilder.create();
        
        // Assert
        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertNull(option.getArgument());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCreate_OptionWithArgument_ReturnsOptionWithArgument() {
        // Arrange
        String pattern = "a:";
        
        // Act
        patternBuilder.withPattern(pattern);
        Option option = patternBuilder.create();
        
        // Assert
        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCreate_OptionWithRequiredArgument_ReturnsOptionWithRequiredArgument() {
        // Arrange
        String pattern = "a!";
        
        // Act
        patternBuilder.withPattern(pattern);
        Option option = patternBuilder.create();
        
        // Assert
        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreate_OptionWithMultipleArguments_ReturnsOptionWithMultipleArguments() {
        // Arrange
        String pattern = "a*";
        
        // Act
        patternBuilder.withPattern(pattern);
        Option option = patternBuilder.create();
        
        // Assert
        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals(0, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCreate_GroupOfOptions_ReturnsGroupedOptions() {
        // Arrange
        String pattern = "ab";
        
        // Act
        patternBuilder.withPattern(pattern);
        Option option = patternBuilder.create();
        
        // Assert
        assertNotNull(option);
        assertTrue(option instanceof Group);
        Group group = (Group)option;
        assertEquals(2, group.getOptions().size());
        assertEquals("a", group.getOptions().get(0).getShortName());
        assertEquals("b", group.getOptions().get(1).getShortName());
    }

    @Test
    public void testReset_OptionsCleared() {
        // Arrange
        String pattern1 = "a";
        String pattern2 = "b";
        
        // Act
        patternBuilder.withPattern(pattern1);
        patternBuilder.reset();
        patternBuilder.withPattern(pattern2);
        Option option = patternBuilder.create();
        
        // Assert
        assertNotNull(option);
        assertEquals("b", option.getShortName());
        assertNull(option.getArgument());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_ValidPattern_ParsesPattern() {
        // Arrange
        String pattern = "a:<";
        
        // Act
        patternBuilder.withPattern(pattern);
        
        // Assert
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_InvalidPattern_NoOptionCreated() {
        // Arrange
        String pattern = "!";
        
        // Act
        patternBuilder.withPattern(pattern);
        
        // Assert
        Option option = patternBuilder.create();
        assertNull(option);
    }

}