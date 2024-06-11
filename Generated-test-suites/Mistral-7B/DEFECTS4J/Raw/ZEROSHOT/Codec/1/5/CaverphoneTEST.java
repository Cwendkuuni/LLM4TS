import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testNullInput() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode(null);
        Assert.assertEquals("1111111111", encoded);
    }

    @Test
    public void testEmptyInput() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("");
        Assert.assertEquals("1111111111", encoded);
    }

    @Test
    public void testLowerCase() {
        Caverphone caverphone = new Caverphone();
        String input = "hello";
        String encoded = caverphone.encode(input);
        String decoded = "";
        try {
            decoded = caverphone.decode(encoded);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testRemoveNonAlphabet() {
        Caverphone caverphone = new Caverphone();
        String input = "123hello45";
        String encoded = caverphone.encode(input);
        String decoded = "";
        try {
            decoded = caverphone.decode(encoded);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("hello", decoded);
    }

    @Test
    public void testRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        String input = "helloWorld";
        String encoded = caverphone.encode(input);
        String decoded = "";
        try {
            decoded = caverphone.decode(encoded);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("helloWorld", decoded);
    }

    @Test
    public void testHandlesStartOptions() {
        Caverphone caverphone = new Caverphone();
        String input = "CoughPotato";
        String encoded = caverphone.encode(input);
        String decoded = "";
        try {
            decoded = caverphone.decode(encoded);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("cou2fpotato", decoded);
    }

    @Test
    public void testHandlesReplacements() {
        Caverphone caverphone = new Caverphone();
        String input = "CaqueCiCeCyTchCat";
        String encoded = caverphone.encode(input);
        String decoded = "";
        try {
            decoded = caverphone.decode(encoded);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("caqsiqcytchcat", decoded);
    }

    @Test
    public void testHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        String input = "123Caqsiqcytchcat23";
        String encoded = caverphone.encode(input);
        String decoded = "";
        try {
            decoded = caverphone.decode(encoded);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("caqsiqcytchcat", decoded);
    }

    @Test
    public void testHandlesAdditionalModifications() {
        Caverphone caverphone = new Caverphone();
        String input = "WhyRyWhyRyWhyWhy";
        String encoded = caverphone.encode(input);
        String decoded = "";
        try {
            decoded = caverphone.decode(encoded);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("whyrywhyrywhy", decoded);
    }

    @Test(expected = EncoderException.class)
    public void testNonStringInput() {
        Caverphone caverphone = new Caverphone();
        Integer input = 123;
        caverphone.encode(input);
    }
}