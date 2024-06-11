package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser = new GnuParser();

    @Test
    public void testFlatten_NoSpecialOptions() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-arg1", "value1", "--", "nonOption", "-arg2", "value2"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-arg1", "value1", "--", "nonOption", "-arg2", "value2"}, result);
    }

    @Test
    public void testFlatten_SpecialOptions() {
        Options options = new Options();
        options.addOption("D", true, "Property option");
        String[] arguments = {"-Dprop1=value1", "-Dprop2=value2", "arg1"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-D", "prop1=value1", "-D", "prop2=value2", "arg1"}, result);
    }

}