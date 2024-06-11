
import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {
    
    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.ajouterColonne("newColumn", String.class);
        
        assertEquals(4, result.getColumnCount());
        assertEquals(3, result.getRowCount());
        assertEquals("newColumn", result.getColumnName(3));
    }
    
    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.ajouterLigne();
        
        assertEquals(3, result.getColumnCount());
        assertEquals(4, result.getRowCount());
    }
    
    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.supprimerColonne(1);
        
        assertEquals(2, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
    
    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.supprimerLigne(1);
        
        assertEquals(3, result.getColumnCount());
        assertEquals(2, result.getRowCount());
    }
    
    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.separerColonne(1);
        
        assertEquals(4, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
    
    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.fusionnerColonnes(1);
        
        assertEquals(2, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
    
    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.colonneMail();
        
        assertEquals(4, result.getColumnCount());
        assertEquals(3, result.getRowCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(3));
        assertEquals(true, result.getValueAt(0, 3));
        assertEquals(true, result.getValueAt(1, 3));
        assertEquals(true, result.getValueAt(2, 3));
    }
    
    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        
        TableStockage result = mts.selectMail();
        
        assertEquals(3, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
}

