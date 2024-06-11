import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
    Assert.assertEquals("testLongOpt", optionBuilder.create().getLongOpt());
  }

  @Test
  public void testHasArg() {
    OptionBuilder optionBuilder = OptionBuilder.hasArg();
    Assert.assertEquals(1, optionBuilder.create().getArgs());
  }

  @Test
  public void testHasArgBoolean() {
    OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
    Assert.assertEquals(1, optionBuilder.create().getArgs());
    optionBuilder = OptionBuilder.hasArg(false);
    Assert.assertEquals(0, optionBuilder.create().getArgs());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
    Assert.assertEquals("testArgName", optionBuilder.create().getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder optionBuilder = OptionBuilder.isRequired();
    Assert.assertTrue(optionBuilder.create().isRequired());
  }

  @Test
  public void testWithValueSeparator() {
    OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
    Assert.assertEquals(':', optionBuilder.create().getValueSeparator());
  }

  @Test
  public void testWithValueSeparatorDefault() {
    OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
    Assert.assertEquals('=', optionBuilder.create().getValueSeparator());
  }

  @Test
  public void testIsRequiredBoolean() {
    OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
    Assert.assertTrue(optionBuilder.create().isRequired());
    optionBuilder = OptionBuilder.isRequired(false);
    Assert.assertFalse(optionBuilder.create().isRequired());
  }

  @Test
  public void testHasArgs() {
    OptionBuilder optionBuilder = OptionBuilder.hasArgs();
    Assert.assertEquals(-1, optionBuilder.create().getArgs());
  }

  @Test
  public void testHasArgsNumber() {
    OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
    Assert.assertEquals(3, optionBuilder.create().getArgs());
  }

  @Test
  public void testHasOptionalArg() {
    OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
    Assert.assertTrue(optionBuilder.create().hasOptionalArg());
    Assert.assertEquals(1, optionBuilder.create().getArgs());
  }

  @Test
  public void testHasOptionalArgs() {
    OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
    Assert.assertTrue(optionBuilder.create().hasOptionalArg());
    Assert.assertEquals(-1, optionBuilder.create().getArgs());
  }

  @Test
  public void testHasOptionalArgsNumber() {
    OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
    Assert.assertTrue(optionBuilder.create().hasOptionalArg());
    Assert.assertEquals(2, optionBuilder.create().getArgs());
  }

  @Test
  public void testWithType() {
    OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
    Assert.assertEquals(String.class, optionBuilder.create().getType());
  }

  @Test
  public void testWithDescription() {
    OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
    Assert.assertEquals("testDescription", optionBuilder.create().getDescription());
  }

  @Test
  public void testCreateChar() {
    OptionBuilder optionBuilder = OptionBuilder.create('c');
    Assert.assertEquals("c", optionBuilder.create().getOpt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullLongOpt() {
    OptionBuilder optionBuilder = OptionBuilder.create();
    optionBuilder.create().getLongOpt();
  }

  @Test
  public void testCreate() {
    OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
    Assert.assertEquals("testLongOpt", optionBuilder.create().getLongOpt());
  }

  @Test
  public void testCreateString() {
    OptionBuilder optionBuilder = OptionBuilder.create("testOpt");
    Assert.assertEquals("testOpt", optionBuilder.create().getOpt());
  }

}