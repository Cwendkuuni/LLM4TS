import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;

 @BeforeEach
 public void setup() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testEmptyRecordType() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 JSType recordType = recordTypeBuilder.build();
 Assertions.assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
 }

 @Test
 public void testRecordTypeWithOneProperty() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 JSType numberType = registry.getNumberType();
 Node propertyNode = Node.newLiteral(1);
 RecordProperty recordProperty = recordTypeBuilder.addProperty("prop", numberType, propertyNode)
 .build();
 JSType recordType = recordTypeBuilder.build();
 Assertions.assertNotNull(recordProperty);
 RecordType expectedRecordType = new RecordType(registry, Collections.singletonMap("prop", recordProperty));
 Assertions.assertEquals(expectedRecordType.getClass(), recordType.getClass());
 }

 @Test
 public void testRecordTypeWithDuplicateProperty() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 JSType numberType = registry.getNumberType();
 Node propertyNode = Node.newLiteral(1);
 RecordProperty recordProperty = recordTypeBuilder.addProperty("prop", numberType, propertyNode)
 .build();
 RecordProperty duplicateRecordProperty = recordTypeBuilder.addProperty("prop", numberType, propertyNode)
 .build();
 JSType recordType = recordTypeBuilder.build();
 Assertions.assertNull(duplicateRecordProperty);
 }

 @Test
 public void testRecordTypeWithInvalidPropertyType() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 JSType numberType = registry.getNumberType();
 JSType stringType = registry.getStringType();
 Node propertyNode = Node.newLiteral(1);
 RecordTypeBuilder addPropertyResult = recordTypeBuilder.addProperty("prop", numberType, propertyNode);
 addPropertyResult.addProperty("prop", stringType, propertyNode);
 JSType recordType = addPropertyResult.build();
 // This assertion will fail as RecordType does not support multiple properties with the same name but different types.
 // It is expected that the addProperty method returns null in this case to prevent such situations from occurring.
 // This test checks that the addProperty method behaves correctly and prevents the creation of an invalid record type.
 Assertions.assertNull(addPropertyResult.addProperty("prop", stringType, propertyNode));
 }
}