package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    Option option;

    @Before
    public void setUp() {
        option = null;
    }

    @Test
    public void createOptionWithChar() {
        option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void createOptionWithNullChar() {
        option = OptionBuilder.create(null);
        assertNotNull(option);
        assertNull(option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOptionWithoutLongOpt() {
        option = OptionBuilder.create();
    }

    @Test
    public void createOptionWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        option = OptionBuilder.create();
        assertNotNull(option);
        assertNull(option.getOpt());
        assertEquals("longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void hasArg() {
        option = OptionBuilder.hasArg().create();
        assertNotNull(option);
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void withArgName() {
        OptionBuilder.withArgName("argName");
        option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void isRequired() {
        option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    @Test
    public void withValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void withValueSeparator() {
        OptionBuilder.withValueSeparator();
        option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals('=', option.getValueSeparator());
    }
    
    @Test
    public void isRequiredBoolean() {
        OptionBuilder.isRequired(true);
        option = OptionBuilder.create();
        assertTrue(option.isRequired());
        
        OptionBuilder.isRequired(false);
        option = OptionBuilder.create();
        assertFalse(option.isRequired());
    }
    
    @Test
    public void hasArgs() {
        option = OptionBuilder.hasArgs().create();
        assertNotNull(option);
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void hasArgsInt() {
        option = OptionBuilder.hasArgs(2).create();
        assertNotNull(option);
        assertEquals(2, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void hasOptionalArg() {
        option = OptionBuilder.hasOptionalArg().create();
        assertNotNull(option);
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void hasOptionalArgs() {
        option = OptionBuilder.hasOptionalArgs().create();
        assertNotNull(option);
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void hasOptionalArgsInt() {
        option = OptionBuilder.hasOptionalArgs(2).create();
        assertNotNull(option);
        assertEquals(2, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void withType() {
        OptionBuilder.withType(Integer.class);
        option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals(Integer.class, option.getType());
    }
    
    @Test
    public void withDescription() {
        OptionBuilder.withDescription("description");
        option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("description", option.getDescription());
    }
}