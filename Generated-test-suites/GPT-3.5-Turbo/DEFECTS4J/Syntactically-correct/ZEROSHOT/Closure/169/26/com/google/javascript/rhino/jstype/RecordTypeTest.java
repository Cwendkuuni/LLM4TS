import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        Map<String, RecordProperty> properties = Maps.newTreeMap();
        properties.put("property1", new RecordProperty("TYPE_1", null));
        properties.put("property2", new RecordProperty("TYPE_2", null));
        recordType = new RecordType(new JSTypeRegistry(), properties);
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(new JSTypeRegistry(), properties, true);
        boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, true);
        assertTrue(result);
    }

    @Test
    public void testCheckRecordEquivalenceHelperWithDifferentProperties() {
        Map<String, RecordProperty> properties = Maps.newTreeMap();
        properties.put("property1", new RecordProperty("TYPE_1", null));
        properties.put("property3", new RecordProperty("TYPE_3", null));
        RecordType otherRecord = new RecordType(new JSTypeRegistry(), properties, true);
        boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, true);
        assertFalse(result);
    }

    @Test
    public void testCheckRecordEquivalenceHelperWithDifferentTypes() {
        Map<String, RecordProperty> properties = Maps.newTreeMap();
        properties.put("property1", new RecordProperty("TYPE_1", null));
        properties.put("property2", new RecordProperty("TYPE_3", null));
        RecordType otherRecord = new RecordType(new JSTypeRegistry(), properties, true);
        boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, true);
        assertFalse(result);
    }

    @Test
    public void testDefineProperty() {
        boolean result = recordType.defineProperty("property3", new JSType(), false, null);
        assertTrue(result);
    }

    @Test
    public void testDefinePropertyWithInferredFalse() {
        boolean result = recordType.defineProperty("property3", new JSType(), true, null);
        assertTrue(result);
    }

    @Test
    public void testDefinePropertyWhenFrozen() {
        Map<String, RecordProperty> properties = Maps.newTreeMap();
        properties.put("property1", new RecordProperty("TYPE_1", null));
        properties.put("property2", new RecordProperty("TYPE_2", null));
        RecordType frozenRecord = new RecordType(new JSTypeRegistry(), properties, true);
        boolean result = frozenRecord.defineProperty("property3", new JSType(), false, null);
        assertFalse(result);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = new JSType();
        JSType result = recordType.getGreatestSubtypeHelper(that);
        assertNotNull(result);
    }

    @Test
    public void testGetGreatestSubtypeHelperWhenThatIsRecordType() {
        Map<String, RecordProperty> properties = Maps.newTreeMap();
        properties.put("property1", new RecordProperty("TYPE_1", null));
        properties.put("property2", new RecordProperty("TYPE_2", null));
        RecordType thatRecord = new RecordType(new JSTypeRegistry(), properties, true);
        JSType result = recordType.getGreatestSubtypeHelper(thatRecord);
        assertNotNull(result);
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType result = recordType.getImplicitPrototype();
        assertNotNull(result);
    }

    @Test
    public void testIsSynthetic() {
        boolean result = recordType.isSynthetic();
        assertFalse(result);
    }

    @Test
    public void testIsSubtype() {
        JSType that = new JSType();
        boolean result = recordType.isSubtype(that);
        assertFalse(result);
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter t = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        JSType result = recordType.resolveInternal(t, scope);
        assertNotNull(result);
    }
}