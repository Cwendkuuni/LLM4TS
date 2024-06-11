import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.LP);
        JSType returnType = new JSType(new JSTypeRegistry());
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        assertNotNull(arrowType);
        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }

    @Test
    public void testArrowTypeSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.LP);
        JSType returnType1 = new JSType(new JSTypeRegistry());
        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

        Node parameters2 = new Node(Token.LP);
        JSType returnType2 = new JSType(new JSTypeRegistry());
        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

        assertTrue(arrowType2.isSubtype(arrowType1));
    }

    // Add more test cases here for other methods and edge cases

}