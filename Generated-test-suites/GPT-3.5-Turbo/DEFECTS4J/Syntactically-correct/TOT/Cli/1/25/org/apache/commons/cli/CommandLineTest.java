import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

  @Test
  public void testHasOption() {
    CommandLine cmd = new CommandLine();
    assertFalse(cmd.hasOption("a"));
    assertFalse(cmd.hasOption('b'));

    cmd.addOption(new Option("a", false, "Option a"));
    cmd.addOption(new Option("b", false, "Option b"));

    assertTrue(cmd.hasOption("a"));
    assertTrue(cmd.hasOption('b'));
  }

  @Test
  public void testGetOptionObject() {
    CommandLine cmd = new CommandLine();
    assertNull(cmd.getOptionObject("a"));
    assertNull(cmd.getOptionObject('b'));

    cmd.addOption(new Option("a", false, "Option a"));
    cmd.addOption(new Option("b", false, "Option b"));

    assertEquals(null, cmd.getOptionObject("a"));
    assertEquals(null, cmd.getOptionObject('b'));

    cmd.addOptionValue("a", "value1");
    cmd.addOptionValue("b", "value2");

    assertEquals("value1", (String) cmd.getOptionObject("a"));
    assertEquals("value2", (String) cmd.getOptionObject('b'));
  }

  @Test
  public void testGetOptionValue() {
    CommandLine cmd = new CommandLine();
    assertNull(cmd.getOptionValue("a"));
    assertNull(cmd.getOptionValue('b'));

    cmd.addOption(new Option("a", false, "Option a"));
    cmd.addOption(new Option("b", false, "Option b"));

    assertEquals(null, cmd.getOptionValue("a"));
    assertEquals(null, cmd.getOptionValue('b'));

    cmd.addOptionValue("a", "value1");
    cmd.addOptionValue("b", "value2");

    assertEquals("value1", cmd.getOptionValue("a"));
    assertEquals("value2", cmd.getOptionValue('b'));
  }

  @Test
  public void testGetOptionValues() {
    CommandLine cmd = new CommandLine();
    assertNull(cmd.getOptionValues("a"));
    assertNull(cmd.getOptionValues('b'));

    cmd.addOption(new Option("a", false, "Option a"));
    cmd.addOption(new Option("b", false, "Option b"));

    assertEquals(null, cmd.getOptionValues("a"));
    assertEquals(null, cmd.getOptionValues('b'));

    cmd.addOptionValue("a", "value1");
    cmd.addOptionValue("a", "value2");
    cmd.addOptionValue("b", "value3");

    String[] valuesA = cmd.getOptionValues("a");
    String[] valuesB = cmd.getOptionValues('b');

    assertEquals(2, valuesA.length);
    assertEquals(1, valuesB.length);
    assertEquals("value1", valuesA[0]);
    assertEquals("value2", valuesA[1]);
    assertEquals("value3", valuesB[0]);
  }

  @Test
  public void testGetOptionValueDefaultValue() {
    CommandLine cmd = new CommandLine();
    assertNull(cmd.getOptionValue("a", "default1"));
    assertNull(cmd.getOptionValue('b', "default2"));

    cmd.addOption(new Option("a", false, "Option a"));
    cmd.addOption(new Option("b", false, "Option b"));

    assertEquals("default1", cmd.getOptionValue("a", "default1"));
    assertEquals("default2", cmd.getOptionValue('b', "default2"));

    cmd.addOptionValue("a", "value1");
    cmd.addOptionValue("b", "value2");

    assertEquals("value1", cmd.getOptionValue("a", "default1"));
    assertEquals("value2", cmd.getOptionValue('b', "default2"));
  }

  @Test
  public void testGetArgs() {
    CommandLine cmd = new CommandLine();
    assertArrayEquals(new String[0], cmd.getArgs());

    cmd.addArg("arg1");
    cmd.addArg("arg2");

    assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
  }

  @Test
  public void testGetArgList() {
    CommandLine cmd = new CommandLine();
    assertEquals(0, cmd.getArgList().size());

    cmd.addArg("arg1");
    cmd.addArg("arg2");

    assertEquals(2, cmd.getArgList().size());
    assertEquals("arg1", cmd.getArgList().get(0));
    assertEquals("arg2", cmd.getArgList().get(1));
  }

  @Test
  public void testAddOption() {
    CommandLine cmd = new CommandLine();
    Option opt1 = new Option("a", false, "Option a");
    Option opt2 = new Option("b", false, "Option b");

    assertNull(cmd.getOptions());
    assertArrayEquals(new Option[0], cmd.getOptions());

    cmd.addOption(opt1);
    cmd.addOption(opt2);

    assertEquals(2, cmd.getOptions().length);
    assertArrayEquals(new Option[]{opt1, opt2}, cmd.getOptions());
  }

  @Test
  public void testIterator() {
    CommandLine cmd = new CommandLine();
    Option opt1 = new Option("a", false, "Option a");
    Option opt2 = new Option("b", false, "Option b");

    assertFalse(cmd.iterator().hasNext());

    cmd.addOption(opt1);
    cmd.addOption(opt2);

    assertTrue(cmd.iterator().hasNext());
    assertEquals(opt1, cmd.iterator().next());
    assertTrue(cmd.iterator().hasNext());
    assertEquals(opt2, cmd.iterator().next());
  }

}