import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    // Test the NamedType constructor
    @Test
    public void testNamedTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        String reference = "testReference";
        String sourceName = "testSource";
        int lineno = 1;
        int charno = 2;
        
        NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
        
        assertEquals(registry, namedType.getRegistry());
        assertEquals(reference, namedType.getReference());
        assertEquals(sourceName, namedType.getSourceName());
        assertEquals(lineno, namedType.getLineno());
        assertEquals(charno, namedType.getCharno());
    }

    // Test other public methods in the NamedType class

    // ...

}