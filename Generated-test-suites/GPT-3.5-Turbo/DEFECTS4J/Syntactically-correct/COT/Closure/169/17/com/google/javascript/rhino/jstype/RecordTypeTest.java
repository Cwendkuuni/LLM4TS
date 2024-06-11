import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;

public class RecordTypeTest {

  @Test
  public void testRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
  }

}