
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", new JSType(), new Node()));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", new JSType(), new Node()));
        properties.put("property3", new RecordTypeBuilder.RecordProperty("property3", new JSType(), new Node()));
        recordType = new RecordType(properties);
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testDefineProperty() {
        assertTrue(recordType.defineProperty("newProperty", new JSType(), true, new Node()));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = new JSType();
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        assertNotNull(greatestSubtype);
    }

    @Test
    public void testIsSubtype() {
        RecordType typeA = new RecordType();
        RecordType typeB = new RecordType();
        assertTrue(RecordType.isSubtype(typeA, typeB));
    }
}

