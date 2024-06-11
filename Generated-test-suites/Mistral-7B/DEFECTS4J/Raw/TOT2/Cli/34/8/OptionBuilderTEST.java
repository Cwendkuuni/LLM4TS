import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option expectedOption = new Option(null, null, true, "arg");
        Option actualOption = optionBuilder.hasArg().create('A');

        assertEquals(expectedOption.getOpt(), actualOption.getOpt());
        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        assertEquals(expectedOption.getArgName(), actualOption.getArgName());
        assertEquals(expectedOption.getNumberOfArgs(), actualOption.getNumberOfArgs());
        assertEquals(expectedOption.getOptionalArg(), actualOption.getOptionalArg());
        assertEquals(expectedOption.getType(), actualOption.getType());
        assertEquals(expectedOption.getValueSeparator(), actualOption.getValueSeparator());
    }

    // Test other methods, edge cases, and error scenarios here...
}