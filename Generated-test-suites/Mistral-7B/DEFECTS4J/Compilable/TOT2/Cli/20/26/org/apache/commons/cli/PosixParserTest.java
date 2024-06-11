package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "--bar=baz", "-c", "--", "-d"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "--", "-a", "--bar=baz", "--", "-c", "--", "--", "-d"}, flattenedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
        fail("Expected ParseException to be thrown");
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "--bar=baz", "-c", "--", "-d", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo", "-a", "--bar=baz", "-c", "--", "-d", "nonOption"}, flattenedArgs);
    }
}