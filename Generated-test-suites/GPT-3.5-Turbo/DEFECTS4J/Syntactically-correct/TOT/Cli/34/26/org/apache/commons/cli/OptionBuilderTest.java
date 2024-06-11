import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  // Test case for withLongOpt()
  @Test
  public void withLongOptTest() {
    OptionBuilder.withLongOpt("testOption");
    Option option = OptionBuilder.create();
    assertEquals("testOption", option.getLongOpt());
  }

  // Test case for hasArg()
  @Test
  public void hasArgTest() {
    OptionBuilder.hasArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
  }

  // Test case for hasArg(boolean hasArg)
  @Test
  public void hasArgBooleanTest() {
    OptionBuilder.hasArg(true);
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());

    OptionBuilder.hasArg(false);
    Option option2 = OptionBuilder.create();
    assertFalse(option2.hasArg());
  }

  // Test case for withArgName()
  @Test
  public void withArgNameTest() {
    OptionBuilder.withArgName("testName");
    Option option = OptionBuilder.create();
    assertEquals("testName", option.getArgName());
  }

  // Test case for isRequired()
  @Test
  public void isRequiredTest() {
    OptionBuilder.isRequired();
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }

  // Test case for withValueSeparator(char sep)
  @Test
  public void withValueSeparatorCharTest() {
    OptionBuilder.withValueSeparator(':');
    Option option = OptionBuilder.create();
    assertEquals(':', option.getValueSeparator());
  }

  // Test case for withValueSeparator()
  @Test
  public void withValueSeparatorTest() {
    OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create();
    assertEquals('=', option.getValueSeparator());
  }

  // Test case for isRequired(boolean newRequired)
  @Test
  public void isRequiredBooleanTest() {
    OptionBuilder.isRequired(true);
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());

    OptionBuilder.isRequired(false);
    Option option2 = OptionBuilder.create();
    assertFalse(option2.isRequired());
  }

  // Test case for hasArgs()
  @Test
  public void hasArgsTest() {
    OptionBuilder.hasArgs();
    Option option = OptionBuilder.create();
    assertTrue(option.hasArgs());
  }

  // Test case for hasArgs(int num)
  @Test
  public void hasArgsIntTest() {
    OptionBuilder.hasArgs(2);
    Option option = OptionBuilder.create();
    assertEquals(2, option.getArgs());
  }

  // Test case for hasOptionalArg()
  @Test
  public void hasOptionalArgTest() {
    OptionBuilder.hasOptionalArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasOptionalArg());
  }

  // Test case for hasOptionalArgs()
  @Test
  public void hasOptionalArgsTest() {
    OptionBuilder.hasOptionalArgs();
    Option option = OptionBuilder.create();
    assertTrue(option.hasOptionalArgs());
  }

  // Test case for hasOptionalArgs(int numArgs)
  @Test
  public void hasOptionalArgsIntTest() {
    OptionBuilder.hasOptionalArgs(3);
    Option option = OptionBuilder.create();
    assertEquals(3, option.getArgs());
    assertTrue(option.hasOptionalArgs());
  }

  // Test case for withType()
  @Test
  public void withTypeTest() {
    OptionBuilder.withType(Integer.class);
    Option option = OptionBuilder.create();
    assertEquals(Integer.class, option.getType());
  }

  // Test case for withDescription()
  @Test
  public void withDescriptionTest() {
    OptionBuilder.withDescription("testDescription");
    Option option = OptionBuilder.create();
    assertEquals("testDescription", option.getDescription());
  }

  // Test case for create(char opt)
  @Test
  public void createCharTest() {
    Option option = OptionBuilder.create('T');
    assertEquals("T", option.getOpt());
  }

  // Test case for create()
  @Test(expected = IllegalArgumentException.class)
  public void createTest() {
    OptionBuilder.create();
  }

  // Test case for create(String opt)
  @Test
  public void createStringTest() {
    Option option = OptionBuilder.create("testOpt");
    assertEquals("testOpt", option.getOpt());
  }

}