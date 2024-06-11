import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TableMetaTest {

    @Test
    public void testGetName() {
        // Create TableMeta object
        TableMeta tableMeta = new TableMeta(null);
        
        // Set name using reflection
        java.lang.reflect.Field nameField;
        try {
            nameField = tableMeta.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(tableMeta, "TestName");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        // Call getName() method and assert the result
        assertEquals("TestName", tableMeta.getName());
    }
}