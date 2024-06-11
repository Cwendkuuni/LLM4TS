import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

@Test
public void testFlattenEmptyOptionsAndArguments() throws ParseException {
    Parser parser = new Parser() {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return new String[0];
        }
    };

    Options options = new Options();
    String[] arguments = new String[0];

    String[] flattened = parser.flatten(options, arguments, false);

    Assert.assertEquals(0, flattened.length);
}

@Test
public void testParseEmptyOptionsAndArguments() throws ParseException {
    Parser parser = new Parser() {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return new String[0];
        }
    };

    Options options = new Options();
    String[] arguments = new String[0];

    CommandLine cmd = parser.parse(options, arguments);

    Assert.assertEquals(0, cmd.getOptions().size());
    Assert.assertEquals(0, cmd.getArgs().length);
}

@Test(expected = UnrecognizedOptionException.class)
public void testParseUnrecognizedOption() throws ParseException {
    Parser parser = new Parser() {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return new String[] {"-a"};
        }
    };

    Options options = new Options();

    parser.parse(options, new String[0]);
}

@Test(expected = MissingOptionException.class)
public void testParseMissingRequiredOption() throws ParseException {
    Parser parser = new Parser() {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return new String[0];
        }
    };

    Option option = new Option("a", "option", true, "required option");
    option.setRequired(true);
    Options options = new Options();
    options.addOption(option);

    parser.parse(options, new String[0]);
}

@Test
public void testProcessArgsWithRequiredArgument() throws ParseException {
    Parser parser = new Parser() {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    };

    Option option = new Option("a", "option", true, "required option");
    Options options = new Options();
    options.addOption(option);

    CommandLine cmd = new CommandLine();
    ListIterator iterator = Arrays.asList(new String[] {"value"}).listIterator();

    parser.processArgs(option, iterator);

    Assert.assertEquals(1, option.getValues().length);
    Assert.assertEquals("value", option.getValue());
}

@Test(expected = MissingArgumentException.class)
public void testProcessArgsMissingRequiredArgument() throws ParseException {
    Parser parser = new Parser() {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    };

    Option option = new Option("a", "option", true, "required option");
    Options options = new Options();
    options.addOption(option);

    CommandLine cmd = new CommandLine();
    ListIterator iterator = Arrays.asList(new String[0]).listIterator();

    parser.processArgs(option, iterator);
}

@Test(expected = UnrecognizedOptionException.class)
public void testProcessOptionUnrecognizedOption() throws ParseException {
    Parser parser = new Parser() {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return new String[0];
        }
    };

    Options options = new Options();

    parser.processOption("-a", new ArrayList<String>().listIterator());
}

}