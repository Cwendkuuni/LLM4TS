import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeI;

import org.junit.Test;

import java.util.Map;

public class RecordTypeTest {

    private static final JSTypeRegistry registry = new JSTypeRegistry();

    // Test case for constructor
    @Test
    public void testRecordTypeConstructor() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = null;
        boolean declared = true;
        try {
            RecordType recordType = new RecordType(registry, properties, declared);
            fail("Expected exception");
        } catch (Exception e) {
            // Success
        }
    }

    // Test case for defineProperty
    @Test
    public void testRecordTypeDefineProperty() {
        RecordType recordType = new RecordType(registry, null);
        String propertyName = "property";
        JSType type = registry.getNativeType(TypeI.UNKNOWN_TYPE);
        boolean inferred = true;
        Node propertyNode = new Node();
        boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
        assertTrue(result);
    }

    // Test case for getGreatestSubtypeHelper
    @Test
    public void testRecordTypeGetGreatestSubtypeHelper() {
        RecordType recordType = new RecordType(registry, null);
        JSType type = registry.getNativeType(TypeI.UNKNOWN_TYPE);
        boolean inferred = true;
        Node propertyNode = new Node();
        recordType.defineProperty("property", type, inferred, propertyNode);

        RecordType that = new RecordType(registry, null);
        that.defineProperty("property", type, inferred, propertyNode);

        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        assertNotNull(greatestSubtype);
    }

    // Test case for checkRecordEquivalenceHelper
    @Test
    public void testRecordTypeCheckRecordEquivalenceHelper() {
        RecordType recordType = new RecordType(registry, null);
        JSType type = registry.getNativeType(TypeI.UNKNOWN_TYPE);
        boolean inferred = true;
        Node propertyNode = new Node();
        recordType.defineProperty("property", type, inferred, propertyNode);

        RecordType otherRecord = new RecordType(registry, null);
        otherRecord.defineProperty("property", type, inferred, propertyNode);

        boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, true);
        assertTrue(result);
    }

    // Test case for isSubtype
    @Test
    public void testRecordTypeIsSubtype() {
        RecordType recordType = new RecordType(registry, null);
        JSType type = registry.getNativeType(TypeI.UNKNOWN_TYPE);
        boolean inferred = true;
        Node propertyNode = new Node();
        recordType.defineProperty("property", type, inferred, propertyNode);

        RecordType that = new RecordType(registry, null);
        that.defineProperty("property", type, inferred, propertyNode);

        boolean result = recordType.isSubtype(that);
        assertTrue(result);
    }

    // Test case for resolveInternal
    @Test
    public void testRecordTypeResolveInternal() {
        RecordType recordType = new RecordType(registry, null);
        JSType type = registry.getNativeType(TypeI.UNKNOWN_TYPE);
        boolean inferred = true;
        Node propertyNode = new Node();
        recordType.defineProperty("property", type, inferred, propertyNode);

        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<JSType>();
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertNotNull(resolvedType);
    }
}