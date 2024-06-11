import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 private WriteableCommandLine commandLine = new BasicParseResults();

 @Test
 public void testProcessOption() {
 Option option = new Option() {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
 args.next(); // consume argument prefix
 args.next(); // consume option name
 String value = args.next(); // consume option value
 assertEquals("expected value", value, commandLine.getOptionValue("optionName"));
 }

 @Override
 public Set getTriggers() {
 return Set.of("optionName");
 }

 @Override
 public Set getPrefixes() {
 return Set.of();
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // No validation needed for this test case
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 // Help messages are not relevant for this test case
 return List.of();
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 // Usage messages are not relevant for this test case
 }

 @Override
 public String getPreferredName() {
 return "optionName";
 }

 @Override
 public String getDescription() {
 return "This is a test option";
 }

 @Override
 public int getId() {
 return 1;
 }

 @Override
 public Option findOption(String trigger) {
 if ("optionName".equals(trigger)) {
 return this;
 }
 return null;
 }

 @Override
 public boolean isRequired() {
 return false;
 }

 @Override
 public Option setParent(Option parent) {
 // No parent for this test option
 return this;
 }
 };

 commandLine.parse("--optionName expected value");
 option.process(commandLine, commandLine.getArgs().listIterator());
 }

 @Test
 public void testProcessOptionWithValue() {
 Option option = new Option() {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
 args.next(); // consume argument prefix
 args.next(); // consume option name
 String value = args.next(); // consume option value
 assertEquals("expected value", value, commandLine.getOptionValue("optionName"));
 }

 @Override
 public Set getTriggers() {
 return Set.of("optionName");
 }

 @Override
 public Set getPrefixes() {
 return Set.of();
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // No validation needed for this test case
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 // Help messages are not relevant for this test case
 return List.of();
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 // Usage messages are not relevant for this test case
 }

 @Override
 public String getPreferredName() {
 return "optionName";
 }

 @Override
 public String getDescription() {
 return "This is a test option with value";
 }

 @Override
 public int getId() {
 return 1;
 }

 @Override
 public Option findOption(String trigger) {
 if ("optionName".equals(trigger)) {
 return this;
 }
 return null;
 }

 @Override
 public boolean isRequired() {
 return false;
 }

 @Override
 public Option setParent(Option parent) {
 // No parent for this test option
 return this;
 }
 };

 commandLine.parse("--optionName expected value");
 option.process(commandLine, commandLine.getArgs().listIterator());
 assertEquals("expected value", commandLine.getOptionValue("optionName"));
 }

 @Test(expected = OptionException.class)
 public void testProcessOptionInvalidArgument() {
 Option option = new Option() {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
 args.next(); // consume argument prefix
 args.next(); // consume option name
 String value = args.next(); // consume option value
 if (!"expected".equals(value)) {
 throw new OptionException("Invalid option value: " + value);
 }
 }

 @Override
 public Set getTriggers() {
 return Set.of("optionName");
 }

 @Override
 public Set getPrefixes() {
 return Set.of();
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // No validation needed for this test case
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 // Help messages are not relevant for this test case
 return List.of();
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 // Usage messages are not relevant for this test case
 }

 @Override
 public String getPreferredName() {
 return "optionName";
 }

 @Override
 public String getDescription() {
 return "This is a test option";
 }

 @Override
 public int getId() {
 return 1;
 }

 @Override
 public Option findOption(String trigger) {
 if ("optionName".equals(trigger)) {
 return this;
 }
 return null;
 }

 @Override
 public boolean isRequired() {
 return false;
 }

 @Override
 public Option setParent(Option parent) {
 // No parent for this test option
 return this;
 }
 };

 commandLine.parse("--optionName invalidValue");
 option.process(commandLine, commandLine.getArgs().listIterator());
 }
}