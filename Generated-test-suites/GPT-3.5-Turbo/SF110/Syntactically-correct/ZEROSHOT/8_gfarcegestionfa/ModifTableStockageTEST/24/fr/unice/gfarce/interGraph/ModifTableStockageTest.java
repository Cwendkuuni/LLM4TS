import org.junit.*;
import static org.junit.Assert.*;

public class ModifTableStockageTest {
    
    // Test data
    private TableStockage ts;
    private ModifTableStockage modifTs;
    
    @Before
    public void setUp() {
        ts = new TableStockage(2, 2);
        ts.setValueAt("Value 1", 0, 0);
        ts.setValueAt("Value 2", 0, 1);
        ts.setValueAt("Value 3", 1, 0);
        ts.setValueAt("Value 4", 1, 1);
        
        modifTs = new ModifTableStockage(ts);
    }
    
    @Test
    public void testAjouterColonne() {
        TableStockage expected = new TableStockage(3, 3);
        expected.setValueAt("Value 1", 0, 0);
        expected.setValueAt("Value 2", 0, 1);
        expected.setValueAt("Value 3", 1, 0);
        expected.setValueAt("Value 4", 1, 1);
        expected.setValueAt(null, 0, 2);
        expected.setValueAt(null, 1, 2);
        
        TableStockage result = modifTs.ajouterColonne("New Column", Object.class);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAjouterLigne() {
        TableStockage expected = new TableStockage(3, 2);
        expected.setValueAt("Value 1", 0, 0);
        expected.setValueAt("Value 2", 0, 1);
        expected.setValueAt("Value 3", 1, 0);
        expected.setValueAt("Value 4", 1, 1);
        expected.setValueAt(null, 2, 0);
        expected.setValueAt(null, 2, 1);
        
        TableStockage result = modifTs.ajouterLigne();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSupprimerColonne() {
        TableStockage expected = new TableStockage(2, 1);
        expected.setValueAt("Value 1", 0, 0);
        expected.setValueAt("Value 3", 1, 0);
        
        TableStockage result = modifTs.supprimerColonne(1);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSupprimerLigne() {
        TableStockage expected = new TableStockage(1, 2);
        expected.setValueAt("Value 1", 0, 0);
        expected.setValueAt("Value 2", 0, 1);
        
        TableStockage result = modifTs.supprimerLigne(1);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSeparerColonne() {
        TableStockage expected = new TableStockage(2, 3);
        expected.setValueAt("Value", 0, 0);
        expected.setValueAt("1", 0, 1);
        expected.setValueAt("Value", 0, 2);
        expected.setValueAt("3", 1, 0);
        expected.setValueAt("Value", 1, 1);
        expected.setValueAt("4", 1, 2);
        
        TableStockage result = modifTs.separerColonne(1);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFusionnerColonnes() {
        TableStockage expected = new TableStockage(2, 1);
        expected.setValueAt("Value 1 Value 2", 0, 0);
        expected.setValueAt("Value 3 Value 4", 1, 0);
        
        TableStockage result = modifTs.fusionnerColonnes(0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testColonneMail() {
        TableStockage expected = new TableStockage(2, 3);
        expected.setValueAt("Value 1", 0, 0);
        expected.setValueAt("Value 2", 0, 1);
        expected.setValueAt(true, 0, 2);
        expected.setValueAt("Value 3", 1, 0);
        expected.setValueAt("Value 4", 1, 1);
        expected.setValueAt(true, 1, 2);
        
        TableStockage result = modifTs.colonneMail();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSelectMail() {
        TableStockage expected = new TableStockage(1, 2);
        expected.setValueAt("Value 1", 0, 0);
        expected.setValueAt("Value 2", 0, 1);
        
        TableStockage result = modifTs.selectMail();
        
        assertEquals(expected, result);
    }
}