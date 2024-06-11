import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("TestString");
        assert(result.equals("TSTN"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        Object result = metaphone.encode("TestString");
        assert(result.equals("TSTN"));
    }

    @Test
    public void testEncodeObject() {
        Metaphone metaphone = new Metaphone();
        Object result = metaphone.encode((Object) "TestString");
        assert(result.equals("TSTN"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("TestString1", "TestString2");
        assert(result == true);
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        int result = metaphone.getMaxCodeLen();
        assert(result == 4);
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        int result = metaphone.getMaxCodeLen();
        assert(result == 5);
    }

}