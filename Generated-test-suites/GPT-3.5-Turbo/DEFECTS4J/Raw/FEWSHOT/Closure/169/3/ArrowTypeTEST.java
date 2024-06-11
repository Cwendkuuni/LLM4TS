import static org.junit.Assert.assertEquals;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.TernaryValue;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ArrowTypeTest {

    @Mock
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(registry, null,
                new JSType(registry), true);
        ArrowType arrowType2 = new ArrowType(registry, null,
                new JSType(registry), true);

        assertEquals(true, arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(registry, null,
                new JSType(registry), true);
        ArrowType arrowType2 = new ArrowType(registry, null,
                new JSType(registry), true);

        assertEquals(true, arrowType1.hasEqualParameters(arrowType2, true));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType arrowType1 = new ArrowType(registry, null,
                new JSType(registry), true);
        ArrowType arrowType2 = new ArrowType(registry, null,
                new JSType(registry), true);

        assertEquals(true, arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(registry, null,
                new JSType(registry), true);

        assertEquals(arrowType.hashCode(), arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType arrowType = new ArrowType(registry, null,
                new JSType(registry), true);

        arrowType.getLeastSupertype(Mockito.any(JSType.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType arrowType = new ArrowType(registry, null,
                new JSType(registry), true);

        arrowType.getGreatestSubtype(Mockito.any(JSType.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType arrowType = new ArrowType(registry, null,
                new JSType(registry), true);

        arrowType.testForEquality(Mockito.any(JSType.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType arrowType = new ArrowType(registry, null,
                new JSType(registry), true);

        arrowType.visit(Mockito.any());
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(registry, null,
                new JSType(registry), true);

        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(registry, null,
                new JSType(registry), true);

        ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
        StaticScope<JSType> staticScope = Mockito.mock(StaticScope.class);
        arrowType.resolveInternal(errorReporter, staticScope);

        Mockito.verify(staticScope, Mockito.times(1))
                .resolve(Mockito.any(ErrorReporter.class), Mockito.any(StaticScope.class));
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(registry, null,
                new JSType(registry), true);

        assertEquals(false, arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(registry, null,
                new JSType(registry), true);

        assertEquals("[ArrowType]", arrowType.toStringHelper(false));
    }

    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType arrowType = new ArrowType(registry, null,
                new JSType(registry), true);

        assertEquals(false, arrowType.hasAnyTemplateInternal());
    }
}