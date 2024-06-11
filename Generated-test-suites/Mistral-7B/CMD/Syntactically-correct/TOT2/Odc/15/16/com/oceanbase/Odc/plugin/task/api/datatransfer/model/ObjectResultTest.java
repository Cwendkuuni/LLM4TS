import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    // Test methods and their basic functionality
    @Test
    public void testEmptyObjectResult() {
        ObjectResult objResult = new ObjectResult();
        // ...
    }

    @Test
    public void testObjectResultWithValues() {
        ObjectStatus that = new ObjectStatus("schema1", "table1", "type1", new AtomicLong(1), new AtomicLong(10), ObjectStatus.Status.SUCCESS);
        ObjectResult objResult = ObjectResult.of(that);
        // ...
    }

    @Test
    public void testGetSummary() {
        ObjectStatus that = new ObjectStatus("schema1", "table1", "type1", new AtomicLong(1), new AtomicLong(10), ObjectStatus.Status.SUCCESS);
        ObjectResult objResult = ObjectResult.of(that);
        // ...
    }

    // Test edge cases and exception handling scenarios
    @Test(expected = NullPointerException.class)
    public void testNullObjectStatus() {
        ObjectResult objResult = ObjectResult.of(null);
    }

    @Test
    public void testEmptyExportPaths() {
        ObjectStatus that = new ObjectStatus("schema1", "table1", "type1", new AtomicLong(1), new AtomicLong(10), ObjectStatus.Status.SUCCESS);
        ObjectResult objResult = ObjectResult.of(that);
        objResult.setExportPaths(null);
        // ...
    }

    @Test
    public void testLongStrings() {
        String schema = "schemaWithVeryLongNameThatExceedsMaxLength";
        String name = "tableWithVeryLongNameThatExceedsMaxLength";
        String type = "typeWithVeryLongNameThatExceedsMaxLength";
        ObjectStatus that = new ObjectStatus(schema, name, type, new AtomicLong(1), new AtomicLong(10), ObjectStatus.Status.SUCCESS);
        ObjectResult objResult = ObjectResult.of(that);
        // ...
    }
}