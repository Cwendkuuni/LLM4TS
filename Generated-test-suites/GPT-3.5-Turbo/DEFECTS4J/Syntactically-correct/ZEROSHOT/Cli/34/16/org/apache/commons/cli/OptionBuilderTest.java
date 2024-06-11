import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder result = OptionBuilder.withLongOpt("testLongOpt");
    assertNotNull(result);
    assertEquals("testLongOpt", OptionBuilder.getLongopt());
  }

  @Test
  public void testHasArg() {
    OptionBuilder result = OptionBuilder.hasArg();
    assertNotNull(result);
    assertEquals(1, OptionBuilder.getNumberOfArgs());
  }

  @Test
  public void testHasArgBoolean() {
    OptionBuilder result = OptionBuilder.hasArg(true);
    assertNotNull(result);
    assertEquals(1, OptionBuilder.getNumberOfArgs());
    result = OptionBuilder.hasArg(false);
    assertNotNull(result);
    assertEquals(-1, OptionBuilder.getNumberOfArgs());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder result = OptionBuilder.withArgName("testArgName");
    assertNotNull(result);
    assertEquals("testArgName", OptionBuilder.getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder result = OptionBuilder.isRequired();
    assertNotNull(result);
    assertEquals(true, OptionBuilder.isRequired());
  }

  @Test
  public void testWithValueSeparator() {
    OptionBuilder result = OptionBuilder.withValueSeparator(':');
    assertNotNull(result);
    assertEquals(':', OptionBuilder.getValuesep());
  }

  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder result = OptionBuilder.withValueSeparator();
    assertNotNull(result);
    assertEquals('=', OptionBuilder.getValuesep());
  }

  @Test
  public void testIsRequiredBoolean() {
    OptionBuilder result = OptionBuilder.isRequired(true);
    assertNotNull(result);
    assertEquals(true, OptionBuilder.isRequired());
    result = OptionBuilder.isRequired(false);
    assertNotNull(result);
    assertEquals(false, OptionBuilder.isRequired());
  }

  @Test
  public void testHasArgs() {
    OptionBuilder result = OptionBuilder.hasArgs();
    assertNotNull(result);
    assertEquals(Integer.MAX_VALUE, OptionBuilder.getNumberOfArgs());
  }

  @Test
  public void testHasArgsInt() {
    OptionBuilder result = OptionBuilder.hasArgs(5);
    assertNotNull(result);
    assertEquals(5, OptionBuilder.getNumberOfArgs());
  }

  @Test
  public void testHasOptionalArg() {
    OptionBuilder result = OptionBuilder.hasOptionalArg();
    assertNotNull(result);
    assertEquals(1, OptionBuilder.getNumberOfArgs());
    assertEquals(true, OptionBuilder.isOptionalArg());
  }

  @Test
  public void testHasOptionalArgs() {
    OptionBuilder result = OptionBuilder.hasOptionalArgs();
    assertNotNull(result);
    assertEquals(Integer.MAX_VALUE, OptionBuilder.getNumberOfArgs());
    assertEquals(true, OptionBuilder.isOptionalArg());
  }

  @Test
  public void testHasOptionalArgsInt() {
    OptionBuilder result = OptionBuilder.hasOptionalArgs(5);
    assertNotNull(result);
    assertEquals(5, OptionBuilder.getNumberOfArgs());
    assertEquals(true, OptionBuilder.isOptionalArg());
  }

  @Test
  public void testWithType() {
    OptionBuilder result = OptionBuilder.withType(String.class);
    assertNotNull(result);
    assertEquals(String.class, OptionBuilder.getType());
  }

  @Test
  public void testWithDescription() {
    OptionBuilder result = OptionBuilder.withDescription("testDescription");
    assertNotNull(result);
    assertEquals("testDescription", OptionBuilder.getDescription());
  }

  @Test
  public void testCreateChar() {
    Option result = OptionBuilder.create('t');
    assertNotNull(result);
    assertEquals("t", result.getOpt());
  }

  @Test
  public void testCreate() {
    OptionBuilder.withLongOpt("testLongOpt");
    Option result = OptionBuilder.create();
    assertNotNull(result);
    assertEquals("testLongOpt", result.getLongOpt());
  }

  @Test
  public void testCreateString() {
    Option result = OptionBuilder.create("testOpt");
    assertNotNull(result);
    assertEquals("testOpt", result.getOpt());
  }
}