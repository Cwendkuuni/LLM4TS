import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

    @Test
    public void testCaverphoneNull() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();
        String input = "hello";
        String result = caverphone.caverphone(input);
        String expected = "hElLoHeLlOHeLlOHeLl";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneRemoveNonAlphabets() {
        Caverphone caverphone = new Caverphone();
        String input = "123abcdef45ghiJklMNOPQRSTuvwxyz";
        String result = caverphone.caverphone(input);
        String expected = "abcdefghiJklMNOPQRSTuvwxyz";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        String input = "test";
        String result = caverphone.caverphone(input);
        String expected = "test";
        assertEquals(expected, result);

        input = "teste";
        result = caverphone.caverphone(input);
        expected = "test2";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesStartOptions() {
        Caverphone caverphone = new Caverphone();
        String input = "coughenough";
        String result = caverphone.caverphone(input);
        String expected = "cou2fenou2f";
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneInvalidType() {
        Caverphone caverphone = new Caverphone();
        Object obj = new Object();
        caverphone.encode(obj);
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
        Caverphone caverphone = new Caverphone();
        String input = "catchecy";
        String result = caverphone.caverphone(input);
        String expected = "siatcsi";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        String input = "123qwerty23";
        String result = caverphone.caverphone(input);
        String expected = "werty";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleSuffixes() {
        Caverphone caverphone = new Caverphone();
        String input = "troughenough";
        String result = caverphone.caverphone(input);
        String expected = "trou2fenou2f";
        assertEquals(expected, result);

        input = "enoughtroughenough";
        result = caverphone.caverphone(input);
        expected = "enou2ftrou2f";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleReplacements() {
        Caverphone caverphone = new Caverphone();
        String input = "catcio";
        String result = caverphone.caverphone(input);
        String expected = "siao";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleStartOptions() {
        Caverphone caverphone = new Caverphone();
        String input = "toughrough";
        String result = caverphone.caverphone(input);
        String expected = "tou2frou2f";
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneHandlesInvalidStartOption() {
        Caverphone caverphone = new Caverphone();
        String input = "coff";
        caverphone.caverphone(input);
    }

    @Test
    public void testCaverphoneHandlesMultipleCases() {
        Caverphone caverphone = new Caverphone();
        String input = "TestHeLLo";
        String result = caverphone.caverphone(input);
        String expected = "Tes2Hell2";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleDigits() {
        Caverphone caverphone = new Caverphone();
        String input = "12345abc";
        String result = caverphone.caverphone(input);
        String expected = "2345ab";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleVowelsAtStart() {
        Caverphone caverphone = new Caverphone();
        String input = "aeiou";
        String result = caverphone.caverphone(input);
        String expected = "A";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleConsecutiveVowels() {
        Caverphone caverphone = new Caverphone();
        String input = "aeeeioouuu";
        String result = caverphone.caverphone(input);
        String expected = "A3";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleConsecutiveLetters() {
        Caverphone caverphone = new Caverphone();
        String input = "abcd";
        String result = caverphone.caverphone(input);
        String expected = "abcd";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleDigitsAtEnd() {
        Caverphone caverphone = new Caverphone();
        String input = "abc123";
        String result = caverphone.caverphone(input);
        String expected = "abc";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleDigitsAtStart() {
        Caverphone caverphone = new Caverphone();
        String input = "123abc";
        String result = caverphone.caverphone(input);
        String expected = "S";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleDigitsInMiddle() {
        Caverphone caverphone = new Caverphone();
        String input = "ab1cd2ef";
        String result = caverphone.caverphone(input);
        String expected = "S";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleWords() {
        Caverphone caverphone = new Caverphone();
        String input = "hello world";
        String result = caverphone.caverphone(input);
        String expected = "hElLoWorLd";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleWordsWithStartOptions() {
        Caverphone caverphone = new Caverphone();
        String input = "tough enough rough";
        String result = caverphone.caverphone(input);
        String expected = "tou2frou2fenou2f";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesUpperCaseLetters() {
        Caverphone caverphone = new Caverphone();
        String input = "HELLO";
        String result = caverphone.caverphone(input);
        String expected = "H2EL2L2O";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleUpperCaseLetters() {
        Caverphone caverphone = new Caverphone();
        String input = "HELLObcd";
        String result = caverphone.caverphone(input);
        String expected = "H2EL2L2bcd";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleVowels() {
        Caverphone caverphone = new Caverphone();
        String input = "aeiou";
        String result = caverphone.caverphone(input);
        String expected = "A";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleConsecutiveVowelsAtEnd() {
        Caverphone caverphone = new Caverphone();
        String input = "aeiouaeiou";
        String result = caverphone.caverphone(input);
        String expected = "A3A3";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleConsecutiveVowelsInMiddle() {
        Caverphone caverphone = new Caverphone();
        String input = "aeiouaeiou";
        String result = caverphone.caverphone(input);
        String expected = "A3A3";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleConsecutiveDigitsAtEnd() {
        Caverphone caverphone = new Caverphone();
        String input = "1234567890123456";
        String result = caverphone.caverphone(input);
        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleConsecutiveDigitsInMiddle() {
        Caverphone caverphone = new Caverphone();
        String input = "1234567890123456";
        String result = caverphone.caverphone(input);
        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleConsecutiveDigitsAtStart() {
        Caverphone caverphone = new Caverphone();
        String input = "123456abc";
        String result = caverphone.caverphone(input);
        String expected = "P";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleConsecutiveLettersAtStart() {
        Caverphone caverphone = new Caverphone();
        String input = "abcdabc";
        String result = caverphone.caverphone(input);
        String expected = "M";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleConsecutiveLettersInMiddle() {
        Caverphone caverphone = new Caverphone();
        String input = "abcdabc";
        String result = caverphone.caverphone(input);
        String expected = "M";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleConsecutiveLettersAtEnd() {
        Caverphone caverphone = new Caverphone();
        String input = "abcdefghijklmnopqrstuvwxyz";
        String result = caverphone.caverphone(input);
        String expected = "N";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleDigitsAndLettersAtEnd() {
        Caverphone caverphone = new Caverphone();
        String input = "abc123def45";
        String result = caverphone.caverphone(input);
        String expected = "N";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleDigitsAndLettersInMiddle() {
        Caverphone caverphone = new Caverphone();
        String input = "abc123def45";
        String result = caverphone.caverphone(input);
        String expected = "N";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleDigitsAndLettersAtStart() {
        Caverphone caverphone = new Caverphone();
        String input = "123abc45def";
        String result = caverphone.caverphone(input);
        String expected = "K";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesMultipleDigitsAndLettersThroughout() {
        Caverphone caverphone = new Caverphone();
        String input = "123abc45def678ghijklmnopqrstuvwxyz";
        String result = caverphone.caverphone(input);
        String expected = "Y";
        assertEquals(expected, result);
    }
}