import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        // typical use case
        ArrowType arrowType1 = new ArrowType();
        arrowType1.returnType = JSTypeNative.NUMBER_TYPE;
        arrowType1.parameters = new Node();

        ArrowType arrowType2 = new ArrowType();
        arrowType2.returnType = JSTypeNative.NUMBER_TYPE;
        arrowType2.parameters = new Node();

        assertTrue(arrowType1.isSubtype(arrowType2));

        // edge case
        arrowType1.returnType = JSTypeNative.STRING_TYPE;
        assertFalse(arrowType1.isSubtype(arrowType2));

        // error scenario
        arrowType2.returnType = null;
        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters() {
        // typical use case
        ArrowType arrowType1 = new ArrowType();
        arrowType1.parameters = new Node();
        arrowType1.parameters.addParameter("param1", JSTypeNative.NUMBER_TYPE, false);

        ArrowType arrowType2 = new ArrowType();
        arrowType2.parameters = new Node();
        arrowType2.parameters.addParameter("param1", JSTypeNative.NUMBER_TYPE, false);

        assertTrue(arrowType1.hasEqualParameters(arrowType2));

        // edge case
        arrowType2.parameters.addParameter("param2", JSTypeNative.STRING_TYPE, false);
        assertFalse(arrowType1.hasEqualParameters(arrowType2));

        // error scenario
        arrowType1.parameters = null;
        assertFalse(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        // typical use case
        ArrowType arrowType1 = new ArrowType();
        arrowType1.returnType = JSTypeNative.NUMBER_TYPE;
        arrowType1.parameters = new Node();
        arrowType1.parameters.addParameter("param", JSTypeNative.STRING_TYPE, false);

        ArrowType arrowType2 = new ArrowType();
        arrowType2.returnType = JSTypeNative.NUMBER_TYPE;
        arrowType2.parameters = new Node();
        arrowType2.parameters.addParameter("param", JSTypeNative.STRING_TYPE, false);

        assertTrue(arrowType1.isEquivalentTo(arrowType2));

        arrowType2.parameters.addParameter("param1", JSTypeNative.NUMBER_TYPE, false);
        assertFalse(arrowType1.isEquivalentTo(arrowType2));
       
        arrowType2.parameters = null;
        assertFalse(arrowType1.isEquivalentTo(arrowType2));

        // error scenario
        arrowType1.parameters = null;
        assertFalse(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testHashCode() {
        // typical use case
        ArrowType arrowType1 = new ArrowType();
        arrowType1.returnType = JSTypeNative.NUMBER_TYPE;
        arrowType1.parameters = new Node();
        arrowType1.parameters.addParameter("param", JSTypeNative.STRING_TYPE, false);

        ArrowType arrowType2 = new ArrowType();
        arrowType2.returnType = JSTypeNative.NUMBER_TYPE;
        arrowType2.parameters = new Node();
        arrowType2.parameters.addParameter("param", JSTypeNative.STRING_TYPE, false);

        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

        // edge case
        arrowType2.parameters.addParameter("param1", JSTypeNative.NUMBER_TYPE, false);
        assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());

        // error scenario
        arrowType1.parameters = null;
        assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
    }
}