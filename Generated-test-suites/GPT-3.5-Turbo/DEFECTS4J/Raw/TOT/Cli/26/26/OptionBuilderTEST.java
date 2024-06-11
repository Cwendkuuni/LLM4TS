import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("testOption");
    // Verify longopt is set correctly
    Option option = OptionBuilder.create();
    Assert.assertEquals("testOption", option.getLongOpt());
  }

  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    // Verify numberOfArgs is set correctly
    Option option = OptionBuilder.create();
    Assert.assertEquals(1, option.getArgs());
  }

  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder.hasArg(true);
    // Verify numberOfArgs is set correctly
    Option option = OptionBuilder.create();
    Assert.assertEquals(1, option.getArgs());

    OptionBuilder.hasArg(false);
    // Verify numberOfArgs is set correctly
    option = OptionBuilder.create();
    Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("argName");
    // Verify argName is set correctly
    Option option = OptionBuilder.create();
    Assert.assertEquals("argName", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    // Verify required is set correctly
    Option option = OptionBuilder.create();
    Assert.assertTrue(option.isRequired());
  }

  @Test
  public void testWithRequired() {
    OptionBuilder.isRequired(true);
    // Verify required is set correctly
    Option option = OptionBuilder.create();
    Assert.assertTrue(option.isRequired());

    OptionBuilder.isRequired(false);
    // Verify required is set correctly
    option = OptionBuilder.create();
    Assert.assertFalse(option.isRequired());
  }

  @Test
  public void testWithOptionalArg() {
    OptionBuilder.hasOptionalArg();
    // Verify optionalArg is set correctly
    Option option = OptionBuilder.create();
    Assert.assertEquals(1, option.getArgs());
    Assert.assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    // Verify numberOfArgs is set correctly
    Option option = OptionBuilder.create();
    Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    Assert.assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgsWithNumArgs() {
    OptionBuilder.hasOptionalArgs(2);
    // Verify numberOfArgs is set correctly
    Option option = OptionBuilder.create();
    Assert.assertEquals(2, option.getArgs());
    Assert.assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testWithType() {
    OptionBuilder.withType(Integer.class);
    // Verify type is set correctly
    Option option = OptionBuilder.create();
    Assert.assertEquals(Integer.class, option.getType());
  }

  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("Test Description");
    // Verify description is set correctly
    Option option = OptionBuilder.create();
    Assert.assertEquals("Test Description", option.getDescription());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateCharWithoutLongOpt() {
    OptionBuilder.create('c');
    // Verify IllegalArgumentException is thrown
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
    // Verify IllegalArgumentException is thrown
  }

  @Test
  public void testCreateChar() {
    OptionBuilder.withLongOpt("testOption");
    Option option = OptionBuilder.create('c');
    // Verify Option properties are set correctly
    Assert.assertEquals('c', option.getOpt());
    Assert.assertEquals("testOption", option.getLongOpt());
  }

  @Test
  public void testCreateString() {
    OptionBuilder.withLongOpt("testOption");
    Option option = OptionBuilder.create("opt");
    // Verify Option properties are set correctly
    Assert.assertEquals("opt", option.getOpt());
    Assert.assertEquals("testOption", option.getLongOpt());
  }

}