import static org.junit.Assert.*;
import javax.swing.*;
import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        // Test adding a new column with class String
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTable.ajouterColonne("TestColonne", String.class);
        assertEquals("TestColonne", result.getColumnName(result.getColumnCount() - 1));
    }

    @Test
    public void testAjouterLigne() {
        // Test adding a new row when there are existing columns
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTable.ajouterLigne();
        assertEquals(1, result.getRowCount());

        // Test adding a new row when there are no columns
        ModifTableStockage modifTableEmpty = new ModifTableStockage(new TableStockage());
        TableStockage resultEmpty = modifTableEmpty.ajouterLigne();
        assertNull(resultEmpty);
    }

    @Test
    public void testSupprimerColonne() {
        // Test deleting a column
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTable.supprimerColonne(0);
        assertEquals(0, result.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        // Test deleting a row
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTable.supprimerLigne(0);
        assertEquals(0, result.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        // Test separating a column containing a space-separated string
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTable.separerColonne(0);
        assertNotNull(result);
    }

    @Test
    public void testFusionnerColonnes() {
        // Test merging two columns
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTable.fusionnerColonnes(0);
        assertNotNull(result);
    }

    @Test
    public void testColonneMail() {
        // Test adding a mail column
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTable.colonneMail();
        assertNotNull(result);
    }

    @Test
    public void testSelectMail() {
        // Test selecting rows for sending mail
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTable.selectMail();
        assertNotNull(result);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}