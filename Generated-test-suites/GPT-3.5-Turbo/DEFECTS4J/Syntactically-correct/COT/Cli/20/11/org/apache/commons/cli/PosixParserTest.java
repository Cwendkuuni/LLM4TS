import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        String[] arguments = {"-a", "-b", "value", "argument"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value", flattened[2]);
        assertEquals("argument", flattened[3]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        String[] arguments = {"-a", "-b", "value", "argument"};

        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value", flattened[2]);
        assertEquals("argument", flattened[3]);
    }

    @Test
    public void testFlattenLongOption() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("alpha").desc("option a").build());
        options.addOption(Option.builder("b").longOpt("bravo").desc("option b").build());
        options.addOption(Option.builder("c").longOpt("charlie").desc("option c").build());

        String[] arguments = {"--alpha", "--bravo=value", "--charlie", "argument"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("--alpha", flattened[0]);
        assertEquals("--bravo=value", flattened[1]);
        assertEquals("--charlie", flattened[2]);
        assertEquals("argument", flattened[3]);
    }

    @Test
    public void testFlattenSingleHyphen() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        String[] arguments = {"-"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("-", flattened[0]);
    }

    @Test
    public void testFlattenOptionDoesNotExist() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        String[] arguments = {"-d"};

        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals("--", flattened[0]);
        assertEquals("-d", flattened[1]);
    }

    @Test
    public void testFlattenOptionDoesNotExistStopAtNonOptionFalse() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        String[] arguments = {"-d"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("-d", flattened[0]);
    }

    @Test
    public void testFlattenBurstToken() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        String[] arguments = {"-abc"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("-c", flattened[2]);
    }

    @Test
    public void testFlattenBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        String[] arguments = {"-abcd"};

        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals("-a", flattened[0]);
        assertEquals("--", flattened[1]);
        assertEquals("bcd", flattened[2]);
    }

    @Test
    public void testFlattenBurstTokenStopAtNonOptionFalse() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        String[] arguments = {"-abcd"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("cd", flattened[2]);
    }
}