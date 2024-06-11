import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption_True() {
        // Test case where stopAtNonOption is true and there are non-option arguments at the end
        String[] arguments = {"-a", "value1", "-b", "value2", "nonOption1", "nonOption2"};
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "value1", "-b", "value2", "nonOption1", "nonOption2"}, flattenedArguments);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        // Test case where stopAtNonOption is false and there are non-option arguments at the end
        String[] arguments = {"-a", "value1", "-b", "value2", "nonOption1", "nonOption2"};
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "value1", "-b", "value2", "nonOption1", "nonOption2"}, flattenedArguments);
    }

    @Test
    public void testFlatten_NoOptionWithArg() {
        // Test case where there is a non-option argument with an option-like format
        // but there is no corresponding option defined
        String[] arguments = {"-a", "value1", "-b", "value2", "-cvalue3"};
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "value1", "-b", "value2", "-cvalue3"}, flattenedArguments);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        // Test case where there is a single hyphen "-"
        String[] arguments = {"-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-"}, flattenedArguments);
    }

    @Test
    public void testFlatten_InvalidOption() {
        // Test case where there is an option-like argument that does not correspond to any defined option
        String[] arguments = {"-abc"};
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        PosixParser parser = new PosixParser();

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(arguments, flattenedArguments);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOption_False() {
        // Test case where there is an option-like argument that does not correspond to any defined option,
        // with stopAtNonOption set to false
        String[] arguments = {"-abc"};
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        PosixParser parser = new PosixParser();

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, flattenedArguments);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOption_True() {
        // Test case where there is an option-like argument that does not correspond to any defined option,
        // with stopAtNonOption set to true
        String[] arguments = {"-abc"};
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        PosixParser parser = new PosixParser();

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-abc"}, flattenedArguments);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        // Test case where there is a long option with a value separated by "="
        String[] arguments = {"--option=value"};
        Options options = new Options();
        options.addOption(Option.builder("option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--option", "value"}, flattenedArguments);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        // Test case where there is a long option without a value
        String[] arguments = {"--option"};
        Options options = new Options();
        options.addOption(Option.builder("option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--option"}, flattenedArguments);
    }

}