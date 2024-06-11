import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test");
        assertNotNull(builder);
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        assertNotNull(builder);
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder builder = OptionBuilder.hasArg(true);
        assertNotNull(builder);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("name");
        assertNotNull(builder);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        assertNotNull(builder);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("description");
        assertNotNull(builder);
    }

    @Test
    public void testCreateChar() {
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create('a'));
    }

    @Test
    public void testCreate() {
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create());
    }

    @Test
    public void testCreateString() {
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create("test"));
    }

}