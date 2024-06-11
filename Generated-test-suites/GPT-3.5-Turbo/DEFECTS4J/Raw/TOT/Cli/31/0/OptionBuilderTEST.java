import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
      OptionBuilder.withLongOpt("longOpt");
      Option opt = OptionBuilder.create();
      assertEquals("longOpt", opt.getLongOpt());
  }

  @Test
  public void testHasArg() {
      OptionBuilder.hasArg();
      Option opt = OptionBuilder.create('a');
      assertTrue(opt.hasArg());
  }

  @Test
  public void testHasArgWithFalse() {
      OptionBuilder.hasArg(false);
      Option opt = OptionBuilder.create('a');
      assertFalse(opt.hasArg());
  }

  @Test
  public void testWithArgName() {
      OptionBuilder.withArgName("name");
      Option opt = OptionBuilder.create();
      assertEquals("name", opt.getArgName());
  }

  @Test
  public void testIsRequired() {
      OptionBuilder.isRequired();
      Option opt = OptionBuilder.create();
      assertTrue(opt.isRequired());

  }

  @Test
  public void testWithMultipleArgs() {
      OptionBuilder.hasArgs(2);
      Option opt = OptionBuilder.create();
      assertEquals(2, opt.getArgs());
  }

  @Test
  public void testHasOptionalArg() {
      OptionBuilder.hasOptionalArg();
      Option opt = OptionBuilder.create();
      assertTrue(opt.hasArg());
      assertTrue(opt.getOptionalArg());
  }

  @Test
  public void testWithValueSeparator() {
      OptionBuilder.withValueSeparator(':');
      Option opt = OptionBuilder.create();
      assertEquals(':', opt.getValueSeparator());
  }

  @Test
  public void testWithMultipleOptionalArgs() {
      OptionBuilder.hasOptionalArgs(2);
      Option opt = OptionBuilder.create();
      assertEquals(2, opt.getArgs());
      assertTrue(opt.getOptionalArgs());
  }

  @Test
  public void testWithType() {
      OptionBuilder.withType(Integer.class);
      Option opt = OptionBuilder.create();
      assertEquals(Integer.class, opt.getType());
  }

  @Test
  public void testWithDescription() {
      OptionBuilder.withDescription("description");
      Option opt = OptionBuilder.create();
      assertEquals("description", opt.getDescription());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
      OptionBuilder.create();
  }
}