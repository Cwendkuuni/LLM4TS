import org.apache.commons.cli2.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineTest {

  Option option = new Option("option", "description");

  @Test
  public void testAddOption() {
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public void addOption(Option option) {
        assertNotNull(option);
      }
      public void addValue(Option option, Object value) {}
      public List getUndefaultedValues(Option option) {return null;}
      public void setDefaultValues(Option option, List defaultValues) {}
      public void addSwitch(Option option, boolean value) throws IllegalStateException {}
      public void setDefaultSwitch(Option option, Boolean defaultSwitch) {}
      public void addProperty(Option option, String property, String value) {}
      public void addProperty(String property, String value) {}
      public boolean looksLikeOption(String argument) {return false;}
    };

    commandLine.addOption(option);
  }

  @Test
  public void testAddValue() {
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public void addOption(Option option) {}
      public void addValue(Option option, Object value) {
        assertNotNull(option);
        assertNotNull(value);
      }
      public List getUndefaultedValues(Option option) {return null;}
      public void setDefaultValues(Option option, List defaultValues) {}
      public void addSwitch(Option option, boolean value) throws IllegalStateException {}
      public void setDefaultSwitch(Option option, Boolean defaultSwitch) {}
      public void addProperty(Option option, String property, String value) {}
      public void addProperty(String property, String value) {}
      public boolean looksLikeOption(String argument) {return false;}
    };

    commandLine.addValue(option, "value");
  }

  @Test
  public void testGetUndefaultedValues() {
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public void addOption(Option option) {}
      public void addValue(Option option, Object value) {}
      public List getUndefaultedValues(Option option) {
        assertNotNull(option);
      }
      public void setDefaultValues(Option option, List defaultValues) {}
      public void addSwitch(Option option, boolean value) throws IllegalStateException {}
      public void setDefaultSwitch(Option option, Boolean defaultSwitch) {}
      public void addProperty(Option option, String property, String value) {}
      public void addProperty(String property, String value) {}
      public boolean looksLikeOption(String argument) {return false;}
    };

    commandLine.getUndefaultedValues(option);
  }

  @Test
  public void testSetDefaultValues() {
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public void addOption(Option option) {}
      public void addValue(Option option, Object value) {}
      public List getUndefaultedValues(Option option) {}
      public void setDefaultValues(Option option, List defaultValues) {
        assertNotNull(option);
        assertNotNull(defaultValues);
      }
      public void addSwitch(Option option, boolean value) throws IllegalStateException {}
      public void setDefaultSwitch(Option option, Boolean defaultSwitch) {}
      public void addProperty(Option option, String property, String value) {}
      public void addProperty(String property, String value) {}
      public boolean looksLikeOption(String argument) {return false;}
    };

    commandLine.setDefaultValues(option, new ArrayList());
  }

  @Test
  public void testAddSwitch() {
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public void addOption(Option option) {}
      public void addValue(Option option, Object value) {}
      public List getUndefaultedValues(Option option) {}
      public void setDefaultValues(Option option, List defaultValues) {}
      public void addSwitch(Option option, boolean value) throws IllegalStateException {
        assertNotNull(option);
      }
      public void setDefaultSwitch(Option option, Boolean defaultSwitch) {}
      public void addProperty(Option option, String property, String value) {}
      public void addProperty(String property, String value) {}
      public boolean looksLikeOption(String argument) {return false;}
    };

    commandLine.addSwitch(option, true);
  }

  @Test
  public void testSetDefaultSwitch() {
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public void addOption(Option option) {}
      public void addValue(Option option, Object value) {}
      public List getUndefaultedValues(Option option) {}
      public void setDefaultValues(Option option, List defaultValues) {}
      public void addSwitch(Option option, boolean value) throws IllegalStateException {}
      public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        assertNotNull(option);
        assertNotNull(defaultSwitch);
      }
      public void addProperty(Option option, String property, String value) {}
      public void addProperty(String property, String value) {}
      public boolean looksLikeOption(String argument) {return false;}
    };

    commandLine.setDefaultSwitch(option, true);
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public void addOption(Option option) {}
      public void addValue(Option option, Object value) {}
      public List getUndefaultedValues(Option option) {}
      public void setDefaultValues(Option option, List defaultValues) {}
      public void addSwitch(Option option, boolean value) throws IllegalStateException {}
      public void setDefaultSwitch(Option option, Boolean defaultSwitch) {}
      public void addProperty(Option option, String property, String value) {
        assertNotNull(option);
        assertNotNull(property);
        assertNotNull(value);
      }
      public void addProperty(String property, String value) {}
      public boolean looksLikeOption(String argument) {return false;}
    };

    commandLine.addProperty(option, "property", "value");
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public void addOption(Option option) {}
      public void addValue(Option option, Object value) {}
      public List getUndefaultedValues(Option option) {}
      public void setDefaultValues(Option option, List defaultValues) {}
      public void addSwitch(Option option, boolean value) throws IllegalStateException {}
      public void setDefaultSwitch(Option option, Boolean defaultSwitch) {}
      public void addProperty(Option option, String property, String value) {}
      public void addProperty(String property, String value) {
        assertNotNull(property);
        assertNotNull(value);
      }
      public boolean looksLikeOption(String argument) {return false;}
    };

    commandLine.addProperty("property", "value");
  }

  @Test
  public void testLooksLikeOption() {
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public void addOption(Option option) {}
      public void addValue(Option option, Object value) {}
      public List getUndefaultedValues(Option option) {}
      public void setDefaultValues(Option option, List defaultValues) {}
      public void addSwitch(Option option, boolean value) throws IllegalStateException {}
      public void setDefaultSwitch(Option option, Boolean defaultSwitch) {}
      public void addProperty(Option option, String property, String value) {}
      public void addProperty(String property, String value) {}
      public boolean looksLikeOption(String argument) {
        assertNotNull(argument);
      }
    };

    commandLine.looksLikeOption("argument");
  }
}