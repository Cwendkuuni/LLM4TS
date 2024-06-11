import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.constructCollectionType(ArrayType.class, String.class);

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }
}