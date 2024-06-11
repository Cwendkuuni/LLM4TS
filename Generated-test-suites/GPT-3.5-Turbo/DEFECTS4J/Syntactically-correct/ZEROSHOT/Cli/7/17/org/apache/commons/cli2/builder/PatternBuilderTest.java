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
    patternBuilder.withPattern("!@1");
    Option option = patternBuilder.create();
    assertNotNull(option);
  }
  
  @Test
  public void testReset() {
    patternBuilder.withPattern("!@1");
    patternBuilder.reset();
    Option option = patternBuilder.create();
    assertNull(option);
  }
  
  @Test
  public void testWithPattern() {
    patternBuilder.withPattern("!@1");
    Option option = patternBuilder.create();
    assertNotNull(option);
    
    patternBuilder.reset();
    
    patternBuilder.withPattern("!:#1");
    option = patternBuilder.create();
    assertNotNull(option);
    
    patternBuilder.reset();
    
    patternBuilder.withPattern("!%1");
    option = patternBuilder.create();
    assertNotNull(option);
    
    patternBuilder.reset();
    
    patternBuilder.withPattern("!#1");
    option = patternBuilder.create();
    assertNotNull(option);
    
    patternBuilder.reset();
    
    patternBuilder.withPattern("<1");
    option = patternBuilder.create();
    assertNotNull(option);
    
    patternBuilder.reset();
    
    patternBuilder.withPattern(">*1");
    option = patternBuilder.create();
    assertNotNull(option);
    
    patternBuilder.reset();
    
    patternBuilder.withPattern("/*1");
    option = patternBuilder.create();
    assertNotNull(option);
    
    patternBuilder.reset();
    
    patternBuilder.withPattern("!:*1");
    option = patternBuilder.create();
    assertNotNull(option);
  }
  
  @Test
  public void testValidator() {
    Validator validator = patternBuilder.validator('@');
    assertNotNull(validator);
    
    validator = patternBuilder.validator('+');
    assertNotNull(validator);
    
    validator = patternBuilder.validator(':');
    assertNull(validator);
    
    validator = patternBuilder.validator('%');
    assertNotNull(validator);
    
    validator = patternBuilder.validator('#');
    assertNotNull(validator);
    
    validator = patternBuilder.validator('<');
    assertNotNull(validator);
    
    validator = patternBuilder.validator('>');
    assertNotNull(validator);
    
    validator = patternBuilder.validator('*');
    assertNotNull(validator);
    
    validator = patternBuilder.validator('/');
    assertNotNull(validator);
    
    validator = patternBuilder.validator('?');
    assertNull(validator);
  }
}