import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;
import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        assertNotNull(splitter);
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(':'));
        assertNotNull(splitter);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(";");
        assertNotNull(splitter);
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        assertNotNull(splitter);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        assertNotNull(splitter);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        assertNotNull(splitter);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(3);
        assertNotNull(splitter);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        assertNotNull(splitter);
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is(':'));
        assertNotNull(splitter);
    }

    @Test
    public void testSplit() {
        Iterable<String> result = Splitter.on(',').split("a,b,c");
        assertNotNull(result);
    }

    @Test
    public void testSplitToList() {
        List<String> result = Splitter.on(',').splitToList("a,b,c");
        assertNotNull(result);
    }

    @Test
    public void testMapSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator('=');
        assertNotNull(mapSplitter);
    }
    
}