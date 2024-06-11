import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        // Test adding a new column to the table
        ModifTableStockage modif = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modif.ajouterColonne("Test", String.class);
        assertEquals(4, result.getColumnCount());
    }

    @Test
    public void testAjouterLigne() {
        // Test adding a new row to the table
        ModifTableStockage modif = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modif.ajouterLigne();
        assertEquals(4, result.getRowCount());
    }

    @Test
    public void testSupprimerColonne() {
        // Test removing a column from the table
        ModifTableStockage modif = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modif.supprimerColonne(1);
        assertEquals(2, result.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        // Test removing a row from the table
        ModifTableStockage modif = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modif.supprimerLigne(1);
        assertEquals(2, result.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        // Test separating a column into two columns
        ModifTableStockage modif = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modif.separerColonne(1);
        assertEquals(4, result.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        // Test merging two columns into one column
        ModifTableStockage modif = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modif.fusionnerColonnes(1);
        assertEquals(2, result.getColumnCount());
    }

    @Test
    public void testColonneMail() {
        // Test adding a column for sending emails
        ModifTableStockage modif = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modif.colonneMail();
        assertEquals(4, result.getColumnCount());
    }

    @Test
    public void testSelectMail() {
        // Test selecting rows for sending emails
        ModifTableStockage modif = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modif.selectMail();
        assertEquals(3, result.getRowCount());
    }
}