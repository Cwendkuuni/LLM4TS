import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.PlainCharSequence;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testConstructorWithNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
        Assert.assertNull(translator.lookupMap);
    }

    @Test
    public void testConstructorWithEmptyLookupTable() {
        CharSequence[][] lookup = new CharSequence[0][];
        LookupTranslator translator = new LookupTranslator(lookup);
        Assert.assertTrue(translator.lookupMap.isEmpty());
    }

    @Test
    public void testTranslateWithSmallLookupTable() throws IOException {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "hello world";
        Writer out = new StringWriter();
        translator.translate(new PlainCharSequence(input), 0, out);
        Assert.assertEquals("hi earth", out.toString());
    }

    @Test
    public void testTranslateWithLargeLookupTable() throws IOException {
        CharSequence[][] lookup = new CharSequence[10][];
        for (int i = 0; i < 10; i++) {
            lookup[i] = new PlainCharSequence("key" + i);
            lookup[i][1] = new PlainCharSequence("value" + i);
        }
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "this is a test with a very long key sequence, key1 key2 key3 key4 key5 key6 key7 key8 key9 key10";
        Writer out = new StringWriter();
        translator.translate(new PlainCharSequence(input), 0, out);
        String expected = "value1 value2 value3 value4 value5 value6 value7 value8 value9 value10";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void testTranslateWithEmptyInputSequence() throws IOException {
        CharSequence[][] lookup = {"key1", "value1"};
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        translator.translate(new PlainCharSequence(""), 0, out);
        Assert.assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithEmptyLookupMap() throws IOException {
        LookupTranslator translator = new LookupTranslator(null);
        String input = "test";
        Writer out = new StringWriter();
        translator.translate(new PlainCharSequence(input), 0, out);
        Assert.assertEquals("", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullInputSequence() throws IOException {
        CharSequence[][] lookup = {"key1", "value1"};
        LookupTranslator translator = new LookupTranslator(lookup);
        translator.translate(null, 0, new StringWriter());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithNegativeIndex() throws IOException {
        CharSequence[][] lookup = {"key1", "value1"};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "test";
        Writer out = new StringWriter();
        translator.translate(new PlainCharSequence(input), -1, out);
    }

    @Test(expected = IOException.class)
    public void testTranslateWithNullWriter() throws IOException, InterruptedException {
        CharSequence[][] lookup = {"key1", "value1"};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "test";
        translator.translate(new PlainCharSequence(input), 0, null);
    }

    @Test
    public void testTranslateWithInputSequenceLargerThanLongestSequenceInLookupTable() throws IOException {
        CharSequence[][] lookup = {"key1", "value1"};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";
        Writer out = new StringWriter();
        translator.translate(new PlainCharSequence(input), 0, out);
        Assert.assertEquals("value1", out.toString());
    }
}