import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testWithContentType() {
        JavaType originalType = new JavaType();
        ReferenceType referenceType = new ReferenceType();
        
        JavaType newContentType = new JavaType();
        ReferenceType updatedType = referenceType.withContentType(newContentType);
        
        assertEquals(newContentType, updatedType.getContentType());
    }
}