
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void testAddProperty() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        RecordTypeBuilder returnedBuilder = recordTypeBuilder.addProperty("name", type, propertyNode);
        
        assertNotNull(returnedBuilder);
    }

    @Test
    public void testBuild_emptyRecord() {
        JSType expectedType = new JSType();

        JSType actualType = recordTypeBuilder.build();

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testBuild_nonEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType type = new JSType();
        Node propertyNode = new Node();
        
        RecordTypeBuilder returnedBuilder = recordTypeBuilder.addProperty("name", type, propertyNode);
        JSType expectedType = new RecordType(registry, Collections.unmodifiableMap(returnedBuilder.properties));

        JSType actualType = recordTypeBuilder.build();

        assertEquals(expectedType, actualType);
    }
}
