import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.GnuParser;

public class GnuParserTest {

    @Test
    public void testFlatten_TypicalUseCase() throws Exception {
        String[] arguments = {"-Dmyprop=value1", "-opt1", "--", "-opt2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);

        String[] expected = {"-D", "myprop", "value1", "-opt1", "--", "-opt2"};
        assertArrayEquals(expected, flattenedArgs);
    }
}