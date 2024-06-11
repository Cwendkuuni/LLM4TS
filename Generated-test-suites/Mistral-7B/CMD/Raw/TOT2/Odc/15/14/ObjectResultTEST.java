import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    @Test
    public void testGetSummary() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        ObjectResult objectResult = new ObjectResult(schema, name, type);

        String expectedSummary = String.format("%s.%s[%s]", schema, name, type);
        String actualSummary = objectResult.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }
}