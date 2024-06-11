import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: Typical use case
        RelativeString result1 = StringIncrementor.diff("abcde", "abfgde");
        assertEquals(2, result1.linksCount());
        assertEquals("fg", result1.getAbsolute());
        assertEquals(0, result1.getLink(0).getPosOrig());
        assertEquals(2, result1.getLink(0).getPosNew());
        assertEquals(0, result1.getLink(0).getLen());
        assertEquals(4, result1.getLink(1).getPosOrig());
        assertEquals(5, result1.getLink(1).getPosNew());
        assertEquals(1, result1.getLink(1).getLen());

        // Test case 2: Edge case
        RelativeString result2 = StringIncrementor.diff("abcde", "abcde");
        assertEquals(0, result2.linksCount());
        assertEquals("", result2.getAbsolute());

        // Test case 3: Error scenario
        RelativeString result3 = StringIncrementor.diff("abcde", "ABCdef");
        assertEquals(0, result3.linksCount());
        assertEquals("ABCdef", result3.getAbsolute());
    }

    @Test
    public void testPatch() {
        // Test case 1: Typical use case
        String result1 = StringIncrementor.patch("abcde", new RelativeString());
        assertEquals("abcde", result1);

        // Test case 2: Edge case
        RelativeString relativeString2 = new RelativeString();
        relativeString2.addLink(new StringLink(0, 0, 0));
        relativeString2.addLink(new StringLink(4, 5, 1));
        relativeString2.setAbsolute("fg");
        String result2 = StringIncrementor.patch("abcde", relativeString2);
        assertEquals("abfgde", result2);

        // Test case 3: Error scenario
        RelativeString relativeString3 = new RelativeString();
        relativeString3.setAbsolute("ABCdef");
        String result3 = StringIncrementor.patch("abcde", relativeString3);
        assertEquals("abcde", result3);
    }
}