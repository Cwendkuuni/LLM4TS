import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.PARAM_LIST);
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        assertNotNull(arrowType);
        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertEquals(false, arrowType.returnTypeInferred);
    }

    @Test
    public void testArrowTypeSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, new Node(Token.PARAM_LIST), registry.getNativeType(JSTypeNative.UNKNOWN_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, new Node(Token.PARAM_LIST), registry.getNativeType(JSTypeNative.UNKNOWN_TYPE));
        
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    // Add more test cases for other public methods as needed

}