package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option optionA = new Option("a", true, "a help message");
        Option optionB = new Option("b", 1, "b help message");
        Option optionC = new Option("c", 2, "c help message");
        Option optionD = new Option("d", true, "d help message");
        Option optionE = new Option("e", 3, "e help message");
        Option optionF = new Option("f", true, "f help message");

        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        options.addOption(optionD);
        options.addOption(optionE);
        options.addOption(optionF);

        String[] arguments = {"-a", "--b", "1", "--c=2", "3", "--d", "--e=foo", "--f"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(11, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("1", result[2]);
        assertEquals("c", result[3]);
        assertEquals("2", result[4]);
        assertEquals("3", result[5]);
        assertEquals("d", result[6]);
        assertEquals("e", result[7]);
        assertEquals("foo", result[8]);
        assertEquals("f", result[9]);
        assertNull(result[10]);

        Option parsedOptionA = options.findOption("a");
        Option parsedOptionB = options.findOption("b");
        Option parsedOptionC = options.findOption("c");
        Option parsedOptionD = options.findOption("d");
        Option parsedOptionE = options.findOption("e");
        Option parsedOptionF = options.findOption("f");

        assertTrue(parsedOptionA.isSettableFromProgram());
        assertTrue(parsedOptionB.isSettableFromProgram());
        assertEquals(1, parsedOptionB.getOptArg().length);
        assertEquals(2, parsedOptionC.getOptArg().length);
        assertNull(parsedOptionD.getOptArg());
        assertTrue(parsedOptionE.isSettableFromProgram());
        assertEquals(3, parsedOptionE.getOptArg().length);

        assertSame(parsedOptionA, parser.findOption("a", result));
        assertSame(parsedOptionB, parser.findOption("b", result));
        assertSame(parsedOptionC, parser.findOption("c", result));
        assertSame(parsedOptionD, parser.findOption("d", result));
        assertSame(parsedOptionE, parser.findOption("e", result));
        assertSame(parsedOptionF, parser.findOption("f", result));

        String[] arguments2 = {"-a", "--b=foo", "-c", "--d", "-e=bar", "--f"};

        result = parser.flatten(options, arguments2, false);

        assertEquals(6, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("foo", result[2]);
        assertEquals("c", result[3]);
        assertEquals("d", result[4]);
        assertEquals("e", result[5]);

        assertSame(parsedOptionA, parser.findOption("a", result));
        assertSame(parsedOptionB, parser.findOption("b", result));
        assertNull(parsedOptionC.getOptArg());
        assertSame(parsedOptionD, parser.findOption("d", result));
        assertSame(parsedOptionE, parser.findOption("e", result));
        assertSame(parsedOptionF, parser.findOption("f", result));

        assertSame(parsedOptionB, parser.findOption("b", result, "foo"));
        assertSame(parsedOptionE, parser.findOption("e", result, "bar"));

        // test with stopAtNonOption
        result = parser.flatten(options, arguments, true);

        assertEquals(7, result.length);
        assertEquals("--", result[0]);
        assertEquals("a", result[1]);
        assertEquals("b", result[2]);
        assertEquals("1", result[3]);
        assertEquals("c", result[4]);
        assertEquals("3", result[5]);

        assertNull(parser.findOption("d", result));
        assertNull(parser.findOption("e", result));
        assertNull(parser.findOption("f", result));

        String[] arguments3 = {"--foo", "-a", "--b", "1", "--c", "--d", "--e", "--f"};

        result = parser.flatten(options, arguments3, false);

        assertEquals(9, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("a", result[1]);
        assertEquals("b", result[2]);
        assertEquals("1", result[3]);
        assertEquals("c", result[4]);
        assertEquals("d", result[5]);
        assertEquals("e", result[6]);
        assertEquals("f", result[7]);

        assertSame(parsedOptionA, parser.findOption("a", result));
        assertSame(parsedOptionB, parser.findOption("b", result));
        assertSame(parsedOptionC, parser.findOption("c", result));
        assertSame(parsedOptionD, parser.findOption("d", result));
        assertSame(parsedOptionE, parser.findOption("e", result));
        assertSame(parsedOptionF, parser.findOption("f", result));

        assertSame(parsedOptionA, parser.findOption("a", result, null));
        assertSame(parsedOptionB, parser.findOption("b", result, null));
        assertSame(parsedOptionC, parser.findOption("c", result, null));
        assertSame(parsedOptionD, parser.findOption("d", result, null));
        assertSame(parsedOptionE, parser.findOption("e", result, null));
        assertSame(parsedOptionF, parser.findOption("f", result, null));

        assertSame(parsedOptionB, parser.findOption("b", result, ""));
        assertSame(parsedOptionB, parser.findOption("b", result, ""));

        assertSame(parsedOptionB, parser.findOption("b", result, ""));
        assertSame(parsedOptionB, parser.findOption("b", result, ""));

        assertSame(parsedOptionB, parser.findOption("b", result, ""));
        assertSame(parsedOptionB, parser.findOption("b", result, ""));

        assertSame(parsedOptionE, parser.findOption("e", result, "foo"));
        assertSame(parsedOptionE, parser.findOption("e", result, "foo"));

        assertSame(parsedOptionE, parser.findOption("e", result, "bar"));
        assertSame(parsedOptionE, parser.findOption("e", result, "bar"));

        assertSame(parsedOptionE, parser.findOption("e", result, "bar"));
        assertSame(parsedOptionE, parser.findOption("e", result, "bar"));
    }
}