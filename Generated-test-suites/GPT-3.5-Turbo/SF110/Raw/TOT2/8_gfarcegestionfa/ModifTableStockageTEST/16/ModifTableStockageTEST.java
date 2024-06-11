import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {
    
    @Test
    public void testAjouterColonne() {
        // Test adding a new column to the table
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.ajouterColonne("New Column", String.class);
        assertEquals(3, result.getColumnCount());
    }
    
    @Test
    public void testAjouterLigne() {
        // Test adding a new row to the table
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals(3, result.getRowCount());
    }
    
    @Test
    public void testSupprimerColonne() {
        // Test removing a column from the table
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.supprimerColonne(1);
        assertEquals(1, result.getColumnCount());
    }
    
    @Test
    public void testSupprimerLigne() {
        // Test removing a row from the table
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.supprimerLigne(1);
        assertEquals(1, result.getRowCount());
    }
    
    @Test
    public void testSeparerColonne() {
        // Test splitting a column into two columns
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.separerColonne(0);
        assertEquals(3, result.getColumnCount());
    }
    
    @Test
    public void testFusionnerColonnes() {
        // Test merging two columns into one
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
    }
    
    @Test
    public void testSelectMail() {
        // Test selecting rows to send emails
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
        TableStockage result = modifTableStockage.selectMail();
        assertEquals(2, result.getRowCount());
    }
}