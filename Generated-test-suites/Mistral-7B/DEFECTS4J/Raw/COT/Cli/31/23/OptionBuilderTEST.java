import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option expectedOption = new Option("option", "optionLong", false, "O");
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option("arg", null, true, null);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("arg", null, false, null);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option("args", null, true, null);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgsWithSpecifiedNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = new Option("args", null, false, null);
        expectedOption.setArgs(2);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option("option", null, false, null);
        expectedOption.setValueSeparator(':');
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option("option", null, true, null);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithDescription() {
        String description = "This is an example option";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        Option expectedOption = new Option("option", null, false, null);
        expectedOption.setLongOpt("option");
        expectedOption.setDescription(description);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testCreateWithType() {
        Integer type = 1;
        OptionBuilder optionBuilder = OptionBuilder.withType(type);
        Option expectedOption = new Option("option", null, false, null);
        expectedOption.setLongOpt("option");
        expectedOption.setType(type.getClass());
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidType() {
        OptionBuilder optionBuilder = OptionBuilder.withType("invalidType");
        optionBuilder.create();
    }

    
}