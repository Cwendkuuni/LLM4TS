import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt_typical() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        assertEquals("longopt", optionBuilder.getLongopt());
    }

    @Test
    public void testWithLongOpt_edge() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
        assertEquals("", optionBuilder.getLongopt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt() {
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_emptyLongOpt() {
        OptionBuilder.withLongOpt("").create();
    }

    @Test
    public void testCreate_typical() {
        Option option = OptionBuilder.withLongOpt("longopt")
                .hasArg()
                .withArgName("argname")
                .create();
        assertEquals("longopt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("argname", option.getArgName());
    }

    @Test
    public void testCreate_noArgName() {
        Option option = OptionBuilder.withLongOpt("longopt")
                .hasArg()
                .create();
        assertEquals("longopt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertNull(option.getArgName());
    }

    @Test
    public void testIsRequired_typical() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testIsRequired_noRequired() {
        OptionBuilder optionBuilder = OptionBuilder.create();
        assertFalse(optionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator_typical() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValuesep());
    }

    @Test
    public void testWithValueSeparator_noSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.create();
        assertEquals(0, optionBuilder.getValuesep());
    }

    @Test
    public void testWithDescription_typical() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertEquals("description", optionBuilder.getDescription());
    }

    @Test
    public void testWithDescription_noDescription() {
        OptionBuilder optionBuilder = OptionBuilder.create();
        assertNull(optionBuilder.getDescription());
    }

    @Test
    public void testCreate_charOption_typical() {
        Option option = OptionBuilder.create('o');
        assertEquals("o", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_charOption_noLongOpt() {
        OptionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_emptyCharOption() {
        OptionBuilder.withLongOpt("longopt").create('');
    }

    @Test
    public void testCreate_emptyStringOption() {
        Option option = OptionBuilder.withLongOpt("longopt").create("");
        assertEquals("", option.getOpt());
    }

}