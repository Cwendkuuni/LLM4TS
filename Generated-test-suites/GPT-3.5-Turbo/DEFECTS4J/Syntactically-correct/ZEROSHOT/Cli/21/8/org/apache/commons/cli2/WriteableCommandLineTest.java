import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

  @Test
  public void testAddOption() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl("option");

    commandLine.addOption(option);

    List<Option> options = commandLine.getOptions();
    assertTrue(options.contains(option));
  }

  @Test
  public void testAddValue() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl("option");
    Object value = new Object();

    commandLine.addValue(option, value);

    List<Object> values = commandLine.getValues(option);
    assertTrue(values.contains(value));
  }

  @Test
  public void testGetUndefaultedValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl("option");
    Object value = new Object();

    commandLine.addValue(option, value);

    List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);
    assertTrue(undefaultedValues.contains(value));
  }

  @Test
  public void testSetDefaultValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl("option");
    List<Object> defaultValues = new ArrayList<>();
    defaultValues.add(new Object());

    commandLine.setDefaultValues(option, defaultValues);

    List<Object> values = commandLine.getValues(option);
    assertTrue(values.containsAll(defaultValues));
  }

  @Test
  public void testAddSwitch() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl("option");

    commandLine.addSwitch(option, true);

    Boolean switchValue = commandLine.getSwitch(option);
    assertTrue(switchValue);
  }

  @Test
  public void testSetDefaultSwitch() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl("option");
    Boolean defaultSwitch = true;

    commandLine.setDefaultSwitch(option, defaultSwitch);

    Boolean switchValue = commandLine.getSwitch(option);
    assertEquals(defaultSwitch, switchValue);
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl("option");
    String property = "property";
    String value = "value";

    commandLine.addProperty(option, property, value);

    String propertyValue = commandLine.getProperty(option, property);
    assertEquals(value, propertyValue);
  }

  @Test
  public void testAddDefaultProperty() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String property = "property";
    String value = "value";

    commandLine.addProperty(property, value);

    String propertyValue = commandLine.getProperty(property);
    assertEquals(value, propertyValue);
  }

  @Test
  public void testLooksLikeOption() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String argument = "-option";

    boolean looksLikeOption = commandLine.looksLikeOption(argument);
    assertTrue(looksLikeOption);
  }
  
  private class WriteableCommandLineImpl implements WriteableCommandLine {
    private List<Option> options;
    private Map<Option, List<Object>> values;
    private Map<Option, Boolean> switches;
    private Map<Option, Map<String, String>> properties;
    private Map<String, String> defaultProperties;

    public WriteableCommandLineImpl() {
      options = new ArrayList<>();
      values = new HashMap<>();
      switches = new HashMap<>();
      properties = new HashMap<>();
      defaultProperties = new HashMap<>();
    }

    @Override
    public void addOption(final Option option) {
      options.add(option);
    }

    @Override
    public void addValue(final Option option, final Object value) {
      List<Object> optionValues = values.get(option);
      if (optionValues == null) {
        optionValues = new ArrayList<>();
        values.put(option, optionValues);
      }
      optionValues.add(value);
    }

    @Override
    public List getUndefaultedValues(final Option option) {
      List<Object> optionValues = values.get(option);
      if (optionValues == null) {
        return new ArrayList<>();
      }
      return optionValues;
    }

    @Override
    public void setDefaultValues(final Option option, final List defaultValues) {
      values.put(option, defaultValues);
    }

    @Override
    public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
      switches.put(option, value);
    }

    @Override
    public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
      switches.put(option, defaultSwitch);
    }

    @Override
    public void addProperty(final Option option, final String property, final String value) {
      Map<String, String> optionProperties = properties.get(option);
      if (optionProperties == null) {
        optionProperties = new HashMap<>();
        properties.put(option, optionProperties);
      }
      optionProperties.put(property, value);
    }

    @Override
    public void addProperty(final String property, final String value) {
      defaultProperties.put(property, value);
    }

    @Override
    public boolean looksLikeOption(final String argument) {
      return argument.startsWith("-");
    }
  }
}