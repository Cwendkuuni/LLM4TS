import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeSubtype() {
        // Test for isSubtype method
        ArrowType arrowType1 = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        ArrowType arrowType2 = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testArrowTypeEqualParameters() {
        // Test for hasEqualParameters method
        ArrowType arrowType1 = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        ArrowType arrowType2 = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    }

    @Test
    public void testArrowTypeCheckArrowEquivalenceHelper() {
        // Test for checkArrowEquivalenceHelper method
        ArrowType arrowType1 = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        ArrowType arrowType2 = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    }

    @Test
    public void testArrowTypeHashCode() {
        // Test for hashCode method
        ArrowType arrowType = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        assertNotNull(arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrowTypeResolveInternal() {
        // Test for resolveInternal method
        ArrowType arrowType = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        ErrorReporter errorReporter = new ErrorReporter();
        arrowType.resolveInternal(errorReporter, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrowTypeGetLeastSupertype() {
        // Test for getLeastSupertype method
        ArrowType arrowType = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        arrowType.getLeastSupertype(JSType.STRING_TYPE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrowTypeGetGreatestSubtype() {
        // Test for getGreatestSubtype method
        ArrowType arrowType = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        arrowType.getGreatestSubtype(JSType.STRING_TYPE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrowTypeTestForEquality() {
        // Test for testForEquality method
        ArrowType arrowType = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        arrowType.testForEquality(JSType.STRING_TYPE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrowTypeVisit() {
        // Test for visit method
        ArrowType arrowType = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        arrowType.visit(null);
    }

    @Test
    public void testArrowTypeToStringHelper() {
        // Test for toStringHelper method
        ArrowType arrowType = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        assertNotNull(arrowType.toStringHelper(false));
    }

    @Test
    public void testArrowTypeHasAnyTemplateInternal() {
        // Test for hasAnyTemplateInternal method
        ArrowType arrowType = new ArrowType(null, new Node(Token.STRING), JSType.STRING_TYPE, false);
        assertFalse(arrowType.hasAnyTemplateInternal());
    }

}