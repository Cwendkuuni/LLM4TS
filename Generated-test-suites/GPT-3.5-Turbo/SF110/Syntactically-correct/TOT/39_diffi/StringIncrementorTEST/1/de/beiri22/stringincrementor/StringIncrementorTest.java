import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringIncrementorTest {

    private StringIncrementor stringIncrementor;

    @Before
    public void setUp() {
        stringIncrementor = new StringIncrementor();
    }

    @Test
    public void testDiff_default_verboseFalse() {
        String a = "abc";
        String b = "def";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("def");
        RelativeString result = stringIncrementor.diff(a, b);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiff_default_verboseTrue() {
        String a = "abc";
        String b = "def";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("def");
        RelativeString result = stringIncrementor.diff(a, b, true);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiff_edgeCase_aEmpty() {
        String a = "";
        String b = "def";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("def");
        RelativeString result = stringIncrementor.diff(a, b);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiff_edgeCase_bEmpty() {
        String a = "abc";
        String b = "";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("");
        RelativeString result = stringIncrementor.diff(a, b);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiff_errorCase_aNull() {
        String a = null;
        String b = "def";
        RelativeString result = stringIncrementor.diff(a, b);
        Assert.assertNull(result);
    }

    @Test
    public void testDiff_errorCase_bNull() {
        String a = "abc";
        String b = null;
        RelativeString result = stringIncrementor.diff(a, b);
        Assert.assertNull(result);
    }

    @Test
    public void testPatch_default_verboseFalse() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.setAbsolute("def");
        String expected = "abcdef";
        String result = stringIncrementor.patch(a, r);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatch_default_verboseTrue() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.setAbsolute("def");
        String expected = "abcdef";
        String result = stringIncrementor.patch(a, r, true);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatch_edgeCase_emptyPatch() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.setAbsolute("");
        String expected = "abc";
        String result = stringIncrementor.patch(a, r);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatch_errorCase_aNull() {
        String a = null;
        RelativeString r = new RelativeString();
        r.setAbsolute("def");
        String result = stringIncrementor.patch(a, r);
        Assert.assertNull(result);
    }

    @Test
    public void testPatch_errorCase_rNull() {
        String a = "abc";
        RelativeString r = null;
        String result = stringIncrementor.patch(a, r);
        Assert.assertNull(result);
    }
}