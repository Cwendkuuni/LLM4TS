import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "value1", "--b=value2"};
        boolean stopAtNonOption = false;
        String[] result = posixParser.flatten(options, arguments, stopAtNonOption);
    
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--b=value2", result[2]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-a", "value1", "non-option", "--b=value2"};
        boolean stopAtNonOption = true;
        String[] result = posixParser.flatten(options, arguments, stopAtNonOption);
    
        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--", result[2]);
        assertEquals("non-option", result[3]);
    }

    @Test
    public void testFlatten_singleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};
        boolean stopAtNonOption = false;
        String[] result = posixParser.flatten(options, arguments, stopAtNonOption);
    
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_validOptionId() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        String[] arguments = {"-a"};
        boolean stopAtNonOption = false;
        String[] result = posixParser.flatten(options, arguments, stopAtNonOption);
    
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_invalidOptionId() {
        Options options = new Options();
        String[] arguments = {"-c"};
        boolean stopAtNonOption = false;
        String[] result = posixParser.flatten(options, arguments, stopAtNonOption);
    
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-c", result[0]);
    }

    @Test
    public void testFlatten_burstToken() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        String[] arguments = {"-ab"};
        boolean stopAtNonOption = false;
        String[] result = posixParser.flatten(options, arguments, stopAtNonOption);
    
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("b", result[1]);
    }
    
    @Test
    public void testFlatten_burstToken_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        String[] arguments = {"-abc"};
        boolean stopAtNonOption = true;
        String[] result = posixParser.flatten(options, arguments, stopAtNonOption);
    
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("bc", result[1]);
    }

}