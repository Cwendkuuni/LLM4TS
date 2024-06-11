import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new JavaType(String.class, null);
        resolvedRecursiveType.setReference(javaType);
        JavaType expectedResult = javaType;
        JavaType actualResult = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(expectedResult, actualResult);
    }
}