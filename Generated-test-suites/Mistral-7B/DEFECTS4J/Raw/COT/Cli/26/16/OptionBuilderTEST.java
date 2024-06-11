import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class OptionBuilderTest {

    @org.junit.Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionName");
        Option expectedOption = new Option("optionName", "Option description", false, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option("arg", "Option description", true, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasArgWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option expectedOption = new Option("argName", "Option description", true, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option("arg", "Option description", true, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option("optionName", "Option description", false, null);
        expectedOption.setValueSeparator(':');
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option("arg", "Option description", Option.UNLIMITED_VALUES, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("arg", "Option description", true, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = new Option("arg", "Option description", Option.UNLIMITED_VALUES, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option expectedOption = new Option("arg", "Option description", 3, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option expectedOption = new Option("arg", "Option description", true, Integer.class);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testCreateWithEmptyLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
        try {
            Option actualOption = optionBuilder.create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testCreateWithNullLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
        try {
            Option actualOption = optionBuilder.create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}