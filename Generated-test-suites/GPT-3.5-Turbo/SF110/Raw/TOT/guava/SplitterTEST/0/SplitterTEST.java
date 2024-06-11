import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    Splitter splitter;
    
    @Before
    public void setup(){
        splitter = new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter splitter, final CharSequence toSplit) {
                return new SplittingIterator(splitter, toSplit) {
                    @Override
                    int separatorStart(final int start) {
                        return 0;
                    }
                    
                    @Override
                    int separatorEnd(final int separatorPosition) {
                        return 1;
                    }
                };
            }
        });
    }
    
    @Test
    public void testOnCharSeparator_NormalCase(){
        Splitter result = Splitter.on(':');
        assertNotNull(result);
        assertEquals(':'.getClass(), result.getClass());
    }
    
    @Test
    public void testOnCharSeparator_NullSeparator(){
        try{
            Splitter result = Splitter.on((Character)null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e){
            assertEquals("java.util.Objects.requireNonNull", e.getStackTrace()[0].getClassName());
        }
    }
    
    @Test
    public void testOnCharMatcherSeparator_NormalCase(){
        Splitter result = Splitter.on(CharMatcher.is(':'));
        assertNotNull(result);
        assertEquals(':', result);
    }
    
    @Test
    public void testOnCharMatcherSeparator_NullMatcher(){
        try{
            Splitter result = Splitter.on((CharMatcher)null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e){
            assertEquals("java.util.Objects.requireNonNull", e.getStackTrace()[0].getClassName());
        }
    }
    
    @Test
    public void testOnStringSeparator_NormalCase(){
        String separator = ":";
        Splitter result = Splitter.on(separator);
        assertNotNull(result);
        assertEquals(separator.getClass(), result.getClass());
    }
    
    @Test
    public void testOnStringSeparator_EmptySeparator(){
        try{
            Splitter result = Splitter.on("");
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e){
            assertEquals("The separator may not be the empty string.", e.getMessage());
        }
    }
    
    @Test
    public void testOnPatternSeparator_NormalCase(){
        Pattern separatorPattern = Pattern.compile(":");
        Splitter result = Splitter.on(separatorPattern);
        assertNotNull(result);
        assertEquals(separatorPattern.getClass(), result.getClass());
    }
    
    @Test
    public void testOnPatternSeparator_NullSeparatorPattern(){
        try{
            Splitter result = Splitter.on((Pattern)null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e){
            assertEquals("java.util.Objects.requireNonNull", e.getStackTrace()[0].getClassName());
        }
    }
    
    // similar tests for other methods
    
}