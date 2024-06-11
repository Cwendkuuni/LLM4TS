import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
 Option expectedOption = new Option("optionLongName", null, false, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder.reset();
 OptionBuilder.create();
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = new Option("arg", null, true, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testHasArgWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
 Option expectedOption = new Option("argName", null, true, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = new Option("arg", null, true, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option expectedOption = new Option("arg", null, true, null, "argValue", ':');
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option expectedOption = new Option("arg", null, true, null, Option.UNLIMITED_VALUES, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = new Option("arg", null, true, null, Option.UNINITIALIZED, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option expectedOption = new Option("arg", null, true, null, Option.UNINITIALIZED, null);
 Option actualOption = OptionBuilder.create();
 Option TypeCastOption = new Option("arg", null, true, null, Option.UNINITIALIZED, null);
 TypeCastOption.setValueType(Integer.class);
 assertEquals(expectedOption, actualOption);
 assertEquals(TypeCastOption, actualOption);
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
 Option expectedOption = new Option("arg", "Option description", true, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptRequired() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName").isRequired();
 Option expectedOption = new Option("optionLongName", "Option description", true, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName").hasOptionalArg();
 Option expectedOption = new Option("optionLongName", "Option description", true, null, Option.UNINITIALIZED, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName").hasArg();
 Option expectedOption = new Option("optionLongName", "Option description", true, null, 1, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName").hasArgs();
 Option expectedOption = new Option("optionLongName", "Option description", true, null, Option.UNLIMITED_VALUES, null);
 Option actualOption = OptionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Edge cases
 @Test
 public void testCreateWithEmptyLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
 Option actualOption = OptionBuilder.create();
 Option expectedOption = new Option("", "Option description", true, null);
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptStartingWithNumber() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("1optionLongName");
 Option actualOption = OptionBuilder.create();
 Option expectedOption = new Option("1optionLongName", "Option description", true, null);
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptStartingWithDash() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("-optionLongName");
 Option actualOption = OptionBuilder.create();
 Option expectedOption = new Option("-optionLongName", "Option description", true, null);
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptStartingWithTwoDashes() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--optionLongName");
 Option actualOption = OptionBuilder.create();
 Option expectedOption = new Option("--optionLongName", "Option description", true, null);
 assertEquals(expectedOption, actualOption);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithLongOptStartingWithMoreThanTwoDashes() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("---optionLongName");
 OptionBuilder.create();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithInvalidCharAsLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("@optionLongName");
 OptionBuilder.create();
 }
}