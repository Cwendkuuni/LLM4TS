import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff_noVerbose() {
        String a = "abcdefg";
        String b = "bcdefgh";
        RelativeString result = StringIncrementor.diff(a, b);

        assertEquals(2, result.linksCount());
        assertEquals("a", result.getAbsolute());

        StringLink link1 = result.getLink(0);
        assertEquals(0, link1.getPosOrig());
        assertEquals(0, link1.getPosNew());
        assertEquals(1, link1.getLen());

        StringLink link2 = result.getLink(1);
        assertEquals(1, link2.getPosOrig());
        assertEquals(1, link2.getPosNew());
        assertEquals(7, link2.getLen());
    }

    @Test
    public void testDiff_verbose() {
        String a = "abcdefg";
        String b = "bcdefgh";
        RelativeString result = StringIncrementor.diff(a, b, true);

        assertEquals(2, result.linksCount());
        assertEquals("a", result.getAbsolute());

        StringLink link1 = result.getLink(0);
        assertEquals(0, link1.getPosOrig());
        assertEquals(0, link1.getPosNew());
        assertEquals(1, link1.getLen());

        StringLink link2 = result.getLink(1);
        assertEquals(1, link2.getPosOrig());
        assertEquals(1, link2.getPosNew());
        assertEquals(7, link2.getLen());
    }

    @Test
    public void testPatch_noVerbose() {
        String a = "abcdefg";
        StringLink link1 = new StringLink(0, 0, 1);
        StringLink link2 = new StringLink(1, 1, 7);
        RelativeString r = new RelativeString();
        r.addLink(link1);
        r.addLink(link2);
        r.setAbsolute("a");

        String result = StringIncrementor.patch(a, r);

        assertEquals("bcdefgh", result);
    }

    @Test
    public void testPatch_verbose() {
        String a = "abcdefg";
        StringLink link1 = new StringLink(0, 0, 1);
        StringLink link2 = new StringLink(1, 1, 7);
        RelativeString r = new RelativeString();
        r.addLink(link1);
        r.addLink(link2);
        r.setAbsolute("a");

        String result = StringIncrementor.patch(a, r, true);

        assertEquals("bcdefgh", result);
    }
}