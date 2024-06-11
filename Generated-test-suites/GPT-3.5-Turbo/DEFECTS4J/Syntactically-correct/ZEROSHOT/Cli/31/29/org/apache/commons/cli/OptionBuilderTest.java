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
  public void testHasArgWithParam() {
    OptionBuilder.hasArg(true);
    assertEquals(1, OptionBuilder.getNumberOfArgs());

    OptionBuilder.hasArg(false);
    assertEquals(OptionBuilder.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("test");
    assertEquals("test", OptionBuilder.getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    assertTrue(OptionBuilder.isRequired());

    OptionBuilder.isRequired(false);
    assertFalse(OptionBuilder.isRequired());
  }

  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator(':');
    assertEquals(':', OptionBuilder.getValuesep());

    OptionBuilder.withValueSeparator();
    assertEquals('=', OptionBuilder.getValuesep());
  }

  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    assertEquals(OptionBuilder.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
  }

  @Test
  public void testHasArgsWithParam() {
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
    assertEquals(OptionBuilder.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    assertTrue(OptionBuilder.isOptionalArg());
  }

  @Test
  public void testHasOptionalArgsWithParam() {
    OptionBuilder.hasOptionalArgs(2);
    assertEquals(2, OptionBuilder.getNumberOfArgs());
    assertTrue(OptionBuilder.isOptionalArg());
  }

  @Test
  public void testWithType() {
    Object type = new Object();
    OptionBuilder.withType(type);
    assertEquals(type, OptionBuilder.getType());
  }

  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("test");
    assertEquals("test", OptionBuilder.getDescription());
  }

  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithCharNullLongOpt() {
    OptionBuilder.create();
  }

  @Test
  public void testCreateWithCharAndLongOpt() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
    assertEquals("test", option.getLongOpt());
  }

  private void assertEquals(char sep, char valuesep) {
    assertEquals(sep, valuesep);
  }

  private void assertEquals(int expected, int actual) {
    assertEquals(expected, actual);
  }

  private void assertFalse(boolean bool) {
    assertFalse(bool);
  }

  private void assertTrue(boolean bool) {
    assertTrue(bool);
  }

}