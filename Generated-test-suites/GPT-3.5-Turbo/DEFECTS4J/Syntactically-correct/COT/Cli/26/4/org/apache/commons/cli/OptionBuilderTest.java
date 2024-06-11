import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.getOption().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getOption().getArgs());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getOption().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        assertEquals("name", OptionBuilder.getOption().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.getOption().isRequired());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.getOption().hasOptionalArg());
    }

    @Test
    public void testWithOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getOption().getArgs());
    }

    @Test
    public void testWithOptionalArgsNum() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.getOption().getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.getOption().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.getOption().getDescription());
    }

}