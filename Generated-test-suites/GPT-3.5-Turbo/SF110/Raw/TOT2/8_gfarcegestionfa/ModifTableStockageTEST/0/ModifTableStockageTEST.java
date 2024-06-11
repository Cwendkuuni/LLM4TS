import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {
    
    // Test the constructor of ModifTableStockage
    @Test
    public void testModifTableStockageConstructor() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        assertEquals(5, mts.nbColonnes);
        assertEquals(6, mts.nbLignes);
    }
    
    // Test adding a new column
    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage newTable = mts.ajouterColonne("New Column", Integer.class);
        assertEquals(6, newTable.getColumnCount());
        assertEquals("New Column", newTable.getColumnName(5));
    }
    
    // Test adding a new line
    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage newTable = mts.ajouterLigne();
        assertEquals(6, newTable.getRowCount());
    }
    
    // Test deleting a column
    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage newTable = mts.supprimerColonne(3);
        assertEquals(4, newTable.getColumnCount());
    }
    
    // Test deleting a line
    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage newTable = mts.supprimerLigne(2);
        assertEquals(4, newTable.getRowCount());
    }
    
    // Test separating a column
    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage newTable = mts.separerColonne(3);
        assertEquals(6, newTable.getColumnCount());
    }
    
    // Test merging columns
    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage newTable = mts.fusionnerColonnes(1);
        assertEquals(4, newTable.getColumnCount());
    }
    
    // Test adding a column for emails
    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage newTable = mts.colonneMail();
        assertEquals(6, newTable.getColumnCount());
        assertEquals(Boolean.class, newTable.getColumnClass(5));
    }
    
    // Test selecting emails to send
    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage newTable = mts.selectMail();
        assertEquals(5, newTable.getRowCount());
    }
}