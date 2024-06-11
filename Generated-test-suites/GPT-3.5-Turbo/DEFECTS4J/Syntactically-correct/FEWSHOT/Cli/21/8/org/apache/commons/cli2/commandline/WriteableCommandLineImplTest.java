import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
  @Test
  public void testAddOption() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    writeableCommandLineImpl.addOption(option);
    assertTrue(writeableCommandLineImpl.getOptions().contains(option));
    assertNotNull(writeableCommandLineImpl.getOptionTriggers());
  }

  @Test
  public void testAddValue() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    writeableCommandLineImpl.addValue(option, new Object());
    List<Object> valueList = new ArrayList<>();
    valueList.add(new Object());
    assertEquals(valueList, writeableCommandLineImpl.getValues(option, null));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    writeableCommandLineImpl.addSwitch(option, false);
    assertTrue(writeableCommandLineImpl.hasOption(option));
    assertEquals(false, writeableCommandLineImpl.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertFalse(writeableCommandLineImpl.hasOption(option));
    writeableCommandLineImpl.addOption(option);
    assertTrue(writeableCommandLineImpl.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertNull(writeableCommandLineImpl.getOption(""));
    writeableCommandLineImpl.addOption(option);
    assertEquals(option, writeableCommandLineImpl.getOption(""));
  }

  @Test
  public void testGetValues() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertNull(writeableCommandLineImpl.getValues(option, null));
    writeableCommandLineImpl.addValue(option, new Object());
    List<Object> valueList = new ArrayList<>();
    valueList.add(new Object());
    assertEquals(valueList, writeableCommandLineImpl.getValues(option, null));
  }

  @Test
  public void testGetUndefaultedValues() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertNull(writeableCommandLineImpl.getUndefaultedValues(option));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertNull(writeableCommandLineImpl.getSwitch(option, null));
  }

  @Test
  public void testGetProperty() {
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertNull(writeableCommandLineImpl.getProperty(""));
  }

  @Test
  public void testAddProperty() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    writeableCommandLineImpl.addProperty(new PropertyOption(), "", "");
  }

  @Test
  public void testGetProperty1() {
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertEquals("", writeableCommandLineImpl.getProperty(new PropertyOption(), "", ""));
  }

  @Test
  public void testGetProperties() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertEquals(Collections.emptySet(), writeableCommandLineImpl.getProperties(option));
  }

  @Test
  public void testGetProperties1() {
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertEquals(Collections.emptySet(), writeableCommandLineImpl.getProperties());
  }

  @Test
  public void testLooksLikeOption() {
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertFalse(writeableCommandLineImpl.looksLikeOption(""));
  }

  @Test
  public void testToString() {
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertEquals("", writeableCommandLineImpl.toString());
  }

  @Test
  public void testGetOptions() {
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertEquals(Collections.emptyList(), writeableCommandLineImpl.getOptions());
  }

  @Test
  public void testGetOptionTriggers() {
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertEquals(Collections.emptySet(), writeableCommandLineImpl.getOptionTriggers());
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    writeableCommandLineImpl.setDefaultValues(option, Collections.emptyList());
    writeableCommandLineImpl.setDefaultValues(option, null);
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option();
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    writeableCommandLineImpl.setDefaultSwitch(option, null);
  }

  @Test
  public void testGetNormalised() {
    WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
    assertEquals(Collections.emptyList(), writeableCommandLineImpl.getNormalised());
  }
}