import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneConstructor() {
        Caverphone caverphone = new Caverphone();
        // No specific assertion needed for constructor
    }

    @Test
    public void testCaverphoneNullInput() {
        Caverphone caverphone = new Caverphone();
        String output = caverphone.caverphone(null);
        Assert.assertEquals("1111111111", output);
    }

    @Test
    public void testCaverphoneEmptyInput() {
        Caverphone caverphone = new Caverphone();
        String output = caverphone.caverphone("");
        Assert.assertEquals("1111111111", output);
    }

    @Test
    public void testCaverphoneLowercase() {
        Caverphone caverphone = new Caverphone();
        String input = "HelloWorld";
        String output = caverphone.caverphone(input);
        String expected = "h2el2or2ld";
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testCaverphoneRemoveNonAlpha() {
        Caverphone caverphone = new Caverphone();
        String input = "123aBcDef456";
        String output = caverphone.caverphone(input);
        String expected = "AbC";
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        String input = "end";
        String output = caverphone.caverphone(input);
        String expected = "en";
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        String input = "Cough";
        String output = caverphone.caverphone(input);
        String expected = "cou2f";
        Assert.assertEquals(expected, output);

        input = "Rough";
        output = caverphone.caverphone(input);
        expected = "rou2f";
        Assert.assertEquals(expected, output);

        input = "Tough";
        output = caverphone.caverphone(input);
        expected = "tou2f";
        Assert.assertEquals(expected, output);

        input = "Enough";
        output = caverphone.caverphone(input);
        expected = "enou2f";
        Assert.assertEquals(expected, output);

        input = "Trough";
        output = caverphone.caverphone(input);
        expected = "trou2f";
        Assert.assertEquals(expected, output);

        input = "Gn";
        output = caverphone.caverphone(input);
        expected = "2n";
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testCaverphoneReplacements() {
        Caverphone caverphone = new Caverphone();
        String input = "Cue";
        String output = caverphone.caverphone(input);
        String expected = "2q";
        Assert.assertEquals(expected, output);

        input = "Ice";
        output = caverphone.caverphone(input);
        expected = "si";
        Assert.assertEquals(expected, output);

        input = "Cease";
        output = caverphone.caverphone(input);
        expected = "se";
        Assert.assertEquals(expected, output);

        input = "Cypress";
        output = caverphone.caverphone(input);
        expected = "sy";
        Assert.assertEquals(expected, output);

        input = "Tchin";
        output = caverphone.caverphone(input);
        expected = "2ch";
        Assert.assertEquals(expected, output);

        input = "Cat";
        output = caverphone.caverphone(input);
        expected = "k";
        Assert.assertEquals(expected, output);

        input = "Quilt";
        output = caverphone.caverphone(input);
        expected = "k";
        Assert.assertEquals(expected, output);

        input = "Exchange";
        output = caverphone.caverphone(input);
        expected = "x";
        Assert.assertEquals(expected, output);

        input = "Victory";
        output = caverphone.caverphone(input);
        expected = "f";
        Assert.assertEquals(expected, output);

        input = "Dog";
        output = caverphone.caverphone(input);
        expected = "g";
        Assert.assertEquals(expected, output);

        input = "Phone";
        output = caverphone.caverphone(input);
        expected = "fh";
        Assert.assertEquals(expected, output);

        input = "Bubble";
        output = caverphone.caverphone(input);
        expected = "p";
        Assert.assertEquals(expected, output);

        input = "Shirt";
        output = caverphone.caverphone(input);
        expected = "s2";
        Assert.assertEquals(expected, output);

        input = "Zebra";
        output = caverphone.caverphone(input);
        expected = "s";
        Assert.assertEquals(expected, output);

        input = "Why";
        output = caverphone.caverphone(input);
        expected = "A";
        Assert.assertEquals(expected, output);

        input = "Why3";
        output = caverphone.caverphone(input);
        expected = "W3";
        Assert.assertEquals(expected, output);

        input = "Why";
        output = caverphone.caverphone(input);
        expected = "A";
        Assert.assertEquals(expected, output);

        input = "Why";
        output = caverphone.caverphone(input);
        expected = "3";
        Assert.assertEquals(expected, output);

        input = "Y3gh3";
        output = caverphone.caverphone(input);
        expected = "3kh3";
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        String input = "Two2Three2Four5Six";
        String output = caverphone.caverphone(input);
        String expected = "";
        Assert.assertEquals(expected, output);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneInvalidInput() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }
}