import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    @Test
    public void testArrowType_Constructor() {
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(parameters, returnType);
        
        assertNotNull(arrowType);
        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
    }

    @Test
    public void testArrowType_Subtype() {
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType1 = new ArrowType(parameters, returnType);
        ArrowType arrowType2 = new ArrowType(parameters, returnType);

        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testArrowType_EqualParameters() {
        Node parameters1 = new Node();
        Node parameters2 = new Node();
        parameters1.addChildToFront(new Node("param1"));
        parameters2.addChildToFront(new Node("param1"));
        
        JSType returnType = new JSType();
        ArrowType arrowType1 = new ArrowType(parameters1, returnType);
        ArrowType arrowType2 = new ArrowType(parameters2, returnType);

        assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }

    // Add more test cases for other methods as needed

}