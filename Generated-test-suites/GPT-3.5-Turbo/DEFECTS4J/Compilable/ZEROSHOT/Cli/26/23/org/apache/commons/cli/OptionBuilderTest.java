package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import static org.junit.Assert.*;

public class OptionBuilderTest {
  
  private Option option;

  @Before
  public void setUp() {
    option = null;
  }

  @Test
  public void testWithLongOpt() {
    option = OptionBuilder.withLongOpt("test").create();
    assertEquals("test", option.getLongOpt());
  }

  @Test
  public void testHasArg() {
    option = OptionBuilder.hasArg().create();
    assertEquals(true, option.hasArg());
  }

  @Test
  public void testHasArgWithBoolean() {
    option = OptionBuilder.hasArg(true).create();
    assertEquals(true, option.hasArg());

    option = OptionBuilder.hasArg(false).create();
    assertEquals(false, option.hasArg());
  }

  @Test
  public void testWithArgName() {
    option = OptionBuilder.withArgName("name").create();
    assertEquals("name", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    option = OptionBuilder.isRequired().create();
    assertEquals(true, option.isRequired());
  }

  @Test
  public void testWithOptionalArg() {
    option = OptionBuilder.hasOptionalArg().create();
    assertEquals(true, option.hasOptionalArg());
    assertEquals(1 ,option.getArgs());
  }

  @Test
  public void testWithOptionalArgs() {
    option = OptionBuilder.hasOptionalArgs().create();
    assertEquals(true, option.hasOptionalArg());
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testWithOptionalArgsNum() {
    option = OptionBuilder.hasOptionalArgs(2).create();
    assertEquals(true, option.hasOptionalArg());
    assertEquals(2, option.getArgs());
  }

  @Test
  public void testWithType() {
    option = OptionBuilder.withType(Integer.class).create();
    assertEquals(Integer.class, option.getType());
  }

  @Test
  public void testWithDescription() {
    option = OptionBuilder.withDescription("desc").create();
    assertEquals("desc", option.getDescription());
  }

  @Test
  public void testCreateChar() {
    option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
  }

  @Test
  public void testCreate() {
    option = OptionBuilder.create();
    assertNotNull(option);
  }

  @Test
  public void testCreateString() {
    option = OptionBuilder.create("test");
    assertEquals("test", option.getOpt());
  }

}