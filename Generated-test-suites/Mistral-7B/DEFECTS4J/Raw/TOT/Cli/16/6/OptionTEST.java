import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 private WriteableCommandLine commandLine = new BasicCommandLine();

 @Test
 public void testProcess() {
 Option option = new Option() {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
 args.next(); // skip argument
 commandLine.setValue("value", "value");
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 commandLine.setValue("defaultValue", "defaultValue");
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, String argument) {
 return argument.equals("argument");
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
 return arguments.hasNext() && arguments.next().equals("argument");
 }

 @Override
 public Set getTriggers() {
 return Set.of("trigger1", "trigger2");
 }

 @Override
 public Set getPrefixes() {
 return Set.of("-p", "--prefix");
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // no validation needed
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return List.of();
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 buffer.append("-p, --prefix\n");
 buffer.append("  Option description\n");
 }

 @Override
 public String getPreferredName() {
 return "option";
 }

 @Override
 public String getDescription() {
 return "Option description";
 }

 @Override
 public int getId() {
 return 1;
 }

 @Override
 public Option findOption(String trigger) {
 return null;
 }

 @Override
 public boolean isRequired() {
 return false;
 }

 @Override
 public Option getParent() {
 return null;
 }

 @Override
 public void setParent(Option parent) {
 // not implemented
 }
 };

 commandLine.parse("--prefix argument value");
 assertTrue(commandLine.hasOption("option"));
 assertEquals("value", commandLine.getValue("value"));
 assertEquals("defaultValue", commandLine.getValue("defaultValue"));
 }

 @Test(expected = OptionException.class)
 public void testProcessException() {
 Option option = new Option() {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
 throw new OptionException("Error message");
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 // not implemented
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, String argument) {
 return false;
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
 return false;
 }

 @Override
 public Set getTriggers() {
 return null;
 }

 @Override
 public Set getPrefixes() {
 return null;
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // not implemented
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return List.of();
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 // not implemented
 }

 @Override
 public String getPreferredName() {
 return null;
 }

 @Override
 public String getDescription() {
 return null;
 }

 @Override
 public int getId() {
 return 0;
 }

 @Override
 public Option findOption(String trigger) {
 return null;
 }

 @Override
 public boolean isRequired() {
 return false;
 }

 @Override
 public Option getParent() {
 return null;
 }

 @Override
 public void setParent(Option parent) {
 // not implemented
 }
 };
 commandLine.parse(List.of("--prefix"));
 fail("OptionException expected");
 }

 @Test
 public void testCanProcess() {
 Option option = new Option() {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
 return true;
 }

 @Override
 public Set getTriggers() {
 return Set.of("trigger1", "trigger2");
 }

 @Override
 public Set getPrefixes() {
 return Set.of("-p", "--prefix");
 }

 @Override
 public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
 args.next(); // skip argument
 commandLine.setValue("value", "value");
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 commandLine.setValue("defaultValue", "defaultValue");
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // no validation needed
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return List.of();
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 buffer.append("-p, --prefix\n");
 buffer.append("  Option description\n");
 }

 @Override
 public String getPreferredName() {
 return "option";
 }

 @Override
 public String getDescription() {
 return "Option description";
 }

 @Override
 public int getId() {
 return 1;
 }

 @Override
 public Option findOption(String trigger) {
 return null;
 }

 @Override
 public boolean isRequired() {
 return false;
 }

 @Override
 public Option getParent() {
 return null;
 }

 @Override
 public void setParent(Option parent) {
 // not implemented
 }
 };
 ListIterator args = List.of("--prefix", "argument", "value").listIterator();
 assertTrue(option.canProcess(commandLine, args));
 commandLine.parse(args);
 assertEquals("value", commandLine.getValue("value"));
 }

 @Test
 public void testCanProcessFailure() {
 Option option = new Option() {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
 return false;
 }

 @Override
 public Set getTriggers() {
 return Set.of("trigger1", "trigger2");
 }

 @Override
 public Set getPrefixes() {
 return Set.of("-p", "--prefix");
 }

 @Override
 public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
 args.next(); // skip argument
 commandLine.setValue("value", "value");
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 commandLine.setValue("defaultValue", "defaultValue");
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // no validation needed
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return List.of();
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 buffer.append("-p, --prefix\n");
 buffer.append("  Option description\n");
 }

 @Override
 public String getPreferredName() {
 return "option";
 }

 @Override
 public String getDescription() {
 return "Option description";
 }

 @Override
 public int getId() {
 return 1;
 }

 @Override
 public Option findOption(String trigger) {
 return null;
 }

 @Override
 public boolean isRequired() {
 return false;
 }

 @Override
 public Option getParent() {
 return null;
 }

 @Override
 public void setParent(Option parent) {
 // not implemented
 }
 };
 ListIterator args = List.of("--prefix", "argument").listIterator();
 assertFalse(option.canProcess(commandLine, args));
 }
}