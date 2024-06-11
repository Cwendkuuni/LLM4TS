import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordType() {
    // Create a sample RecordType
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("b", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType = new RecordType(properties);

    // Test the getPropertyType method
    Assert.assertEquals(JSTypeNative.STRING_TYPE, recordType.getPropertyType("a"));
    Assert.assertEquals(JSTypeNative.NUMBER_TYPE, recordType.getPropertyType("b"));

    // Test the hasProperty method
    Assert.assertTrue(recordType.hasProperty("a"));
    Assert.assertTrue(recordType.hasProperty("b"));
    Assert.assertFalse(recordType.hasProperty("c"));

    // Test the isSubtype method
    Assert.assertTrue(recordType.isSubtype(recordType));

    // Test the defineProperty method
    JSType newType = new JSType("newType");
    recordType.defineProperty("c", newType, false, null);
    Assert.assertEquals(newType, recordType.getPropertyType("c"));
    Assert.assertTrue(recordType.hasProperty("c"));
    
    // Test the resolveInternal method
    Assert.assertEquals(recordType, recordType.resolveInternal(null, null));
  }
}