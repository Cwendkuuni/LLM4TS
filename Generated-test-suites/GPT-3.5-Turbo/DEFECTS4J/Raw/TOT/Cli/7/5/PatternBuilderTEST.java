import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

   private PatternBuilder patternBuilder;

   @Before
   public void setUp() {
      patternBuilder = new PatternBuilder();
   }

   // Test create method when only one option is added
   @Test
   public void testCreateWithSingleOption() {
      patternBuilder.withPattern("a");
      Option option = patternBuilder.create();

      Assert.assertEquals(1, option.getOptions().size());
      Assert.assertEquals("a", option.getOptions().get(0).getShortName());
   }

   // Test create method when multiple options are added
   @Test
   public void testCreateWithMultipleOptions() {
      patternBuilder.withPattern("a!b");
      Option option = patternBuilder.create();

      Assert.assertEquals(2, option.getOptions().size());
      Assert.assertEquals("a", option.getOptions().get(0).getShortName());
      Assert.assertEquals("b", option.getOptions().get(1).getShortName());

      Assert.assertTrue(option.getOptions().get(0).isRequired());
      Assert.assertFalse(option.getOptions().get(1).isRequired());
   }

   // Test create method when option type is defined
   @Test
   public void testCreateWithOptionType() {
      patternBuilder.withPattern("a:*b");
      Option option = patternBuilder.create();

      Assert.assertEquals(2, option.getOptions().size());
      Assert.assertEquals("a", option.getOptions().get(0).getShortName());
      Assert.assertEquals("b", option.getOptions().get(1).getShortName());

      Assert.assertTrue(option.getOptions().get(0).getArgument().hasValidator());
      Assert.assertTrue(option.getOptions().get(1).getArgument().hasValidator());
   }

   // Test create method when option has minimum and maximum values
   @Test
   public void testCreateWithMinimumAndMaximumValues() {
      patternBuilder.withPattern("a:+b");
      Option option = patternBuilder.create();

      Assert.assertEquals(2, option.getOptions().size());
      Assert.assertEquals("a", option.getOptions().get(0).getShortName());
      Assert.assertEquals("b", option.getOptions().get(1).getShortName());

      Assert.assertTrue(option.getOptions().get(0).getArgument().hasMinimum());
      Assert.assertTrue(option.getOptions().get(1).getArgument().hasMaximum());

      Assert.assertEquals(1, option.getOptions().get(0).getArgument().getMinimum());
      Assert.assertEquals(1, option.getOptions().get(1).getArgument().getMaximum());
   }

   // Test reset method
   @Test
   public void testReset() {
      patternBuilder.withPattern("a!b");
      patternBuilder.reset();

      Option option = patternBuilder.create();

      Assert.assertEquals(0, option.getOptions().size());
   }

   // Test withPattern method with different characters
   @Test
   public void testWithPatternWithDifferentCharacters() {
      patternBuilder.withPattern("a:!b%<");
      Option option = patternBuilder.create();

      Assert.assertEquals(3, option.getOptions().size());
      Assert.assertEquals("a", option.getOptions().get(0).getShortName());
      Assert.assertEquals("b", option.getOptions().get(1).getShortName());
      Assert.assertEquals("b", option.getOptions().get(2).getShortName());

      Assert.assertTrue(option.getOptions().get(0).isRequired());
      Assert.assertTrue(option.getOptions().get(1).getArgument().hasValidator());

      Assert.assertTrue(option.getOptions().get(2).getArgument().hasValidator());
      Assert.assertTrue(option.getOptions().get(2).getArgument().isFile());
      Assert.assertTrue(option.getOptions().get(2).getArgument().isExisting());
   }

   // Test validator method
   @Test
   public void testValidator() {
      Assert.assertNull(patternBuilder.validator(':'));

      Validator validator = patternBuilder.validator('%');
      Assert.assertTrue(validator instanceof NumberValidator);

      validator = patternBuilder.validator('<');
      Assert.assertTrue(validator instanceof FileValidator);
      Assert.assertTrue(((FileValidator)validator).isExisting());
      Assert.assertTrue(((FileValidator)validator).isFile());

      validator = patternBuilder.validator('*');
      Assert.assertTrue(validator instanceof FileValidator);

      validator = patternBuilder.validator('/');
      Assert.assertTrue(validator instanceof UrlValidator);
   }
}