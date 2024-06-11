import static org.junit.Assert.*;
import org.junit.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;

public class PatternBuilderTest {

  private PatternBuilder patternBuilder;

  @Before
  public void setUp() {
    patternBuilder = new PatternBuilder();
  }

  @Test
  public void testCreate_SingleOption() {
    patternBuilder.withPattern("a");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(1, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(false, option.getOptions().get(0).isRequired());
  }

  @Test
  public void testCreate_MultipleOptions() {
    patternBuilder.withPattern("ab");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(2, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(false, option.getOptions().get(0).isRequired());
    assertEquals("b", option.getOptions().get(1).getShortName());
    assertEquals(false, option.getOptions().get(1).isRequired());
  }

  @Test
  public void testCreate_RequiredOption() {
    patternBuilder.withPattern("!a");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(1, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(true, option.getOptions().get(0).isRequired());
  }

  @Test
  public void testCreate_SingleArgument() {
    patternBuilder.withPattern("a@");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(1, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(false, option.getOptions().get(0).isRequired());
    assertNotNull(option.getOptions().get(0).getArgument());
    assertEquals(1, option.getOptions().get(0).getArgument().getMinimum());
    assertEquals(1, option.getOptions().get(0).getArgument().getMaximum());
  }

  @Test
  public void testCreate_MultipleArguments() {
    patternBuilder.withPattern("a@:");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(1, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(false, option.getOptions().get(0).isRequired());
    assertNotNull(option.getOptions().get(0).getArgument());
    assertEquals(1, option.getOptions().get(0).getArgument().getMinimum());
    assertEquals(Integer.MAX_VALUE, option.getOptions().get(0).getArgument().getMaximum());
  }

  @Test
  public void testCreate_RequiredArgument() {
    patternBuilder.withPattern("!a@:+");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(1, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(true, option.getOptions().get(0).isRequired());
    assertNotNull(option.getOptions().get(0).getArgument());
    assertEquals(1, option.getOptions().get(0).getArgument().getMinimum());
    assertEquals(Integer.MAX_VALUE, option.getOptions().get(0).getArgument().getMaximum());
  }

  @Test
  public void testCreate_ArgumentWithClassValidator() {
    patternBuilder.withPattern("a@:");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(1, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(false, option.getOptions().get(0).isRequired());
    assertNotNull(option.getOptions().get(0).getArgument());
    assertEquals(ClassValidator.class, option.getOptions().get(0).getArgument().getValidator().getClass());
  }

  @Test
  public void testCreate_ArgumentWithNumberValidator() {
    patternBuilder.withPattern("a%");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(1, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(false, option.getOptions().get(0).isRequired());
    assertNotNull(option.getOptions().get(0).getArgument());
    assertEquals(NumberValidator.class, option.getOptions().get(0).getArgument().getValidator().getClass());
  }

  @Test
  public void testCreate_ArgumentWithDateValidator() {
    patternBuilder.withPattern("a#");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(1, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(false, option.getOptions().get(0).isRequired());
    assertNotNull(option.getOptions().get(0).getArgument());
    assertEquals(DateValidator.class, option.getOptions().get(0).getArgument().getValidator().getClass());
  }

  @Test
  public void testCreate_ArgumentWithFileValidator() {
    patternBuilder.withPattern("a<");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(1, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(false, option.getOptions().get(0).isRequired());
    assertNotNull(option.getOptions().get(0).getArgument());
    assertEquals(FileValidator.class, option.getOptions().get(0).getArgument().getValidator().getClass());
    assertEquals(true, ((FileValidator)option.getOptions().get(0).getArgument().getValidator()).isExisting());
    assertEquals(true, ((FileValidator)option.getOptions().get(0).getArgument().getValidator()).isFile());
  }

  @Test
  public void testCreate_ArgumentWithMultipleValidators() {
    patternBuilder.withPattern("a@/%");
    Option option = patternBuilder.create();

    assertNotNull(option);
    assertEquals(1, option.getOptions().size());
    assertEquals("a", option.getOptions().get(0).getShortName());
    assertEquals(false, option.getOptions().get(0).isRequired());
    assertNotNull(option.getOptions().get(0).getArgument());
    assertTrue(option.getOptions().get(0).getArgument().getValidator() instanceof Validator[]);
    Validator[] validators = (Validator[])option.getOptions().get(0).getArgument().getValidator();
    assertEquals(2, validators.length);
    assertEquals(ClassValidator.class, validators[0].getClass());
    assertEquals(UrlValidator.class, validators[1].getClass());
  }

  @Test
  public void testReset() {
    patternBuilder.withPattern("a");
    patternBuilder.reset();

    Option option = patternBuilder.create();
    assertNull(option);
  }

  @Test
  public void testValidator_InvalidCharacter() {
    char invalidChar = '#';
    Validator validator = PatternBuilder.validator(invalidChar);
    assertNull(validator);
  }

  @Test
  public void testValidator_ClassValidator_Instance() {
    char validChar = '@';
    Validator validator = PatternBuilder.validator(validChar);
    assertNotNull(validator);
    assertTrue(validator instanceof ClassValidator);
    assertTrue(((ClassValidator)validator).isInstance());
  }

  @Test
  public void testValidator_ClassValidator() {
    char validChar = '+';
    Validator validator = PatternBuilder.validator(validChar);
    assertNotNull(validator);
    assertTrue(validator instanceof ClassValidator);
    assertFalse(((ClassValidator)validator).isInstance());
  }

  @Test
  public void testValidator_NumberValidator() {
    char validChar = '%';
    Validator validator = PatternBuilder.validator(validChar);
    assertNotNull(validator);
    assertTrue(validator instanceof NumberValidator);
  }

  @Test
  public void testValidator_DateValidator() {
    char validChar = '#';
    Validator validator = PatternBuilder.validator(validChar);
    assertNotNull(validator);
    assertTrue(validator instanceof DateValidator);
  }

  @Test
  public void testValidator_FileValidator_Existing() {
    char validChar = '<';
    Validator validator = PatternBuilder.validator(validChar);
    assertNotNull(validator);
    assertTrue(validator instanceof FileValidator);
    assertTrue(((FileValidator)validator).isExisting());
    assertTrue(((FileValidator)validator).isFile());
  }

  @Test
  public void testValidator_FileValidator() {
    char validChar = '>';
    Validator validator = PatternBuilder.validator(validChar);
    assertNotNull(validator);
    assertTrue(validator instanceof FileValidator);
    assertFalse(((FileValidator)validator).isExisting());
    assertFalse(((FileValidator)validator).isFile());
  }

  @Test
  public void testValidator_UrlValidator() {
    char validChar = '/';
    Validator validator = PatternBuilder.validator(validChar);
    assertNotNull(validator);
    assertTrue(validator instanceof UrlValidator);
  }

}