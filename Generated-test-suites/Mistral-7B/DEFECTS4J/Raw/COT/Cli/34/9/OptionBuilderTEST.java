import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        String longOpt = optionBuilder.getLongOpt();
        assertEquals("optionLongName", longOpt);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithLongOpt_whenCalledMultipleTimesWithoutReset_shouldThrowException() {
        OptionBuilder.withLongOpt("optionLongName1");
        OptionBuilder.withLongOpt("optionLongName2");
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(1, numberOfArgs);
    }

    @Test
    public void testHasArg_whenCalledMultipleTimesWithoutReset_shouldReturnSameInstance() {
        OptionBuilder optionBuilder1 = OptionBuilder.hasArg();
        OptionBuilder optionBuilder2 = OptionBuilder.hasArg();
        assertEquals(optionBuilder1, optionBuilder2);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        boolean required = optionBuilder.isRequired();
        assertTrue(required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        char valueSeparator = optionBuilder.getValueSeparator();
        assertEquals(':', valueSeparator);
    }

    @Test
    public void testWithValueSeparator_whenCalledMultipleTimesWithoutReset_shouldReturnSameInstance() {
        OptionBuilder optionBuilder1 = OptionBuilder.withValueSeparator(':');
        OptionBuilder optionBuilder2 = OptionBuilder.withValueSeparator(':');
        assertEquals(optionBuilder1, optionBuilder2);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(Option.UNLIMITED_VALUES, numberOfArgs);
    }

    @Test
    public void testHasArgs_whenCalledMultipleTimesWithoutReset_shouldReturnSameInstance() {
        OptionBuilder optionBuilder1 = OptionBuilder.hasArgs();
        OptionBuilder optionBuilder2 = OptionBuilder.hasArgs();
        assertEquals(optionBuilder1, optionBuilder2);
    }

    @Test
    public void testHasArgs_withSpecifiedNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(3, numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        boolean optionalArg = optionBuilder.isOptionalArg();
        assertTrue(optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(Option.UNLIMITED_VALUES, numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs_withSpecifiedNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(2, numberOfArgs);
    }

    @Test
    public void testWithType() {
        Class type = String.class;
        OptionBuilder optionBuilder = OptionBuilder.withType(type);
        Class optionType = optionBuilder.getType();
        assertEquals(type, optionType);
    }

    @Test
    public void testWithDescription() {
        String description = "Option description";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        String optionDescription = optionBuilder.getDescription();
        assertEquals(description, optionDescription);
    }

    @Test
    public void testCreate_whenCalledWithValidSettings_shouldReturnOptionInstance() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName")
                .withDescription("Option description")
                .isRequired()
                .hasArg();
        Option option = optionBuilder.create('o');
        assertNotNull(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_whenCalledWithoutLongOpt_shouldThrowException() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        optionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_whenCalledWithoutLongOpt_whenCalledMultipleTimes_shouldThrowException() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        optionBuilder.create('o');
        optionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_whenCalledWithInvalidLongOpt_shouldThrowException() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("opt")
                .isRequired()
                .hasArg();
        optionBuilder.create('x');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_whenCalledWithInvalidLongOpt_whenCalledMultipleTimes_shouldThrowException() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("opt")
                .isRequired()
                .hasArg();
        optionBuilder.create('x');
        optionBuilder.create('x');
    }

    @Test
    public void testCreate_whenCalledWithValidSettingsAndOptionalArg_shouldReturnOptionInstanceWithOptionalArg() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName")
                .isOptionalArg()
                .hasOptionalArg();
        Option option = optionBuilder.create('o');
        assertTrue(option.isOptionalArg());
    }

    @Test
    public void testCreate_whenCalledWithValidSettingsAndOptionalArgs_shouldReturnOptionInstanceWithOptionalArgs() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName")
                .isOptionalArg()
                .hasOptionalArgs(2);
        Option option = optionBuilder.create('o');
        assertEquals(2, option.getArgs().length);
    }

    @Test
    public void testCreate_whenCalledWithValidSettingsAndValueSeparator_shouldReturnOptionInstanceWithValueSeparator() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName")
                .withValueSeparator('=')
                .hasArg();
        Option option = optionBuilder.create('o');
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testCreate_whenCalledWithValidSettingsAndType_shouldReturnOptionInstanceWithType() throws IllegalArgumentException {
        Class type = Integer.class;
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName")
                .withType(type)
                .hasArg();
        Option option = optionBuilder.create('o');
        assertEquals(type, option.getType());
    }

    @Test
    public void testCreate_whenCalledWithValidSettingsAndDescription_shouldReturnOptionInstanceWithDescription() throws IllegalArgumentException {
        String description = "Option description";
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName")
                .withDescription(description)
                .hasArg();
        Option option = optionBuilder.create('o');
        assertEquals(description, option.getDescription());
    }

    
}