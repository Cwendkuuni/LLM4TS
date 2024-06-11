import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Whitelist;
import org.jsoup.parser.Parser;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CleanerTest {

    @org.junit.Test
    public void testConstructor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertThat(cleaner, is(instanceOf(Cleaner.class)));
    }

    // ...
}