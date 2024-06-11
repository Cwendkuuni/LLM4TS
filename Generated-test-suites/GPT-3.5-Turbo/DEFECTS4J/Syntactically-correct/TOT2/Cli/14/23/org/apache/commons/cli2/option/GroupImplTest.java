import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class GroupImplTest {
  
  @Test
  public void testCanProcessWithValidOption() {
    List options = new ArrayList<>();
    GroupImpl group = new GroupImpl(options, "testGroup", "Test group", 0, 1);
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String arg = "-option";
    assertTrue(group.canProcess(commandLine, arg));
  }
  
  @Test
  public void testCanProcessWithInvalidOption() {
    List options = new ArrayList<>();
    GroupImpl group = new GroupImpl(options, "testGroup", "Test group", 0, 1);
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String arg = "--invalid";
    assertFalse(group.canProcess(commandLine, arg));
  }
  
  @Test
  public void testProcessWithValidOptions() {
    List options = new ArrayList<>();
    GroupImpl group = new GroupImpl(options, "testGroup", "Test group", 1, 2);
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator arguments = Arrays.asList("--option1", "--option2").listIterator();
    group.process(commandLine, arguments);
    assertTrue(commandLine.hasOption("--option1"));
    assertTrue(commandLine.hasOption("--option2"));
  }
  
  @Test
  public void testProcessWithInvalidOption() {
    List options = new ArrayList<>();
    GroupImpl group = new GroupImpl(options, "testGroup", "Test group", 1, 2);
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator arguments = Arrays.asList("--option1", "--invalid").listIterator();
    try {
      group.process(commandLine, arguments);
      fail("Expected OptionException");
    } catch (OptionException e) {
      assertEquals(ResourceConstants.UNEXPECTED_TOKEN, e.getMessage());
    }
  }
  
  @Test
  public void testValidateWithValidOptions() {
    List options = new ArrayList<>();
    OptionMock firstOption = new OptionMock();
    firstOption.setRequired(true);
    OptionMock secondOption = new OptionMock();
    options.add(firstOption);
    options.add(secondOption);
    GroupImpl group = new GroupImpl(options, "testGroup", "Test group", 1, 2);
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    commandLine.addOption(firstOption);
    commandLine.addOption(secondOption);
    group.validate(commandLine);
  }
  
  @Test
  public void testValidateWithMissingOption() {
    List options = new ArrayList<>();
    OptionMock requiredOption = new OptionMock();
    requiredOption.setRequired(true);
    options.add(requiredOption);
    GroupImpl group = new GroupImpl(options, "testGroup", "Test group", 1, 2);
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    try {
      group.validate(commandLine);
      fail("Expected OptionException");
    } catch (OptionException e) {
      assertEquals(ResourceConstants.MISSING_OPTION, e.getMessage());
    }
  }

}