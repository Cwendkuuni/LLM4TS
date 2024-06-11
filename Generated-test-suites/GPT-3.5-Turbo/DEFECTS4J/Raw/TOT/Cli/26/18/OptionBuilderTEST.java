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
        Option option = optionBuilder.withLongOpt("testLongOpt").create();
        assertNotNull(option);
        assertEquals("testLongOpt", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = optionBuilder.hasArg().create();
        assertNotNull(option);
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        Option option1 = optionBuilder.hasArg(true).create();
        assertNotNull(option1);
        assertTrue(option1.hasArg());
        
        Option option2 = optionBuilder.hasArg(false).create();
        assertNotNull(option2);
        assertFalse(option2.hasArg());
    }
    
    @Test
    public void testWithArgName() {
        Option option = optionBuilder.withArgName("testArg").create();
        assertNotNull(option);
        assertEquals("testArg", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        Option option = optionBuilder.isRequired().create();
        assertNotNull(option);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithOptionalArg() {
        Option option = optionBuilder.hasOptionalArg().create();
        assertNotNull(option);
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgs() {
        Option option = optionBuilder.hasOptionalArgs().create();
        assertNotNull(option);
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testWithOptionalArgsWithNum() {
        Option option = optionBuilder.hasOptionalArgs(2).create();
        assertNotNull(option);
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testWithType() {
        Option option = optionBuilder.withType(String.class).create();
        assertNotNull(option);
        assertEquals(String.class, option.getType());
    }
    
    @Test
    public void testWithDescription() {
        Option option = optionBuilder.withDescription("test description").create();
        assertNotNull(option);
        assertEquals("test description", option.getDescription());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoLongOpt() {
        optionBuilder.create();
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = optionBuilder.create('t');
        assertNotNull(option);
        assertEquals('t', option.getOpt().charAt(0));
    }
    
    @Test
    public void testCreateWithString() {
        Option option = optionBuilder.create("test");
        assertNotNull(option);
        assertEquals("test", option.getOpt());
    }
}