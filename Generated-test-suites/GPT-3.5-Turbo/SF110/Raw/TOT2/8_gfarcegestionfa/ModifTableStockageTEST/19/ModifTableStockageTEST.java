import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        // Test adding a new column with String type
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage resultTable = modifTable.ajouterColonne("NewColumn", String.class);
        assertEquals(4, resultTable.getColumnCount());
        assertEquals("NewColumn", resultTable.getColumnName(3));
    }

    @Test
    public void testAjouterLigne() {
        // Test adding a new row
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage resultTable = modifTable.ajouterLigne();
        assertEquals(4, resultTable.getRowCount());
    }

    @Test
    public void testSupprimerColonne() {
        // Test removing a column at index 0
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage resultTable = modifTable.supprimerColonne(0);
        assertEquals(2, resultTable.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        // Test removing a row at index 1
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage resultTable = modifTable.supprimerLigne(1);
        assertEquals(2, resultTable.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        // Test separating a column at index 1
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage resultTable = modifTable.separerColonne(1);
        assertEquals(4, resultTable.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        // Test merging columns at index 0 and index 1
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage resultTable = modifTable.fusionnerColonnes(0);
        assertEquals(2, resultTable.getColumnCount());
    }

    @Test
    public void testColonneMail() {
        // Test adding a new column for email with Boolean type
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage resultTable = modifTable.colonneMail();
        assertEquals(4, resultTable.getColumnCount());
        assertEquals(Boolean.class, resultTable.getColumnClass(3));
    }

    @Test
    public void testSelectMail() {
        // Test selecting rows for sending email
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 4));
        TableStockage resultTable = modifTable.selectMail();
        assertEquals(2, resultTable.getRowCount());
    }

}