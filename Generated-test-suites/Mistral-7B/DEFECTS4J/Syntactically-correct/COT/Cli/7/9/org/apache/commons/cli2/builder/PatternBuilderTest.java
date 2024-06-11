import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Assert;
import org.junit.Test;

public class PatternBuilderTest {

 private PatternBuilder patternBuilder;

 @Before
 public void setUp() {
 patternBuilder = new PatternBuilder();
 }

 /**
 * Test creating a new Option instance
 */
 @Test
 public void testCreateOptionInstance() {
 Option option = patternBuilder.create();
 Assert.assertNotNull(option);
 }

 /**
 * Test creating multiple Options using the same pattern
 */
 @Test
 public void testCreateMultipleOptions() {
 patternBuilder.withPattern("a:b:c:d");
 Option option1 = patternBuilder.create();
 Option option2 = patternBuilder.create();
 Option option3 = patternBuilder.create();
 Option option4 = patternBuilder.create();

 Assert.assertEquals("Option count should be 4", 4, patternBuilder.options.size());
 Assert.assertNotSame("Options should be different instances", option1, option2);
 Assert.assertNotSame("Options should be different instances", option1, option3);
 Assert.assertNotSame("Options should be different instances", option1, option4);
 }

 /**
 * Test creating an Option with required argument
 */
 @Test
 public void testCreateOptionWithRequiredArgument() {
 patternBuilder.withPattern("a:b!");
 Option option = patternBuilder.create();
 Argument argument = (Argument) option.getArgument();
 Assert.assertTrue("Argument should be required", argument.isRequired());
 }

 /**
 * Test creating an Option without required argument
 */
 @Test
 public void testCreateOptionWithoutRequiredArgument() {
 patternBuilder.withPattern("a:b");
 Option option = patternBuilder.create();
 Argument argument = (Argument) option.getArgument();
 Assert.assertFalse("Argument should not be required", argument.isRequired());
 }

 /**
 * Test creating an Option with validator
 */
 @Test
 public void testCreateOptionWithValidator() {
 patternBuilder.withPattern("%d");
 Option option = patternBuilder.create();
 NumberValidator validator = (NumberValidator) option.getValidator();
 Assert.assertNotNull("Validator should not be null", validator);
 }

 /**
 * Test creating an Option with multiple validators
 */
 @Test
 public void testCreateOptionWithMultipleValidators() {
 patternBuilder.withPattern("a:@java.lang.String:#yyyy-MM-dd");
 Option option = patternBuilder.create();
 ClassValidator classValidator = (ClassValidator) option.getValidator();
 DateValidator dateValidator = (DateValidator) option.getValidator(DateValidator.class);
 Assert.assertNotNull("ClassValidator should not be null", classValidator);
 Assert.assertNotNull("DateValidator should not be null", dateValidator);
 }

 /**
 * Test creating an Option with file validator for existing file
 */
 @Test
 public void testCreateOptionWithFileValidatorForExistingFile() {
 patternBuilder.withPattern("<file>");
 Option option = patternBuilder.create();
 FileValidator existingValidator = (FileValidator) option.getValidator();
 File file = new File("test.txt");
 existingValidator.validate(file);
 }

 /**
 * Test creating an Option with file validator for non-existing file
 * @expected: java.lang.IllegalArgumentException
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithFileValidatorForNonExistingFile() {
 patternBuilder.withPattern("<nonExistingFile>");
 Option option = patternBuilder.create();
 FileValidator existingValidator = (FileValidator) option.getValidator();
 File file = new File("nonExistingFile.txt");
 existingValidator.validate(file);
 }

 /**
 * Test creating an Option with url validator
 */
 @Test
 public void testCreateOptionWithUrlValidator() {
 patternBuilder.withPattern("/url");
 Option option = patternBuilder.create();
 UrlValidator urlValidator = (UrlValidator) option.getValidator();
 urlValidator.validate("http://www.example.com");
 }

 /**
 * Test creating an Option with multiple types
 * @expected: java.lang.IllegalArgumentException
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithMultipleTypes() {
 patternBuilder.withPattern("a:@java.lang.String:/file");
 patternBuilder.create();
 }

}