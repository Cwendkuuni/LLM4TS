import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        // Test adding a column with String type
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.ajouterColonne("NewColumn", String.class);
        assertEquals(3, result.getColumnCount());
        assertEquals("NewColumn", result.getColumnName(2));
    }

    @Test
    public void testAjouterLigne() {
        // Test adding a row to an empty table
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals(3, result.getRowCount());
    }

    @Test
    public void testSupprimerColonne() {
        // Test removing a column
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.supprimerColonne(1);
        assertEquals(1, result.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        // Test removing a row
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.supprimerLigne(1);
        assertEquals(1, result.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        // Test separating a column
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.separerColonne(1);
        assertEquals(4, result.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        // Test merging two columns
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        assertEquals(1, result.getColumnCount());
    }

    @Test
    public void testColonneMail() {
        // Test adding a column for email
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.colonneMail();
        assertEquals(3, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
    }

    @Test
    public void testSelectMail() {
        // Test selecting rows for email
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.selectMail();
        assertEquals(2, result.getRowCount());
    }
}