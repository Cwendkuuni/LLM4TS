import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("test");
    assertEquals("test", OptionBuilder.getLongopt());
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    assertEquals(1, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasArg_withArg() {
    OptionBuilder.hasArg(true);
    assertEquals(1, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasArg_withoutArg() {
    OptionBuilder.hasArg(false);
    assertEquals(-1, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("arg");
    assertEquals("arg", OptionBuilder.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    assertTrue(OptionBuilder.getRequired());
  }
  
  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator(':');
    assertEquals(':', OptionBuilder.getValuesep());
  }
  
  @Test
  public void testWithValueSeparator_default() {
    OptionBuilder.withValueSeparator();
    assertEquals('=', OptionBuilder.getValuesep());
  }
  
  @Test
  public void testIsRequired_true() {
    OptionBuilder.isRequired(true);
    assertTrue(OptionBuilder.getRequired());
  }
  
  @Test
  public void testIsRequired_false() {
    OptionBuilder.isRequired(false);
    assertFalse(OptionBuilder.getRequired());
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasArgs_withNum() {
    OptionBuilder.hasArgs(2);
    assertEquals(2, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
    assertTrue(OptionBuilder.getOptionalArg());
    assertEquals(1, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    assertTrue(OptionBuilder.getOptionalArg());
    assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasOptionalArgs_withNum() {
    OptionBuilder.hasOptionalArgs(3);
    assertTrue(OptionBuilder.getOptionalArg());
    assertEquals(3, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(Object.class);
    assertEquals(Object.class, OptionBuilder.getType());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("test");
    assertEquals("test", OptionBuilder.getDescription());
  }
  
  @Test
  public void testCreate_char() {
    Option option = OptionBuilder.create('t');
    assertNotNull(option);
    assertEquals("t", option.getOpt());
  }
  
  @Test
  public void testCreate() {
    Option option = OptionBuilder.create();
    assertNotNull(option);
    assertNull(option.getOpt());
    assertNotNull(option.getLongOpt());
  }
  
  @Test
  public void testCreate_string() {
    Option option = OptionBuilder.create("test");
    assertNotNull(option);
    assertEquals("test", option.getOpt());
  }
}