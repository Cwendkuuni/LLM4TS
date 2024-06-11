import static org.junit.Assert.*;

import org.junit.Test;


public class ArrowTypeTest {

    @Test
    public void testArrowTypeConstructor1() {
        Node parameters = new Node("x");
        JSType returnType = new JSType("x");
        ArrowType arrowType = new ArrowType(parameters, returnType);
        assertNotNull(arrowType);
    }

    @Test
    public void testArrowTypeConstructor2() {
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(parameters, returnType, true);
        assertNotNull(arrowType);
    }

    @Test
    public void testIsSubtype() {
        Node parameters1 = new Node("x");
        JSType returnType1 = new JSType("x");
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);

        Node parameters2 = new Node("y");
        JSType returnType2 = new JSType("y");
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsSubtype_notSubtype() {
        Node parameters1 = new Node("x");
        JSType returnType1 = new JSType("x");
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);

        Node parameters2 = new Node("y");
        JSType returnType2 = null;
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters() {
        Node parameters1 = new Node("x");
        JSType returnType1 = new JSType("x");
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);

        Node parameters2 = new Node("x");
        JSType returnType2 = new JSType("x");
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testHasEqualParameters_notEqual() {
        Node parameters1 = new Node("x");
        JSType returnType1 = new JSType("x");
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);

        Node parameters2 = new Node("y");
        JSType returnType2 = new JSType("y");
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        assertFalse(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        Node parameters1 = new Node("x");
        JSType returnType1 = new JSType("x");
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);

        Node parameters2 = new Node("x");
        JSType returnType2 = new JSType("x");
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testIsEquivalentTo_notEquivalent() {
        Node parameters1 = new Node("x");
        JSType returnType1 = new JSType("x");
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);

        Node parameters2 = new Node("y");
        JSType returnType2 = new JSType("y");
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        assertFalse(arrowType1.isEquivalentTo(arrowType2));
    }
    
    @Test
    public void testHashCode() {
        Node parameters1 = new Node("x");
        JSType returnType1 = new JSType("x");
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);

        Node parameters2 = new Node("x");
        JSType returnType2 = new JSType("x");
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
    }

    @Test
    public void testHashCode_notEqual() {
        Node parameters1 = new Node("x");
        JSType returnType1 = new JSType("x");
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);

        Node parameters2 = new Node("y");
        JSType returnType2 = new JSType("y");
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
    }

    @Test
    public void testHasUnknownParamsOrReturn_withUnknownParamsAndReturn() {
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(parameters, returnType);
        assertTrue(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testHasUnknownParamsOrReturn_withUnknownParamsAndReturn_nonUnknownParams() {
        Node parameters = new Node("x");
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(parameters, returnType);
        assertTrue(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testHasUnknownParamsOrReturn_withUnknownParamsAndReturn_nonUnknownReturn() {
        Node parameters = null;
        JSType returnType = new JSType("x");
        ArrowType arrowType = new ArrowType(parameters, returnType);
        assertTrue(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testHasUnknownParamsOrReturn_withUnknownParamsAndReturn_noUnknownParamsOrReturn() {
        Node parameters = new Node("x");
        JSType returnType = new JSType("x");
        ArrowType arrowType = new ArrowType(parameters, returnType);
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

}