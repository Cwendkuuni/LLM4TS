
import de.beiri22.stringincrementor.Relativestring.StringLink;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "hello world";
        String b = "hello earth";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        StringLink link = result.getLink(0);
        
        assertEquals(0, link.getPosOrig());
        assertEquals(0, link.getPosNew());
        assertEquals(5, link.getLen());
    }

    @Test
    public void testDiffVerbose() {
        String a = "hello world";
        String b = "hello earth";
        
        RelativeString result = StringIncrementor.diff(a, b, true);
        
        StringLink link = result.getLink(0);
        
        assertEquals(0, link.getPosOrig());
        assertEquals(0, link.getPosNew());
        assertEquals(5, link.getLen());
    }

    @Test
    public void testPatch() {
        String a = "hello world";
        RelativeString r = new RelativeString();
        StringLink link = new StringLink(0, 0, 5);
        r.addLink(link);
        
        String expectedResult = "hello earth";
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPatchVerbose() {
        String a = "hello world";
        RelativeString r = new RelativeString();
        StringLink link = new StringLink(0, 0, 5);
        r.addLink(link);
        
        String expectedResult = "hello earth";
        
        String result = StringIncrementor.patch(a, r, true);
        
        assertEquals(expectedResult, result);
    }
}

