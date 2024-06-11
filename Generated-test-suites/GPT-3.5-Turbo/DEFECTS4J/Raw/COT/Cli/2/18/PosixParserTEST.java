import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;
    private String[] arguments;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        arguments = new String[] {};
    }

    @Test
    public void testFlatten_emptyArguments() {
        String[] result = parser.flatten(options, arguments, false);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_singleArgument() {
        arguments = new String[] {"-a"};
        options.addOption(Option.builder("a").build());
        String[] result = parser.flatten(options, arguments, false);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_multipleArguments() {
        arguments = new String[] {"-a", "-b", "-c"};
        options.addOption(Option.builder("a").build())
            .addOption(Option.builder("b").build())
            .addOption(Option.builder("c").build());
        String[] result = parser.flatten(options, arguments, false);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        arguments = new String[] {"-a", "value", "non-option"};
        options.addOption(Option.builder("a").build());
        String[] result = parser.flatten(options, arguments, true);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_optionWithArgument() {
        arguments = new String[] {"-a", "value"};
        options.addOption(Option.builder("a").hasArg().build());
        String[] result = parser.flatten(options, arguments, false);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_optionWithMultipleArguments() {
        arguments = new String[] {"-a", "value1", "value2"};
        options.addOption(Option.builder("a").hasArgs().build());
        String[] result = parser.flatten(options, arguments, false);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }

    @Test
    public void testFlatten_unknownOption() {
        arguments = new String[] {"-a"};
        options.addOption(Option.builder("b").build());
        String[] result = parser.flatten(options, arguments, false);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_unknownOption_stopAtNonOption() {
        arguments = new String[] {"-a", "non-option"};
        options.addOption(Option.builder("b").build());
        String[] result = parser.flatten(options, arguments, true);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("non-option", result[1]);
    }

    @Test
    public void testFlatten_hyphen() {
        arguments = new String[] {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
}