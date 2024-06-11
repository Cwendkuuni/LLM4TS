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
  public void testHasArgWithBoolean() {
    OptionBuilder.hasArg(true);
    assertEquals(1, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasArgWithFalseBoolean() {
    OptionBuilder.hasArg(false);
    assertEquals(-1, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("name");
    assertEquals("name", OptionBuilder.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    assertTrue(OptionBuilder.isRequired());
  }
  
  @Test
  public void testWithSeparatorChar() {
    OptionBuilder.withValueSeparator(':');
    assertEquals(':', OptionBuilder.getValuesep());
  }
  
  @Test
  public void testWithSeparator() {
    OptionBuilder.withValueSeparator();
    assertEquals('=', OptionBuilder.getValuesep());
  }
  
  @Test
  public void testIsRequiredWithBoolean() {
    OptionBuilder.isRequired(true);
    assertTrue(OptionBuilder.isRequired());
  }
  
  @Test
  public void testIsRequiredWithFalseBoolean() {
    OptionBuilder.isRequired(false);
    assertFalse(OptionBuilder.isRequired());
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    assertEquals(-2, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasArgsWithInt() {
    OptionBuilder.hasArgs(3);
    assertEquals(3, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
    assertEquals(1, OptionBuilder.getNumberOfArgs());
    assertTrue(OptionBuilder.isOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    assertEquals(-2, OptionBuilder.getNumberOfArgs());
    assertTrue(OptionBuilder.isOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgsWithInt() {
    OptionBuilder.hasOptionalArgs(3);
    assertEquals(3, OptionBuilder.getNumberOfArgs());
    assertTrue(OptionBuilder.isOptionalArg());
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(Object.class);
    assertEquals(Object.class, OptionBuilder.getType());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("description");
    assertEquals("description", OptionBuilder.getDescription());
  }
  
  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('c');
    assertEquals('c', option.getOpt());
  }
  
  @Test
  public void testCreate() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create();
    assertEquals("test", option.getLongOpt());
  }
  
  @Test
  public void testCreateWithString() {
    Option option = OptionBuilder.create("opt");
    assertEquals("opt", option.getOpt());
  }
}