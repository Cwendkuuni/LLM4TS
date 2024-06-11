package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_OptionsAndArguments_ReturnFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "-b", "arg1", "arg2"};

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals("-a", flattenedArguments[0]);
        assertEquals("-b", flattenedArguments[1]);
        assertEquals("arg1", flattenedArguments[2]);
        assertEquals("arg2", flattenedArguments[3]);
        assertEquals(4, flattenedArguments.length);
    }

    @Test
    public void testFlatten_OptionsWithEqualsSign_ReturnFlattenedArguments() {
        Options options = new Options();
        options.addOption("o", true, "Option with equals sign");

        String[] arguments = {"-o=arg1"};

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals("-o", flattenedArguments[0]);
        assertEquals("arg1", flattenedArguments[1]);
        assertEquals(2, flattenedArguments.length);
    }

    @Test
    public void testFlatten_DoubleHyphenToken_ReturnFlattenedArguments() {
        Options options = new Options();

        String[] arguments = {"--", "arg1", "-arg2"};

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals("--", flattenedArguments[0]);
        assertEquals("arg1", flattenedArguments[1]);
        assertEquals("-arg2", flattenedArguments[2]);
        assertEquals(3, flattenedArguments.length);
    }

    @Test
    public void testFlatten_SingleHyphenToken_ReturnFlattenedArguments() {
        Options options = new Options();

        String[] arguments = {"-"};

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals("-", flattenedArguments[0]);
        assertEquals(1, flattenedArguments.length);
    }

    @Test
    public void testFlatten_ShortOptionToken_ReturnFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a"};

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals("-a", flattenedArguments[0]);
        assertEquals(1, flattenedArguments.length);
    }

    @Test
    public void testFlatten_InvalidOptionToken_StopAtNonOptionTrue_ReturnFlattenedArguments() {
        Options options = new Options();

        String[] arguments = {"-x", "arg1"};

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals("--", flattenedArguments[0]);
        assertEquals("arg1", flattenedArguments[1]);
        assertEquals(2, flattenedArguments.length);
    }

    @Test
    public void testFlatten_InvalidOptionToken_StopAtNonOptionFalse_ReturnFlattenedArguments() {
        Options options = new Options();

        String[] arguments = {"-x"};

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals("-x", flattenedArguments[0]);
        assertEquals(1, flattenedArguments.length);
    }

    @Test
    public void testFlatten_LongOptionToken_StopAtNonOptionTrue_ReturnFlattenedArguments() {
        Options options = new Options();
        options.addOption("o", true, "Option with equals sign");

        String[] arguments = {"--input-file", "file1.txt"};

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals("--input-file", flattenedArguments[0]);
        assertEquals("file1.txt", flattenedArguments[1]);
        assertEquals(2, flattenedArguments.length);
    }

    @Test
    public void testFlatten_LongOptionToken_StopAtNonOptionFalse_ReturnFlattenedArguments() {
        Options options = new Options();
        options.addOption("o", true, "Option with equals sign");

        String[] arguments = {"--input-file", "file1.txt"};

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals("--input-file", flattenedArguments[0]);
        assertEquals("file1.txt", flattenedArguments[1]);
        assertEquals(2, flattenedArguments.length);
    }

}