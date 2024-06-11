import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("testOption");
    OptionBuilder optionBuilder = OptionBuilder.create();
    assertNotNull(optionBuilder);
    assertEquals("testOption", optionBuilder.getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    OptionBuilder optionBuilder = OptionBuilder.create();
    assertNotNull(optionBuilder);
    assertEquals(1, optionBuilder.getArgs());
  }
  
  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder.hasArg(true);
    OptionBuilder optionBuilder = OptionBuilder.create();
    assertNotNull(optionBuilder);
    assertEquals(1, optionBuilder.getArgs());
    
    OptionBuilder.hasArg(false);
    optionBuilder = OptionBuilder.create();
    assertNotNull(optionBuilder);
    assertEquals(-1, optionBuilder.getArgs());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("testName");
    OptionBuilder optionBuilder = OptionBuilder.create();
    assertNotNull(optionBuilder);
    assertEquals("testName", optionBuilder.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    OptionBuilder optionBuilder = OptionBuilder.create();
    assertNotNull(optionBuilder);
    assertTrue(optionBuilder.isRequired());
  }
  
  @Test
  public void testWithOptionalArg() {
    OptionBuilder.hasOptionalArg();
    OptionBuilder optionBuilder = OptionBuilder.create();
    assertNotNull(optionBuilder);
    assertTrue(optionBuilder.hasOptionalArg());
  }
  
  @Test
  public void testWithOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    OptionBuilder optionBuilder = OptionBuilder.create();
    assertNotNull(optionBuilder);
    assertTrue(optionBuilder.hasOptionalArgs());
  }
  
  @Test
  public void testWithOptionalArgsWithNum() {
    OptionBuilder.hasOptionalArgs(2);
    OptionBuilder optionBuilder = OptionBuilder.create();
    assertNotNull(optionBuilder);
    assertTrue(optionBuilder.hasOptionalArgs());
    assertEquals(2, optionBuilder.getArgs());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("testDescription");
    OptionBuilder optionBuilder = OptionBuilder.create();
    assertNotNull(optionBuilder);
    assertEquals("testDescription", optionBuilder.getDescription());
  }
  
  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('t');
    assertNotNull(option);
    assertEquals('t', option.getOpt());
  }
  
  @Test
  public void testCreate() {
    Option option = OptionBuilder.create();
    assertNull(option.getOpt());
    assertNotNull(option.getLongOpt());
  }
  
  @Test
  public void testCreateWithString() {
    Option option = OptionBuilder.create("testOption");
    assertNotNull(option);
    assertEquals("testOption", option.getOpt());
  }
}