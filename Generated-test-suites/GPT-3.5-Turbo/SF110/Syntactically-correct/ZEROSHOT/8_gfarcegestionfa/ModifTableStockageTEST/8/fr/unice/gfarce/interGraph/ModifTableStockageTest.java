import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        // Test case 1
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.ajouterColonne("New Column", String.class);
        assertEquals(6, result.getColumnCount());
        assertEquals(4, result.getRowCount());
        
        // Test case 2
        ts = new TableStockage(2, 4);
        mts = new ModifTableStockage(ts);
        result = mts.ajouterColonne("Column 1", Integer.class);
        assertEquals(5, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
    
    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.ajouterLigne();
        assertEquals(5, result.getColumnCount());
        assertEquals(4, result.getRowCount());
    }
    
    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.supprimerColonne(2);
        assertEquals(4, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
    
    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.supprimerLigne(1);
        assertEquals(5, result.getColumnCount());
        assertEquals(2, result.getRowCount());
    }
    
    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.separerColonne(2);
        assertEquals(6, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
    
    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.fusionnerColonnes(2);
        assertEquals(4, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
    
    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.colonneMail();
        assertEquals(6, result.getColumnCount());
        assertEquals(4, result.getRowCount());
    }
    
    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage result = mts.selectMail();
        assertEquals(5, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
}