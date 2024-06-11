import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;

public class NamedTypeTest {
    
    @Test
    public void testConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        String reference = "testReference";
        String sourceName = "testSourceName";
        int lineno = 1;
        int charno = 2;
        
        NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
        
        assertNotNull(namedType);
        assertEquals(reference, namedType.getReferenceName());
        assertEquals(sourceName, namedType.getSourceName());
        assertEquals(lineno, namedType.getLineno());
        assertEquals(charno, namedType.getCharno());
    }
    
    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        String reference = "testReference";
        String sourceName = "testSourceName";
        int lineno = 1;
        int charno = 2;
        
        NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
        
        String propertyName = "testProperty";
        JSType type = registry.getNativeFunctionType(JSTypeNative.NUMBER_TYPE).getInstanceType();;
        boolean inferred = true;
        Node propertyNode = new Node();
        
        boolean isDefined = namedType.defineProperty(propertyName, type, inferred, propertyNode);
        
        assertTrue(isDefined);
        assertEquals(type, namedType.getPropertyType(propertyName));
    }
}