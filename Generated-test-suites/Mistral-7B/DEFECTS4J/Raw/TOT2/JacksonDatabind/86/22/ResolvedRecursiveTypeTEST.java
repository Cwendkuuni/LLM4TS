import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new JavaxAnnotationType(); // assuming JavaxAnnotationType is a valid JavaType

        resolvedRecursiveType.setReference(ref);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();

        assertNotNull(selfReferencedType);
        assertEquals(ref.getClass(), selfReferencedType.getClass());
        assertEquals(ref, selfReferencedType);
    }
}