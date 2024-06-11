import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create();
        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertTrue(option.hasArg());
    }

    @Test
    public void testHasArgWithType() {
        Object type = new Object();
        Option option = OptionBuilder.hasArg().withType(type).create();
        assertEquals(type, option.getType());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("testArgName").create();
        assertEquals("testArgName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription() {
        String description = "testDescription";
        Option option = OptionBuilder.withDescription(description).create();
        assertEquals(description, option.getDescription());
    }

    @Test
    public void testCreateWithOpt() {
        char opt = 't';
        Option option = OptionBuilder.create(opt);
        assertEquals(opt, option.getOpt());
    }

    @Test
    public void testCreateWithLongOpt() {
        String longOpt = "testLongOpt";
        Option option = OptionBuilder.withLongOpt(longOpt).create();
        assertEquals(longOpt, option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
}