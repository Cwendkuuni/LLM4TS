import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

public class ParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = new String[0];
        boolean stopAtNonOption = false;
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);

        Assert.assertArrayEquals(arguments, flattened);
    }

    @Test
    public void testParse() {
        Options options = new Options();
        String[] arguments = new String[]{"--option1", "value1", "--option2", "value2"};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, arguments);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals(2, cmd.getOptions().length);
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test
    public void testProcessArgs() {
        Option option = Option.builder("o").build();
        ListIterator iterator = Arrays.asList("value1", "value2").listIterator();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        try {
            parser.processArgs(option, iterator);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals(2, option.getValues().length);
        Assert.assertEquals("value1", option.getValue(0));
        Assert.assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testProcessOption() {
        Option option = Option.builder("o").hasArgs().build();
        ListIterator iterator = Arrays.asList("value1", "value2").listIterator();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        try {
            parser.processOption("-o", iterator);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals(2, option.getValues().length);
        Assert.assertEquals("value1", option.getValue(0));
        Assert.assertEquals("value2", option.getValue(1));
    }

}