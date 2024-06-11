import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Before
  public void setUp() {

  }

  @Test
  public void testWithLongOpt() {
    OptionBuilder builder = OptionBuilder.withLongOpt("testLongOpt");
    assertEquals("testLongOpt", builder.longopt);
  }

  @Test
  public void testHasArg() {
    OptionBuilder builder = OptionBuilder.hasArg();
    assertEquals(1, builder.numberOfArgs);
  }

  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder builder = OptionBuilder.hasArg(true);
    assertEquals(1, builder.numberOfArgs);

    builder = OptionBuilder.hasArg(false);
    assertEquals(Option.UNINITIALIZED, builder.numberOfArgs);
  }

  @Test
  public void testWithArgName() {
    OptionBuilder builder = OptionBuilder.withArgName("testArgName");
    assertEquals("testArgName", builder.argName);
  }

  @Test
  public void testIsRequired() {
    OptionBuilder builder = OptionBuilder.isRequired();
    assertTrue(builder.required);
  }

  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder builder = OptionBuilder.withValueSeparator(':');
    assertEquals(':', builder.valuesep);
  }

  @Test
  public void testWithValueSeparator() {
    OptionBuilder builder = OptionBuilder.withValueSeparator();
    assertEquals('=', builder.valuesep);
  }

  @Test
  public void testIsRequiredBoolean() {
    OptionBuilder builder = OptionBuilder.isRequired(true);
    assertTrue(builder.required);

    builder = OptionBuilder.isRequired(false);
    assertFalse(builder.required);
  }

  @Test
  public void testHasArgs() {
    OptionBuilder builder = OptionBuilder.hasArgs();
    assertEquals(Option.UNLIMITED_VALUES, builder.numberOfArgs);
  }

  @Test
  public void testHasArgsInt() {
    OptionBuilder builder = OptionBuilder.hasArgs(2);
    assertEquals(2, builder.numberOfArgs);
  }

  @Test
  public void testHasOptionalArg() {
    OptionBuilder builder = OptionBuilder.hasOptionalArg();
    assertTrue(builder.optionalArg);
    assertEquals(1, builder.numberOfArgs);
  }

  @Test
  public void testHasOptionalArgs() {
    OptionBuilder builder = OptionBuilder.hasOptionalArgs();
    assertTrue(builder.optionalArg);
    assertEquals(Option.UNLIMITED_VALUES, builder.numberOfArgs);
  }

  @Test
  public void testHasOptionalArgsInt() {
    OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
    assertTrue(builder.optionalArg);
    assertEquals(3, builder.numberOfArgs);
  }

  @Test
  public void testWithType() {
    OptionBuilder builder = OptionBuilder.withType(Integer.class);
    assertEquals(Integer.class, builder.type);
  }

  @Test
  public void testWithDescription() {
    OptionBuilder builder = OptionBuilder.withDescription("testDescription");
    assertEquals("testDescription", builder.description);
  }

  @Test
  public void testCreateChar() {
    OptionBuilder builder = OptionBuilder.create('t');
    assertNotNull(builder);
    assertEquals("t", builder.longopt);
  }

  @Test
  public void testCreate() {
    OptionBuilder builder = OptionBuilder.create();
    assertNotNull(builder);
    assertNull(builder.longopt);
  }

  @Test
  public void testCreateString() {
    OptionBuilder builder = OptionBuilder.create("testOption");
    assertNotNull(builder);
    assertEquals("testOption", builder.longopt);
  }

}