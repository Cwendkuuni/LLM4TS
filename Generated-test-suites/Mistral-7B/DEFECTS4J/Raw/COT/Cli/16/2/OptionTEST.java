import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

 private WriteableCommandLine commandLine = new WriteableCommandLine();

 @Test
 public void testProcess() throws OptionException {
 Option option = createMockOption();
 option.process(commandLine, commandLine.iterator());
 assertEquals("Expected at least one argument to be processed", 1, commandLine.getArgList().size());
 }

 @Test(expected = OptionException.class)
 public void testProcessWithInvalidArgument() throws OptionException {
 Option option = createMockOption();
 option.process(commandLine, commandLine.iterator());
 commandLine.addOption("--invalid");
 option.process(commandLine, commandLine.iterator());
 }

 @Test
 public void testDefaults() {
 Option option = createMockOption();
 option.defaults(commandLine);
 assertNull("Expected no default value set for a mock option", commandLine.getOptionValue("mockOption"));
 }

 @Test
 public void testCanProcess() {
 Option option = createMockOption();
 assertTrue("Expected mock option to be able to process argument 'mockArgument'", option.canProcess(commandLine, Arrays.asList("mockArgument").iterator()));
 }

 @Test(expected = OptionException.class)
 public void testCanProcessWithInvalidArgument() throws OptionException {
 Option option = createMockOption();
 assertFalse("Expected mock option to not be able to process argument 'invalidArgument'", option.canProcess(commandLine, Arrays.asList("invalidArgument").iterator()));
 commandLine.addOption("--mockOption");
 option.canProcess(commandLine, Arrays.asList("--mockOption", "invalidArgument").iterator());
 }

 @Test
 public void testGetTriggers() {
 Option option = createMockOption();
 Set<String> expectedTriggers = new HashSet<>();
 expectedTriggers.add("mock");
 expectedTriggers.add("-");
 Set<String> actualTriggers = option.getTriggers();
 assertEquals("Expected mock option to have triggers 'mock' and '-'", expectedTriggers, actualTriggers);
 }

 @Test
 public void testGetPrefixes() {
 Option option = createMockOption();
 Set<String> expectedPrefixes = new LinkedHashSet<>();
 expectedPrefixes.add("--");
 expectedPrefixes.add("-");
 Set<String> actualPrefixes = option.getPrefixes();
 assertEquals("Expected mock option to have prefixes '--' and '-'", expectedPrefixes, actualPrefixes);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidCommandLine() throws OptionException {
 Option option = createMockOption();
 commandLine.addOption("--mockOption");
 commandLine.addOption("--invalid");
 option.validate(commandLine);
 }

 @Test
 public void testHelpLines() {
 Option option = createMockOption();
 List<String> expectedHelpLines = Arrays.asList(
 "mockOption",
 "  Option description for mockOption",
 "  Usage: --mockOption [options]",
 "      or:",
 "  Option description for mockOption",
 "  Usage: -m [options]",
 "  mockOption details"
 );
 List<String> actualHelpLines = option.helpLines(0, new HashSet<>(), null);
 assertEquals("Expected mock option to have the given help lines", expectedHelpLines, actualHelpLines);
 }

 @Test
 public void testAppendUsage() {
 Option option = createMockOption();
 StringBuffer expectedUsage = new StringBuffer();
 expectedUsage.append("--mockOption [options]\n");
 expectedUsage.append("Option description for mockOption\n");
 expectedUsage.append("Usage: --mockOption [options]\n");
 expectedUsage.append("or:\n");
 expectedUsage.append("Option description for mockOption\n");
 expectedUsage.append("Usage: -m [options]\n");
 expectedUsage.append("mockOption details\n");
 StringBuffer actualUsage = new StringBuffer();
 option.appendUsage(actualUsage, new HashSet<>(), null);
 assertEquals("Expected mock option to have the given usage string", expectedUsage.toString(), actualUsage.toString());
 }

 @Test
 public void testGetPreferredName() {
 Option option = createMockOption();
 String expectedPreferredName = "mockOption";
 String actualPreferredName = option.getPreferredName();
 assertEquals("Expected mock option to have preferred name 'mockOption'", expectedPreferredName, actualPreferredName);
 }

 @Test
 public void testGetDescription() {
 Option option = createMockOption();
 String expectedDescription = "Option description for mockOption";
 String actualDescription = option.getDescription();
 assertEquals("Expected mock option to have description 'Option description for mockOption'", expectedDescription, actualDescription);
 }

 @Test
 public void testgetId() {
 Option option = createMockOption();
 int expectedId = 1;
 int actualId = option.getId();
 assertEquals("Expected mock option to have id 1", expectedId, actualId);
 }

 @Test
 public void testFindOption() {
 Option parentOption = new MockParentOption();
 Option childOption = new MockChildOption();
 parentOption.addOption(childOption);
 Option foundOption = parentOption.findOption("mockChildOption");
 assertEquals("Expected parent option to return child option when finding option with trigger 'mockChildOption'", childOption, foundOption);
 }

 @Test
 public void testIsRequired() {
 Option option = createMockOption();
 assertFalse("Expected mock option to not be required", option.isRequired());
 }

 @Test
 public void testGetParent() {
 Option option = new MockChildOption();
 Option parentOption = new MockParentOption();
 option.setParent(parentOption);
 Option foundParentOption = option.getParent();
 assertEquals("Expected child option to have parent option", parentOption, foundParentOption);
 }

 private Option createMockOption() {
 return new MockOption("mockOption", "mockArgument");
 }

 private static class MockOption extends AbstractOption {
 MockOption(String name, String argument) {
 super(name, argument, null, null, null, null, null, null, null);
 }
 }

 private static class MockParentOption extends Option {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
 super.process(commandLine, args);
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 super.defaults(commandLine);
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> args) {
 return super.canProcess(commandLine, args);
 }

 @Override
 public Set getTriggers() {
 return new HashSet<>();
 }

 @Override
 public Set getPrefixes() {
 return new HashSet<>();
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 super.validate(commandLine);
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return Arrays.asList(new String[0]);
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 }

 @Override
 public String getPreferredName() {
 return "mockParentOption";
 }

 @Override
 public String getDescription() {
 return "Option description for mockParentOption";
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
 }

 private static class MockChildOption extends Option {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> args) {
 return false;
 }

 @Override
 public Set getTriggers() {
 return new HashSet<>();
 }

 @Override
 public Set getPrefixes() {
 return new HashSet<>();
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return Arrays.asList(new String[0]);
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 }

 @Override
 public String getPreferredName() {
 return "mockChildOption";
 }

 @Override
 public String getDescription() {
 return "Option description for mockChildOption";
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
 }
}