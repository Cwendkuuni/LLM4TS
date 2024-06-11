
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.*;

public class RecordTypeBuilderTest {
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }
  
    @Test
    public void testAddProperty_noDuplicateProperty() {
        JSType expectedType = new JSType();
        String propertyName = "testProperty";
        Node propertyNode = new Node();
      
        RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, expectedType, propertyNode);
      
        assertNotNull(result);
      
        JSType actualType = result.build();
        assertEquals(expectedType, actualType);
    }
  
    @Test
    public void testAddProperty_duplicateProperty() {
        JSType type = new JSType();
        String propertyName = "testProperty";
        Node propertyNode = new Node();
      
        recordTypeBuilder.addProperty(propertyName, type, propertyNode);

        RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, new JSType(), new Node());

        assertNull(result);
    }
  
    @Test
    public void testBuild_emptyRecord() {
        JSType actualType = recordTypeBuilder.build();
        JSType expectedType = new JSType();
        assertEquals(expectedType, actualType);
    }
  
    @Test
    public void testBuild_nonEmptyRecord() {
        String propertyName = "testProperty";
        JSType expectedType = new JSType();
        Node propertyNode = new Node();
      
        recordTypeBuilder.addProperty(propertyName, expectedType, propertyNode);
      
        JSType actualType = recordTypeBuilder.build();
        assertTrue(actualType instanceof RecordType);
        assertEquals(expectedType, actualType.getPropertyType(propertyName));
    }
}
