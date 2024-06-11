import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser = new GnuParser();

    @Test
    public void testFlatten_WithValidOptions_ReturnsFlattenedArguments() {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");
        options.addOption("o", "output", true, "output file");

        String[] arguments = {"-f", "/path/to/input.txt", "-o", "/path/to/output.txt"};

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(4, flattenedArguments.length);
        assertEquals("-f", flattenedArguments[0]);
        assertEquals("/path/to/input.txt", flattenedArguments[1]);
        assertEquals("-o", flattenedArguments[2]);
        assertEquals("/path/to/output.txt", flattenedArguments[3]);
    }

    @Test
    public void testFlatten_WithStopAtNonOption_ReturnsFlattenedArgumentsUntilNonOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");
        options.addOption("o", "output", true, "output file");

        String[] arguments = {"-f", "/path/to/input.txt", "-o", "/path/to/output.txt", "value1", "value2"};

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals(4, flattenedArguments.length);
        assertEquals("-f", flattenedArguments[0]);
        assertEquals("/path/to/input.txt", flattenedArguments[1]);
        assertEquals("-o", flattenedArguments[2]);
        assertEquals("/path/to/output.txt", flattenedArguments[3]);
    }

    @Test
    public void testFlatten_WithNoOptions_ReturnsFlattenedArgumentsAsIs() {
        Options options = new Options();

        String[] arguments = {"-f", "/path/to/input.txt"};

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(2, flattenedArguments.length);
        assertEquals("-f", flattenedArguments[0]);
        assertEquals("/path/to/input.txt", flattenedArguments[1]);
    }

    @Test
    public void testFlatten_WithDoubleHyphenArgument_ReturnsFlattenedArgumentsIncludingDoubleHyphen() {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");

        String[] arguments = {"-f", "/path/to/input.txt", "--"};

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(3, flattenedArguments.length);
        assertEquals("-f", flattenedArguments[0]);
        assertEquals("/path/to/input.txt", flattenedArguments[1]);
        assertEquals("--", flattenedArguments[2]);
    }

    @Test
    public void testFlatten_WithSingleHyphenArgument_ReturnsFlattenedArgumentsIncludingSingleHyphen() {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");

        String[] arguments = {"-f", "/path/to/input.txt", "-"};

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(3, flattenedArguments.length);
        assertEquals("-f", flattenedArguments[0]);
        assertEquals("/path/to/input.txt", flattenedArguments[1]);
        assertEquals("-", flattenedArguments[2]);
    }

    @Test
    public void testFlatten_WithUnknownOption_ReturnsFlattenedArgumentsIncludingUnknownOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");

        String[] arguments = {"-f", "/path/to/input.txt", "-o", "/path/to/output.txt"};

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(4, flattenedArguments.length);
        assertEquals("-f", flattenedArguments[0]);
        assertEquals("/path/to/input.txt", flattenedArguments[1]);
        assertEquals("-o", flattenedArguments[2]);
        assertEquals("/path/to/output.txt", flattenedArguments[3]);
    }

    @Test
    public void testFlatten_WithSpecialPropertiesOption_ReturnsFlattenedArgumentsWithSeparatedPropertyAndValue() {
        Options options = new Options();
        options.addOption("D", true, "property=value");

        String[] arguments = {"-D", "property=value"};

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(2, flattenedArguments.length);
        assertEquals("-D", flattenedArguments[0]);
        assertEquals("property=value", flattenedArguments[1]);
    }

}