import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLineParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class OptionTest {

 private Option option;
 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 commandLine = Mockito.mock(WriteableCommandLine.class);
 option = Mockito.spy(new Option() {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator<String> args) {
 // implementation not tested here
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 // implementation not tested here
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, String argument) {
 // implementation not tested here
 return true;
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 // implementation not tested here
 return true;
 }

 @Override
 public Set<String> getTriggers() {
 return new HashSet<>(Arrays.asList("optionTrigger"));
 }

 @Override
 public Set<String> getPrefixes() {
 return new HashSet<>(Arrays.asList("--", "-"));
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // implementation not tested here
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return new ArrayList<>();
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 // implementation not tested here
 }

 @Override
 public String getPreferredName() {
 return "OptionName";
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
 // not implemented in test
 }
 });
 }

 @Test
 public void testGetTriggers() {
 Set<String> triggers = option.getTriggers();
 assertNotNull(triggers);
 assertTrue(triggers.contains("optionTrigger"));
 }

 @Test
 public void testGetPrefixes() {
 Set<String> prefixes = option.getPrefixes();
 assertNotNull(prefixes);
 assertTrue(prefixes.contains("--"));
 assertTrue(prefixes.contains("-"));
 }

 @Test
 public void testCanProcess() {
 assertFalse(option.canProcess(commandLine, new String[]{"wrongArgument"}));
 assertTrue(option.canProcess(commandLine, new String[]{"optionTrigger", "value"}));
 }

 @Test
 public void testFindOption() {
 Option otherOption = Mockito.spy(new Option() {
 @Override
 public Set<String> getTriggers() {
 return new HashSet<>(Arrays.asList("otherOptionTrigger"));
 }
 });
 option.setParent(otherOption);
 assertNull(option.findOption("optionTrigger"));
 assertSame(otherOption, option.findOption("otherOptionTrigger"));
 }

 @Test(expected = OptionException.class)
 public void testInvalidCommandLine() {
 option.validate(commandLine);
 }
}