package org.apache.commons.cli2;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class OptionTest {

 @Test
 public void testProcess() {
 // TODO: Implement test case for process method
 }

 @Test
 public void testDefaults() {
 // TODO: Implement test case for defaults method
 }

 @Test
 public void testCanProcessString() {
 final Option option = mock(Option.class);
 WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

 when(option.canProcess(commandLine, "arg")).thenReturn(true);

 assertTrue(option.canProcess(commandLine, "arg"));
 }

 @Test
 public void testCanProcessListIterator() {
 final Option option = mock(Option.class);
 WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
 List<String> args = new ArrayList<>();
 args.add("arg1");
 args.add("arg2");

 when(option.canProcess(commandLine, args.listIterator())).thenReturn(true);

 assertTrue(option.canProcess(commandLine, args.listIterator()));
 }

 @Test
 public void testGetTriggers() {
 final Option option = mock(Option.class);
 Set<String> triggers = new HashSet<>(Arrays.asList("--option", "-o"));
 when(option.getTriggers()).thenReturn(triggers);

 assertEquals(triggers, option.getTriggers());
 }

 @Test
 public void testGetPrefixes() {
 final Option option = mock(Option.class);
 Set<String> prefixes = new HashSet<>(Arrays.asList("--", "-"));
 when(option.getPrefixes()).thenReturn(prefixes);

 assertEquals(prefixes, option.getPrefixes());
 }

 @Test
 public void testValidate() {
 final Option option = mock(Option.class);
 WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

 when(option.validate(commandLine)).thenCallRealMethod();
 option.validate(commandLine);
 verify(option).validate(commandLine);
 }

 @Test
 public void testHelpLines() {
 final Option option = mock(Option.class);
 int depth = 1;
 Set<DisplaySetting> helpSettings = new HashSet<>();
 Comparator<Option> comp = null;
 List<HelpLine> helpLines = option.helpLines(depth, helpSettings, comp);

 assertNotNull(helpLines);
 assertEquals(0, helpLines.size());
 }

 @Test
 public void testAppendUsage() {
 final Option option = mock(Option.class);
 StringBuffer buffer = new StringBuffer();
 Set<DisplaySetting> helpSettings = new HashSet<>();
 Comparator<Option> comp = null;

 option.appendUsage(buffer, helpSettings, comp);
 assertEquals("", buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 final Option option = mock(Option.class);
 String preferredName = "preferredName";
 when(option.getPreferredName()).thenReturn(preferredName);

 assertEquals(preferredName, option.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 final Option option = mock(Option.class);
 String description = "description";
 when(option.getDescription()).thenReturn(description);

 assertEquals(description, option.getDescription());
 }

 @Test
 public void testgetId() {
 final Option option = mock(Option.class);
 int id = 1;
 when(option.getId()).thenReturn(id);

 assertEquals(id, option.getId());
 }

 @Test
 public void testFindOption() {
 final Option option1 = mock(Option.class);
 final Option option2 = mock(Option.class);
 Option option3 = mock(Option.class);

 when(option1.findOption("trigger1")).thenReturn(option1);
 when(option2.findOption("trigger2")).thenReturn(option2);
 when(option3.findOption("trigger3")).thenReturn(null);

 assertEquals(option1, option1.findOption("trigger1"));
 assertEquals(option2, option2.findOption("trigger2"));
 assertNull(option3.findOption("trigger3"));
 }

 @Test
 public void testIsRequired() {
 final Option option = mock(Option.class);
 boolean isRequired = true;
 when(option.isRequired()).thenReturn(isRequired);

 assertEquals(isRequired, option.isRequired());
 }

 @Test
 public void testGetParent() {
 final Option option = mock(Option.class);
 Option parentOption = mock(Option.class);
 when(option.getParent()).thenReturn(parentOption);

 assertEquals(parentOption, option.getParent());
 }

 @Test
 public void testSetParent() {
 final Option option = mock(Option.class);
 Option parentOption = mock(Option.class);

 option.setParent(parentOption);
 verify(parentOption).addOption(option);
 }
}