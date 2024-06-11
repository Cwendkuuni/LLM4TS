import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testCreateWithDefaultOptions() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.create('optionName');

 assertEquals("optionName", option.getLongOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 assertEquals(null, option.getType());
 assertEquals(false, option.hasOptionalArg());
 }

 @Test
 public void testCreateWithLongOpt() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("longOptName").create('optionName');

 assertEquals("longOptName", option.getLongOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 assertEquals(null, option.getType());
 assertEquals(false, option.hasOptionalArg());
 }

 @Test
 public void testCreateWithDescription() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withDescription("description").create('optionName');

 assertEquals("optionName", option.getLongOpt());
 assertEquals("description", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 assertEquals(null, option.getType());
 assertEquals(false, option.hasOptionalArg());
 }

 @Test
 public void testCreateWithArgName() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withArgName("argName").create('optionName');

 assertEquals("optionName", option.getLongOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 assertEquals(null, option.getType());
 assertEquals(false, option.hasOptionalArg());
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testCreateWithRequiredOption() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.isRequired().create('optionName');

 assertEquals("optionName", option.getLongOpt());
 assertEquals("", option.getDescription());
 assertEquals(true, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 assertEquals(null, option.getType());
 assertEquals(false, option.hasOptionalArg());
 }

 @Test
 public void testCreateWithOptionalArg() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.hasOptionalArg().create('optionName');

 assertEquals("optionName", option.getLongOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 assertEquals(null, option.getType());
 assertEquals(true, option.hasOptionalArg());
 }

 @Test
 public void testCreateWithValueSeparator() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withValueSeparator(',').create('optionName');

 assertEquals("optionName", option.getLongOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 assertEquals(null, option.getType());
 assertEquals(false, option.hasOptionalArg());
 assertEquals(',', option.getValueSeparator());
 }

 @Test
 public void testCreateWithType() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withType(String.class).create('optionName');

 assertEquals("optionName", option.getLongOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 assertEquals(String.class, option.getType());
 assertEquals(false, option.hasOptionalArg());
 }

 @Test
 public void testCreateWithArgs() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.hasArgs(1).create('optionName');

 assertEquals("optionName", option.getLongOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(new String[1], option.getArgs());
 assertEquals(null, option.getType());
 assertEquals(false, option.hasOptionalArg());
 }

 @Test
 public void testCreateWithOptionalArgs() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.hasOptionalArgs(2).create('optionName');

 assertEquals("optionName", option.getLongOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(new String[2], option.getArgs());
 assertEquals(null, option.getType());
 assertEquals(true, option.hasOptionalArg());
 }

}