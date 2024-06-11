import org.junit.Test;

public class PosixParserTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_NullOptions() {
        PosixParser parser = new PosixParser();
        parser.flatten(null, new String[]{"-a"}, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_NullArguments() {
        PosixParser parser = new PosixParser();
        parser.flatten(new Options(), null, false);
    }

    @Test
    public void testFlatten_EmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), new String[]{}, false);
        // assert result is an empty array
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] result = parser.flatten(options, new String[]{"-a", "nonOption"}, true);
        // assert result contains only "-a" and "--nonOption"
    }

    @Test(expected = MissingArgumentException.class)
    public void testFlatten_MissingArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        String[] result = parser.flatten(options, new String[]{"-a"}, false);
    }

    @Test
    public void testFlatten_SingleArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        String[] result = parser.flatten(options, new String[]{"-a", "value"}, false);
        // assert result contains "-a" and "value"
    }

    @Test
    public void testFlatten_MultipleArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArgs().build());
        String[] result = parser.flatten(options, new String[]{"-a", "value1", "value2"}, false);
        // assert result contains "-a", "value1", and "value2"
    }

    @Test(expected = MissingArgumentException.class)
    public void testFlatten_MissingArgumentInMultipleArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArgs().build());
        String[] result = parser.flatten(options, new String[]{"-a", "value1", "-b"}, false);
    }
}