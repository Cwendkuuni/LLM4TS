package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
       OptionBuilder.withLongOpt("test");
       Option option = OptionBuilder.create();
       assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
       OptionBuilder.hasArg();
       Option option = OptionBuilder.create();
       assertTrue(option.hasArg());
    }

    @Test
    public void testHasArgWithArg() {
       OptionBuilder.hasArg(true);
       Option option = OptionBuilder.create();
       assertTrue(option.hasArg());
    }

    @Test
    public void testWithArgName() {
       OptionBuilder.withArgName("arg");
       Option option = OptionBuilder.create();
       assertEquals("arg", option.getArgName());
    }

    @Test
    public void testIsRequired() {
       OptionBuilder.isRequired();
       Option option = OptionBuilder.create();
       assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
       OptionBuilder.withValueSeparator(':');
       Option option = OptionBuilder.create('D');
       assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithDefaultValueSeparator() {
       OptionBuilder.withValueSeparator();
       Option option = OptionBuilder.create('D');
       assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsOptionalArg() {
       OptionBuilder.hasOptionalArg();
       Option option = OptionBuilder.create();
       assertTrue(option.hasArg());
       assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testIsOptionalArgWithNumArgs() {
       OptionBuilder.hasOptionalArgs(2);
       Option option = OptionBuilder.create();
       assertTrue(option.hasArg());
       assertEquals(2, option.getArgs());
    }

    @Test
    public void testWithType() {
       OptionBuilder.withType(String.class);
       Option option = OptionBuilder.create();
       assertEquals(String.class, option.getType());
    }

    @Test
    public void testWithDescription() {
       OptionBuilder.withDescription("test");
       Option option = OptionBuilder.create();
       assertEquals("test", option.getDescription());
    }

    @Test
    public void testCreateWithChar() {
       Option option = OptionBuilder.create('t');
       assertEquals("t", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullChar() {
       OptionBuilder.create((char) 0);
    }

    @Test
    public void testCreate() {
       OptionBuilder.withLongOpt("test");
       Option option = OptionBuilder.create();
       assertEquals("test", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() {
       OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
       Option option = OptionBuilder.create("t");
       assertEquals("t", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullString() {
       OptionBuilder.create(null);
    }
}