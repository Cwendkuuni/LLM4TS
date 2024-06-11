import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class GnuParserTest {
    
    private GnuParser parser;
    private Options options;
    private String[] arguments;
    
    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
        arguments = new String[0];
    }
    
    @Test
    public void testFlatten() {
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(0, flattened.length);
    }
    
    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] flattened = parser.flatten(options, arguments, true);
        assertNotNull(flattened);
        assertEquals(0, flattened.length);
    }
    
    @Test
    public void testFlattenWithOption() {
        options.addOption("t", false, "test option");
        arguments = new String[]{"-t"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("-t", flattened[0]);
    }
    
    @Test
    public void testFlattenWithNonOption() {
        arguments = new String[]{"test"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("test", flattened[0]);
    }
    
    @Test
    public void testFlattenWithSpecialOption() {
        options.addOption("D", true, "special option");
        arguments = new String[]{"-Dproperty=value"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(2, flattened.length);
        assertEquals("-D", flattened[0]);
        assertEquals("property=value", flattened[1]);
    }
    
    @Test
    public void testFlattenWithStopAtNonOptionAndRest() {
        arguments = new String[]{"-t", "test"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("-t", flattened[0]);
    }
}