import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.ArgumentImpl;
import org.apache.commons.cli2.option.OptionException;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.Option;

public class ArgumentImplTest {

  private ArgumentImpl argument;

  @Before
  public void setUp() {
    argument = new ArgumentImpl("arg", "Description", 1, 3, ' ', ';', null, "--", null, 0);
  }

  @Test
  public void testGetPreferredName() {
    assertEquals("arg", argument.getPreferredName());
  }

  @Test
  public void testGetInitialSeparator() {
    assertEquals(' ', argument.getInitialSeparator());
  }

  @Test
  public void testGetSubsequentSeparator() {
    assertEquals(';', argument.getSubsequentSeparator());
  }

  @Test
  public void testGetConsumeRemaining() {
    assertEquals("--", argument.getConsumeRemaining());
  }

  @Test
  public void testGetDefaultValues() {
    assertNull(argument.getDefaultValues());
  }

  @Test
  public void testIsValidDefaultValue() {
    assertTrue(argument.getDefaultValues().size() >= argument.getMinimum());
    assertTrue(argument.getDefaultValues().size() <= argument.getMaximum());
  }

  @Test
  public void testValidationWithNoValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl(1, false);
    commandLine.addValue(option, "value");
    try {
      argument.validate(commandLine, option);
      fail("Expected OptionException to be thrown");
    } catch (OptionException e) {
      assertEquals(ResourceConstants.ARGUMENT_MISSING_VALUES, e.getMessage());
    }
  }

  @Test
  public void testValidationWithTooManyValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl(1, false);
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    try {
      argument.validate(commandLine, option);
      fail("Expected OptionException to be thrown");
    } catch (OptionException e) {
      assertEquals(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE, e.getMessage());
      assertEquals("value2", e.getMessageArgs()[0]);
    }
  }

  @Test
  public void testValidationWithValidator() {
    Validator validator = new Validator() {
      public void validate(List values) throws InvalidArgumentException {
        if (values.contains("invalid")) {
          throw new InvalidArgumentException("Value is invalid");
        }
      }
    };

    argument = new ArgumentImpl("arg", "Description", 1, 3, ' ', ';', validator, "--", null, 0);

    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl(1, false);
    commandLine.addValue(option, "invalid");
    try {
      argument.validate(commandLine, option);
      fail("Expected OptionException to be thrown");
    } catch (OptionException e) {
      assertEquals(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE, e.getMessage());
      assertEquals("Value is invalid", e.getMessageArgs()[0]);
    }
  }

  @Test
  public void testProcessValuesWithConsumeRemaining() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator<String> arguments = createListIterator("value1", "--", "value2", "value3");

    argument.processValues(commandLine, arguments, null);

    List<String> values = commandLine.getValues(argument);
    assertEquals(3, values.size());
    assertEquals("value1", values.get(0));
    assertEquals("value2", values.get(1));
    assertEquals("value3", values.get(2));
  }

  @Test
  public void testProcessValuesWithSeparator() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator<String> arguments = createListIterator("value1;value2");

    argument.processValues(commandLine, arguments, null);

    List<String> values = commandLine.getValues(argument);
    assertEquals(2, values.size());
    assertEquals("value1", values.get(0));
    assertEquals("value2", values.get(1));
  }

  @Test
  public void testProcessValuesWithTrailingSeparator() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator<String> arguments = createListIterator("value1;value2;");

    try {
      argument.processValues(commandLine, arguments, null);
      fail("Expected OptionException to be thrown");
    } catch (OptionException e) {
      assertEquals(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE, e.getMessage());
      assertEquals("", e.getMessageArgs()[0]);
    }
  }

  @Test
  public void testProcessValuesWithLooksLikeOption() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator<String> arguments = createListIterator("--value");

    argument.processValues(commandLine, arguments, null);

    List<String> values = commandLine.getValues(argument);
    assertEquals(1, values.size());
    assertEquals("--value", values.get(0));
  }

  @Test
  public void testProcessValuesWithNoSplit() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator<String> arguments = createListIterator("value1 value2");

    argument.processValues(commandLine, arguments, null);

    List<String> values = commandLine.getValues(argument);
    assertEquals(1, values.size());
    assertEquals("value1 value2", values.get(0));
  }

  @Test
  public void testCanProcess() {
    assertTrue(argument.canProcess(null, "value"));
  }

  private ListIterator<String> createListIterator(String... args) {
    List<String> list = new ArrayList<String>();
    for (String arg : args) {
      list.add(arg);
    }
    return list.listIterator();
  }
}

class WriteableCommandLineImpl implements WriteableCommandLine {

  @Override
  public void addValue(Option option, Object value) {

  }

  @Override
  public List getValues(Option option) {
    return null;
  }

  @Override
  public void setDefaultValues(Option option, List values) {

  }

  @Override
  public boolean looksLikeOption(String value) {
    return value.startsWith("-");
  }
}

class OptionImpl implements Option {

  private int id;
  private boolean optional;

  public OptionImpl(int id, boolean optional) {
    this.id = id;
    this.optional = optional;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public boolean isOptional() {
    return optional;
  }
}