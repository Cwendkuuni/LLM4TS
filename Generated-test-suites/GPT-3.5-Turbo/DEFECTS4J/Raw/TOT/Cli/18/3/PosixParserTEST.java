import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_specialToken() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = new String[]{"--test"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert that the special token is added to the processed tokens list
        assertThat(result).contains("--test");
    }

    @Test
    public void testFlatten_singleHyphen() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = new String[]{"-", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert that the single hyphen is added to the processed tokens list
        assertThat(result).contains("-");
    }

    @Test
    public void testFlatten_validOptionId() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = new String[]{"-a", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert that the option token is added to the processed tokens list
        assertThat(result).contains("-a");
    }

    @Test
    public void testFlatten_invalidOptionId_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = new String[]{"-b", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        // Assert that the special token "--" and the remaining tokens are added to the processed tokens list
        assertThat(result).contains("--", "-b", "value");
    }

    @Test
    public void testFlatten_invalidOptionId() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = new String[]{"-b", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert that the invalid option token is added to the processed tokens list
        assertThat(result).contains("-b");
    }

    @Test
    public void testFlatten_burstToken() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = new String[]{"-ab", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert that the bursted option tokens are added to the processed tokens list
        assertThat(result).contains("-a", "-b");
    }

    @Test
    public void testFlatten_nonOption_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = new String[]{"value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        // Assert that the special token "--" and the remaining tokens are added to the processed tokens list
        assertThat(result).contains("--", "value");
    }

    @Test
    public void testFlatten_nonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = new String[]{"value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert that the non-option token is added to the processed tokens list
        assertThat(result).contains("value");
    }

    @Test
    public void testFlatten_multipleArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert that the option tokens and their argument values are added to the processed tokens list
        assertThat(result).contains("-a", "value1", "-b", "value2");
    }

}