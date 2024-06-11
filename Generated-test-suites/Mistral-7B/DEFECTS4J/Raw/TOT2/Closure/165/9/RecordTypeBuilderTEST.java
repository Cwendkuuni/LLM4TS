import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class RecordTypeBuilderTest {
  @Test
  public void testRecordTypeBuilderConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry(); // Assuming JSTypeRegistry is instantiated properly
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    assertNotNull(recordTypeBuilder);
  }
}