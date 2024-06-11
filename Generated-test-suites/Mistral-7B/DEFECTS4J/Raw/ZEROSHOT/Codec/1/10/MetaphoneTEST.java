import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        String expected = "";
        String actual = metaphone.metaphone("");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String expected = "A";
        String actual = metaphone.metaphone("A");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneMultipleCharacters() {
        Metaphone metaphone = new Metaphone();
        String expected = "BA";
        String actual = metaphone.metaphone("Bob");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneKN() {
        Metaphone metaphone = new Metaphone();
        String expected = "KN";
        String actual = metaphone.metaphone("Knight");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneAE() {
        Metaphone metaphone = new Metaphone();
        String expected = "A";
        String actual = metaphone.metaphone("Apple");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneWR() {
        Metaphone metaphone = new Metaphone();
        String expected = "W";
        String actual = metaphone.metaphone("Water");
        assertEquals(expected, actual);

        expected = "WR";
        actual = metaphone.metaphone("Wrong");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneX() {
        Metaphone metaphone = new Metaphone();
        String expected = "S";
        String actual = metaphone.metaphone("Jix");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneC() {
        Metaphone metaphone = new Metaphone();
        String expected = "S";
        String actual = metaphone.metaphone("Citizen");
        assertEquals(expected, actual);

        expected = "CK";
        actual = metaphone.metaphone("Cork");
        assertEquals(expected, actual);

        expected = "CX";
        actual = metaphone.metaphone("Cinnamon");
        assertEquals(expected, actual);

        expected = "K";
        actual = metaphone.metaphone("Scotch");
        assertEquals(expected, actual);

        expected = "KT";
        actual = metaphone.metaphone("Skillet");
        assertEquals(expected, actual);

        expected = "KS";
        actual = metaphone.metaphone("Pecan");
        assertEquals(expected, actual);

        expected = "KN";
        actual = metaphone.metaphone("Quilt");
        assertEquals(expected, actual);

        expected = "KN";
        actual = metaphone.metaphone("Canada");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneCIA() {
        Metaphone metaphone = new Metaphone();
        String expected = "X";
        String actual = metaphone.metaphone("Cia");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneDGE() {
        Metaphone metaphone = new Metaphone();
        String expected = "J";
        String actual = metaphone.metaphone("Dge");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneGH() {
        Metaphone metaphone = new Metaphone();
        String expected = "K";
        String actual = metaphone.metaphone("Ghana");
        assertEquals(expected, actual);

        expected = "J";
        actual = metaphone.metaphone("Ghost");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphonePH() {
        Metaphone metaphone = new Metaphone();
        String expected = "F";
        String actual = metaphone.metaphone("Phoneme");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneTIO() {
        Metaphone metaphone = new Metaphone();
        String expected = "X";
        String actual = metaphone.metaphone("Tio");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneTIA() {
        Metaphone metaphone = new Metaphone();
        String expected = "X";
        String actual = metaphone.metaphone("Tia");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneTH() {
        Metaphone metaphone = new Metaphone();
        String expected = "0";
        String actual = metaphone.metaphone("Thy");
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNullString() {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(null);
    }

    @Test
    public void testMetaphoneIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        String str1 = "Bob";
        String str2 = "Bob";
        boolean expected = true;
        boolean actual = metaphone.isMetaphoneEqual(str1, str2);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneIsMetaphoneNotEqual() {
        Metaphone metaphone = new Metaphone();
        String str1 = "Bob";
        String str2 = "Alice";
        boolean expected = false;
        boolean actual = metaphone.isMetaphoneEqual(str1, str2);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        int maxCodeLen = 5;
        metaphone.setMaxCodeLen(maxCodeLen);
        int actual = metaphone.getMaxCodeLen();
        assertEquals(maxCodeLen, actual);
    }

}