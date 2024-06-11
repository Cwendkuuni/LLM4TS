
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder builder = OptionBuilder.withLongOpt("test");
    assertEquals("test", OptionBuilder.getLongOpt());
  }

  @Test
  public void testHasArg() {
    OptionBuilder builder = OptionBuilder.hasArg();
    assertEquals(1, OptionBuilder.getNumberOfArgs());
  }

  @Test
  public void testHasArgWithArgument() {
    OptionBuilder builder = OptionBuilder.hasArg(true);
    assertEquals(1, OptionBuilder.getNumberOfArgs());
    
    builder = OptionBuilder.hasArg(false);
    assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder builder = OptionBuilder.withArgName("test");
    assertEquals("test", OptionBuilder.getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder builder = OptionBuilder.isRequired();
    assertTrue(OptionBuilder.isRequired());
  }

  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder builder = OptionBuilder.withValueSeparator(':');
    assertEquals(':', OptionBuilder.getValueSeparator());
  }

  @Test
  public void testWithValueSeparatorDefault() {
    OptionBuilder builder = OptionBuilder.withValueSeparator();
    assertEquals('=', OptionBuilder.getValueSeparator());
  }

  @Test
  public void testIsRequiredWithBoolean() {
    OptionBuilder builder = OptionBuilder.isRequired(true);
    assertTrue(OptionBuilder.isRequired());
    
    builder = OptionBuilder.isRequired(false);
    assertFalse(OptionBuilder.isRequired());
  }

  @Test
  public void testHasArgs() {
    OptionBuilder builder = OptionBuilder.hasArgs();
    assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
  }

  @Test
  public void testHasArgsWithNumber() {
    OptionBuilder builder = OptionBuilder.hasArgs(3);
    assertEquals(3, OptionBuilder.getNumberOfArgs());
  }

  @Test
  public void testHasOptionalArg() {
    OptionBuilder builder = OptionBuilder.hasOptionalArg();
    assertEquals(1, OptionBuilder.getNumberOfArgs());
    assertTrue(OptionBuilder.isOptionalArg());
  }

  @Test
  public void testHasOptionalArgs() {
    OptionBuilder builder = OptionBuilder.hasOptionalArgs();
    assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    assertTrue(OptionBuilder.isOptionalArg());
  }

  @Test
  public void testHasOptionalArgsWithNumber() {
    OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
    assertEquals(3, OptionBuilder.getNumberOfArgs());
    assertTrue(OptionBuilder.isOptionalArg());
  }

  @Test
  public void testWithType() {
    Object obj = new Object();
    OptionBuilder builder = OptionBuilder.withType(obj);
    assertEquals(obj, OptionBuilder.getType());
  }

  @Test
  public void testWithDescription() {
    OptionBuilder builder = OptionBuilder.withDescription("test");
    assertEquals("test", OptionBuilder.getDescription());
  }

  @Test
  public void testCreateWithCharacter() {
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
    assertNull(option.getLongOpt());
    assertNull(option.getDescription());
    assertFalse(option.isRequired());
    assertFalse(option.hasOptionalArg());
    assertEquals(Option.UNINITIALIZED, option.getArgs());
    assertNull(option.getType());
    assertEquals((char) 0, option.getValueSeparator());
    assertEquals("arg", option.getArgName());
  }

  @Test
  public void testCreate() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create();
    assertNull(option.getOpt());
    assertEquals("test", option.getLongOpt());
    assertNull(option.getDescription());
    assertFalse(option.isRequired());
    assertFalse(option.hasOptionalArg());
    assertEquals(Option.UNINITIALIZED, option.getArgs());
    assertNull(option.getType());
    assertEquals((char) 0, option.getValueSeparator());
    assertEquals("arg", option.getArgName());
  }

  @Test
  public void testCreateWithString() {
    Option option = OptionBuilder.create("t");
    assertEquals("t", option.getOpt());
    assertNull(option.getLongOpt());
    assertNull(option.getDescription());
    assertFalse(option.isRequired());
    assertFalse(option.hasOptionalArg());
    assertEquals(Option.UNINITIALIZED, option.getArgs());
    assertNull(option.getType());
    assertEquals((char) 0, option.getValueSeparator());
    assertEquals("arg", option.getArgName());
  }

}
