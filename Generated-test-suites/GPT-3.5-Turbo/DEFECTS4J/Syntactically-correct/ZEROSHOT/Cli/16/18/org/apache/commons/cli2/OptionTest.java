package org.apache.commons.cli2;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class OptionTest {

  @Test
  public void testProcess() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator args = null;  // your test data for arguments
    Option option = new OptionImpl();  // your Option implementation
    OptionException expectedException = null;  // your expected exception

    try {
      option.process(commandLine, args);
    } catch (OptionException e) {
      Assert.assertEquals(expectedException, e);
    }
  }

  @Test
  public void testDefaults() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl();  // your Option implementation

    option.defaults(commandLine);
  }

  @Test
  public void testCanProcessString() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String argument = null;  // your test data for argument
    Option option = new OptionImpl();  // your Option implementation

    boolean result = option.canProcess(commandLine, argument);
    Assert.assertTrue(result);
  }

  @Test
  public void testCanProcessListIterator() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator arguments = null;  // your test data for arguments
    Option option = new OptionImpl();  // your Option implementation

    boolean result = option.canProcess(commandLine, arguments);
    Assert.assertTrue(result);
  }

  @Test
  public void testGetTriggers() {
    Option option = new OptionImpl();  // your Option implementation

    Set triggers = option.getTriggers();
    Assert.assertNotNull(triggers);
  }

  @Test
  public void testGetPrefixes() {
    Option option = new OptionImpl();  // your Option implementation

    Set prefixes = option.getPrefixes();
    Assert.assertNotNull(prefixes);
  }

  @Test
  public void testValidate() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl();  // your Option implementation
    OptionException expectedException = null;  // your expected exception

    try {
      option.validate(commandLine);
    } catch (OptionException e) {
      Assert.assertEquals(expectedException, e);
    }
  }

  @Test
  public void testHelpLines() {
    int depth = 0;  // your test data for depth
    Set helpSettings = null;  // your test data for help settings
    Comparator comp = null;  // your test data for comparator
    Option option = new OptionImpl();  // your Option implementation

    List helpLines = option.helpLines(depth, helpSettings, comp);
    Assert.assertNotNull(helpLines);
  }

  @Test
  public void testAppendUsage() {
    StringBuffer buffer = new StringBuffer();
    Set helpSettings = null;  // your test data for help settings
    Comparator comp = null;  // your test data for comparator
    Option option = new OptionImpl();  // your Option implementation

    option.appendUsage(buffer, helpSettings, comp);
    Assert.assertNotNull(buffer);
  }

  @Test
  public void testGetPreferredName() {
    Option option = new OptionImpl();  // your Option implementation

    String preferredName = option.getPreferredName();
    Assert.assertNotNull(preferredName);
  }

  @Test
  public void testGetDescription() {
    Option option = new OptionImpl();  // your Option implementation

    String description = option.getDescription();
    Assert.assertNotNull(description);
  }

  @Test
  public void testGetId() {
    Option option = new OptionImpl();  // your Option implementation

    int id = option.getId();
    Assert.assertNotNull(id);
  }

  @Test
  public void testFindOption() {
    String trigger = null;  // your test data for trigger
    Option option = new OptionImpl();  // your Option implementation

    Option result = option.findOption(trigger);
    Assert.assertNotNull(result);
  }

  @Test
  public void testIsRequired() {
    Option option = new OptionImpl();  // your Option implementation

    boolean required = option.isRequired();
    Assert.assertNotNull(required);
  }

  @Test
  public void testGetParent() {
    Option option = new OptionImpl();  // your Option implementation

    Option parent = option.getParent();
    Assert.assertNotNull(parent);
  }

  @Test
  public void testSetParent() {
    Option option = new OptionImpl();  // your Option implementation
    Option parent = null;  // your test data for parent option

    option.setParent(parent);
  }
}