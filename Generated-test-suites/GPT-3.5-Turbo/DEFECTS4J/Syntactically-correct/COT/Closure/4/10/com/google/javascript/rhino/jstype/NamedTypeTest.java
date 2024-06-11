import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        String reference = "TestClass";
        String sourceName = "test.js";
        int lineno = 1;
        int charno = 1;

        NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

        assertEquals(registry, namedType.getRegistry());
        assertEquals(reference, namedType.getReference());
        assertEquals(sourceName, namedType.getSourceName());
        assertEquals(lineno, namedType.getLineno());
        assertEquals(charno, namedType.getCharno());
    }
}