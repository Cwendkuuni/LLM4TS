import org.junit.Test;
import static org.junit.Assert.*;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        TableMeta table = new TableMeta();
        table.setName("Table1");
        assertEquals("Table1", table.getName());
    }
}