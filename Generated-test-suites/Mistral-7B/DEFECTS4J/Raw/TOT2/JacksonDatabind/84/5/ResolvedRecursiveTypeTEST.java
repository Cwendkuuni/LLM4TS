import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType testObject = new ResolvedRecursiveType(Object.class, null);
        JavaType reference = new SimpleType(String.class);
        testObject.setReference(reference);
        JavaType expectedReference = reference;
        JavaType actualReference = testObject.getSelfReferencedType();
        assertEquals(expectedReference, actualReference);
    }
}