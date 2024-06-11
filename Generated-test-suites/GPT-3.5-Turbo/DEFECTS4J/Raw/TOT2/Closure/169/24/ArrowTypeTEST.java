import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.PARAM_LIST);
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        assertNotNull(arrowType);
        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }

    // Add more test cases for other public methods

}