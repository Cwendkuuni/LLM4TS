import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.apache.commons.cli.Option;

public class OptionsTest {

  private Options options;

  @Before
  public void setUp() {
    options = new Options();
  }

  @Test
  public void testAddOptionGroup() {
    OptionGroup group1 = new OptionGroup();
    Option option1 = new Option("a", "alpha", false, "Option alpha");
    Option option2 = new Option("b", "beta", false, "Option beta");
    group1.addOption(option1).addOption(option2);

    OptionGroup group2 = new OptionGroup();
    Option option3 = new Option("c", "gamma", false, "Option gamma");
    group2.addOption(option3);

    options.addOptionGroup(group1);
    options.addOptionGroup(group2);

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("b"));
    assertTrue(options.hasOption("c"));
    assertFalse(options.hasOption("d"));
  }

  @Test
  public void testAddOption_shortNameNoArgument() {
    options.addOption("a", "Option a");

    assertTrue(options.hasOption("a"));
    assertFalse(options.hasOption("b"));
  }

  @Test
  public void testAddOption_shortNameWithArgument() {
    options.addOption("a", true, "Option a");

    Option opt = options.getOption("a");
    assertNotNull(opt);
    assertTrue(opt.isRequired());
    assertTrue(opt.hasArg());
    assertEquals("Option a", opt.getDescription());
  }

  @Test
  public void testAddOption_shortAndLongNameNoArgument() {
    options.addOption("a", "alpha", false, "Option alpha");

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("alpha"));
    assertFalse(options.hasOption("b"));
  }

  @Test
  public void testAddOption_shortAndLongNameWithArgument() {
    options.addOption("a", "alpha", true, "Option alpha");

    Option opt = options.getOption("a");
    assertNotNull(opt);
    assertTrue(opt.isRequired());
    assertTrue(opt.hasArg());
    assertEquals("Option alpha", opt.getDescription());

    opt = options.getOption("alpha");
    assertNotNull(opt);
    assertTrue(opt.isRequired());
    assertTrue(opt.hasArg());
    assertEquals("Option alpha", opt.getDescription());
  }

  @Test
  public void testGetOptions() {
    options.addOption("a", "Option a");
    options.addOption("b", "Option b");

    List<Option> opts = options.getOptions();
    assertNotNull(opts);
    assertEquals(2, opts.size());

    Option opt1 = opts.get(0);
    assertNotNull(opt1);
    assertEquals("a", opt1.getOpt());
    assertEquals("Option a", opt1.getDescription());

    Option opt2 = opts.get(1);
    assertNotNull(opt2);
    assertEquals("b", opt2.getOpt());
    assertEquals("Option b", opt2.getDescription());
  }

  @Test
  public void testGetRequiredOptions() {
    options.addOption("a", true, "Option a");
    options.addOption("b", true, "Option b");

    List<String> requiredOpts = options.getRequiredOptions();
    assertNotNull(requiredOpts);
    assertEquals(2, requiredOpts.size());
    assertTrue(requiredOpts.contains("a"));
    assertTrue(requiredOpts.contains("b"));
  }

  @Test
  public void testGetOption_shortName() {
    options.addOption("a", "Option a");

    Option opt = options.getOption("a");
    assertNotNull(opt);
    assertEquals("a", opt.getOpt());
    assertNull(opt.getLongOpt());
    assertFalse(opt.isRequired());
    assertFalse(opt.hasArg());
    assertEquals("Option a", opt.getDescription());
  }

  @Test
  public void testGetOption_longName() {
    options.addOption("a", "alpha", true, "Option alpha");

    Option opt = options.getOption("alpha");
    assertNotNull(opt);
    assertEquals("a", opt.getOpt());
    assertEquals("alpha", opt.getLongOpt());
    assertTrue(opt.isRequired());
    assertTrue(opt.hasArg());
    assertEquals("Option alpha", opt.getDescription());
  }

  @Test
  public void testGetOption_invalidName() {
    options.addOption("a", "Option a");

    Option opt = options.getOption("b");
    assertNull(opt);
  }

  @Test
  public void testHasOption_shortName() {
    options.addOption("a", "Option a");

    assertTrue(options.hasOption("a"));
    assertFalse(options.hasOption("b"));
  }

  @Test
  public void testHasOption_longName() {
    options.addOption("a", "alpha", true, "Option alpha");

    assertTrue(options.hasOption("alpha"));
    assertFalse(options.hasOption("beta"));
  }

  @Test
  public void testHasOption_invalidName() {
    options.addOption("a", "Option a");

    assertFalse(options.hasOption("b"));
  }

  @Test
  public void testHasLongOption() {
    options.addOption("a", "alpha", true, "Option alpha");

    assertTrue(options.hasLongOption("alpha"));
    assertFalse(options.hasLongOption("beta"));
  }

  @Test
  public void testHasShortOption() {
    options.addOption("a", "alpha", true, "Option alpha");

    assertTrue(options.hasShortOption("a"));
    assertFalse(options.hasShortOption("b"));
  }

  @Test
  public void testGetOptionGroup() {
    OptionGroup group1 = new OptionGroup();
    Option option1 = new Option("a", "alpha", false, "Option alpha");
    group1.addOption(option1);

    OptionGroup group2 = new OptionGroup();
    Option option2 = new Option("b", "beta", false, "Option beta");
    group2.addOption(option2);

    options.addOptionGroup(group1);
    options.addOptionGroup(group2);

    OptionGroup optGroup1 = options.getOptionGroup(option1);
    assertNotNull(optGroup1);
    assertEquals(group1, optGroup1);

    OptionGroup optGroup2 = options.getOptionGroup(option2);
    assertNotNull(optGroup2);
    assertEquals(group2, optGroup2);

    OptionGroup optGroup3 = options.getOptionGroup(new Option("c", "gamma", false, "Option gamma"));
    assertNull(optGroup3);
  }

  @Test
  public void testToString() {
    options.addOption("a", "Option a");
    options.addOption("b", "Option b");
  
    String str = options.toString();
    assertNotNull(str);
    assertTrue(str.contains("[ short {a=Option a, b=Option b}"));
    assertTrue(str.contains("[ long {a=null, b=null}]"));
  }
}