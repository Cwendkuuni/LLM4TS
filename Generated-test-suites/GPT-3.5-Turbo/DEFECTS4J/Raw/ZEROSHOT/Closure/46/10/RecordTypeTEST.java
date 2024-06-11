import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        // Initialize properties
        RecordTypeBuilder.RecordProperty property1 =
                new RecordTypeBuilder.RecordProperty("a", JSType.UNKNOWN);
        RecordTypeBuilder.RecordProperty property2 =
                new RecordTypeBuilder.RecordProperty("b", JSType.UNKNOWN);

        // Create record types
        RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
            put("a", property1);
            put("b", property2);
        }});
        RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
            put("a", property1);
            put("b", property2);
        }});
        RecordType recordType3 = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
            put("a", property2);
        }});

        // Test isEquivalentTo
        assertTrue(recordType1.isEquivalentTo(recordType2));
        assertFalse(recordType1.isEquivalentTo(recordType3));
    }

    @Test
    public void testGetLeastSupertype() {
        // Initialize properties
        RecordTypeBuilder.RecordProperty property1 =
                new RecordTypeBuilder.RecordProperty("a", JSType.UNKNOWN);
        RecordTypeBuilder.RecordProperty property2 =
                new RecordTypeBuilder.RecordProperty("b", JSType.UNKNOWN);

        // Create record types
        RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
            put("a", property1);
            put("b", property2);
        }});
        RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
            put("a", property2);
        }});

        // Get least supertype
        JSType leastSupertype = recordType1.getLeastSupertype(recordType2);

        // Check least supertype
        assertTrue(leastSupertype instanceof RecordType);
        RecordType leastSupertypeRecordType = (RecordType) leastSupertype;
        assertEquals("a", leastSupertypeRecordType.getProperties().keySet().stream().findFirst().orElse(null));
        assertEquals(JSType.UNKNOWN, leastSupertypeRecordType.getProperties().values().stream().findFirst().orElse(null));
    }

    @Test
    public void testIsSubtype() {
        // Initialize properties
        RecordTypeBuilder.RecordProperty property1 =
                new RecordTypeBuilder.RecordProperty("a", JSType.UNKNOWN);
        RecordTypeBuilder.RecordProperty property2 =
                new RecordTypeBuilder.RecordProperty("b", JSType.UNKNOWN);

        // Create record types
        RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
            put("a", property1);
            put("b", property2);
        }});
        RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
            put("a", property1);
            put("b", property2);
        }});
        RecordType recordType3 = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
            put("a", property2);
        }});

        // Test isSubtype
        assertTrue(recordType1.isSubtype(recordType2));
        assertFalse(recordType1.isSubtype(recordType3));
    }

    @Test
    public void testResolveInternal() {
        // Initialize properties
        RecordTypeBuilder.RecordProperty property =
                new RecordTypeBuilder.RecordProperty("a", JSType.UNKNOWN);

        // Create record type
        RecordType recordType = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
            put("a", property);
        }});

        // Resolve internal
        JSType resolvedType = recordType.resolveInternal(t, scope);

        // Check resolved type
        assertTrue(resolvedType instanceof RecordType);
        RecordType resolvedRecordType = (RecordType) resolvedType;
        assertEquals("a", resolvedRecordType.getProperties().keySet().stream().findFirst().orElse(null));
        assertEquals(JSType.UNKNOWN, resolvedRecordType.getProperties().values().stream().findFirst().orElse(null));
    }
}