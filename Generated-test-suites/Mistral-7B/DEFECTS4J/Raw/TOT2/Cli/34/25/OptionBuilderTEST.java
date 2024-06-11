import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionBuilderTest {
    // Test for withLongOpt method
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptName");
        Option option = optionBuilder.create('L');
        assert (option.getLongOpt().equals("longOptName"));
    }

    // Test for hasArg method with required argument
    @Test
    public void testHasArgWithRequired() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('A');
        option.setValue("argValue");
        assertFalse(option.isRequired());
        assertTrue(option.hasArgValue());
    }

    // Test for hasArg method with optional argument
    @Test
    public void testHasArgWithOptional() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('O');
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
    }

    // Test for isRequired method
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('R');
        assertTrue(option.isRequired());
        assertFalse(option.hasOptionalArg());
    }

    // Test for hasArgs method with unlimited number of arguments
    @Test
    public void testHasArgsWithUnlimited() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('U');
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        option.parseArgument(args);
        assertEquals(3, option.getArgs().length);
    }

    // Test for create method with required argument and no long opt set
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMissingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        optionBuilder.create('M');
    }
}