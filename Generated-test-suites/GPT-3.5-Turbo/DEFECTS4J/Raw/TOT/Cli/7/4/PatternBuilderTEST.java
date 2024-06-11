import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

  PatternBuilder patternBuilder;

  @Before
  public void setUp() {
    patternBuilder = new PatternBuilder();
  }

  @Test
  public void testCreate_EmptyPattern_ShouldCreateEmptyOption() {
    patternBuilder.withPattern("");
    Option option = patternBuilder.create();
    assertEquals(option.getOptions().size(), 0);
    assertTrue(option.getArguments().isEmpty());
  }

  @Test
  public void testCreate_SingleOptionWithNoArgument_ShouldCreateOptionWithNoArgument() {
    patternBuilder.withPattern("a");
    Option option = patternBuilder.create();
    assertEquals(option.getOptions().size(), 1);
    assertNotNull(option.getOption("a"));
    assertTrue(option.getArgument("a").getValues().isEmpty());
  }

  @Test
  public void testCreate_SingleOptionWithRequiredArgument_ShouldCreateOptionWithRequiredArgument() {
    patternBuilder.withPattern("a!");
    Option option = patternBuilder.create();
    assertEquals(option.getOptions().size(), 1);
    assertNotNull(option.getOption("a"));
    assertTrue(option.getArgument("a").isRequired());
  }

  @Test
  public void testCreate_SingleOptionWithOptionalArgument_ShouldCreateOptionWithOptionalArgument() {
    patternBuilder.withPattern("a*");
    Option option = patternBuilder.create();
    assertEquals(option.getOptions().size(), 1);
    assertNotNull(option.getOption("a"));
    assertFalse(option.getArgument("a").isRequired());
  }

  @Test
  public void testCreate_MultipleOptionsWithArguments_ShouldCreateSingleOptionGroupWithArguments() {
    patternBuilder.withPattern("a# b:% c@!");
    Option option = patternBuilder.create();
    assertEquals(option.getOptions().size(), 1);
    assertNotNull(option.getOption("a"));
    assertNotNull(option.getOption("b"));
    assertNotNull(option.getOption("c"));
    assertFalse(option.getOptionGroup("a").isRequired());
    assertTrue(option.getOptionGroup("b").isRequired());
    assertTrue(option.getOptionGroup("c").isRequired());
  }

  @Test
  public void testReset_AfterCreatingOption_ShouldResetPatternBuilder() {
    patternBuilder.withPattern("a");
    patternBuilder.reset();
    Option option = patternBuilder.create();
    assertEquals(option.getOptions().size(), 0);
    assertTrue(option.getArguments().isEmpty());
  }

}