import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    
    private RecordType recordType;
    
    @Before
    public void setUp() {
        recordType = new RecordType(new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>(), true);
    }
    
    @Test
    public void testIsSynthetic() {
        boolean isSynthetic = recordType.isSynthetic();
        assertFalse(isSynthetic);
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>(), true);
        boolean tolerateUnknowns = true;
        boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, tolerateUnknowns);
        assertTrue(result);
    }
    
    @Test
    public void testDefineProperty() {
        String propertyName = "property";
        JSType type = new JSType();
        boolean inferred = true;
        Node propertyNode = new Node();
        boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
        assertFalse(result);
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = new JSType();
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        assertNotNull(greatestSubtype);
    }
    
    @Test
    public void testIsSubtype() {
        JSType that = new JSType();
        boolean isSubtype = recordType.isSubtype(that);
        assertFalse(isSubtype);
    }
    
    @Test
    public void testResolveInternal() {
        ErrorReporter t = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<JSType>();
        JSType resolvedType = recordType.resolveInternal(t, scope);
        assertNotNull(resolvedType);
    }

}