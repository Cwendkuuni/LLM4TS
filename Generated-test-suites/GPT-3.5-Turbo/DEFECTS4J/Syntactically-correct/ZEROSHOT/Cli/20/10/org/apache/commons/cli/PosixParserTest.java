import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();

        Option option1 = Option.builder("f")
                .required()
                .desc("Option 1")
                .build();

        Option option2 = Option.builder("v")
                .required()
                .desc("Option 2")
                .hasArg()
                .argName("arg")
                .build();

        options.addOption(option1);
        options.addOption(option2);

        String[] args = {"-f", "-v", "test"};

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(3, flattenedArgs.length);
        assertEquals("-f", flattenedArgs[0]);
        assertEquals("-v", flattenedArgs[1]);
        assertEquals("test", flattenedArgs[2]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();

        Option option1 = Option.builder("f")
                .required()
                .desc("Option 1")
                .build();

        Option option2 = Option.builder("v")
                .required()
                .desc("Option 2")
                .hasArg()
                .argName("arg")
                .build();

        options.addOption(option1);
        options.addOption(option2);

        String[] args = {"-f", "-v", "test"};

        String[] flattenedArgs = parser.flatten(options, args, true);

        assertEquals(3, flattenedArgs.length);
        assertEquals("-f", flattenedArgs[0]);
        assertEquals("-v", flattenedArgs[1]);
        assertEquals("test", flattenedArgs[2]);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();

        Option option1 = Option.builder("f")
                .required()
                .desc("Option 1")
                .build();

        Option option2 = Option.builder("v")
                .required()
                .desc("Option 2")
                .hasArg()
                .argName("arg")
                .build();

        Option option3 = Option.builder()
                .longOpt("verbose")
                .desc("Verbose output")
                .build();

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        String[] args = {"-f", "-v", "test", "--verbose"};

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(4, flattenedArgs.length);
        assertEquals("-f", flattenedArgs[0]);
        assertEquals("-v", flattenedArgs[1]);
        assertEquals("test", flattenedArgs[2]);
        assertEquals("--verbose", flattenedArgs[3]);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        Options options = new Options();

        Option option1 = Option.builder("f")
                .required()
                .desc("Option 1")
                .build();

        Option option2 = Option.builder("v")
                .required()
                .desc("Option 2")
                .hasArg()
                .argName("arg")
                .build();

        options.addOption(option1);
        options.addOption(option2);

        String[] args = {"-f", "-v", "test", "-x"};

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(4, flattenedArgs.length);
        assertEquals("-f", flattenedArgs[0]);
        assertEquals("-v", flattenedArgs[1]);
        assertEquals("test", flattenedArgs[2]);
        assertEquals("-x", flattenedArgs[3]);
    }

    @Test
    public void testFlattenWithBurstToken() {
        Options options = new Options();

        Option option1 = Option.builder("f")
                .required()
                .desc("Option 1")
                .build();

        Option option2 = Option.builder("v")
                .required()
                .desc("Option 2")
                .hasArg()
                .argName("arg")
                .build();

        options.addOption(option1);
        options.addOption(option2);

        String[] args = {"-fvtest"};

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(4, flattenedArgs.length);
        assertEquals("-f", flattenedArgs[0]);
        assertEquals("-v", flattenedArgs[1]);
        assertEquals("test", flattenedArgs[2]);
        assertEquals("-vtest", flattenedArgs[3]);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndBurstToken() {
        Options options = new Options();

        Option option1 = Option.builder("f")
                .required()
                .desc("Option 1")
                .build();

        Option option2 = Option.builder("v")
                .required()
                .desc("Option 2")
                .hasArg()
                .argName("arg")
                .build();

        options.addOption(option1);
        options.addOption(option2);

        String[] args = {"-fvtest"};

        String[] flattenedArgs = parser.flatten(options, args, true);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-fvtest", flattenedArgs[0]);
    }
}