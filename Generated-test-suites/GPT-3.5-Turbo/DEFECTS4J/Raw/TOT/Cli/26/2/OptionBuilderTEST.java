import org.junit.Test;
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
    assertEquals(1, option.getArgs());
  }
  
  @Test
  public void testHasArgWithArg() {
    Option option = OptionBuilder.hasArg(true).create();
    
    assertNotNull(option);
    assertTrue(option.hasArg());
    assertEquals(1, option.getArgs());
  }
  
  @Test
  public void testHasArgWithoutArg() {
    Option option = OptionBuilder.hasArg(false).create();
    
    assertNotNull(option);
    assertFalse(option.hasArg());
    assertEquals(Option.UNINITIALIZED, option.getArgs());
  }
  
  @Test
  public void testWithArgName() {
    Option option = OptionBuilder.withArgName("test").create();
    
    assertNotNull(option);
    assertEquals("test", option.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    Option option = OptionBuilder.isRequired().create();
    
    assertNotNull(option);
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testWithDescription() {
    Option option = OptionBuilder.withDescription("test").create();
    
    assertNotNull(option);
    assertEquals("test", option.getDescription());
  }
  
  @Test
  public void testWithValueSeparator() {
    Option option = OptionBuilder.withValueSeparator(':').create();
    
    assertNotNull(option);
    assertEquals(':', option.getValueSeparator());
  }
  
  @Test
  public void testWithDefaultValueSeparator() {
    Option option = OptionBuilder.withValueSeparator().create();
    
    assertNotNull(option);
    assertEquals('=', option.getValueSeparator());
  }
  
  @Test
  public void testIsRequiredWithRequired() {
    Option option = OptionBuilder.isRequired(true).create();
    
    assertNotNull(option);
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testHasArgs() {
    Option option = OptionBuilder.hasArgs().create();
    
    assertNotNull(option);
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }
  
  @Test
  public void testHasArgsWithNum() {
    Option option = OptionBuilder.hasArgs(2).create();
    
    assertNotNull(option);
    assertEquals(2, option.getArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    Option option = OptionBuilder.hasOptionalArg().create();
    
    assertNotNull(option);
    assertTrue(option.hasOptionalArg());
    assertEquals(1, option.getArgs());
  }
  
  @Test
  public void testHasOptionalArgs() {
    Option option = OptionBuilder.hasOptionalArgs().create();
    
    assertNotNull(option);
    assertTrue(option.hasOptionalArg());
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }
  
  @Test
  public void testHasOptionalArgsWithNum() {
    Option option = OptionBuilder.hasOptionalArgs(2).create();
    
    assertNotNull(option);
    assertTrue(option.hasOptionalArg());
    assertEquals(2, option.getArgs());
  }
  
  @Test
  public void testWithType() {
    Option option = OptionBuilder.withType(Integer.class).create();
    
    assertNotNull(option);
    assertEquals(Integer.class, option.getType());
  }
}