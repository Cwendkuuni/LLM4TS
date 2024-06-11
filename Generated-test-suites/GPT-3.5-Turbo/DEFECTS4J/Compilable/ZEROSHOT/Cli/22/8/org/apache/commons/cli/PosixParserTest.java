package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() throws Exception {
        String[] arguments = {"--foo", "--bar=xyz", "-a", "arg1", "-barg2"};
        Options options = new Options();
        options.addOption("foo", false, "foo option");
        options.addOption("bar", true, "bar option");
        options.addOption("a", false, "a option");
        options.addOption("b", true, "b option");

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--foo", "--bar", "xyz", "-a", "arg1", "-barg2"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws Exception {
        String[] arguments = {"--foo", "--bar=xyz", "-a", "arg1", "-barg2"};
        Options options = new Options();
        options.addOption("foo", false, "foo option");
        options.addOption("bar", true, "bar option");
        options.addOption("a", false, "a option");
        options.addOption("b", true, "b option");

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"--foo", "--bar", "xyz", "-a", "arg1", "-barg2"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithoutOptions() throws Exception {
        String[] arguments = {"--foo", "--bar=xyz", "-a", "arg1", "-barg2"};
        Options options = new Options();

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--foo", "--bar=xyz", "-a", "arg1", "-barg2"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOnlyNonOptions() throws Exception {
        String[] arguments = {"arg1", "arg2", "arg3"};
        Options options = new Options();

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"arg1", "arg2", "arg3"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndOnlyNonOptions() throws Exception {
        String[] arguments = {"arg1", "arg2", "arg3"};
        Options options = new Options();

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"arg1", "arg2", "arg3"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndOptionsAfterNonOptions() throws Exception {
        String[] arguments = {"--foo", "--bar", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", false, "foo option");
        options.addOption("bar", false, "bar option");

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"--foo", "--bar", "arg1", "arg2"};

        assertArrayEquals(expected, result);
    }

}