package org.apache.commons.cli;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = new String[]{"-a", "arg1", "--", "--arg2"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(4, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("arg1", flattenedArgs[1]);
        assertEquals("--", flattenedArgs[2]);
        assertEquals("--arg2", flattenedArgs[3]);
    }

}