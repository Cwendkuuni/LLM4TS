import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GnuParserTest {
    private GnuParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-option1", "value1", "--option2", "value2"};
        
        String[] flattened = parser.flatten(options, arguments, false);
        
        assertEquals("Number of flattened arguments", 4, flattened.length);
        assertEquals("Flattened argument 1", "-option1", flattened[0]);
        assertEquals("Flattened argument 2", "value1", flattened[1]);
        assertEquals("Flattened argument 3", "--option2", flattened[2]);
        assertEquals("Flattened argument 4", "value2", flattened[3]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"-option1", "value1", "--", "non-option", "value"};

        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals("Number of flattened arguments", 3, flattened.length);
        assertEquals("Flattened argument 1", "-option1", flattened[0]);
        assertEquals("Flattened argument 2", "value1", flattened[1]);
        assertEquals("Flattened argument 3", "--", flattened[2]);
    }

    @Test
    public void testFlattenWithNoOptions() {
        String[] arguments = {"arg1", "arg2", "arg3"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertArrayEquals("Flattened arguments", arguments, flattened);
    }

    @Test
    public void testFlattenWithHyphen() {
        String[] arguments = {"-", "-option1"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("Number of flattened arguments", 2, flattened.length);
        assertEquals("Flattened argument 1", "-", flattened[0]);
        assertEquals("Flattened argument 2", "-option1", flattened[1]);
    }

    @Test
    public void testFlattenWithSpecialPropertiesOption() {
        String[] arguments = {"-Dproperty=value"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("Number of flattened arguments", 2, flattened.length);
        assertEquals("Flattened argument 1", "-D", flattened[0]);
        assertEquals("Flattened argument 2", "property=value", flattened[1]);
    }
}