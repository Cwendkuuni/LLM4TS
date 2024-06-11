import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    @Test
    public void testArgumentImplWithMinimumOneValue() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Collections.singletonList("defaultValue"), 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        argument.process(commandLine, commandLine.getArguments().listIterator());

        assertEquals("defaultValue", commandLine.getValue(argument.getOptionSpec()));
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplWithMoreValuesThanMaximum() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Collections.singletonList("defaultValue"), 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.addValue(argument.getOptionSpec(), "defaultValue1");
        commandLine.addValue(argument.getOptionSpec(), "defaultValue2");
        commandLine.addValue(argument.getOptionSpec(), "unexpectedValue");

        argument.process(commandLine, commandLine.getArguments().listIterator());
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplWithInvalidValue() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Collections.singletonList("defaultValue"), 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.addValue(argument.getOptionSpec(), "invalidValue");

        try {
            argument.process(commandLine, commandLine.getArguments().listIterator());
            fail();
        } catch (OptionException e) {
            // expected
        }
    }
}