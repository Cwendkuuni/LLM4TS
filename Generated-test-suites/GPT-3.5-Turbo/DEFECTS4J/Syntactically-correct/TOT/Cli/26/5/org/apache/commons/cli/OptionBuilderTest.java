import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    OptionBuilder optionBuilder;

    @Before
    public void setUp() {
        optionBuilder = new OptionBuilder();
    }

    @Test
    public void testWithLongOpt_shouldSetLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg_shouldSetNumberOfArgsToOne() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }
    
    @Test
    public void testHasArg_withTrue_shouldSetNumberOfArgsToOne() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArg_withFalse_shouldSetNumberOfArgsToUninitialized() {
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName_shouldSetArgName() {
        OptionBuilder.withArgName("test");
        assertEquals("test", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired_shouldSetRequiredToTrue() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValuesSeparator_shouldSetValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValuesSeparator_withoutParameter_shouldSetValueSeparatorToEquals() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequired_withTrue_shouldSetRequiredToTrue() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testIsRequired_withFalse_shouldSetRequiredToFalse() {
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs_shouldSetNumberOfArgsToUnlimited() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgs_withNumber_shouldSetNumberOfArgsToNumber() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg_shouldSetNumberOfArgsToOneAndOptionalArgToTrue() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_shouldSetNumberOfArgsToUnlimitedAndOptionalArgToTrue() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_withNumber_shouldSetNumberOfArgsToNumberAndOptionalArgToTrue() {
        OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType_shouldSetType() {
        Object obj = new Object();
        OptionBuilder.withType(obj);
        assertEquals(obj, OptionBuilder.type);
    }

    @Test
    public void testWithDescription_shouldSetDescription() {
        OptionBuilder.withDescription("test");
        assertEquals("test", OptionBuilder.description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withInvalidOptChar_shouldThrowIllegalArgumentException() {
        OptionBuilder.create('#');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withoutLongOpt_shouldThrowIllegalArgumentException() {
        OptionBuilder.create();
    }

    @Test
    public void testCreate_withValidOptChar_andLongOpt() {
        Option option = OptionBuilder.create('t').create("test");
        assertEquals("t", option.getOpt());
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testCreate_withValidOptString_andLongOpt() {
        Option option = OptionBuilder.create("t").create("test");
        assertEquals("t", option.getOpt());
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testCreate_shouldResetOptionBuilderProperties() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.longopt);

        OptionBuilder.create('t');
        assertNull(OptionBuilder.longopt);
    }

}