import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class SplitterTest {
    
    private Splitter splitter;
    
    @Before
    public void setup() {
        splitter = new Splitter(new Strategy() {

            @Override
            public SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
                return null;
            }
        });
    }
    
    @Test
    public void testOnChar() {
        splitter = Splitter.on(' ');
        assertNotNull(splitter);
    }
    
    @Test
    public void testOnCharMatcher() {
        splitter = Splitter.on(CharMatcher.DIGIT);
        assertNotNull(splitter);
    }
    
    @Test
    public void testOnString() {
        splitter = Splitter.on(",");
        assertNotNull(splitter);
    }
    
    @Test(expected = NullPointerException.class)
    public void testOnNullString() {
        splitter = Splitter.on(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnEmptyString() {
        splitter = Splitter.on("");
    }
    
    @Test
    public void testOnPattern() {
        splitter = Splitter.onPattern("\\s+");
        assertNotNull(splitter);
    }
    
    @Test(expected = NullPointerException.class)
    public void testOnNullPattern() {
        splitter = Splitter.onPattern(null);
    }
    
    @Test
    public void testFixedLength() {
        splitter = Splitter.fixedLength(3);
        assertNotNull(splitter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFixedLengthNegativeLength() {
        splitter = Splitter.fixedLength(-1);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        splitter = splitter.omitEmptyStrings();
        assertNotNull(splitter);
    }
    
    @Test
    public void testLimit() {
        splitter = splitter.limit(5);
        assertNotNull(splitter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLimitZero() {
        splitter = splitter.limit(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLimitNegative() {
        splitter = splitter.limit(-1);
    }
    
    @Test
    public void testTrimResults() {
        splitter = splitter.trimResults();
        assertNotNull(splitter);
    }
    
    @Test(expected = NullPointerException.class)
    public void testTrimResultsNull() {
        splitter = splitter.trimResults(null);
    }
    
    @Test
    public void testSplitCharSequence() {
        Iterable<String> result = splitter.split("hello,world");
        assertNotNull(result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSplitNullCharSequence() {
        splitter.split(null);
    }
    
    @Test
    public void testSplitToList() {
        List<String> result = splitter.splitToList("hello,world");
        assertNotNull(result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSplitToListNullCharSequence() {
        splitter.splitToList(null);
    }
    
}