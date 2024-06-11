import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionImplTest {

 @Test
 public void testOptionWithNoPrefix() {
 Option option = new OptionImpl(1, "output", true, "o") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
 return true;
 }
 };

 Options options = new Options();
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options, new String[]{"-o"});
 Assert.assertTrue("Option should be processed", option.isSelected());
 } catch (ParseException e) {
 e.printStackTrace();
 Assert.fail("Parsing should succeed");
 }
 }

 @Test
 public void testOptionWithPrefix() {
 Option option = new OptionImpl(2, null, true, "longOption") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
 return arguments.hasNext() && arguments.next().equals("--longOption");
 }
 };

 Options options = new Options();
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options, new String[]{"--longOption"});
 Assert.assertTrue("Option should be processed", option.isSelected());
 } catch (ParseException e) {
 e.printStackTrace();
 Assert.fail("Parsing should succeed");
 }
 }

 @Test
 public void testOptionWithRequiredArgument() {
 Option option = new OptionImpl(3, "input", true, null) {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
 return arguments.hasNext();
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 commandLine.setValue(this, "defaultValue");
 }
 };

 Options options = new Options();
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options, new String[]{"-i", "testValue"});
 Assert.assertEquals("Option value should be set", "testValue", option.getValue());
 } catch (ParseException e) {
 e.printStackTrace();
 Assert.fail("Parsing should succeed");
 }
 }

 @Test
 public void testOptionWithInvalidArgument() {
 Option option = new OptionImpl(4, "input", true, null) {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
 return arguments.hasNext();
 }
 };

 Options options = new Options();
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options, new String[]{"-i", " testValue"});
 Assert.fail("Parsing should fail due to invalid argument");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testOptionWithMissingArgument() {
 Option option = new OptionImpl(5, "input", true, null) {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
 return arguments.hasNext();
 }
 };

 Options options = new Options();
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options, new String[]{"-i"});
 Assert.fail("Parsing should fail due to missing argument");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testHelp() {
 Option option = new OptionImpl(6, "help", false, null) {
 @Override
 public String getLongOpt() {
 return "help";
 }
 };

 Options options = new Options();
 options.addOption(option);

 HelpFormatter formatter = new HelpFormatter();

 formatter.printHelp("test", "Test program", options, true);
 }
}