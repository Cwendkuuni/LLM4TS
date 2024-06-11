import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 // Test Case 1: Basic functionality of OptionBuilder with mandatory argument
 @Test
 public void testMandatoryArgumentOption() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("optionA").hasArg().isRequired().create('A');
 CommandLine cmdLine = new DefaultParser().parse(new String[]{"-A", "value"});
 Assert.assertEquals("value", option.getValue(0));
 }

 // Test Case 2: Basic functionality of OptionBuilder with optional argument
 @Test
 public void testOptionalArgumentOption() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("optionB").hasArg(false).create('B');
 CommandLine cmdLine = new DefaultParser().parse(new String[]{"-B", "value1", "value2"});
 Assert.assertEquals("value1", option.getValue(0));
 Assert.assertNull(option.getValue(1));
 }

 // Test Case 3: Basic functionality of OptionBuilder with no argument
 @Test
 public void testNoArgumentOption() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("optionC").create('C');
 CommandLine cmdLine = new DefaultParser().parse(new String[]{"-C"});
 // No value for this option
 Assert.assertNull(option.getValue(0));
 }

 // Test Case 4: Edge case: mandatory argument with empty value
 @Test(expected = IllegalArgumentException.class)
 public void testMandatoryArgumentEmptyValue() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("optionD").hasArg().isRequired().create('D');
 CommandLine cmdLine = new DefaultParser().parse(new String[]{"-D", ""});
 }

 // Test Case 5: Edge case: optional argument with empty value
 @Test
 public void testOptionalArgumentEmptyValue() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("optionE").hasArg(false).create('E');
 CommandLine cmdLine = new DefaultParser().parse(new String[]{"-E"});
 // No value for this option
 Assert.assertNull(option.getValue(0));
 }

 // Test Case 6: Edge case: mandatory argument with non-existent value
 @Test(expected = IllegalArgumentException.class)
 public void testMandatoryArgumentNonExistentValue() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("optionF").hasArg().isRequired().create('F');
 CommandLine cmdLine = new DefaultParser().parse(new String[]{"-G"}); // G is not the same as F
 }

 // Test Case 7: Edge case: optional argument with non-existent value
 @Test
 public void testOptionalArgumentNonExistentValue() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("optionG").hasArg(false).create('G');
 CommandLine cmdLine = new DefaultParser().parse(new String[]{"-H"}); // H is not the same as G
 // No value for this option
 Assert.assertNull(option.getValue(0));
 }

 // Test Case 8: Test creating multiple Options using OptionBuilder
 @Test
 public void testCreateMultipleOptions() {
 OptionBuilder builder = new OptionBuilder();
 Option optionA = builder.withLongOpt("optionA").hasArg().isRequired().create('A');
 Option optionB = builder.withLongOpt("optionB").hasArg().isRequired().create('B');
 CommandLine cmdLine = new DefaultParser().parse(new String[]{"-A", "valueA", "-B", "valueB"});
 Assert.assertEquals("valueA", optionA.getValue(0));
 Assert.assertEquals("valueB", optionB.getValue(0));
 }

 // Test Case 9: Test creating multiple Options with different types
 @Test
 public void testCreateMultipleOptionsWithDifferentTypes() {
 OptionBuilder builder = new OptionBuilder();
 Option optionA = builder.withLongOpt("optionA").hasArg().isRequired().withType(Integer.TYPE).create('A');
 Option optionB = builder.withLongOpt("optionB").hasArg().isRequired().withType(String.class).create('B');
 CommandLine cmdLine = new DefaultParser().parse(new String[]{"-A", "1", "-B", "valueB"});
 int valueA = (Integer) optionA.getValue(0);
 String valueB = (String) optionB.getValue(0);
 Assert.assertEquals(1, valueA.intValue());
 Assert.assertEquals("valueB", valueB);
 }
}