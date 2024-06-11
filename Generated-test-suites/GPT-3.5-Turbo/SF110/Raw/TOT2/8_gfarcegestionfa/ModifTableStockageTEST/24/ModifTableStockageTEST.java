import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {
    
    private ModifTableStockage mt;
    
    @Before
    public void setUp() {
        mt = new ModifTableStockage(new TableStockage(5, 3));
    }
    
    @Test
    public void testAjouterColonne() {
        TableStockage result = mt.ajouterColonne("Test", String.class);
        assertEquals(4, result.getColumnCount());
        assertEquals(6, result.getRowCount());
    }
    
    @Test
    public void testAjouterLigne() {
        TableStockage result = mt.ajouterLigne();
        assertEquals(3, result.getColumnCount());
        assertEquals(6, result.getRowCount());
    }
    
    @Test
    public void testSupprimerColonne() {
        TableStockage result = mt.supprimerColonne(1);
        assertEquals(2, result.getColumnCount());
        assertEquals(5, result.getRowCount());
    }
    
    @Test
    public void testSupprimerLigne() {
        TableStockage result = mt.supprimerLigne(2);
        assertEquals(3, result.getColumnCount());
        assertEquals(4, result.getRowCount());
    }
    
    @Test
    public void testSeparerColonne() {
        TableStockage result = mt.separerColonne(0);
        assertEquals(4, result.getColumnCount());
        assertEquals(5, result.getRowCount());
    }
    
    @Test
    public void testFusionnerColonnes() {
        TableStockage result = mt.fusionnerColonnes(0);
        assertEquals(2, result.getColumnCount());
        assertEquals(5, result.getRowCount());
    }
    
    @Test
    public void testColonneMail() {
        TableStockage result = mt.colonneMail();
        assertEquals(4, result.getColumnCount());
        assertEquals(6, result.getRowCount());
    }
    
    @Test
    public void testSelectMail() {
        TableStockage result = mt.selectMail();
        assertEquals(6, result.getRowCount());
    }
    
}