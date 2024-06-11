import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testDefaultConstructor() {
        ReferenceType referenceType = new ReferenceType(null, null);
        assertNotNull(referenceType);
    }
}