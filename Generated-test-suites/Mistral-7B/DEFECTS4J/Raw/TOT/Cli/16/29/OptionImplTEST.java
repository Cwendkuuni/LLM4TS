import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private OptionImpl option;
 private CommandLineParser parser;
 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 option = new OptionImpl(1, "input", true, "i") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return true;
 }

 @Override
 public String getDescription() {
 return "Input option description";
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 commandLine.setValue(getName(), null);
 }
 };

 parser = new DefaultParser();
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testOptionProcessing() {
 String[] args = {"-i", "testInput"};
 parser.parse(args, commandLine, option.getClass(), null);
 assertTrue(commandLine.hasOption(option.getName()));
 assertEquals("testInput", commandLine.getValue(option.getName()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionProcessingWithoutPrefix() {
 String[] args = {"-t", "testInput"};
 parser.parse(args, commandLine, option.getClass(), null);
 fail("Option should not be processed with incorrect prefix");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionProcessingWithDuplicatePrefix() {
 option = new OptionImpl(1, "input", true, "i i") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return true;
 }
 };

 String[] args = {"-ii", "testInput"};
 parser.parse(args, commandLine, option.getClass(), null);
 fail("Option should not be processed with duplicate prefix");
 }

 @Test
 public void testOptionHelp() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp("test", "Test program", true, "1.0", "Usage:", option.toString(), null);
 assertEquals("Input option description", option.getDescription());
 }
}