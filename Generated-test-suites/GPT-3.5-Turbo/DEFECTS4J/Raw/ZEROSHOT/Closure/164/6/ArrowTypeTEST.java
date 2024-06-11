import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
    
    private ArrowType arrowType;
    
    @Before
    public void setUp() {
        arrowType = new ArrowType(null, null, null);
    }
    
    @Test
    public void testIsSubtype() {
        JSType other1 = new ArrowType(null, null, null);
        JSType other2 = new JSType(null);
        
        assertTrue(arrowType.isSubtype(other1));
        assertFalse(arrowType.isSubtype(other2));
    }
    
    @Test
    public void testHasEqualParameters() {
        ArrowType that = new ArrowType(null, null, null);
        
        assertTrue(arrowType.hasEqualParameters(that));
    }
    
    @Test
    public void testIsEquivalentTo() {
        JSType object1 = new ArrowType(null, null, null);
        JSType object2 = new JSType(null);
        
        assertTrue(arrowType.isEquivalentTo(object1));
        assertFalse(arrowType.isEquivalentTo(object2));
    }
    
    @Test
    public void testHashCode() {
        assertNotNull(arrowType.hashCode());
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSType that = new JSType(null);
        
        arrowType.getLeastSupertype(that);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSType that = new JSType(null);
        
        arrowType.getGreatestSubtype(that);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSType that = new JSType(null);
        
        arrowType.testForEquality(that);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testVisit() {
        Visitor<T> visitor = new Visitor<T>() {
            public T visit() {
                return null;
            }
        };
        
        arrowType.visit(visitor);
    }
    
    @Test
    public void testGetPossibleToBooleanOutcomes() {
        assertNotNull(arrowType.getPossibleToBooleanOutcomes());
    }
    
    @Test
    public void testResolveInternal() {
        ErrorReporter t = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<JSType>();
        
        assertNotNull(arrowType.resolveInternal(t, scope));
    }
    
    @Test
    public void testHasUnknownParamsOrReturn() {
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }
    
    @Test
    public void testToStringHelper() {
        assertFalse(arrowType.toStringHelper(true));
        assertFalse(arrowType.toStringHelper(false));
    }
}