package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value a", "non-option"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value a", result[1]);
        assertEquals("non-option", result[2]);
    }

    @Test
    public void testFlatten_ContinueBursting() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-bvalueb", "non-option"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("valueb", result[2]);
        assertEquals("non-option", result[3]);
    }

    @Test
    public void testFlatten_NotOptionAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "non-option1", "non-option2"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("non-option1", result[1]);
        assertEquals("non-option2", result[2]);
    }

    @Test
    public void testFlatten_NotOptionAndContinueBursting() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "non-option1", "non-option2"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("non-option1", result[1]);
        assertEquals("non-option2", result[2]);
    }

    @Test
    public void testFlatten_SpecialTokenWithValue() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"--special=value", "-a"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("--special", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-a", result[2]);
    }

    @Test
    public void testFlatten_SpecialTokenWithoutValue() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"--special", "-a"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("--special", result[0]);
        assertEquals("-a", result[1]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_SingleCharacterOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_SingleCharacterOptionInvalid() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-c"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(1, result.length);
        assertEquals("-c", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"--option=value", "-a"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-a", result[2]);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"--option", "-a"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("-a", result[1]);
    }

    @Test
    public void testFlatten_NotHandledOptionAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-c"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(1, result.length);
        assertEquals("-c", result[0]);
    }

    @Test
    public void testFlatten_NotHandledOptionAndContinueBursting() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-c"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-c", result[0]);
    }
}