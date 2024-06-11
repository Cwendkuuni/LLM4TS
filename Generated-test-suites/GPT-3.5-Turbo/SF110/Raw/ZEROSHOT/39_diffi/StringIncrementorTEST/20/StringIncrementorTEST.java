import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: When both strings are empty
        assertEquals(new RelativeString(), StringIncrementor.diff("", ""));

        // Test case 2: When strings have the same characters
        assertEquals(new RelativeString(), StringIncrementor.diff("abcde", "abcde"));

        // Test case 3: When strings have completely different characters
        assertEquals(new RelativeString(), StringIncrementor.diff("abcde", "fghij"));

        // Test case 4: When strings have partially different characters
        RelativeString expectedResult4 = new RelativeString();
        expectedResult4.addLink(new StringLink(0, 5, 3));
        assertEquals(expectedResult4, StringIncrementor.diff("abcdefghi", "xyzdefuvw"));

        // Test case 5: When first string is shorter than the second string
        RelativeString expectedResult5 = new RelativeString();
        expectedResult5.addLink(new StringLink(0, 0, 3));
        expectedResult5.setAbsolute("xyz");
        assertEquals(expectedResult5, StringIncrementor.diff("abc", "xyzabcd"));

        // Test case 6: When second string is shorter than the first string
        RelativeString expectedResult6 = new RelativeString();
        expectedResult6.addLink(new StringLink(0, 0, 3));
        expectedResult6.setAbsolute("def");
        assertEquals(expectedResult6, StringIncrementor.diff("defghi", "def"));

        // Test case 7: When both strings have the same characters but different lengths
        RelativeString expectedResult7 = new RelativeString();
        expectedResult7.setAbsolute("jk");
        assertEquals(expectedResult7, StringIncrementor.diff("ijklmno", "jk"));

        // Test case 8: When both strings have the same characters but different lengths (with verbosity)
        RelativeString expectedResult8 = new RelativeString();
        expectedResult8.setAbsolute("hi");
        assertEquals(expectedResult8, StringIncrementor.diff("abcdefghi", "hijkl"));

    }

    @Test
    public void testPatch() {
        // Test case 1: When relative string is empty
        assertEquals("", StringIncrementor.patch("abcde", new RelativeString()));

        // Test case 2: When relative string has one link
        RelativeString relativeString2 = new RelativeString();
        relativeString2.addLink(new StringLink(2, 1, 3));
        assertEquals("abcfde", StringIncrementor.patch("abcdef", relativeString2));

        // Test case 3: When relative string has multiple links
        RelativeString relativeString3 = new RelativeString();
        relativeString3.addLink(new StringLink(0, 0, 2));
        relativeString3.addLink(new StringLink(4, 7, 3));
        relativeString3.setAbsolute("xyz");
        assertEquals("xyabctudefghxyz", StringIncrementor.patch("abcdefgh", relativeString3));

        // Test case 4: When relative string has multiple links and verbosity enabled
        RelativeString relativeString4 = new RelativeString();
        relativeString4.addLink(new StringLink(0, 0, 2));
        relativeString4.addLink(new StringLink(4, 7, 3));
        relativeString4.setAbsolute("xyz");
        assertEquals("xyabctudefghxyz", StringIncrementor.patch("abcdefgh", relativeString4, true));

        // Test case 5: When relative string has no links and absolute string is empty
        RelativeString relativeString5 = new RelativeString();
        relativeString5.setAbsolute("");
        assertEquals("abcdefgh", StringIncrementor.patch("abcdefgh", relativeString5));
    }

}