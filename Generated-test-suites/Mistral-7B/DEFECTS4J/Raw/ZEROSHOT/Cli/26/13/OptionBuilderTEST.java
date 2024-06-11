import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testCreateWithDefaults() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.create('X');
 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isValueSeparatorPresent());
 assertFalse(option.isUnlimitedValues());
 assertNull(option.getType());
 assertNull(option.getArgName());
 }

 @Test
 public void testCreateWithLongOpt() {
 OptionBuilder builder = new OptionBuilder().withLongOpt("optionA");
 Option option = builder.create('A');
 assertEquals("optionA", option.getLongOpt());
 }

 @Test
 public void testCreateWithLongOptAndDescription() {
 OptionBuilder builder = new OptionBuilder().withLongOpt("optionA").withDescription("Option A description");
 Option option = builder.create('A');
 assertEquals("Option A description", option.getDescription());
 }

 @Test
 public void testCreateWithArgName() {
 OptionBuilder builder = new OptionBuilder().withArgName("argName");
 Option option = builder.create('A');
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testCreateWithHasArg() {
 OptionBuilder builder = new OptionBuilder().hasArg();
 Option option = builder.create('A');
 assertTrue(option.hasArg());
 }

 @Test
 public void testCreateWithHasOptionalArg() {
 OptionBuilder builder = new OptionBuilder().hasOptionalArg();
 Option option = builder.create('A');
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testCreateWithValueSeparator() {
 OptionBuilder builder = new OptionBuilder().withValueSeparator(':');
 Option option = builder.create('A');
 assertTrue(option.isValueSeparatorPresent());
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testCreateWithIsRequired() {
 OptionBuilder builder = new OptionBuilder().isRequired();
 Option option = builder.create('A');
 assertTrue(option.isRequired());
 }

 @Test
 public void testCreateWithUnlimitedArgs() {
 OptionBuilder builder = new OptionBuilder().hasArgs();
 Option option = builder.create('A');
 assertTrue(option.isUnlimitedValues());
 }

 @Test
 public void testCreateWithSpecifiedNumberOfArgs() {
 OptionBuilder builder = new OptionBuilder().hasArgs(2);
 Option option = builder.create('A');
 assertEquals(2, option.getArgs().numberOfValues());
 }

 @Test
 public void testCreateWithType() {
 OptionBuilder builder = new OptionBuilder().withType(Integer.class);
 Option option = builder.create('A');
 assertEquals(Integer.class, option.getType());
 }
}