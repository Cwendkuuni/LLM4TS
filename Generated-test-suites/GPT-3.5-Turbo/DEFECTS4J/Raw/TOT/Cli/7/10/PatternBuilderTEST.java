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
    // Test when options set is empty
    Option option = patternBuilder.create();
    assertNull(option);

    // Test when options set has only one option
    patternBuilder.withPattern("!o");
    option = patternBuilder.create();
    assertNotNull(option);
    assertEquals("o", option.getShortName());

    // Test when options set has more than one option
    patternBuilder.reset();
    patternBuilder.withPattern("!o!p");
    option = patternBuilder.create();
    assertNotNull(option);
    assertTrue(option instanceof Group);
    Group group = (Group) option;
    assertEquals(2, group.getOptions().size());
    assertTrue(group.getOptions().contains(OptionBuilder.withShortName("o").create()));
    assertTrue(group.getOptions().contains(OptionBuilder.withShortName("p").create()));
  }

  @Test
  public void testReset() {
    patternBuilder.withPattern("!o");
    patternBuilder.reset();
    Option option = patternBuilder.create();
    assertNull(option);
  }

  @Test
  public void testWithPattern() {
    // Test pattern with required option
    patternBuilder.withPattern("!o");
    Option option = patternBuilder.create();
    assertNotNull(option);
    assertTrue(option instanceof OptionBuilder);
    OptionBuilder optionBuilder = (OptionBuilder) option;
    assertEquals("o", optionBuilder.getShortName());
    assertTrue(optionBuilder.isRequired());

    // Test pattern with optional option
    patternBuilder.reset();
    patternBuilder.withPattern("o");
    option = patternBuilder.create();
    assertNotNull(option);
    assertTrue(option instanceof OptionBuilder);
    optionBuilder = (OptionBuilder) option;
    assertEquals("o", optionBuilder.getShortName());
    assertFalse(optionBuilder.isRequired());

    // Test pattern with multiple options
    patternBuilder.reset();
    patternBuilder.withPattern("!o!p");
    option = patternBuilder.create();
    assertNotNull(option);
    assertTrue(option instanceof Group);
    Group group = (Group) option;
    assertEquals(2, group.getOptions().size());
    assertTrue(group.getOptions().contains(OptionBuilder.withShortName("o").create()));
    assertTrue(group.getOptions().contains(OptionBuilder.withShortName("p").create()));

    // Test pattern with argument types
    patternBuilder.reset();
    patternBuilder.withPattern("!o@!p%");
    option = patternBuilder.create();
    assertNotNull(option);
    assertTrue(option instanceof Group);
    group = (Group) option;
    assertEquals(2, group.getOptions().size());
    assertTrue(group.getOptions().contains(OptionBuilder.withShortName("o")
        .withArgument(ArgumentBuilder.withValidator(new ClassValidator()).create()).create()));
    assertTrue(group.getOptions().contains(OptionBuilder.withShortName("p")
        .withArgument(ArgumentBuilder.withValidator(NumberValidator.getNumberInstance())
            .withMinimum(1)
            .withMaximum(1)
            .create()).create()));

    // Test pattern with argument types and required options
    patternBuilder.reset();
    patternBuilder.withPattern("!o!p@");
    option = patternBuilder.create();
    assertNotNull(option);
    assertTrue(option instanceof Group);
    group = (Group) option;
    assertEquals(2, group.getOptions().size());
    assertTrue(group.getOptions().contains(OptionBuilder.withShortName("o").isRequired().create()));
    assertTrue(group.getOptions().contains(OptionBuilder.withShortName("p")
        .withArgument(ArgumentBuilder.withValidator(new ClassValidator()).create()).create()));

    // Test pattern with invalid pattern character
    patternBuilder.reset();
    patternBuilder.withPattern("#o");
    option = patternBuilder.create();
    assertNull(option);
  }


}