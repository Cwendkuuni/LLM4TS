import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {
    
    @Test
    public void testAjouterColonne() {
        // Test adding a new column with String type
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.ajouterColonne("New Column", String.class);
        assertEquals(result.getColumnCount(), 1);
        assertEquals(result.getRowCount(), 1);
    }
    
    @Test
    public void testAjouterLigne() {
        // Test adding a new row
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.ajouterLigne();
        assertNotNull(result);
    }
    
    @Test
    public void testSupprimerColonne() {
        // Test deleting a column
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.supprimerColonne(0);
        assertNotNull(result);
    }

    @Test
    public void testSupprimerLigne() {
        // Test deleting a row
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.supprimerLigne(0);
        assertNotNull(result);
    }
    
    @Test
    public void testSeparerColonne() {
        // Test separating a column
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.separerColonne(0);
        assertNotNull(result);
    }
    
    @Test
    public void testFusionnerColonnes() {
        // Test merging two columns
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        assertNotNull(result);
    }
    
    @Test
    public void testColonneMail() {
        // Test adding a new column for email
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.colonneMail();
        assertNotNull(result);
    }
    
    @Test
    public void testSelectMail() {
        // Test selecting rows for sending email
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.selectMail();
        assertNotNull(result);
    }
}