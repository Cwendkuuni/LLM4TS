import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
  
  private OptionBuilder optionBuilder;
  
  @Before
  public void setUp() {
    optionBuilder = new OptionBuilder();
  }
  
  @Test
  public void testWithLongOpt() {
    optionBuilder.withLongOpt("test");
    
    assertEquals("test", optionBuilder.getLongopt());
  }
  
  @Test
  public void testHasArg() {
    optionBuilder.hasArg();
    
    int expectedNumberOfArgs = 1;
    
    assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasArgWithBoolean() {
    optionBuilder.hasArg(true);
    
    int expectedNumberOfArgs = 1;
    
    assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testWithArgName() {
    optionBuilder.withArgName("test");
    
    assertEquals("test", optionBuilder.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    optionBuilder.isRequired();
    
    assertTrue(optionBuilder.isRequired());
  }
  
  @Test
  public void testWithValueSeparator() {
    optionBuilder.withValueSeparator(':');
    
    assertEquals(':', optionBuilder.getValuesep());
  }
  
  @Test
  public void testWithValueSeparatorDefault() {
    optionBuilder.withValueSeparator();
    
    assertEquals('=', optionBuilder.getValuesep());
  }
  
  @Test
  public void testIsRequiredWithBoolean() {
    optionBuilder.isRequired(true);
    
    assertTrue(optionBuilder.isRequired());
  }
  
  @Test
  public void testHasArgs() {
    optionBuilder.hasArgs();
    
    int expectedNumberOfArgs = Option.UNLIMITED_VALUES;
    
    assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasArgsWithNum() {
    optionBuilder.hasArgs(2);
    
    int expectedNumberOfArgs = 2;
    
    assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    optionBuilder.hasOptionalArg();
    
    int expectedNumberOfArgs = 1;
    
    assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
    assertTrue(optionBuilder.isOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    optionBuilder.hasOptionalArgs();
    
    int expectedNumberOfArgs = Option.UNLIMITED_VALUES;
    
    assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
    assertTrue(optionBuilder.isOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgsWithNum() {
    optionBuilder.hasOptionalArgs(2);
    
    int expectedNumberOfArgs = 2;
    
    assertEquals(expectedNumberOfArgs, optionBuilder.getNumberOfArgs());
    assertTrue(optionBuilder.isOptionalArg());
  }
  
  @Test
  public void testWithType() {
    optionBuilder.withType(Object.class);
    
    assertEquals(Object.class, optionBuilder.getType());
  }
  
  @Test
  public void testWithDescription() {
    optionBuilder.withDescription("test");
    
    assertEquals("test", optionBuilder.getDescription());
  }
  
  // Additional test cases for potential edge cases and exception handling scenarios
  
  // Test create method when longOpt is not set
  @Test(expected = IllegalArgumentException.class)
  public void testCreateLongOptNotSet() {
    optionBuilder.create();
  }
  
  // Test create with no arguments
  @Test
  public void testCreateNoArgs() {
    optionBuilder.withLongOpt("test");
    
    Option option = optionBuilder.create();
    
    assertNotNull(option);
    assertEquals("test", option.getLongOpt());
    assertEquals(OptionBuilderTest.class, option.getClass());
  }
  
  // Test create with character option
  @Test
  public void testCreateWithCharOption() {
    Option option = optionBuilder.create('t');
    
    assertNotNull(option);
    assertEquals("t", option.getOpt());
    assertEquals(OptionBuilderTest.class, option.getClass());
  }
  
  // Test create with string option
  @Test
  public void testCreateWithStringOption() {
    Option option = optionBuilder.create("test");
    
    assertNotNull(option);
    assertEquals("test", option.getOpt());
    assertEquals(OptionBuilderTest.class, option.getClass());
  }
}