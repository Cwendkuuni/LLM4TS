import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        
        assertNotNull(splitter);
        // Add more assertions as needed
    }
    
    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        
        assertNotNull(splitter);
        // Add more assertions as needed
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(";");
        
        assertNotNull(splitter);
        // Add more assertions as needed
    }
    
    @Test
    public void testOnPatternSeparator() {
        Pattern pattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.on(pattern);
        
        assertNotNull(splitter);
        // Add more assertions as needed
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        
        assertNotNull(splitter);
        // Add more assertions as needed
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        
        assertNotNull(splitter);
        // Add more assertions as needed
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(3);
        
        assertNotNull(splitter);
        // Add more assertions as needed
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        
        assertNotNull(splitter);
        // Add more assertions as needed
    }

    // Add more test methods for other public methods

}