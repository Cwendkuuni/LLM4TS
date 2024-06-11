import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder recordTypeBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordTypeBuilder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddPropertyEmptyRecord() {
 JSType result = recordTypeBuilder.build();
 assertNotNull(result);
 }

 @Test
 public void testAddPropertyDuplicateName() {
 String name = "testName";
 recordTypeBuilder.addProperty(name, new JSTypeString(), null);
 assertNull(recordTypeBuilder.addProperty(name, new JSTypeNumber(), null));
 }

 @Test
 public void testAddPropertyDifferentNames() {
 String name1 = "testName1";
 String name2 = "testName2";
 recordTypeBuilder.addProperty(name1, new JSTypeString(), null);
 recordTypeBuilder.addProperty(name2, new JSTypeNumber(), null);
 JSType result = recordTypeBuilder.build();
 RecordType recordType = (RecordType) result;
 RecordProperty property1 = recordType.getMember(name1);
 RecordProperty property2 = recordType.getMember(name2);
 assertNotNull(property1);
 assertNotNull(property2);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPropertyNullRegistry() {
 recordTypeBuilder = new RecordTypeBuilder(null);
 }

 @Test
 public void testBuildEmptyRecord() {
 JSType result = recordTypeBuilder.build();
 RecordType recordType = (RecordType) result;
 assertNotNull(recordType.getClass());
 assertNotNull(recordType.getMembers());
 assertTrue(recordType.getMembers().isEmpty());
 }

 @Test
 public void testBuildRecordWithProperties() {
 String name1 = "testName1";
 String name2 = "testName2";
 recordTypeBuilder.addProperty(name1, new JSTypeString(), null);
 recordTypeBuilder.addProperty(name2, new JSTypeNumber(), null);
 JSType result = recordTypeBuilder.build();
 RecordType recordType = (RecordType) result;
 RecordProperty property1 = recordType.getMember(name1);
 RecordProperty property2 = recordType.getMember(name2);
 assertNotNull(property1);
 assertNotNull(property2);
 }
}