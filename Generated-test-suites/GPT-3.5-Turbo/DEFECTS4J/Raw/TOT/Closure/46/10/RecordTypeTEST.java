import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {
    private RecordType recordType;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordType = new RecordType(registry, null);
    }
    
    // Test case for defineDeclaredProperty method
    @Test
    public void testDefineDeclaredProperty() {
        String propertyName = "name";
        JSType type = new JSType();
        Node propertyNode = new Node();
        
        assertTrue(recordType.defineProperty(propertyName, type, false, propertyNode));
        assertEquals(type, recordType.getPropertyType(propertyName));
        assertEquals(true, recordType.isPropertyTypeDeclared(propertyName));
    }
    
    // Test case for getLeastSupertype method
    @Test
    public void testGetLeastSupertype() {
        JSType typeA = new JSType();
        JSType typeB = new JSType();
        recordType.defineProperty("propA", typeA, false, null);
        recordType.defineProperty("propB", typeB, false, null);
        
        RecordType otherRecordType = new RecordType(registry, null);
        otherRecordType.defineProperty("propA", typeA, false, null);
        
        JSType leastSupertype = recordType.getLeastSupertype(otherRecordType);
        assertEquals(recordType, leastSupertype);
        
        otherRecordType.defineProperty("propB", typeB, false, null);
        leastSupertype = recordType.getLeastSupertype(otherRecordType);
        assertEquals(recordType, leastSupertype);
        
        otherRecordType.defineProperty("propC", new JSType(), false, null);
        leastSupertype = recordType.getLeastSupertype(otherRecordType);
        assertNull(leastSupertype);
    }
    
    // Test case for isSubtype method
    @Test
    public void testIsSubtype() {
        JSType typeA = new JSType();
        JSType typeB = new JSType();
        recordType.defineProperty("propA", typeA, false, null);
        recordType.defineProperty("propB", typeB, false, null);
        
        RecordType subType = new RecordType(registry, null);
        subType.defineProperty("propA", typeA, false, null);
        
        assertTrue(recordType.isSubtype(recordType));
        assertTrue(subType.isSubtype(recordType));
        assertFalse(recordType.isSubtype(subType));
    }
    
    // Test case for resolveInternal method
    @Test
    public void testResolveInternal() {
        JSType type = new JSType();
        recordType.defineProperty("prop", type, false, null);
        
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertEquals(type, resolvedType);
    }
}