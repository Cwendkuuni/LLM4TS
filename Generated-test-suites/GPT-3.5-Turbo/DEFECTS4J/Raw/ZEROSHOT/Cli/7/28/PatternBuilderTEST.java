import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
  
  private PatternBuilder patternBuilder;
  
  @Before
  public void setUp() {
    patternBuilder = new PatternBuilder();
  }
  
  @Test
  public void testCreate() {
    // Test when options size is 1
    Option option = new Option();
    patternBuilder.options.add(option);
    Option createdOption = patternBuilder.create();
    
    assertEquals(option, createdOption);
    assertEquals(0, patternBuilder.options.size());
    
    // Test when options size is greater than 1
    Option option1 = new Option();
    Option option2 = new Option();
    patternBuilder.options.add(option1);
    patternBuilder.options.add(option2);
    
    GroupBuilder groupBuilder = new GroupBuilder();
    groupBuilder.reset();
    groupBuilder.withOption(option1);
    groupBuilder.withOption(option2);
    Option createdGroupOption = groupBuilder.create();
    
    Option createdOption2 = patternBuilder.create();
    
    assertEquals(createdGroupOption, createdOption2);
    assertEquals(0, patternBuilder.options.size());
  }
  
  @Test
  public void testReset() {
    Option option = new Option();
    
    patternBuilder.options.add(option);
    patternBuilder.reset();
    
    assertEquals(0, patternBuilder.options.size());
  }
  
  @Test
  public void testWithPattern() {
    // Test with empty pattern
    String pattern = "";
    patternBuilder.withPattern(pattern);
    
    assertEquals(0, patternBuilder.options.size());
    
    // Test with single option pattern
    pattern = "o";
    patternBuilder.withPattern(pattern);
    
    assertEquals(1, patternBuilder.options.size());
    
    Option option = patternBuilder.options.iterator().next();
    DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();
    defaultOptionBuilder.reset();
    defaultOptionBuilder.withArgument(null);
    defaultOptionBuilder.withShortName("o");
    defaultOptionBuilder.withRequired(false);
    Option expectedOption = defaultOptionBuilder.create();
    
    assertEquals(expectedOption, option);
    
    // Test with multiple option pattern
    pattern = "o:f";
    patternBuilder.reset();
    patternBuilder.withPattern(pattern);
    
    assertEquals(2, patternBuilder.options.size());
    
    Option option1 = null;
    Option option2 = null;
    
    for (Option opt : patternBuilder.options) {
      if (opt.getShortName().equals("o")) {
        option1 = opt;
      } else if (opt.getShortName().equals("f")) {
        option2 = opt;
      }
    }
    
    assertNotNull(option1);
    assertNotNull(option2);
    
    DefaultOptionBuilder defaultOptionBuilder1 = new DefaultOptionBuilder();
    defaultOptionBuilder1.reset();
    defaultOptionBuilder1.withArgument(null);
    defaultOptionBuilder1.withShortName("o");
    defaultOptionBuilder1.withRequired(false);
    Option expectedOption1 = defaultOptionBuilder1.create();
    
    DefaultOptionBuilder defaultOptionBuilder2 = new DefaultOptionBuilder();
    defaultOptionBuilder2.reset();
    defaultOptionBuilder2.withArgument(null);
    defaultOptionBuilder2.withShortName("f");
    defaultOptionBuilder2.withRequired(false);
    Option expectedOption2 = defaultOptionBuilder2.create();
    
    assertEquals(expectedOption1, option1);
    assertEquals(expectedOption2, option2);
  }
  
  @Test
  public void testValidator() {
    char c = '@';
    Validator validator = patternBuilder.validator(c);
    
    assertTrue(validator instanceof ClassValidator);
    
    ClassValidator classValidator = (ClassValidator) validator;
    assertTrue(classValidator.isInstance());
    
    c = '+';
    validator = patternBuilder.validator(c);
    
    assertTrue(validator instanceof ClassValidator);
    
    classValidator = (ClassValidator) validator;
    assertFalse(classValidator.isInstance());
    
    c = '%';
    validator = patternBuilder.validator(c);
    
    assertTrue(validator instanceof NumberValidator);
    
    c = '#';
    validator = patternBuilder.validator(c);
    
    assertTrue(validator instanceof DateValidator);
    
    c = '<';
    validator = patternBuilder.validator(c);
    
    assertTrue(validator instanceof FileValidator);
    
    FileValidator fileValidator = (FileValidator) validator;
    assertTrue(fileValidator.isExisting());
    assertTrue(fileValidator.isFile());
    
    c = '>';
    validator = patternBuilder.validator(c);
    
    assertTrue(validator instanceof FileValidator);
    
    c = '*';
    validator = patternBuilder.validator(c);
    
    assertTrue(validator instanceof FileValidator);
    
    c = '/';
    validator = patternBuilder.validator(c);
    
    assertTrue(validator instanceof UrlValidator);
    
    c = ':';
    validator = patternBuilder.validator(c);
    
    assertNull(validator);
    
    c = ' ';
    validator = patternBuilder.validator(c);
    
    assertNull(validator);
  }
}