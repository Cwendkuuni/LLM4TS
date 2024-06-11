import org.junit.*;
import static org.junit.Assert.*;

public class ModifTableStockageTest {

    // Test case for ModifTableStockage constructor
    @Test
    public void testConstructor() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        assertEquals(3, mts.nbColonnes);
        assertEquals(3, mts.nbColonnes);
        assertEquals(ts, mts.ts);
    }
    
    // Test case for ajouterColonne method
    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.ajouterColonne("New Column", String.class);
        assertEquals(4, result.getColumnCount());
        assertEquals(3, result.getRowCount());
        assertEquals("New Column", result.getColumnName(3));
        assertEquals(String.class, result.getColumnClass(3));
    }
    
    // Test case for ajouterLigne method
    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.ajouterLigne();
        assertEquals(3, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
    
    // Test case for supprimerColonne method
    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.supprimerColonne(1);
        assertEquals(2, result.getColumnCount());
        assertEquals(2, result.getRowCount());
    }
    
    // Test case for supprimerLigne method
    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.supprimerLigne(1);
        assertEquals(3, result.getColumnCount());
        assertEquals(1, result.getRowCount());
    }
    
    // Test case for separerColonne method
    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.separerColonne(1);
        assertEquals(4, result.getColumnCount());
        assertEquals(2, result.getRowCount());
    }
    
    // Test case for fusionnerColonnes method
    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.fusionnerColonnes(1);
        assertEquals(2, result.getColumnCount());
        assertEquals(2, result.getRowCount());
    }
    
    // Test case for colonneMail method
    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.colonneMail();
        assertEquals(4, result.getColumnCount());
        assertEquals(2, result.getRowCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(3));
        assertEquals(Boolean.class, result.getColumnClass(3));
    }
    
    // Test case for selectMail method
    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.selectMail();
        assertEquals(3, result.getColumnCount());
        assertEquals(2, result.getRowCount());
    }
}