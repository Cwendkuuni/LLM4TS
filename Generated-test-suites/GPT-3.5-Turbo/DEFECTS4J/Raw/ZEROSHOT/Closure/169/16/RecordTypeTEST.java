import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    private RecordType recordType;
    private JSTypeRegistry registry;
    private Map<String, RecordTypeBuilder.RecordProperty> properties;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        // TODO: Implement test
    }

    @Test
    public void testGetImplicitPrototype() {
        // TODO: Implement test
    }

    @Test
    public void testDefineProperty() {
        // TODO: Implement test
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        // TODO: Implement test
    }

    @Test
    public void testIsSubtype() {
        // TODO: Implement test
    }

    @Test
    public void testResolveInternal() {
        // TODO: Implement test
    }
}