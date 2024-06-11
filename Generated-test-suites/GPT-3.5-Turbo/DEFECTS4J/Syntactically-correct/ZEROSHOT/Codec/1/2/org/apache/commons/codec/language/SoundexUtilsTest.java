import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String str = null;
        String expectedResult = null;
        String actualResult = SoundexUtils.clean(str);
        assertEquals(expectedResult, actualResult);

        str = "";
        expectedResult = "";
        actualResult = SoundexUtils.clean(str);
        assertEquals(expectedResult, actualResult);

        str = "Test";
        expectedResult = "TEST";
        actualResult = SoundexUtils.clean(str);
        assertEquals(expectedResult, actualResult);

        str = "Test123";
        expectedResult = "TEST";
        actualResult = SoundexUtils.clean(str);
        assertEquals(expectedResult, actualResult);

        str = "#Test#";
        expectedResult = "TEST";
        actualResult = SoundexUtils.clean(str);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = null;
        String s2 = null;
        int expectedResult = 0;
        int actualResult = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedResult, actualResult);

        s1 = "";
        s2 = "";
        expectedResult = 4;
        actualResult = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedResult, actualResult);

        s1 = "Test";
        s2 = "Test";
        expectedResult = 4;
        actualResult = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedResult, actualResult);

        s1 = "Test";
        s2 = "Tst";
        expectedResult = 3;
        actualResult = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedResult, actualResult);

        s1 = "Test1";
        s2 = "Test2";
        expectedResult = 4;
        actualResult = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = null;
        String es2 = null;
        int expectedResult = 0;
        int actualResult = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedResult, actualResult);

        es1 = "";
        es2 = "";
        expectedResult = 0;
        actualResult = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedResult, actualResult);

        es1 = "1234";
        es2 = "1234";
        expectedResult = 4;
        actualResult = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedResult, actualResult);

        es1 = "1234";
        es2 = "4321";
        expectedResult = 1;
        actualResult = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedResult, actualResult);
    }
}