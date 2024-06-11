import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {
    
    @Test
    public void testArrowTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.NAME, new Node(Token.EMPTY), new Node(Token.EMPTY));
        JSType returnType = registry.createType(Token.NAME);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertNotNull(arrowType);
    }
    
    @Test
    public void testArrowTypeIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.NAME, new Node(Token.EMPTY));
        JSType returnType1 = registry.createType(Token.NAME);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
        
        Node parameters2 = new Node(Token.NAME, new Node(Token.EMPTY), new Node(Token.EMPTY));
        JSType returnType2 = registry.createType(Token.NUMBER);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
        
        assertTrue(arrowType2.isSubtype(arrowType1));
    }
    
    @Test
    public void testArrowTypeHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.NAME, new Node(Token.EMPTY));
        JSType returnType = registry.createType(Token.NAME);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        assertNotNull(arrowType.hashCode());
    }
    
    @Test
    public void testArrowTypeResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.NAME, new Node(Token.EMPTY));
        JSType returnType = registry.createType(Token.NAME);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        arrowType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>());
        
        assertNotNull(arrowType);
    }
    
}