import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.PrototypeObjectType;

public class RecordTypeTest {

    private JSTypeRegistry registry;
    private RecordType recordType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new TreeMap<>();
        properties.put("a", new RecordProperty(new JSType(), null));
        properties.put("b", new RecordProperty(new JSType(), null));
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType other = new RecordType(registry, recordType.properties);
        Assert.assertTrue(recordType.isEquivalentTo(other));
    }

    @Test
    public void testGetImplicitPrototype() {
        PrototypeObjectType implicitPrototype = recordType.getImplicitPrototype();
        Assert.assertNotNull(implicitPrototype);
    }

    @Test
    public void testDefineProperty() {
        boolean result = recordType.defineProperty("c", new JSType(), false, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetLeastSupertype() {
        JSType that = new JSType();
        JSType leastSupertype = recordType.getLeastSupertype(that);
        Assert.assertNotNull(leastSupertype);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = new JSType();
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        Assert.assertNotNull(greatestSubtype);
    }

    @Test
    public void testToMaybeRecordType() {
        Assert.assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testIsSubtype() {
        JSType that = new JSType();
        boolean result = recordType.isSubtype(that);
        Assert.assertFalse(result);
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter t = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = recordType.resolveInternal(t, scope);
        Assert.assertNotNull(resolvedType);
    }
}