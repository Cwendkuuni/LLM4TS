
import org.junit.Test;
import org.apache.commons.cli.Option;
import static org.junit.Assert.*;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    Option option = OptionBuilder.withLongOpt("test").create();
    assertNotNull(option);
    assertEquals("test", option.getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    Option option = OptionBuilder.hasArg().create();
    assertNotNull(option);
    assertTrue(option.hasArg());
  }
  
  @Test
  public void testHasArgWithFalse() {
    Option option = OptionBuilder.hasArg(false).create();
    assertNotNull(option);
    assertFalse(option.hasArg());
  }
  
  @Test
  public void testWithArgName() {
    Option option = OptionBuilder.withArgName("name").create();
    assertNotNull(option);
    assertEquals("name", option.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    Option option = OptionBuilder.isRequired().create();
    assertNotNull(option);
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testWithOptionalArg() {
    Option option = OptionBuilder.hasOptionalArg().create();
    assertNotNull(option);
    assertEquals(1, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    Option option = OptionBuilder.hasOptionalArgs().create();
    assertNotNull(option);
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgsWithNumArgs() {
    Option option = OptionBuilder.hasOptionalArgs(2).create();
    assertNotNull(option);
    assertEquals(2, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testWithType() {
    Option option = OptionBuilder.withType(Integer.class).create();
    assertNotNull(option);
    assertEquals(Integer.class, option.getType());
  }
  
  @Test
  public void testWithDescription() {
    Option option = OptionBuilder.withDescription("description").create();
    assertNotNull(option);
    assertEquals("description", option.getDescription());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }
  
  @Test
  public void testCreateWithCharOpt() {
    Option option = OptionBuilder.create('a');
    assertNotNull(option);
    assertEquals("a", option.getOpt());
  }
  
  @Test
  public void testCreateWithStringOpt() {
    Option option = OptionBuilder.create("test");
    assertNotNull(option);
    assertEquals("test", option.getOpt());
  }
}

