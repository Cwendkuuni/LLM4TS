import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_noArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = new String[0];
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_singleOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-a"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_multipleOptions() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-a", "-b"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_optionWithArgument() {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A with argument");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-a", "arg"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_multipleOptionsWithArgument() {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A with argument");
        options.addOption("b", "optionB", true, "Option B with argument");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-a", "arg1", "-b", "arg2"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-a", "-b", "nonOption"};
        parser.flatten(options, args, true);
    }

    @Test
    public void testFlatten_specialTokens() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--special", "-special"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_argumentAfterSpecialToken() {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A with argument");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--special=arg", "-special", "arg"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_notHandledToken() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-notHandled"};
        parser.flatten(options, args, false);
    }

}