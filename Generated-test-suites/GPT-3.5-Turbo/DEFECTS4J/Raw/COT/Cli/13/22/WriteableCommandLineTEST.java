
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class WriteableCommandLineTest {
  
  private WriteableCommandLine writeableCommandLine;
  private Option option;
  
  @Before
  public void setUp() {
    writeableCommandLine = new WriteableCommandLineImpl();
    option = new OptionImpl("test");
  }
  
  @Test
  public void testAddOption() {
    writeableCommandLine.addOption(option);
    assertTrue(writeableCommandLine.hasOption("test"));
  }
  
  @Test
  public void testAddValue() {
    writeableCommandLine.addOption(option);
    writeableCommandLine.addValue(option, "value");
    assertEquals(List.of("value"), writeableCommandLine.getValues(option));
  }
  
  @Test
  public void testSetDefaultValues() {
    writeableCommandLine.addOption(option);
    writeableCommandLine.setDefaultValues(option, List.of("default"));
    assertEquals(List.of("default"), writeableCommandLine.getValues(option));
  }
  
  @Test
  public void testAddSwitch() {
    writeableCommandLine.addOption(option);
    writeableCommandLine.addSwitch(option, true);
    assertTrue(writeableCommandLine.hasSwitch(option));
  }
  
  @Test
  public void testSetDefaultSwitch() {
    writeableCommandLine.addOption(option);
    writeableCommandLine.setDefaultSwitch(option, true);
    assertEquals(true, writeableCommandLine.getSwitchValue(option));
  }
  
  @Test
  public void testAddProperty() {
    writeableCommandLine.addProperty("property", "value");
    assertEquals("value", writeableCommandLine.getProperty("property"));
  }
  
  @Test
  public void testLooksLikeOption() {
    assertTrue(writeableCommandLine.looksLikeOption("-test"));
    assertFalse(writeableCommandLine.looksLikeOption("value"));
  }
}
