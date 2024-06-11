import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {
  
  @Test
  public void testCreate() {
    PatternBuilder patternBuilder = new PatternBuilder();
    assertNotNull(patternBuilder.create());
  }
  
  @Test
  public void testReset() {
    PatternBuilder patternBuilder = new PatternBuilder();
    patternBuilder.reset();
    // Assert that options set is cleared after reset
    assertEquals(0, patternBuilder.options.size());
  }
  
  @Test
  public void testWithPattern() {
    PatternBuilder patternBuilder = new PatternBuilder();
    patternBuilder.withPattern("!@:abc");
    // Assert that option with pattern is added to the options set
    assertEquals(1, patternBuilder.options.size());
  }
  
  @Test
  public void testValidator() {
    Validator validator = PatternBuilder.validator('@');
    assertNotNull(validator);
  }
}