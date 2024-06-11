import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine cmd;

 @Test
 public void testAddOption() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").build();
 cmd.addOption(option);
 assertTrue(cmd.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
 cmd.addOption(option);
 cmd.addValue(option, "test");
 assertEquals("test", cmd.getOptionValues(option).get(0));
 }

 @Test
 public void testGetOptionValues() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").build();
 cmd.addOption(option);
 cmd.addValue(option, "test1");
 cmd.addValue(option, "test2");
 List values = cmd.getOptionValues(option);
 assertEquals(2, values.size());
 assertEquals("test1", values.get(0));
 assertEquals("test2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").build();
 List defaults = List.of("default1", "default2");
 cmd.setDefaultValues(option, defaults);
 List values = cmd.getOptionValues(option);
 assertEquals(2, values.size());
 assertEquals("default1", values.get(0));
 assertEquals("default2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").build();
 cmd.addSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").build();
 cmd.addSwitch(option, true);
 cmd.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").build();
 cmd.setDefaultSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test
 public void testAddProperty() {
 cmd = new Cli2WriterMock();
 cmd.addProperty("property", "value");
 assertEquals("value", cmd.getProperties().get("property"));
 }

 @Test
 public void testLooksLikeOptionTrue() {
 cmd = new Cli2WriterMock();
 assertTrue(cmd.looksLikeOption("--option"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 cmd = new Cli2WriterMock();
 assertFalse(cmd.looksLikeOption("arg"));
 }

 @Test
 public void testAddOptionNull() {
 cmd = new Cli2WriterMock();
 Option option = null;
 try {
 cmd.addOption(option);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddValueNullOption() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").build();
 Object value = null;
 try {
 cmd.addValue(option, value);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testAddValueNullValue() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").build();
 try {
 cmd.addValue(option, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetDefaultValuesNullOption() {
 cmd = new Cli2WriterMock();
 Option option = null;
 List defaults = List.of("default1", "default2");
 try {
 cmd.setDefaultValues(option, defaults);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetDefaultValuesNullDefaults() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").build();
 try {
 cmd.setDefaultValues(option, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddSwitchNullValue() {
 cmd = new Cli2WriterMock();
 Option option = Option.builder("o").longOpt("option").build();
 try {
 cmd.addSwitch(option, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetDefaultSwitchNullOption() {
 cmd = new Cli2WriterMock();
 Option option = null;
 Boolean defaultSwitch = true;
 try {
 cmd.setDefaultSwitch(option, defaultSwitch);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddPropertyNullValue() {
 cmd = new Cli2WriterMock();
 try {
 cmd.addProperty(null, "value");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}

class Cli2WriterMock implements WriteableCommandLine {
 @Override
 public void addOption(Option option) {
 // empty implementation for testing
 }

 @Override
 public void addValue(Option option, Object value) {
 // empty implementation for testing
 }

 @Override
 public List<Object> getOptionValues(Option option) {
 return List.of();
 }

 @Override
 public void setDefaultValues(Option option, List<Object> defaultValues) {
 // empty implementation for testing
 }

 @Override
 public void addSwitch(Option option, boolean value) throws IllegalStateException {
 // empty implementation for testing
 }

 @Override
 public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
 // empty implementation for testing
 }

 @Override
 public void addProperty(String property, String value) {
 // empty implementation for testing
 }

 @Override
 public boolean looksLikeOption(String argument) {
 return false;
 }
}