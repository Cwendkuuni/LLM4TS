import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

  @Test
  public void testCreateOptionWithClassValidator() {
    // Create PatternBuilder instance
    PatternBuilder patternBuilder = new PatternBuilder();
    
    // Call withPattern method to set pattern
    patternBuilder.withPattern("@!");

    // Create Option using the pattern
    Option option = patternBuilder.create();

    // Assert that option is not null
    assertNotNull(option);

    // Additional test cases for edge cases and error scenarios can be added here
  }

  @Test
  public void testCreateOptionWithNumberValidator() {
    // Create PatternBuilder instance
    PatternBuilder patternBuilder = new PatternBuilder();
    
    // Call withPattern method to set pattern
    patternBuilder.withPattern("%");

    // Create Option using the pattern
    Option option = patternBuilder.create();

    // Assert that option is not null
    assertNotNull(option);

    // Additional test cases for edge cases and error scenarios can be added here
  }

  @Test
  public void testCreateOptionWithDateValidator() {
    // Create PatternBuilder instance
    PatternBuilder patternBuilder = new PatternBuilder();
    
    // Call withPattern method to set pattern
    patternBuilder.withPattern("#");

    // Create Option using the pattern
    Option option = patternBuilder.create();

    // Assert that option is not null
    assertNotNull(option);

    // Additional test cases for edge cases and error scenarios can be added here
  }

  // Additional test cases for other methods in PatternBuilder class can be added here

}