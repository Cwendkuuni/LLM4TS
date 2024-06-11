
import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VerifierTest {

    @Test
    public void testConstructorWithValidValue() {
        String value = "ValidValue";
        Verifier verifier = new Verifier(value);
        
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        String value = null;
        Verifier verifier = new Verifier(value);
    }

    @Test
    public void testGetValue() {
        String value = "123";
        Verifier verifier = new Verifier(value);
        
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testGetValueWithEmptyValue() {
        String value = "";
        Verifier verifier = new Verifier(value);

        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testGetValueWithMaxLengthValue() {
        String value = "ThisIsALongValueOfStringToTestTheMaxLength";
        Verifier verifier = new Verifier(value);

        assertEquals(value, verifier.getValue());
    }

}
