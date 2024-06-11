import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {
    
    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.ajouterColonne("newColumn", Integer.class);
        assertNotNull(result);
        assertEquals(3, result.getColumnCount());
        assertEquals("newColumn", result.getColumnName(2));
        
        // Add more assertions if needed
    }
    
    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.ajouterLigne();
        assertNotNull(result);
        assertEquals(3, result.getRowCount());
        
        // Add more assertions if needed
    }
    
    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.supprimerColonne(1);
        assertNotNull(result);
        assertEquals(1, result.getColumnCount());
        
        // Add more assertions if needed
    }
    
    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.supprimerLigne(1);
        assertNotNull(result);
        assertEquals(1, result.getRowCount());
        
        // Add more assertions if needed
    }
    
    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.separerColonne(1);
        assertNotNull(result);
        assertEquals(3, result.getColumnCount());
        
        // Add more assertions if needed
    }
    
    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.fusionnerColonnes(1);
        assertNotNull(result);
        assertEquals(1, result.getColumnCount());
        
        // Add more assertions if needed
    }
    
    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.colonneMail();
        assertNotNull(result);
        assertEquals(3, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
        
        // Add more assertions if needed
    }
    
    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.selectMail();
        assertNotNull(result);
        
        // Add assertions specific to this method
    }
    
    // Add more test methods for other public methods if needed
    
}