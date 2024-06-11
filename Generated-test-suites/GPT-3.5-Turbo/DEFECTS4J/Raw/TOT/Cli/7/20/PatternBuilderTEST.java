import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PatternBuilderTest {

  private PatternBuilder patternBuilder;
  private GroupBuilder gbuilder;
  private DefaultOptionBuilder obuilder;
  private ArgumentBuilder abuilder;

  @Before
  public void setUp() {
    gbuilder = new GroupBuilder();
    obuilder = new DefaultOptionBuilder();
    abuilder = new ArgumentBuilder();
    patternBuilder = new PatternBuilder(gbuilder, obuilder, abuilder);
  }

  @Test
  public void testCreate_SingleOption() {
    patternBuilder.withPattern("a:");
    Option option = patternBuilder.create();
    assertEquals(1, option.getOptions().size());
    Option subOption = (Option)option.getOptions().iterator().next();
    assertEquals("a", subOption.getShortName());
    Argument argument = subOption.getArgument();
    assertNotNull(argument);
    assertEquals(1, argument.getMinimum());
    assertEquals(1, argument.getMaximum());
  }

  @Test
  public void testCreate_MultipleOptions() {
    patternBuilder.withPattern("a:b:c:");
    Option option = patternBuilder.create();
    assertEquals(1, option.getOptions().size());
    Option subOption = (Option)option.getOptions().iterator().next();
    assertEquals("a", subOption.getShortName());
    Argument argument = subOption.getArgument();
    assertNotNull(argument);
    assertEquals(1, argument.getMinimum());
    assertEquals(1, argument.getMaximum());

    patternBuilder.reset();
    patternBuilder.withPattern("b:c:");
    option = patternBuilder.create();
    assertEquals(1, option.getOptions().size());
    subOption = (Option)option.getOptions().iterator().next();
    assertEquals("b", subOption.getShortName());
    argument = subOption.getArgument();
    assertNotNull(argument);
    assertEquals(1, argument.getMinimum());
    assertEquals(1, argument.getMaximum());

    patternBuilder.reset();
    patternBuilder.withPattern("c:");
    option = patternBuilder.create();
    assertEquals(1, option.getOptions().size());
    subOption = (Option)option.getOptions().iterator().next();
    assertEquals("c", subOption.getShortName());
    argument = subOption.getArgument();
    assertNotNull(argument);
    assertEquals(1, argument.getMinimum());
    assertEquals(1, argument.getMaximum());
  }

  @Test
  public void testCreate_MultipleOptionsWithDifferentTypes() {
    patternBuilder.withPattern("a@:b%:c#");
    Option option = patternBuilder.create();
    assertEquals(1, option.getOptions().size());

    Option subOption = (Option)option.getOptions().iterator().next();
    assertEquals("a", subOption.getShortName());
    Argument argument = subOption.getArgument();
    assertNotNull(argument);
    assertEquals(1, argument.getMinimum());
    assertEquals(1, argument.getMaximum());
    assertTrue(argument.getValidator() instanceof ClassValidator);
    assertTrue(((ClassValidator)argument.getValidator()).isInstance());

    patternBuilder.reset();
    patternBuilder.withPattern("b%:c#");
    option = patternBuilder.create();
    assertEquals(1, option.getOptions().size());

    subOption = (Option)option.getOptions().iterator().next();
    assertEquals("b", subOption.getShortName());
    argument = subOption.getArgument();
    assertNotNull(argument);
    assertEquals(1, argument.getMinimum());
    assertEquals(1, argument.getMaximum());
    assertTrue(argument.getValidator() instanceof NumberValidator);

    patternBuilder.reset();
    patternBuilder.withPattern("c#");
    option = patternBuilder.create();
    assertEquals(1, option.getOptions().size());

    subOption = (Option)option.getOptions().iterator().next();
    assertEquals("c", subOption.getShortName());
    argument = subOption.getArgument();
    assertNotNull(argument);
    assertEquals(1, argument.getMinimum());
    assertEquals(1, argument.getMaximum());
    assertTrue(argument.getValidator() instanceof DateValidator);
  }

  @Test
  public void testWithPattern_ValidPattern() {
    patternBuilder.withPattern("a@:b%:c#");
    Option option = patternBuilder.create();
    assertEquals(1, option.getOptions().size());
  }

  @Test
  public void testWithPattern_InvalidPattern() {
    patternBuilder.withPattern("invalidPattern");
    Option option = patternBuilder.create();
    assertNull(option);
  }

  @Test
  public void testReset() {
    patternBuilder.withPattern("a:");
    patternBuilder.reset();
    Option option = patternBuilder.create();
    assertNull(option);
  }

  @Test
  public void testValidator_ClassValidatorWithInstance() {
    Validator validator = patternBuilder.validator('@');
    assertNotNull(validator);
    assertTrue(validator instanceof ClassValidator);
    assertTrue(((ClassValidator)validator).isInstance());
  }

  @Test
  public void testValidator_ClassValidator() {
    Validator validator = patternBuilder.validator('+');
    assertNotNull(validator);
    assertTrue(validator instanceof ClassValidator);
  }

  @Test
  public void testValidator_NumberValidator() {
    Validator validator = patternBuilder.validator('%');
    assertNotNull(validator);
    assertTrue(validator instanceof NumberValidator);
  }

  @Test
  public void testValidator_DateValidator() {
    Validator validator = patternBuilder.validator('#');
    assertNotNull(validator);
    assertTrue(validator instanceof DateValidator);
  }

  @Test
  public void testValidator_FileValidatorWithExisting() {
    Validator validator = patternBuilder.validator('<');
    assertNotNull(validator);
    assertTrue(validator instanceof FileValidator);
    assertTrue(((FileValidator)validator).isExisting());
  }

  @Test
  public void testValidator_FileValidator() {
    Validator validator = patternBuilder.validator('>');
    assertNotNull(validator);
    assertTrue(validator instanceof FileValidator);

    patternBuilder.reset();
    validator = patternBuilder.validator('*');
    assertNotNull(validator);
    assertTrue(validator instanceof FileValidator);
  }

  @Test
  public void testValidator_UrlValidator() {
    Validator validator = patternBuilder.validator('/');
    assertNotNull(validator);
    assertTrue(validator instanceof UrlValidator);
  }

  @Test
  public void testValidator_Null() {
    Validator validator = patternBuilder.validator(' ');
    assertNull(validator);
  }

}