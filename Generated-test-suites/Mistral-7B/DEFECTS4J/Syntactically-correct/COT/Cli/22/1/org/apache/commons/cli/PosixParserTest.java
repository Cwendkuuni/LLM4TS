import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest
{
    @Test
    public void testFlatten()
    {
        // Given
        String[] args = new String[]{"--foo", "--bar=baz", "-a", "-b", "-c", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", "value", "Option D with argument");

        // When
        Parser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        // Then
        String[] expected = new String[]{"--foo", "--bar=baz", "-a", "--", "arg1", "arg2"};
        assertArrayEquals(expected, flattenedArgs);
    }

    @Test(expected = IllegalStateException.class)
    public void testFlattenWithInvalidOption()
    {
        // Given
        String[] args = new String[]{"--invalidOption"};
        Options options = new Options();

        // When
        Parser parser = new PosixParser();
        parser.flatten(options, args, false);

        // Then
        // An exception should be thrown
    }

    @Test
    public void testFlattenWithStopAtNonOption()
    {
        // Given
        String[] args = new String[]{"--foo", "--bar=baz", "-a", "-b", "-c", "arg1", "arg2", "nonOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", "value", "Option D with argument");

        // When
        Parser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, true);

        // Then
        String[] expected = new String[]{"--foo", "--bar=baz", "-a", "-c", "nonOption", "--", "arg1", "arg2"};
        assertArrayEquals(expected, flattenedArgs);
    }

    @Test
    public void testBurstToken()
    {
        // Given
        String token = "--abcdef";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", "value", "Option D with argument");

        // When
        Parser parser = new PosixParser();
        parser.burstToken(token, false);

        // Then
        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("-b");
        expected.add("-c");
        expected.add("d");
        expected.add("value");
        List<String> actual = (List<String>) parser.tokens;
        assertEquals(expected, actual);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption()
    {
        // Given
        String token = "--abcdef";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", "value", "Option D with argument");

        // When
        Parser parser = new PosixParser();
        parser.burstToken(token, true);

        // Then
        String[] expected = new String[]{"--", "abcdef"};
        String[] actual = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);
        assertArrayEquals(expected, actual);
    }
}