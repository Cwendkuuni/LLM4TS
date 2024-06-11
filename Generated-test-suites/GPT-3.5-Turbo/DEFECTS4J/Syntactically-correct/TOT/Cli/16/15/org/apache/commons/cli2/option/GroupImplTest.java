import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.*;
import java.util.ArrayList;
import java.util.List;

public class GroupImplTest {
  
  private GroupImpl group;
  
  @Before
  public void setUp() {
    List<Option> options = new ArrayList<Option>();
    options.add(new StringOption("name", "Name of the person"));
    options.add(new IntegerOption("age", "Age of the person"));
    options.add(new BooleanOption("married", "Is the person married?"));
    options.add(new DoubleOption("salary", "Salary of the person"));
    group = new GroupImpl(options, "Person", "Options for a person", 1, 3);
  }
  
  @Test
  public void testCanProcess() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    
    assertFalse(group.canProcess(commandLine, null));
    
    assertTrue(group.canProcess(commandLine, "name"));
    group.process(commandLine, "name");
    
    assertTrue(group.canProcess(commandLine, "age"));
    group.process(commandLine, "age");
    
    assertTrue(group.canProcess(commandLine, "married"));
    group.process(commandLine, "married");
    
    assertTrue(group.canProcess(commandLine, "salary"));
    group.process(commandLine, "salary");
    
    assertFalse(group.canProcess(commandLine, "address"));
  }
  
  @Test
  public void testProcess() throws OptionException {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    StringOption nameOption = new StringOption("name", "Name of the person");
    IntegerOption ageOption = new IntegerOption("age", "Age of the person");
    BooleanOption marriedOption = new BooleanOption("married", "Is the person married?");
    DoubleOption salaryOption = new DoubleOption("salary", "Salary of the person");
    
    group.process(commandLine, nameOption);
    assertTrue(commandLine.hasOption(nameOption));
    
    group.process(commandLine, ageOption);
    assertTrue(commandLine.hasOption(ageOption));
    
    group.process(commandLine, marriedOption);
    assertTrue(commandLine.hasOption(marriedOption));
    
    group.process(commandLine, salaryOption);
    assertTrue(commandLine.hasOption(salaryOption));
  }
  
  @Test
  public void testValidate() throws OptionException {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    
    try {
      group.validate(commandLine);
      fail("OptionException expected");
    } catch (OptionException e) {
      assertEquals(ResourceConstants.MISSING_OPTION, e.getMessage());
    }
    
    commandLine.addValue(group, "name", "John");
    commandLine.addValue(group, "age", "25");
    commandLine.addValue(group, "married", "true");
    
    try {
      group.validate(commandLine);
      fail("OptionException expected");
    } catch (OptionException e) {
      assertEquals(ResourceConstants.MISSING_OPTION, e.getMessage());
    }
    
    commandLine.addValue(group, "salary", "5000.0");
    
    try {
      group.validate(commandLine);
    } catch (OptionException e) {
      fail("OptionException not expected");
    }
    
    commandLine.addValue(group, "address", "123 Main St");
    
    try {
      group.validate(commandLine);
      fail("OptionException expected");
    } catch (OptionException e) {
      assertEquals(ResourceConstants.UNEXPECTED_TOKEN, e.getMessage());
    }
  }
  
  @Test
  public void testGetPreferredName() {
    assertEquals("Person", group.getPreferredName());
  }
  
  @Test
  public void testGetDescription() {
    assertEquals("Options for a person", group.getDescription());
  }
  
  @Test
  public void testAppendUsage() {
    StringBuffer buffer = new StringBuffer();
    Set<DisplaySetting> helpSettings = new HashSet<>();
    
    group.appendUsage(buffer, helpSettings, null);
    
    assertEquals("[name|age|married|salary]", buffer.toString());
  }
  
  @Test
  public void testGetOptions() {
    List<Option> options = group.getOptions();
    
    assertEquals(4, options.size());
  }
  
  @Test
  public void testFindOption() {
    Option option = group.findOption("name");
    
    assertTrue(option instanceof StringOption);
    assertEquals("name", option.getPreferredName());
  }
  
}