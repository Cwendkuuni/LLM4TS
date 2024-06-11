import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "--option", "value1", "value2"};
        String[] expected = {"-a", "--option", "value1", "value2"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "--option", "value1", "value2"};
        String[] expected = {"-a", "--option", "value1", "value2"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();

        parser.init();

        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

}