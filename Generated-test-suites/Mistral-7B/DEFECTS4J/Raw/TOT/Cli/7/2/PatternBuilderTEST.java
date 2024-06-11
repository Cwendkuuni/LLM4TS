import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

 private PatternBuilder builder;

 @Test
 public void testCreateOption() {
 builder = new PatternBuilder();
 builder.withPattern("@com.example.MyClass:arg1=%d:arg2=@<file>.log:arg3=/test.html");
 Option result = builder.create();

 assertEquals("Complies with the specified pattern", "arg1", result.getLongOpt());
 assertEquals("Complies with the specified pattern", "arg2", result.getArgName());
 assertEquals("Complies with the specified pattern", NumberValidator.class, result.getValidator());
 assertEquals("Complies with the specified pattern", "arg1", result.getOpt());
 assertEquals("Complies with the specified pattern", "arg1", result.getFullName());

 Argument arg1 = result.getArgument();
 assertEquals("Complies with the specified pattern", NumberValidator.class, arg1.getValidator());
 assertEquals("Complies with the specified pattern", "arg1", arg1.getName());
 }

 @Test
 public void testCreateMultipleOptions() {
 builder = new PatternBuilder();
 builder.withPattern("@com.example.MyClass:arg1=%d:arg2=@<file>.log:arg3=/test.html:arg4=@<url>");
 Option result = builder.create();

 assertEquals("Complies with the specified pattern", "arg1", result.getLongOpt());
 assertEquals("Complies with the specified pattern", "arg2", result.getArgName(1));
 assertEquals("Complies with the specified pattern", NumberValidator.class, result.getValidator(0));
 assertEquals("Complies with the specified pattern", "arg1", result.getFullName(0));

 Argument arg1 = result.getArgument(0);
 assertEquals("Complies with the specified pattern", NumberValidator.class, arg1.getValidator());
 assertEquals("Complies with the specified pattern", "arg1", arg1.getName());

 assertEquals("Complies with the specified pattern", "arg3", result.getLongOpt(2));
 assertEquals("Complies with the specified pattern", "arg3", result.getArgName(3));
 assertEquals("Complies with the specified pattern", FileValidator.class, result.getValidator(2));
 assertEquals("Complies with the specified pattern", "arg3", result.getFullName(2));

 Argument arg3 = result.getArgument(2);
 assertEquals("Complies with the specified pattern", FileValidator.class, arg3.getValidator());
 assertEquals("Complies with the specified pattern", "arg3", arg3.getName());
 }

 @Test
 public void testCreateMissingPattern() {
 builder = new PatternBuilder();
 builder.create();
 assertNull("Should not create an option without a pattern", builder.create());
 }

 @Test
 public void testCreateWrongPattern() {
 builder = new PatternBuilder();
 builder.withPattern("abc");
 assertNull("Should not create an option with an invalid pattern", builder.create());
 }
}