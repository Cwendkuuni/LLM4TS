import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionContext;
import org.apache.commons.cli2.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArgumentImplTest {

    @Test
    public void testArgumentWithMinimumOneValue() throws Exception {
        Option option = OptionBuilder.withName("arg")
                .withDescription("A test argument")
                .withMinimum(1)
                .create(false);
        CommandLine cmd = new CommandLine();

        cmd.parse(new String[]{"--arg", "value1"});
        OptionContext context = new OptionContext();
        Option result = option.parse(context, cmd.getArgs(), null);

        assertEquals("value1", result.getValue());
    }

    @Test
    public void testArgumentWithMaximumOneValue() throws Exception {
        Option option = OptionBuilder.withName("arg")
                .withDescription("A test argument")
                .withMaximum(1)
                .create(false);
        CommandLine cmd = new CommandLine();

        cmd.parse(new String[]{"--arg", "value1", "--arg", "value2"});
        OptionContext context = new OptionContext();
        Option result = option.parse(context, cmd.getArgs(), null);

        assertEquals("value1", result.getValue());
        assertEquals(1, cmd.getValues(option).size());
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithInvalidValue() throws Exception {
        Option option = OptionBuilder.withName("arg")
                .withDescription("A test argument")
                .create(false);
        CommandLine cmd = new CommandLine();

        cmd.parse(new String[]{"--arg", "value1", "--arg", "value2", "value3"});
        OptionContext context = new OptionContext();
        Option result = option.parse(context, cmd.getArgs(), null);

        assertEquals("value1", result.getValue());
        assertEquals(2, cmd.getValues(option).size());
    }

    @Test
    public void testArgumentWithDefaultValue() throws Exception {
        Option option = OptionBuilder.withName("arg")
                .withDescription("A test argument")
                .create(false);
        CommandLine cmd = new CommandLine();

        cmd.parse(new String[]{"--arg"});
        OptionContext context = new OptionContext();
        Option result = option.parse(context, cmd.getArgs(), null);

        assertEquals(null, result.getValue());
        assertEquals(cmd.getDefaultValues(option), cmd.getValues(option));
    }
}