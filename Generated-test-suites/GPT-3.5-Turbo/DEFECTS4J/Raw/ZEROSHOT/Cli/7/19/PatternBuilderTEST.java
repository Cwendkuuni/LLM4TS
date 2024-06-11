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
    Option option = patternBuilder.create();
    assertNotNull(option);
    // Add assertions for option properties
  }

  @Test
  public void testReset() {
    patternBuilder.reset();
    // Add assertions to check if options are cleared
  }

  @Test
  public void testWithPattern() {
    String pattern = "!@/:*";
    patternBuilder.withPattern(pattern);
    // Add assertions to check if options are created as expected
  }

  @Test
  public void testValidator() {
    char c = '@';
    Validator validator = patternBuilder.validator(c);
    assertNotNull(validator);
    // Add assertions to check the correct type of validator is returned for given char
  }
}