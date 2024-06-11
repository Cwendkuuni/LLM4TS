import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 3, "Option C with argument");
        options.addOption("d", "Default value", "Option D with default value");

        String[] arguments = {"-a", "--b", "-c", "arg1", "--d"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "-a",
                "--b",
                "-c",
                "arg1"
        );
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArguments);

        String[] argumentsWithArg = {"-a", "--d", "value"};
        String[] parsedArgumentsWithArg = parser.flatten(options, argumentsWithArg, false);

        List<String> expectedWithArg = Arrays.asList(
                "-a",
                "--d",
                "value"
        );
        Assert.assertArrayEquals(expectedWithArg.toArray(new String[expectedWithArg.size()]), parsedArgumentsWithArg);

        String[] argumentsWithArgAndNonOption = {"-a", "--d", "value", "nonOption"};
        String[] parsedArgumentsWithArgAndNonOption = parser.flatten(options, argumentsWithArgAndNonOption, true);

        List<String> expectedWithArgAndNonOption = Arrays.asList(
                "-a",
                "--d",
                "value",
                "nonOption"
        );
        Assert.assertArrayEquals(expectedWithArgAndNonOption.toArray(new String[expectedWithArgAndNonOption.size()]), parsedArgumentsWithArgAndNonOption);

        String[] argumentsWithLongOption = {"--option-c", "arg"};
        String[] parsedArgumentsWithLongOption = parser.flatten(options, argumentsWithLongOption, false);

        List<String> expectedWithLongOption = Arrays.asList(
                "--option-c",
                "arg"
        );
        Assert.assertArrayEquals(expectedWithLongOption.toArray(new String[expectedWithLongOption.size()]), parsedArgumentsWithLongOption);

        String[] argumentsWithLongOptionAndArg = {"--option-d", "value"};
        String[] parsedArgumentsWithLongOptionAndArg = parser.flatten(options, argumentsWithLongOptionAndArg, false);

        List<String> expectedWithLongOptionAndArg = Arrays.asList(
                "--option-d",
                "value"
        );
        Assert.assertArrayEquals(expectedWithLongOptionAndArg.toArray(new String[expectedWithLongOptionAndArg.size()]), parsedArgumentsWithLongOptionAndArg);

        String[] argumentsWithLongOptionAndArgAndNonOption = {"--option-d", "value", "nonOption"};
        String[] parsedArgumentsWithLongOptionAndArgAndNonOption = parser.flatten(options, argumentsWithLongOptionAndArgAndNonOption, true);

        List<String> expectedWithLongOptionAndArgAndNonOption = Arrays.asList(
                "--option-d",
                "value",
                "nonOption"
        );
        Assert.assertArrayEquals(expectedWithLongOptionAndArgAndNonOption.toArray(new String[expectedWithLongOptionAndArgAndNonOption.size()]), parsedArgumentsWithLongOptionAndArgAndNonOption);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithUnknownOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-z", "--c", "arg"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }
}