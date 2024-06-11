import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    private SoundexUtils soundexUtils;

    @Before
    public void setUp() {
        soundexUtils = new SoundexUtils();
    }

    @Test
    public void testClean_strIsNull_returnNull() {
        String str = null;
        String cleanStr = soundexUtils.clean(str);
        assertNull(cleanStr);
    }

    @Test
    public void testClean_strIsEmpty_returnEmptyString() {
        String str = "";
        String cleanStr = soundexUtils.clean(str);
        assertEquals("", cleanStr);
    }

    @Test
    public void testClean_strHasUpperCaseAlphabets_returnUpperCaseString() {
        String str = "This is a Test";
        String expectedCleanStr = "THISISATEST";
        String cleanStr = soundexUtils.clean(str);
        assertEquals(expectedCleanStr, cleanStr);
    }

    @Test
    public void testClean_strHasLowerCaseAlphabets_returnUpperCaseString() {
        String str = "this is a test";
        String expectedCleanStr = "THISISATEST";
        String cleanStr = soundexUtils.clean(str);
        assertEquals(expectedCleanStr, cleanStr);
    }

    @Test
    public void testDifference_encoderIsNull_throwEncoderException() {
        StringEncoder encoder = null;
        String s1 = "String1";
        String s2 = "String2";
        assertThrows(EncoderException.class, () -> soundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifference_s1IsNull_throwEncoderException() {
        StringEncoder encoder = new SomeEncoder();
        String s1 = null;
        String s2 = "String2";
        assertThrows(EncoderException.class, () -> soundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifference_s2IsNull_throwEncoderException() {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "String1";
        String s2 = null;
        assertThrows(EncoderException.class, () -> soundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifference_bothStringsAreSame_returnStringLength() {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "String";
        String s2 = "String";
        int expectedDifference = s1.length();
        int difference = soundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedDifference, difference);
    }

    @Test
    public void testDifference_bothStringsAreDifferent_returnDifference() {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "String1";
        String s2 = "String2";
        int expectedDifference = 0;
        int difference = soundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedDifference, difference);
    }

    @Test
    public void testDifferenceEncoded_es1IsNull_returnZero() {
        String es1 = null;
        String es2 = "EncodedString";
        int expectedDifference = 0;
        int difference = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedDifference, difference);
    }

    @Test
    public void testDifferenceEncoded_es2IsNull_returnZero() {
        String es1 = "EncodedString";
        String es2 = null;
        int expectedDifference = 0;
        int difference = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedDifference, difference);
    }

    @Test
    public void testDifferenceEncoded_stringsWithDifferentLength_returnDifference() {
        String es1 = "ABCD";
        String es2 = "ABCDE";
        int expectedDifference = 4;
        int difference = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedDifference, difference);
    }

    @Test
    public void testDifferenceEncoded_stringsWithSameLength_returnDifference() {
        String es1 = "ABCD";
        String es2 = "XBCE";
        int expectedDifference = 2;
        int difference = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedDifference, difference);
    }
}