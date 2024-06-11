import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstructor() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null);
        assertNotNull(referenceType);
    }
}