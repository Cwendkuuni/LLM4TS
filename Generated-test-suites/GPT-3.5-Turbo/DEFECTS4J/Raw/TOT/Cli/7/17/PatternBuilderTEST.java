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
  public void testCreate_SingleOption_ReturnsOptionInstance() {
    patternBuilder.withPattern("a");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertEquals("a", option.getShortName());
  }
  
  @Test
  public void testCreate_MultipleOptions_ReturnsGroupOptionInstance() {
    patternBuilder.withPattern("ab");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertTrue(option instanceof GroupOption);
    assertEquals(2, ((GroupOption) option).getOptions().size());
  }
  
  @Test
  public void testReset_ClearsOptions() {
    patternBuilder.withPattern("ab");
    patternBuilder.reset();
    
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertTrue(option instanceof DefaultOption);
    assertEquals(0, ((DefaultOption) option).getArgument().getValidators().size());
  }
  
  @Test
  public void testWithPattern_StringWithRequiredOption_CreatesOptionWithRequiredTrue() {
    patternBuilder.withPattern("!a");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testWithPattern_StringWithOptionalOption_CreatesOptionWithRequiredFalse() {
    patternBuilder.withPattern("a");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertFalse(option.isRequired());
  }
  
  @Test
  public void testWithPattern_StringWithSingleOptionalArgument_CreatesOptionWithArgumentWithMinimumAndMaximumOne() {
    patternBuilder.withPattern("a:");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertEquals(1, option.getArgument().getMinimum());
    assertEquals(1, option.getArgument().getMaximum());
  }
  
  @Test
  public void testWithPattern_StringWithMultipleOptionalArguments_CreatesOptionWithArgumentWithMinimumZeroAndMaximumOne() {
    patternBuilder.withPattern("a**");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertEquals(0, option.getArgument().getMinimum());
    assertEquals(1, option.getArgument().getMaximum());
  }
  
  @Test
  public void testWithPattern_StringWithRequiredArgument_CreatesOptionWithArgumentWithMinimumOne() {
    patternBuilder.withPattern("a!");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertEquals(1, option.getArgument().getMinimum());
  }
  
  @Test
  public void testWithPattern_StringWithClassTypeValidator_CreatesOptionWithArgumentWithClassValidator() {
    patternBuilder.withPattern("a@");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertTrue(option.getArgument().getValidators().get(0) instanceof ClassValidator);
  }
  
  @Test
  public void testWithPattern_StringWithInstanceTypeValidator_CreatesOptionWithArgumentWithInstanceValidator() {
    patternBuilder.withPattern("a+");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertTrue(option.getArgument().getValidators().get(0) instanceof ClassValidator);
  }
  
  @Test
  public void testWithPattern_StringWithNumberTypeValidator_CreatesOptionWithArgumentWithNumberValidator() {
    patternBuilder.withPattern("a%");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertTrue(option.getArgument().getValidators().get(0) instanceof NumberValidator);
  }
  
  @Test
  public void testWithPattern_StringWithDateTypeValidator_CreatesOptionWithArgumentWithDateValidator() {
    patternBuilder.withPattern("a#");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertTrue(option.getArgument().getValidators().get(0) instanceof DateValidator);
  }
  
  @Test
  public void testWithPattern_StringWithExistingFileTypeValidator_CreatesOptionWithArgumentWithExistingFileTypeValidator() {
    patternBuilder.withPattern("a<");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertTrue(option.getArgument().getValidators().get(0) instanceof FileValidator);
    assertTrue(((FileValidator) option.getArgument().getValidators().get(0)).isExisting());
    assertTrue(((FileValidator) option.getArgument().getValidators().get(0)).isFile());
  }
  
  @Test
  public void testWithPattern_StringWithFileTypeValidator_CreatesOptionWithArgumentWithFileTypeValidator() {
    patternBuilder.withPattern("a>");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertTrue(option.getArgument().getValidators().get(0) instanceof FileValidator);
    assertFalse(((FileValidator) option.getArgument().getValidators().get(0)).isExisting());
    assertTrue(((FileValidator) option.getArgument().getValidators().get(0)).isFile());
  }
  
  @Test
  public void testWithPattern_StringWithUrlTypeValidator_CreatesOptionWithArgumentWithUrlValidator() {
    patternBuilder.withPattern("a/");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertTrue(option.getArgument().getValidators().get(0) instanceof UrlValidator);
  }
  
  @Test
  public void testWithPattern_StringWithInvalidTypeValidator_CreatesOptionWithArgumentWithoutValidator() {
    patternBuilder.withPattern("a?");
    Option option = patternBuilder.create();
    
    assertNotNull(option);
    assertNotNull(option.getArgument());
    assertTrue(option.getArgument().getValidators().isEmpty());
  }
}