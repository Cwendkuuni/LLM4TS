import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder result = optionBuilder.withLongOpt("longopt");
        assertEquals("longopt", result.longopt);
    }

    // ...

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidArgument() {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.withLongOpt("longopt");
        optionBuilder.hasArg(false);
        Option option = optionBuilder.create('A'); // invalid option char
        // assert exception is thrown
    }

    // ...
}
//