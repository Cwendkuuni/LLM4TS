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
 Option option = patternBuilder.create();
 assertNotNull(option);
 }

 @Test
 public void testCreateMultipleOptions() {
 patternBuilder.withPattern("a:@ClassA b:i c:d e:@ClassB f:#yyyy-MM-dd g:<file.txt> h:/url.com i:+MyClass j:");
 Option optionA = ((Option) patternBuilder.options.iterator().next());
 Option optionB = ((Option) patternBuilder.options.iterator().next());
 assertEquals("ClassA", optionA.getLongOpt());
 assertEquals("ClassB", optionB.getLongOpt());
 reset(patternBuilder);
 }

 @Test
 public void testCreateRequiredOption() {
 patternBuilder.withPattern("a:!");
 Option option = ((Option) patternBuilder.options.iterator().next());
 assertTrue(option.isRequired());
 reset(patternBuilder);
 }

 @Test
 public void testCreateOptionalOption() {
 patternBuilder.withPattern("a:");
 Option option = ((Option) patternBuilder.options.iterator().next());
 assertFalse(option.isRequired());
 reset(patternBuilder);
 }

 @Test
 public void testCreateNumberOption() {
 patternBuilder.withPattern("a:%");
 Option option = ((Option) patternBuilder.options.iterator().next());
 assertTrue(option.hasArgs());
 assertEquals(NumberValidator.class, option.getValidator().getClass());
 reset(patternBuilder);
 }

 @Test
 public void testCreateDateOption() {
 patternBuilder.withPattern("a:#");
 Option option = ((Option) patternBuilder.options.iterator().next());
 assertTrue(option.hasArgs());
 assertEquals(DateValidator.class, option.getValidator().getClass());
 reset(patternBuilder);
 }

 @Test
 public void testCreateFileOption() {
 patternBuilder.withPattern("a:<");
 Option option = ((Option) patternBuilder.options.iterator().next());
 assertTrue(option.hasArgs());
 assertEquals(FileValidator.class, option.getValidator().getClass());
 reset(patternBuilder);
 }

 @Test
 public void testCreateUrlOption() {
 patternBuilder.withPattern("a:/");
 Option option = ((Option) patternBuilder.options.iterator().next());
 assertTrue(option.hasArgs());
 assertEquals(UrlValidator.class, option.getValidator().getClass());
 reset(patternBuilder);
 }

 @Test
 public void testCreateClassOption() {
 patternBuilder.withPattern("a:@");
 Option option = ((Option) patternBuilder.options.iterator().next());
 assertTrue(option.hasArgs());
 assertEquals(ClassValidator.class, option.getValidator().getClass());
 reset(patternBuilder);
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateInvalidPattern() {
 patternBuilder.withPattern("a:x");
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateMultipleValueOption() {
 patternBuilder.withPattern("a:*");
 }

 private void reset(PatternBuilder patternBuilder) {
 patternBuilder.reset();
 }

}