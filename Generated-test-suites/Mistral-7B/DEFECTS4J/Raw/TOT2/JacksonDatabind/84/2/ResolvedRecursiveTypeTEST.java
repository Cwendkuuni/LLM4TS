import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType reference = new BasicType(Integer.class);
        type.setReference(reference);
        assertSame(reference, type.getSelfReferencedType());
    }
}