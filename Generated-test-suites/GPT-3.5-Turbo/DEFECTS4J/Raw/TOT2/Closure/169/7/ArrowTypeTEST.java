import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    ArrowType arrowType;

    @Test
    public void testArrowTypeConstructor() {
        Node parameters = new Node(Token.PARAM_LIST);
        JSType returnType = JSTypeNative.STRING_TYPE;
        arrowType = new ArrowType(parameters, returnType);

        assertNotNull(arrowType);
        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }

    @Test
    public void testIsSubtype_WhenOtherIsArrowType() {
        Node parameters1 = new Node(Token.PARAM_LIST);
        JSType returnType1 = JSTypeNative.NUMBER_TYPE;
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);

        Node parameters2 = new Node(Token.PARAM_LIST);
        JSType returnType2 = JSTypeNative.NUMBER_TYPE;
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    // More test cases for other methods in ArrowType class can be added here

}