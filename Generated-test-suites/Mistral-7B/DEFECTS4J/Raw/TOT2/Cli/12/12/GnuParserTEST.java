import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlattenTypicalUsage() {
        String[] args = {"-Dproperty1=value1", "-Dproperty2=value2", "--foo", "arg1", "arg2", "--"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"-D", "property1", "value1", "-D", "property2", "value2", "-", "foo", "arg1", "arg2"}, result);
    }
}