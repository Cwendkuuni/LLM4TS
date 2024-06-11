import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
  
  private PatternBuilder patternBuilder;
  
  @Before
  public void setUp() {
    patternBuilder = new PatternBuilder();
  }
  
  @After
  public void tearDown() {
    patternBuilder = null;
  }
  
  @Test
  public void testCreate() {
    Option option = patternBuilder.create();
    assertNull(option);
  }
  
  @Test
  public void testReset() {
    patternBuilder.reset();
    
    Option option = patternBuilder.create();
    assertNull(option);
  }
  
  @Test
  public void testWithPattern() {
    patternBuilder.withPattern("!a:@b:%c:d#/e*");
    
    Option option = patternBuilder.create();
    assertNotNull(option);
    
    assertEquals("a", option.getShortName());
    
    Argument argument = option.getArgument();
    assertNotNull(argument);
    
    assertTrue(argument.getValidator() instanceof ClassValidator);
    
    option = option.getOptions().get(0);
    assertNotNull(option);
    
    assertEquals("b", option.getShortName());
    
    argument = option.getArgument();
    assertNotNull(argument);
    
    assertTrue(argument.getValidator() instanceof NumberValidator);
    
    option = option.getOptions().get(0);
    assertNotNull(option);
    
    assertEquals("c", option.getShortName());
    
    argument = option.getArgument();
    assertNotNull(argument);
    
    assertNull(argument.getValidator());
    
    option = option.getOptions().get(0);
    assertNotNull(option);
    
    assertEquals("d", option.getShortName());
    
    argument = option.getArgument();
    assertNotNull(argument);
    
    assertTrue(argument.getValidator() instanceof DateValidator);
    
    option = option.getOptions().get(0);
    assertNotNull(option);
    
    assertEquals("e", option.getShortName());
    
    argument = option.getArgument();
    assertNotNull(argument);
    
    assertTrue(argument.getValidator() instanceof FileValidator);
  }
  
  @Test
  public void testWithPatternInvalidOption() {
    patternBuilder.withPattern("@a");
    
    Option option = patternBuilder.create();
    assertNotNull(option);
    
    assertEquals("a", option.getShortName());
    
    Argument argument = option.getArgument();
    assertNotNull(argument);
    
    assertTrue(argument.getValidator() instanceof ClassValidator);
  }
  
  @Test
  public void testWithPatternInvalidValidatorChar() {
    patternBuilder.withPattern("!a:^b");
    
    Option option = patternBuilder.create();
    assertNotNull(option);
    
    assertEquals("a", option.getShortName());
    
    Argument argument = option.getArgument();
    assertNotNull(argument);
    
    assertNull(argument.getValidator());
  }
}